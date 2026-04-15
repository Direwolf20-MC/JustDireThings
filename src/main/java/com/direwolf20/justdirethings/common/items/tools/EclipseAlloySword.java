package com.direwolf20.justdirethings.common.items.tools;

import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.common.items.interfaces.AbilityParams;
import com.direwolf20.justdirethings.common.items.interfaces.GooTieredItem;
import com.direwolf20.justdirethings.common.items.interfaces.PoweredTool;
import com.direwolf20.justdirethings.common.items.tools.basetools.BaseSword;
import com.direwolf20.justdirethings.common.items.tools.utils.GooTier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class EclipseAlloySword extends BaseSword implements PoweredTool, GooTieredItem {
    public EclipseAlloySword() {
        super(new Item.Properties()
                .sword(GooTier.ECLIPSEALLOY.material(), 3, -2.0F)
                .fireResistant());
        registerAbility(Ability.GLOWING);
        registerAbility(Ability.CAUTERIZEWOUNDS, new AbilityParams(1, 1, 1, 1, 0, 1200));
        registerAbility(Ability.DROPTELEPORT);
        registerAbility(Ability.SMOKER);
    }

    @Override
    public GooTier getGooTier() {
        return GooTier.ECLIPSEALLOY;
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
