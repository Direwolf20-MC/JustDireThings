package com.direwolf20.justdirethings.client.blockentityrenders;

import com.direwolf20.justdirethings.client.blockentityrenders.baseber.AreaAffectingBER;
import com.direwolf20.justdirethings.common.blockentities.EclipseGateBE;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import org.joml.Matrix4f;

public class EclipseGateRenderer extends AreaAffectingBER<EclipseGateBE, EclipseGateRenderer.EclipseGateRenderState> {

    public static class EclipseGateRenderState extends AreaAffectingRenderState {
        public long gameTime;
        public float partialTicks;
    }

    public EclipseGateRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public EclipseGateRenderState createRenderState() {
        return new EclipseGateRenderState();
    }

    @Override
    public void extractRenderState(EclipseGateBE blockEntity, EclipseGateRenderState state, float partialTicks, Vec3 cameraPosition,
                                    ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.gameTime = blockEntity.getLevel() != null ? blockEntity.getLevel().getGameTime() : 0L;
        state.partialTicks = partialTicks;
    }

    @Override
    public void submit(EclipseGateRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        super.submit(state, poseStack, submitNodeCollector, camera);
        long gameTime = state.gameTime;
        float partialTicks = state.partialTicks;
        submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.endPortal(),
                (pose, buffer) -> renderCube(gameTime, partialTicks, pose.pose(), buffer));
    }

    private void renderCube(long gameTime, float partialTicks, Matrix4f matrixStack, VertexConsumer vertexConsumer) {
        Direction direction = Direction.UP;
        float oneSmall = 0.515625f;
        float zeroSmall = 0.484375f;
        float oneBig = 0.53125f;
        float zeroBig = 0.46875f;
        int ticks = 80;
        float f1 = (float) Math.floorMod(gameTime, ticks) + partialTicks;
        float lerp = f1 / ticks;
        float zero;
        float one;
        if (f1 < ticks / 2f) {
            zero = Mth.lerp(lerp, zeroSmall, zeroBig);
            one = Mth.lerp(lerp, oneSmall, oneBig);
        } else {
            zero = Mth.lerp(lerp, zeroBig, zeroSmall);
            one = Mth.lerp(lerp, oneBig, oneSmall);
        }
        float diff = one - zero;
        float f;
        switch (direction) {
            case UP:
                f = 0.5f;
                this.renderFace(matrixStack, vertexConsumer, zero, one, f, f + diff, one, one, one, one);
                this.renderFace(matrixStack, vertexConsumer, zero, one, f + diff, f, zero, zero, zero, zero);
                this.renderFace(matrixStack, vertexConsumer, one, one, f + diff, f, zero, one, one, zero);
                this.renderFace(matrixStack, vertexConsumer, zero, zero, f, f + diff, zero, one, one, zero);
                this.renderFace(matrixStack, vertexConsumer, zero, one, f, f, zero, zero, one, one);
                this.renderFace(matrixStack, vertexConsumer, zero, one, f + diff, f + diff, one, one, zero, zero);
                break;
            case DOWN:
                f = 0.5f;
                this.renderFace(matrixStack, vertexConsumer, zero, one, f, f - diff, one, one, one, one);
                this.renderFace(matrixStack, vertexConsumer, zero, one, f - diff, f, zero, zero, zero, zero);
                this.renderFace(matrixStack, vertexConsumer, one, one, f - diff, f, zero, one, one, zero);
                this.renderFace(matrixStack, vertexConsumer, zero, zero, f, f - diff, zero, one, one, zero);
                this.renderFace(matrixStack, vertexConsumer, zero, one, f, f, zero, zero, one, one);
                this.renderFace(matrixStack, vertexConsumer, zero, one, f - diff, f - diff, one, one, zero, zero);
                break;
            case NORTH:
                f = 0.5f;
                this.renderFace(matrixStack, vertexConsumer, zero, one, zero, one, f, f, f, f);
                this.renderFace(matrixStack, vertexConsumer, zero, one, one, zero, f - diff, f - diff, f - diff, f - diff);
                this.renderFace(matrixStack, vertexConsumer, one, one, one, zero, f - diff, f, f, f - diff);
                this.renderFace(matrixStack, vertexConsumer, zero, zero, zero, one, f - diff, f, f, f - diff);
                this.renderFace(matrixStack, vertexConsumer, zero, one, zero, zero, f - diff, f - diff, f, f);
                this.renderFace(matrixStack, vertexConsumer, zero, one, one, one, f, f, f - diff, f - diff);
                break;
            case SOUTH:
                f = 0.5f;
                this.renderFace(matrixStack, vertexConsumer, zero, one, zero, one, f, f, f, f);
                this.renderFace(matrixStack, vertexConsumer, zero, one, one, zero, f + diff, f + diff, f + diff, f + diff);
                this.renderFace(matrixStack, vertexConsumer, zero, zero, zero, one, f + diff, f, f, f + diff);
                this.renderFace(matrixStack, vertexConsumer, one, one, one, zero, f + diff, f, f, f + diff);
                this.renderFace(matrixStack, vertexConsumer, zero, one, one, one, f, f, f + diff, f + diff);
                this.renderFace(matrixStack, vertexConsumer, zero, one, zero, zero, f + diff, f + diff, f, f);
                break;
            case EAST:
                f = 0.5f;
                this.renderFace(matrixStack, vertexConsumer, f, f + diff, zero, one, one, one, one, one);
                this.renderFace(matrixStack, vertexConsumer, f, f + diff, one, zero, zero, zero, zero, zero);
                this.renderFace(matrixStack, vertexConsumer, f + diff, f + diff, one, zero, zero, one, one, zero);
                this.renderFace(matrixStack, vertexConsumer, f, f, zero, one, zero, one, one, zero);
                this.renderFace(matrixStack, vertexConsumer, f, f + diff, zero, zero, zero, zero, one, one);
                this.renderFace(matrixStack, vertexConsumer, f, f + diff, one, one, one, one, zero, zero);
                break;
            case WEST:
                f = 0.5f;
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
