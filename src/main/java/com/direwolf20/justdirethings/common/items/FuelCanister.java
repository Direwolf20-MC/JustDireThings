package com.direwolf20.justdirethings.common.items;

import com.direwolf20.justdirethings.common.blocks.resources.CoalBlock_T1;
import com.direwolf20.justdirethings.common.containers.FuelCanisterContainer;
import com.direwolf20.justdirethings.common.items.datacomponents.JustDireDataComponents;
import com.direwolf20.justdirethings.common.items.resources.Coal_T1;
import com.direwolf20.justdirethings.setup.Config;
import com.direwolf20.justdirethings.util.MagicHelpers;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.FuelValues;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class FuelCanister extends Item {
    public FuelCanister(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, context, display, tooltip, flagIn);
        Level level = context.level();
        if (level == null) {
            return;
        }

        List<Component> buffer = new ArrayList<>();
        boolean sneakPressed = Minecraft.getInstance().hasShiftDown();
        if (sneakPressed)
            buffer.add(Component.translatable("justdirethings.fuelcanisteramt", MagicHelpers.formatted(getFuelLevel(stack))).withStyle(ChatFormatting.AQUA));
        else
            buffer.add(Component.translatable("justdirethings.fuelcanisteritemsamt", MagicHelpers.formatted(((float) getFuelLevel(stack) / 200))).withStyle(ChatFormatting.AQUA));
        buffer.forEach(tooltip);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (level.isClientSide()) return InteractionResult.PASS;

        player.openMenu(new SimpleMenuProvider(
                (windowId, playerInventory, playerEntity) -> new FuelCanisterContainer(windowId, playerInventory, player, itemstack), Component.translatable("")), (buf -> {
            ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, itemstack);
        }));

        return InteractionResult.PASS;
    }

    @Override
    public int getBurnTime(ItemStack stack, @Nullable RecipeType<?> recipeType, FuelValues fuelValues) {
        return getFuelLevel(stack) >= Config.FUEL_CANISTER_MINIMUM_TICKS_CONSUMED.get() ? Config.FUEL_CANISTER_MINIMUM_TICKS_CONSUMED.get() : 0;
    }

    @Override
    public @Nullable ItemStackTemplate getCraftingRemainder(net.minecraft.world.item.ItemInstance instance) {
        // Retain crafting-remainder semantics: return a decremented-fuel copy of this canister.
        if (!(instance instanceof ItemStack source)) return null;
        ItemStack copy = source.copy();
        decrementFuel(copy);
        return ItemStackTemplate.fromNonEmptyStack(copy);
    }

    public static int getFuelLevel(ItemStack stack) {
        return stack.getOrDefault(JustDireDataComponents.FUELCANISTER_FUELLEVEL, 0);
    }

    public static void setFuelLevel(ItemStack stack, int fuelLevel) {
        stack.set(JustDireDataComponents.FUELCANISTER_FUELLEVEL, fuelLevel);
    }

    public static double getBurnSpeed(ItemStack stack) {
        return stack.getOrDefault(JustDireDataComponents.FUELCANISTER_BURNSPEED, 1.0);
    }

    public static int getBurnSpeedMultiplier(ItemStack stack) {
        double burnSpeed = getBurnSpeed(stack);
        return (int) Math.round(burnSpeed);
    }

    public static void setBurnSpeed(ItemStack stack, double burnSpeed) {
        stack.set(JustDireDataComponents.FUELCANISTER_BURNSPEED, burnSpeed);
    }

    public static void decrementFuel(ItemStack stack) {
        int currentFuel = getFuelLevel(stack);
        if (currentFuel >= Config.FUEL_CANISTER_MINIMUM_TICKS_CONSUMED.get()) //Should always be true but lets be sure!
            currentFuel = currentFuel - Config.FUEL_CANISTER_MINIMUM_TICKS_CONSUMED.get();
        setFuelLevel(stack, currentFuel);
    }

    public static double calculateBurnSpeed(int currentFuelLevel, double currentBurnSpeedMultiplier, int newFuelLevel, double newFuelMultiplier) {
        double totalFuel = currentFuelLevel + newFuelLevel;
        // Calculate the weighted average of the multipliers based on the fuel levels.
        double newBurnSpeedMultiplier = ((currentFuelLevel * currentBurnSpeedMultiplier) + (newFuelLevel * newFuelMultiplier)) / totalFuel;
        return newBurnSpeedMultiplier;
    }

    public static void incrementFuel(ItemStack stack, ItemStack fuelStack, FuelValues fuelValues) {
        int currentFuel = getFuelLevel(stack);
        int fuelPerPiece = fuelStack.getBurnTime(RecipeType.SMELTING, fuelValues);
        if (fuelPerPiece == 0) return;
        double currentBurnSpeedMultiplier = getBurnSpeed(stack);
        int fuelMultiplier = 1;
        if (fuelStack.getItem() instanceof Coal_T1 direCoal)
            fuelMultiplier = direCoal.getBurnSpeedMultiplier();
        else if (fuelStack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof CoalBlock_T1 coalBlock)
            fuelMultiplier = coalBlock.getBurnSpeedMultiplier();
        int totalNewFuel = 0;
        while ((currentFuel + totalNewFuel) + fuelPerPiece <= Config.FUEL_CANISTER_MAXIMUM_FUEL.get() && !fuelStack.isEmpty()) {
            totalNewFuel += fuelPerPiece;
            fuelStack.shrink(1); // Consume one unit of the fuel stack.
        }

        if (totalNewFuel > 0) {
            currentBurnSpeedMultiplier = calculateBurnSpeed(currentFuel, currentBurnSpeedMultiplier, totalNewFuel, fuelMultiplier);
        }

        setFuelLevel(stack, currentFuel + totalNewFuel);
        setBurnSpeed(stack, currentBurnSpeedMultiplier);
    }

}
