package com.direwolf20.justdirethings.common.items;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FuelCanister extends Item {

    public FuelCanister() {
        super(new Properties()
                .stacksTo(1));
    }

    @Override
    public void appendHoverText(ItemStack stack, @javax.annotation.Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, level, tooltip, flagIn);

        Minecraft mc = Minecraft.getInstance();

        if (level == null || mc.player == null) {
            return;
        }

        tooltip.add(Component.translatable("justdirethings.fuelcanisteramt", getFuelLevel(stack)).withStyle(ChatFormatting.AQUA));

    }

    @Override //Temporary for testing!
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (level.isClientSide()) return new InteractionResultHolder<>(InteractionResult.PASS, itemstack);

        incrementFuel(itemstack, 1);

        return new InteractionResultHolder<>(InteractionResult.PASS, itemstack);
    }

    @Override
    public int getBurnTime(ItemStack stack, @Nullable RecipeType<?> recipeType) {
        return getFuelLevel(stack) > 0 ? 200 : 0;
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack stack) {
        ItemStack copy = stack.copy();
        decrementFuel(copy);
        return copy;
    }

    public static int getFuelLevel(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null && tag.contains("FuelLevel") ? tag.getInt("FuelLevel") : 0;
    }

    public static void setFuelLevel(ItemStack stack, int fuelLevel) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt("FuelLevel", fuelLevel);
    }

    public static void decrementFuel(ItemStack stack) {
        int currentFuel = getFuelLevel(stack);
        if (currentFuel > 0) //Should always be true but lets be sure!
            currentFuel--;
        setFuelLevel(stack, currentFuel);
    }

    public static void incrementFuel(ItemStack stack, int amt) {
        int currentFuel = getFuelLevel(stack);
        if (currentFuel < 200) //TODO Config Option
            currentFuel++;
        setFuelLevel(stack, currentFuel);
    }

}
