package com.direwolf20.justdirethings.client.blockentityrenders;

import com.direwolf20.justdirethings.client.blockentityrenders.baseber.AreaAffectingBER;
import com.direwolf20.justdirethings.common.blockentities.InventoryHolderBE;
import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

public class InventoryHolderBER extends AreaAffectingBER<InventoryHolderBE, InventoryHolderBER.InventoryHolderRenderState> {

    public static class InventoryHolderRenderState extends AreaAffectingRenderState {
        public @Nullable EntityRenderState mockPlayerState;
        public boolean shouldRender;
    }

    private final EntityRenderDispatcher dispatcher;

    public InventoryHolderBER(BlockEntityRendererProvider.Context context) {
        this.dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
    }

    @Override
    public InventoryHolderRenderState createRenderState() {
        return new InventoryHolderRenderState();
    }

    @Override
    public void extractRenderState(InventoryHolderBE blockEntity, InventoryHolderRenderState state, float partialTicks, Vec3 cameraPosition,
                                    ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.shouldRender = false;
        state.mockPlayerState = null;

        if (!blockEntity.renderPlayer) return;
        UUID placedBy = blockEntity.placedByUUID;
        if (placedBy == null) return;
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) return;

        AbstractClientPlayer mockPlayer = new AbstractClientPlayer(level, new GameProfile(placedBy, "MockPlayer")) {
        };
        mockPlayer.yHeadRot = 0F;
        mockPlayer.yHeadRotO = 0F;
        mockPlayer.yBodyRot = 0F;
        mockPlayer.yBodyRotO = 0F;
        equipMockPlayer(mockPlayer, blockEntity);

        try {
            state.mockPlayerState = this.dispatcher.extractEntity(mockPlayer, partialTicks);
            state.shouldRender = true;
        } catch (Throwable ignored) {
            state.mockPlayerState = null;
            state.shouldRender = false;
        }
    }

    private static void equipMockPlayer(AbstractClientPlayer mockPlayer, InventoryHolderBE blockEntity) {
        ItemStacksResourceHandler handler = blockEntity.getMachineHandler();

        ItemStack mainHand = readSlot(handler, blockEntity.renderedSlot);
        if (!mainHand.isEmpty()) mockPlayer.setItemInHand(InteractionHand.MAIN_HAND, mainHand);

        ItemStack offHand = readSlot(handler, 40);
        if (!offHand.isEmpty()) mockPlayer.setItemInHand(InteractionHand.OFF_HAND, offHand);

        ItemStack head = readSlot(handler, 36);
        if (!head.isEmpty()) mockPlayer.setItemSlot(EquipmentSlot.HEAD, head);

        ItemStack chest = readSlot(handler, 37);
        if (!chest.isEmpty()) mockPlayer.setItemSlot(EquipmentSlot.CHEST, chest);

        ItemStack legs = readSlot(handler, 38);
        if (!legs.isEmpty()) mockPlayer.setItemSlot(EquipmentSlot.LEGS, legs);

        ItemStack feet = readSlot(handler, 39);
        if (!feet.isEmpty()) mockPlayer.setItemSlot(EquipmentSlot.FEET, feet);
    }

    private static ItemStack readSlot(ItemStacksResourceHandler handler, int slot) {
        if (slot < 0 || slot >= handler.size()) return ItemStack.EMPTY;
        ItemResource resource = handler.getResource(slot);
        if (resource.isEmpty()) return ItemStack.EMPTY;
        int amount = handler.getAmountAsInt(slot);
        if (amount <= 0) return ItemStack.EMPTY;
        return resource.toStack(amount);
    }

    @Override
    public void submit(InventoryHolderRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        super.submit(state, poseStack, submitNodeCollector, camera);
        if (!state.shouldRender || state.mockPlayerState == null) return;

        poseStack.pushPose();
        poseStack.translate(0.5, 1.0, 0.5);
        poseStack.scale(0.5F, 0.5F, 0.5F);
        float rotation = (System.currentTimeMillis() % 7200L) / 20.0F;
        poseStack.mulPose(Axis.YP.rotationDegrees(rotation));
        this.dispatcher.submit(state.mockPlayerState, camera, 0.0, 0.0, 0.0, poseStack, submitNodeCollector);
        poseStack.popPose();
    }
}
