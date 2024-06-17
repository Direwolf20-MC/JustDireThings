package com.direwolf20.justdirethings.client.renderactions;

import com.direwolf20.justdirethings.common.items.interfaces.AbilityMethods;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

public class MiscRenders {
    public static void renderTransparentPlayer(RenderLevelStageEvent evt, Player player, ItemStack itemStack) {
        //TODO Validate Partial Ticks is correct, AND check out how to make them transparent again...
        Vec3 renderPosition = AbilityMethods.getShiftPosition(player.level(), player, itemStack);
        Vec3 projectedView = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        PoseStack matrixStack = evt.getPoseStack();
        matrixStack.pushPose();
        matrixStack.translate(-projectedView.x(), -projectedView.y(), -projectedView.z());
        // Translate to the designated position
        matrixStack.translate(renderPosition.x, renderPosition.y, renderPosition.z);

        int lightLevel = player.level().getMaxLocalRawBrightness(BlockPos.containing(renderPosition));
        int packedLight = LightTexture.pack(lightLevel, lightLevel);

        // Render the player model
        MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
        EntityRenderDispatcher renderManager = Minecraft.getInstance().getEntityRenderDispatcher();
        EntityRenderer<? super Player> renderer = renderManager.getRenderer(player);
        RenderType renderType = RenderType.itemEntityTranslucentCull(renderer.getTextureLocation(player));
        VertexConsumer vertexconsumer = buffer.getBuffer(renderType);
        int i = LivingEntityRenderer.getOverlayCoords(player, 0);
        DeltaTracker pPartialTicks = evt.getPartialTick();
        if (renderer instanceof LivingEntityRenderer<?, ?>) {
            LivingEntityRenderer<Player, ?> livingRenderer = (LivingEntityRenderer<Player, ?>) renderer;
            float f = Mth.rotLerp(pPartialTicks.getGameTimeDeltaTicks(), player.yBodyRotO, player.yBodyRot);
            float f1 = Mth.rotLerp(pPartialTicks.getGameTimeDeltaTicks(), player.yHeadRotO, player.yHeadRot);
            float f2 = f1 - f;
            float f5 = Mth.lerp(pPartialTicks.getGameTimeDeltaTicks(), player.xRotO, player.getXRot());
            float f7 = 0;
            setupRotations(player, matrixStack, f7, f, pPartialTicks.getGameTimeDeltaTicks());
            matrixStack.scale(-1.0F, -1.0F, 1.0F);
            scale(player, matrixStack, pPartialTicks.getGameTimeDeltaTicks());
            matrixStack.translate(0.0F, -1.501F, 0.0F);
            float f8 = 0.0F;
            float f4 = 0.0F;
            if (player.isAlive()) {
                f8 = player.walkAnimation.speed(pPartialTicks.getGameTimeDeltaTicks());
                f4 = player.walkAnimation.position(pPartialTicks.getGameTimeDeltaTicks());
                if (player.isBaby()) {
                    f4 *= 3.0F;
                }

                if (f8 > 1.0F) {
                    f8 = 1.0F;
                }
            }
            EntityModel<Player> entityModel = livingRenderer.getModel();
            entityModel.attackTime = 0f;
            entityModel.riding = false;
            entityModel.young = player.isBaby();
            entityModel.prepareMobModel(player, f4, f8, pPartialTicks.getGameTimeDeltaTicks());
            entityModel.setupAnim(player, f4, f8, f7, f2, f5);
            entityModel.renderToBuffer(matrixStack, vertexconsumer, packedLight, i);
        }

        matrixStack.popPose();

    }

    protected static void setupRotations(Player pEntityLiving, PoseStack pPoseStack, float pAgeInTicks, float pRotationYaw, float pPartialTicks) {
        if (!pEntityLiving.hasPose(Pose.SLEEPING)) {
            pPoseStack.mulPose(Axis.YP.rotationDegrees(180.0F - pRotationYaw));
        }
    }

    protected static void scale(Player pLivingEntity, PoseStack pPoseStack, float pPartialTickTime) {
        float f = 0.9375F;
        pPoseStack.scale(0.9375F, 0.9375F, 0.9375F);
    }
}
