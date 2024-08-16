package com.direwolf20.justdirethings.common.items;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

import static com.direwolf20.justdirethings.util.TooltipHelpers.appendShiftForInfo;

public class PolymorphicCatalyst extends Item {
    public PolymorphicCatalyst() {
        super(new Properties());
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, context, tooltip, flagIn);
        Level level = context.level();
        if (level == null) {
            return;
        }
        if (Screen.hasShiftDown())
            tooltip.add(Component.translatable("justdirethings.hint.dropinwater").withStyle(ChatFormatting.LIGHT_PURPLE));
        else
            appendShiftForInfo(stack, tooltip);
    }
}
