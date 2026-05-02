package com.direwolf20.justdirethings.client.entityrenders;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.client.renderers.OurRenderTypes;
import com.direwolf20.justdirethings.client.renderers.shader.DireRenderTypes;
import com.direwolf20.justdirethings.client.renderers.shader.ShaderTexture;
import com.direwolf20.justdirethings.common.entities.PortalEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.AABB;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.List;

public class PortalEntityRender<T extends PortalEntity> extends EntityRenderer<T, PortalEntityRender.PortalEntityRenderState> {
    protected static final RenderType PORTAL_RENDER_TYPE = DireRenderTypes.getRenderType("portal_entity")
            .using(List.of(
                    new ShaderTexture(Identifier.fromNamespaceAndPath(JustDireThings.MODID, "textures/block/portal_shader.png"))
            ));

    public static class PortalEntityRenderState extends EntityRenderState {
        public int tickCount;
        public boolean dying;
        public int deathCounter;
        public boolean isPrimary;
        public Direction direction = Direction.NORTH;
        public Direction.Axis alignment = Direction.Axis.Y;
        public AABB localBounds = new AABB(0, 0, 0, 1, 1, 1);
    }

    public PortalEntityRender(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public PortalEntityRenderState createRenderState() {
        return new PortalEntityRenderState();
    }

    @Override
    public void extractRenderState(T entity, PortalEntityRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.tickCount = entity.tickCount;
        state.dying = entity.isDying();
        state.deathCounter = entity.getDeathCounter();
        state.isPrimary = entity.getIsPrimary();
        state.direction = entity.getDirection();
        state.alignment = entity.getAlignment();
        state.localBounds = entity.getBoundingBox().move(-entity.getX(), -entity.getY(), -entity.getZ());
    }

    @Override
    public void submit(PortalEntityRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        super.submit(state, poseStack, submitNodeCollector, camera);

        float partialTicks = state.partialTick;

        float startValue = 0.0f;
        float endValueStart = -0.5f;
        float endValueEnd = 0.5f;

        float startValue2 = 1.0f;
        float endValueStart2 = 0.0f;
        float endValueEnd2 = 2.0f;

        int maxTicks = PortalEntity.ANIMATION_COOLDOWN;
        int currentTick = state.tickCount;
        float progress;

        if (state.dying) {
            float deathTicks = state.deathCounter + partialTicks;
            progress = (maxTicks - Math.min(deathTicks, maxTicks)) / (float) maxTicks;
        } else {
            float totalTicks = currentTick + partialTicks;
            progress = Math.min(totalTicks / maxTicks, 1.0f);
        }

        float start = startValue + (endValueStart - startValue) * progress;
        float end = startValue + (endValueEnd - startValue) * progress;
        float start2 = startValue2 + (endValueStart2 - startValue2) * progress;
        float end2 = startValue2 + (endValueEnd2 - startValue2) * progress;

        Direction direction = state.direction;
        Direction.Axis alignment = state.alignment;
        AABB renderBounds = state.localBounds;
        Vector3f renderSpots = new Vector3f(
                renderBounds.getXsize() < 0.5 ? 0f : (float) renderBounds.getXsize(),
                renderBounds.getYsize() < 0.5 ? 0f : (float) renderBounds.getYsize(),
                renderBounds.getZsize() < 0.5 ? 0f : (float) renderBounds.getZsize());

        final float fStart = start;
        final float fEnd = end;
        final float fStart2 = start2;
        final float fEnd2 = end2;
        submitNodeCollector.submitCustomGeometry(poseStack, PORTAL_RENDER_TYPE, (pose, buffer) -> {
            Matrix4f matrix = pose.pose();
            if (direction.getAxis() == Direction.Axis.Z) {
                renderFace(matrix, buffer, fStart, fEnd, fStart2, fEnd2, 0, 0, 0, 0);
                renderFace(matrix, buffer, fStart, fEnd, fEnd2, fStart2, 0, 0, 0, 0);
            } else if (direction.getAxis() == Direction.Axis.X) {
                renderFace(matrix, buffer, 0, 0, fEnd2, fStart2, fStart, fEnd, fEnd, fStart);
                renderFace(matrix, buffer, 0, 0, fStart2, fEnd2, fStart, fEnd, fEnd, fStart);
            } else {
                if (alignment == Direction.Axis.X) {
                    renderFace(matrix, buffer, fStart2, fEnd2, 0, 0, fStart, fStart, fEnd, fEnd);
                    renderFace(matrix, buffer, fStart2, fEnd2, 0, 0, fEnd, fEnd, fStart, fStart);
                } else {
                    renderFace(matrix, buffer, fStart, fEnd, 0, 0, fStart2, fStart2, fEnd2, fEnd2);
                    renderFace(matrix, buffer, fStart, fEnd, 0, 0, fEnd2, fEnd2, fStart2, fStart2);
                }
            }
        });

        float r = state.isPrimary ? 0.0f : 1.0f;
        float g = state.isPrimary ? 0.6f : 0.647f;
        float b = state.isPrimary ? 1.0f : 0.0f;
        float alpha = 1.0f;
        // Submit the frame at order(1) so it draws strictly after order(0)'s translucent custom geometry
        // (the portal swirl). Without this, the swirl and frame both land in order(0)'s HashMap-keyed
        // custom-geometry storage and the per-launch iteration order randomly buries one or the other.
        submitNodeCollector.order(1).submitCustomGeometry(poseStack, OurRenderTypes.PORTAL_FRAME, (pose, buffer) -> {
            Matrix4f matrix = pose.pose();
            renderFlatFrame(matrix, buffer, renderBounds, 0.025f, r, g, b, alpha, direction.getAxis());
        });
    }

    private void renderFlatFrame(Matrix4f matrix, VertexConsumer buffer, AABB aabb, float thickness, float r, float g, float b, float alpha, Direction.Axis alignment) {
        float minX = (float) aabb.minX;
        float minY = (float) aabb.minY;
        float minZ = (float) aabb.minZ;
        float maxX = (float) aabb.maxX;
        float maxY = (float) aabb.maxY;
        float maxZ = (float) aabb.maxZ;

        if (alignment == Direction.Axis.Z) {
            float midZ = (minZ + maxZ) / 2f;
            renderBox(matrix, buffer, minX, minY, midZ, minX + thickness, maxY, midZ, r, g, b, alpha);
            renderBox(matrix, buffer, maxX - thickness, minY, midZ, maxX, maxY, midZ, r, g, b, alpha);
            renderBox(matrix, buffer, minX, minY, midZ, maxX, minY + thickness, midZ, r, g, b, alpha);
            renderBox(matrix, buffer, minX, maxY - thickness, midZ, maxX, maxY, midZ, r, g, b, alpha);
        } else if (alignment == Direction.Axis.X) {
            float midX = (minX + maxX) / 2f;
            renderBox(matrix, buffer, midX, minY, minZ, midX, maxY, minZ + thickness, r, g, b, alpha);
            renderBox(matrix, buffer, midX, minY, maxZ - thickness, midX, maxY, maxZ, r, g, b, alpha);
            renderBox(matrix, buffer, midX, minY, minZ, midX, minY + thickness, maxZ, r, g, b, alpha);
            renderBox(matrix, buffer, midX, maxY - thickness, minZ, midX, maxY, maxZ, r, g, b, alpha);
        } else {
            float midY = (minY + maxY) / 2f;
            renderBox(matrix, buffer, minX, midY, minZ, minX + thickness, midY, maxZ, r, g, b, alpha);
            renderBox(matrix, buffer, maxX - thickness, midY, minZ, maxX, midY, maxZ, r, g, b, alpha);
            renderBox(matrix, buffer, minX, midY, minZ, maxX, midY, minZ + thickness, r, g, b, alpha);
            renderBox(matrix, buffer, minX, midY, maxZ - thickness, maxX, midY, maxZ, r, g, b, alpha);
        }
    }

    private void renderBox(Matrix4f matrix, VertexConsumer builder, float x, float y, float z, float xEnd, float yEnd, float zEnd, float red, float green, float blue, float alpha) {
        //down
        builder.addVertex(matrix, x, y, z).setColor(red, green, blue, alpha);
        builder.addVertex(matrix, xEnd, y, z).setColor(red, green, blue, alpha);
        builder.addVertex(matrix, xEnd, y, zEnd).setColor(red, green, blue, alpha);
        builder.addVertex(matrix, x, y, zEnd).setColor(red, green, blue, alpha);
        //up
        builder.addVertex(matrix, x, yEnd, z).setColor(red, green, blue, alpha);
        builder.addVertex(matrix, x, yEnd, zEnd).setColor(red, green, blue, alpha);
        builder.addVertex(matrix, xEnd, yEnd, zEnd).setColor(red, green, blue, alpha);
        builder.addVertex(matrix, xEnd, yEnd, z).setColor(red, green, blue, alpha);
        //east
        builder.addVertex(matrix, x, y, z).setColor(red, green, blue, alpha);
        builder.addVertex(matrix, x, yEnd, z).setColor(red, green, blue, alpha);
        builder.addVertex(matrix, xEnd, yEnd, z).setColor(red, green, blue, alpha);
        builder.addVertex(matrix, xEnd, y, z).setColor(red, green, blue, alpha);
        //west
        builder.addVertex(matrix, x, y, zEnd).setColor(red, green, blue, alpha);
        builder.addVertex(matrix, xEnd, y, zEnd).setColor(red, green, blue, alpha);
        builder.addVertex(matrix, xEnd, yEnd, zEnd).setColor(red, green, blue, alpha);
        builder.addVertex(matrix, x, yEnd, zEnd).setColor(red, green, blue, alpha);
        //south
        builder.addVertex(matrix, xEnd, y, z).setColor(red, green, blue, alpha);
        builder.addVertex(matrix, xEnd, yEnd, z).setColor(red, green, blue, alpha);
        builder.addVertex(matrix, xEnd, yEnd, zEnd).setColor(red, green, blue, alpha);
        builder.addVertex(matrix, xEnd, y, zEnd).setColor(red, green, blue, alpha);
        //north
        builder.addVertex(matrix, x, y, z).setColor(red, green, blue, alpha);
        builder.addVertex(matrix, x, y, zEnd).setColor(red, green, blue, alpha);
        builder.addVertex(matrix, x, yEnd, zEnd).setColor(red, green, blue, alpha);
        builder.addVertex(matrix, x, yEnd, z).setColor(red, green, blue, alpha);
    }

    private void renderFace(Matrix4f matrixStack, VertexConsumer vertexConsumer, float x1, float x2, float y1, float y2, float z1, float z2, float z3, float z4) {
        vertexConsumer.addVertex(matrixStack, x1, y1, z1).setUv(0f, 0f);
        vertexConsumer.addVertex(matrixStack, x2, y1, z2).setUv(1f, 0f);
        vertexConsumer.addVertex(matrixStack, x2, y2, z3).setUv(1f, 1f);
        vertexConsumer.addVertex(matrixStack, x1, y2, z4).setUv(0f, 1f);
    }
}
