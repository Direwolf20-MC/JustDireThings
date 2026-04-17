package com.direwolf20.justdirethings.client.entityrenders;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.client.renderers.OurRenderTypes;
import com.direwolf20.justdirethings.common.entities.ParadoxEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

import java.awt.*;
import java.util.Random;

public class ParadoxEntityRender extends EntityRenderer<ParadoxEntity, ParadoxEntityRender.ParadoxEntityRenderState> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(JustDireThings.MODID, "textures/entity/vortex1.png");

    public static class ParadoxEntityRenderState extends EntityRenderState {
        public float currentRadius;
        public float targetRadius;
        public int growthTicks;
        public int growthDuration;
        public float shrinkScale;
        public int tickCount;
        public float partialTicks;
        public float savedPulseScale = -1F;
    }

    public ParadoxEntityRender(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public ParadoxEntityRenderState createRenderState() {
        return new ParadoxEntityRenderState();
    }

    @Override
    public void extractRenderState(ParadoxEntity entity, ParadoxEntityRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.currentRadius = entity.getRadius();
        state.targetRadius = entity.getTargetRadius();
        state.growthTicks = entity.getGrowthTicks();
        state.growthDuration = entity.growthDuration;
        state.shrinkScale = entity.getShrinkScale();
        state.tickCount = entity.tickCount;
        state.partialTicks = partialTicks;
    }

    @Override
    public void submit(ParadoxEntityRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        super.submit(state, poseStack, submitNodeCollector, camera);

        float currentRadius = state.currentRadius + 1F;
        float targetRadius = state.targetRadius + 1F;

        int pulseCycleDuration = 50;
        float tickProgress = (state.tickCount + state.partialTicks) % pulseCycleDuration;
        float pulsePhase = tickProgress / pulseCycleDuration;
        float pulseScale = 0.25F + 0.025F * (1F - Math.abs(2F * pulsePhase - 1F));

        if (state.growthTicks > 0) {
            if (state.savedPulseScale == -1F) state.savedPulseScale = pulseScale;
            float growthProgress = state.growthDuration > 0 ? (float) state.growthTicks / (float) state.growthDuration : 0F;
            currentRadius = Mth.lerp(growthProgress, currentRadius, targetRadius);
        } else {
            state.savedPulseScale = -1F;
        }
        if (state.savedPulseScale != -1F) pulseScale = state.savedPulseScale;

        final float finalPulseScale = pulseScale;
        final float sphereRadius = 0.25F * (float) Math.pow(currentRadius, 1.25);

        // Vortex sphere — TRIANGLE_STRIP pipeline.
        poseStack.pushPose();
        poseStack.translate(0F, 0.5F, 0F);
        poseStack.scale(finalPulseScale * state.shrinkScale, finalPulseScale * state.shrinkScale, finalPulseScale * state.shrinkScale);
        submitNodeCollector.submitCustomGeometry(poseStack, OurRenderTypes.TRIANGLE_STRIP,
                (pose, buffer) -> writeSphere(pose, buffer, Color.BLACK, sphereRadius));
        poseStack.popPose();

        // Lightning arcs — LINES render type. Color mixes dark red ↔ dark purple per segment.
        Random mixRandom = new Random(state.tickCount * 31L);
        float mixRatio = mixRandom.nextFloat();
        int red = (int) (100 * (1F - mixRatio) + 75 * mixRatio);
        int green = 0;
        int blue = 0;
        Color arcColor = new Color(red, green, blue, 255);

        final float arcMaxLength = (float) Math.pow(state.currentRadius + 1F, 1.25);
        final int tickCountFinal = state.tickCount;

        submitNodeCollector.submitCustomGeometry(poseStack, OurRenderTypes.lines(),
                (pose, buffer) -> writeLightning(pose, buffer, tickCountFinal, arcColor,
                        0.025F, arcMaxLength, 5, 0.25F, 5, true, state.lightCoords));
    }

    // --- Vortex sphere (TRIANGLE_STRIP) -------------------------------------------------

    private static void writeSphere(PoseStack.Pose pose, VertexConsumer buffer, Color color, float radius) {
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        int a = color.getAlpha();
        int lat = 16;
        int lon = 16;
        for (int latNumber = 0; latNumber <= lat; latNumber++) {
            float theta1 = (float) (latNumber * Math.PI / lat);
            float theta2 = (float) ((latNumber + 1) * Math.PI / lat);
            float sinTheta1 = Mth.sin(theta1);
            float cosTheta1 = Mth.cos(theta1);
            float sinTheta2 = Mth.sin(theta2);
            float cosTheta2 = Mth.cos(theta2);
            for (int longNumber = 0; longNumber <= lon; longNumber++) {
                float phi = (float) (longNumber * 2 * Math.PI / lon);
                float sinPhi = Mth.sin(phi);
                float cosPhi = Mth.cos(phi);
                float x1 = cosPhi * sinTheta1;
                float y1 = cosTheta1;
                float z1 = sinPhi * sinTheta1;
                float x2 = cosPhi * sinTheta2;
                float y2 = cosTheta2;
                float z2 = sinPhi * sinTheta2;
                buffer.addVertex(pose.pose(), x1 * radius, y1 * radius, z1 * radius).setColor(r, g, b, a);
                buffer.addVertex(pose.pose(), x2 * radius, y2 * radius, z2 * radius).setColor(r, g, b, a);
            }
        }
    }

    // --- Lightning arcs (LINES with per-vertex LineWidth) -------------------------------

    private static void writeLightning(PoseStack.Pose pose, VertexConsumer buffer, int tickCountSeed, Color color,
                                       float frequency, float maxLength, int numBranches, float branchChance,
                                       int segments, boolean branchAnywhere, int lightCoords) {
        Random rand = new Random(tickCountSeed);
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        int a = color.getAlpha();

        int iterations = (int) (frequency * 100F);
        for (int i = 0; i < iterations; i++) {
            if (rand.nextFloat() > frequency) continue;

            float angle = rand.nextFloat() * 360F;
            float pitch = rand.nextFloat() * 180F - 90F;
            float length = rand.nextFloat() * maxLength;

            float sx = 0F, sy = 0.5F, sz = 0F;
            for (int segment = 0; segment < segments; segment++) {
                float segmentLength = length / segments;
                float x = sx + segmentLength * Mth.cos((float) Math.toRadians(angle)) * Mth.cos((float) Math.toRadians(pitch));
                float y = sy + segmentLength * Mth.sin((float) Math.toRadians(pitch));
                float z = sz + segmentLength * Mth.sin((float) Math.toRadians(angle)) * Mth.cos((float) Math.toRadians(pitch));

                line(pose, buffer, r, g, b, a, sx, sy, sz, x, y, z);

                sx = x;
                sy = y;
                sz = z;

                for (int branch = 0; branch < numBranches; branch++) {
                    if (rand.nextFloat() < branchChance && (branchAnywhere || segment == segments - 1)) {
                        float branchAngle = rand.nextFloat() * 360F;
                        float branchPitch = rand.nextFloat() * 180F - 90F;
                        float branchLength = segmentLength * (0.5F + rand.nextFloat() * 0.5F);
                        float bx = sx + branchLength * Mth.cos((float) Math.toRadians(branchAngle)) * Mth.cos((float) Math.toRadians(branchPitch));
                        float by = sy + branchLength * Mth.sin((float) Math.toRadians(branchPitch));
                        float bz = sz + branchLength * Mth.sin((float) Math.toRadians(branchAngle)) * Mth.cos((float) Math.toRadians(branchPitch));
                        line(pose, buffer, r, g, b, a, sx, sy, sz, bx, by, bz);
                    }
                }
            }
        }
    }

    private static void line(PoseStack.Pose pose, VertexConsumer buffer, int r, int g, int b, int a,
                             float x0, float y0, float z0, float x1, float y1, float z1) {
        float dx = x1 - x0, dy = y1 - y0, dz = z1 - z0;
        float len = Mth.sqrt(dx * dx + dy * dy + dz * dz);
        if (len > 0F) {
            dx /= len;
            dy /= len;
            dz /= len;
        }
        buffer.addVertex(pose.pose(), x0, y0, z0).setColor(r, g, b, a).setNormal(pose, dx, dy, dz).setLineWidth(2.0F);
        buffer.addVertex(pose.pose(), x1, y1, z1).setColor(r, g, b, a).setNormal(pose, dx, dy, dz).setLineWidth(2.0F);
    }
}
