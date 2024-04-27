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

    static Set<Ability> getCustomBindingAbilities(ItemStack stack) {
        Set<Ability> returnSet = new HashSet<>();
        CompoundTag compoundTag = stack.getOrCreateTag();
        // Iterate over all keys in the CompoundTag
        for (String key : compoundTag.getAllKeys()) {
            // Check if the key starts with the desired prefix
            if (key.startsWith("bindingMode_")) {
                if (compoundTag.getInt(key) == 2) { //Custom Binding
                    String[] keyParts = key.split("_");
                    String abilityName = keyParts[1];
                    Ability ability = Ability.valueOf(abilityName.toUpperCase(Locale.ROOT));
                    returnSet.add(ability);
                }
            }
        }
        return returnSet;
    }

    static void setBindingMode(ItemStack stack, Ability ability, int mode) {
        CompoundTag compoundTag = stack.getOrCreateTag();
        compoundTag.putInt("bindingMode_" + ability.getName(), mode);
    }

    static int getBindingMode(ItemStack stack, Ability ability) {
        CompoundTag compoundTag = stack.getOrCreateTag();
        return compoundTag.getInt("bindingMode_" + ability.getName());
    }

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

    static Binding getAbilityBinding(ItemStack stack, Ability ability) {
        CompoundTag compoundTag = stack.getOrCreateTag();

        // Iterate over all keys in the CompoundTag
        for (String key : compoundTag.getAllKeys()) {
            if (key.startsWith("customBindingAbilities_")) {
                ListTag listTag = compoundTag.getList(key, Tag.TAG_COMPOUND);

                // Check each ability in the list
                for (int i = 0; i < listTag.size(); i++) {
                    CompoundTag abilityTag = listTag.getCompound(i);
                    if (abilityTag.getString("abilityName").equalsIgnoreCase(ability.getName())) {
                        // Extract the key code and the mouse flag from the key name
                        String[] keyParts = key.split("_");
                        int keyCode = Integer.parseInt(keyParts[1]);
                        boolean isMouse = Boolean.parseBoolean(keyParts[2]);

                        return new Binding(keyCode, isMouse);
                    }
                }
            }
        }
        return null; // Return null if no binding is found
    }

    static void removeFromCustomBindingList(ItemStack stack, Ability ability) {
        CompoundTag compoundTag = stack.getOrCreateTag();
        // Iterate over all keys in the CompoundTag
        for (String key : compoundTag.getAllKeys()) {
            // Check if the key starts with the desired prefix
            if (key.startsWith("customBindingAbilities_")) {
                ListTag listTag = compoundTag.getList(key, Tag.TAG_COMPOUND);
                ListTag updatedListTag = new ListTag();
                boolean modified = false;

                // Iterate through all abilities in the list
                for (int i = 0; i < listTag.size(); i++) {
                    CompoundTag abilityTag = listTag.getCompound(i);
                    if (!abilityTag.getString("abilityName").equalsIgnoreCase(ability.getName())) {
                        updatedListTag.add(abilityTag);
                    } else {
                        modified = true;  // Mark as modified if the ability is removed
                    }
                }

                // Only update the tag if there was a modification
                if (modified) {
                    compoundTag.put(key, updatedListTag);
                }
            }
        }
    }

    static void addToCustomBindingList(ItemStack stack, Ability ability, Binding binding) {
        removeFromCustomBindingList(stack, ability);
        Set<Ability> abilityList = getCustomBindingList(stack, binding);
        abilityList.add(ability);
        setCustomBindingList(stack, abilityList, binding);
    }

    static void setCustomBindingList(ItemStack stack, Set<Ability> abilityList, Binding binding) {
        CompoundTag compoundTag = stack.getOrCreateTag();
        ListTag abilityListTag = new ListTag();
        for (Ability ability : abilityList) {
            CompoundTag comp = new CompoundTag();
            comp.putString("abilityName", ability.getName());
            abilityListTag.add(comp);
        }
        compoundTag.put("customBindingAbilities_" + binding.keyCode + "_" + binding.isMouse, abilityListTag);
    }

    static Set<Ability> getCustomBindingList(ItemStack stack, Binding binding) {
        Set<Ability> abilities = new HashSet<>();
        Set<Ability> customBoundAbilities = getCustomBindingAbilities(stack);
        CompoundTag compoundTag = stack.getOrCreateTag();
        if (compoundTag.contains("customBindingAbilities_" + binding.keyCode + "_" + binding.isMouse)) {
            ListTag listTag = compoundTag.getList("customBindingAbilities_" + binding.keyCode + "_" + binding.isMouse, Tag.TAG_COMPOUND);
            for (int i = 0; i < listTag.size(); i++) {
                Ability ability = Ability.valueOf(listTag.getCompound(i).getString("abilityName").toUpperCase(Locale.ROOT));
                if (customBoundAbilities.contains(ability))
                    abilities.add(ability);
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


    public static class Binding {
        public final int keyCode;
        public final boolean isMouse;

        public Binding(int keyCode, boolean isMouse) {
            this.keyCode = keyCode;
            this.isMouse = isMouse;
        }
    }
}
