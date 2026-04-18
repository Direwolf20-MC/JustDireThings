package com.direwolf20.justdirethings.common.items;

import com.direwolf20.justdirethings.common.items.interfaces.*;
import com.direwolf20.justdirethings.setup.Config;
import com.direwolf20.justdirethings.util.MagicHelpers;
import com.direwolf20.justdirethings.util.MiscTools;
import com.direwolf20.justdirethings.util.ModTags;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import static com.direwolf20.justdirethings.common.items.datacomponents.JustDireDataComponents.ENTITIYTYPE;

public class PolymorphicWandV2 extends BaseToggleableTool implements LeftClickableTool, FluidContainingItem, PoweredItem {
    public PolymorphicWandV2(Properties pProperties) {
        super(pProperties);
        registerAbility(Ability.POLYMORPH_RANDOM);
        registerAbility(Ability.POLYMORPH_TARGET);
    }

    @Override
    public int getMaxMB() {
        return Config.POLYMORPHIC_WAND_V2_MAX_FLUID.get();
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        ItemStack itemStack = pContext.getItemInHand();
        Player player = pContext.getPlayer();
        if (player == null || itemStack.isEmpty()) return InteractionResult.FAIL;
        BlockHitResult blockhitresult = getPlayerPOVHitResult(player.level(), player, ClipContext.Fluid.SOURCE_ONLY);
        if (blockhitresult.getType() == HitResult.Type.BLOCK) {
            if (FluidContainingItem.pickupFluid(player.level(), player, itemStack, blockhitresult))
                return InteractionResult.SUCCESS;
        }
        return super.useOn(pContext);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        Level level = player.level();
        if (level.isClientSide()) return true;
        ItemStack itemStack = player.getMainHandItem();
        Set<Ability> abilities = LeftClickableTool.getLeftClickList(itemStack);
        if (itemStack.getItem() instanceof ToggleableTool toggleableTool && !abilities.isEmpty()) {
            toggleableTool.useAbility(player.level(), player, InteractionHand.MAIN_HAND, false);
        }
        return true;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        BlockHitResult blockhitresult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        if (blockhitresult.getType() == HitResult.Type.BLOCK) {
            if (FluidContainingItem.pickupFluid(level, player, itemStack, blockhitresult))
                return InteractionResult.FAIL;
        }
        Entity entity = MiscTools.getEntityLookedAt(player, 4);
        if (!level.isClientSide() && player.isShiftKeyDown() && entity instanceof LivingEntity livingEntity) { //Custom Handling due to shift-clicking mobs to set target
            savePolymorphTarget(itemStack, player, livingEntity);
            return InteractionResult.PASS;
        }
        return super.use(level, player, hand);
    }

    public static void savePolymorphTarget(ItemStack stack, Player player, LivingEntity interactionTarget) {
        if (interactionTarget instanceof Mob && !interactionTarget.getType().builtInRegistryHolder().is(ModTags.Entities.POLYMORPHIC_TARGET_DENY)) {
            stack.set(ENTITIYTYPE, EntityType.getKey(interactionTarget.getType()).toString());
            player.sendOverlayMessage(Component.translatable("justdirethings.polymorphset", interactionTarget.getType().getDescription()));
        } else {
            player.sendOverlayMessage(Component.translatable("justdirethings.invalidpolymorphentity"));
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, context, display, tooltip, flagIn);
        Level level = context.level();
        if (level == null) {
            return;
        }
        ResourceHandler<FluidResource> fluidHandler = stack.getCapability(Capabilities.Fluid.ITEM, null);
        if (fluidHandler == null) {
            return;
        }
        List<Component> buffer = new ArrayList<>();
        buffer.add(Component.translatable("justdirethings.polymorphicfluidamt", MagicHelpers.formatted(fluidHandler.getAmountAsInt(0)), MagicHelpers.formatted(fluidHandler.getCapacityAsInt(0, fluidHandler.getResource(0)))).withStyle(ChatFormatting.GREEN));

        if (stack.has(ENTITIYTYPE)) {
            EntityType<?> newType = EntityType.byString(stack.get(ENTITIYTYPE)).orElse(null);
            if (newType != null) {
                buffer.add(Component.translatable("justdirethings.polymorphset", newType.getDescription())
                        .withStyle(ChatFormatting.AQUA));
            }
        }
        buffer.forEach(tooltip);
    }

    @Override
    public int getMaxEnergy() {
        return Config.POLYMORPHIC_WAND_V2_FE_CAPACITY.get();
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return ItemUseAnimation.NONE;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

}
