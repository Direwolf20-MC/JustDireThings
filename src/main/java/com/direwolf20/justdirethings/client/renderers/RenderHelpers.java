package com.direwolf20.justdirethings.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import org.joml.Matrix4f;

import java.awt.*;

public class RenderHelpers {
    public static final ResourceLocation DUMMY_TEXTURE = ResourceLocation.fromNamespaceAndPath("neoforge", "white");
    private static float dummyU0 = 0F;
    private static float dummyU1 = 1F;
    private static float dummyV0 = 0F;
    private static float dummyV1 = 1F;

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

        builder.addVertex(matrix4f, x, y, z).setColor(colorRGB).setNormal(matrix3f, 1.0F, 0.0F, 0.0F);
        builder.addVertex(matrix4f, dx, y, z).setColor(colorRGB).setNormal(matrix3f, 1.0F, 0.0F, 0.0F);
        builder.addVertex(matrix4f, x, y, z).setColor(colorRGB).setNormal(matrix3f, 0.0F, 1.0F, 0.0F);
        builder.addVertex(matrix4f, x, dy, z).setColor(colorRGB).setNormal(matrix3f, 0.0F, 1.0F, 0.0F);
        builder.addVertex(matrix4f, x, y, z).setColor(colorRGB).setNormal(matrix3f, 0.0F, 0.0F, 1.0F);
        builder.addVertex(matrix4f, x, y, dz).setColor(colorRGB).setNormal(matrix3f, 0.0F, 0.0F, 1.0F);
        builder.addVertex(matrix4f, dx, y, z).setColor(colorRGB).setNormal(matrix3f, 0.0F, 1.0F, 0.0F);
        builder.addVertex(matrix4f, dx, dy, z).setColor(colorRGB).setNormal(matrix3f, 0.0F, 1.0F, 0.0F);
        builder.addVertex(matrix4f, dx, dy, z).setColor(colorRGB).setNormal(matrix3f, -1.0F, 0.0F, 0.0F);
        builder.addVertex(matrix4f, x, dy, z).setColor(colorRGB).setNormal(matrix3f, -1.0F, 0.0F, 0.0F);
        builder.addVertex(matrix4f, x, dy, z).setColor(colorRGB).setNormal(matrix3f, 0.0F, 0.0F, 1.0F);
        builder.addVertex(matrix4f, x, dy, dz).setColor(colorRGB).setNormal(matrix3f, 0.0F, 0.0F, 1.0F);
        builder.addVertex(matrix4f, x, dy, dz).setColor(colorRGB).setNormal(matrix3f, 0.0F, -1.0F, 0.0F);
        builder.addVertex(matrix4f, x, y, dz).setColor(colorRGB).setNormal(matrix3f, 0.0F, -1.0F, 0.0F);
        builder.addVertex(matrix4f, x, y, dz).setColor(colorRGB).setNormal(matrix3f, 1.0F, 0.0F, 0.0F);
        builder.addVertex(matrix4f, dx, y, dz).setColor(colorRGB).setNormal(matrix3f, 1.0F, 0.0F, 0.0F);
        builder.addVertex(matrix4f, dx, y, dz).setColor(colorRGB).setNormal(matrix3f, 0.0F, 0.0F, -1.0F);
        builder.addVertex(matrix4f, dx, y, z).setColor(colorRGB).setNormal(matrix3f, 0.0F, 0.0F, -1.0F);
        builder.addVertex(matrix4f, x, dy, dz).setColor(colorRGB).setNormal(matrix3f, 1.0F, 0.0F, 0.0F);
        builder.addVertex(matrix4f, dx, dy, dz).setColor(colorRGB).setNormal(matrix3f, 1.0F, 0.0F, 0.0F);
        builder.addVertex(matrix4f, dx, y, dz).setColor(colorRGB).setNormal(matrix3f, 0.0F, 1.0F, 0.0F);
        builder.addVertex(matrix4f, dx, dy, dz).setColor(colorRGB).setNormal(matrix3f, 0.0F, 1.0F, 0.0F);
        builder.addVertex(matrix4f, dx, dy, z).setColor(colorRGB).setNormal(matrix3f, 0.0F, 0.0F, 1.0F);
        builder.addVertex(matrix4f, dx, dy, dz).setColor(colorRGB).setNormal(matrix3f, 0.0F, 0.0F, 1.0F);

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

        builder.addVertex(matrix4f, x, y, z).setColor(colorRGB).setNormal(matrix3f, 1.0F, 0.0F, 0.0F);
        builder.addVertex(matrix4f, dx, y, z).setColor(colorRGB).setNormal(matrix3f, 1.0F, 0.0F, 0.0F);
        builder.addVertex(matrix4f, x, y, z).setColor(colorRGB).setNormal(matrix3f, 0.0F, 1.0F, 0.0F);
        builder.addVertex(matrix4f, x, dy, z).setColor(colorRGB).setNormal(matrix3f, 0.0F, 1.0F, 0.0F);
        builder.addVertex(matrix4f, x, y, z).setColor(colorRGB).setNormal(matrix3f, 0.0F, 0.0F, 1.0F);
        builder.addVertex(matrix4f, x, y, dz).setColor(colorRGB).setNormal(matrix3f, 0.0F, 0.0F, 1.0F);
        builder.addVertex(matrix4f, dx, y, z).setColor(colorRGB).setNormal(matrix3f, 0.0F, 1.0F, 0.0F);
        builder.addVertex(matrix4f, dx, dy, z).setColor(colorRGB).setNormal(matrix3f, 0.0F, 1.0F, 0.0F);
        builder.addVertex(matrix4f, dx, dy, z).setColor(colorRGB).setNormal(matrix3f, -1.0F, 0.0F, 0.0F);
        builder.addVertex(matrix4f, x, dy, z).setColor(colorRGB).setNormal(matrix3f, -1.0F, 0.0F, 0.0F);
        builder.addVertex(matrix4f, x, dy, z).setColor(colorRGB).setNormal(matrix3f, 0.0F, 0.0F, 1.0F);
        builder.addVertex(matrix4f, x, dy, dz).setColor(colorRGB).setNormal(matrix3f, 0.0F, 0.0F, 1.0F);
        builder.addVertex(matrix4f, x, dy, dz).setColor(colorRGB).setNormal(matrix3f, 0.0F, -1.0F, 0.0F);
        builder.addVertex(matrix4f, x, y, dz).setColor(colorRGB).setNormal(matrix3f, 0.0F, -1.0F, 0.0F);
        builder.addVertex(matrix4f, x, y, dz).setColor(colorRGB).setNormal(matrix3f, 1.0F, 0.0F, 0.0F);
        builder.addVertex(matrix4f, dx, y, dz).setColor(colorRGB).setNormal(matrix3f, 1.0F, 0.0F, 0.0F);
        builder.addVertex(matrix4f, dx, y, dz).setColor(colorRGB).setNormal(matrix3f, 0.0F, 0.0F, -1.0F);
        builder.addVertex(matrix4f, dx, y, z).setColor(colorRGB).setNormal(matrix3f, 0.0F, 0.0F, -1.0F);
        builder.addVertex(matrix4f, x, dy, dz).setColor(colorRGB).setNormal(matrix3f, 1.0F, 0.0F, 0.0F);
        builder.addVertex(matrix4f, dx, dy, dz).setColor(colorRGB).setNormal(matrix3f, 1.0F, 0.0F, 0.0F);
        builder.addVertex(matrix4f, dx, y, dz).setColor(colorRGB).setNormal(matrix3f, 0.0F, 1.0F, 0.0F);
        builder.addVertex(matrix4f, dx, dy, dz).setColor(colorRGB).setNormal(matrix3f, 0.0F, 1.0F, 0.0F);
        builder.addVertex(matrix4f, dx, dy, z).setColor(colorRGB).setNormal(matrix3f, 0.0F, 0.0F, 1.0F);
        builder.addVertex(matrix4f, dx, dy, dz).setColor(colorRGB).setNormal(matrix3f, 0.0F, 0.0F, 1.0F);

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

        builder.addVertex(matrix4f, x, y, z).setColor(colorRGB).setNormal(matrix3f, 1.0F, 0.0F, 0.0F);
        builder.addVertex(matrix4f, dx, y, z).setColor(colorRGB).setNormal(matrix3f, 1.0F, 0.0F, 0.0F);
        builder.addVertex(matrix4f, x, y, z).setColor(colorRGB).setNormal(matrix3f, 0.0F, 1.0F, 0.0F);
        builder.addVertex(matrix4f, x, dy, z).setColor(colorRGB).setNormal(matrix3f, 0.0F, 1.0F, 0.0F);
        builder.addVertex(matrix4f, x, y, z).setColor(colorRGB).setNormal(matrix3f, 0.0F, 0.0F, 1.0F);
        builder.addVertex(matrix4f, x, y, dz).setColor(colorRGB).setNormal(matrix3f, 0.0F, 0.0F, 1.0F);
        builder.addVertex(matrix4f, dx, y, z).setColor(colorRGB).setNormal(matrix3f, 0.0F, 1.0F, 0.0F);
        builder.addVertex(matrix4f, dx, dy, z).setColor(colorRGB).setNormal(matrix3f, 0.0F, 1.0F, 0.0F);
        builder.addVertex(matrix4f, dx, dy, z).setColor(colorRGB).setNormal(matrix3f, -1.0F, 0.0F, 0.0F);
        builder.addVertex(matrix4f, x, dy, z).setColor(colorRGB).setNormal(matrix3f, -1.0F, 0.0F, 0.0F);
        builder.addVertex(matrix4f, x, dy, z).setColor(colorRGB).setNormal(matrix3f, 0.0F, 0.0F, 1.0F);
        builder.addVertex(matrix4f, x, dy, dz).setColor(colorRGB).setNormal(matrix3f, 0.0F, 0.0F, 1.0F);
        builder.addVertex(matrix4f, x, dy, dz).setColor(colorRGB).setNormal(matrix3f, 0.0F, -1.0F, 0.0F);
        builder.addVertex(matrix4f, x, y, dz).setColor(colorRGB).setNormal(matrix3f, 0.0F, -1.0F, 0.0F);
        builder.addVertex(matrix4f, x, y, dz).setColor(colorRGB).setNormal(matrix3f, 1.0F, 0.0F, 0.0F);
        builder.addVertex(matrix4f, dx, y, dz).setColor(colorRGB).setNormal(matrix3f, 1.0F, 0.0F, 0.0F);
        builder.addVertex(matrix4f, dx, y, dz).setColor(colorRGB).setNormal(matrix3f, 0.0F, 0.0F, -1.0F);
        builder.addVertex(matrix4f, dx, y, z).setColor(colorRGB).setNormal(matrix3f, 0.0F, 0.0F, -1.0F);
        builder.addVertex(matrix4f, x, dy, dz).setColor(colorRGB).setNormal(matrix3f, 1.0F, 0.0F, 0.0F);
        builder.addVertex(matrix4f, dx, dy, dz).setColor(colorRGB).setNormal(matrix3f, 1.0F, 0.0F, 0.0F);
        builder.addVertex(matrix4f, dx, y, dz).setColor(colorRGB).setNormal(matrix3f, 0.0F, 1.0F, 0.0F);
        builder.addVertex(matrix4f, dx, dy, dz).setColor(colorRGB).setNormal(matrix3f, 0.0F, 1.0F, 0.0F);
        builder.addVertex(matrix4f, dx, dy, z).setColor(colorRGB).setNormal(matrix3f, 0.0F, 0.0F, 1.0F);
        builder.addVertex(matrix4f, dx, dy, dz).setColor(colorRGB).setNormal(matrix3f, 0.0F, 0.0F, 1.0F);

        buffer.endBatch(OurRenderTypes.lines()); // @mcp: draw = finish
        matrix.popPose();
    }

    public static void renderBoxSolid(PoseStack pose, Matrix4f matrix, MultiBufferSource buffer, BlockPos pos, float r, float g, float b, float alpha) {
        double x = pos.getX() - 0.001;
        double y = pos.getY() - 0.001;
        double z = pos.getZ() - 0.001;
        double xEnd = pos.getX() + 1.0015;
        double yEnd = pos.getY() + 1.0015;
        double zEnd = pos.getZ() + 1.0015;

        renderBoxSolid(pose.last(), matrix, buffer, x, y, z, xEnd, yEnd, zEnd, r, g, b, alpha);
    }

    public static void renderBoxSolid(PoseStack pose, Matrix4f matrix, MultiBufferSource buffer, AABB aabb, float r, float g, float b, float alpha) {
        float minX = (float) aabb.minX;
        float minY = (float) aabb.minY;
        float minZ = (float) aabb.minZ;
        float maxX = (float) aabb.maxX;
        float maxY = (float) aabb.maxY;
        float maxZ = (float) aabb.maxZ;

        renderBoxSolid(pose.last(), matrix, buffer, minX, minY, minZ, maxX, maxY, maxZ, r, g, b, alpha);
    }

    public static void renderBoxSolid(PoseStack.Pose pose, Matrix4f matrix, MultiBufferSource buffer, double x, double y, double z, double xEnd, double yEnd, double zEnd, float red, float green, float blue, float alpha) {
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
        builder.addVertex(matrix, startX, startY, startZ).setColor(red, green, blue, alpha).setUv(dummyU0, dummyV0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT).setNormal(pose, 0F, 0F, 1F);
        builder.addVertex(matrix, endX, startY, startZ).setColor(red, green, blue, alpha).setUv(dummyU0, dummyV1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT).setNormal(pose, 0F, 0F, 1F);
        builder.addVertex(matrix, endX, startY, endZ).setColor(red, green, blue, alpha).setUv(dummyU1, dummyV1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT).setNormal(pose, 0F, 0F, 1F);
        builder.addVertex(matrix, startX, startY, endZ).setColor(red, green, blue, alpha).setUv(dummyU1, dummyV0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT).setNormal(pose, 0F, 0F, 1F);

        //up
        builder.addVertex(matrix, startX, endY, startZ).setColor(red, green, blue, alpha).setUv(dummyU0, dummyV0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT).setNormal(pose, 0F, 0F, 1F);
        builder.addVertex(matrix, startX, endY, endZ).setColor(red, green, blue, alpha).setUv(dummyU0, dummyV1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT).setNormal(pose, 0F, 0F, 1F);
        builder.addVertex(matrix, endX, endY, endZ).setColor(red, green, blue, alpha).setUv(dummyU1, dummyV1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT).setNormal(pose, 0F, 0F, 1F);
        builder.addVertex(matrix, endX, endY, startZ).setColor(red, green, blue, alpha).setUv(dummyU1, dummyV0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT).setNormal(pose, 0F, 0F, 1F);

        //east
        builder.addVertex(matrix, startX, startY, startZ).setColor(red, green, blue, alpha).setUv(dummyU0, dummyV0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT).setNormal(pose, 0F, 0F, 1F);
        builder.addVertex(matrix, startX, endY, startZ).setColor(red, green, blue, alpha).setUv(dummyU0, dummyV1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT).setNormal(pose, 0F, 0F, 1F);
        builder.addVertex(matrix, endX, endY, startZ).setColor(red, green, blue, alpha).setUv(dummyU1, dummyV1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT).setNormal(pose, 0F, 0F, 1F);
        builder.addVertex(matrix, endX, startY, startZ).setColor(red, green, blue, alpha).setUv(dummyU1, dummyV0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT).setNormal(pose, 0F, 0F, 1F);

        //west
        builder.addVertex(matrix, startX, startY, endZ).setColor(red, green, blue, alpha).setUv(dummyU0, dummyV0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT).setNormal(pose, 0F, 0F, 1F);
        builder.addVertex(matrix, endX, startY, endZ).setColor(red, green, blue, alpha).setUv(dummyU0, dummyV1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT).setNormal(pose, 0F, 0F, 1F);
        builder.addVertex(matrix, endX, endY, endZ).setColor(red, green, blue, alpha).setUv(dummyU1, dummyV1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT).setNormal(pose, 0F, 0F, 1F);
        builder.addVertex(matrix, startX, endY, endZ).setColor(red, green, blue, alpha).setUv(dummyU1, dummyV0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT).setNormal(pose, 0F, 0F, 1F);

        //south
        builder.addVertex(matrix, endX, startY, startZ).setColor(red, green, blue, alpha).setUv(dummyU0, dummyV0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT).setNormal(pose, 0F, 0F, 1F);
        builder.addVertex(matrix, endX, endY, startZ).setColor(red, green, blue, alpha).setUv(dummyU0, dummyV1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT).setNormal(pose, 0F, 0F, 1F);
        builder.addVertex(matrix, endX, endY, endZ).setColor(red, green, blue, alpha).setUv(dummyU1, dummyV1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT).setNormal(pose, 0F, 0F, 1F);
        builder.addVertex(matrix, endX, startY, endZ).setColor(red, green, blue, alpha).setUv(dummyU1, dummyV0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT).setNormal(pose, 0F, 0F, 1F);

        //north
        builder.addVertex(matrix, startX, startY, startZ).setColor(red, green, blue, alpha).setUv(dummyU0, dummyV0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT).setNormal(pose, 0F, 0F, 1F);
        builder.addVertex(matrix, startX, startY, endZ).setColor(red, green, blue, alpha).setUv(dummyU0, dummyV1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT).setNormal(pose, 0F, 0F, 1F);
        builder.addVertex(matrix, startX, endY, endZ).setColor(red, green, blue, alpha).setUv(dummyU1, dummyV1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT).setNormal(pose, 0F, 0F, 1F);
        builder.addVertex(matrix, startX, endY, startZ).setColor(red, green, blue, alpha).setUv(dummyU1, dummyV0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT).setNormal(pose, 0F, 0F, 1F);
    }

    public static void captureDummySprite(TextureAtlas atlas) {
        TextureAtlasSprite sprite = atlas.getSprite(DUMMY_TEXTURE);
        dummyU0 = sprite.getU0();
        dummyU1 = sprite.getU1();
        dummyV0 = sprite.getV0();
        dummyV1 = sprite.getV1();
    }
}
