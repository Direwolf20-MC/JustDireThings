package com.direwolf20.justdirethings.client.entityrenders;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.client.renderers.OurRenderTypes;
import com.direwolf20.justdirethings.client.renderers.RenderHelpers;
import com.direwolf20.justdirethings.common.entities.ParadoxEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
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

   /* @Override
    public void render(ParadoxEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int pPackedLight) {
        AABB renderBounds = pEntity.getBoundingBox().move(-pEntity.getX(), -pEntity.getY(), -pEntity.getZ());
        matrixStackIn.pushPose();
        matrixStackIn.translate(0, 0.5, 0);
        matrixStackIn.pushPose();
        // Calculate the scale for pulsing
        //matrixStackIn.translate(0, 0.25, 0);
        float pulseScale = 0.25f + 0.025f * (float) Math.sin((pEntity.tickCount + pPartialTicks) / 10.0);
        // Apply the scale transformation for pulsing
        matrixStackIn.scale(pulseScale, pulseScale, pulseScale);
        RenderHelpers.renderSphere(matrixStackIn, bufferIn, Color.WHITE, 0.5f, pPackedLight);
        matrixStackIn.popPose();

        matrixStackIn.pushPose();
        // Render the vortex around the sphere
        matrixStackIn.mulPose(new Quaternionf().rotationX((float) Math.toRadians(90))); // Rotate 90 degrees around the X-axis

        RenderHelpers.renderVortex(matrixStackIn, bufferIn, pPackedLight, Color.BLUE, 0.5f, 64, 16, pEntity.tickCount, 0.5f, 0.002f);
        matrixStackIn.popPose();
        matrixStackIn.popPose();

        /*matrixStackIn.pushPose();
        Color color = new Color(0, 153, 255, 255);
        RenderHelpers.renderLines(matrixStackIn, renderBounds, color, bufferIn);
        matrixStackIn.popPose();*/
    //}

    @Override
    public ResourceLocation getTextureLocation(ParadoxEntity pEntity) {
        return TEXTURE;
    }

    @Override
    public void render(ParadoxEntity paradoxEntity, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        matrixStackIn.pushPose();

        // Rotate towards the player
        Player player = Minecraft.getInstance().player;
        Vec3 playerPos = player.getEyePosition(partialTicks);
        Vec3 paradoxPos = paradoxEntity.position();

        // Center the rotation and scaling on the entity's bounding box
        //matrixStackIn.translate(0.5, paradoxEntity.getBbHeight() / 2, 0.5);

        // Rotate towards the player
        //float yaw = 180.0F - (float) Math.toDegrees(Math.atan2(playerPos.x - paradoxPos.x, playerPos.z - paradoxPos.z));
        //matrixStackIn.mulPose(new Quaternionf().rotationAxis(Mth.DEG_TO_RAD * yaw, 0, 1, 0));

        // Scale the core of the Paradox
        //float scale = 0.5f + 0.1f * (float) Math.sin(paradoxEntity.tickCount / 10.0);
        //matrixStackIn.scale(scale, scale, scale);

        // Render the glowing core as a solid cube
        //RenderHelpers.renderBoxSolid(matrixStackIn, matrixStackIn.last().pose(), bufferIn, paradoxEntity.getBoundingBox(), 1, 0, 0, 0.3f);

        matrixStackIn.pushPose();
        // Calculate the scale for pulsing
        matrixStackIn.translate(0, 0.5, 0);
        float pulseScale = 0.25f + 0.025f * (float) Math.sin((paradoxEntity.tickCount + partialTicks) / 10.0);
        // Apply the scale transformation for pulsing
        matrixStackIn.scale(pulseScale, pulseScale, pulseScale);
        RenderHelpers.renderSphere(matrixStackIn, bufferIn, Color.BLACK, 0.25f * (float) Math.pow(paradoxEntity.getRadius() + 1, 1.25), packedLightIn);
        matrixStackIn.popPose();

        // Render the swirling effect
        //renderSwirl(paradoxEntity, matrixStackIn, bufferIn, packedLightIn, partialTicks, 1.0f, 13f);

        // Render the lightning arcs
        // Base colors for dark red and dark purple
        Color baseRed = new Color(100, 0, 0, 255);   // Dark red
        Color basePurple = new Color(75, 0, 0, 255); // Dark purple

        // Randomly interpolate between dark red and dark purple
        float mixRatio = random.nextFloat();
        int red = (int) (baseRed.getRed() * (1 - mixRatio) + basePurple.getRed() * mixRatio);
        int green = (int) (baseRed.getGreen() * (1 - mixRatio) + basePurple.getGreen() * mixRatio);
        int blue = (int) (baseRed.getBlue() * (1 - mixRatio) + basePurple.getBlue() * mixRatio);

        renderLightning2(paradoxEntity, matrixStackIn, bufferIn, packedLightIn, new Color(red, green, blue, 255), 0.025f, (float) Math.pow(paradoxEntity.getRadius() + 1, 1.25), 20, 0.7f, 5, 0.25f, 5, true);

        matrixStackIn.popPose();

        /*AABB renderBounds = paradoxEntity.getBoundingBox().move(-paradoxEntity.getX(), -paradoxEntity.getY(), -paradoxEntity.getZ());
        matrixStackIn.pushPose();
        Color color = new Color(0, 153, 255, 255);
        RenderHelpers.renderLines(matrixStackIn, renderBounds, color, bufferIn);
        matrixStackIn.popPose();*/
    }

    private void renderLightning(ParadoxEntity paradoxEntity, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, Color color, float frequency, float maxLength, int duration, float thickness) {
        for (int i = 0; i < frequency * 100; i++) {
            if (random.nextFloat() > frequency)
                continue;  // Randomly decide whether to render a lightning arc based on frequency

            matrixStackIn.pushPose();
            matrixStackIn.translate(0, 0.5, 0);  // Translate to the center of the entity

            float angle = random.nextFloat() * 360.0f;  // Random angle in 360 degrees
            float pitch = random.nextFloat() * 180.0f - 90.0f;  // Random pitch between -90 and 90 degrees
            float length = random.nextFloat() * maxLength;  // Random length of the lightning arc

            // Calculate the endpoint of the lightning arc
            float x = length * Mth.cos((float) Math.toRadians(angle)) * Mth.cos((float) Math.toRadians(pitch));
            float y = length * Mth.sin((float) Math.toRadians(pitch));
            float z = length * Mth.sin((float) Math.toRadians(angle)) * Mth.cos((float) Math.toRadians(pitch));

            VertexConsumer vertexConsumer = bufferIn.getBuffer(OurRenderTypes.LINE_STRIP);
            Matrix4f matrix = matrixStackIn.last().pose();
            Vector3f normal = new Vector3f(0.0F, 1.0F, 0.0F);  // Assuming lightning is striking upwards

            // Draw the lightning arc
            vertexConsumer.addVertex(matrix, 0.0F, 0.0F, 0.0F)
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

            matrixStackIn.popPose();
        }
    }

    private void renderLightning2(ParadoxEntity paradoxEntity, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, Color color, float frequency, float maxLength, int duration, float thickness, int numBranches, float branchChance, int segments, boolean branchAnywhere) {
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

    private void createLightningSegment(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, Color color, Vec3 startPoint, Vec3 endPoint, float thickness) {
        VertexConsumer vertexConsumer = bufferIn.getBuffer(OurRenderTypes.LINE_STRIP);
        Matrix4f matrix = matrixStackIn.last().pose();
        Vector3f normal = new Vector3f(0.0F, 1.0F, 0.0F);  // Assuming lightning is striking upwards

        // Draw the lightning segment
        vertexConsumer.addVertex(matrix, (float) startPoint.x, (float) startPoint.y, (float) startPoint.z)
                .setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
                .setUv(0.0F, 0.0F)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(packedLightIn)
                .setNormal(matrixStackIn.last(), normal.x(), normal.y(), normal.z());

        vertexConsumer.addVertex(matrix, (float) endPoint.x, (float) endPoint.y, (float) endPoint.z)
                .setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
                .setUv(1.0F, 1.0F)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(packedLightIn)
                .setNormal(matrixStackIn.last(), normal.x(), normal.y(), normal.z());
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

    private void renderSwirl(ParadoxEntity paradoxEntity, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, float partialTicks, float radius, float spinSpeed) {
        matrixStackIn.pushPose();

        // Translate to the center and scale according to the desired radius
        matrixStackIn.translate(0, 0.5, 0);
        matrixStackIn.scale(radius, radius, radius);

        // Apply rotation for the spinning effect around the X-axis (or Z-axis if preferred)
        float rotationAngle = (paradoxEntity.tickCount + partialTicks) * spinSpeed;
        matrixStackIn.mulPose(Axis.ZN.rotationDegrees(rotationAngle));  // Change Axis.XP to Axis.ZP if you prefer Z-axis rotation

        // Set up the vertex consumer to use the texture
        VertexConsumer vertexConsumer = bufferIn.getBuffer(RenderType.entityTranslucent(getTextureLocation(paradoxEntity)));
        Matrix4f matrix = matrixStackIn.last().pose();
        PoseStack.Pose lastPose = matrixStackIn.last();

        // Define the normal vector
        Vector3f normal = new Vector3f(0.0F, 0.0F, -1.0F);

        // Define the corners of the quad (using the scaled size now)
        vertexConsumer.addVertex(matrix, -1.0F, -1.0F, 0.0F)
                .setColor(255, 255, 255, 255)
                .setUv(0.0F, 0.0F)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(packedLightIn)
                .setNormal(lastPose, normal.x(), normal.y(), normal.z());

        vertexConsumer.addVertex(matrix, 1.0F, -1.0F, 0.0F)
                .setColor(255, 255, 255, 255)
                .setUv(1.0F, 0.0F)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(packedLightIn)
                .setNormal(lastPose, normal.x(), normal.y(), normal.z());

        vertexConsumer.addVertex(matrix, 1.0F, 1.0F, 0.0F)
                .setColor(255, 255, 255, 255)
                .setUv(1.0F, 1.0F)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(packedLightIn)
                .setNormal(lastPose, normal.x(), normal.y(), normal.z());

        vertexConsumer.addVertex(matrix, -1.0F, 1.0F, 0.0F)
                .setColor(255, 255, 255, 255)
                .setUv(0.0F, 1.0F)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(packedLightIn)
                .setNormal(lastPose, normal.x(), normal.y(), normal.z());

        matrixStackIn.popPose();
    }
}
