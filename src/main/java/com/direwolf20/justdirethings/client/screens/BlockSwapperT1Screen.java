package com.direwolf20.justdirethings.client.screens;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.client.screens.basescreens.BaseMachineScreen;
import com.direwolf20.justdirethings.client.screens.standardbuttons.ToggleButtonFactory;
import com.direwolf20.justdirethings.client.screens.widgets.ToggleButton;
import com.direwolf20.justdirethings.common.blockentities.BlockSwapperT1BE;
import com.direwolf20.justdirethings.common.containers.BlockSwapperT1Container;
import com.direwolf20.justdirethings.common.network.data.SwapperPayload;
import com.direwolf20.justdirethings.util.MiscTools;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.GlobalPos;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Arrays;

public class BlockSwapperT1Screen extends BaseMachineScreen<BlockSwapperT1Container> {
    protected final ResourceLocation ACTIVE = new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/active.png");
    protected final ResourceLocation INACTIVE = new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/inactive.png");
    public GlobalPos boundTo;
    public BlockSwapperT1BE be;
    BlockSwapperT1Container container;
    public int swap_entity_type;
    public boolean swapBlocks;
    public int activeX;
    public int activeY;

    public BlockSwapperT1Screen(BlockSwapperT1Container container, Inventory inv, Component name) {
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
        addRenderableWidget(ToggleButtonFactory.SWAPPERENTITYBUTTON(getGuiLeft() + 106, topSectionTop + 38, swap_entity_type, b -> {
            ((ToggleButton) b).nextTexturePosition();
            swap_entity_type = ((ToggleButton) b).getTexturePosition();
            saveSettings();
        }));
        addRenderableWidget(ToggleButtonFactory.SWAPPERBLOCKBUTTON(getGuiLeft() + 88, topSectionTop + 38, swapBlocks ? 0 : 1, b -> {
            ((ToggleButton) b).nextTexturePosition();
            swapBlocks = ((ToggleButton) b).getTexturePosition() == 0;
            saveSettings();
        }));

        activeX = topSectionLeft + 70;
        activeY = topSectionTop + 38;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        boundTo = be.boundTo;
        if (boundTo != null) {
            ResourceLocation icon = container.getPartnerExists() == 1 ? ACTIVE : INACTIVE;
            guiGraphics.blit(icon, activeX, activeY, 0, 0, 16, 16, 16, 16);
        } else {
            guiGraphics.blit(INACTIVE, activeX, activeY, 0, 0, 16, 16, 16, 16);
        }
    }

    @Override
    public void setTopSection() {
        extraWidth = 0;
        extraHeight = 0;
    }

    @Override
    public void addRedstoneButtons() {
        addRenderableWidget(ToggleButtonFactory.REDSTONEBUTTON(getGuiLeft() + 124, topSectionTop + 38, redstoneMode.ordinal(), b -> {
            redstoneMode = redstoneMode.next();
            ((ToggleButton) b).nextTexturePosition();
            saveSettings();
        }));
    }

    @Override
    protected void renderTooltip(GuiGraphics pGuiGraphics, int pX, int pY) {
        super.renderTooltip(pGuiGraphics, pX, pY);
        if (MiscTools.inBounds(activeX, activeY, 16, 16, pX, pY)) {
            boundTo = be.boundTo;
            if (boundTo != null) {
                ChatFormatting chatFormatting = container.getPartnerExists() == 1 ? ChatFormatting.BLUE : ChatFormatting.DARK_RED;
                String key = container.getPartnerExists() == 1 ? "justdirethings.boundto" : "justdirethings.boundto-missing";
                pGuiGraphics.renderTooltip(this.font, Language.getInstance().getVisualOrder(Arrays.asList(
                        Component.translatable(key, Component.translatable(boundTo.dimension().location().getPath()), "[" + boundTo.pos().toShortString() + "]").withStyle(chatFormatting)
                )), pX, pY);
            } else {
                ChatFormatting chatFormatting = ChatFormatting.DARK_RED;
                pGuiGraphics.renderTooltip(this.font, Language.getInstance().getVisualOrder(Arrays.asList(
                        Component.translatable("justdirethings.unbound-screen").withStyle(chatFormatting)
                )), pX, pY);
            }
        }
    }

    @Override
    public void saveSettings() {
        super.saveSettings();
        PacketDistributor.SERVER.noArg().send(new SwapperPayload(swapBlocks, swap_entity_type));
    }
}
