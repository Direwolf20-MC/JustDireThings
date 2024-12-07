package com.direwolf20.justdirethings.common.items;

import com.direwolf20.justdirethings.common.items.interfaces.*;
import com.direwolf20.justdirethings.setup.Config;
import com.direwolf20.justdirethings.util.MagicHelpers;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

import java.util.List;

public class PolymorphicWand extends BaseToggleableTool implements LeftClickableTool, FluidContainingItem {
    public PolymorphicWand() {
        super(new Properties()
                .fireResistant()
                .durability(200));
        registerAbility(Ability.LAVAREPAIR);
        registerAbility(Ability.POLYMORPH_RANDOM);
    }

    @Override
    public int getMaxMB() {
        return Config.POLYMORPHIC_WAND_MAX_FLUID.get();
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
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        BlockHitResult blockhitresult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        if (blockhitresult.getType() == HitResult.Type.BLOCK) {
            if (FluidContainingItem.pickupFluid(level, player, itemStack, blockhitresult))
                return InteractionResultHolder.fail(itemStack);
        }
        return super.use(level, player, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, context, tooltip, flagIn);
        Level level = context.level();
        if (level == null) {
            return;
        }
        IFluidHandlerItem fluidHandler = stack.getCapability(Capabilities.FluidHandler.ITEM);
        if (fluidHandler == null) {
            return;
        }
        tooltip.add(Component.translatable("justdirethings.polymorphicfluidamt", MagicHelpers.formatted(fluidHandler.getFluidInTank(0).getAmount()), MagicHelpers.formatted(fluidHandler.getTankCapacity(0))).withStyle(ChatFormatting.GREEN));
    }

}
