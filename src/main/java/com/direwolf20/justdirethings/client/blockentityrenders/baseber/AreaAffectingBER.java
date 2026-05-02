package com.direwolf20.justdirethings.client.blockentityrenders.baseber;

import com.direwolf20.justdirethings.client.renderers.OurRenderTypes;
import com.direwolf20.justdirethings.common.blockentities.basebe.AreaAffectingBE;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.awt.*;

public class AreaAffectingBER<T extends BlockEntity, S extends AreaAffectingBER.AreaAffectingRenderState>
        implements BlockEntityRenderer<T, S> {

    public static class AreaAffectingRenderState extends BlockEntityRenderState {
        public boolean renderArea;
        public @Nullable AABB aabb;
        public @Nullable AABB aabbOffsetOnly;
        public boolean hasOffset;
    }

    @Override
    @SuppressWarnings("unchecked")
    public S createRenderState() {
        return (S) new AreaAffectingRenderState();
    }

    @Override
    public void extractRenderState(T blockEntity, S state, float partialTicks, Vec3 cameraPosition,
                                    ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        extractAreaAffecting(blockEntity, state);
    }

    protected static void extractAreaAffecting(BlockEntity blockEntity, AreaAffectingRenderState state) {
        if (blockEntity instanceof AreaAffectingBE areaAffectingBE) {
            state.renderArea = areaAffectingBE.getAreaAffectingData().renderArea;
            state.aabb = areaAffectingBE.getAABB(BlockPos.ZERO);
            state.aabbOffsetOnly = areaAffectingBE.getAABBOffsetOnly(BlockPos.ZERO);
            state.hasOffset = areaAffectingBE.getAreaAffectingData().xRadius > 0
                    || areaAffectingBE.getAreaAffectingData().yRadius > 0
                    || areaAffectingBE.getAreaAffectingData().zRadius > 0;
        } else {
            state.renderArea = false;
        }
    }

    @Override
    public void submit(S state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        if (!state.renderArea || state.aabb == null) return;
        final AABB primary = state.aabb;
        submitNodeCollector.submitCustomGeometry(poseStack, OurRenderTypes.lines(),
                (pose, buffer) -> drawAABBLines(pose, buffer, primary, Color.GREEN));
        submitNodeCollector.submitCustomGeometry(poseStack, OurRenderTypes.TRANSPARENT_BOX,
                (pose, buffer) -> drawAABBSolid(pose, buffer, primary, 1F, 0F, 0F, 0.125F));
        if (state.hasOffset && state.aabbOffsetOnly != null) {
            final AABB offset = state.aabbOffsetOnly;
            submitNodeCollector.submitCustomGeometry(poseStack, OurRenderTypes.lines(),
                    (pose, buffer) -> drawAABBLines(pose, buffer, offset, Color.WHITE));
            submitNodeCollector.submitCustomGeometry(poseStack, OurRenderTypes.TRANSPARENT_BOX,
                    (pose, buffer) -> drawAABBSolid(pose, buffer, offset, 0F, 0F, 1F, 0.125F));
        }
    }

    protected static void drawAABBLines(PoseStack.Pose pose, VertexConsumer buffer, AABB aabb, Color color) {
        float x = (float) aabb.minX;
        float y = (float) aabb.minY;
        float z = (float) aabb.minZ;
        float dx = (float) aabb.maxX;
        float dy = (float) aabb.maxY;
        float dz = (float) aabb.maxZ;
        int c = color.getRGB();
        edge(pose, buffer, c, x, y, z, dx, y, z, 1F, 0F, 0F);
        edge(pose, buffer, c, x, y, z, x, dy, z, 0F, 1F, 0F);
        edge(pose, buffer, c, x, y, z, x, y, dz, 0F, 0F, 1F);
        edge(pose, buffer, c, dx, y, z, dx, dy, z, 0F, 1F, 0F);
        edge(pose, buffer, c, dx, dy, z, x, dy, z, -1F, 0F, 0F);
        edge(pose, buffer, c, x, dy, z, x, dy, dz, 0F, 0F, 1F);
        edge(pose, buffer, c, x, dy, dz, x, y, dz, 0F, -1F, 0F);
        edge(pose, buffer, c, x, y, dz, dx, y, dz, 1F, 0F, 0F);
        edge(pose, buffer, c, dx, y, dz, dx, y, z, 0F, 0F, -1F);
        edge(pose, buffer, c, x, dy, dz, dx, dy, dz, 1F, 0F, 0F);
        edge(pose, buffer, c, dx, y, dz, dx, dy, dz, 0F, 1F, 0F);
        edge(pose, buffer, c, dx, dy, z, dx, dy, dz, 0F, 0F, 1F);
    }

    private static void edge(PoseStack.Pose pose, VertexConsumer buffer, int color,
                             float x0, float y0, float z0, float x1, float y1, float z1,
                             float nx, float ny, float nz) {
        buffer.addVertex(pose.pose(), x0, y0, z0).setColor(color).setNormal(pose, nx, ny, nz).setLineWidth(2.0F);
        buffer.addVertex(pose.pose(), x1, y1, z1).setColor(color).setNormal(pose, nx, ny, nz).setLineWidth(2.0F);
    }

    public static void drawAABBSolid(PoseStack.Pose pose, VertexConsumer buffer, AABB aabb,
                                     float r, float g, float b, float alpha) {
        float sx = (float) aabb.minX;
        float sy = (float) aabb.minY;
        float sz = (float) aabb.minZ;
        float ex = (float) aabb.maxX;
        float ey = (float) aabb.maxY;
        float ez = (float) aabb.maxZ;
        // Emit a POSITION_COLOR quad per face (TRANSPARENT_BOX uses DEBUG_QUADS pipeline → POSITION_COLOR only).
        // Down
        quad(pose, buffer, r, g, b, alpha, sx, sy, sz, ex, sy, sz, ex, sy, ez, sx, sy, ez);
        // Up
        quad(pose, buffer, r, g, b, alpha, sx, ey, sz, sx, ey, ez, ex, ey, ez, ex, ey, sz);
        // East (+X)
        quad(pose, buffer, r, g, b, alpha, sx, sy, sz, sx, ey, sz, ex, ey, sz, ex, sy, sz);
        // West (-X)
        quad(pose, buffer, r, g, b, alpha, sx, sy, ez, ex, sy, ez, ex, ey, ez, sx, ey, ez);
        // South (+Z)
        quad(pose, buffer, r, g, b, alpha, ex, sy, sz, ex, ey, sz, ex, ey, ez, ex, sy, ez);
        // North (-Z)
        quad(pose, buffer, r, g, b, alpha, sx, sy, sz, sx, sy, ez, sx, ey, ez, sx, ey, sz);
    }

    private static void quad(PoseStack.Pose pose, VertexConsumer buffer,
                             float r, float g, float b, float alpha,
                             float x0, float y0, float z0,
                             float x1, float y1, float z1,
                             float x2, float y2, float z2,
                             float x3, float y3, float z3) {
        buffer.addVertex(pose.pose(), x0, y0, z0).setColor(r, g, b, alpha);
        buffer.addVertex(pose.pose(), x1, y1, z1).setColor(r, g, b, alpha);
        buffer.addVertex(pose.pose(), x2, y2, z2).setColor(r, g, b, alpha);
        buffer.addVertex(pose.pose(), x3, y3, z3).setColor(r, g, b, alpha);
    }

    @Override
    public AABB getRenderBoundingBox(T blockEntity) {
        return AABB.encapsulatingFullBlocks(blockEntity.getBlockPos().above(10).north(10).east(10),
                blockEntity.getBlockPos().below(10).south(10).west(10));
    }
}
