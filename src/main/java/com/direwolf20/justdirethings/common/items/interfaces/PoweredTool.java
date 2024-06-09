package com.direwolf20.justdirethings.common.items.interfaces;

import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public interface PoweredTool extends PoweredItem {
    default ItemAttributeModifiers getPoweredAttributeModifiers(ItemStack stack, ItemAttributeModifiers originalModifiers) {
        ItemAttributeModifiers modifiers = ItemAttributeModifiers.builder().build();
        if (PoweredItem.getAvailableEnergy(stack) >= getBlockBreakFECost()) {
            return originalModifiers;
        } else {
            for (ItemAttributeModifiers.Entry entry : originalModifiers.modifiers()) {
                if (!entry.attribute().equals(Attributes.ATTACK_DAMAGE))
                    modifiers.withModifierAdded(entry.attribute(), entry.modifier(), entry.slot());
            }
        }
        return modifiers;
    }

    default int getBlockBreakFECost() {
        return 50; //Todo Config?
    }
}
