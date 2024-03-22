package com.direwolf20.justdirethings.client.screens;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.client.screens.basescreens.AreaAffectingScreen;
import com.direwolf20.justdirethings.client.screens.standardbuttons.ToggleButtonFactory;
import com.direwolf20.justdirethings.client.screens.widgets.GrayscaleButton;
import com.direwolf20.justdirethings.client.screens.widgets.ToggleButton;
import com.direwolf20.justdirethings.common.blockentities.ItemCollectorBE;
import com.direwolf20.justdirethings.common.containers.ItemCollectorContainer;
import com.direwolf20.justdirethings.common.containers.slots.FilterBasicSlot;
import com.direwolf20.justdirethings.common.network.data.FilterSettingPayload;
import com.direwolf20.justdirethings.common.network.data.GhostSlotPayload;
import com.direwolf20.justdirethings.common.network.data.RedstoneSettingPayload;
import com.direwolf20.justdirethings.util.MiscHelpers;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

public class ItemCollectorScreen extends AreaAffectingScreen<ItemCollectorContainer> {
    private final ResourceLocation GUI = new ResourceLocation(JustDireThings.MODID, "textures/gui/itemcollector.png");
    protected boolean allowlist = false;
    protected boolean compareNBT = false;
    protected MiscHelpers.RedstoneMode redstoneMode;

    public ItemCollectorScreen(ItemCollectorContainer container, Inventory inv, Component name) {
        super(container, inv, name);
        if (areaAffectingBE instanceof ItemCollectorBE itemCollectorBE) {
            this.allowlist = itemCollectorBE.getFilterData().allowlist;
            this.compareNBT = itemCollectorBE.getFilterData().compareNBT;
            this.redstoneMode = itemCollectorBE.getRedstoneControlData().redstoneMode;
        }
    }

    @Override
    public void init() {
        super.init();

        addRenderableWidget(ToggleButtonFactory.REDSTONEBUTTON(getGuiLeft() + 134, getGuiTop() + 45, redstoneMode.ordinal(), b -> {
            redstoneMode = redstoneMode.next();
            ((ToggleButton) b).nextTexturePosition();
            saveSettings();
        }));

        addRenderableWidget(ToggleButtonFactory.ALLOWLISTBUTTON(getGuiLeft() + 8, getGuiTop() + 45, allowlist, b -> {
            allowlist = !allowlist;
            ((GrayscaleButton) b).toggleActive();
            saveSettings();
        }));

        addRenderableWidget(ToggleButtonFactory.COMPARENBTBUTTON(getGuiLeft() + 26, getGuiTop() + 45, compareNBT, b -> {
            compareNBT = !compareNBT;
            ((GrayscaleButton) b).toggleActive();
            saveSettings();
        }));
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, GUI);
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(GUI, relX, relY, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public boolean mouseClicked(double x, double y, int btn) {
        if (hoveredSlot == null || !(hoveredSlot instanceof FilterBasicSlot))
            return super.mouseClicked(x, y, btn);

        // By splitting the stack we can get air easily :) perfect removal basically
        ItemStack stack = this.menu.getCarried();// getMinecraft().player.inventoryMenu.getCarried();
        stack = stack.copy().split(hoveredSlot.getMaxStackSize()); // Limit to slot limit
        hoveredSlot.set(stack); // Temporarily update the client for continuity purposes
        PacketDistributor.SERVER.noArg().send(new GhostSlotPayload(hoveredSlot.index, stack, stack.getCount(), -1));

        return true;
    }

    @Override
    public void saveSettings() {
        super.saveSettings();
        PacketDistributor.SERVER.noArg().send(new FilterSettingPayload(allowlist, compareNBT));
        PacketDistributor.SERVER.noArg().send(new RedstoneSettingPayload(redstoneMode.ordinal()));
    }

}
