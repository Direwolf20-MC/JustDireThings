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
import net.minecraft.world.phys.AABB;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class PortalEntityRender<T extends PortalEntity> extends EntityRenderer<T> {
    public PortalEntityRender(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public void render(T pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        pPoseStack.pushPose();
        float startValue = 0.0f;
        float endValueStart = -0.5f;
        float endValueEnd = 0.5f;

        float startValue2 = 1.0f;
        float endValueStart2 = 0.0f;
        float endValueEnd2 = 2.0f;

        int maxTicks = PortalEntity.ANIMATION_COOLDOWN;
        int currentTick = pEntity.tickCount;
        float progress;

        if (pEntity.isDying()) {
            int deathCounter = pEntity.getDeathCounter();
            // Calculate the reverse progress factor (1.0 to 0.0)
            float deathTicks = deathCounter + pPartialTicks;
            progress = (maxTicks - Math.min(deathTicks, maxTicks)) / maxTicks;
        } else {
            // Calculate the forward progress factor (0.0 to 1.0)
            float totalTicks = currentTick + pPartialTicks;
            progress = Math.min(totalTicks / maxTicks, 1.0f);
        }

        // Interpolate values
        float interpolatedStart = startValue + (endValueStart - startValue) * progress;
        float interpolatedEnd = startValue + (endValueEnd - startValue) * progress;

        float start = interpolatedStart;
        float end = interpolatedEnd;

        float interpolatedStart2 = startValue2 + (endValueStart2 - startValue2) * progress;
        float interpolatedEnd2 = startValue2 + (endValueEnd2 - startValue2) * progress;

        float start2 = interpolatedStart2;
        float end2 = interpolatedEnd2;

        VertexConsumer vertexConsumer = pBuffer.getBuffer(RenderType.endPortal());
        Direction direction = pEntity.getDirection();
        Direction.Axis alignment = pEntity.getAlignment();

        AABB renderBounds = pEntity.getBoundingBox().move(-pEntity.getX(), -pEntity.getY(), -pEntity.getZ());
        Vector3f renderSpots = new Vector3f(renderBounds.getXsize() < 0.5 ? 0f : (float) renderBounds.getXsize(),
                renderBounds.getYsize() < 0.5 ? 0f : (float) renderBounds.getYsize(),
                renderBounds.getZsize() < 0.5 ? 0f : (float) renderBounds.getZsize());

        //this.renderFace(pPoseStack.last().pose(), vertexConsumer, -renderSpots.x/2, renderSpots.x/2, -renderSpots.y/2, renderSpots.y/2, -renderSpots.z/2, renderSpots.z/2, -renderSpots.z/2, renderSpots.z/2);
        //this.renderFace(pPoseStack.last().pose(), vertexConsumer, -renderSpots.x/2, renderSpots.x/2, -renderSpots.y/2, renderSpots.y/2, -renderSpots.z/2, renderSpots.z/2, -renderSpots.z/2, renderSpots.z/2);

        if (direction.getAxis() == Direction.Axis.Z) { //North and South
            this.renderFace(pPoseStack.last().pose(), vertexConsumer, start, end, start2, end2, 0, 0, 0, 0);
            this.renderFace(pPoseStack.last().pose(), vertexConsumer, start, end, end2, start2, 0, 0, 0, 0);
        } else if (direction.getAxis() == Direction.Axis.X) { //East and West
            this.renderFace(pPoseStack.last().pose(), vertexConsumer, 0, 0, end2, start2, start, end, end, start);
            this.renderFace(pPoseStack.last().pose(), vertexConsumer, 0, 0, start2, end2, start, end, end, start);
        } else { //Top and Bottom
            if (alignment == Direction.Axis.X) {
                this.renderFace(pPoseStack.last().pose(), vertexConsumer, start2, end2, 0, 0, start, start, end, end);
                this.renderFace(pPoseStack.last().pose(), vertexConsumer, start2, end2, 0, 0, end, end, start, start);
            } else {
                this.renderFace(pPoseStack.last().pose(), vertexConsumer, start, end, 0, 0, start2, start2, end2, end2);
                this.renderFace(pPoseStack.last().pose(), vertexConsumer, start, end, 0, 0, end2, end2, start2, start2);
            }
        }

        //Color color = pEntity.getIsPrimary() ? Color.GREEN : Color.RED;
        //RenderHelpers.renderLines(pPoseStack, new AABB(pEntity.getX() - 0.05, pEntity.getY() - 0.05, pEntity.getZ() - 0.05, pEntity.getX() + 0.05, pEntity.getY() + 0.05, pEntity.getZ() + 0.05).move(-pEntity.getX(), -pEntity.getY(), -pEntity.getZ()), Color.WHITE, pBuffer);
        //RenderHelpers.renderLines(pPoseStack, renderBounds, color, pBuffer);
        //RenderHelpers.renderLines(pPoseStack, pEntity.getVelocityBoundingBox().move(-pEntity.getX(), -pEntity.getY(), -pEntity.getZ()), Color.BLUE, pBuffer);
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
