package com.direwolf20.justdirethings.common.items.armors;

import com.direwolf20.justdirethings.common.items.armors.basearmors.BaseBoots;
import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.common.items.interfaces.AbilityParams;
import com.direwolf20.justdirethings.common.items.interfaces.PoweredTool;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class CelestigemBoots extends BaseBoots implements PoweredTool {
    public CelestigemBoots(Item.Properties pProperties) {
        super(pProperties);
        registerAbility(Ability.STEPHEIGHT);
        registerAbility(Ability.JUMPBOOST, new AbilityParams(1, 3, 1, 3));
        registerAbility(Ability.GROUNDSTOMP, new AbilityParams(1, 3, 1, 3, 0, 200));
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
}
