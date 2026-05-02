package com.direwolf20.justdirethings.client.entityrenders;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.client.entitymodels.PortalProjectileModel;
import com.direwolf20.justdirethings.common.entities.PortalProjectile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

public class PortalProjectileRender extends EntityRenderer<PortalProjectile, PortalProjectileRender.PortalProjectileRenderState> {
    protected static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(JustDireThings.MODID, "textures/entity/portal_projectile.png");
    protected final PortalProjectileModel model;

    public static class PortalProjectileRenderState extends EntityRenderState {
        public float yRot;
        public float xRot;
        public float ageTicks;
    }

    public PortalProjectileRender(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.model = new PortalProjectileModel(pContext.bakeLayer(PortalProjectileModel.Portal_Projectile_Layer));
    }

    @Override
    public PortalProjectileRenderState createRenderState() {
        return new PortalProjectileRenderState();
    }

    @Override
    public void extractRenderState(PortalProjectile entity, PortalProjectileRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.yRot = Mth.rotLerp(partialTicks, entity.yRotO, entity.getYRot());
        state.xRot = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
        state.ageTicks = entity.tickCount + partialTicks;
    }

    @Override
    public void submit(PortalProjectileRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();
        float f2 = state.ageTicks;
        poseStack.translate(0.0D, 0.15F, 0.0D);
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.sin(f2 * 0.1F) * 180.0F));
        poseStack.mulPose(Axis.XP.rotationDegrees(Mth.cos(f2 * 0.1F) * 180.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.sin(f2 * 0.15F) * 360.0F));
        poseStack.scale(0.5f, 0.5f, 0.5f);

        this.model.setupAnim(state);
        submitNodeCollector.submitModel(
                this.model, state, poseStack,
                RenderTypes.entityTranslucent(TEXTURE),
                state.lightCoords, OverlayTexture.NO_OVERLAY,
                state.outlineColor, null);

        poseStack.popPose();
        super.submit(state, poseStack, submitNodeCollector, camera);
    }
}
