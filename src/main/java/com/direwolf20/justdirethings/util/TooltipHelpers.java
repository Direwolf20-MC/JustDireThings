package com.direwolf20.justdirethings.util;

import com.direwolf20.justdirethings.client.KeyBindings;
import com.direwolf20.justdirethings.common.items.tools.utils.Ability;
import com.direwolf20.justdirethings.common.items.tools.utils.PoweredItem;
import com.direwolf20.justdirethings.common.items.tools.utils.ToggleableTool;
import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.List;

import static com.direwolf20.justdirethings.common.items.PocketGenerator.COUNTER;
import static com.direwolf20.justdirethings.common.items.PocketGenerator.MAXBURN;

public class TooltipHelpers {

    public static void appendFEText(ItemStack stack, List<Component> tooltip) {
        if (!(stack.getItem() instanceof PoweredItem poweredItem))
            return;

        var energy = stack.getCapability(Capabilities.EnergyStorage.ITEM);
        if (energy == null) {
            return;
        }
        if (Screen.hasShiftDown())
            tooltip.add(Component.translatable("justdirethings.festored", MagicHelpers.formatted(energy.getEnergyStored()), MagicHelpers.formatted(energy.getMaxEnergyStored())).withStyle(ChatFormatting.GREEN));
        else
            tooltip.add(Component.translatable("justdirethings.festored", MagicHelpers.tidyValue(energy.getEnergyStored()), MagicHelpers.tidyValue(energy.getMaxEnergyStored())).withStyle(ChatFormatting.GREEN));
    }

    public static void appendToolEnabled(ItemStack stack, List<Component> tooltip) {
        if (ToggleableTool.getEnabled(stack))
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

    public static void appendAbilityList(ItemStack stack, List<Component> tooltip) {
        if (stack.getItem() instanceof ToggleableTool toggleableTool) {
            NBTHelpers.BoundInventory boundInventory = ToggleableTool.getBoundInventory(stack);
            for (Ability ability : toggleableTool.getAbilities()) {
                boolean active = ToggleableTool.getSetting(stack, ability.getName());
                ChatFormatting chatFormatting = active ? ChatFormatting.GREEN : ChatFormatting.DARK_RED;
                tooltip.add(Component.translatable(ability.getLocalization()).withStyle(chatFormatting));
                if (ability.equals(Ability.DROPTELEPORT)) {
                    chatFormatting = ChatFormatting.DARK_PURPLE;
                    String dimString;
                    if (boundInventory == null)
                        dimString = I18n.get("justdirethings.unbound");
                    else
                        dimString = " -" + I18n.get(boundInventory.globalPos().dimension().location().getPath()) + ": [" + boundInventory.globalPos().pos().toShortString() + "]";
                    tooltip.add(Component.literal(dimString).withStyle(chatFormatting));
                }
            }
        }
    }

    public static void appendShiftForInfo(ItemStack stack, List<Component> tooltip) {
        tooltip.add(Component.translatable("justdirethings.shiftmoreinfo").withStyle(ChatFormatting.GRAY));
    }

    public static void appendGeneratorDetails(ItemStack stack, List<Component> tooltip) {
        tooltip.add(Component.translatable("justdirethings.pocketgeneratorburntime", NBTHelpers.getIntValue(stack, COUNTER), NBTHelpers.getIntValue(stack, MAXBURN)).withStyle(ChatFormatting.DARK_RED));
        ItemStackHandler itemStackHandler = stack.getData(Registration.HANDLER);
        ItemStack fuelStack = itemStackHandler.getStackInSlot(0);
        if (fuelStack.isEmpty())
            tooltip.add(Component.translatable("justdirethings.pocketgeneratornofuel").withStyle(ChatFormatting.RED));
        else
            tooltip.add(Component.translatable("justdirethings.pocketgeneratorfuelstack", fuelStack.getCount(), fuelStack.getItem().getName(fuelStack)).withStyle(ChatFormatting.DARK_AQUA));
    }
}
