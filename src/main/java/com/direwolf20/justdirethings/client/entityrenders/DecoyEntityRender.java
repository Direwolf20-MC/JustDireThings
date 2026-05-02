package com.direwolf20.justdirethings.client.entityrenders;

import com.direwolf20.justdirethings.common.entities.DecoyEntity;
import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.ArrowLayer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.WingsLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.entity.player.PlayerSkin;
import net.minecraft.world.level.Level;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

public class DecoyEntityRender extends LivingEntityRenderer<DecoyEntity, AvatarRenderState, PlayerModel> {
    public static final UUID defaultPlayerUUID = UUID.fromString("0192723f-b3dc-495a-959f-52c53fa63bff");

    public DecoyEntityRender(EntityRendererProvider.Context context) {
        super(context, new PlayerModel(context.bakeLayer(ModelLayers.PLAYER), false), 0.5F);
        this.addLayer(new HumanoidArmorLayer<>(
                this,
                ArmorModelSet.bake(ModelLayers.PLAYER_ARMOR, context.getModelSet(), part -> new PlayerModel(part, false)),
                context.getEquipmentRenderer()));
        this.addLayer(new ArrowLayer<>(this, context));
        this.addLayer(new CustomHeadLayer<>(this, context.getModelSet(), context.getPlayerSkinRenderCache()));
        this.addLayer(new WingsLayer<>(this, context.getModelSet(), context.getEquipmentRenderer()));
    }

    @Override
    public AvatarRenderState createRenderState() {
        return new AvatarRenderState();
    }

    @Override
    public void extractRenderState(DecoyEntity entity, AvatarRenderState state, float partialTicks) {
        // Position/rotation come from the decoy itself.
        super.extractRenderState(entity, state, partialTicks);

        Optional<UUID> ownerUUID = entity.getOwnerUUID();
        UUID uuid = ownerUUID.orElse(defaultPlayerUUID);

        Level level = entity.level();
        Player owner = level.getPlayerByUUID(uuid);
        PlayerSkin skin = resolveSkin(owner, uuid);
        state.skin = skin;

        if (owner != null) {
            // Mirror live owner's appearance, equipment, pose, and use-state.
            HumanoidMobRenderer.extractHumanoidRenderState(owner, state, partialTicks, this.itemModelResolver);
            state.leftArmPose = armPose(owner, HumanoidArm.LEFT);
            state.rightArmPose = armPose(owner, HumanoidArm.RIGHT);
            state.arrowCount = owner.getArrowCount();
            state.stingerCount = owner.getStingerCount();
            state.isSpectator = owner.isSpectator();
            state.showHat = owner.isModelPartShown(PlayerModelPart.HAT);
            state.showJacket = owner.isModelPartShown(PlayerModelPart.JACKET);
            state.showLeftPants = owner.isModelPartShown(PlayerModelPart.LEFT_PANTS_LEG);
            state.showRightPants = owner.isModelPartShown(PlayerModelPart.RIGHT_PANTS_LEG);
            state.showLeftSleeve = owner.isModelPartShown(PlayerModelPart.LEFT_SLEEVE);
            state.showRightSleeve = owner.isModelPartShown(PlayerModelPart.RIGHT_SLEEVE);
            state.showCape = owner.isModelPartShown(PlayerModelPart.CAPE);
            state.id = entity.getId();
        } else {
            // Owner offline/out of range — render a plain player silhouette with the owner's skin,
            // empty equipment, neutral pose. The decoy still walks and animates via the super.extractRenderState call above.
            state.leftArmPose = net.minecraft.client.model.HumanoidModel.ArmPose.EMPTY;
            state.rightArmPose = net.minecraft.client.model.HumanoidModel.ArmPose.EMPTY;
            state.arrowCount = 0;
            state.stingerCount = 0;
            state.isSpectator = false;
            state.showHat = true;
            state.showJacket = true;
            state.showLeftPants = true;
            state.showRightPants = true;
            state.showLeftSleeve = true;
            state.showRightSleeve = true;
            state.showCape = true;
            state.id = entity.getId();
        }
    }

    private static PlayerSkin resolveSkin(Player owner, UUID uuid) {
        GameProfile profile = owner != null ? owner.getGameProfile() : new GameProfile(uuid, "DireDecoy");
        Supplier<PlayerSkin> lookup = Minecraft.getInstance().getSkinManager().createLookup(profile, false);
        PlayerSkin resolved = lookup != null ? lookup.get() : null;
        return resolved != null ? resolved : DefaultPlayerSkin.get(profile);
    }

    private static net.minecraft.client.model.HumanoidModel.ArmPose armPose(Player player, HumanoidArm arm) {
        net.minecraft.world.item.ItemStack mainHand = player.getMainHandItem();
        net.minecraft.world.item.ItemStack offHand = player.getOffhandItem();
        net.minecraft.world.item.ItemStack armItem = (player.getMainArm() == arm) ? mainHand : offHand;
        if (armItem.isEmpty()) {
            return net.minecraft.client.model.HumanoidModel.ArmPose.EMPTY;
        }
        if (player.getUsedItemHand() != null && player.isUsingItem()) {
            net.minecraft.world.InteractionHand usedHand = player.getUsedItemHand();
            boolean isMain = (player.getMainArm() == arm);
            boolean matches = (usedHand == net.minecraft.world.InteractionHand.MAIN_HAND) == isMain;
            if (matches) {
                net.minecraft.world.item.ItemUseAnimation anim = armItem.getUseAnimation();
                if (anim == net.minecraft.world.item.ItemUseAnimation.BLOCK)
                    return net.minecraft.client.model.HumanoidModel.ArmPose.BLOCK;
                if (anim == net.minecraft.world.item.ItemUseAnimation.BOW)
                    return net.minecraft.client.model.HumanoidModel.ArmPose.BOW_AND_ARROW;
                if (anim == net.minecraft.world.item.ItemUseAnimation.TRIDENT)
                    return net.minecraft.client.model.HumanoidModel.ArmPose.THROW_TRIDENT;
                if (anim == net.minecraft.world.item.ItemUseAnimation.SPEAR)
                    return net.minecraft.client.model.HumanoidModel.ArmPose.SPEAR;
                if (anim == net.minecraft.world.item.ItemUseAnimation.CROSSBOW)
                    return net.minecraft.client.model.HumanoidModel.ArmPose.CROSSBOW_CHARGE;
                if (anim == net.minecraft.world.item.ItemUseAnimation.SPYGLASS)
                    return net.minecraft.client.model.HumanoidModel.ArmPose.SPYGLASS;
                if (anim == net.minecraft.world.item.ItemUseAnimation.TOOT_HORN)
                    return net.minecraft.client.model.HumanoidModel.ArmPose.TOOT_HORN;
                if (anim == net.minecraft.world.item.ItemUseAnimation.BRUSH)
                    return net.minecraft.client.model.HumanoidModel.ArmPose.BRUSH;
            }
        }
        return net.minecraft.client.model.HumanoidModel.ArmPose.ITEM;
    }

    @Override
    public Identifier getTextureLocation(AvatarRenderState state) {
        return state.skin.body().texturePath();
    }

    @Override
    public void submit(AvatarRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        super.submit(state, poseStack, submitNodeCollector, camera);
    }
}
