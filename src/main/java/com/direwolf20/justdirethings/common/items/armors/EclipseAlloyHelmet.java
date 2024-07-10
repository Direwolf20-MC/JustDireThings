package com.direwolf20.justdirethings.common.items.armors;

import com.direwolf20.justdirethings.common.items.armors.basearmors.BaseHelmet;
import com.direwolf20.justdirethings.common.items.armors.utils.ArmorTiers;
import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.common.items.interfaces.AbilityParams;
import com.direwolf20.justdirethings.common.items.interfaces.PoweredTool;
import net.minecraft.world.item.ItemStack;

public class EclipseAlloyHelmet extends BaseHelmet implements PoweredTool {
    public EclipseAlloyHelmet() {
        super(ArmorTiers.ECLIPSEALLOY, new Properties()
                .fireResistant()
                .durability(Type.HELMET.getDurability(25)));
        registerAbility(Ability.MINDFOG);
        registerAbility(Ability.STUPEFY, new AbilityParams(1, 1, 1, 1, 100, 200));
        registerAbility(Ability.NIGHTVISION);
        registerAbility(Ability.NOAI, new AbilityParams(1, 1, 1, 1, 0, 2400));
        registerAbility(Ability.DEBUFFREMOVER, new AbilityParams(1, 1, 1, 1, 0, 400));

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
