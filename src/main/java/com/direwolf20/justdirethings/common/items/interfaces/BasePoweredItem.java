package com.direwolf20.justdirethings.common.items.interfaces;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static com.direwolf20.justdirethings.util.TooltipHelpers.appendFEText;

public abstract class BasePoweredItem extends Item {
    public BasePoweredItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, context, display, tooltip, flagIn);
        Level level = context.level();
        if (level == null) {
            return;
        }

        List<Component> buffer = new ArrayList<>();
        appendFEText(stack, buffer);
        buffer.forEach(tooltip);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        if (stack.getItem() instanceof PoweredItem poweredItem) {
            return poweredItem.isPowerBarVisible(stack);
        }
        return super.isBarVisible(stack);
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        if (stack.getItem() instanceof PoweredItem poweredItem) {
            return poweredItem.getPowerBarWidth(stack);
        }
        return super.getBarWidth(stack);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        if (stack.getItem() instanceof PoweredItem poweredItem) {
            int color = poweredItem.getPowerBarColor(stack);
            if (color == -1)
                return super.getBarColor(stack);
            return color;
        }
        return super.getBarColor(stack);
    }

}
