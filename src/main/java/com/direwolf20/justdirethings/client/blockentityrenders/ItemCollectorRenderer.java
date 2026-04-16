package com.direwolf20.justdirethings.client.blockentityrenders;

import com.direwolf20.justdirethings.client.blockentityrenders.baseber.AreaAffectingBER;
import com.direwolf20.justdirethings.common.blockentities.ItemCollectorBE;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import org.joml.Matrix4f;

public class ItemCollectorRenderer extends AreaAffectingBER<ItemCollectorBE, ItemCollectorRenderer.ItemCollectorRenderState> {

    public static class ItemCollectorRenderState extends AreaAffectingRenderState {
        public long gameTime;
        public float partialTicks;
        public Direction facing = Direction.UP;
    }

    public ItemCollectorRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public ItemCollectorRenderState createRenderState() {
        return new ItemCollectorRenderState();
    }

    @Override
    public void extractRenderState(ItemCollectorBE blockEntity, ItemCollectorRenderState state, float partialTicks, Vec3 cameraPosition,
                                    ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.gameTime = blockEntity.getLevel() != null ? blockEntity.getLevel().getGameTime() : 0L;
        state.partialTicks = partialTicks;
        state.facing = blockEntity.getBlockState().getValue(BlockStateProperties.FACING).getOpposite();
    }

    @Override
    public void submit(ItemCollectorRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        super.submit(state, poseStack, submitNodeCollector, camera);
        long gameTime = state.gameTime;
        float partialTicks = state.partialTicks;
        Direction direction = state.facing;
        submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.endPortal(),
                (pose, buffer) -> renderCube(direction, gameTime, partialTicks, pose.pose(), buffer));
    }

    private void renderCube(Direction direction, long gameTime, float partialTicks, Matrix4f matrixStack, VertexConsumer vertexConsumer) {
        float oneSmall = 0.53125f;
        float zeroSmall = 0.46875f;
        float oneBig = 0.5625f;
        float zeroBig = 0.4375f;
        int animationDuration = 80;
        float animationTick = Math.floorMod(gameTime, animationDuration) + partialTicks;
        float lerp = Mth.cos(animationTick / animationDuration * Mth.TWO_PI) * 0.25f + 0.25f;
        float zero = Mth.lerp(lerp, zeroSmall, zeroBig);
        float one = Mth.lerp(lerp, oneSmall, oneBig);
        float diff = one - zero;
        float f;
        switch (direction) {
            case UP:
                f = 0.5f + 0.25f;
                this.renderFace(matrixStack, vertexConsumer, zero, one, f, f + diff, one, one, one, one);
                this.renderFace(matrixStack, vertexConsumer, zero, one, f + diff, f, zero, zero, zero, zero);
                this.renderFace(matrixStack, vertexConsumer, one, one, f + diff, f, zero, one, one, zero);
                this.renderFace(matrixStack, vertexConsumer, zero, zero, f, f + diff, zero, one, one, zero);
                this.renderFace(matrixStack, vertexConsumer, zero, one, f, f, zero, zero, one, one);
                this.renderFace(matrixStack, vertexConsumer, zero, one, f + diff, f + diff, one, one, zero, zero);
                break;
            case DOWN:
                f = 0.5f - 0.25f;
                this.renderFace(matrixStack, vertexConsumer, zero, one, f, f - diff, one, one, one, one);
                this.renderFace(matrixStack, vertexConsumer, zero, one, f - diff, f, zero, zero, zero, zero);
                this.renderFace(matrixStack, vertexConsumer, one, one, f - diff, f, zero, one, one, zero);
                this.renderFace(matrixStack, vertexConsumer, zero, zero, f, f - diff, zero, one, one, zero);
                this.renderFace(matrixStack, vertexConsumer, zero, one, f, f, zero, zero, one, one);
                this.renderFace(matrixStack, vertexConsumer, zero, one, f - diff, f - diff, one, one, zero, zero);
                break;
            case NORTH:
                f = 0.5f - 0.25f;
                this.renderFace(matrixStack, vertexConsumer, zero, one, zero, one, f, f, f, f);
                this.renderFace(matrixStack, vertexConsumer, zero, one, one, zero, f - diff, f - diff, f - diff, f - diff);
                this.renderFace(matrixStack, vertexConsumer, one, one, one, zero, f - diff, f, f, f - diff);
                this.renderFace(matrixStack, vertexConsumer, zero, zero, zero, one, f - diff, f, f, f - diff);
                this.renderFace(matrixStack, vertexConsumer, zero, one, zero, zero, f - diff, f - diff, f, f);
                this.renderFace(matrixStack, vertexConsumer, zero, one, one, one, f, f, f - diff, f - diff);
                break;
            case SOUTH:
                f = 0.5f + 0.25f;
                this.renderFace(matrixStack, vertexConsumer, zero, one, zero, one, f, f, f, f);
                this.renderFace(matrixStack, vertexConsumer, zero, one, one, zero, f + diff, f + diff, f + diff, f + diff);
                this.renderFace(matrixStack, vertexConsumer, zero, zero, zero, one, f + diff, f, f, f + diff);
                this.renderFace(matrixStack, vertexConsumer, one, one, one, zero, f + diff, f, f, f + diff);
                this.renderFace(matrixStack, vertexConsumer, zero, one, one, one, f, f, f + diff, f + diff);
                this.renderFace(matrixStack, vertexConsumer, zero, one, zero, zero, f + diff, f + diff, f, f);
                break;
            case EAST:
                f = 0.5f + 0.25f;
                this.renderFace(matrixStack, vertexConsumer, f, f + diff, zero, one, one, one, one, one);
                this.renderFace(matrixStack, vertexConsumer, f, f + diff, one, zero, zero, zero, zero, zero);
                this.renderFace(matrixStack, vertexConsumer, f + diff, f + diff, one, zero, zero, one, one, zero);
                this.renderFace(matrixStack, vertexConsumer, f, f, zero, one, zero, one, one, zero);
                this.renderFace(matrixStack, vertexConsumer, f, f + diff, zero, zero, zero, zero, one, one);
                this.renderFace(matrixStack, vertexConsumer, f, f + diff, one, one, one, one, zero, zero);
                break;
            case WEST:
                f = 0.5f - 0.25f;
                this.renderFace(matrixStack, vertexConsumer, f, f - diff, zero, one, one, one, one, one);
                this.renderFace(matrixStack, vertexConsumer, f, f - diff, one, zero, zero, zero, zero, zero);
                this.renderFace(matrixStack, vertexConsumer, f, f, zero, one, zero, one, one, zero);
                this.renderFace(matrixStack, vertexConsumer, f - diff, f - diff, one, zero, zero, one, one, zero);
                this.renderFace(matrixStack, vertexConsumer, f, f - diff, one, one, one, one, zero, zero);
                this.renderFace(matrixStack, vertexConsumer, f, f - diff, zero, zero, zero, zero, one, one);
                break;
            default:
                break;
        }
    }

    private void renderFace(Matrix4f matrixStack, VertexConsumer vertexConsumer, float x1, float x2, float y1, float y2, float z1, float z2, float z3, float z4) {
        vertexConsumer.addVertex(matrixStack, x1, y1, z1);
        vertexConsumer.addVertex(matrixStack, x2, y1, z2);
        vertexConsumer.addVertex(matrixStack, x2, y2, z3);
        vertexConsumer.addVertex(matrixStack, x1, y2, z4);
    }
}
