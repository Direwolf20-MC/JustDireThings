package com.direwolf20.justdirethings.client.renderactions;

import com.direwolf20.justdirethings.common.items.interfaces.AbilityMethods;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

public class MiscRenders {
    public static void renderTransparentPlayer(RenderLevelStageEvent evt, Player player, ItemStack itemStack) {
        Vec3 renderPosition = AbilityMethods.getShiftPosition(player.level(), player, itemStack);
        Vec3 projectedView = Minecraft.getInstance().gameRenderer.getMainCamera().position();
        PoseStack matrixStack = evt.getPoseStack();
        matrixStack.pushPose();
        matrixStack.translate(-projectedView.x(), -projectedView.y(), -projectedView.z());
        matrixStack.translate(renderPosition.x, renderPosition.y, renderPosition.z);

        int lightLevel = player.level().getMaxLocalRawBrightness(BlockPos.containing(renderPosition));
        int packedLight = LightCoordsUtil.pack(lightLevel, lightLevel);

        MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
        EntityRenderDispatcher renderManager = Minecraft.getInstance().getEntityRenderDispatcher();
        EntityRenderer<? super Player, ?> renderer = renderManager.getRenderer(player);
        if (!(renderer instanceof LivingEntityRenderer<?, ?, ?> livingRenderer)) {
            matrixStack.popPose();
            return;
        }

        float partialTick = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(false);
        renderLivingTransparent(livingRenderer, player, matrixStack, buffer, packedLight, partialTick);

        matrixStack.popPose();
        buffer.endBatch();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static <T extends Player, S extends LivingEntityRenderState, M extends EntityModel<? super S>>
            void renderLivingTransparent(LivingEntityRenderer<?, ?, ?> rendererRaw, Player player, PoseStack matrixStack,
                                         MultiBufferSource.BufferSource buffer, int packedLight, float partialTick) {
        LivingEntityRenderer<T, S, M> renderer = (LivingEntityRenderer<T, S, M>) rendererRaw;
        S state = renderer.createRenderState();
        renderer.extractRenderState((T) player, state, partialTick);

        matrixStack.pushPose();
        setupRotations(player, matrixStack, state.bodyRot);
        matrixStack.scale(-1.0F, -1.0F, 1.0F);
        matrixStack.translate(0.0F, -1.501F, 0.0F);

        M model = renderer.getModel();
        model.setupAnim(state);

        Identifier texture = renderer.getTextureLocation(state);
        int tintedColor = ARGB.color(127, 255, 255, 255);
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderTypes.entityTranslucent(texture));
        model.renderToBuffer(matrixStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, tintedColor);

        matrixStack.popPose();
    }

    protected static void setupRotations(Player entity, PoseStack poseStack, float bodyRot) {
        if (!entity.hasPose(Pose.SLEEPING)) {
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - bodyRot));
        }
    }
}
