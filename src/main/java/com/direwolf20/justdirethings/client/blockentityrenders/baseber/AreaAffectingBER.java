package com.direwolf20.justdirethings.client.blockentityrenders.baseber;

import com.direwolf20.justdirethings.common.blockentities.basebe.AreaAffectingBE;
import com.mojang.blaze3d.vertex.PoseStack;
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
        // TODO(port, stage-16): restore RenderHelpers.renderLines / renderBoxSolid against the new RenderType/pipeline system.
        // Draw: lines (Color.GREEN) + solid fill (1,0,0,0.125f) around state.aabb.
        // If state.hasOffset, also draw state.aabbOffsetOnly with Color.WHITE lines + blue fill (0,0,1,0.125f).
    }

    @Override
    public AABB getRenderBoundingBox(T blockEntity) {
        return AABB.encapsulatingFullBlocks(blockEntity.getBlockPos().above(10).north(10).east(10),
                blockEntity.getBlockPos().below(10).south(10).west(10));
    }
}
