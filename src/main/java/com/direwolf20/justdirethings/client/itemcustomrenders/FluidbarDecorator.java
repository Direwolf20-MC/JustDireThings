package com.direwolf20.justdirethings.client.itemcustomrenders;

import com.direwolf20.justdirethings.common.items.interfaces.FluidContainingItem;
import com.direwolf20.justdirethings.common.items.interfaces.PoweredItem;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.IItemDecorator;

public class FluidbarDecorator implements IItemDecorator {
    @Override
    public boolean render(GuiGraphicsExtractor guiGraphics, Font font, ItemStack stack, int xOffset, int yOffset) {
        if (stack.getItem() instanceof FluidContainingItem fluidContainingItem) {
            boolean isPowerBarVisible = false;
            if (stack.getItem() instanceof PoweredItem poweredItem) {
                isPowerBarVisible = poweredItem.isPowerBarVisible(stack);
            }
            if (stack.isBarVisible())
                isPowerBarVisible = true;

            boolean isFluidBarVisible = fluidContainingItem.isFluidBarVisible(stack);

            if (!isFluidBarVisible) {
                return false;
            }

            int fluidBarY = isPowerBarVisible ? yOffset + 11 : yOffset + 13;
            int fluidBarWidth = fluidContainingItem.getFluidBarWidth(stack);
            int fluidBarColor = fluidContainingItem.getFluidBarColor(stack);

            renderBar(guiGraphics, xOffset + 2, fluidBarY, fluidBarWidth, fluidBarColor);

            return true;
        }
        return false;
    }

    private void renderBar(GuiGraphicsExtractor guiGraphics, int x, int y, int width, int color) {
        guiGraphics.fill(x, y, x + 13, y + 2, 0xFF303030);
        guiGraphics.fill(x, y, x + width, y + 1, color | 0xFF000000);
    }
}
