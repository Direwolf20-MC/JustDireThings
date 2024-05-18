package com.direwolf20.justdirethings.client.entityrenders;

import com.direwolf20.justdirethings.common.entities.PortalEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

public class PortalEntityRender<T extends PortalEntity> extends EntityRenderer<T> {

    public PortalEntityRender(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public void render(T pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        pPoseStack.pushPose();
        float start = -0.5f;
        float end = 0.5f;

        VertexConsumer vertexConsumer = pBuffer.getBuffer(RenderType.endPortal());
        Direction direction = pEntity.getDirection();


        if (direction.getAxis() == Direction.Axis.Z) { //North and South
            this.renderFace(pPoseStack.last().pose(), vertexConsumer, start, end, start - 1, end, 0, 0, 0, 0);
            this.renderFace(pPoseStack.last().pose(), vertexConsumer, start, end, end, start - 1, 0, 0, 0, 0);
        } else if (direction.getAxis() == Direction.Axis.X) { //East and West
            this.renderFace(pPoseStack.last().pose(), vertexConsumer, 0, 0, end, start - 1, start, end, end, start);
            this.renderFace(pPoseStack.last().pose(), vertexConsumer, 0, 0, start - 1, end, start, end, end, start);
        } else { //Top and Bottom
            this.renderFace(pPoseStack.last().pose(), vertexConsumer, start - 1, end, 0, 0, start, start, end, end);
            this.renderFace(pPoseStack.last().pose(), vertexConsumer, start - 1, end, 0, 0, end, end, start, start);
        }
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
