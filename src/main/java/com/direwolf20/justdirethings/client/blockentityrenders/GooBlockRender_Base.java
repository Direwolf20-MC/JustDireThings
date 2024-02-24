package com.direwolf20.justdirethings.client.blockentityrenders;

import com.direwolf20.justdirethings.client.renderers.DireVertexConsumerSquished;
import com.direwolf20.justdirethings.client.renderers.OurRenderTypes;
import com.direwolf20.justdirethings.common.blockentities.gooblocks.GooBlockBE_Base;
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
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;

import java.util.BitSet;
import java.util.List;

public class GooBlockRender_Base<T extends GooBlockBE_Base> implements BlockEntityRenderer<T> {
    public GooBlockRender_Base(BlockEntityRendererProvider.Context p_173636_) {

    }

    @Override
    public void render(T blockentity, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightsIn, int combinedOverlayIn) {
        Level level = blockentity.getLevel();
        BlockPos pos = blockentity.getBlockPos().above(0);
        BlockState renderState = Blocks.FURNACE.defaultBlockState();
        BlockRenderDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRenderer();
        BakedModel ibakedmodel = blockrendererdispatcher.getBlockModel(renderState);
        BlockColors blockColors = Minecraft.getInstance().getBlockColors();
        ModelBlockRenderer modelBlockRenderer = new ModelBlockRenderer(blockColors);
        renderSquished(level, pos, matrixStackIn, bufferIn, combinedLightsIn, combinedOverlayIn, 1f, renderState, ibakedmodel, blockrendererdispatcher, modelBlockRenderer, true, false);
    }

    public void renderSquished(Level level, BlockPos pos, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightsIn, int combinedOverlayIn, float scale, BlockState renderState, BakedModel ibakedmodel, BlockRenderDispatcher blockrendererdispatcher, ModelBlockRenderer modelBlockRenderer, boolean adjustUV, boolean bottomUp) {
        matrixStackIn.pushPose();
        matrixStackIn.translate(0, 1, 0);
        VertexConsumer builder = bufferIn.getBuffer(OurRenderTypes.RenderBlockBackface);

        scale = Mth.lerp(scale, 0f, 1f);
        DireVertexConsumerSquished chunksConsumer = new DireVertexConsumerSquished(builder, 0, 0, 0, 0.75f, scale, 0.75f, matrixStackIn.last().pose());
        chunksConsumer.adjustUV = adjustUV;
        chunksConsumer.bottomUp = bottomUp;
        if (!renderState.isSolidRender(level, pos))
            chunksConsumer.adjustUV = false;

        float[] afloat = new float[Direction.values().length * 2];
        BitSet bitset = new BitSet(3);
        RandomSource randomSource = RandomSource.create();
        randomSource.setSeed(renderState.getSeed(pos));
        BlockPos.MutableBlockPos blockpos$mutableblockpos = pos.mutable();

        ModelBlockRenderer.AmbientOcclusionFace modelblockrenderer$ambientocclusionface = new ModelBlockRenderer.AmbientOcclusionFace();
        //Direction direction = Direction.EAST;
        for (Direction direction : Direction.values()) {
            List<BakedQuad> list = ibakedmodel.getQuads(renderState, direction, randomSource, ModelData.EMPTY, null);
            if (!list.isEmpty()) {
                TextureAtlasSprite sprite = list.get(0).getSprite();
                chunksConsumer.setSprite(sprite);
                chunksConsumer.setDirection(direction);
                blockpos$mutableblockpos.setWithOffset(pos, direction);
                modelBlockRenderer.renderModelFaceAO(level, renderState, pos, matrixStackIn, chunksConsumer, list, afloat, bitset, modelblockrenderer$ambientocclusionface, combinedOverlayIn);
            }
        }
        /*List<BakedQuad> list = ibakedmodel.getQuads(renderState, null, randomSource, ModelData.EMPTY, null);
        if (!list.isEmpty()) {
            TextureAtlasSprite sprite = list.get(0).getSprite();
            chunksConsumer.setSprite(sprite);
            chunksConsumer.setDirection(null);
            modelBlockRenderer.renderModelFaceAO(level, renderState, pos, matrixStackIn, chunksConsumer, list, afloat, bitset, modelblockrenderer$ambientocclusionface, combinedOverlayIn);
        }*/


        matrixStackIn.popPose();
    }

}
