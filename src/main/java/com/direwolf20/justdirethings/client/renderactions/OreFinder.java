package com.direwolf20.justdirethings.client.renderactions;

import com.direwolf20.justdirethings.client.renderers.DireBufferBuilder;
import com.direwolf20.justdirethings.client.renderers.OurRenderTypes;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.VertexSorting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.common.Tags;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OreFinder {
    public static long visibleStartTime;
    //public static Map<BlockPos, BlockState> oreMap = new HashMap<>();
    public static List<BlockPos> oreBlocksList = new ArrayList<>();
    private static int sortCounter = 0;

    //A eBufferBuilder, so we can draw the render
    private static final DireBufferBuilder builder = new DireBufferBuilder(RenderType.cutout().bufferSize());
    //Cached SortStates used for re-sorting every so often
    private static BufferBuilder.SortState sortState;
    //Vertex Buffer to buffer the different ores.
    private static VertexBuffer vertexBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
    //The render type
    private static RenderType renderType = RenderType.cutout();

    public static void render(RenderLevelStageEvent evt, Player player, ItemStack heldItemMain) {
        OurRenderTypes.updateRenders();
        if (((System.currentTimeMillis() - visibleStartTime) / 1000) >= 10) return; //Lasts for 10 seconds
        //if (oreMap.isEmpty())
        //    discoverOres(Minecraft.getInstance().player);
        drawVBO(evt, player);
    }

    public static void discoverOres(Player player) {
        oreBlocksList.clear();
        BlockPos playerPos = player.getOnPos();
        int radius = 50; //TODO 50 seems to be ok perf wise but ridiculous
        long tempTimer = System.currentTimeMillis();
        oreBlocksList = BlockPos.betweenClosedStream(playerPos.offset(-radius, -radius, -radius), playerPos.offset(radius, radius, radius))
                .filter(blockPos -> player.level().getBlockState(blockPos).getTags().anyMatch(tag -> tag.equals(Tags.Blocks.ORES)))
                .map(BlockPos::immutable)
                //.sorted(Comparator.comparingDouble(playerPos::distSqr))
                .collect(Collectors.toList());
        /*BlockPos.betweenClosed(playerPos.offset(-radius, -radius, -radius), playerPos.offset(radius, radius, radius))
                .forEach(blockPos -> {
                    //System.out.println(blockPos);
                    BlockState blockState = player.level().getBlockState(blockPos);
                    if (blockState.getTags().anyMatch(tag -> tag.equals(Tags.Blocks.ORES))) {
                        oreMap.put(blockPos, blockState);
                    }
                });*/
        visibleStartTime = System.currentTimeMillis(); //Todo Elsewhere
        System.out.println("Took: " + (visibleStartTime - tempTimer));
        generateVBO(player);
    }

    public static void generateVBO(Player player) {
        if (oreBlocksList == null || oreBlocksList.isEmpty()) return;
        PoseStack matrix = new PoseStack(); //Create a new matrix stack for use in the buffer building process
        BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
        ModelBlockRenderer modelBlockRenderer = dispatcher.getModelRenderer();
        final RandomSource random = RandomSource.create();
        Level level = player.level();
        BlockPos renderPos = player.getOnPos();

        RenderType renderType = RenderType.translucent();
        builder.begin(renderType.mode(), renderType.format());

        for (BlockPos pos : oreBlocksList) {
            BlockState renderState = level.getBlockState(pos);
            if (renderState.isAir()) continue;

            BakedModel ibakedmodel = dispatcher.getBlockModel(renderState);
            matrix.pushPose();
            matrix.translate(pos.getX(), pos.getY(), pos.getZ());
            //matrix.translate(-0.0005f, -0.0005f, -0.0005f); //Avoid Z-Fighting
            //matrix.scale(1.001f, 1.001f, 1.001f); //Avoid Z-Fighting


            for (RenderType renderTypeDraw : ibakedmodel.getRenderTypes(renderState, random, ModelData.EMPTY)) {
                try {
                    modelBlockRenderer.tesselateBlock(level, ibakedmodel, renderState, pos.above(255), matrix, builder, false, random, renderState.getSeed(pos), OverlayTexture.NO_OVERLAY, ibakedmodel.getModelData(level, pos, renderState, ModelData.EMPTY), renderTypeDraw);
                } catch (Exception e) {
                    //System.out.println(e);
                }
                //dispatcher.renderBatched(renderState, pos.above(2), player.level(), matrix, builder, true, RandomSource.create(), ModelData.EMPTY, renderTypeDraw);
            }
            matrix.popPose();
        }
        //Sort all the builder's vertices and then upload them to the vertex buffers
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
        MultiBufferSource.BufferSource buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
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
        ArrayList<RenderType> drawSet = new ArrayList<>();
        //drawSet.add(RenderType.solid());
        drawSet.add(renderType);
        //drawSet.add(RenderType.cutoutMipped());
        //drawSet.add(RenderType.translucent());
        //drawSet.add(RenderType.tripwire());
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        try {
            for (RenderType renderType : drawSet) {
                RenderType drawRenderType;
                if (renderType.equals(RenderType.cutout()))
                    drawRenderType = OurRenderTypes.RenderBlock;
                else
                    drawRenderType = RenderType.translucent();
                if (vertexBuffer.getFormat() == null)
                    continue; //IDE says this is never null, but if we remove this check we crash because its null so....
                drawRenderType.setupRenderState();
                vertexBuffer.bind();
                vertexBuffer.drawWithShader(matrix.last().pose(), new Matrix4f(evt.getProjectionMatrix()), RenderSystem.getShader());
                VertexBuffer.unbind();
                drawRenderType.clearRenderState();
            }
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
