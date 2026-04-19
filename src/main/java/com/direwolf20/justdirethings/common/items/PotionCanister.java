package com.direwolf20.justdirethings.common.items;

import com.direwolf20.justdirethings.common.containers.PotionCanisterContainer;
import com.direwolf20.justdirethings.common.items.datacomponents.JustDireDataComponents;
import com.direwolf20.justdirethings.util.MagicHelpers;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

public class PotionCanister extends Item {
    public PotionCanister(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (level.isClientSide()) return InteractionResult.PASS;

        player.openMenu(new SimpleMenuProvider(
                (windowId, playerInventory, playerEntity) -> new PotionCanisterContainer(windowId, playerInventory, player, itemstack), Component.translatable("")), (buf -> {
            ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, itemstack);
        }));

        return InteractionResult.PASS;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, context, display, tooltip, flagIn);
        Level level = context.level();
        if (level == null) {
            return;
        }

        PotionContents potionContents = PotionCanister.getPotionContents(stack);
        int potionAmt = PotionCanister.getPotionAmount(stack);
        if (potionAmt == 0 || potionContents.equals(PotionContents.EMPTY)) return;

        tooltip.accept(Component.literal(MagicHelpers.formatted(potionAmt) + "/" + MagicHelpers.formatted(PotionCanister.getMaxMB())));
        PotionContents.addPotionTooltip(potionContents.getAllEffects(), tooltip, 1, 20);
    }

    public static int getMaxMB() {
        return 1000;
    }

    public static PotionContents getPotionContents(ItemStack itemStack) {
        return itemStack.getOrDefault(JustDireDataComponents.POTION_CONTENTS, PotionContents.EMPTY);
    }

    public static void setPotionContents(ItemStack itemStack, PotionContents potionContents) {
        itemStack.set(JustDireDataComponents.POTION_CONTENTS, potionContents);
    }

    public static int getPotionAmount(ItemStack itemStack) {
        return itemStack.getOrDefault(JustDireDataComponents.POTION_AMOUNT, 0);
    }

    public static void addPotionAmount(ItemStack itemStack, int amt) {
        setPotionAmount(itemStack, getPotionAmount(itemStack) + amt);
    }

    public static void setPotionAmount(ItemStack itemStack, int amt) {
        itemStack.set(JustDireDataComponents.POTION_AMOUNT, Math.max(0, Math.min(getMaxMB(), amt)));
        if (getPotionAmount(itemStack) == 0)
            setPotionContents(itemStack, PotionContents.EMPTY);
    }

    public static void reducePotionAmount(ItemStack itemStack, int amt) {
        setPotionAmount(itemStack, getPotionAmount(itemStack) - amt);
    }

    public static int getFullness(ItemStack itemStack) {
        int potionAmt = getPotionAmount(itemStack);
        if (potionAmt == 0) return 0;
        if (potionAmt > 0 && potionAmt <= 250) return 1;
        if (potionAmt > 250 && potionAmt <= 500) return 2;
        if (potionAmt > 500 && potionAmt <= 750) return 3;
        return 4;
    }

    public static int getPotionColor(ItemStack itemStack) {
        return getPotionContents(itemStack).getColor();
    }
}
