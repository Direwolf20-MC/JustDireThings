package com.direwolf20.justdirethings.common.items.abilityupgrades;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static com.direwolf20.justdirethings.util.TooltipHelpers.appendShiftForInfo;
import static com.direwolf20.justdirethings.util.TooltipHelpers.appendUpgradeDetails;

public class Upgrade extends Item {
    public Upgrade(Properties pProperties) {
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
        boolean sneakPressed = Minecraft.getInstance().hasShiftDown();
        if (sneakPressed) {
            appendUpgradeDetails(stack, buffer);
        } else {
            appendShiftForInfo(stack, buffer);
        }
        buffer.forEach(tooltip);
    }
}
