package com.direwolf20.justdirethings.common.items.armors;

import com.direwolf20.justdirethings.common.items.armors.basearmors.BaseChestplate;
import com.direwolf20.justdirethings.common.items.armors.utils.ArmorTiers;
import com.direwolf20.justdirethings.common.items.interfaces.*;
import net.minecraft.world.item.ItemStack;

public class EclipseAlloyChestplate extends BaseChestplate implements PoweredTool {
    public EclipseAlloyChestplate() {
        super(ArmorTiers.ECLIPSEALLOY, new Properties()
                .fireResistant()
                .durability(Type.CHESTPLATE.getDurability(25)));
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
    public boolean canElytraFly(ItemStack stack, net.minecraft.world.entity.LivingEntity entity) {
        return isFlyEnabled(stack);
    }

    @Override
    public boolean elytraFlightTick(ItemStack stack, net.minecraft.world.entity.LivingEntity entity, int flightTicks) {
        if (!entity.level().isClientSide) {
            int nextFlightTick = flightTicks + 1;
            if (nextFlightTick % 10 == 0) {
                if (nextFlightTick % 20 == 0) {
                    Helpers.damageTool(stack, entity, Ability.ELYTRA);
                }
                entity.gameEvent(net.minecraft.world.level.gameevent.GameEvent.ELYTRA_GLIDE);
            }
        }
        return true;
    }

    @Override
    public int getMaxEnergy() {
        return 500000;
    }
}
