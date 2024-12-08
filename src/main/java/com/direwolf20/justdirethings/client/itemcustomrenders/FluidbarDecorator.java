package com.direwolf20.justdirethings.client.itemcustomrenders;

import com.direwolf20.justdirethings.common.items.interfaces.FluidContainingItem;
import com.direwolf20.justdirethings.common.items.interfaces.PoweredItem;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.IItemDecorator;

public class FluidbarDecorator implements IItemDecorator {
    @Override
    public boolean render(GuiGraphics guiGraphics, Font font, ItemStack stack, int xOffset, int yOffset) {
        if (stack.getItem() instanceof FluidContainingItem fluidContainingItem) {
            // Retrieve energy capability
            boolean isPowerBarVisible = false;
            if (stack.getItem() instanceof PoweredItem poweredItem) {
                isPowerBarVisible = poweredItem.isPowerBarVisible(stack);
            }
            if (stack.isBarVisible())
                isPowerBarVisible = true;

            // Retrieve fluid capability
            boolean isFluidBarVisible = fluidContainingItem.isFluidBarVisible(stack);

            if (!isFluidBarVisible) {
                return false; // Do not render if fluid is at max capacity
            }

            // Calculate positions based on whether the power bar is visible
            int fluidBarY = isPowerBarVisible ? yOffset + 11 : yOffset + 13; // Adjust Y position based on power bar visibility
            int fluidBarWidth = fluidContainingItem.getFluidBarWidth(stack);
            int fluidBarColor = fluidContainingItem.getFluidBarColor(stack);

            // Render fluid bar
            renderBar(guiGraphics, xOffset + 2, fluidBarY, fluidBarWidth, fluidBarColor);

            return true;
        }
        return false;
    }

    private void renderBar(GuiGraphics guiGraphics, int x, int y, int width, int color) {
        // Render the background of the bar (black)
        guiGraphics.fill(RenderType.guiOverlay(), x, y, x + 13, y + 2, 0xFF303030);

        // Render the fluid bar with the calculated width and color
        guiGraphics.fill(RenderType.guiOverlay(), x, y, x + width, y + 1, color | 0xFF000000);
    }
}
