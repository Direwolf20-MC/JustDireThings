package com.direwolf20.justdirethings.common.items.interfaces;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public interface PoweredTool extends PoweredItem {
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
        return 50; //Todo Config?
    }
}
