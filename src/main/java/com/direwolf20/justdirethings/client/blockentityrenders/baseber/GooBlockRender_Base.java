package com.direwolf20.justdirethings.client.blockentityrenders.baseber;

import com.direwolf20.justdirethings.client.renderers.OurRenderTypes;
import com.direwolf20.justdirethings.common.blockentities.basebe.GooBlockBE_Base;
import com.direwolf20.justdirethings.common.blocks.gooblocks.GooBlock_Base;
import com.direwolf20.justdirethings.common.blocks.gooblocks.GooPatternBlock;
import com.direwolf20.justdirethings.setup.Registration;
import com.direwolf20.justdirethings.util.ModTags;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.QuadInstance;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class GooBlockRender_Base<T extends GooBlockBE_Base> implements BlockEntityRenderer<T, GooBlockRender_Base.GooBlockRenderState> {

    private static final long CYCLE_MILLIS = 3600L;
    private static final float FADE_VISIBILITY_THRESHOLD = 0.5f;
    private static final int TOTAL_STAGES = GooPatternBlock.GOOSTAGE.getPossibleValues().size(); // 12
    private static final float PERCENT_DIVISOR = 100F / TOTAL_STAGES;

    private static int currentItemIndex = 0;
    private static long lastChangeTime = 0L;
    private static ItemStack cachedItemStack = ItemStack.EMPTY;

    public static class GooBlockRenderState extends BlockEntityRenderState {
        public boolean alive;
        public int tier;
        public BlockState renderBlockState = net.minecraft.world.level.block.Blocks.AIR.defaultBlockState();
        public final Map<Direction, int[]> progress = new EnumMap<>(Direction.class); // [remainingTicks, maxTicks]
        public boolean showFloatingItem;
        public boolean floatingItemIsBlock;
        public final ItemStackRenderState floatingItem = new ItemStackRenderState();
    }

    private final ItemModelResolver itemModelResolver;

    public GooBlockRender_Base(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
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

        state.showFloatingItem = false;
        if (!state.alive) {
            long currentTime = System.currentTimeMillis();
            long elapsedTime = (currentTime - lastChangeTime) % CYCLE_MILLIS;
            float fadeFactor = (float) (0.5 - 0.5 * Math.cos((2 * Math.PI * elapsedTime) / CYCLE_MILLIS));

            if (elapsedTime < 50 && currentTime - lastChangeTime >= CYCLE_MILLIS) {
                cachedItemStack = nextItemFromTag(state.tier);
                lastChangeTime = currentTime;
            }

            if (!cachedItemStack.isEmpty() && fadeFactor >= FADE_VISIBILITY_THRESHOLD) {
                state.showFloatingItem = true;
                state.floatingItemIsBlock = cachedItemStack.getItem() instanceof BlockItem;
                this.itemModelResolver.updateForTopItem(
                        state.floatingItem, cachedItemStack, ItemDisplayContext.GROUND,
                        blockEntity.getLevel(), null, 0);
            }
        }
    }

    @Override
    public void submit(GooBlockRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        if (state.showFloatingItem) {
            for (Direction direction : Direction.values()) {
                poseStack.pushPose();
                Vec3 itemPos = getOffsetPositionForSide(direction, state.floatingItemIsBlock);
                poseStack.translate(itemPos.x, itemPos.y, itemPos.z);
                applyRotationForSide(poseStack, direction);
                poseStack.scale(0.6f, 0.6f, 0.6f);
                state.floatingItem.submit(poseStack, submitNodeCollector,
                        state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
                poseStack.popPose();
            }
        }

        if (!state.progress.isEmpty()) {
            for (Map.Entry<Direction, int[]> entry : state.progress.entrySet()) {
                Direction direction = entry.getKey();
                int[] p = entry.getValue();
                renderCraftingOverlay(state, poseStack, submitNodeCollector, direction, p[0], p[1]);
            }
        }
    }

    private void renderCraftingOverlay(GooBlockRenderState state, PoseStack poseStack, SubmitNodeCollector collector,
                                       Direction direction, int remainingTicks, int maxTicks) {
        float percentComplete = (1F - (float) remainingTicks / (float) maxTicks) * 100F;
        int tensDigit = (int) (percentComplete / PERCENT_DIVISOR);
        if (tensDigit >= TOTAL_STAGES) tensDigit = TOTAL_STAGES - 1;
        float startOfCurrentStage = tensDigit * PERCENT_DIVISOR;
        float stageFrac = (percentComplete - startOfCurrentStage) / PERCENT_DIVISOR; // 0..1 inside this stage
        float alpha = (tensDigit + stageFrac) / (float) TOTAL_STAGES; // monotonic 0..1 across all stages
        int alphaByte = Math.max(0, Math.min(255, Math.round(alpha * 255F)));
        int packedColor = (alphaByte << 24) | 0x00FFFFFF;

        BlockState patternState = Registration.GooPatternBlock.get().defaultBlockState().setValue(GooPatternBlock.GOOSTAGE, tensDigit);

        poseStack.pushPose();
        // Offset to the direction we're crafting at (render on neighbor block).
        net.minecraft.core.Vec3i normal = direction.getUnitVec3i();
        poseStack.translate(normal.getX(), normal.getY(), normal.getZ());
        // Slightly larger than a normal block to prevent z-fighting, scaled per tier.
        float translateF = (float) state.tier / 2000F;
        poseStack.translate(-translateF, -translateF, -translateF);
        float scaleF = (float) state.tier / 1000F;
        poseStack.scale(1F + scaleF, 1F + scaleF, 1F + scaleF);
        // Rotate so pattern + real block align.
        poseStack.translate(0.5, 0.5, 0.5);
        poseStack.mulPose(direction.getRotation());
        poseStack.translate(-0.5, -0.5, -0.5);

        List<BlockStateModelPart> patternParts = collectModelParts(patternState);
        if (!patternParts.isEmpty()) {
            // Pass 1 (depth-only): stamp the pattern silhouette into the depth buffer.
            collector.submitBlockModel(poseStack, OurRenderTypes.GooPattern, patternParts,
                    new int[]{-1}, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
        }

        List<BlockStateModelPart> realParts = collectModelParts(state.renderBlockState);
        if (!realParts.isEmpty()) {
            // Pass 2 (color): depth-EQUAL test; real block shows only where pattern stamped.
            // QuadInstance carries the alpha via packed ARGB; putBakedQuad multiplies instance color × baked color per vertex.
            collector.submitCustomGeometry(poseStack, OurRenderTypes.RenderBlockBackface,
                    (pose, buffer) -> writeModelQuads(realParts, pose, buffer, packedColor, state.lightCoords));
        }

        poseStack.popPose();
    }

    private static List<BlockStateModelPart> collectModelParts(BlockState state) {
        BlockStateModel model = Minecraft.getInstance().getModelManager().getBlockStateModelSet().get(state);
        List<BlockStateModelPart> parts = new ArrayList<>();
        model.collectParts(RandomSource.create(42L), parts);
        return parts;
    }

    private static void writeModelQuads(List<BlockStateModelPart> parts, PoseStack.Pose pose, VertexConsumer buffer,
                                        int packedColor, int lightCoords) {
        QuadInstance instance = new QuadInstance();
        instance.setColor(packedColor);
        instance.setLightCoords(lightCoords);
        for (BlockStateModelPart part : parts) {
            writeFace(part.getQuads(null), pose, buffer, instance);
            for (Direction d : Direction.values()) {
                writeFace(part.getQuads(d), pose, buffer, instance);
            }
        }
    }

    private static void writeFace(List<BakedQuad> quads, PoseStack.Pose pose, VertexConsumer buffer, QuadInstance instance) {
        for (BakedQuad quad : quads) {
            buffer.putBakedQuad(pose, quad, instance);
        }
    }

    @Override
    public AABB getRenderBoundingBox(T blockEntity) {
        return AABB.encapsulatingFullBlocks(blockEntity.getBlockPos().above(10).north(10).east(10),
                blockEntity.getBlockPos().below(10).south(10).west(10));
    }

    private static ItemStack nextItemFromTag(int tier) {
        TagKey<Item> tag = switch (tier) {
            case 1 -> ModTags.Items.GOO_REVIVE_TIER_1;
            case 2 -> ModTags.Items.GOO_REVIVE_TIER_2;
            case 3 -> ModTags.Items.GOO_REVIVE_TIER_3;
            case 4 -> ModTags.Items.GOO_REVIVE_TIER_4;
            default -> null;
        };
        if (tag == null) return ItemStack.EMPTY;
        HolderSet.Named<Item> holders = BuiltInRegistries.ITEM.get(tag).orElse(null);
        if (holders == null) return ItemStack.EMPTY;
        List<Holder<Item>> items = holders.stream().toList();
        if (items.isEmpty()) return ItemStack.EMPTY;
        ItemStack next = new ItemStack(items.get(currentItemIndex % items.size()).value());
        currentItemIndex = (currentItemIndex + 1) % items.size();
        return next;
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

    private static void applyRotationForSide(PoseStack matrixStackIn, Direction direction) {
        switch (direction) {
            case UP -> matrixStackIn.mulPose(Axis.XP.rotationDegrees(90));
            case DOWN -> matrixStackIn.mulPose(Axis.XN.rotationDegrees(90));
            case NORTH -> matrixStackIn.mulPose(Axis.YP.rotationDegrees(180));
            case SOUTH -> matrixStackIn.mulPose(Axis.YN.rotationDegrees(0));
            case WEST -> matrixStackIn.mulPose(Axis.YP.rotationDegrees(90));
            case EAST -> matrixStackIn.mulPose(Axis.YP.rotationDegrees(-90));
        }
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
