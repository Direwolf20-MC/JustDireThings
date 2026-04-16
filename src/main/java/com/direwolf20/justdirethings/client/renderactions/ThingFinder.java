package com.direwolf20.justdirethings.client.renderactions;

import com.direwolf20.justdirethings.client.particles.alwaysvisibleparticle.AlwaysVisibleParticleData;
import com.direwolf20.justdirethings.client.renderers.OurRenderTypes;
import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.util.MiscHelpers;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.common.Tags;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class ThingFinder {
    public static long xRayStartTime;
    public static long blockParticlesStartTime;
    private static long lastBlockDrawTime = 0;
    private static long lastEntityDrawTime = 0;
    public static long entityParticlesStartTime;

    public static List<BlockPos> oreBlocksList = new ArrayList<>();
    public static List<Entity> entityList = new ArrayList<>();
    private static List<BlockPos> xRayBlocks = new ArrayList<>();

    public static void render(RenderLevelStageEvent evt, Player player, ItemStack heldItemMain) {
        if (((System.currentTimeMillis() - xRayStartTime) / 1000) < 10)
            drawXRay(evt, player);
        if (!oreBlocksList.isEmpty()) {
            long currentTime = System.currentTimeMillis();
            if ((currentTime - blockParticlesStartTime) < 10000) {
                if ((currentTime - lastBlockDrawTime) >= 500) {
                    drawParticlesOre(evt, player);
                    lastBlockDrawTime = currentTime;
                }
            } else
                oreBlocksList.clear();
        }
        if (!entityList.isEmpty()) {
            long currentTime = System.currentTimeMillis();
            if ((currentTime - entityParticlesStartTime) < 10000) {
                if ((currentTime - lastEntityDrawTime) >= 500) {
                    discoverMobs(player, false);
                    drawParticlesEntity(evt, player);
                    lastEntityDrawTime = currentTime;
                }
            } else
                entityList.clear();
        }
    }

    public static void discover(Player player, Ability toolAbility, ItemStack itemStack) {
        if (toolAbility.equals(Ability.MOBSCANNER))
            discoverMobs(player, true);
        else if (toolAbility.equals(Ability.ORESCANNER) || toolAbility.equals(Ability.OREXRAY))
            discoverOres(player, toolAbility, itemStack);
    }

    private static void discoverOres(Player player, Ability toolAbility, ItemStack itemStack) {
        oreBlocksList.clear();
        BlockPos playerPos = player.getOnPos();
        int radius = 10;
        oreBlocksList = BlockPos.betweenClosedStream(playerPos.offset(-radius, -radius, -radius), playerPos.offset(radius, radius, radius))
                .filter(blockPos -> isValidBlock(blockPos, player, itemStack))
                .map(BlockPos::immutable)
                .collect(Collectors.toList());
        if (toolAbility.equals(Ability.OREXRAY)) {
            xRayStartTime = System.currentTimeMillis();
            xRayBlocks = new ArrayList<>(oreBlocksList);
        } else if (toolAbility.equals(Ability.ORESCANNER)) {
            blockParticlesStartTime = System.currentTimeMillis();
        }
    }

    private static boolean isValidBlock(BlockPos blockPos, Player player, ItemStack itemStack) {
        BlockState blockState = player.level().getBlockState(blockPos);
        if (!blockState.is(Tags.Blocks.ORES))
            return false;
        return itemStack.isCorrectToolForDrops(blockState);
    }

    private static void discoverMobs(Player player, boolean startTimer) {
        entityList.clear();
        BlockPos playerPos = player.getOnPos();
        int radius = 10;

        entityList = player.level().getEntities(player, AABB.encapsulatingFullBlocks(playerPos.offset(-radius, -radius, -radius), playerPos.offset(radius, radius, radius)))
                .stream()
                .filter(entity -> entity instanceof Monster)
                .collect(Collectors.toList());
        if (startTimer)
            entityParticlesStartTime = System.currentTimeMillis();
    }

    public static void drawParticlesOre(RenderLevelStageEvent evt, Player player) {
        Random random = new Random();
        Level level = player.level();
        AlwaysVisibleParticleData data = new AlwaysVisibleParticleData(BuiltInRegistries.PARTICLE_TYPE.getKey(ParticleTypes.HAPPY_VILLAGER));
        for (int i = 0; i < 2; i++) {
            for (BlockPos pos : oreBlocksList) {
                double d0 = (double) pos.getX() + random.nextDouble();
                double d1 = (double) pos.getY() + random.nextDouble();
                double d2 = (double) pos.getZ() + random.nextDouble();
                level.addParticle(data, d0, d1, d2, 0.0, 0.0, 0.0);
            }
        }
    }

    public static void drawParticlesEntity(RenderLevelStageEvent evt, Player player) {
        Level level = player.level();
        AlwaysVisibleParticleData data = new AlwaysVisibleParticleData(BuiltInRegistries.PARTICLE_TYPE.getKey(ParticleTypes.SOUL));
        for (int i = 0; i < 5; i++) {
            for (Entity entity : entityList) {
                AABB aabb = entity.getBoundingBox();
                double d0 = MiscHelpers.nextDouble(aabb.minX, aabb.maxX);
                double d1 = MiscHelpers.nextDouble(aabb.minY, aabb.maxY);
                double d2 = MiscHelpers.nextDouble(aabb.minZ, aabb.maxZ);
                level.addParticle(data, d0, d1, d2, 0.0, 0.0, 0.0);
            }
        }
    }

    public static void drawXRay(RenderLevelStageEvent evt, Player player) {
        if (xRayBlocks == null || xRayBlocks.isEmpty()) return;

        Minecraft mc = Minecraft.getInstance();
        Level level = player.level();
        MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
        RandomSource random = RandomSource.create();

        Vec3 projectedView = mc.gameRenderer.getMainCamera().position();
        PoseStack matrix = evt.getPoseStack();
        matrix.pushPose();
        matrix.translate(-projectedView.x(), -projectedView.y(), -projectedView.z());

        for (BlockPos pos : xRayBlocks) {
            BlockState renderState = level.getBlockState(pos);
            if (renderState.isAir()) continue;

            BlockStateModel model = mc.getModelManager().getBlockStateModelSet().get(renderState);
            List<BlockStateModelPart> parts = new ArrayList<>();
            model.collectParts(random, parts);

            matrix.pushPose();
            matrix.translate(pos.getX(), pos.getY(), pos.getZ());
            float translateF = 1f / 2000f;
            matrix.translate(translateF, translateF, translateF);
            float scaleF = 1f / 1000f;
            matrix.scale(1 - scaleF, 1 - scaleF, 1 - scaleF);

            try {
                com.mojang.blaze3d.vertex.VertexConsumer buffer = bufferSource.getBuffer(OurRenderTypes.OreXRAY);
                for (BlockStateModelPart part : parts) {
                    renderPart(part, matrix, buffer);
                }
            } catch (Exception e) {
                // Swallow broken models rather than crash the render pipeline
            }
            matrix.popPose();
        }

        matrix.popPose();
        bufferSource.endBatch(OurRenderTypes.OreXRAY);
    }

    private static void renderPart(BlockStateModelPart part,
                                   PoseStack matrix, com.mojang.blaze3d.vertex.VertexConsumer buffer) {
        com.mojang.blaze3d.vertex.QuadInstance instance = new com.mojang.blaze3d.vertex.QuadInstance();
        instance.setColor(-1);
        instance.setLightCoords(net.minecraft.util.LightCoordsUtil.FULL_BRIGHT);
        instance.setOverlayCoords(OverlayTexture.NO_OVERLAY);
        com.mojang.blaze3d.vertex.PoseStack.Pose pose = matrix.last();

        for (net.minecraft.core.Direction direction : net.minecraft.core.Direction.values()) {
            for (net.minecraft.client.resources.model.geometry.BakedQuad quad : part.getQuads(direction)) {
                buffer.putBakedQuad(pose, quad, instance);
            }
        }
        for (net.minecraft.client.resources.model.geometry.BakedQuad quad : part.getQuads(null)) {
            buffer.putBakedQuad(pose, quad, instance);
        }
    }
}
