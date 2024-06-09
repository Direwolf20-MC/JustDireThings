package com.direwolf20.justdirethings.client.entityrenders;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.client.entitymodels.PortalProjectileModel;
import com.direwolf20.justdirethings.common.entities.PortalProjectile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class PortalProjectileRender extends EntityRenderer<PortalProjectile> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(JustDireThings.MODID, "textures/entity/portal_projectile.png");
    protected final PortalProjectileModel model;

    public PortalProjectileRender(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.model = new PortalProjectileModel(pContext.bakeLayer(PortalProjectileModel.Portal_Projectile_Layer));
    }

    @Override
    public void render(PortalProjectile pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        pPoseStack.pushPose();
        //RenderHelpers.renderLines(pPoseStack, pEntity.getBoundingBox().move(-pEntity.getX(), -pEntity.getY(), -pEntity.getZ()), Color.BLUE, pBuffer);
        float f = Mth.rotLerp(pPartialTicks, pEntity.yRotO, pEntity.getYRot());
        float f1 = Mth.lerp(pPartialTicks, pEntity.xRotO, pEntity.getXRot());
        float f2 = (float) pEntity.tickCount + pPartialTicks;
        pPoseStack.translate(0.0D, (double) 0.15F, 0.0D);
        pPoseStack.mulPose(Axis.YP.rotationDegrees(Mth.sin(f2 * 0.1F) * 180.0F));
        pPoseStack.mulPose(Axis.XP.rotationDegrees(Mth.cos(f2 * 0.1F) * 180.0F));
        pPoseStack.mulPose(Axis.ZP.rotationDegrees(Mth.sin(f2 * 0.15F) * 360.0F));
        //pPoseStack.mulPose(Axis.YP.rotationDegrees(f2 % 360 * 20));
        //pPoseStack.mulPose(Vector3f.YP.rotationDegrees(f2 % 360 * 20));
        pPoseStack.scale(0.5f, 0.5f, 0.5f);

        this.model.setupAnim(pEntity, 0.0F, 0.0F, 0.0F, f, f1);
        VertexConsumer vertexconsumer = pBuffer.getBuffer(RenderType.entityTranslucentCull(TEXTURE));
        this.model.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1F);
        pPoseStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(PortalProjectile pEntity) {
        return TEXTURE;
    }
}
