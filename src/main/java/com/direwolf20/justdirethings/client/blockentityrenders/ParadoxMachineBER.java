package com.direwolf20.justdirethings.client.blockentityrenders;

import com.direwolf20.justdirethings.client.blockentityrenders.baseber.AreaAffectingBER;
import com.direwolf20.justdirethings.common.blockentities.ParadoxMachineBE;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.Map;

public class ParadoxMachineBER extends AreaAffectingBER<ParadoxMachineBE, ParadoxMachineBER.ParadoxMachineRenderState> {

    public static class ParadoxMachineRenderState extends AreaAffectingRenderState {
        public boolean isRunning;
        public boolean renderParadox;
        public int targetType;
        public float runningAlpha = 1f;
        public int runningIntAlpha = 255;
        public @Nullable Map<BlockPos, BlockState> blocks;
        public @Nullable Map<Vec3, LivingEntity> entities;
        public @Nullable java.util.Set<Vec3> restoringEntities;
        public BlockPos machinePos = BlockPos.ZERO;
    }

    public ParadoxMachineBER(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public ParadoxMachineRenderState createRenderState() {
        return new ParadoxMachineRenderState();
    }

    @Override
    public void extractRenderState(ParadoxMachineBE blockEntity, ParadoxMachineRenderState state, float partialTicks, Vec3 cameraPosition,
                                    ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.isRunning = blockEntity.isRunning;
        state.renderParadox = blockEntity.renderParadox;
        state.targetType = blockEntity.targetType;
        state.machinePos = blockEntity.getBlockPos();
        if (state.isRunning) {
            float alpha = Mth.clamp(0.05f + (blockEntity.timeRunning / (float) blockEntity.getRunTime()) * 0.95f, 0.05f, 1.0f);
            state.runningAlpha = alpha;
            state.runningIntAlpha = (int) (alpha * 255);
            state.blocks = blockEntity.restoringBlocks;
            state.entities = blockEntity.getEntitiesFromNBT();
            state.restoringEntities = blockEntity.restoringEntites;
        } else {
            state.runningAlpha = 0.5f;
            state.runningIntAlpha = 175;
            state.blocks = blockEntity.getBlocksFromNBT();
            state.entities = blockEntity.getEntitiesFromNBT();
            state.restoringEntities = null;
        }
    }

    @Override
    public void submit(ParadoxMachineRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        super.submit(state, poseStack, submitNodeCollector, camera);
        // TODO(port, stage-16): re-render paradox blocks.
        // In 1.21.1 this used BlockRenderDispatcher.getBlockModel + ModelBlockRenderer.renderModelFaceAO
        // into OurRenderTypes.RenderBlockFade / RenderBlockFadeNoCull with a DireVertexConsumer(alpha).
        // 26.1 replacements:
        //   - model via Minecraft.getInstance().getModelManager().getBlockStateModelSet().get(state)
        //   - submitNodeCollector.submitBlockModel(...) OR submitCustomGeometry with ModelBlockRenderer.tesselateBlock +
        //     a BlockQuadOutput bridge (RENDER_PORTING.md §5.3) if per-quad alpha modulation is needed.
        //   - AmbientOcclusionFace path is gone; drop AO or run tesselateBlock and filter by direction.
        // Iterate state.blocks skipping entries where restoringBlocks-as-collection doesn't contain the pos
        // when state.isRunning is true; else use getBlocksFromNBT (already in state.blocks).

        // TODO(port, stage-18): re-render paradox entities with transparency.
        // The old code invoked EntityRenderDispatcher and LivingEntityRenderer#getModel + setupAnim + renderToBuffer.
        // 26.1 moved LivingEntity rendering to a render-state pipeline; reproducing per-frame mock entity animation
        // now requires building each LivingEntityRenderState by hand. Revisit after Stage 18 is clean.
    }
}
