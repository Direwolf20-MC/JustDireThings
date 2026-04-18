package com.direwolf20.justdirethings.client.blockentityrenders;

import com.direwolf20.justdirethings.client.blockentityrenders.baseber.AreaAffectingBER;
import com.direwolf20.justdirethings.client.renderers.OurRenderTypes;
import com.direwolf20.justdirethings.common.blockentities.ParadoxMachineBE;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.QuadInstance;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.*;

public class ParadoxMachineBER extends AreaAffectingBER<ParadoxMachineBE, ParadoxMachineBER.ParadoxMachineRenderState> {

    public static class ParadoxMachineRenderState extends AreaAffectingRenderState {
        public boolean isRunning;
        public boolean renderParadox;
        public int targetType;
        public float runningAlpha = 1f;
        public int runningIntAlpha = 255;
        public @Nullable Map<BlockPos, BlockState> blocks;
        public @Nullable Map<Vec3, PreparedMob> entities;
        public @Nullable Set<Vec3> restoringEntities; // Used only while running, membership test.
        public BlockPos machinePos = BlockPos.ZERO;
    }

    /**
     * Pre-extracted render state + model accessors for one captured/restored mob.
     */
    public static final class PreparedMob {
        public final LivingEntityRenderState state;
        public final EntityModel<LivingEntityRenderState> model;
        public final Identifier texture;

        public PreparedMob(LivingEntityRenderState state, EntityModel<LivingEntityRenderState> model, Identifier texture) {
            this.state = state;
            this.model = model;
            this.texture = texture;
        }
    }

    private final EntityRenderDispatcher dispatcher;

    public ParadoxMachineBER(BlockEntityRendererProvider.Context context) {
        this.dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
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

        Level level = blockEntity.getLevel();
        Map<BlockPos, BlockState> blocks;
        Map<Vec3, LivingEntity> rawEntities;

        if (state.isRunning) {
            float alpha = Mth.clamp(0.05f + (blockEntity.timeRunning / (float) blockEntity.getRunTime()) * 0.95f, 0.05f, 1.0f);
            state.runningAlpha = alpha;
            state.runningIntAlpha = (int) (alpha * 255);
            blocks = blockEntity.restoringBlocks;
            rawEntities = blockEntity.getEntitiesFromNBT();
            state.restoringEntities = new HashSet<>(blockEntity.restoringEntites);
        } else {
            state.runningAlpha = 0.5f;
            state.runningIntAlpha = 175;
            blocks = blockEntity.getBlocksFromNBT();
            rawEntities = blockEntity.getEntitiesFromNBT();
            state.restoringEntities = null;
        }

        // Filter blocks against replaceability on the client level, so we don't draw previews over
        // blocks that couldn't be restored anyway (matches the old `canBeReplaced()` guard).
        Map<BlockPos, BlockState> filteredBlocks = new HashMap<>();
        if (blocks != null && level != null) {
            for (Map.Entry<BlockPos, BlockState> e : blocks.entrySet()) {
                BlockPos pos = e.getKey();
                if (level.getBlockState(pos).canBeReplaced()) {
                    filteredBlocks.put(pos, e.getValue());
                }
            }
        }
        state.blocks = filteredBlocks;

        // Pre-extract each mob's LivingEntityRenderState + capture its renderer's model/texture so
        // submit() can call submitModel(...) with a fade-enabled tintedColor.
        Map<Vec3, PreparedMob> preparedEntities = new HashMap<>();
        if (rawEntities != null) {
            for (Map.Entry<Vec3, LivingEntity> e : rawEntities.entrySet()) {
                Vec3 pos = e.getKey();
                if (state.isRunning && state.restoringEntities != null && !state.restoringEntities.contains(pos))
                    continue;
                LivingEntity entity = e.getValue();
                if (entity == null) continue;
                // Keep yBodyRotO / yHeadRotO in sync with the NBT-loaded current values so the extracted
                // state doesn't interpolate from a stale previous frame (the mob is transient per frame).
                entity.yBodyRotO = entity.yBodyRot;
                entity.yHeadRotO = entity.yHeadRot;
                PreparedMob prepared = prepareMob(entity, partialTicks);
                if (prepared != null) preparedEntities.put(pos, prepared);
            }
        }
        state.entities = preparedEntities;
    }

    @SuppressWarnings("unchecked")
    private @Nullable PreparedMob prepareMob(LivingEntity entity, float partialTicks) {
        EntityRenderer<?, ?> rawRenderer = this.dispatcher.getRenderer(entity);
        if (!(rawRenderer instanceof LivingEntityRenderer<?, ?, ?> livingRenderer)) return null;
        try {
            var state = ((EntityRenderer<LivingEntity, LivingEntityRenderState>) rawRenderer).createRenderState(entity, partialTicks);
            var renderer = (LivingEntityRenderer<LivingEntity, LivingEntityRenderState, EntityModel<LivingEntityRenderState>>) livingRenderer;
            EntityModel<LivingEntityRenderState> model = renderer.getModel();
            Identifier texture = renderer.getTextureLocation(state);
            if (texture == null) return null;
            return new PreparedMob(state, model, texture);
        } catch (Throwable ignored) {
            return null;
        }
    }

    @Override
    public void submit(ParadoxMachineRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        super.submit(state, poseStack, submitNodeCollector, camera);

        if (!shouldRenderPreview(state)) return;

        // Blocks.
        if (state.blocks != null && !state.blocks.isEmpty() && (state.isRunning || state.targetType == 0 || state.targetType == 1)) {
            renderBlocks(state, poseStack, submitNodeCollector);
        }
        // Entities.
        if (state.entities != null && !state.entities.isEmpty() && (state.isRunning || state.targetType == 0 || state.targetType == 2)) {
            renderEntities(state, poseStack, submitNodeCollector);
        }
    }

    private static boolean shouldRenderPreview(ParadoxMachineRenderState state) {
        return state.isRunning || state.renderParadox;
    }

    private void renderBlocks(ParadoxMachineRenderState state, PoseStack poseStack, SubmitNodeCollector collector) {
        Map<BlockPos, BlockState> blocks = state.blocks;
        if (blocks == null) return;

        int alphaByte = Math.max(0, Math.min(255, Math.round(state.runningAlpha * 255F)));
        int packedColor = (alphaByte << 24) | 0x00FFFFFF;
        RandomSource random = RandomSource.create();

        for (Map.Entry<BlockPos, BlockState> entry : blocks.entrySet()) {
            BlockPos pos = entry.getKey();
            BlockState blockState = entry.getValue();

            BlockStateModel model = Minecraft.getInstance().getModelManager().getBlockStateModelSet().get(blockState);
            random.setSeed(blockState.getSeed(pos));
            List<BlockStateModelPart> parts = new ArrayList<>();
            model.collectParts(random, parts);
            if (parts.isEmpty()) continue;

            boolean solidRender = blockState.isSolidRender();
            RenderType rt = solidRender ? OurRenderTypes.RenderBlockFade : OurRenderTypes.RenderBlockFadeNoCull;

            poseStack.pushPose();
            poseStack.translate(pos.getX() - state.machinePos.getX(),
                    pos.getY() - state.machinePos.getY(),
                    pos.getZ() - state.machinePos.getZ());

            // Build the per-direction skip set: if a neighbor is also a restoring block and it's solid-render,
            // skip this block's face toward that neighbor (prevents double-drawing interior faces).
            boolean[] skipDirection = new boolean[6];
            for (Direction dir : Direction.values()) {
                BlockPos neighbor = pos.relative(dir);
                BlockState neighborState = blocks.get(neighbor);
                if (neighborState != null && neighborState.isSolidRender()) {
                    skipDirection[dir.get3DDataValue()] = true;
                }
            }

            collector.submitCustomGeometry(poseStack, rt,
                    (pose, buffer) -> writeBlockQuads(parts, pose, buffer, packedColor, state.lightCoords, skipDirection));
            poseStack.popPose();
        }
    }

    private static void writeBlockQuads(List<BlockStateModelPart> parts, PoseStack.Pose pose, VertexConsumer buffer,
                                        int packedColor, int lightCoords, boolean[] skipDirection) {
        QuadInstance instance = new QuadInstance();
        instance.setColor(packedColor);
        instance.setLightCoords(lightCoords);
        for (BlockStateModelPart part : parts) {
            writeQuads(part.getQuads(null), pose, buffer, instance);
            for (Direction d : Direction.values()) {
                if (skipDirection[d.get3DDataValue()]) continue;
                writeQuads(part.getQuads(d), pose, buffer, instance);
            }
        }
    }

    private static void writeQuads(List<BakedQuad> quads, PoseStack.Pose pose, VertexConsumer buffer, QuadInstance instance) {
        for (BakedQuad quad : quads) {
            buffer.putBakedQuad(pose, quad, instance);
        }
    }

    private void renderEntities(ParadoxMachineRenderState state, PoseStack poseStack, SubmitNodeCollector collector) {
        Map<Vec3, PreparedMob> entities = state.entities;
        if (entities == null) return;

        int alphaByte = Math.max(0, Math.min(255, state.runningIntAlpha));
        int tintedColor = (alphaByte << 24) | 0x00FFFFFF;

        for (Map.Entry<Vec3, PreparedMob> entry : entities.entrySet()) {
            Vec3 worldPos = entry.getKey();
            PreparedMob mob = entry.getValue();

            poseStack.pushPose();
            poseStack.translate(worldPos.x - state.machinePos.getX(),
                    worldPos.y - state.machinePos.getY(),
                    worldPos.z - state.machinePos.getZ());

            // Replicate LivingEntityRenderer.submit transform chain for the model-only body render.
            float scale = mob.state.scale;
            poseStack.scale(scale, scale, scale);
            if (!mob.state.hasPose(Pose.SLEEPING)) {
                poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - mob.state.bodyRot));
            }
            poseStack.scale(-1.0F, -1.0F, 1.0F);
            poseStack.translate(0.0F, -1.501F, 0.0F);

            RenderType renderType = RenderTypes.entityTranslucent(mob.texture);
            int overlayCoords = LivingEntityRenderer.getOverlayCoords(mob.state, 0.0F);

            collector.submitModel(mob.model, mob.state, poseStack, renderType,
                    mob.state.lightCoords, overlayCoords, tintedColor, null, 0, null);

            poseStack.popPose();
        }
    }
}
