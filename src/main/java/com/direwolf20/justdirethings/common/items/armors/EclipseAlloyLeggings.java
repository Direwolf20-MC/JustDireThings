package com.direwolf20.justdirethings.common.items.armors;

import com.direwolf20.justdirethings.common.items.armors.basearmors.BaseLeggings;
import com.direwolf20.justdirethings.common.items.armors.utils.ArmorTiers;
import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.common.items.interfaces.AbilityParams;
import com.direwolf20.justdirethings.common.items.interfaces.PoweredTool;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.ArmorType;

public class EclipseAlloyLeggings extends BaseLeggings implements PoweredTool {
    public EclipseAlloyLeggings() {
        super(new Item.Properties()
                .humanoidArmor(ArmorTiers.ECLIPSEALLOY, ArmorType.LEGGINGS)
                .fireResistant()
                .durability(ArmorType.LEGGINGS.getDurability(25)));
        registerAbility(Ability.RUNSPEED, new AbilityParams(1, 5, 1));
        registerAbility(Ability.WALKSPEED, new AbilityParams(1, 5, 1));
        registerAbility(Ability.SWIMSPEED, new AbilityParams(1, 5, 1));
        registerAbility(Ability.DECOY, new AbilityParams(1, 1, 1, 1, 200, 1200));
        registerAbility(Ability.PHASE);
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
    public int getMaxEnergy() {
        return 500000;
    }
}
