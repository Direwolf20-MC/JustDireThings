package com.direwolf20.justdirethings.client.entityrenders;

import com.direwolf20.justdirethings.common.entities.TimeWandEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.BlockPos;

// TODO(port, stage-16): reinstate time-wand overlay rendering.
// Old 1.21.1 code drew green/red progress bars on each of the six block sides and 6 copies of
// rotated text ("xN" and "1.23s") via RenderHelpers.renderBoxSolid + Font.drawInBatch with
// Matrix3x2f rotation. Both helpers are being rebuilt in Stage 16 (RenderHelpers against the new
// vertex path; GuiGraphicsExtractor.pose() gives back Matrix3x2fStack without Z).
public class TimeWandEntityRender extends EntityRenderer<TimeWandEntity, TimeWandEntityRender.TimeWandRenderState> {

    public static class TimeWandRenderState extends EntityRenderState {
        public boolean blockIsAir;
        public float tickRateProgress;
        public float timeProgress;
        public float accelerationRate;
        public int remainingTimeTicks;
    }

    public TimeWandEntityRender(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public TimeWandRenderState createRenderState() {
        return new TimeWandRenderState();
    }

    @Override
    public void extractRenderState(TimeWandEntity entity, TimeWandRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        BlockPos blockPos = entity.blockPosition();
        state.blockIsAir = entity.level().getBlockState(blockPos).isAir();
        state.accelerationRate = entity.getAccelerationRate();
        state.remainingTimeTicks = entity.getRemainingTime();
        state.tickRateProgress = entity.getTickSpeed();
        state.timeProgress = entity.getTotalTime() > 0 ? (float) entity.getRemainingTime() / entity.getTotalTime() : 0f;
    }

    @Override
    public void submit(TimeWandRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        if (state.blockIsAir) return;
        super.submit(state, poseStack, submitNodeCollector, camera);
        // TODO(port, stage-16): re-submit progress bars + text here via submitCustomGeometry + GuiGraphicsExtractor text.
    }
}
