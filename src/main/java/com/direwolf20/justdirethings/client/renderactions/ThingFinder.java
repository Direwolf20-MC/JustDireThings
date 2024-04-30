package com.direwolf20.justdirethings.client.renderactions;

import com.direwolf20.justdirethings.client.particles.alwaysvisibleparticle.AlwaysVisibleParticleData;
import com.direwolf20.justdirethings.client.renderers.DireBufferBuilder;
import com.direwolf20.justdirethings.client.renderers.OurRenderTypes;
import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.util.MiscHelpers;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.VertexSorting;
import net.minecraft.client.Camera;
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
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.common.Tags;
import org.joml.Matrix4f;
import org.joml.Vector3f;

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
    private static final RenderType xRayRender = OurRenderTypes.OreXRAY;
    private static BlockPos renderedAtPos = BlockPos.ZERO;

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

    public static void discover(Player player, Ability toolAbility, ItemStack itemStack) {
        if (toolAbility.equals(Ability.MOBSCANNER))
            discoverMobs(player, true);
        else if (toolAbility.equals(Ability.ORESCANNER) || toolAbility.equals(Ability.OREXRAY))
            discoverOres(player, toolAbility, itemStack);
    }

    private static void discoverOres(Player player, Ability toolAbility, ItemStack itemStack) {
        oreBlocksList.clear();
        BlockPos playerPos = player.getOnPos();
        int radius = 10; //TODO 50 seems to be ok perf wise but ridiculous
        oreBlocksList = BlockPos.betweenClosedStream(playerPos.offset(-radius, -radius, -radius), playerPos.offset(radius, radius, radius))
                .filter(blockPos -> isValidBlock(blockPos, player, itemStack))
                .map(BlockPos::immutable)
                .collect(Collectors.toList());
        if (toolAbility.equals(Ability.OREXRAY)) {
            xRayStartTime = System.currentTimeMillis();
            generateVBO(player);
        } else if (toolAbility.equals(Ability.ORESCANNER)) {
            blockParticlesStartTime = System.currentTimeMillis();
        }
    }

    private static boolean isValidBlock(BlockPos blockPos, Player player, ItemStack itemStack) {
        BlockState blockState = player.level().getBlockState(blockPos);
        if (!blockState.getTags().anyMatch(tag -> tag.equals(Tags.Blocks.ORES)))
            return false;
        if (itemStack.getItem() instanceof TieredItem tieredItem) {
            return itemStack.isCorrectToolForDrops(blockState);
        }
        return true;
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
        renderedAtPos = player.getOnPos();

        builder.begin(renderType.mode(), renderType.format());

        for (BlockPos pos : oreBlocksList) {
            BlockState renderState = level.getBlockState(pos);
            if (renderState.isAir()) continue;

            BakedModel ibakedmodel = dispatcher.getBlockModel(renderState);
            matrix.pushPose();
            matrix.translate(-renderedAtPos.getX(), -renderedAtPos.getY(), -renderedAtPos.getZ());
            matrix.translate(pos.getX(), pos.getY(), pos.getZ());

            //We make this just a TINY bit smaller than a full block - because we're doing GREATERTHAN depth testing.
            float translateF = (float) 1 / 2000;
            matrix.translate(translateF, translateF, translateF);
            float scaleF = (float) 1 / 1000;
            matrix.scale(1 - scaleF, 1 - scaleF, 1 - scaleF);

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
        Vec3 subtracted = projectedView.subtract(renderedAtPos.getX(), renderedAtPos.getY(), renderedAtPos.getZ());
        Vector3f sortPos = new Vector3f((float) subtracted.x, (float) subtracted.y, (float) subtracted.z);

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
        BlockPos currentPos = player.getOnPos();
        BlockPos renderPos = new BlockPos(
                currentPos.getX() - (currentPos.getX() - renderedAtPos.getX()),
                currentPos.getY() - (currentPos.getY() - renderedAtPos.getY()),
                currentPos.getZ() - (currentPos.getZ() - renderedAtPos.getZ())
        );

        //Sort every <X> Frames to prevent screendoor effect
        if (sortCounter > 20) {
            if (sortState != null)
                sortAll(renderPos);
            sortCounter = 0;
        } else {
            sortCounter++;
        }
        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        net.neoforged.neoforge.client.event.ViewportEvent.ComputeCameraAngles cameraSetup = net.neoforged.neoforge.client.ClientHooks.onCameraSetup(Minecraft.getInstance().gameRenderer, camera, evt.getPartialTick());
        camera.setAnglesInternal(cameraSetup.getYaw(), cameraSetup.getPitch());
        Matrix4f matrix4f1 = new Matrix4f()
                .rotationXYZ(camera.getXRot() * (float) (Math.PI / 180.0), camera.getYRot() * (float) (Math.PI / 180.0) + (float) Math.PI, cameraSetup.getRoll() * (float) (Math.PI / 180.0));

        PoseStack matrix = evt.getPoseStack();
        matrix.pushPose();
        matrix.mulPose(matrix4f1);
        matrix.translate(-projectedView.x(), -projectedView.y(), -projectedView.z());
        matrix.translate(renderPos.getX(), renderPos.getY(), renderPos.getZ());
        //Draw the renders in the specified order
        try {
            if (vertexBuffer.getFormat() == null)
                return; //IDE says this is never null, but if we remove this check we crash because its null so....
            xRayRender.setupRenderState();
            vertexBuffer.bind();
            vertexBuffer.drawWithShader(matrix.last().pose(), new Matrix4f(evt.getProjectionMatrix()), RenderSystem.getShader());
            VertexBuffer.unbind();
            xRayRender.clearRenderState();
        } catch (Exception e) {
            System.out.println(e);
        }
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
        Vec3 subtracted = projectedView.subtract(lookingAt.getX(), lookingAt.getY(), lookingAt.getZ());
        Vector3f sortPos = new Vector3f((float) subtracted.x, (float) subtracted.y, (float) subtracted.z);
        builder.begin(renderType.mode(), renderType.format());
        builder.restoreSortState(sortState);
        builder.setQuadSorting(VertexSorting.byDistance(sortPos));
        sortState = builder.getSortState();
        return builder.end();
    }
}
