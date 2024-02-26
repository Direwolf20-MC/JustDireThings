package com.direwolf20.justdirethings.client.blockentityrenders;

import com.direwolf20.justdirethings.client.renderers.DireVertexConsumer;
import com.direwolf20.justdirethings.client.renderers.OurRenderTypes;
import com.direwolf20.justdirethings.common.blockentities.gooblocks.GooBlockBE_Base;
import com.direwolf20.justdirethings.common.blocks.gooblocks.GooPatternBlock;
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
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;

import java.util.BitSet;
import java.util.List;

public class GooBlockRender_Base<T extends GooBlockBE_Base> implements BlockEntityRenderer<T> {
    private final static float percentageDivisor = (float) 100 / GooPatternBlock.GOOSTAGE.getPossibleValues().size();

    public GooBlockRender_Base(BlockEntityRendererProvider.Context p_173636_) {

    }

    @Override
    public void render(T blockentity, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightsIn, int combinedOverlayIn) {
        for (Direction direction : Direction.values()) {
            int remainingTicks = blockentity.getRemainingTimeFor(direction);
            if (remainingTicks > 0) {
                int maxTicks = blockentity.getCraftingDuration(direction);
                renderTextures(direction, blockentity.getLevel(), blockentity.getBlockPos(), matrixStackIn, bufferIn, combinedOverlayIn, remainingTicks, maxTicks, blockentity.getBlockState());
            }
        }
    }

    public void renderTextures(Direction direction, Level level, BlockPos pos, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedOverlayIn, int remainingTicks, int maxTicks, BlockState renderState) {
        float percentComplete = ((1 - (float) remainingTicks / (float) maxTicks) * 100);
        int tensDigit = (int) (percentComplete / percentageDivisor);
        if (tensDigit > 0) { //Render the prior stage with full transparency
            BlockState patternState = Registration.GooPatternBlock.get().defaultBlockState().setValue(GooPatternBlock.GOOSTAGE, tensDigit - 1);
            renderTexturePattern(direction, level, pos, matrixStackIn, bufferIn, combinedOverlayIn, 1f, patternState, renderState);
        }
        BlockState patternState = Registration.GooPatternBlock.get().defaultBlockState().setValue(GooPatternBlock.GOOSTAGE, tensDigit);
        float percentagePart = percentComplete % percentageDivisor;
        float alpha = percentagePart / percentageDivisor;
        renderTexturePattern(direction, level, pos, matrixStackIn, bufferIn, combinedOverlayIn, alpha, patternState, renderState);
    }

    public void renderTexturePattern(Direction direction, Level level, BlockPos pos, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedOverlayIn, float transparency, BlockState pattern, BlockState renderState) {
        BlockRenderDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRenderer();
        BlockColors blockColors = Minecraft.getInstance().getBlockColors();
        ModelBlockRenderer modelBlockRenderer = new ModelBlockRenderer(blockColors);
        BlockPos renderAtPos = pos.above(255); //Render up in the sky to make it not affected by shading and shadows

        //These are used for the rendering below
        float[] afloat = new float[Direction.values().length * 2];
        BitSet bitset = new BitSet(3);
        RandomSource randomSource = RandomSource.create();
        BlockPos.MutableBlockPos blockpos$mutableblockpos = renderAtPos.mutable();
        ModelBlockRenderer.AmbientOcclusionFace modelblockrenderer$ambientocclusionface = new ModelBlockRenderer.AmbientOcclusionFace();


        matrixStackIn.pushPose();
        //Offset the render to the direction we're crafting at
        matrixStackIn.translate(direction.getNormal().getX(), direction.getNormal().getY(), direction.getNormal().getZ());
        //Slightly larger than a normal block, to prevent z-fighting
        matrixStackIn.translate(-0.0005f, -0.0005f, -0.0005f);
        matrixStackIn.scale(1.001f, 1.001f, 1.001f);

        //Rotate based on the direction of the block we're drawing. If we don't rotate both blocks together we get z-fighting!
        matrixStackIn.translate(0.5, 0.5, 0.5);
        matrixStackIn.mulPose(direction.getRotation());
        matrixStackIn.translate(-0.5, -0.5, -0.5);

        //Pattern Block - Renders to the depth buffer ONLY, to set the pattern that we draw
        VertexConsumer builder = bufferIn.getBuffer(OurRenderTypes.GooPattern);
        DireVertexConsumer chunksConsumer = new DireVertexConsumer(builder, 1f);
        BakedModel ibakedmodel = blockrendererdispatcher.getBlockModel(pattern);

        randomSource.setSeed(pattern.getSeed(renderAtPos));

        List<BakedQuad> list;

        for (Direction renderSide : Direction.values()) {
            list = ibakedmodel.getQuads(pattern, renderSide, randomSource, ModelData.EMPTY, null);
            if (!list.isEmpty()) {
                blockpos$mutableblockpos.setWithOffset(renderAtPos, renderSide);
                modelBlockRenderer.renderModelFaceAO(level, pattern, renderAtPos, matrixStackIn, chunksConsumer, list, afloat, bitset, modelblockrenderer$ambientocclusionface, combinedOverlayIn);
            }
        }

        //Now draw the REAL block overtop, but with BlendFunction set to EQUALS -- Meaning it'll only draw where theres already a pixel exiting from the pattern above
        VertexConsumer builder2 = bufferIn.getBuffer(OurRenderTypes.RenderBlockBackface);
        DireVertexConsumer chunksConsumer2 = new DireVertexConsumer(builder2, transparency);
        BakedModel ibakedmodel2 = blockrendererdispatcher.getBlockModel(renderState);

        List<BakedQuad> list2;
        for (Direction renderSide : Direction.values()) {
            list2 = ibakedmodel2.getQuads(renderState, renderSide, randomSource, ModelData.EMPTY, null);
            if (!list2.isEmpty()) {
                blockpos$mutableblockpos.setWithOffset(renderAtPos, renderSide);
                modelBlockRenderer.renderModelFaceAO(level, renderState, renderAtPos, matrixStackIn, chunksConsumer2, list2, afloat, bitset, modelblockrenderer$ambientocclusionface, combinedOverlayIn);
            }
        }

        matrixStackIn.popPose();

    }
}
