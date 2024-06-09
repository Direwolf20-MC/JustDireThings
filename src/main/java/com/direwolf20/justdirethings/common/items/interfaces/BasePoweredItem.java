package com.direwolf20.justdirethings.common.items.interfaces;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

import static com.direwolf20.justdirethings.util.TooltipHelpers.appendFEText;

public abstract class BasePoweredItem extends Item {
    public BasePoweredItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, context, tooltip, flagIn);
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) {
            return;
        }

        appendFEText(stack, tooltip);
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
