package com.direwolf20.justdirethings.client.blockentityrenders;

import com.direwolf20.justdirethings.client.blockentityrenders.baseber.AreaAffectingBER;
import com.direwolf20.justdirethings.common.blockentities.InventoryHolderBE;
import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.UUID;

public class InventoryHolderBER extends AreaAffectingBER {
    public static AbstractClientPlayer mockPlayer;
    public InventoryHolderBER(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(BlockEntity blockentity, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightsIn, int combinedOverlayIn) {
        if (blockentity instanceof InventoryHolderBE inventoryHolderBE && inventoryHolderBE.renderPlayer) {
            // Create a fake player entity
            if (mockPlayer == null || mockPlayer.getUUID() != inventoryHolderBE.placedByUUID)
                mockPlayer = createMockPlayer(inventoryHolderBE);
            if (mockPlayer == null) return;
            mockPlayer.yHeadRot = 0;
            mockPlayer.yHeadRotO = 0;
            // Equip the mock player with items
            equipMockPlayer(mockPlayer, inventoryHolderBE);

            // Render the mock player model above the block
            renderMockPlayerEntity(matrixStackIn, bufferIn, mockPlayer, combinedLightsIn, partialTicks);
            //UnEquip items, so the fake player is ready for the next inventory holder (In case theres more than one)
            unEquipMockPlayer(mockPlayer, inventoryHolderBE);
        }
    }

    public AbstractClientPlayer createMockPlayer(InventoryHolderBE blockEntity) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) return null;
        UUID placedByUUID = blockEntity.placedByUUID;
        if (placedByUUID == null) return null;
        // Create a mock AbstractClientPlayer using the current level and a GameProfile
        GameProfile gameProfile = new GameProfile(placedByUUID, "MockPlayer");

        // AbstractClientPlayer does not need the full connection setup like LocalPlayer
        return new AbstractClientPlayer(minecraft.level, gameProfile) {
            @Override
            public boolean isSpectator() {
                return false;
            }

            @Override
            public boolean isCreative() {
                return false;
            }
        };
    }

    public void equipMockPlayer(AbstractClientPlayer mockPlayer, InventoryHolderBE blockEntity) {
        ItemStackHandler itemStackHandler = blockEntity.getMachineHandler();
        // Equip the pickaxe in the main hand
        if (!itemStackHandler.getStackInSlot(blockEntity.renderedSlot).isEmpty())
            mockPlayer.setItemInHand(InteractionHand.MAIN_HAND, itemStackHandler.getStackInSlot(blockEntity.renderedSlot));

        // Equip the offhand
        if (!itemStackHandler.getStackInSlot(40).isEmpty())
            mockPlayer.setItemInHand(InteractionHand.OFF_HAND, itemStackHandler.getStackInSlot(40));

        if (!itemStackHandler.getStackInSlot(36).isEmpty())
            mockPlayer.setItemSlot(EquipmentSlot.HEAD, itemStackHandler.getStackInSlot(36));
        if (!itemStackHandler.getStackInSlot(37).isEmpty())
            mockPlayer.setItemSlot(EquipmentSlot.CHEST, itemStackHandler.getStackInSlot(37));
        if (!itemStackHandler.getStackInSlot(38).isEmpty())
            mockPlayer.setItemSlot(EquipmentSlot.LEGS, itemStackHandler.getStackInSlot(38));
        if (!itemStackHandler.getStackInSlot(39).isEmpty())
            mockPlayer.setItemSlot(EquipmentSlot.FEET, itemStackHandler.getStackInSlot(39));
    }

    public void unEquipMockPlayer(AbstractClientPlayer mockPlayer, InventoryHolderBE blockEntity) {
        ItemStackHandler itemStackHandler = blockEntity.getMachineHandler();
        // Equip the pickaxe in the main hand
        mockPlayer.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);

        // Equip the offhand
        mockPlayer.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);

        mockPlayer.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
        mockPlayer.setItemSlot(EquipmentSlot.CHEST, ItemStack.EMPTY);
        mockPlayer.setItemSlot(EquipmentSlot.LEGS, ItemStack.EMPTY);
        mockPlayer.setItemSlot(EquipmentSlot.FEET, ItemStack.EMPTY);
    }

    public void renderMockPlayerEntity(PoseStack matrixStackIn, MultiBufferSource bufferIn, AbstractClientPlayer mockPlayer, int combinedLightsIn, float partialTicks) {
        matrixStackIn.pushPose();

        // Position the player rendering above the block
        matrixStackIn.translate(0.5, 1f, 0.5); // Adjust Y for height
        matrixStackIn.scale(0.5f, 0.5f, 0.5f);

        // Add Y-axis rotation
        long gameTime = System.currentTimeMillis();
        float rotation = (gameTime % 7200L) / 20.0F;
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(rotation));

        // Render the mock player using Minecraft's built-in rendering system
        Minecraft.getInstance().getEntityRenderDispatcher().render(mockPlayer, 0.0D, 0.0D, 0.0D, 0.0F, partialTicks, matrixStackIn, bufferIn, 15728880);

        matrixStackIn.popPose();
    }
}
