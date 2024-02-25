package com.direwolf20.justdirethings.client.blockentityrenders;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.client.renderers.DireVertexConsumer;
import com.direwolf20.justdirethings.client.renderers.OurRenderTypes;
import com.direwolf20.justdirethings.common.blockentities.gooblocks.GooBlockBE_Base;
import com.direwolf20.justdirethings.setup.Registration;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.BitSet;
import java.util.List;

public class GooBlockRender_Base<T extends GooBlockBE_Base> implements BlockEntityRenderer<T> {
    public static final ResourceLocation[] patterns = {
            new ResourceLocation(JustDireThings.MODID, "textures/misc/goorender1.png"),
            new ResourceLocation(JustDireThings.MODID, "textures/misc/goorender2.png"),
            new ResourceLocation(JustDireThings.MODID, "textures/misc/goorender3.png"),
            new ResourceLocation(JustDireThings.MODID, "textures/misc/goorender4.png"),
            new ResourceLocation(JustDireThings.MODID, "textures/misc/goorender5.png"),
            new ResourceLocation(JustDireThings.MODID, "textures/misc/goorender6.png"),
            new ResourceLocation(JustDireThings.MODID, "textures/misc/goorender7.png"),
            new ResourceLocation(JustDireThings.MODID, "textures/misc/goorender8.png"),
            new ResourceLocation(JustDireThings.MODID, "textures/misc/goorender9.png"),
            new ResourceLocation(JustDireThings.MODID, "textures/misc/goorender10.png")
    };

    public GooBlockRender_Base(BlockEntityRendererProvider.Context p_173636_) {

    }

    @Override
    public void render(T blockentity, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightsIn, int combinedOverlayIn) {
        Level level = blockentity.getLevel();
        BlockPos pos = blockentity.getBlockPos().above(0);
        BlockState renderState = Registration.GooBlock_Tier1.get().defaultBlockState();
        BlockRenderDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRenderer();
        BakedModel ibakedmodel = blockrendererdispatcher.getBlockModel(renderState);
        BlockColors blockColors = Minecraft.getInstance().getBlockColors();
        ModelBlockRenderer modelBlockRenderer = new ModelBlockRenderer(blockColors);
        int remainingTicks = blockentity.getRemainingTimeFor(Direction.UP); //Todo All sides
        if (remainingTicks > 0) {
            int maxTicks = blockentity.getCraftingDuration();
            renderTextures(Direction.UP, level, pos, matrixStackIn, bufferIn, combinedOverlayIn, renderState, ibakedmodel, modelBlockRenderer, remainingTicks, maxTicks);
        }

        //ResourceLocation patternLocation = new ResourceLocation(JustDireThings.MODID, "textures/misc/goorender4.png");
        //renderTexturePattern(level, pos, matrixStackIn, bufferIn, combinedOverlayIn, 1, patternLocation, renderState, ibakedmodel, modelBlockRenderer);
        //ResourceLocation patternLocation2 = new ResourceLocation(JustDireThings.MODID, "textures/misc/goorender6.png");
        //renderTexturePattern(level, pos, matrixStackIn, bufferIn, combinedOverlayIn, 0.4f, patternLocation2, renderState, ibakedmodel, modelBlockRenderer);
    }

    public void renderTextures(Direction direction, Level level, BlockPos pos, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedOverlayIn, BlockState renderState, BakedModel ibakedmodel, ModelBlockRenderer modelBlockRenderer, int remainingTicks, int maxTicks) {
        float percentComplete = ((1 - (float) remainingTicks / (float) maxTicks) * 100);
        int tensDigit = (int) (percentComplete / 10);
        if (tensDigit > 0) {
            ResourceLocation patternLocation = patterns[tensDigit - 1];
            renderTexturePattern(direction, level, pos, matrixStackIn, bufferIn, combinedOverlayIn, 1f, patternLocation, renderState, ibakedmodel, modelBlockRenderer);
        }
        ResourceLocation patternLocation2 = patterns[tensDigit];
        float percentagePart = percentComplete % 10;
        float alpha = percentagePart / (float) 10;
        renderTexturePattern(direction, level, pos, matrixStackIn, bufferIn, combinedOverlayIn, alpha, patternLocation2, renderState, ibakedmodel, modelBlockRenderer);
    }

    public void renderTexturePattern(Direction direction, Level level, BlockPos pos, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedOverlayIn, float transparency, ResourceLocation pattern, BlockState renderState, BakedModel ibakedmodel, ModelBlockRenderer modelBlockRenderer) {
        matrixStackIn.pushPose();
        matrixStackIn.translate(0, 1, 0); //Todo proper sidedness

        VertexConsumer vertexconsumer = bufferIn.getBuffer(OurRenderTypes.gooPatternAlpha(pattern));

        matrixStackIn.pushPose();
        matrixStackIn.translate(0, 0, -0.0003f); //Push forward on Z - for the pattern draw TODO variable?

        PoseStack.Pose posestack$pose = matrixStackIn.last();
        Matrix4f matrix4f = posestack$pose.pose();
        Matrix3f matrix3f = posestack$pose.normal();

        renderQuad(matrix4f, matrix3f, vertexconsumer, 1f, 1f, 1f, 1f, 0, 1, 0, 0, 1, 0, 0, 1, 0, 1);
        matrixStackIn.popPose();

        matrixStackIn.pushPose();
        matrixStackIn.translate(0, 0, -0.0002f);

        VertexConsumer builder = bufferIn.getBuffer(OurRenderTypes.RenderBlockBackface);
        DireVertexConsumer chunksConsumer = new DireVertexConsumer(builder, transparency);

        float[] afloat = new float[Direction.values().length * 2];
        BitSet bitset = new BitSet(3);
        RandomSource randomSource = RandomSource.create();
        randomSource.setSeed(renderState.getSeed(pos));
        BlockPos.MutableBlockPos blockpos$mutableblockpos = pos.mutable();

        List<BakedQuad> list;
        ModelBlockRenderer.AmbientOcclusionFace modelblockrenderer$ambientocclusionface = new ModelBlockRenderer.AmbientOcclusionFace();

        Direction renderSide = Direction.NORTH;

        list = ibakedmodel.getQuads(renderState, renderSide, randomSource, ModelData.EMPTY, null);
        if (!list.isEmpty()) {
            blockpos$mutableblockpos.setWithOffset(pos, renderSide);
            modelBlockRenderer.renderModelFaceAO(level, renderState, pos, matrixStackIn, chunksConsumer, list, afloat, bitset, modelblockrenderer$ambientocclusionface, combinedOverlayIn);
        }
        matrixStackIn.popPose();

        matrixStackIn.popPose();
    }


    private static void renderQuad(Matrix4f pPose, Matrix3f pNormal, VertexConsumer pConsumer, float pRed, float pGreen, float pBlue, float pAlpha, int pMinY, int pMaxY,
                                   float pMinX, float pMinZ, float pMaxX, float pMaxZ, float pMinU, float pMaxU, float pMinV, float pMaxV) {
        addVertex(pPose, pNormal, pConsumer, pRed, pGreen, pBlue, pAlpha, pMaxY, pMinX, pMinZ, pMaxU, pMinV);
        addVertex(pPose, pNormal, pConsumer, pRed, pGreen, pBlue, pAlpha, pMinY, pMinX, pMinZ, pMaxU, pMaxV);
        addVertex(pPose, pNormal, pConsumer, pRed, pGreen, pBlue, pAlpha, pMinY, pMaxX, pMaxZ, pMinU, pMaxV);
        addVertex(pPose, pNormal, pConsumer, pRed, pGreen, pBlue, pAlpha, pMaxY, pMaxX, pMaxZ, pMinU, pMinV);
    }

    private static void addVertex(Matrix4f pPose, Matrix3f pNormal, VertexConsumer pConsumer, float pRed, float pGreen, float pBlue, float pAlpha,
                                  int pY, float pX, float pZ, float pU, float pV) {
        pConsumer.vertex(pPose, pX, (float) pY, pZ)
                .color(pRed, pGreen, pBlue, pAlpha)
                .uv(pU, pV)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(15728880)
                .normal(pNormal, 0.0F, 1.0F, 0.0F)
                .endVertex();
    }

}
