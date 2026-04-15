package com.direwolf20.justdirethings.common.items.armors;

import com.direwolf20.justdirethings.common.items.armors.basearmors.BaseChestplate;
import com.direwolf20.justdirethings.common.items.armors.utils.ArmorTiers;
import com.direwolf20.justdirethings.common.items.interfaces.*;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Unit;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.ArmorType;

public class EclipseAlloyChestplate extends BaseChestplate implements PoweredTool {
    // 26.1: gliding is component-driven via DataComponents.GLIDER. See CelestigemChestplate for details.
    // TODO: restore toggle-gated gliding. The GLIDER component is static here, so the ELYTRA toggle
    //   no longer actually stops gliding. Re-wire via a LivingTickEvent handler that adds/removes
    //   DataComponents.GLIDER based on canUseAbilityAndDurability(stack, Ability.ELYTRA), and calls
    //   entity.stopFallFlying() when toggling off mid-flight.
    public EclipseAlloyChestplate() {
        super(new Item.Properties()
                .humanoidArmor(ArmorTiers.ECLIPSEALLOY, ArmorType.CHESTPLATE)
                .fireResistant()
                .durability(ArmorType.CHESTPLATE.getDurability(25))
                .component(DataComponents.GLIDER, Unit.INSTANCE));
        registerAbility(Ability.INVULNERABILITY, new AbilityParams(1, 1, 1, 1, 200, 400));
        registerAbility(Ability.EXTINGUISH, new AbilityParams(1, 1, 1, 1, 0, 40));
        registerAbility(Ability.ELYTRA);
        registerAbility(Ability.FLIGHT);
        registerAbility(Ability.LAVAIMMUNITY);
        registerAbility(Ability.DEATHPROTECTION, new AbilityParams(1, 1, 1, 1, 0, 6000));
        registerAbility(Ability.TIMEPROTECTION);
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

    public static boolean isFlyEnabled(ItemStack elytraStack) {
        return elytraStack.getItem() instanceof ToggleableTool toggleableTool && toggleableTool.canUseAbilityAndDurability(elytraStack, Ability.ELYTRA);
    }

    @Override
    public int getMaxEnergy() {
        return 500000;
    }
}
