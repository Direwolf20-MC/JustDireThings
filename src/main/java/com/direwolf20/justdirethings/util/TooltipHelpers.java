package com.direwolf20.justdirethings.util;

import com.direwolf20.justdirethings.client.KeyBindings;
import com.direwolf20.justdirethings.common.items.datacomponents.JustDireDataComponents;
import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.common.items.interfaces.PoweredItem;
import com.direwolf20.justdirethings.common.items.interfaces.ToggleableItem;
import com.direwolf20.justdirethings.common.items.interfaces.ToggleableTool;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;

import java.util.List;

public class TooltipHelpers {

    public static void appendFEText(ItemStack stack, List<Component> tooltip) {
        if (!(stack.getItem() instanceof PoweredItem poweredItem))
            return;

        EnergyHandler energy = stack.getCapability(Capabilities.Energy.ITEM, ItemAccess.forStack(stack));
        if (energy == null) {
            return;
        }
        int stored = energy.getAmountAsInt();
        int max = energy.getCapacityAsInt();
        if (Minecraft.getInstance().hasShiftDown())
            tooltip.add(Component.translatable("justdirethings.festored", MagicHelpers.formatted(stored), MagicHelpers.formatted(max)).withStyle(ChatFormatting.GREEN));
        else
            tooltip.add(Component.translatable("justdirethings.festored", MagicHelpers.tidyValue(stored), MagicHelpers.tidyValue(max)).withStyle(ChatFormatting.GREEN));
    }

    public static void appendToolEnabled(ItemStack stack, List<Component> tooltip) {
        if (stack.getItem() instanceof ToggleableItem toggleableItem) {
            if (toggleableItem.getEnabled(stack))
                tooltip.add(Component.translatable("justdirethings.enabled")
                        .withStyle(ChatFormatting.GREEN)
                        .append(Component.literal(" ")
                                .append(Component.translatable("justdirethings.presshotkey", KeyBindings.toggleTool.getKey().getDisplayName())
                                        .withStyle(ChatFormatting.DARK_GRAY)))
                );
            else
                tooltip.add(Component.translatable("justdirethings.disabled")
                        .withStyle(ChatFormatting.DARK_RED)
                        .append(Component.literal(" ")
                                .append(Component.translatable("justdirethings.presshotkey", KeyBindings.toggleTool.getKey().getDisplayName())
                                        .withStyle(ChatFormatting.DARK_GRAY)))
                );
        }
    }

    public static void appendAbilityList(ItemStack stack, List<Component> tooltip) {
        if (stack.getItem() instanceof ToggleableTool toggleableTool) {
            NBTHelpers.BoundInventory boundInventory = ToggleableTool.getBoundInventory(stack);
            for (Ability ability : toggleableTool.getAbilities()) {
                boolean active = ToggleableTool.getSetting(stack, ability.getName());
                ChatFormatting chatFormatting = ChatFormatting.GRAY;
                if (!ToggleableTool.hasUpgrade(stack, ability)) {
                    tooltip.add(Component.translatable(ability.getLocalization()).append(Component.translatable("justdirethings.missingupgrade")).withStyle(chatFormatting));
                } else {
                    chatFormatting = active ? ChatFormatting.GREEN : ChatFormatting.DARK_RED;
                    tooltip.add(Component.translatable(ability.getLocalization()).withStyle(chatFormatting));
                }
                if (ability.equals(Ability.DROPTELEPORT) && ToggleableTool.hasUpgrade(stack, ability)) {
                    chatFormatting = ChatFormatting.DARK_PURPLE;
                    String dimString;
                    if (boundInventory == null) {
                        dimString = I18n.get("justdirethings.unbound");
                        tooltip.add(Component.literal(dimString).withStyle(chatFormatting));
                    } else {
                        dimString = " -" + I18n.get(boundInventory.globalPos().dimension().identifier().getPath()) + ": [" + boundInventory.globalPos().pos().toShortString() + "]";
                        tooltip.add(Component.literal(dimString).withStyle(chatFormatting));
                        tooltip.add(Component.literal("").append(Component.translatable("justdirethings.boundside")).append(Component.translatable("justdirethings.screen.direction-" + boundInventory.direction().getName())).withStyle(chatFormatting));
                    }
                }
            }
        }
    }

    public static void appendShiftForInfo(ItemStack stack, List<Component> tooltip) {
        tooltip.add(Component.translatable("justdirethings.shiftmoreinfo").withStyle(ChatFormatting.GRAY));
    }

    public static void appendUpgradeDetails(ItemStack stack, List<Component> tooltip) {
        Ability ability = Ability.getAbilityFromUpgradeItem(stack.getItem());
        if (ability == null) return;

        String detailTextKey = "justdirethings." + ability.getName() + ".detailtext";
        String flavorTextKey = "justdirethings." + ability.getName() + ".flavortext";

        MutableComponent detailTextComponent = Component.translatable(detailTextKey);
        if (!detailTextComponent.getString().equals(detailTextKey)) {
            tooltip.add(detailTextComponent.withStyle(ChatFormatting.GREEN));
        }

        MutableComponent flavorTextComponent = Component.translatable(flavorTextKey);
        if (!flavorTextComponent.getString().equals(flavorTextKey)) {
            tooltip.add(flavorTextComponent.withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
        }
    }

    public static void appendGeneratorDetails(ItemStack stack, List<Component> tooltip) {
        ResourceHandler<ItemResource> handler = ItemAccess.forStack(stack).getCapability(Capabilities.Item.ITEM);
        ItemStack fuelStack = handler == null ? ItemStack.EMPTY : handler.getResource(0).toStack(handler.getAmountAsInt(0));
        if (Minecraft.getInstance().hasShiftDown()) {
            tooltip.add(Component.translatable("justdirethings.pocketgeneratorburntime", stack.getOrDefault(JustDireDataComponents.POCKETGEN_COUNTER, 0), stack.getOrDefault(JustDireDataComponents.POCKETGEN_MAXBURN, 0)).withStyle(ChatFormatting.DARK_RED));
            if (fuelStack.isEmpty())
                tooltip.add(Component.translatable("justdirethings.pocketgeneratornofuel").withStyle(ChatFormatting.RED));
            else
                tooltip.add(Component.translatable("justdirethings.pocketgeneratorfuelstack", fuelStack.getCount(), fuelStack.getItem().getName(fuelStack)).withStyle(ChatFormatting.DARK_AQUA));
        } else {
            if (fuelStack.isEmpty())
                tooltip.add(Component.translatable("justdirethings.pocketgeneratornofuel").withStyle(ChatFormatting.RED));
        }
    }
}
