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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.ItemStackHandler;

public class InventoryHolderBER extends AreaAffectingBER {
    public InventoryHolderBER(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(BlockEntity blockentity, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightsIn, int combinedOverlayIn) {
        if (blockentity instanceof InventoryHolderBE inventoryHolderBE && inventoryHolderBE.renderPlayer) {
            // Create a fake player entity
            AbstractClientPlayer mockPlayer = createMockPlayer(inventoryHolderBE);
            if (mockPlayer == null) return;
            mockPlayer.yHeadRot = 0;
            mockPlayer.yHeadRotO = 0;
            // Equip the mock player with items
            equipMockPlayer(mockPlayer, inventoryHolderBE);

            // Render the mock player model above the block
            renderMockPlayerEntity(matrixStackIn, bufferIn, mockPlayer, combinedLightsIn, partialTicks);


            // Create a fake player model
            /*Minecraft minecraft = Minecraft.getInstance();
            PlayerModel<Player> playerModel = new PlayerModel<>(minecraft.getEntityModels().bakeLayer(ModelLayers.PLAYER), false);
            playerModel.young = false;
            playerModel.rightArmPose = HumanoidModel.ArmPose.ITEM;
            playerModel.leftArmPose = HumanoidModel.ArmPose.ITEM;
            // Render the player model above the block
            renderFakePlayer(matrixStackIn, bufferIn, playerModel, inventoryHolderBE, combinedLightsIn, combinedOverlayIn);*/

        }
    }

    public AbstractClientPlayer createMockPlayer(InventoryHolderBE blockEntity) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) return null;
        // Create a mock AbstractClientPlayer using the current level and a GameProfile
        GameProfile gameProfile = new GameProfile(blockEntity.placedByUUID, "MockPlayer");

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

    /*public void renderFakePlayer(PoseStack matrixStackIn, MultiBufferSource bufferIn, PlayerModel<Player> playerModel, InventoryHolderBE blockEntity, int combinedLightsIn, int combinedOverlayIn) {
        matrixStackIn.pushPose();

        // Position the player rendering above the block
        matrixStackIn.translate(0.5, 1f, 0.5); // Adjust Y for height
        matrixStackIn.scale(1.0F, -1.0F, -1.0F);
        matrixStackIn.scale(0.5f, 0.5f, 0.5f);
        matrixStackIn.translate(0.0F, -1.501F, 0.0F);

        // Add Y-axis rotation (for smooth continuous rotation, use System time)
        long gameTime = System.currentTimeMillis(); // Use time to smoothly rotate
        float rotation = (gameTime % 7200L) / 20.0F; // Calculate a rotation value
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(rotation)); // Rotate around the Y axis

        // Retrieve the player's skin from their UUID (this can be cached)
        PlayerSkin playerSkin = Minecraft.getInstance().getSkinManager().getInsecureSkin(new GameProfile(blockEntity.placedByUUID, ""));

        // Render the player model using the skin
        VertexConsumer vertexConsumer = bufferIn.getBuffer(RenderType.entityTranslucent(playerSkin.texture()));
        int packedARGB = (175 << 24) | (255 << 16) | (255 << 8) | 255;
        playerModel.renderToBuffer(matrixStackIn, vertexConsumer, 15728880, OverlayTexture.NO_OVERLAY, packedARGB); // 0.8F for transparency

        // Render the items (pickaxe and shield)
        renderHeldItems(matrixStackIn, bufferIn, blockEntity, combinedLightsIn, combinedOverlayIn, playerModel);

        matrixStackIn.popPose();
    }

    public void renderHeldItems(PoseStack matrixStackIn, MultiBufferSource bufferIn, InventoryHolderBE blockEntity, int combinedLightsIn, int combinedOverlayIn, PlayerModel<Player> playerModel) {
        Minecraft mc = Minecraft.getInstance();

        // Render pickaxe in the right hand
        ItemStack pickaxeStack = new ItemStack(Items.DIAMOND_PICKAXE); // Replace with whatever pickaxe you want
        mc.getItemRenderer().renderStatic(pickaxeStack, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, combinedLightsIn, combinedOverlayIn, matrixStackIn, bufferIn, blockEntity.getLevel(), 0);

        // Render shield in the left hand
        ItemStack shieldStack = new ItemStack(Items.SHIELD); // Shield in the offhand
        mc.getItemRenderer().renderStatic(shieldStack, ItemDisplayContext.THIRD_PERSON_LEFT_HAND, combinedLightsIn, combinedOverlayIn, matrixStackIn, bufferIn, blockEntity.getLevel(), 0);
    }

    public static void renderTransparentPlayer(PoseStack matrixStackIn, Player player, float pPartialTicks, int combinedLightsIn) {
        matrixStackIn.pushPose();

        // Render the player model
        MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
        EntityRenderDispatcher renderManager = Minecraft.getInstance().getEntityRenderDispatcher();
        EntityRenderer<? super Player> renderer = renderManager.getRenderer(player);
        RenderType renderType = RenderType.itemEntityTranslucentCull(renderer.getTextureLocation(player));
        VertexConsumer vertexconsumer = buffer.getBuffer(renderType);
        int i = LivingEntityRenderer.getOverlayCoords(player, 0);
        if (renderer instanceof LivingEntityRenderer<?, ?>) {
            LivingEntityRenderer<Player, ?> livingRenderer = (LivingEntityRenderer<Player, ?>) renderer;
            float f = Mth.rotLerp(pPartialTicks, player.yBodyRotO, player.yBodyRot);
            float f1 = Mth.rotLerp(pPartialTicks, player.yHeadRotO, player.yHeadRot);
            float f2 = f1 - f;
            float f5 = Mth.lerp(pPartialTicks, player.xRotO, player.getXRot());
            float f7 = 0;
            //setupRotations(player, matrixStackIn, f7, f, pPartialTicks);
            matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
            scale(player, matrixStackIn, pPartialTicks);
            matrixStackIn.translate(0.0F, -1.501F, 0.0F);
            float f8 = 0.0F;
            float f4 = 0.0F;
            if (player.isAlive()) {
                f8 = player.walkAnimation.speed(pPartialTicks);
                f4 = player.walkAnimation.position(pPartialTicks);
                if (player.isBaby()) {
                    f4 *= 3.0F;
                }

                if (f8 > 1.0F) {
                    f8 = 1.0F;
                }
            }
            EntityModel<Player> entityModel = livingRenderer.getModel();
            entityModel.attackTime = 0f;
            entityModel.riding = false;
            entityModel.young = player.isBaby();
            entityModel.prepareMobModel(player, f4, f8, pPartialTicks);
            entityModel.setupAnim(player, f4, f8, f7, f2, f5);
            int packedARGB = (127 << 24) | (255 << 16) | (255 << 8) | 255;
            entityModel.renderToBuffer(matrixStackIn, vertexconsumer, combinedLightsIn, i, packedARGB);
        }

        matrixStackIn.popPose();

    }

    protected static void setupRotations(Player pEntityLiving, PoseStack pPoseStack, float pAgeInTicks, float pRotationYaw, float pPartialTicks) {
        if (!pEntityLiving.hasPose(Pose.SLEEPING)) {
            pPoseStack.mulPose(Axis.YP.rotationDegrees(180.0F - pRotationYaw));
        }
    }

    protected static void scale(Player pLivingEntity, PoseStack pPoseStack, float pPartialTickTime) {
        float f = 0.9375F;
        pPoseStack.scale(0.9375F, 0.9375F, 0.9375F);
    }*/
}
