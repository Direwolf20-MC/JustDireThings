package com.direwolf20.justdirethings.client.blockentityrenders;

import com.direwolf20.justdirethings.client.blockentityrenders.baseber.AreaAffectingBER;
import com.direwolf20.justdirethings.common.blockentities.InventoryHolderBE;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

// TODO(port, stage-18): reinstate mock-player rendering.
// 1.21.1 used EntityRenderDispatcher.render(entity, x, y, z, yaw, partialTicks, pose, buffer, light).
// 26.1 replaced that with a three-phase pipeline that needs an extracted AvatarRenderState and
// EntityRenderDispatcher.submit(renderState, camera, x, y, z, poseStack, submitNodeCollector).
// Reconstructing a mock AbstractClientPlayer + building its AvatarRenderState off-thread every frame
// is non-trivial; revisit once the rest of the port is stable.
public class InventoryHolderBER extends AreaAffectingBER<InventoryHolderBE, AreaAffectingBER.AreaAffectingRenderState> {
    public InventoryHolderBER(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void extractRenderState(InventoryHolderBE blockEntity, AreaAffectingRenderState state, float partialTicks, Vec3 cameraPosition,
                                    ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
    }

    @Override
    public void submit(AreaAffectingRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        super.submit(state, poseStack, submitNodeCollector, camera);
        // TODO(port, stage-18): render mock player entity with equipped inventory (main/off-hand + armor slots 36-40)
        // rotating on Y axis above the block, at scale 0.5, based on InventoryHolderBE.renderPlayer / placedByUUID.
    }
}
