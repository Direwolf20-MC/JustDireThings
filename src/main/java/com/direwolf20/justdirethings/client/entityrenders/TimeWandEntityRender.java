package com.direwolf20.justdirethings.client.entityrenders;

import com.direwolf20.justdirethings.client.renderers.RenderHelpers;
import com.direwolf20.justdirethings.common.entities.TimeWandEntity;
import com.direwolf20.justdirethings.setup.Config;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
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
        BlockPos blockPos = pEntity.blockPosition();
        if (pEntity.level().getBlockState(blockPos).isAir())
            return;
        matrixStackIn.pushPose();

        // Calculate the progress for tick rate and time remaining
        float tickRateProgress = pEntity.getTickSpeed() / (float) Config.logBase2(Config.TIME_WAND_MAX_MULTIPLIER.get());
        float timeProgress = (float) pEntity.getRemainingTime() / pEntity.getTotalTime(); // Assuming methods for total time

        // Render progress bars on all 6 sides of the block
        renderProgressBarOnSide(matrixStackIn, bufferIn, 0.3f, tickRateProgress, 0.0f, 1.0f, 0.0f, 0.5f, pPackedLight); // Green tick rate bar
        renderProgressBarOnSide(matrixStackIn, bufferIn, 0.6f, timeProgress, 1.0f, 0.0f, 0.0f, 0.5f, pPackedLight); // Red time remaining bar

        String timeRate = "x" + pEntity.getAccelerationRate();
        int timeRemaining = pEntity.getRemainingTime(); // Assuming this gives you time in ticks
        float timeInSeconds = timeRemaining / 20.0f; // Convert ticks to seconds
        String timeRemains = String.format("%.2f", timeInSeconds) + "s"; // Format to 2 decimal places and append "s"
        float paddingLeftRight = 0.13F;
        drawText(matrixStackIn, bufferIn, timeRate, new Vector3f(-paddingLeftRight, 0.39f, 0.51F), Axis.YP.rotationDegrees(0), ChatFormatting.WHITE.getColor()); // Front
        drawText(matrixStackIn, bufferIn, timeRate, new Vector3f(paddingLeftRight, 0.39f, -0.51F), Axis.YP.rotationDegrees(180F), ChatFormatting.WHITE.getColor()); // Back
        drawText(matrixStackIn, bufferIn, timeRate, new Vector3f(0.51F, 0.39f, paddingLeftRight), Axis.YP.rotationDegrees(90F), ChatFormatting.WHITE.getColor()); // Right
        drawText(matrixStackIn, bufferIn, timeRate, new Vector3f(-0.51F, 0.39f, -paddingLeftRight), Axis.YP.rotationDegrees(-90F), ChatFormatting.WHITE.getColor()); // Left
        drawText(matrixStackIn, bufferIn, timeRate, new Vector3f(-paddingLeftRight, 1.01F, 0.5f - 0.39f), Axis.XP.rotationDegrees(90F), ChatFormatting.WHITE.getColor()); // Top
        drawText(matrixStackIn, bufferIn, timeRate, new Vector3f(-paddingLeftRight, -0.01F, -0.5f + 0.39f), Axis.XP.rotationDegrees(-90F), ChatFormatting.WHITE.getColor()); // Bottom

        drawText(matrixStackIn, bufferIn, timeRemains, new Vector3f(-paddingLeftRight, 0.69f, 0.51F), Axis.YP.rotationDegrees(0), ChatFormatting.WHITE.getColor()); // Front
        drawText(matrixStackIn, bufferIn, timeRemains, new Vector3f(paddingLeftRight, 0.69f, -0.51F), Axis.YP.rotationDegrees(180F), ChatFormatting.WHITE.getColor()); // Back
        drawText(matrixStackIn, bufferIn, timeRemains, new Vector3f(0.51F, 0.69f, paddingLeftRight), Axis.YP.rotationDegrees(90F), ChatFormatting.WHITE.getColor()); // Right
        drawText(matrixStackIn, bufferIn, timeRemains, new Vector3f(-0.51F, 0.69f, -paddingLeftRight), Axis.YP.rotationDegrees(-90F), ChatFormatting.WHITE.getColor()); // Left
        drawText(matrixStackIn, bufferIn, timeRemains, new Vector3f(-paddingLeftRight, 1.01F, 0.5f - 0.69f), Axis.XP.rotationDegrees(90F), ChatFormatting.WHITE.getColor()); // Top
        drawText(matrixStackIn, bufferIn, timeRemains, new Vector3f(-paddingLeftRight, -0.01F, -0.5f + 0.69f), Axis.XP.rotationDegrees(-90F), ChatFormatting.WHITE.getColor()); // Bottom

        matrixStackIn.popPose();
    }

    private void renderProgressBarOnSide(PoseStack matrixStack, MultiBufferSource buffer, float yStart, float progress, float r, float g, float b, float a, int pPackedLight) {
        // Define the bar width and height for rendering
        float barWidth = 0.8f;
        float barHeight = 0.1f;
        float barProgress = barWidth * progress; // Adjust width based on progress

        // Front face
        renderBarFace(matrixStack, buffer, -0.4f, yStart, 0.5f, barProgress, barHeight, r, g, b, a, pPackedLight, new Quaternionf().rotateY(0.0f));

        // Back face
        renderBarFace(matrixStack, buffer, -0.4f, yStart, 0.5f, barProgress, barHeight, r, g, b, a, pPackedLight, new Quaternionf().rotateY((float) Math.PI));

        // Left face
        renderBarFace(matrixStack, buffer, -0.4f, yStart, 0.5f, barProgress, barHeight, r, g, b, a, pPackedLight, new Quaternionf().rotateY((float) Math.PI / 2.0f));

        // Right face
        renderBarFace(matrixStack, buffer, -0.4f, yStart, 0.5f, barProgress, barHeight, r, g, b, a, pPackedLight, new Quaternionf().rotateY((float) -Math.PI / 2.0f));

        // Top face
        renderBarFace(matrixStack, buffer, -0.4f, yStart - 0.5f, 1f, barProgress, barHeight, r, g, b, a, pPackedLight, new Quaternionf().rotateX((float) -Math.PI / 2.0f));

        // Bottom face
        renderBarFace(matrixStack, buffer, -0.4f, yStart - 0.5f, 0f, barProgress, barHeight, r, g, b, a, pPackedLight, new Quaternionf().rotateX((float) Math.PI / 2.0f));
    }

    private void renderBarFace(PoseStack matrixStack, MultiBufferSource buffer, float xStart, float yStart, float zStart, float barWidth, float barHeight, float r, float g, float b, float a, int pPackedLight, Quaternionf rotation) {
        matrixStack.pushPose();
        matrixStack.mulPose(rotation);

        RenderHelpers.renderBoxSolid(matrixStack, matrixStack.last().pose(), buffer, new AABB(xStart, yStart, zStart, xStart + barWidth, yStart + barHeight, zStart), r, g, b, a);

        matrixStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(TimeWandEntity pEntity) {
        return null;
    }

    private void drawText(PoseStack matrixStack, MultiBufferSource source, String text, Vector3f translateVector, Quaternionf rotate, int color) {
        matrixStack.pushPose();
        matrixStack.translate(translateVector.x(), translateVector.y(), translateVector.z());
        matrixStack.scale(0.01F, -0.01F, 0.01F);
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
