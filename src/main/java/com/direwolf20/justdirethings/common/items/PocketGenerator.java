package com.direwolf20.justdirethings.common.items;

import com.direwolf20.justdirethings.common.blocks.resources.CoalBlock_T1;
import com.direwolf20.justdirethings.common.capabilities.EnergyStorageItemStackNoReceive;
import com.direwolf20.justdirethings.common.containers.PocketGeneratorContainer;
import com.direwolf20.justdirethings.common.items.datacomponents.JustDireDataComponents;
import com.direwolf20.justdirethings.common.items.interfaces.PoweredItem;
import com.direwolf20.justdirethings.common.items.interfaces.ToggleableItem;
import com.direwolf20.justdirethings.common.items.resources.Coal_T1;
import com.direwolf20.justdirethings.setup.Config;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.ComponentItemHandler;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.direwolf20.justdirethings.util.TooltipHelpers.*;

public class PocketGenerator extends Item implements PoweredItem, ToggleableItem {

    public static final EntityCapability<IItemHandler, Void> CURIOS_INVENTORY =
            EntityCapability.createVoid(ResourceLocation.fromNamespaceAndPath("curios", "item_handler"), IItemHandler.class);

    public PocketGenerator() {
        super(new Properties()
                .stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (level.isClientSide()) return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);

        if (!player.isShiftKeyDown()) {
            player.openMenu(new SimpleMenuProvider(
                    (windowId, playerInventory, playerEntity) -> new PocketGeneratorContainer(windowId, playerInventory, player, itemstack), Component.translatable("")), (buf -> {
                ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, itemstack);
            }));
        }
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
    }

    @Override
    public void inventoryTick(@NotNull ItemStack itemStack, @NotNull Level world, @NotNull Entity entity, int itemSlot, boolean isSelected) {
        if (world.isClientSide) return;
        if (entity instanceof Player player && itemStack.getItem() instanceof ToggleableItem toggleableItem && toggleableItem.getEnabled(itemStack)) {
            IEnergyStorage energyStorage = itemStack.getCapability(Capabilities.EnergyStorage.ITEM);
            if (energyStorage == null) return;
            if (energyStorage instanceof EnergyStorageItemStackNoReceive EnergyStorageItemStackNoReceive) {
                tryBurn(EnergyStorageItemStackNoReceive, itemStack);
                if (energyStorage.getEnergyStored() >= (getFEPerTick() / 10)) { //If we have 1/10th the max transfer speed, go ahead and let it rip
                    for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                        ItemStack slotStack = player.getInventory().getItem(i);
                        transferEnergy(slotStack, energyStorage);
                    }
                    IItemHandler curios = player.getCapability(CURIOS_INVENTORY);
                    if (curios != null) {
                        for (int i = 0; i < curios.getSlots(); i++) {
                            ItemStack slotStack = curios.getStackInSlot(i);
                            transferEnergy(slotStack, energyStorage);
                        }
                    }
                }
            }
        }
    }

    private void transferEnergy(ItemStack slotStack, IEnergyStorage energyStorage) {
        IEnergyStorage slotEnergy = slotStack.getCapability(Capabilities.EnergyStorage.ITEM);
        if (slotEnergy != null) {
            int acceptedEnergy = slotEnergy.receiveEnergy(getFEPerTick(), true);
            if (acceptedEnergy > 0) {
                int extractedEnergy = energyStorage.extractEnergy(acceptedEnergy, false);
                slotEnergy.receiveEnergy(extractedEnergy, false);
            }
        }
    }

    private int fePerTick(ItemStack itemStack) {
        return (getFePerFuelTick() * getBurnSpeedMultiplier(itemStack));
    }

    public void tryBurn(EnergyStorageItemStackNoReceive energyStorage, ItemStack itemStack) {
        boolean canInsertEnergy = energyStorage.forceReceiveEnergy(fePerTick(itemStack), true) > 0;
        if (itemStack.getOrDefault(JustDireDataComponents.POCKETGEN_COUNTER, 0) > 0 && canInsertEnergy) {
            burn(energyStorage, itemStack);
        } else if (canInsertEnergy) {
            if (initBurn(itemStack))
                burn(energyStorage, itemStack);
        }
    }


    private void burn(EnergyStorageItemStackNoReceive energyStorage, ItemStack itemStack) {
        energyStorage.forceReceiveEnergy(fePerTick(itemStack), false);
        int counter = itemStack.getOrDefault(JustDireDataComponents.POCKETGEN_COUNTER, 0);
        counter--;
        itemStack.set(JustDireDataComponents.POCKETGEN_COUNTER, counter);
        if (counter == 0) {
            itemStack.set(JustDireDataComponents.POCKETGEN_MAXBURN, 0);
            initBurn(itemStack);
        }
    }

    private boolean initBurn(ItemStack itemStack) {
        ComponentItemHandler handler = new ComponentItemHandler(itemStack, JustDireDataComponents.ITEMSTACK_HANDLER.get(), 1);
        ItemStack fuelStack = handler.getStackInSlot(0);

        int burnTime = fuelStack.getBurnTime(RecipeType.SMELTING);
        if (burnTime > 0) {
            if (fuelStack.getItem() instanceof Coal_T1 direCoal) {
                setFuelMultiplier(itemStack, direCoal.getBurnSpeedMultiplier());
            } else if (fuelStack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof CoalBlock_T1 coalBlock) {
                setFuelMultiplier(itemStack, coalBlock.getBurnSpeedMultiplier());
            } else if (fuelStack.getItem() instanceof FuelCanister) {
                setFuelMultiplier(itemStack, FuelCanister.getBurnSpeedMultiplier(fuelStack));
            } else {
                setFuelMultiplier(itemStack, 1);
            }
            if (fuelStack.hasCraftingRemainingItem())
                handler.setStackInSlot(0, fuelStack.getCraftingRemainingItem());
            else {
                fuelStack.shrink(1);
                handler.setStackInSlot(0, fuelStack);
            }


            int counter = (int) (Math.floor(burnTime) / getBurnSpeedMultiplier(itemStack));
            int maxBurn = counter;
            itemStack.set(JustDireDataComponents.POCKETGEN_COUNTER, counter);
            itemStack.set(JustDireDataComponents.POCKETGEN_MAXBURN, maxBurn);
            return true;
        }
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, context, tooltip, flagIn);
        Level level = context.level();
        if (level == null) {
            return;
        }
        appendFEText(stack, tooltip);
        appendToolEnabled(stack, tooltip);
        appendGeneratorDetails(stack, tooltip);
        appendShiftForInfo(stack, tooltip);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return isPowerBarVisible(stack);
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return getPowerBarWidth(stack);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        int color = getPowerBarColor(stack);
        if (color == -1)
            return super.getBarColor(stack);
        return color;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.NONE;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    public void setFuelMultiplier(ItemStack itemStack, int amount) {
        itemStack.set(JustDireDataComponents.POCKETGEN_FUELMULT, amount);
    }

    public int getFuelMultiplier(ItemStack itemStack) {
        return itemStack.getOrDefault(JustDireDataComponents.POCKETGEN_FUELMULT, 1);
    }

    @Override
    public int getMaxEnergy() {
        return Config.POCKET_GENERATOR_MAX_FE.get();
    }

    public int getFEPerTick() {
        return Config.POCKET_GENERATOR_FE_PER_TICK.get();
    }

    public int getFePerFuelTick() {
        return Config.POCKET_GENERATOR_FE_PER_FUEL_TICK.get();
    }

    public int getBurnSpeedMultiplier(ItemStack itemStack) {
        return Config.POCKET_GENERATOR_BURN_SPEED_MULTIPLIER.get() * getFuelMultiplier(itemStack);
    }
}
