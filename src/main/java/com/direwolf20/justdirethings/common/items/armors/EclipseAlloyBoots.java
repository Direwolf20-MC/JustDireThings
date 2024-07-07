package com.direwolf20.justdirethings.common.items.armors;

import com.direwolf20.justdirethings.common.items.armors.basearmors.BaseBoots;
import com.direwolf20.justdirethings.common.items.armors.utils.ArmorTiers;
import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.common.items.interfaces.AbilityParams;
import com.direwolf20.justdirethings.common.items.interfaces.PoweredTool;
import net.minecraft.world.item.ItemStack;

public class EclipseAlloyBoots extends BaseBoots implements PoweredTool {
    public EclipseAlloyBoots() {
        super(ArmorTiers.ECLIPSEALLOY, new Properties()
                .fireResistant()
                .durability(Type.BOOTS.getDurability(25)));
        registerAbility(Ability.STEPHEIGHT);
        registerAbility(Ability.JUMPBOOST, new AbilityParams(1, 5, 1, 5));
        registerAbility(Ability.GROUNDSTOMP, new AbilityParams(1, 5, 1, 5, 0, 200));
        registerAbility(Ability.NEGATEFALLDAMAGE);
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
