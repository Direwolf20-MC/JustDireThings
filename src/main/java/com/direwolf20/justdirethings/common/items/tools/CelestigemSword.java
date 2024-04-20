package com.direwolf20.justdirethings.common.items.tools;

import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.common.items.interfaces.PoweredTool;
import com.direwolf20.justdirethings.common.items.tools.basetools.BaseSword;
import com.direwolf20.justdirethings.common.items.tools.utils.GooTier;
import net.minecraft.world.item.ItemStack;

public class CelestigemSword extends BaseSword implements PoweredTool {
    public CelestigemSword() {
        super(GooTier.CELESTIGEM, 3, -2.0F, new Properties().fireResistant());
        registerAbility(Ability.MOBSCANNER);
        registerAbility(Ability.CAUTERIZEWOUNDS);
        registerAbility(Ability.DROPTELEPORT);
        registerAbility(Ability.SMOKER);
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
