package com.direwolf20.justdirethings.common.items.interfaces;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public interface LeftClickableTool {
    static void removeFromLeftClickList(ItemStack stack, Ability ability) {
        Set<Ability> abilityList = getLeftClickList(stack);
        abilityList.remove(ability);
        setLeftClickList(stack, abilityList);
    }

    static void addToLeftClickList(ItemStack stack, Ability ability) {
        Set<Ability> abilityList = getLeftClickList(stack);
        abilityList.add(ability);
        setLeftClickList(stack, abilityList);
    }

    static void setLeftClickList(ItemStack stack, Set<Ability> abilityList) {
        CompoundTag compoundTag = stack.getOrCreateTag();
        ListTag abilityListTag = new ListTag();
        for (Ability ability : abilityList) {
            CompoundTag comp = new CompoundTag();
            comp.putString("abilityName", ability.getName());
            abilityListTag.add(comp);
        }
        compoundTag.put("leftClickAbilities", abilityListTag);
    }

    static Set<Ability> getLeftClickList(ItemStack stack) {
        Set<Ability> abilities = new HashSet<>();
        CompoundTag compoundTag = stack.getOrCreateTag();
        if (compoundTag.contains("leftClickAbilities")) {
            ListTag listTag = compoundTag.getList("leftClickAbilities", Tag.TAG_COMPOUND);
            for (int i = 0; i < listTag.size(); i++) {
                abilities.add(Ability.valueOf(listTag.getCompound(i).getString("abilityName").toUpperCase(Locale.ROOT)));
            }
        }
        return abilities;
    }

    static ItemStack getLeftClickableItem(Player player) {
        ItemStack mainHand = player.getMainHandItem();
        if (mainHand.getItem() instanceof LeftClickableTool)
            return mainHand;
        ItemStack offHand = player.getOffhandItem();
        if (offHand.getItem() instanceof LeftClickableTool)
            return offHand;
        return ItemStack.EMPTY;
    }
}
