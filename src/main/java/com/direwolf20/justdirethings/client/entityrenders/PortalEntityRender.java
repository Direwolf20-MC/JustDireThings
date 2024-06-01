package com.direwolf20.justdirethings.client.entityrenders;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.client.renderers.shader.DireRenderTypes;
import com.direwolf20.justdirethings.client.renderers.shader.ShaderTexture;
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

import java.awt.*;
import java.util.List;

import static com.direwolf20.justdirethings.client.renderers.RenderHelpers.renderBoxSolid;

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

        VertexConsumer vertexConsumer = pBuffer.getBuffer(this.renderType());
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

        Color lightBlue = new Color(0, 153, 255, 255); // The last value is for alpha (opacity)
        Color orangePortalColor = new Color(255, 165, 0, 255);
        Color color = pEntity.getIsPrimary() ? lightBlue : orangePortalColor;
        renderFlatFrame(pPoseStack, pBuffer, renderBounds, 0.025f, color, direction.getAxis());
        //RenderHelpers.renderLines(pPoseStack, new AABB(pEntity.getX() - 0.05, pEntity.getY() - 0.05, pEntity.getZ() - 0.05, pEntity.getX() + 0.05, pEntity.getY() + 0.05, pEntity.getZ() + 0.05).move(-pEntity.getX(), -pEntity.getY(), -pEntity.getZ()), Color.WHITE, pBuffer);
        //RenderHelpers.renderLines(pPoseStack, renderBounds, color, pBuffer);
        //RenderHelpers.renderLines(pPoseStack, pEntity.getVelocityBoundingBox().move(-pEntity.getX(), -pEntity.getY(), -pEntity.getZ()), Color.BLUE, pBuffer);
        pPoseStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }

    // New method to render a thick border
    private void renderFlatFrame(PoseStack matrix, MultiBufferSource buffer, AABB aabb, float thickness, Color color, Direction.Axis alignment) {
        float minX = (float) aabb.minX;
        float minY = (float) aabb.minY;
        float minZ = (float) aabb.minZ;
        float maxX = (float) aabb.maxX;
        float maxY = (float) aabb.maxY;
        float maxZ = (float) aabb.maxZ;

        float r = color.getRed() / 255.0f;
        float g = color.getGreen() / 255.0f;
        float b = color.getBlue() / 255.0f;
        float alpha = color.getAlpha() / 255.0f;

        if (alignment == Direction.Axis.Z) {
            // Render the frame on the Z plane (North-South)
            // Left border
            renderBoxSolid(matrix.last().pose(), buffer, new AABB(minX, minY, (minZ + maxZ) / 2, minX + thickness, maxY, (minZ + maxZ) / 2), r, g, b, alpha);
            // Right border
            renderBoxSolid(matrix.last().pose(), buffer, new AABB(maxX - thickness, minY, (minZ + maxZ) / 2, maxX, maxY, (minZ + maxZ) / 2), r, g, b, alpha);
            // Bottom border
            renderBoxSolid(matrix.last().pose(), buffer, new AABB(minX, minY, (minZ + maxZ) / 2, maxX, minY + thickness, (minZ + maxZ) / 2), r, g, b, alpha);
            // Top border
            renderBoxSolid(matrix.last().pose(), buffer, new AABB(minX, maxY - thickness, (minZ + maxZ) / 2, maxX, maxY, (minZ + maxZ) / 2), r, g, b, alpha);
        } else if (alignment == Direction.Axis.X) {
            // Render the frame on the X plane (East-West)
            // Left border
            renderBoxSolid(matrix.last().pose(), buffer, new AABB((minX + maxX) / 2, minY, minZ, (minX + maxX) / 2, maxY, minZ + thickness), r, g, b, alpha);
            // Right border
            renderBoxSolid(matrix.last().pose(), buffer, new AABB((minX + maxX) / 2, minY, maxZ - thickness, (minX + maxX) / 2, maxY, maxZ), r, g, b, alpha);
            // Bottom border
            renderBoxSolid(matrix.last().pose(), buffer, new AABB((minX + maxX) / 2, minY, minZ, (minX + maxX) / 2, minY + thickness, maxZ), r, g, b, alpha);
            // Top border
            renderBoxSolid(matrix.last().pose(), buffer, new AABB((minX + maxX) / 2, maxY - thickness, minZ, (minX + maxX) / 2, maxY, maxZ), r, g, b, alpha);
        } else {
            // Render the frame on the Y plane (Top-Bottom)
            // Left border
            renderBoxSolid(matrix.last().pose(), buffer, new AABB(minX, minY, minZ, minX + thickness, minY, maxZ), r, g, b, alpha);
            // Right border
            renderBoxSolid(matrix.last().pose(), buffer, new AABB(maxX - thickness, minY, minZ, maxX, minY, maxZ), r, g, b, alpha);
            // Front border
            renderBoxSolid(matrix.last().pose(), buffer, new AABB(minX, minY, minZ, maxX, minY, minZ + thickness), r, g, b, alpha);
            // Back border
            renderBoxSolid(matrix.last().pose(), buffer, new AABB(minX, minY, maxZ - thickness, maxX, minY, maxZ), r, g, b, alpha);
        }
    }

    @Override
    public ResourceLocation getTextureLocation(T pEntity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }

    private void renderFace(Matrix4f matrixStack, VertexConsumer vertexConsumer, float x1, float x2, float y1, float y2, float z1, float z2, float z3, float z4) {
//        vertexConsumer.vertex(matrixStack, x1, y1, z1).color(1f, 0f, 0f, 0.25f).endVertex();
//        vertexConsumer.vertex(matrixStack, x2, y1, z2).color(1f, 0f, 0f, 0.25f).endVertex();
//        vertexConsumer.vertex(matrixStack, x2, y2, z3).color(1f, 0f, 0f, 0.25f).endVertex();
//        vertexConsumer.vertex(matrixStack, x1, y2, z4).color(1f, 0f, 0f, 0.25f).endVertex();
        vertexConsumer.vertex(matrixStack, x1, y1, z1).uv(0f, 0f).endVertex();
        vertexConsumer.vertex(matrixStack, x2, y1, z2).uv(1f, 0f).endVertex();
        vertexConsumer.vertex(matrixStack, x2, y2, z3).uv(1f, 1f).endVertex();
        vertexConsumer.vertex(matrixStack, x1, y2, z4).uv(0f, 1f).endVertex();
    }

    protected RenderType renderType() {
        return DireRenderTypes.getRenderType("portal_entity")
                .using(List.of(
                        new ShaderTexture(new ResourceLocation(JustDireThings.MODID,"textures/block/portal_shader.png"))
                ));
    }
}
