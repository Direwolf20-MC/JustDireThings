package com.direwolf20.justdirethings.client.entityrenders;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.client.renderers.OurRenderTypes;
import com.direwolf20.justdirethings.client.renderers.RenderHelpers;
import com.direwolf20.justdirethings.common.entities.ParadoxEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.awt.*;
import java.util.Random;

public class ParadoxEntityRender extends EntityRenderer<ParadoxEntity> {
    private static final Random random = new Random();  // Use entity's tickCount to seed the randomness

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "textures/entity/vortex1.png");


    public ParadoxEntityRender(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public ResourceLocation getTextureLocation(ParadoxEntity pEntity) {
        return TEXTURE;
    }

    @Override
    public void render(ParadoxEntity paradoxEntity, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        matrixStackIn.pushPose();

        matrixStackIn.pushPose();
        // Calculate the current scale based on the interpolated radius
        float currentRadius = paradoxEntity.getRadius() + 1;
        if (paradoxEntity.getGrowthTicks() > 0) {
            currentRadius = currentRadius + (Math.min(1.0f, (float) paradoxEntity.getGrowthTicks() / paradoxEntity.growthDuration));
        }

        // Calculate the scale for pulsing
        matrixStackIn.translate(0, 0.5, 0);
        float pulseScale = 0.25f + 0.025f * (float) Math.sin((paradoxEntity.tickCount + partialTicks) / 10.0);
        float shrinkScale = paradoxEntity.getShrinkScale(); // Get the current shrink scale

        // Apply the scale transformation for pulsing
        matrixStackIn.scale(pulseScale * shrinkScale, pulseScale * shrinkScale, pulseScale * shrinkScale);
        RenderHelpers.renderSphere(matrixStackIn, bufferIn, Color.BLACK, 0.25f * (float) Math.pow(currentRadius, 1.25), packedLightIn);
        matrixStackIn.popPose();

        // Render the lightning arcs
        // Base colors for dark red and dark purple
        Color baseRed = new Color(100, 0, 0, 255);   // Dark red
        Color basePurple = new Color(75, 0, 0, 255); // Dark purple

        // Randomly interpolate between dark red and dark purple
        float mixRatio = random.nextFloat();
        int red = (int) (baseRed.getRed() * (1 - mixRatio) + basePurple.getRed() * mixRatio);
        int green = (int) (baseRed.getGreen() * (1 - mixRatio) + basePurple.getGreen() * mixRatio);
        int blue = (int) (baseRed.getBlue() * (1 - mixRatio) + basePurple.getBlue() * mixRatio);

        renderLightning(paradoxEntity, matrixStackIn, bufferIn, packedLightIn, new Color(red, green, blue, 255), 0.025f, (float) Math.pow(paradoxEntity.getRadius() + 1, 1.25), 20, 0.7f, 5, 0.25f, 5, true);

        matrixStackIn.popPose();

        /*AABB renderBounds = paradoxEntity.getBoundingBox().move(-paradoxEntity.getX(), -paradoxEntity.getY(), -paradoxEntity.getZ());
        matrixStackIn.pushPose();
        Color color = new Color(0, 153, 255, 255);
        RenderHelpers.renderLines(matrixStackIn, renderBounds, color, bufferIn);
        matrixStackIn.popPose();*/
    }

    private void renderLightning(ParadoxEntity paradoxEntity, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, Color color, float frequency, float maxLength, int duration, float thickness, int numBranches, float branchChance, int segments, boolean branchAnywhere) {
        Random random = new Random(paradoxEntity.tickCount);  // Use entity's tickCount to seed the randomness

        for (int i = 0; i < frequency * 100; i++) {
            if (random.nextFloat() > frequency)
                continue;  // Randomly decide whether to render a lightning arc based on frequency

            matrixStackIn.pushPose();
            matrixStackIn.translate(0, 0.5, 0);  // Translate to the center of the entity

            float angle = random.nextFloat() * 360.0f;  // Random angle in 360 degrees
            float pitch = random.nextFloat() * 180.0f - 90.0f;  // Random pitch between -90 and 90 degrees
            float length = random.nextFloat() * maxLength;  // Random length of the lightning arc

            // Start position
            float startX = 0.0F, startY = 0.0F, startZ = 0.0F;

            for (int segment = 0; segment < segments; segment++) {
                float segmentLength = length / segments;

                // Calculate the endpoint of the lightning arc segment
                float x = startX + segmentLength * Mth.cos((float) Math.toRadians(angle)) * Mth.cos((float) Math.toRadians(pitch));
                float y = startY + segmentLength * Mth.sin((float) Math.toRadians(pitch));
                float z = startZ + segmentLength * Mth.sin((float) Math.toRadians(angle)) * Mth.cos((float) Math.toRadians(pitch));

                VertexConsumer vertexConsumer = bufferIn.getBuffer(OurRenderTypes.LINE_STRIP);
                Matrix4f matrix = matrixStackIn.last().pose();
                Vector3f normal = new Vector3f(0.0F, 1.0F, 0.0F);

                // Draw the lightning arc segment
                vertexConsumer.addVertex(matrix, startX, startY, startZ)
                        .setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
                        .setUv(0.0F, 0.0F)
                        .setOverlay(OverlayTexture.NO_OVERLAY)
                        .setLight(packedLightIn)
                        .setNormal(matrixStackIn.last(), normal.x(), normal.y(), normal.z());

                vertexConsumer.addVertex(matrix, x, y, z)
                        .setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
                        .setUv(1.0F, 1.0F)
                        .setOverlay(OverlayTexture.NO_OVERLAY)
                        .setLight(packedLightIn)
                        .setNormal(matrixStackIn.last(), normal.x(), normal.y(), normal.z());

                // Update the start position for the next segment
                startX = x;
                startY = y;
                startZ = z;

                // Generate branches
                for (int branch = 0; branch < numBranches; branch++) {
                    if (random.nextFloat() < branchChance && (branchAnywhere || segment == segments - 1)) {
                        renderBranch(matrixStackIn, bufferIn, packedLightIn, color, startX, startY, startZ, segmentLength, branchAnywhere ? random.nextInt(segments) : segment, random, normal);
                    }
                }
            }

            matrixStackIn.popPose();
        }
    }

    private void renderBranch(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, Color color, float startX, float startY, float startZ, float segmentLength, int segment, Random random, Vector3f normal) {
        float branchAngle = random.nextFloat() * 360.0f;
        float branchPitch = random.nextFloat() * 180.0f - 90.0f;
        float branchLength = segmentLength * (0.5f + random.nextFloat() * 0.5f);  // Branches are shorter

        // Calculate the endpoint of the branch
        float bx = startX + branchLength * Mth.cos((float) Math.toRadians(branchAngle)) * Mth.cos((float) Math.toRadians(branchPitch));
        float by = startY + branchLength * Mth.sin((float) Math.toRadians(branchPitch));
        float bz = startZ + branchLength * Mth.sin((float) Math.toRadians(branchAngle)) * Mth.cos((float) Math.toRadians(branchPitch));

        VertexConsumer vertexConsumer = bufferIn.getBuffer(OurRenderTypes.LINE_STRIP);
        Matrix4f matrix = matrixStackIn.last().pose();

        // Draw the branch
        vertexConsumer.addVertex(matrix, startX, startY, startZ)
                .setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
                .setUv(0.0F, 0.0F)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(packedLightIn)
                .setNormal(matrixStackIn.last(), normal.x(), normal.y(), normal.z());

        vertexConsumer.addVertex(matrix, bx, by, bz)
                .setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
                .setUv(1.0F, 1.0F)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(packedLightIn)
                .setNormal(matrixStackIn.last(), normal.x(), normal.y(), normal.z());
    }
}
