package com.direwolf20.justdirethings.client.screens;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.client.screens.basescreens.BaseMachineScreen;
import com.direwolf20.justdirethings.client.screens.standardbuttons.ToggleButtonFactory;
import com.direwolf20.justdirethings.client.screens.widgets.ToggleButton;
import com.direwolf20.justdirethings.common.blockentities.BlockSwapperT1BE;
import com.direwolf20.justdirethings.common.containers.BlockSwapperT1Container;
import com.direwolf20.justdirethings.common.network.data.SwapperPayload;
import com.direwolf20.justdirethings.util.MiscHelpers;
import com.direwolf20.justdirethings.util.MiscTools;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

public class BlockSwapperT1Screen extends BaseMachineScreen<BlockSwapperT1Container> {
    protected final Identifier ACTIVE = Identifier.fromNamespaceAndPath(JustDireThings.MODID, "textures/gui/buttons/active.png");
    protected final Identifier INACTIVE = Identifier.fromNamespaceAndPath(JustDireThings.MODID, "textures/gui/buttons/inactive.png");
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
        addRenderableWidget(ToggleButtonFactory.SWAPPERENTITYBUTTON(leftPos + 106, topSectionTop + 38, swap_entity_type, b -> {
            swap_entity_type = ((ToggleButton) b).getTexturePosition();
            saveSettings();
        }));
        addRenderableWidget(ToggleButtonFactory.SWAPPERBLOCKBUTTON(leftPos + 88, topSectionTop + 38, swapBlocks ? 0 : 1, b -> {
            swapBlocks = ((ToggleButton) b).getTexturePosition() == 0;
            saveSettings();
        }));

        activeX = topSectionLeft + 70;
        activeY = topSectionTop + 38;
    }

    @Override
    public void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        super.extractContents(graphics, mouseX, mouseY, partialTicks);
        boundTo = be.boundTo;
        Identifier icon = (boundTo != null && container.getPartnerExists() == 1) ? ACTIVE : INACTIVE;
        graphics.blit(RenderPipelines.GUI_TEXTURED, icon, activeX, activeY, 0.0F, 0.0F, 16, 16, 16, 16);
    }

    @Override
    public void setTopSection() {
        extraWidth = 0;
        extraHeight = 0;
    }

    @Override
    public void addRedstoneButtons() {
        addRenderableWidget(ToggleButtonFactory.REDSTONEBUTTON(leftPos + 124, topSectionTop + 38, redstoneMode.ordinal(), b -> {
            redstoneMode = MiscHelpers.RedstoneMode.values()[((ToggleButton) b).getTexturePosition()];
            saveSettings();
        }));
    }

    @Override
    protected void extractTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        super.extractTooltip(graphics, mouseX, mouseY);
        if (MiscTools.inBounds(activeX, activeY, 16, 16, mouseX, mouseY)) {
            boundTo = be.boundTo;
            if (boundTo != null) {
                ChatFormatting chatFormatting = container.getPartnerExists() == 1 ? ChatFormatting.BLUE : ChatFormatting.DARK_RED;
                String key = container.getPartnerExists() == 1 ? "justdirethings.boundto" : "justdirethings.boundto-missing";
                graphics.setTooltipForNextFrame(this.font,
                        Component.translatable(key, Component.translatable(boundTo.dimension().location().getPath()), "[" + boundTo.pos().toShortString() + "]").withStyle(chatFormatting),
                        mouseX, mouseY);
            } else {
                graphics.setTooltipForNextFrame(this.font,
                        Component.translatable("justdirethings.unbound-screen").withStyle(ChatFormatting.DARK_RED),
                        mouseX, mouseY);
            }
        }
    }

    @Override
    public void saveSettings() {
        super.saveSettings();
        ClientPacketDistributor.sendToServer(new SwapperPayload(swapBlocks, swap_entity_type));
    }
}
