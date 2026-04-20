package com.direwolf20.justdirethings.client.renderers;

import com.direwolf20.justdirethings.client.FluidModels;
import com.direwolf20.justdirethings.setup.JDTRegistration;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.SubmitCustomGeometryEvent;

import java.util.HashSet;
import java.util.Set;

/**
 * Overlays a per-frame rainbow color on top of every loaded polymorphic fluid source block,
 * so the fluid cycles color smoothly without forcing chunk remeshes. The fluid's baked-in
 * tint is white ({@link FluidModels} colorInWorld), so the overlay supplies all of the color.
 * <p>
 * Positions are refreshed every {@link #RESCAN_INTERVAL_TICKS} ticks by walking loaded chunks
 * around the player and filtering sections via {@code LevelChunkSection.maybeHas}, which is
 * a cheap palette-level check.
 * <p>
 * <b>Currently unwired.</b> Polymorphic fluid coloring was moved to a sprite-source approach
 * (see {@code com.direwolf20.justdirethings.client.fluid.RainbowFluidSpriteSource}) that
 * re-tints the atlas sprite each tick, which avoids the per-tick chunk scan and per-frame
 * custom geometry this class used. Kept in place for easy rollback — to restore, re-add
 * {@code @EventBusSubscriber(modid = JustDireThings.MODID, value = Dist.CLIENT)} above the
 * class and drop the sprite-source wiring.
 */
public final class PolymorphicFluidOverlayRenderer {
    /**
     * How often (in ticks) to rescan nearby chunks for polymorphic fluid source positions.
     */
    private static final int RESCAN_INTERVAL_TICKS = 1;
    /**
     * Chunk radius around the player to scan. 8 covers a 16-chunk-diameter window.
     */
    private static final int SCAN_CHUNK_RADIUS = 8;
    /**
     * Alpha of the overlay tint; low enough that the underlying fluid texture shows through.
     * Temporarily non-final + refreshed every tick so the value can be edited in-place and
     * picked up via hotswap without relaunching Minecraft.
     */
    private static float OVERLAY_ALPHA = 0.5f;

    private static final Set<BlockPos> sources = new HashSet<>();
    private static int ticksUntilRescan = 0;

    private PolymorphicFluidOverlayRenderer() {
    }

    @SubscribeEvent
    static void onClientTick(ClientTickEvent.Post event) {
        // Re-read the literal every tick so hotswap picks it up without a restart.
        OVERLAY_ALPHA = currentOverlayAlpha();
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) {
            sources.clear();
            ticksUntilRescan = 0;
            return;
        }
        if (ticksUntilRescan-- > 0) return;
        ticksUntilRescan = RESCAN_INTERVAL_TICKS;
        rescan(mc.level, mc.player);
    }

    /**
     * Edit this return value and hotswap — the tick handler picks it up live.
     */
    private static float currentOverlayAlpha() {
        return 0.3f;
    }

    private static void rescan(ClientLevel level, Player player) {
        sources.clear();
        Fluid targetSource = JDTRegistration.POLYMORPHIC_FLUID_SOURCE.get();
        Fluid targetFlowing = JDTRegistration.POLYMORPHIC_FLUID_FLOWING.get();
        int centerChunkX = SectionPos.blockToSectionCoord((int) player.getX());
        int centerChunkZ = SectionPos.blockToSectionCoord((int) player.getZ());
        for (int dx = -SCAN_CHUNK_RADIUS; dx <= SCAN_CHUNK_RADIUS; dx++) {
            for (int dz = -SCAN_CHUNK_RADIUS; dz <= SCAN_CHUNK_RADIUS; dz++) {
                LevelChunk chunk = level.getChunkSource().getChunk(centerChunkX + dx, centerChunkZ + dz, false);
                if (chunk == null) continue;
                LevelChunkSection[] sections = chunk.getSections();
                int minSectionY = chunk.getMinSectionY();
                for (int i = 0; i < sections.length; i++) {
                    LevelChunkSection section = sections[i];
                    if (section == null || section.hasOnlyAir()) continue;
                    if (!section.maybeHas(state -> {
                        Fluid f = state.getFluidState().getType();
                        return f == targetSource || f == targetFlowing;
                    })) continue;
                    int baseY = SectionPos.sectionToBlockCoord(minSectionY + i);
                    int baseX = chunk.getPos().getMinBlockX();
                    int baseZ = chunk.getPos().getMinBlockZ();
                    for (int y = 0; y < 16; y++) {
                        for (int x = 0; x < 16; x++) {
                            for (int z = 0; z < 16; z++) {
                                Fluid f = section.getFluidState(x, y, z).getType();
                                if (f == targetSource || f == targetFlowing) {
                                    sources.add(new BlockPos(baseX + x, baseY + y, baseZ + z));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    static void onSubmitCustomGeometry(SubmitCustomGeometryEvent event) {
        if (sources.isEmpty()) return;
        Minecraft mc = Minecraft.getInstance();
        ClientLevel level = mc.level;
        if (level == null) return;

        int color = FluidModels.currentRainbowArgb(mc.getDeltaTracker().getGameTimeDeltaPartialTick(true));
        float r = ((color >> 16) & 0xFF) / 255.0f;
        float g = ((color >> 8) & 0xFF) / 255.0f;
        float b = (color & 0xFF) / 255.0f;

        Vec3 cam = event.getLevelRenderState().cameraRenderState.pos;
        PoseStack poseStack = event.getPoseStack();
        SubmitNodeCollector collector = event.getSubmitNodeCollector();

        Fluid source = JDTRegistration.POLYMORPHIC_FLUID_SOURCE.get();

        poseStack.pushPose();
        poseStack.translate(-cam.x, -cam.y, -cam.z);
        // Snapshot so render lambdas don't observe mutation mid-frame. For each position,
        // compute the four corner heights the same way vanilla's FluidRenderer does, so the
        // overlay quad matches the slanted surface between neighboring flowing blocks.
        int count = sources.size();
        BlockPos[] positions = new BlockPos[count];
        float[] cornerHeights = new float[count * 4]; // NW, NE, SE, SW
        int idx = 0;
        for (BlockPos pos : sources) {
            FluidState fs = level.getFluidState(pos);
            if (!fs.getType().isSame(source)) continue;
            float heightSelf = getHeight(level, source, pos);
            float nw, ne, se, sw;
            if (heightSelf >= 1.0F) {
                nw = ne = se = sw = 1.0F;
            } else {
                float hN = getHeight(level, source, pos.north());
                float hS = getHeight(level, source, pos.south());
                float hE = getHeight(level, source, pos.east());
                float hW = getHeight(level, source, pos.west());
                nw = averageCornerHeight(level, source, heightSelf, hN, hW, pos.north().west());
                ne = averageCornerHeight(level, source, heightSelf, hN, hE, pos.north().east());
                se = averageCornerHeight(level, source, heightSelf, hS, hE, pos.south().east());
                sw = averageCornerHeight(level, source, heightSelf, hS, hW, pos.south().west());
            }
            positions[idx] = pos;
            cornerHeights[idx * 4] = nw;
            cornerHeights[idx * 4 + 1] = ne;
            cornerHeights[idx * 4 + 2] = se;
            cornerHeights[idx * 4 + 3] = sw;
            idx++;
        }
        int finalCount = idx;
        collector.submitCustomGeometry(poseStack, OurRenderTypes.TRANSPARENT_BOX, (pose, buffer) -> {
            for (int i = 0; i < finalCount; i++) {
                BlockPos pos = positions[i];
                float nw = cornerHeights[i * 4];
                float ne = cornerHeights[i * 4 + 1];
                float se = cornerHeights[i * 4 + 2];
                float sw = cornerHeights[i * 4 + 3];
                drawOverlayTop(pose, buffer, pos, nw, ne, se, sw, r, g, b, OVERLAY_ALPHA);
            }
        });
        poseStack.popPose();
    }

    private static float getHeight(ClientLevel level, Fluid source, BlockPos pos) {
        var state = level.getBlockState(pos);
        FluidState fs = state.getFluidState();
        if (source.isSame(fs.getType())) {
            Fluid above = level.getFluidState(pos.above()).getType();
            return source.isSame(above) ? 1.0F : fs.getOwnHeight();
        }
        return !state.isSolid() ? 0.0F : -1.0F;
    }

    /**
     * Mirror of {@code FluidRenderer.calculateAverageHeight}.
     */
    private static float averageCornerHeight(ClientLevel level, Fluid source, float heightSelf,
                                             float h1, float h2, BlockPos cornerPos) {
        if (h1 >= 1.0F || h2 >= 1.0F) return 1.0F;
        float[] weighted = new float[2];
        if (h1 > 0.0F || h2 > 0.0F) {
            float heightCorner = getHeight(level, source, cornerPos);
            if (heightCorner >= 1.0F) return 1.0F;
            addWeighted(weighted, heightCorner);
        }
        addWeighted(weighted, heightSelf);
        addWeighted(weighted, h1);
        addWeighted(weighted, h2);
        return weighted[0] / weighted[1];
    }

    private static void addWeighted(float[] weighted, float h) {
        if (h >= 0.8F) {
            weighted[0] += h * 10.0F;
            weighted[1] += 10.0F;
        } else if (h >= 0.0F) {
            weighted[0] += h;
            weighted[1]++;
        }
    }

    private static void drawOverlayTop(PoseStack.Pose pose, VertexConsumer buffer, BlockPos pos,
                                       float nw, float ne, float se, float sw,
                                       float r, float g, float b, float alpha) {
        // Undercut by 0.002 so our overlay doesn't Z-fight the fluid top (vanilla also subtracts
        // 0.001 from top vertices to avoid fighting with the block above).
        float bx = pos.getX();
        float by = pos.getY();
        float bz = pos.getZ();
        // Corner order NW, SW, SE, NE (matches vanilla's top-face winding).
        quad(pose, buffer, r, g, b, alpha,
                bx, by + nw - 0.002f, bz,
                bx, by + sw - 0.002f, bz + 1.0f,
                bx + 1.0f, by + se - 0.002f, bz + 1.0f,
                bx + 1.0f, by + ne - 0.002f, bz);

        // Side faces, from baseline up to the two neighboring corner heights. Only emit a side
        // when the block in that direction isn't also polymorphic fluid — otherwise we'd paint
        // a seam between two adjacent overlays. (Baseline sits a hair above the floor to avoid
        // Z-fighting the block below; sideInset pulls the face slightly inward off the block
        // boundary to avoid Z-fighting the neighboring block's face.)
        float baseY = by + 0.001f;
        float sideInset = 0.001f;
        // North (-Z): NW (left) to NE (right), viewed from outside
        if (!sources.contains(pos.north())) {
            quad(pose, buffer, r, g, b, alpha,
                    bx + 1.0f, baseY, bz + sideInset,
                    bx, baseY, bz + sideInset,
                    bx, by + nw - 0.002f, bz + sideInset,
                    bx + 1.0f, by + ne - 0.002f, bz + sideInset);
        }
        // South (+Z): SW (left) to SE (right), viewed from outside
        if (!sources.contains(pos.south())) {
            quad(pose, buffer, r, g, b, alpha,
                    bx, baseY, bz + 1.0f - sideInset,
                    bx + 1.0f, baseY, bz + 1.0f - sideInset,
                    bx + 1.0f, by + se - 0.002f, bz + 1.0f - sideInset,
                    bx, by + sw - 0.002f, bz + 1.0f - sideInset);
        }
        // West (-X): SW (left) to NW (right), viewed from outside
        if (!sources.contains(pos.west())) {
            quad(pose, buffer, r, g, b, alpha,
                    bx + sideInset, baseY, bz,
                    bx + sideInset, baseY, bz + 1.0f,
                    bx + sideInset, by + sw - 0.002f, bz + 1.0f,
                    bx + sideInset, by + nw - 0.002f, bz);
        }
        // East (+X): NE (left) to SE (right), viewed from outside
        if (!sources.contains(pos.east())) {
            quad(pose, buffer, r, g, b, alpha,
                    bx + 1.0f - sideInset, baseY, bz + 1.0f,
                    bx + 1.0f - sideInset, baseY, bz,
                    bx + 1.0f - sideInset, by + ne - 0.002f, bz,
                    bx + 1.0f - sideInset, by + se - 0.002f, bz + 1.0f);
        }
        // Bottom face, viewed from below. Only emit if the block below isn't also polymorphic.
        if (!sources.contains(pos.below())) {
            quad(pose, buffer, r, g, b, alpha,
                    bx, baseY, bz,
                    bx + 1.0f, baseY, bz,
                    bx + 1.0f, baseY, bz + 1.0f,
                    bx, baseY, bz + 1.0f);
        }
    }

    private static void quad(PoseStack.Pose pose, VertexConsumer buffer,
                             float r, float g, float b, float alpha,
                             float x0, float y0, float z0,
                             float x1, float y1, float z1,
                             float x2, float y2, float z2,
                             float x3, float y3, float z3) {
        buffer.addVertex(pose.pose(), x0, y0, z0).setColor(r, g, b, alpha);
        buffer.addVertex(pose.pose(), x1, y1, z1).setColor(r, g, b, alpha);
        buffer.addVertex(pose.pose(), x2, y2, z2).setColor(r, g, b, alpha);
        buffer.addVertex(pose.pose(), x3, y3, z3).setColor(r, g, b, alpha);
    }
}
