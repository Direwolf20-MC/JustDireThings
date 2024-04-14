package com.direwolf20.justdirethings.client.screens;

import com.direwolf20.justdirethings.client.screens.basescreens.BaseMachineScreen;
import com.direwolf20.justdirethings.client.screens.standardbuttons.ToggleButtonFactory;
import com.direwolf20.justdirethings.client.screens.widgets.ToggleButton;
import com.direwolf20.justdirethings.common.blockentities.PlayerAccessorBE;
import com.direwolf20.justdirethings.common.containers.PlayerAccessorContainer;
import com.direwolf20.justdirethings.common.network.data.PlayerAccessorPayload;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.HashMap;

public class PlayerAccessorScreen extends BaseMachineScreen<PlayerAccessorContainer> {
    PlayerAccessorBE playerAccessorBE;
    public HashMap<Direction, Integer> sidedInventoryTypes;
    public PlayerAccessorScreen(PlayerAccessorContainer container, Inventory inv, Component name) {
        super(container, inv, name);
        if (container.baseMachineBE instanceof PlayerAccessorBE playerAccessorBE) {
            this.playerAccessorBE = playerAccessorBE;
            sidedInventoryTypes = playerAccessorBE.sidedInventoryTypes;
        }
    }

    @Override
    public void addTickSpeedButton() {
        //No-Op
    }

    @Override
    public void init() {
        super.init();
        addRenderableWidget(ToggleButtonFactory.INVENTORYCONNECTIONBUTTON(getGuiLeft() + 80, topSectionTop + 22, Component.translatable("justdirethings.screen.direction-up"), sidedInventoryTypes.get(Direction.UP), b -> {
            ((ToggleButton) b).nextTexturePosition();
            sidedInventoryTypes.put(Direction.UP, ((ToggleButton) b).getTexturePosition());
            PacketDistributor.SERVER.noArg().send(new PlayerAccessorPayload(Direction.UP.ordinal(), sidedInventoryTypes.get(Direction.UP)));
        }));
        addRenderableWidget(ToggleButtonFactory.INVENTORYCONNECTIONBUTTON(getGuiLeft() + 80, topSectionTop + 40, Component.translatable("justdirethings.screen.direction-north"), sidedInventoryTypes.get(Direction.NORTH), b -> {
            ((ToggleButton) b).nextTexturePosition();
            sidedInventoryTypes.put(Direction.NORTH, ((ToggleButton) b).getTexturePosition());
            PacketDistributor.SERVER.noArg().send(new PlayerAccessorPayload(Direction.NORTH.ordinal(), sidedInventoryTypes.get(Direction.NORTH)));
        }));
        addRenderableWidget(ToggleButtonFactory.INVENTORYCONNECTIONBUTTON(getGuiLeft() + 62, topSectionTop + 40, Component.translatable("justdirethings.screen.direction-west"), sidedInventoryTypes.get(Direction.WEST), b -> {
            ((ToggleButton) b).nextTexturePosition();
            sidedInventoryTypes.put(Direction.WEST, ((ToggleButton) b).getTexturePosition());
            PacketDistributor.SERVER.noArg().send(new PlayerAccessorPayload(Direction.WEST.ordinal(), sidedInventoryTypes.get(Direction.WEST)));
        }));
        addRenderableWidget(ToggleButtonFactory.INVENTORYCONNECTIONBUTTON(getGuiLeft() + 98, topSectionTop + 40, Component.translatable("justdirethings.screen.direction-east"), sidedInventoryTypes.get(Direction.EAST), b -> {
            ((ToggleButton) b).nextTexturePosition();
            sidedInventoryTypes.put(Direction.EAST, ((ToggleButton) b).getTexturePosition());
            PacketDistributor.SERVER.noArg().send(new PlayerAccessorPayload(Direction.EAST.ordinal(), sidedInventoryTypes.get(Direction.EAST)));
        }));
        addRenderableWidget(ToggleButtonFactory.INVENTORYCONNECTIONBUTTON(getGuiLeft() + 80, topSectionTop + 58, Component.translatable("justdirethings.screen.direction-down"), sidedInventoryTypes.get(Direction.DOWN), b -> {
            ((ToggleButton) b).nextTexturePosition();
            sidedInventoryTypes.put(Direction.DOWN, ((ToggleButton) b).getTexturePosition());
            PacketDistributor.SERVER.noArg().send(new PlayerAccessorPayload(Direction.DOWN.ordinal(), sidedInventoryTypes.get(Direction.DOWN)));
        }));
        addRenderableWidget(ToggleButtonFactory.INVENTORYCONNECTIONBUTTON(getGuiLeft() + 62, topSectionTop + 58, Component.translatable("justdirethings.screen.direction-south"), sidedInventoryTypes.get(Direction.SOUTH), b -> {
            ((ToggleButton) b).nextTexturePosition();
            sidedInventoryTypes.put(Direction.SOUTH, ((ToggleButton) b).getTexturePosition());
            PacketDistributor.SERVER.noArg().send(new PlayerAccessorPayload(Direction.SOUTH.ordinal(), sidedInventoryTypes.get(Direction.SOUTH)));
        }));
    }

    @Override
    public void setTopSection() {
        extraWidth = 0;
        extraHeight = 0;
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        Component title = playerAccessorBE.getBlockState().getBlock().getName();
        int titleX = topSectionLeft - getGuiLeft() + 20 + ((topSectionWidth - 40) / 2) - this.font.width(title) / 2;
        guiGraphics.drawString(this.font, title, titleX, topSectionTop - getGuiTop() - 14, 4210752, false);
    }

    @Override
    public void saveSettings() {
        super.saveSettings();

    }
}
