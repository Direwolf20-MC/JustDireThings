package com.direwolf20.justdirethings.common.items.tools.utils;

import com.direwolf20.justdirethings.util.MagicHelpers;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;

import java.util.List;
import java.util.Map;

public interface PoweredItem {
    default Multimap<Attribute, AttributeModifier> getPoweredAttributeModifiers(EquipmentSlot slot, ItemStack stack, Multimap<Attribute, AttributeModifier> originalModifiers) {
        Multimap<Attribute, AttributeModifier> modifiers = HashMultimap.create();
        if (slot == EquipmentSlot.MAINHAND) {
            if (getAvailableEnergy(stack) >= getBlockBreakFECost()) {
                return originalModifiers;
            } else {
                for (Map.Entry<Attribute, AttributeModifier> entry : originalModifiers.entries()) {
                    if (!entry.getKey().equals(Attributes.ATTACK_DAMAGE))
                        modifiers.put(entry.getKey(), entry.getValue());
                }
            }
        }
        return modifiers;
    }

    default int getBlockBreakFECost() {
        return 10; //Todo Config?
    }

    default int getAvailableEnergy(ItemStack stack) {
        var energy = stack.getCapability(Capabilities.EnergyStorage.ITEM);
        if (energy == null) {
            return -1;
        }
        return energy.getEnergyStored();
    }

    default boolean isPowerBarVisible(ItemStack stack) {
        var energy = stack.getCapability(Capabilities.EnergyStorage.ITEM);
        if (energy == null) {
            return false;
        }
        return (energy.getEnergyStored() < energy.getMaxEnergyStored());
    }

    default int getPowerBarWidth(ItemStack stack) {
        var energy = stack.getCapability(Capabilities.EnergyStorage.ITEM);
        if (energy == null) {
            return 13;
        }
        return Math.min(13 * energy.getEnergyStored() / energy.getMaxEnergyStored(), 13);
    }

    default int getPowerBarColor(ItemStack stack) {
        var energy = stack.getCapability(Capabilities.EnergyStorage.ITEM);
        if (energy == null) {
            return -1; //Tell caller to call super
        }
        return Mth.hsvToRgb(Math.max(0.0F, (float) energy.getEnergyStored() / (float) energy.getMaxEnergyStored()) / 3.0F, 1.0F, 1.0F);
    }

    default int getMaxEnergy() {
        return 10000;
    }


    static void appendFEText(ItemStack stack, @javax.annotation.Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
        if (!(stack.getItem() instanceof PoweredItem poweredItem))
            return;
        Minecraft mc = Minecraft.getInstance();

        if (level == null || mc.player == null) {
            return;
        }
        var energy = stack.getCapability(Capabilities.EnergyStorage.ITEM);
        if (energy == null) {
            return;
        }
        tooltip.add(Component.translatable("justdirethings.festored", MagicHelpers.tidyValue(energy.getEnergyStored()), MagicHelpers.tidyValue(energy.getMaxEnergyStored())).withStyle(ChatFormatting.GREEN));
    }
}
