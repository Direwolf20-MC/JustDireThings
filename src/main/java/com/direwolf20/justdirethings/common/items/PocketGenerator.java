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
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static com.direwolf20.justdirethings.util.TooltipHelpers.*;

public class PocketGenerator extends Item implements PoweredItem, ToggleableItem {

    public static final EntityCapability<ResourceHandler<ItemResource>, Void> CURIOS_INVENTORY =
            EntityCapability.createVoid(Identifier.fromNamespaceAndPath("curios", "item_handler"),
                    (Class<ResourceHandler<ItemResource>>) (Class<?>) ResourceHandler.class);

    public PocketGenerator(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (level.isClientSide()) return InteractionResult.SUCCESS.heldItemTransformedTo(itemstack);

        if (!player.isShiftKeyDown()) {
            player.openMenu(new SimpleMenuProvider(
                    (windowId, playerInventory, playerEntity) -> new PocketGeneratorContainer(windowId, playerInventory, player, itemstack), Component.translatable("")), (buf -> {
                ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, itemstack);
            }));
        }
        return InteractionResult.SUCCESS.heldItemTransformedTo(itemstack);
    }

    @Override
    public void inventoryTick(@NotNull ItemStack itemStack, @NotNull ServerLevel world, @NotNull Entity entity, @Nullable EquipmentSlot slot) {
        if (entity instanceof Player player && itemStack.getItem() instanceof ToggleableItem toggleableItem && toggleableItem.getEnabled(itemStack)) {
            EnergyHandler energyStorage = itemStack.getCapability(Capabilities.Energy.ITEM, null);
            if (energyStorage == null) return;
            if (energyStorage instanceof EnergyStorageItemStackNoReceive pocketEnergy) {
                tryBurn(pocketEnergy, itemStack, world);
                if (pocketEnergy.getAmountAsInt() >= (getFEPerTick() / 10)) { //If we have 1/10th the max transfer speed, go ahead and let it rip
                    for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                        ItemStack slotStack = player.getInventory().getItem(i);
                        transferEnergy(slotStack, pocketEnergy);
                    }
                    ResourceHandler<ItemResource> curios = player.getCapability(CURIOS_INVENTORY);
                    if (curios != null) {
                        for (int i = 0; i < curios.size(); i++) {
                            ItemResource resource = curios.getResource(i);
                            if (resource.isEmpty()) continue;
                            ItemStack slotStack = resource.toStack(curios.getAmountAsInt(i));
                            transferEnergy(slotStack, pocketEnergy);
                        }
                    }
                }
            }
        }
    }

    private void transferEnergy(ItemStack slotStack, EnergyHandler energyStorage) {
        EnergyHandler slotEnergy = slotStack.getCapability(Capabilities.Energy.ITEM, null);
        if (slotEnergy == null) return;
        int acceptedEnergy;
        try (Transaction probe = Transaction.openRoot()) {
            acceptedEnergy = slotEnergy.insert(getFEPerTick(), probe);
        }
        if (acceptedEnergy <= 0) return;
        try (Transaction tx = Transaction.openRoot()) {
            int extracted = energyStorage.extract(acceptedEnergy, tx);
            if (extracted <= 0) return;
            int inserted = slotEnergy.insert(extracted, tx);
            if (inserted != extracted) return;
            tx.commit();
        }
    }

    private int fePerTick(ItemStack itemStack) {
        return (getFePerFuelTick() * getBurnSpeedMultiplier(itemStack));
    }

    public void tryBurn(EnergyStorageItemStackNoReceive energyStorage, ItemStack itemStack, ServerLevel world) {
        boolean canInsertEnergy = energyStorage.forceReceiveEnergy(fePerTick(itemStack), true) > 0;
        if (itemStack.getOrDefault(JustDireDataComponents.POCKETGEN_COUNTER, 0) > 0 && canInsertEnergy) {
            burn(energyStorage, itemStack);
        } else if (canInsertEnergy) {
            if (initBurn(itemStack, world))
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
            initBurn(itemStack, null);
        }
    }

    private boolean initBurn(ItemStack itemStack, @Nullable ServerLevel world) {
        ResourceHandler<ItemResource> handler = ItemAccess.forStack(itemStack).getCapability(Capabilities.Item.ITEM);
        if (handler == null) return false;
        ItemResource fuelResource = handler.getResource(0);
        int fuelCount = handler.getAmountAsInt(0);
        if (fuelResource.isEmpty() || fuelCount <= 0) return false;
        ItemStack fuelStack = fuelResource.toStack(fuelCount);

        int burnTime = world != null ? fuelStack.getBurnTime(RecipeType.SMELTING, world.fuelValues()) : 0;
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
            ItemStackTemplate remainderTemplate = fuelStack.getItem().getCraftingRemainder();
            try (Transaction tx = Transaction.openRoot()) {
                handler.extract(0, fuelResource, 1, tx);
                if (remainderTemplate != null) {
                    ItemStack remainder = remainderTemplate.create();
                    handler.insert(0, ItemResource.of(remainder), remainder.getCount(), tx);
                }
                tx.commit();
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
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, context, display, tooltip, flagIn);
        Level level = context.level();
        if (level == null) {
            return;
        }
        List<Component> buffer = new ArrayList<>();
        appendFEText(stack, buffer);
        appendToolEnabled(stack, buffer);
        appendGeneratorDetails(stack, buffer);
        appendShiftForInfo(stack, buffer);
        buffer.forEach(tooltip);
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
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return ItemUseAnimation.NONE;
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
