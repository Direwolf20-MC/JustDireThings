package com.direwolf20.justdirethings.client.entityrenders;

import com.direwolf20.justdirethings.client.blockentityrenders.baseber.AreaAffectingBER;
import com.direwolf20.justdirethings.client.renderers.OurRenderTypes;
import com.direwolf20.justdirethings.common.entities.TimeWandEntity;
import com.direwolf20.justdirethings.setup.Config;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.AABB;
import org.joml.Quaternionf;
import org.joml.Vector3f;

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
        int maxMult = Config.TIME_WAND_MAX_MULTIPLIER.get();
        float denom = (float) Config.logBase2(maxMult);
        state.tickRateProgress = denom > 0F ? entity.getTickSpeed() / denom : 0F;
        state.timeProgress = entity.getTotalTime() > 0 ? (float) entity.getRemainingTime() / entity.getTotalTime() : 0f;
    }

    @Override
    public void submit(TimeWandRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        if (state.blockIsAir) return;
        super.submit(state, poseStack, submitNodeCollector, camera);

        // Progress bars (green tick-rate + red time) on each of the six block sides.
        submitProgressBarAllSides(poseStack, submitNodeCollector, 0.3F, state.tickRateProgress, 0F, 1F, 0F, 0.5F);
        submitProgressBarAllSides(poseStack, submitNodeCollector, 0.6F, state.timeProgress, 1F, 0F, 0F, 0.5F);

        // Text labels on every face — rate ("xN") above time ("X.XXs").
        String timeRate = "x" + state.accelerationRate;
        String timeRemains = String.format("%.2f", state.remainingTimeTicks / 20.0F) + "s";
        final float padX = 0.13F;
        final int color = 0xFFFFFFFF;

        submitTextAllFaces(poseStack, submitNodeCollector, timeRate, 0.39F, padX, state.lightCoords, color);
        submitTextAllFaces(poseStack, submitNodeCollector, timeRemains, 0.69F, padX, state.lightCoords, color);
    }

    private void submitProgressBarAllSides(PoseStack poseStack, SubmitNodeCollector collector,
                                           float yStart, float progress, float r, float g, float b, float a) {
        submitBarFace(poseStack, collector, -0.4F, yStart, 0.5F, progress, r, g, b, a, new Quaternionf().rotateY(0F));
        submitBarFace(poseStack, collector, -0.4F, yStart, 0.5F, progress, r, g, b, a, new Quaternionf().rotateY((float) Math.PI));
        submitBarFace(poseStack, collector, -0.4F, yStart, 0.5F, progress, r, g, b, a, new Quaternionf().rotateY((float) Math.PI / 2F));
        submitBarFace(poseStack, collector, -0.4F, yStart, 0.5F, progress, r, g, b, a, new Quaternionf().rotateY((float) -Math.PI / 2F));
        submitBarFace(poseStack, collector, -0.4F, yStart - 0.5F, 1F, progress, r, g, b, a, new Quaternionf().rotateX((float) -Math.PI / 2F));
        submitBarFace(poseStack, collector, -0.4F, yStart - 0.5F, 0F, progress, r, g, b, a, new Quaternionf().rotateX((float) Math.PI / 2F));
    }

    private void submitBarFace(PoseStack poseStack, SubmitNodeCollector collector,
                               float xStart, float yStart, float zStart, float progress,
                               float r, float g, float b, float a, Quaternionf rotation) {
        final float barWidth = 0.8F * progress;
        final float barHeight = 0.1F;
        if (barWidth <= 0F) return;
        poseStack.pushPose();
        poseStack.mulPose(rotation);
        final AABB aabb = new AABB(xStart, yStart, zStart, xStart + barWidth, yStart + barHeight, zStart);
        collector.submitCustomGeometry(poseStack, OurRenderTypes.TRANSPARENT_BOX,
                (pose, buffer) -> AreaAffectingBER.drawAABBSolid(pose, buffer, aabb, r, g, b, a));
        poseStack.popPose();
    }

    private void submitTextAllFaces(PoseStack poseStack, SubmitNodeCollector collector, String text,
                                    float y, float padX, int lightCoords, int color) {
        net.minecraft.util.FormattedCharSequence seq = Component.literal(text).getVisualOrderText();
        submitTextAt(poseStack, collector, seq, new Vector3f(-padX, y, 0.51F), Axis.YP.rotationDegrees(0F), lightCoords, color);
        submitTextAt(poseStack, collector, seq, new Vector3f(padX, y, -0.51F), Axis.YP.rotationDegrees(180F), lightCoords, color);
        submitTextAt(poseStack, collector, seq, new Vector3f(0.51F, y, padX), Axis.YP.rotationDegrees(90F), lightCoords, color);
        submitTextAt(poseStack, collector, seq, new Vector3f(-0.51F, y, -padX), Axis.YP.rotationDegrees(-90F), lightCoords, color);
        submitTextAt(poseStack, collector, seq, new Vector3f(-padX, 1.01F, 0.5F - y), Axis.XP.rotationDegrees(90F), lightCoords, color);
        submitTextAt(poseStack, collector, seq, new Vector3f(-padX, -0.01F, -0.5F + y), Axis.XP.rotationDegrees(-90F), lightCoords, color);
    }

    private void submitTextAt(PoseStack poseStack, SubmitNodeCollector collector,
                              net.minecraft.util.FormattedCharSequence text, Vector3f offset,
                              Quaternionf rotation, int lightCoords, int color) {
        poseStack.pushPose();
        poseStack.translate(offset.x(), offset.y(), offset.z());
        poseStack.scale(0.01F, -0.01F, 0.01F);
        poseStack.mulPose(rotation);
        collector.submitText(poseStack, 0F, 0F, text, false, Font.DisplayMode.NORMAL, lightCoords, color, 0, 0);
        poseStack.popPose();
    }
}
