package com.direwolf20.justdirethings.client.entityrenders;

import com.direwolf20.justdirethings.common.entities.PortalEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;

// TODO(port, stage-16): reinstate portal shader render and frame rendering.
// Old 1.21.1 code used DireRenderTypes + ShaderTexture custom RenderType with a sampler2D shader
// texture uniform, drew two animated portal-plane faces with UV on a custom pipeline, and drew a
// thick colored AABB "frame" around the portal via RenderHelpers.renderBoxSolid.
// In 26.1:
//   - Custom pipelines are built via RenderSetup.builder(...) + RegisterRenderPipelinesEvent (see RENDER_PORTING.md §1.7)
//   - RenderHelpers.renderBoxSolid needs rewriting against the new VertexConsumer path (Stage 16 rewrites RenderHelpers).
//   - Vertex submission inside submit(...) must go through submitNodeCollector.submitCustomGeometry(pose, renderType, cb).
public class PortalEntityRender<T extends PortalEntity> extends EntityRenderer<T, PortalEntityRender.PortalEntityRenderState> {

    public static class PortalEntityRenderState extends EntityRenderState {
        public int tickCount;
        public boolean dying;
        public int deathCounter;
        public boolean isPrimary;
        public Direction direction = Direction.NORTH;
        public Direction.Axis alignment = Direction.Axis.Y;
        public AABB localBounds = new AABB(0, 0, 0, 1, 1, 1);
    }

    public PortalEntityRender(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public PortalEntityRenderState createRenderState() {
        return new PortalEntityRenderState();
    }

    @Override
    public void extractRenderState(T entity, PortalEntityRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.tickCount = entity.tickCount;
        state.dying = entity.isDying();
        state.deathCounter = entity.getDeathCounter();
        state.isPrimary = entity.getIsPrimary();
        state.direction = entity.getDirection();
        state.alignment = entity.getAlignment();
        state.localBounds = entity.getBoundingBox().move(-entity.getX(), -entity.getY(), -entity.getZ());
    }

    @Override
    public void submit(PortalEntityRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        super.submit(state, poseStack, submitNodeCollector, camera);
        // TODO(port, stage-16): submit portal planes + colored border here via
        // submitNodeCollector.submitCustomGeometry(...) once DireRenderTypes + RenderHelpers are rebuilt.
    }
}
