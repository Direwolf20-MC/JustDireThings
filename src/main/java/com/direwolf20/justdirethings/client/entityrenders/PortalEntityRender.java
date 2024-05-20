package com.direwolf20.justdirethings.client.entityrenders;

import com.direwolf20.justdirethings.client.renderers.RenderHelpers;
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
import net.minecraft.world.phys.AABB;
import org.joml.Matrix4f;

import java.awt.*;

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
        Direction.Axis alignment = pEntity.getAlignment();


        if (direction.getAxis() == Direction.Axis.Z) { //North and South
            this.renderFace(pPoseStack.last().pose(), vertexConsumer, start, end, start - 1, end, 0, 0, 0, 0);
            this.renderFace(pPoseStack.last().pose(), vertexConsumer, start, end, end, start - 1, 0, 0, 0, 0);
        } else if (direction.getAxis() == Direction.Axis.X) { //East and West
            this.renderFace(pPoseStack.last().pose(), vertexConsumer, 0, 0, end, start - 1, start, end, end, start);
            this.renderFace(pPoseStack.last().pose(), vertexConsumer, 0, 0, start - 1, end, start, end, end, start);
        } else { //Top and Bottom
            if (alignment == Direction.Axis.X) {
                this.renderFace(pPoseStack.last().pose(), vertexConsumer, start - 1, end, 0, 0, start, start, end, end);
                this.renderFace(pPoseStack.last().pose(), vertexConsumer, start - 1, end, 0, 0, end, end, start, start);
            } else {
                this.renderFace(pPoseStack.last().pose(), vertexConsumer, start, end, 0, 0, start - 1, start - 1, end, end);
                this.renderFace(pPoseStack.last().pose(), vertexConsumer, start, end, 0, 0, end, end, start - 1, start - 1);
            }
        }
        Color color = pEntity.getIsPrimary() ? Color.GREEN : Color.RED;
        RenderHelpers.renderLines(pPoseStack, new AABB(pEntity.getX() - 0.05, pEntity.getY() - 0.05, pEntity.getZ() - 0.05, pEntity.getX() + 0.05, pEntity.getY() + 0.05, pEntity.getZ() + 0.05).move(-pEntity.getX(), -pEntity.getY(), -pEntity.getZ()), Color.WHITE, pBuffer);
        RenderHelpers.renderLines(pPoseStack, pEntity.getBoundingBox().move(-pEntity.getX(), -pEntity.getY(), -pEntity.getZ()), color, pBuffer);
        RenderHelpers.renderLines(pPoseStack, pEntity.getVelocityBoundingBox().move(-pEntity.getX(), -pEntity.getY(), -pEntity.getZ()), Color.BLUE, pBuffer);
        pPoseStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(T pEntity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }

    private void renderFace(Matrix4f matrixStack, VertexConsumer vertexConsumer, float x1, float x2, float y1, float y2, float z1, float z2, float z3, float z4) {
        vertexConsumer.vertex(matrixStack, x1, y1, z1).color(1f, 0f, 0f, 0.25f).endVertex();
        vertexConsumer.vertex(matrixStack, x2, y1, z2).color(1f, 0f, 0f, 0.25f).endVertex();
        vertexConsumer.vertex(matrixStack, x2, y2, z3).color(1f, 0f, 0f, 0.25f).endVertex();
        vertexConsumer.vertex(matrixStack, x1, y2, z4).color(1f, 0f, 0f, 0.25f).endVertex();
    }
}
