package com.direwolf20.justdirethings.client.entityrenders;

import com.direwolf20.justdirethings.client.renderers.RenderHelpers;
import com.direwolf20.justdirethings.common.entities.TimeWandEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class TimeWandEntityRender extends EntityRenderer<TimeWandEntity> {

    public TimeWandEntityRender(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public void render(TimeWandEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int pPackedLight) {
        // Calculate the color based on the rate
        float rate = pEntity.getTickSpeed();
        float[] color = getRGBColorBasedOnRate(rate);

        // Calculate the Y-level reduction based on the remaining time
        float timeRatio = (float) pEntity.getRemainingTime() / pEntity.getTotalTime(); // Assuming you have methods to get total time
        AABB renderBounds = pEntity.getBoundingBox().move(-pEntity.getX(), -pEntity.getY(), -pEntity.getZ()); // Adjust to entity's position
        renderBounds = renderBounds.setMaxY(renderBounds.maxY - (1 - timeRatio));


        // Render the box with the calculated color and adjusted height
        RenderHelpers.renderBoxSolid(matrixStackIn, matrixStackIn.last().pose(), bufferIn, renderBounds, color[0], color[1], color[2], 0.25f); // Set alpha to 0.125f

        /*String timeRate = "x" + pEntity.getAccelerationRate();
        float paddingLeftRight = 2 * pEntity.getAccelerationRate() < 10 ? 0.11F : 0.19F;
        drawText(pPoseStack, pBuffer, timeRate, new Vector3f(-paddingLeftRight, 0.064F, 0.51F), Axis.YP.rotationDegrees(0), ChatFormatting.WHITE.getColor()); // Front
        drawText(pPoseStack, pBuffer, timeRate, new Vector3f(paddingLeftRight, 0.064F, -0.51F), Axis.YP.rotationDegrees(180F), ChatFormatting.WHITE.getColor()); // Back
        drawText(pPoseStack, pBuffer, timeRate, new Vector3f(0.51F, 0.064F, paddingLeftRight), Axis.YP.rotationDegrees(90F), ChatFormatting.WHITE.getColor()); // Right
        drawText(pPoseStack, pBuffer, timeRate, new Vector3f(-0.51F, 0.064F, -paddingLeftRight), Axis.YP.rotationDegrees(-90F), ChatFormatting.WHITE.getColor()); // Left
        drawText(pPoseStack, pBuffer, timeRate, new Vector3f(-paddingLeftRight, 0.51F, -0.064F), Axis.XP.rotationDegrees(90F),  ChatFormatting.WHITE.getColor()); // Top
        drawText(pPoseStack, pBuffer, timeRate, new Vector3f(-paddingLeftRight, -0.51F, 0.064F), Axis.XP.rotationDegrees(-90F),  ChatFormatting.WHITE.getColor()); // Bottom
*/
    }

    private float[] getRGBColorBasedOnRate(float rate) {
        // Normalize rate between 1 and 8 to a value between 0 and 1
        float progress = (rate) / 8.0f;

        // Calculate RGB values based on progress (green to red)
        float r = (1.0f - progress); // Red increases with progress
        float g = (progress); // Green decreases with progress
        float b = 0; // Blue stays constant

        return new float[]{r, g, b};
    }

    @Override
    public ResourceLocation getTextureLocation(TimeWandEntity pEntity) {
        return null;
    }

    private void drawText(PoseStack matrixStack, MultiBufferSource source, String text, Vector3f translateVector, Quaternionf rotate, int color) {
        matrixStack.pushPose();
        matrixStack.translate(translateVector.x(), translateVector.y(), translateVector.z());
        matrixStack.scale(0.02F, -0.02F, 0.02F);
        matrixStack.mulPose(rotate);
        getFont().drawInBatch(
                text,
                0,
                0,
                -1,
                false,
                matrixStack.last().pose(),
                source,
                Font.DisplayMode.NORMAL,
                0,
                color
        );
        matrixStack.popPose();
    }
}
