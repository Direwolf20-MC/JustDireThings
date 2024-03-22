package com.direwolf20.justdirethings.client.blockentityrenders.baseber;

import com.direwolf20.justdirethings.client.renderers.RenderHelpers;
import com.direwolf20.justdirethings.common.blockentities.basebe.AreaAffectingBE;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import org.joml.Matrix4f;

import java.awt.*;

public class AreaAffectingBER<T extends AreaAffectingBE> implements BlockEntityRenderer<T> {

    @Override
    public void render(AreaAffectingBE blockentity, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightsIn, int combinedOverlayIn) {
        Matrix4f matrix4f = matrixStackIn.last().pose();
        if (blockentity.renderArea) {
            RenderHelpers.renderLines(matrixStackIn, blockentity.getAABB(BlockPos.ZERO), Color.GREEN, bufferIn);
            RenderHelpers.renderBoxSolid(matrix4f, bufferIn, blockentity.getAABB(BlockPos.ZERO), 1, 0, 0, 0.125f);
        }
    }

    @Override
    public AABB getRenderBoundingBox(T blockEntity) {
        return AABB.encapsulatingFullBlocks(blockEntity.getBlockPos().above(10).north(10).east(10), blockEntity.getBlockPos().below(10).south(10).west(10));
    }
}
