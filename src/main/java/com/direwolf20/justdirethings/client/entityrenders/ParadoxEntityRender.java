package com.direwolf20.justdirethings.client.entityrenders;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.common.entities.ParadoxEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;

// TODO(port, stage-16): reinstate the paradox vortex + lightning visuals.
// Old 1.21.1 code drew a sphere via RenderHelpers.renderSphere and per-segment lightning
// arcs using OurRenderTypes.LINE_STRIP. Both targets are being rebuilt in Stage 16
// (OurRenderTypes → new RenderSetup/RenderPipeline; RenderHelpers rewritten for the new vertex path).
public class ParadoxEntityRender extends EntityRenderer<ParadoxEntity, ParadoxEntityRender.ParadoxEntityRenderState> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(JustDireThings.MODID, "textures/entity/vortex1.png");

    public static class ParadoxEntityRenderState extends EntityRenderState {
        public float currentRadius;
        public float targetRadius;
        public int growthTicks;
        public int growthDuration;
        public float shrinkScale;
        public int tickCount;
        public float partialTicks;
    }

    public ParadoxEntityRender(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public ParadoxEntityRenderState createRenderState() {
        return new ParadoxEntityRenderState();
    }

    @Override
    public void extractRenderState(ParadoxEntity entity, ParadoxEntityRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.currentRadius = entity.getRadius();
        state.targetRadius = entity.getTargetRadius();
        state.growthTicks = entity.getGrowthTicks();
        state.growthDuration = entity.growthDuration;
        state.shrinkScale = entity.getShrinkScale();
        state.tickCount = entity.tickCount;
        state.partialTicks = partialTicks;
    }

    @Override
    public void submit(ParadoxEntityRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        super.submit(state, poseStack, submitNodeCollector, camera);
        // TODO(port, stage-16): submit vortex sphere + lightning line strips.
    }
}
