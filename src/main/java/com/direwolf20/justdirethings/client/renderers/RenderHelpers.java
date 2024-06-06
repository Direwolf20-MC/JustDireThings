package com.direwolf20.justdirethings.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import org.joml.Matrix4f;

import java.awt.*;

public class RenderHelpers {

    public static void renderLines(PoseStack matrix, BlockPos startPos, BlockPos endPos, Color color, MultiBufferSource buffer) {
        //We want to draw from the starting position to the (ending position)+1
        int x = Math.min(startPos.getX(), endPos.getX()), y = Math.min(startPos.getY(), endPos.getY()), z = Math.min(startPos.getZ(), endPos.getZ());

        int dx = (startPos.getX() > endPos.getX()) ? startPos.getX() + 1 : endPos.getX() + 1;
        int dy = (startPos.getY() > endPos.getY()) ? startPos.getY() + 1 : endPos.getY() + 1;
        int dz = (startPos.getZ() > endPos.getZ()) ? startPos.getZ() + 1 : endPos.getZ() + 1;

        VertexConsumer builder = buffer.getBuffer(OurRenderTypes.lines());

        matrix.pushPose();
        Matrix4f matrix4f = matrix.last().pose();
        PoseStack.Pose matrix3f = matrix.last();
        int colorRGB = color.getRGB();

        builder.vertex(matrix4f, x, y, z).color(colorRGB).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
        builder.vertex(matrix4f, dx, y, z).color(colorRGB).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
        builder.vertex(matrix4f, x, y, z).color(colorRGB).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
        builder.vertex(matrix4f, x, dy, z).color(colorRGB).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
        builder.vertex(matrix4f, x, y, z).color(colorRGB).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
        builder.vertex(matrix4f, x, y, dz).color(colorRGB).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
        builder.vertex(matrix4f, dx, y, z).color(colorRGB).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
        builder.vertex(matrix4f, dx, dy, z).color(colorRGB).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
        builder.vertex(matrix4f, dx, dy, z).color(colorRGB).normal(matrix3f, -1.0F, 0.0F, 0.0F).endVertex();
        builder.vertex(matrix4f, x, dy, z).color(colorRGB).normal(matrix3f, -1.0F, 0.0F, 0.0F).endVertex();
        builder.vertex(matrix4f, x, dy, z).color(colorRGB).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
        builder.vertex(matrix4f, x, dy, dz).color(colorRGB).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
        builder.vertex(matrix4f, x, dy, dz).color(colorRGB).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
        builder.vertex(matrix4f, x, y, dz).color(colorRGB).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
        builder.vertex(matrix4f, x, y, dz).color(colorRGB).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
        builder.vertex(matrix4f, dx, y, dz).color(colorRGB).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
        builder.vertex(matrix4f, dx, y, dz).color(colorRGB).normal(matrix3f, 0.0F, 0.0F, -1.0F).endVertex();
        builder.vertex(matrix4f, dx, y, z).color(colorRGB).normal(matrix3f, 0.0F, 0.0F, -1.0F).endVertex();
        builder.vertex(matrix4f, x, dy, dz).color(colorRGB).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
        builder.vertex(matrix4f, dx, dy, dz).color(colorRGB).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
        builder.vertex(matrix4f, dx, y, dz).color(colorRGB).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
        builder.vertex(matrix4f, dx, dy, dz).color(colorRGB).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
        builder.vertex(matrix4f, dx, dy, z).color(colorRGB).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
        builder.vertex(matrix4f, dx, dy, dz).color(colorRGB).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();

        matrix.popPose();
    }

    public static void renderLines(PoseStack matrix, AABB aabb, Color color, MultiBufferSource buffer) {
        //We want to draw from the starting position to the (ending position)+1
        float x = (float) aabb.minX;
        float y = (float) aabb.minY;
        float z = (float) aabb.minZ;
        float dx = (float) aabb.maxX;
        float dy = (float) aabb.maxY;
        float dz = (float) aabb.maxZ;

        VertexConsumer builder = buffer.getBuffer(OurRenderTypes.lines());

        matrix.pushPose();
        Matrix4f matrix4f = matrix.last().pose();
        PoseStack.Pose matrix3f = matrix.last();
        int colorRGB = color.getRGB();

        builder.vertex(matrix4f, x, y, z).color(colorRGB).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
        builder.vertex(matrix4f, dx, y, z).color(colorRGB).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
        builder.vertex(matrix4f, x, y, z).color(colorRGB).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
        builder.vertex(matrix4f, x, dy, z).color(colorRGB).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
        builder.vertex(matrix4f, x, y, z).color(colorRGB).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
        builder.vertex(matrix4f, x, y, dz).color(colorRGB).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
        builder.vertex(matrix4f, dx, y, z).color(colorRGB).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
        builder.vertex(matrix4f, dx, dy, z).color(colorRGB).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
        builder.vertex(matrix4f, dx, dy, z).color(colorRGB).normal(matrix3f, -1.0F, 0.0F, 0.0F).endVertex();
        builder.vertex(matrix4f, x, dy, z).color(colorRGB).normal(matrix3f, -1.0F, 0.0F, 0.0F).endVertex();
        builder.vertex(matrix4f, x, dy, z).color(colorRGB).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
        builder.vertex(matrix4f, x, dy, dz).color(colorRGB).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
        builder.vertex(matrix4f, x, dy, dz).color(colorRGB).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
        builder.vertex(matrix4f, x, y, dz).color(colorRGB).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
        builder.vertex(matrix4f, x, y, dz).color(colorRGB).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
        builder.vertex(matrix4f, dx, y, dz).color(colorRGB).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
        builder.vertex(matrix4f, dx, y, dz).color(colorRGB).normal(matrix3f, 0.0F, 0.0F, -1.0F).endVertex();
        builder.vertex(matrix4f, dx, y, z).color(colorRGB).normal(matrix3f, 0.0F, 0.0F, -1.0F).endVertex();
        builder.vertex(matrix4f, x, dy, dz).color(colorRGB).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
        builder.vertex(matrix4f, dx, dy, dz).color(colorRGB).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
        builder.vertex(matrix4f, dx, y, dz).color(colorRGB).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
        builder.vertex(matrix4f, dx, dy, dz).color(colorRGB).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
        builder.vertex(matrix4f, dx, dy, z).color(colorRGB).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
        builder.vertex(matrix4f, dx, dy, dz).color(colorRGB).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();

        matrix.popPose();
    }

    public static void renderLines(PoseStack matrix, BlockPos startPos, BlockPos endPos, Color color) {
        //We want to draw from the starting position to the (ending position)+1
        int x = Math.min(startPos.getX(), endPos.getX()), y = Math.min(startPos.getY(), endPos.getY()), z = Math.min(startPos.getZ(), endPos.getZ());

        int dx = (startPos.getX() > endPos.getX()) ? startPos.getX() + 1 : endPos.getX() + 1;
        int dy = (startPos.getY() > endPos.getY()) ? startPos.getY() + 1 : endPos.getY() + 1;
        int dz = (startPos.getZ() > endPos.getZ()) ? startPos.getZ() + 1 : endPos.getZ() + 1;

        MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
        VertexConsumer builder = buffer.getBuffer(OurRenderTypes.lines());

        matrix.pushPose();
        Matrix4f matrix4f = matrix.last().pose();
        PoseStack.Pose matrix3f = matrix.last();
        int colorRGB = color.getRGB();

        builder.vertex(matrix4f, x, y, z).color(colorRGB).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
        builder.vertex(matrix4f, dx, y, z).color(colorRGB).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
        builder.vertex(matrix4f, x, y, z).color(colorRGB).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
        builder.vertex(matrix4f, x, dy, z).color(colorRGB).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
        builder.vertex(matrix4f, x, y, z).color(colorRGB).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
        builder.vertex(matrix4f, x, y, dz).color(colorRGB).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
        builder.vertex(matrix4f, dx, y, z).color(colorRGB).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
        builder.vertex(matrix4f, dx, dy, z).color(colorRGB).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
        builder.vertex(matrix4f, dx, dy, z).color(colorRGB).normal(matrix3f, -1.0F, 0.0F, 0.0F).endVertex();
        builder.vertex(matrix4f, x, dy, z).color(colorRGB).normal(matrix3f, -1.0F, 0.0F, 0.0F).endVertex();
        builder.vertex(matrix4f, x, dy, z).color(colorRGB).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
        builder.vertex(matrix4f, x, dy, dz).color(colorRGB).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
        builder.vertex(matrix4f, x, dy, dz).color(colorRGB).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
        builder.vertex(matrix4f, x, y, dz).color(colorRGB).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
        builder.vertex(matrix4f, x, y, dz).color(colorRGB).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
        builder.vertex(matrix4f, dx, y, dz).color(colorRGB).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
        builder.vertex(matrix4f, dx, y, dz).color(colorRGB).normal(matrix3f, 0.0F, 0.0F, -1.0F).endVertex();
        builder.vertex(matrix4f, dx, y, z).color(colorRGB).normal(matrix3f, 0.0F, 0.0F, -1.0F).endVertex();
        builder.vertex(matrix4f, x, dy, dz).color(colorRGB).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
        builder.vertex(matrix4f, dx, dy, dz).color(colorRGB).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
        builder.vertex(matrix4f, dx, y, dz).color(colorRGB).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
        builder.vertex(matrix4f, dx, dy, dz).color(colorRGB).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
        builder.vertex(matrix4f, dx, dy, z).color(colorRGB).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
        builder.vertex(matrix4f, dx, dy, dz).color(colorRGB).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();

        buffer.endBatch(OurRenderTypes.lines()); // @mcp: draw = finish
        matrix.popPose();
    }

    public static void renderBoxSolid(Matrix4f matrix, MultiBufferSource buffer, BlockPos pos, float r, float g, float b, float alpha) {
        double x = pos.getX() - 0.001;
        double y = pos.getY() - 0.001;
        double z = pos.getZ() - 0.001;
        double xEnd = pos.getX() + 1.0015;
        double yEnd = pos.getY() + 1.0015;
        double zEnd = pos.getZ() + 1.0015;

        renderBoxSolid(matrix, buffer, x, y, z, xEnd, yEnd, zEnd, r, g, b, alpha);
    }

    public static void renderBoxSolid(Matrix4f matrix, MultiBufferSource buffer, AABB aabb, float r, float g, float b, float alpha) {
        float minX = (float) aabb.minX;
        float minY = (float) aabb.minY;
        float minZ = (float) aabb.minZ;
        float maxX = (float) aabb.maxX;
        float maxY = (float) aabb.maxY;
        float maxZ = (float) aabb.maxZ;

        renderBoxSolid(matrix, buffer, minX, minY, minZ, maxX, maxY, maxZ, r, g, b, alpha);
    }

    public static void renderBoxSolid(Matrix4f matrix, MultiBufferSource buffer, double x, double y, double z, double xEnd, double yEnd, double zEnd, float red, float green, float blue, float alpha) {
        VertexConsumer builder = buffer.getBuffer(OurRenderTypes.TRANSPARENT_BOX);

        //careful: mc want's it's vertices to be defined CCW - if you do it the other way around weird cullling issues will arise
        //CCW herby counts as if you were looking at it from the outside
        float startX = (float) x;
        float startY = (float) y;
        float startZ = (float) z;
        float endX = (float) xEnd;
        float endY = (float) yEnd;
        float endZ = (float) zEnd;

        //down
        builder.vertex(matrix, startX, startY, startZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, endX, startY, startZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, endX, startY, endZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, startX, startY, endZ).color(red, green, blue, alpha).endVertex();

        //up
        builder.vertex(matrix, startX, endY, startZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, startX, endY, endZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, endX, endY, endZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, endX, endY, startZ).color(red, green, blue, alpha).endVertex();

        //east
        builder.vertex(matrix, startX, startY, startZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, startX, endY, startZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, endX, endY, startZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, endX, startY, startZ).color(red, green, blue, alpha).endVertex();

        //west
        builder.vertex(matrix, startX, startY, endZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, endX, startY, endZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, endX, endY, endZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, startX, endY, endZ).color(red, green, blue, alpha).endVertex();

        //south
        builder.vertex(matrix, endX, startY, startZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, endX, endY, startZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, endX, endY, endZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, endX, startY, endZ).color(red, green, blue, alpha).endVertex();

        //north
        builder.vertex(matrix, startX, startY, startZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, startX, startY, endZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, startX, endY, endZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, startX, endY, startZ).color(red, green, blue, alpha).endVertex();
    }
}
