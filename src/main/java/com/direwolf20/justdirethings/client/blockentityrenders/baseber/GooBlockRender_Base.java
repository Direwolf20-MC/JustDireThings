package com.direwolf20.justdirethings.client.blockentityrenders.baseber;

import com.direwolf20.justdirethings.common.blockentities.basebe.GooBlockBE_Base;
import com.direwolf20.justdirethings.common.blocks.gooblocks.GooBlock_Base;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;

public class GooBlockRender_Base<T extends GooBlockBE_Base> implements BlockEntityRenderer<T, GooBlockRender_Base.GooBlockRenderState> {

    public static class GooBlockRenderState extends BlockEntityRenderState {
        public boolean alive;
        public int tier;
        public BlockState renderBlockState = net.minecraft.world.level.block.Blocks.AIR.defaultBlockState();
        public final Map<Direction, int[]> progress = new EnumMap<>(Direction.class); // [remainingTicks, maxTicks]
    }

    public GooBlockRender_Base(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public GooBlockRenderState createRenderState() {
        return new GooBlockRenderState();
    }

    @Override
    public void extractRenderState(T blockEntity, GooBlockRenderState state, float partialTicks, Vec3 cameraPosition,
                                    ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        BlockState bs = blockEntity.getBlockState();
        state.renderBlockState = bs;
        state.alive = bs.getValue(GooBlock_Base.ALIVE);
        state.tier = blockEntity.getTier();
        state.progress.clear();
        for (Direction direction : Direction.values()) {
            int remaining = blockEntity.getRemainingTimeFor(direction);
            if (remaining > 0) {
                state.progress.put(direction, new int[] { remaining, blockEntity.getCraftingDuration(direction) });
            }
        }
    }

    @Override
    public void submit(GooBlockRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        // TODO(port, stage-16): reimplement goo-block rendering.
        //
        // 1) Floating item cycle when !state.alive:
        //    Old: picked item from JustDireItemTags.GOO_REVIVE_TIER_{state.tier}, faded in/out with a 3600 ms cycle
        //    using ItemRenderer.getModel + renderModelLists wrapped in DireVertexConsumer(alpha).
        //    26.1 replacement: cache an ItemStackRenderState per cycle item and call
        //    ItemModelResolver.updateForTopItem + ItemStackRenderState.submit. Per-face placement + rotation
        //    is in getOffsetPositionForSide / applyRotationForSide below.
        //
        // 2) Crafting pattern overlay for each Direction in state.progress.keySet():
        //    Old: ModelBlockRenderer.renderModelFaceAO into OurRenderTypes.GooPattern (depth-mask)
        //    then again into OurRenderTypes.RenderBlockBackface (blend-equal) for the real block,
        //    scaled/translated per state.tier, rotated by direction.getRotation().
        //    26.1 replacement: ModelBlockRenderer.tesselateBlock(BlockQuadOutput, ...) driving a
        //    custom BlockQuadOutput→VertexConsumer bridge (RENDER_PORTING.md §5.3) from
        //    submitCustomGeometry(...). AmbientOcclusionFace path is gone; drop AO lighting.
        //    - Pattern block: Registration.GooPatternBlock with GOOSTAGE = tier-1 (previous stage, full alpha)
        //      and GOOSTAGE = tier (current, alpha = percentagePart/percentageDivisor).
        //    - Real block: state.renderBlockState.
    }

    @Override
    public AABB getRenderBoundingBox(T blockEntity) {
        return AABB.encapsulatingFullBlocks(blockEntity.getBlockPos().above(10).north(10).east(10),
                blockEntity.getBlockPos().below(10).south(10).west(10));
    }

    public static Vec3 getOffsetPositionForSide(Direction direction, boolean isBlockItem) {
        double offset = 0.025;
        double nudge = isBlockItem ? 0.10 : 0.05;
        return switch (direction) {
            case UP -> new Vec3(0.5, 1.0 + offset, 0.5 - nudge);
            case DOWN -> new Vec3(0.5, 0.0 - offset, 0.5 + nudge);
            case NORTH -> new Vec3(0.5, 0.5 - nudge, 0.0 - offset);
            case SOUTH -> new Vec3(0.5, 0.5 - nudge, 1.0 + offset);
            case WEST -> new Vec3(0.0 - offset, 0.5 - nudge, 0.5);
            case EAST -> new Vec3(1.0 + offset, 0.5 - nudge, 0.5);
        };
    }

    public static Direction getDirection(Direction facing, Direction renderSide) {
        return switch (renderSide) {
            case UP -> facing;
            case DOWN -> facing.getOpposite();
            case WEST -> facing == Direction.DOWN || facing == Direction.UP ? Direction.WEST : facing.getClockWise();
            case EAST -> facing == Direction.DOWN || facing == Direction.UP ? Direction.EAST : facing.getCounterClockWise();
            case NORTH -> switch (facing) {
                case DOWN -> Direction.SOUTH;
                case UP -> Direction.NORTH;
                default -> Direction.UP;
            };
            case SOUTH -> switch (facing) {
                case DOWN -> Direction.NORTH;
                case UP -> Direction.SOUTH;
                default -> Direction.DOWN;
            };
        };
    }
}
