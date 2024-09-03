package com.direwolf20.justdirethings.client.blockentityrenders;

import com.direwolf20.justdirethings.client.blockentityrenders.baseber.AreaAffectingBER;
import com.direwolf20.justdirethings.client.renderers.DireVertexConsumer;
import com.direwolf20.justdirethings.client.renderers.OurRenderTypes;
import com.direwolf20.justdirethings.common.blockentities.ParadoxMachineBE;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.model.data.ModelData;

import java.util.BitSet;
import java.util.List;
import java.util.Map;

public class ParadoxMachineBER extends AreaAffectingBER {
    public ParadoxMachineBER(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(BlockEntity blockentity, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightsIn, int combinedOverlayIn) {
        super.render(blockentity, partialTicks, matrixStackIn, bufferIn, combinedLightsIn, combinedOverlayIn);
        if (!(blockentity instanceof ParadoxMachineBE paradoxMachineBE)) return;
        // Render blocks
        if (paradoxMachineBE.isRunning) {
            float alpha = Mth.clamp(0.05f + (paradoxMachineBE.timeRunning / (float) paradoxMachineBE.getRunTime()) * 0.95f, 0.05f, 1.0f);
            int intAlpha = (int) (alpha * 255);
            renderBlocks(paradoxMachineBE, matrixStackIn, bufferIn, combinedLightsIn, combinedOverlayIn, alpha, paradoxMachineBE.restoringBlocks);
            renderEntities(paradoxMachineBE, matrixStackIn, bufferIn, combinedLightsIn, combinedOverlayIn, intAlpha);
        } else {
        if (paradoxMachineBE.renderParadox) {
            int targetType = paradoxMachineBE.targetType;
            if (targetType == 0 || targetType == 1)
                renderBlocks(paradoxMachineBE, matrixStackIn, bufferIn, combinedLightsIn, combinedOverlayIn, 0.5f, paradoxMachineBE.getBlocksFromNBT());
            if (targetType == 0 || targetType == 2)
                renderEntities(paradoxMachineBE, matrixStackIn, bufferIn, combinedLightsIn, combinedOverlayIn, 175);
        }
        }
    }

    private void renderBlocks(ParadoxMachineBE paradoxMachineBE, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightsIn, int combinedOverlayIn, float alpha, Map<BlockPos, BlockState> blocksToRestore) {
        BlockRenderDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();
        Level level = paradoxMachineBE.getLevel();
        if (level == null) return;
        BlockColors blockColors = Minecraft.getInstance().getBlockColors();
        ModelBlockRenderer modelBlockRenderer = new ModelBlockRenderer(blockColors);
        BlockRenderDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRenderer();

        for (Map.Entry<BlockPos, BlockState> entry : blocksToRestore.entrySet()) {
            BlockPos blockPos = entry.getKey();
            BlockState renderState = entry.getValue();

            if (!level.getBlockState(blockPos).canBeReplaced())
                continue;

            float[] afloat = new float[Direction.values().length * 2];
            BitSet bitset = new BitSet(3);
            RandomSource randomSource = RandomSource.create();
            BlockPos.MutableBlockPos blockpos$mutableblockpos = blockPos.mutable();
            BakedModel ibakedmodel = blockrendererdispatcher.getBlockModel(renderState);

            matrixStackIn.pushPose();
            matrixStackIn.translate(blockPos.getX() - paradoxMachineBE.getBlockPos().getX(),
                    blockPos.getY() - paradoxMachineBE.getBlockPos().getY(),
                    blockPos.getZ() - paradoxMachineBE.getBlockPos().getZ());

            // Render the block as semi-transparent
            VertexConsumer builder = renderState.isSolidRender(level, blockPos) ? bufferIn.getBuffer(OurRenderTypes.RenderBlockFade) : bufferIn.getBuffer(OurRenderTypes.RenderBlockFadeNoCull);
            DireVertexConsumer direVertexConsumer = new DireVertexConsumer(builder, alpha);

            ModelBlockRenderer.AmbientOcclusionFace modelblockrenderer$ambientocclusionface = new ModelBlockRenderer.AmbientOcclusionFace();
            for (Direction direction : Direction.values()) {
                randomSource.setSeed(renderState.getSeed(blockPos));
                List<BakedQuad> list = ibakedmodel.getQuads(renderState, direction, randomSource, ModelData.EMPTY, null);
                if (!list.isEmpty()) {
                    blockpos$mutableblockpos.setWithOffset(blockPos, direction);
                    BlockPos testPos = blockPos.relative(direction);
                    boolean renderAdjacent = true;
                    if (blocksToRestore.containsKey(testPos)) {
                        BlockState otherState = blocksToRestore.get(testPos);
                        if (otherState.isSolidRender(level, testPos))
                            renderAdjacent = false;
                    }
                    if (renderAdjacent) {
                        modelBlockRenderer.renderModelFaceAO(level, renderState, blockPos, matrixStackIn, direVertexConsumer, list, afloat, bitset, modelblockrenderer$ambientocclusionface, combinedOverlayIn);
                    }
                }
            }
            randomSource.setSeed(renderState.getSeed(blockPos));
            List<BakedQuad> list = ibakedmodel.getQuads(renderState, null, randomSource, ModelData.EMPTY, null);
            if (!list.isEmpty()) {
                modelBlockRenderer.renderModelFaceAO(level, renderState, blockPos, matrixStackIn, direVertexConsumer, list, afloat, bitset, modelblockrenderer$ambientocclusionface, combinedOverlayIn);
            }

            matrixStackIn.popPose();
        }
    }

    private void renderEntities(ParadoxMachineBE paradoxMachineBE, PoseStack matrixStackIn, MultiBufferSource bufferIn, float partialTicks, int combinedLightsIn, int alpha) {
        Level level = paradoxMachineBE.getLevel();
        if (level == null) return;

        for (Map.Entry<Vec3, LivingEntity> entry : paradoxMachineBE.getEntitiesFromNBT().entrySet()) {
            Vec3 entityPos = entry.getKey();
            if (paradoxMachineBE.isRunning && !paradoxMachineBE.restoringEntites.contains(entityPos))
                continue;
            LivingEntity entity = entry.getValue();

            // Apply transformations and translate to entity position
            matrixStackIn.pushPose();
            matrixStackIn.translate(entityPos.x - paradoxMachineBE.getBlockPos().getX(),
                    entityPos.y - paradoxMachineBE.getBlockPos().getY(),
                    entityPos.z - paradoxMachineBE.getBlockPos().getZ());

            // Render the entity with transparency
            renderTransparentEntity(matrixStackIn, bufferIn, entity, partialTicks, combinedLightsIn, alpha);

            matrixStackIn.popPose();
        }
    }

    private void renderTransparentEntity(PoseStack matrixStackIn, MultiBufferSource bufferIn, LivingEntity entity, float partialTicks, int combinedLightsIn, int alpha) {
        EntityRenderDispatcher renderManager = Minecraft.getInstance().getEntityRenderDispatcher();
        EntityRenderer<? super LivingEntity> renderer = renderManager.getRenderer(entity);

        // Set up render type for transparency
        ResourceLocation resourceLocation = renderer.getTextureLocation(entity);
        if (resourceLocation == null) return; //Caused soaryn to crash without this
        RenderType renderType = RenderType.itemEntityTranslucentCull(resourceLocation);
        VertexConsumer vertexconsumer = bufferIn.getBuffer(renderType);

        // Calculate overlay and lighting
        int overlayCoords = LivingEntityRenderer.getOverlayCoords(entity, 0);

        // Ensure proper rotation and scaling
        float f = Mth.rotLerp(partialTicks, entity.yBodyRot, entity.yBodyRot);
        float f1 = Mth.rotLerp(partialTicks, entity.yHeadRot, entity.yHeadRot);
        float f2 = f1 - f;
        float f5 = Mth.lerp(partialTicks, entity.getXRot(), entity.getXRot());
        float f7 = 0;
        setupRotations(entity, matrixStackIn, f7, f, partialTicks);
        matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
        matrixStackIn.translate(0.0F, -1.501F, 0.0F);
        float f8 = 0.0F;
        float f4 = 0.0F;

        // Set transparency
        int packedARGB = (alpha << 24) | (255 << 16) | (255 << 8) | 255;

        // Render entity model with transparency
        if (renderer instanceof LivingEntityRenderer<?, ?> livingRenderer) {
            EntityModel<LivingEntity> entityModel = (EntityModel<LivingEntity>) livingRenderer.getModel();
            entityModel.attackTime = 0f;
            entityModel.riding = false;
            entityModel.young = entity.isBaby();
            entityModel.prepareMobModel(entity, f4, f8, partialTicks);
            entityModel.setupAnim(entity, f4, f8, f7, f2, f5);
            entityModel.renderToBuffer(matrixStackIn, vertexconsumer, combinedLightsIn, overlayCoords, packedARGB);
        }
    }

    protected static void setupRotations(LivingEntity pEntityLiving, PoseStack pPoseStack, float pAgeInTicks, float pRotationYaw, float pPartialTicks) {
        if (!pEntityLiving.hasPose(Pose.SLEEPING)) {
            pPoseStack.mulPose(Axis.YP.rotationDegrees(180.0F - pRotationYaw));
        }
    }

    protected static void scaleEntity(LivingEntity entity, PoseStack matrixStackIn) {
        float scale = 1F; // Adjust scale if needed
        matrixStackIn.scale(scale, scale, scale);
    }
}
