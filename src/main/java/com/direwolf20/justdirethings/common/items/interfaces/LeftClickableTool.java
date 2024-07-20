package com.direwolf20.justdirethings.common.items.interfaces;

import com.direwolf20.justdirethings.common.items.datacomponents.JustDireDataComponents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface LeftClickableTool {
    static void setBindingMode(ItemStack stack, Ability ability, int mode) {
        stack.set(JustDireDataComponents.ABILITY_BINDING_MODES.get(ability), mode);
    }

    static int getBindingMode(ItemStack stack, Ability ability) {
        return stack.getOrDefault(JustDireDataComponents.ABILITY_BINDING_MODES.get(ability), 0);
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
        List<String> abilitiesNamesList = new ArrayList<>();
        for (Ability ability : abilityList) {
            abilitiesNamesList.add(ability.getName());
        }
        stack.set(JustDireDataComponents.LEFT_CLICK_ABILITIES, abilitiesNamesList);
    }

    static Set<Ability> getLeftClickList(ItemStack stack) {
        Set<Ability> abilities = new HashSet<>();
        List<String> abilitiesList = stack.getOrDefault(JustDireDataComponents.LEFT_CLICK_ABILITIES, new ArrayList<>());
        for (String abilityName : abilitiesList) {
            if (getBindingMode(stack, Ability.byName(abilityName)) == 1)
                abilities.add(Ability.byName(abilityName));
        }
        return abilities;
    }

    static ToolRecords.AbilityBinding getAbilityBinding(ItemStack stack, Ability ability) {
        List<ToolRecords.AbilityBinding> abilityBindings = getCustomBindingList(stack);
        return abilityBindings.stream().filter(k -> k.abilityName().equals(ability.getName()))
                .findFirst()
                .orElse(null);
    }

    static void removeFromCustomBindingList(ItemStack stack, Ability ability) {
        List<ToolRecords.AbilityBinding> abilityBindings = new ArrayList<>(getCustomBindingList(stack));
        abilityBindings.removeIf(k -> k.abilityName().equals(ability.getName()));
        setCustomBindingList(stack, abilityBindings);
    }

    static void addToCustomBindingList(ItemStack stack, ToolRecords.AbilityBinding binding) {
        Ability ability = Ability.byName(binding.abilityName());
        removeFromCustomBindingList(stack, ability);
        List<ToolRecords.AbilityBinding> abilityList = getCustomBindingList(stack);
        abilityList.add(binding);
        setCustomBindingList(stack, abilityList);
    }

    static void setCustomBindingList(ItemStack stack, List<ToolRecords.AbilityBinding> abilityList) {
        stack.set(JustDireDataComponents.ABILITY_BINDINGS, abilityList);
    }

    static List<ToolRecords.AbilityBinding> getCustomBindingList(ItemStack stack) {
        return stack.getOrDefault(JustDireDataComponents.ABILITY_BINDINGS, new ArrayList<>());
    }

    static List<Ability> getCustomBindingListFor(ItemStack stack, int key, boolean isMouse, Player player) {
        List<Ability> returnSet = new ArrayList<>();
        List<ToolRecords.AbilityBinding> abilityBindings = getCustomBindingList(stack);
        boolean isEquipped = ToggleableTool.isItemEquipped(stack, player);
        returnSet.addAll(
                abilityBindings.stream().filter(k -> k.isMouse() == isMouse &&
                                k.key() == key &&
                                stack.get(JustDireDataComponents.ABILITY_BINDING_MODES.get(Ability.byName(k.abilityName()))) == 2 &&
                                (!k.requireEquipped() || isEquipped))
                        .map(ToolRecords.AbilityBinding::abilityName)
                        .map(Ability::byName)
                        .toList()
        );
        return returnSet;
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
