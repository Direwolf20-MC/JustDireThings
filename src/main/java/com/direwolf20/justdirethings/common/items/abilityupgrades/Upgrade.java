package com.direwolf20.justdirethings.common.items.abilityupgrades;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

import static com.direwolf20.justdirethings.util.TooltipHelpers.appendShiftForInfo;
import static com.direwolf20.justdirethings.util.TooltipHelpers.appendUpgradeDetails;

public class Upgrade extends Item {
    public Upgrade() {
        super(new Properties()
                .stacksTo(1));
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, context, tooltip, flagIn);
        Level level = context.level();
        if (level == null) {
            return;
        }

        boolean sneakPressed = Screen.hasShiftDown();
        if (sneakPressed) {
            appendUpgradeDetails(stack, tooltip);
        } else {
            appendShiftForInfo(stack, tooltip);
        }
    }
}
