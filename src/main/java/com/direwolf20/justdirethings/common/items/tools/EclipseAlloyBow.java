package com.direwolf20.justdirethings.common.items.tools;

import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.common.items.interfaces.AbilityParams;
import com.direwolf20.justdirethings.common.items.interfaces.PoweredTool;
import com.direwolf20.justdirethings.common.items.tools.basetools.BaseBow;
import net.minecraft.world.item.ItemStack;

public class EclipseAlloyBow extends BaseBow implements PoweredTool {
    public EclipseAlloyBow() {
        super(new Properties().durability(450).fireResistant());
        registerAbility(Ability.POTIONARROW);
        registerAbility(Ability.SPLASH);
        registerAbility(Ability.LINGERING);
        registerAbility(Ability.HOMING);
        registerAbility(Ability.EPICARROW, new AbilityParams(1, 1, 1, 1, 200, 100));
        registerAbility(Ability.PHASE);
    }

    public float getMaxDraw() {
        return 10;
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
