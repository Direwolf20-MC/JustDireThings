package com.direwolf20.justdirethings.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

import java.util.BitSet;
import java.util.List;

public class DireModelBlockRenderer extends ModelBlockRenderer {
    /** Note For Updates: This class uses several AT's **/
    public Direction newDirection;

    public DireModelBlockRenderer(BlockColors pBlockColors, Direction newD) {
        super(pBlockColors);
        newDirection = newD;
    }

    public void setDirection(Direction direction) {
        this.newDirection = direction;
    }

    @Override
    public void renderModelFaceAO(
            BlockAndTintGetter pLevel,
            BlockState pState,
            BlockPos pPos,
            PoseStack pPoseStack,
            VertexConsumer pConsumer,
            List<BakedQuad> pQuads,
            float[] pShape,
            BitSet pShapeFlags,
            ModelBlockRenderer.AmbientOcclusionFace pAoFace,
            int pPackedOverlay
    ) {
        for (BakedQuad bakedquad : pQuads) {
            this.calculateShape(pLevel, pState, pPos, bakedquad.getVertices(), bakedquad.getDirection(), pShape, pShapeFlags);
            if (!net.neoforged.neoforge.client.ClientHooks.calculateFaceWithoutAO(pLevel, pState, pPos, bakedquad, pShapeFlags.get(0), pAoFace.brightness, pAoFace.lightmap))
                pAoFace.calculate(pLevel, pState, pPos, newDirection, pShape, pShapeFlags, bakedquad.isShade());
            this.putQuadData(
                    pLevel,
                    pState,
                    pPos,
                    pConsumer,
                    pPoseStack.last(),
                    bakedquad,
                    pAoFace.brightness[0],
                    pAoFace.brightness[1],
                    pAoFace.brightness[2],
                    pAoFace.brightness[3],
                    pAoFace.lightmap[0],
                    pAoFace.lightmap[1],
                    pAoFace.lightmap[2],
                    pAoFace.lightmap[3],
                    pPackedOverlay
            );
        }
    }
}
