package com.direwolf20.justdirethings.client.blockentityrenders;

import com.direwolf20.justdirethings.client.blockentityrenders.baseber.AreaAffectingBER;
import com.direwolf20.justdirethings.common.blockentities.ItemCollectorBE;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.joml.Matrix4f;

public class ItemCollectorRenderer<T extends ItemCollectorBE> extends AreaAffectingBER<T> {
    public ItemCollectorRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(ItemCollectorBE blockentity, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightsIn, int combinedOverlayIn) {
        super.render(blockentity, partialTicks, matrixStackIn, bufferIn, combinedLightsIn, combinedOverlayIn);
        long gameTime = blockentity.getLevel().getGameTime();
        Matrix4f matrix4f = matrixStackIn.last().pose();
        this.renderCube(blockentity, matrix4f, bufferIn.getBuffer(this.renderType()), gameTime, partialTicks);
    }

    private void renderCube(ItemCollectorBE blockEntity, Matrix4f matrixStack, VertexConsumer vertexConsumer, long gameTime, float partialTicks) {
        Direction direction = blockEntity.getBlockState().getValue(BlockStateProperties.FACING).getOpposite();
        float oneSmall = 0.53125f;
        float zeroSmall = 0.46875f;
        float oneBig = 0.5625f;
        float zeroBig = 0.4375f;
        int ticks = 80;
        float f1 = (float) Math.floorMod(gameTime, ticks) + partialTicks;
        float lerp = f1 / ticks;
        float zero;
        float one;
        if (f1 < ticks / 2f) {
            zero = Mth.lerp(lerp, zeroSmall, zeroBig);
            one = Mth.lerp(lerp, oneSmall, oneBig);
        } else {
            zero = Mth.lerp(lerp, zeroBig, zeroSmall);
            one = Mth.lerp(lerp, oneBig, oneSmall);
        }
        float diff = one - zero;
        float f;
        switch (direction) {
            case UP:
                f = 0.22f + 0.25f; //Center of cube up 1/4 block
                this.renderFace(matrixStack, vertexConsumer, zero, one, f, f + diff, one, one, one, one); //South
                this.renderFace(matrixStack, vertexConsumer, zero, one, f + diff, f, zero, zero, zero, zero); //North
                this.renderFace(matrixStack, vertexConsumer, one, one, f + diff, f, zero, one, one, zero); //East
                this.renderFace(matrixStack, vertexConsumer, zero, zero, f, f + diff, zero, one, one, zero); //West
                this.renderFace(matrixStack, vertexConsumer, zero, one, f, f, zero, zero, one, one); //Down
                this.renderFace(matrixStack, vertexConsumer, zero, one, f + diff, f + diff, one, one, zero, zero); //Up
                break;
            case DOWN:
                f = 0.5f - 0.25f; //Center of cube down 1/4 block
                this.renderFace(matrixStack, vertexConsumer, zero, one, f, f - diff, one, one, one, one); //South
                this.renderFace(matrixStack, vertexConsumer, zero, one, f - diff, f, zero, zero, zero, zero); //North
                this.renderFace(matrixStack, vertexConsumer, one, one, f - diff, f, zero, one, one, zero); //East
                this.renderFace(matrixStack, vertexConsumer, zero, zero, f, f - diff, zero, one, one, zero); //West
                this.renderFace(matrixStack, vertexConsumer, zero, one, f, f, zero, zero, one, one); //Down
                this.renderFace(matrixStack, vertexConsumer, zero, one, f - diff, f - diff, one, one, zero, zero); //Up
                break;
            case NORTH:
                f = 0.5f - 0.25f; //Center of cube up 1/4 block
                this.renderFace(matrixStack, vertexConsumer, zero, one, zero, one, f, f, f, f); //South
                this.renderFace(matrixStack, vertexConsumer, zero, one, one, zero, f - diff, f - diff, f - diff, f - diff); //North
                this.renderFace(matrixStack, vertexConsumer, one, one, one, zero, f - diff, f, f, f - diff); //East
                this.renderFace(matrixStack, vertexConsumer, zero, zero, zero, one, f - diff, f, f, f - diff); //West
                this.renderFace(matrixStack, vertexConsumer, zero, one, zero, zero, f - diff, f - diff, f, f); //Down
                this.renderFace(matrixStack, vertexConsumer, zero, one, one, one, f, f, f - diff, f - diff); //Up
                break;
            case SOUTH:
                f = 0.5f + 0.25f; //Center of cube down 1/4 block
                this.renderFace(matrixStack, vertexConsumer, zero, one, zero, one, f, f, f, f); //South
                this.renderFace(matrixStack, vertexConsumer, zero, one, one, zero, f + diff, f + diff, f + diff, f + diff); //North
                this.renderFace(matrixStack, vertexConsumer, zero, zero, zero, one, f + diff, f, f, f + diff); //East
                this.renderFace(matrixStack, vertexConsumer, one, one, one, zero, f + diff, f, f, f + diff); //West
                this.renderFace(matrixStack, vertexConsumer, zero, one, one, one, f, f, f + diff, f + diff); //Down
                this.renderFace(matrixStack, vertexConsumer, zero, one, zero, zero, f + diff, f + diff, f, f); //Up
                break;
            case EAST:
                f = 0.5f + 0.25f; //Center of cube up 1/4 block
                this.renderFace(matrixStack, vertexConsumer, f, f + diff, zero, one, one, one, one, one); //South
                this.renderFace(matrixStack, vertexConsumer, f, f + diff, one, zero, zero, zero, zero, zero); //North
                this.renderFace(matrixStack, vertexConsumer, f + diff, f + diff, one, zero, zero, one, one, zero); //East
                this.renderFace(matrixStack, vertexConsumer, f, f, zero, one, zero, one, one, zero); //West
                this.renderFace(matrixStack, vertexConsumer, f, f + diff, zero, zero, zero, zero, one, one); //Down
                this.renderFace(matrixStack, vertexConsumer, f, f + diff, one, one, one, one, zero, zero); //Up
                break;
            case WEST:
                f = 0.5f - 0.25f; //Center of cube shifted 1/4 block to the west
                this.renderFace(matrixStack, vertexConsumer, f, f - diff, zero, one, one, one, one, one); //South
                this.renderFace(matrixStack, vertexConsumer, f, f - diff, one, zero, zero, zero, zero, zero); //North
                this.renderFace(matrixStack, vertexConsumer, f, f, zero, one, zero, one, one, zero); //East
                this.renderFace(matrixStack, vertexConsumer, f - diff, f - diff, one, zero, zero, one, one, zero); //West
                this.renderFace(matrixStack, vertexConsumer, f, f - diff, one, one, one, one, zero, zero); //Down
                this.renderFace(matrixStack, vertexConsumer, f, f - diff, zero, zero, zero, zero, one, one); //Up
                break;
            default:
                break;
        }
    }

    private void renderFace(Matrix4f matrixStack, VertexConsumer vertexConsumer, float x1, float x2, float y1, float y2, float z1, float z2, float z3, float z4) {
        vertexConsumer.vertex(matrixStack, x1, y1, z1).endVertex();
        vertexConsumer.vertex(matrixStack, x2, y1, z2).endVertex();
        vertexConsumer.vertex(matrixStack, x2, y2, z3).endVertex();
        vertexConsumer.vertex(matrixStack, x1, y2, z4).endVertex();
    }

    protected RenderType renderType() {
        return RenderType.endPortal();
    }

}
