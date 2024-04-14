package com.direwolf20.justdirethings.client.blockentityrenders;

import com.direwolf20.justdirethings.client.blockentityrenders.baseber.AreaAffectingBER;
import com.direwolf20.justdirethings.common.blockentities.EnergyTransmitterBE;
import com.direwolf20.justdirethings.setup.Registration;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.joml.Matrix4f;

public class EnergyTransmitterRenderer extends AreaAffectingBER {
    public static final ItemStack itemStack = new ItemStack(Registration.Celestigem.get());
    public EnergyTransmitterRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(BlockEntity blockentity, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightsIn, int combinedOverlayIn) {
        super.render(blockentity, partialTicks, matrixStackIn, bufferIn, combinedLightsIn, combinedOverlayIn);

        if (blockentity instanceof EnergyTransmitterBE energyTransmitterBE)
            this.renderItemStack(energyTransmitterBE, matrixStackIn, bufferIn, combinedOverlayIn);
    }

    private void renderItemStack(EnergyTransmitterBE blockEntity, PoseStack poseStack, MultiBufferSource bufferIn, int combinedOverlayIn) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        Direction direction = blockEntity.getBlockState().getValue(BlockStateProperties.FACING).getOpposite();
        long millis = System.currentTimeMillis();

        poseStack.pushPose();
        poseStack.translate(0.5f + (direction.getStepX() * 0.3f), 0.5f + (direction.getStepY() * 0.3f), 0.5f + (direction.getStepZ() * 0.3f));
        poseStack.mulPose(Axis.XP.rotationDegrees(direction.getStepZ() * -90));
        poseStack.mulPose(Axis.ZP.rotationDegrees(direction.getStepX() * 90));
        poseStack.mulPose(Axis.XP.rotationDegrees(direction.getStepY() == 1 ? 0 : 180));
        float angle = ((millis / 15) % 360);
        poseStack.mulPose(Axis.YP.rotationDegrees(angle));
        poseStack.scale(.15f, .15f, .15f);
        itemRenderer.renderStatic(itemStack, ItemDisplayContext.FIXED, LightTexture.FULL_BRIGHT, combinedOverlayIn, poseStack, bufferIn, Minecraft.getInstance().level, 0);
        poseStack.popPose();
    }

    private void renderCube(EnergyTransmitterBE blockEntity, Matrix4f matrixStack, VertexConsumer vertexConsumer, long gameTime, float partialTicks) {
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
                f = 0.5f + 0.25f; //Center of cube up 1/4 block
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
