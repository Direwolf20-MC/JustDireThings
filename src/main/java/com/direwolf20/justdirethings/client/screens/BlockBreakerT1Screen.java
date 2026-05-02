package com.direwolf20.justdirethings.client.screens;

import com.direwolf20.justdirethings.client.screens.basescreens.BaseMachineScreen;
import com.direwolf20.justdirethings.client.screens.standardbuttons.ToggleButtonFactory;
import com.direwolf20.justdirethings.client.screens.widgets.GrayscaleButton;
import com.direwolf20.justdirethings.client.screens.widgets.ToggleButton;
import com.direwolf20.justdirethings.common.blockentities.BlockBreakerT1BE;
import com.direwolf20.justdirethings.common.containers.BlockBreakerT1Container;
import com.direwolf20.justdirethings.common.network.data.BreakerPayload;
import com.direwolf20.justdirethings.util.MiscHelpers;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

public class BlockBreakerT1Screen extends BaseMachineScreen<BlockBreakerT1Container> {
    public boolean sneaking;

    public BlockBreakerT1Screen(BlockBreakerT1Container container, Inventory inv, Component name) {
        super(container, inv, name);
        if (baseMachineBE instanceof BlockBreakerT1BE breaker) {
            sneaking = breaker.sneaking;
        }
    }

    @Override
    public void init() {
        super.init();

        addRenderableWidget(ToggleButtonFactory.SNEAKCLICKBUTTON(leftPos + 56, topSectionTop + 38, sneaking, b -> {
            sneaking = !sneaking;
            ((GrayscaleButton) b).toggleActive();
            saveSettings();
        }));
    }

    @Override
    public void setTopSection() {
        extraWidth = 0;
        extraHeight = 0;
    }

    @Override
    public void addRedstoneButtons() {
        addRenderableWidget(ToggleButtonFactory.REDSTONEBUTTON(leftPos + 104, topSectionTop + 38, redstoneMode.ordinal(), b -> {
            redstoneMode = MiscHelpers.RedstoneMode.values()[((ToggleButton) b).getTexturePosition()];
            saveSettings();
        }));
    }

    @Override
    protected void drawMachineSlot(GuiGraphicsExtractor graphics, Slot slot) {
        ItemStack itemStack = slot.getItem();
        if (itemStack.isEmpty())
            graphics.blit(RenderPipelines.GUI_TEXTURED, JUSTSLOT, leftPos + slot.x - 1, topPos + slot.y - 1, 18.0F, 0.0F, 18, 18, 256, 256);
        else
            super.drawMachineSlot(graphics, slot);
    }

    @Override
    public void saveSettings() {
        super.saveSettings();
        ClientPacketDistributor.sendToServer(new BreakerPayload(sneaking));
    }
}
