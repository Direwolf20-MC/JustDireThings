package com.direwolf20.justdirethings.common.items.armors;

import com.direwolf20.justdirethings.common.items.armors.basearmors.BaseChestplate;
import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.common.items.interfaces.AbilityParams;
import com.direwolf20.justdirethings.common.items.interfaces.PoweredTool;
import com.direwolf20.justdirethings.common.items.interfaces.ToggleableTool;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class CelestigemChestplate extends BaseChestplate implements PoweredTool {
    public CelestigemChestplate(Item.Properties pProperties) {
        super(pProperties);
        registerAbility(Ability.INVULNERABILITY, new AbilityParams(1, 1, 1, 1, 200, 600));
        registerAbility(Ability.EXTINGUISH, new AbilityParams(1, 1, 1, 1, 0, 100));
        registerAbility(Ability.ELYTRA);
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
}
