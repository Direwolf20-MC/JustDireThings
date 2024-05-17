package com.direwolf20.justdirethings.client.entityrenders;

import com.direwolf20.justdirethings.common.entities.PortalEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

public class PortalEntityRender<T extends PortalEntity> extends EntityRenderer<T> {

    public PortalEntityRender(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public void render(T pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        pPoseStack.pushPose();

        // Translate to the entity's position
        BlockPos blockPos = pEntity.blockPosition();
        //pPoseStack.translate(blockPos.getX() - 0.5, blockPos.getY(), blockPos.getZ() - 0.5);

        // Prepare the buffer for rendering the end portal effect
        VertexConsumer vertexConsumer = pBuffer.getBuffer(RenderType.endPortal());

        // Render the portal effect for each of the two blocks
        //for (int y = 0; y < 2; y++) {
        pPoseStack.pushPose();
        //pPoseStack.translate(0, y, 0);
        //North and South
        this.renderFace(pPoseStack.last().pose(), vertexConsumer, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
        this.renderFace(pPoseStack.last().pose(), vertexConsumer, 0.0F, 1.0F, 1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F);
        //this.renderFace(pPoseStack.last().pose(), vertexConsumer, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F);
        //this.renderFace(pPoseStack.last().pose(), vertexConsumer, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F);
        //this.renderFace(pPoseStack.last().pose(), vertexConsumer, 0.0F, 1.0F, 0.375F, 0.375F, 0.0F, 0.0F, 1.0F, 1.0F);
        //this.renderFace(pPoseStack.last().pose(), vertexConsumer, 0.0F, 1.0F, 0.75F, 0.75F, 1.0F, 1.0F, 0.0F, 0.0F);
        //renderFace(pPoseStack.last().pose(), vertexConsumer, 0, 1, 0, 1, 0, 1, 1, 1);
        pPoseStack.popPose();
        //}

        pPoseStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(T pEntity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }

    private void renderFace(Matrix4f matrixStack, VertexConsumer vertexConsumer, float x1, float x2, float y1, float y2, float z1, float z2, float z3, float z4) {
        vertexConsumer.vertex(matrixStack, x1, y1, z1).endVertex();
        vertexConsumer.vertex(matrixStack, x2, y1, z2).endVertex();
        vertexConsumer.vertex(matrixStack, x2, y2, z3).endVertex();
        vertexConsumer.vertex(matrixStack, x1, y2, z4).endVertex();
    }
}
