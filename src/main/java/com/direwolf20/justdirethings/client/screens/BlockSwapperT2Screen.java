package com.direwolf20.justdirethings.client.screens;

import com.direwolf20.justdirethings.client.screens.basescreens.BaseMachineScreen;
import com.direwolf20.justdirethings.client.screens.standardbuttons.ToggleButtonFactory;
import com.direwolf20.justdirethings.client.screens.widgets.ToggleButton;
import com.direwolf20.justdirethings.common.blockentities.BlockSwapperT1BE;
import com.direwolf20.justdirethings.common.containers.BlockSwapperT2Container;
import com.direwolf20.justdirethings.common.network.data.SwapperPayload;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;

public class BlockSwapperT2Screen extends BaseMachineScreen<BlockSwapperT2Container> {
    public GlobalPos boundTo;
    public BlockSwapperT1BE be;
    BlockSwapperT2Container container;
    public int swap_entity_type;
    public boolean swapBlocks;

    public BlockSwapperT2Screen(BlockSwapperT2Container container, Inventory inv, Component name) {
        super(container, inv, name);
        this.container = container;
        if (container.baseMachineBE instanceof BlockSwapperT1BE blockSwapper) {
            boundTo = blockSwapper.boundTo;
            be = blockSwapper;
            swap_entity_type = blockSwapper.swap_entity_type.ordinal();
            swapBlocks = blockSwapper.swapBlocks;
        }
    }

    @Override
    public void init() {
        super.init();
        addRenderableWidget(ToggleButtonFactory.SWAPPERENTITYBUTTON(getGuiLeft() + 26, topSectionTop + 44, swap_entity_type, b -> {
            ((ToggleButton) b).nextTexturePosition();
            swap_entity_type = ((ToggleButton) b).getTexturePosition();
            saveSettings();
        }));
        addRenderableWidget(ToggleButtonFactory.SWAPPERBLOCKBUTTON(getGuiLeft() + 8, topSectionTop + 44, swapBlocks ? 0 : 1, b -> {
            ((ToggleButton) b).nextTexturePosition();
            swapBlocks = ((ToggleButton) b).getTexturePosition() == 0;
            saveSettings();
        }));
    }

    @Override
    public void setTopSection() {
        extraWidth = 60;
        extraHeight = 0;
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderLabels(guiGraphics, mouseX, mouseY);
        boundTo = be.boundTo;
        if (boundTo != null) {
            ChatFormatting chatFormatting = container.getPartnerExists() == 1 ? ChatFormatting.BLUE : ChatFormatting.DARK_RED;
            guiGraphics.drawString(this.font, Component.translatable("justdirethings.boundto", Component.translatable(boundTo.dimension().location().getPath()), "[" + boundTo.pos().toShortString() + "]").withStyle(chatFormatting), 5, topSectionTop - getGuiTop() + 14, 4210752, false);
        }
    }

    @Override
    public void saveSettings() {
        super.saveSettings();
        PacketDistributor.SERVER.noArg().send(new SwapperPayload(swapBlocks, swap_entity_type));
    }
}
