package com.direwolf20.justdirethings.client.renderactions;

import com.direwolf20.justdirethings.client.particles.alwaysvisibleparticle.AlwaysVisibleParticleData;
import com.direwolf20.justdirethings.client.renderers.DireBufferBuilder;
import com.direwolf20.justdirethings.common.items.tools.utils.Ability;
import com.direwolf20.justdirethings.util.MiscHelpers;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.VertexSorting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
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
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.common.Tags;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class ThingFinder {
    public static long xRayStartTime;
    public static long blockParticlesStartTime;
    private static long lastBlockDrawTime = 0; // The last time particles were drawn
    private static long lastEntityDrawTime = 0; // The last time particles were drawn
    public static long entityParticlesStartTime;
    //public static Map<BlockPos, BlockState> oreMap = new HashMap<>();
    public static List<BlockPos> oreBlocksList = new ArrayList<>();
    public static List<Entity> entityList = new ArrayList<>();
    private static int sortCounter = 0;

    //A eBufferBuilder, so we can draw the render
    private static final DireBufferBuilder builder = new DireBufferBuilder(RenderType.cutout().bufferSize());
    //Cached SortStates used for re-sorting every so often
    private static BufferBuilder.SortState sortState;
    //Vertex Buffer to buffer the different ores.
    private static final VertexBuffer vertexBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
    //The render type
    private static final RenderType renderType = RenderType.translucent();

    public static void render(RenderLevelStageEvent evt, Player player, ItemStack heldItemMain) {
        if (((System.currentTimeMillis() - xRayStartTime) / 1000) < 10)  //Lasts for 10 seconds
            drawVBO(evt, player);
        if (!oreBlocksList.isEmpty()) {
            long currentTime = System.currentTimeMillis();
            if ((currentTime - blockParticlesStartTime) < 10000) { //Lasts for 10 seconds
                if ((currentTime - lastBlockDrawTime) >= 500) { //Every 1/2 second
                    drawParticlesOre(evt, player);
                    lastBlockDrawTime = currentTime;
                }
            } else
                oreBlocksList.clear();
        }
        if (!entityList.isEmpty()) {
            long currentTime = System.currentTimeMillis();
            if ((currentTime - entityParticlesStartTime) < 10000) { //Lasts for 10 seconds
                if ((currentTime - lastEntityDrawTime) >= 500) { //Every 1/2 second
                    discoverMobs(player, false);
                    drawParticlesEntity(evt, player);
                    lastEntityDrawTime = currentTime;
                }
            } else
                entityList.clear();
        }
    }

    public static void discover(Player player, Ability toolAbility) {
        if (toolAbility.equals(Ability.MOBSCANNER))
            discoverMobs(player, true);
        else if (toolAbility.equals(Ability.ORESCANNER) || toolAbility.equals(Ability.OREXRAY))
            discoverOres(player, toolAbility);
    }

    private static void discoverOres(Player player, Ability toolAbility) {
        oreBlocksList.clear();
        BlockPos playerPos = player.getOnPos();
        int radius = 10; //TODO 50 seems to be ok perf wise but ridiculous
        oreBlocksList = BlockPos.betweenClosedStream(playerPos.offset(-radius, -radius, -radius), playerPos.offset(radius, radius, radius))
                .filter(blockPos -> player.level().getBlockState(blockPos).getTags().anyMatch(tag -> tag.equals(Tags.Blocks.ORES)))
                .map(BlockPos::immutable)
                .collect(Collectors.toList());
        if (toolAbility.equals(Ability.OREXRAY)) {
            xRayStartTime = System.currentTimeMillis();
            generateVBO(player);
        } else if (toolAbility.equals(Ability.ORESCANNER)) {
            blockParticlesStartTime = System.currentTimeMillis();
        }
    }

    private static void discoverMobs(Player player, boolean startTimer) {
        entityList.clear();
        BlockPos playerPos = player.getOnPos();
        int radius = 10; //TODO 50 seems to be ok perf wise but ridiculous

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

    public static void generateVBO(Player player) {
        if (oreBlocksList == null || oreBlocksList.isEmpty()) return;
        PoseStack matrix = new PoseStack(); //Create a new matrix stack for use in the buffer building process
        BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
        ModelBlockRenderer modelBlockRenderer = dispatcher.getModelRenderer();
        final RandomSource random = RandomSource.create();
        Level level = player.level();

        builder.begin(renderType.mode(), renderType.format());

        for (BlockPos pos : oreBlocksList) {
            BlockState renderState = level.getBlockState(pos);
            if (renderState.isAir()) continue;

            BakedModel ibakedmodel = dispatcher.getBlockModel(renderState);
            matrix.pushPose();
            matrix.translate(pos.getX(), pos.getY(), pos.getZ());


            for (RenderType renderTypeDraw : ibakedmodel.getRenderTypes(renderState, random, ModelData.EMPTY)) {
                try {
                    modelBlockRenderer.tesselateBlock(level, ibakedmodel, renderState, pos.above(255), matrix, builder, false, random, renderState.getSeed(pos), OverlayTexture.NO_OVERLAY, ibakedmodel.getModelData(level, pos, renderState, ModelData.EMPTY), renderTypeDraw);
                } catch (Exception e) {
                    //System.out.println(e);
                }
            }
            matrix.popPose();
        }
        //Sort all the builder's vertices and then upload them to the vertex buffer
        Vec3 projectedView = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        Vector3f sortPos = new Vector3f((float) projectedView.x, (float) projectedView.y, (float) projectedView.z);

        builder.setQuadSorting(VertexSorting.byDistance(sortPos));
        sortState = builder.getSortState();
        vertexBuffer.bind();
        vertexBuffer.upload(builder.end());
        VertexBuffer.unbind();

        oreBlocksList.clear();
    }

    public static void drawVBO(RenderLevelStageEvent evt, Player player) {
        if (vertexBuffer == null || oreBlocksList == null) {
            return;
        }

        Vec3 projectedView = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        BlockPos renderPos = player.getOnPos();

        //Sort every <X> Frames to prevent screendoor effect
        if (sortCounter > 20) {
            sortAll(renderPos);
            sortCounter = 0;
        } else {
            sortCounter++;
        }

        PoseStack matrix = evt.getPoseStack();
        matrix.pushPose();
        matrix.translate(-projectedView.x(), -projectedView.y(), -projectedView.z());
        //matrix.translate(0,5,0);
        //Draw the renders in the specified order
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        try {
            if (vertexBuffer.getFormat() == null)
                return; //IDE says this is never null, but if we remove this check we crash because its null so....
            renderType.setupRenderState();
            vertexBuffer.bind();
            vertexBuffer.drawWithShader(matrix.last().pose(), new Matrix4f(evt.getProjectionMatrix()), RenderSystem.getShader());
            VertexBuffer.unbind();
            renderType.clearRenderState();
        } catch (Exception e) {
            System.out.println(e);
        }
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        matrix.popPose();
    }

    public static void sortAll(BlockPos lookingAt) {
        BufferBuilder.RenderedBuffer renderedBuffer = sort(lookingAt, renderType);
        vertexBuffer.bind();
        vertexBuffer.upload(renderedBuffer);
        VertexBuffer.unbind();

    }

    //Sort the render type we pass in - using DireBufferBuilder because we want to sort in the opposite direction from normal
    public static BufferBuilder.RenderedBuffer sort(BlockPos lookingAt, RenderType renderType) {
        Vec3 projectedView = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        Vector3f sortPos = new Vector3f((float) projectedView.x, (float) projectedView.y, (float) projectedView.z);
        builder.begin(renderType.mode(), renderType.format());
        builder.restoreSortState(sortState);
        builder.setQuadSorting(VertexSorting.byDistance(sortPos));
        sortState = builder.getSortState();
        return builder.end();
    }
}
