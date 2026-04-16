package com.direwolf20.justdirethings.client.screens;

import com.direwolf20.justdirethings.client.KeyBindings;
import com.direwolf20.justdirethings.common.items.PortalGunV2;
import com.direwolf20.justdirethings.common.network.data.PortalGunFavoriteChangePayload;
import com.direwolf20.justdirethings.util.MiscHelpers;
import com.direwolf20.justdirethings.util.NBTHelpers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.gui.widget.ExtendedButton;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

import java.util.function.Predicate;

public class AdvPortalEditMenu extends Screen {
    private int slotSelected = 0;
    private int ticksOpened = 0;
    private ItemStack portalGun;
    private EditBox nameField;
    private EditBox xPos;
    private EditBox yPos;
    private EditBox zPos;
    private final Predicate<String> doubleInputValidator = this::isValidDoubleInput;

    protected AdvPortalEditMenu(ItemStack itemStack, int favoritePosition) {
        super(Component.literal(""));
        this.portalGun = itemStack;
        this.slotSelected = favoritePosition;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        // Suppress default background.
    }

    @Override
    public void init() {
        super.init();
        this.nameField = new EditBox(this.font, width / 2 - 75, height / 2 - 34, 200, this.font.lineHeight + 3, Component.translatable("buildinggadgets2.screen.namefieldtext"));
        this.xPos = new EditBox(this.font, width / 2 - 75, height / 2 - 20, 60, this.font.lineHeight + 3, Component.translatable("buildinggadgets2.screen.namefieldtext"));
        this.yPos = new EditBox(this.font, width / 2 - 5, height / 2 - 20, 60, this.font.lineHeight + 3, Component.translatable("buildinggadgets2.screen.namefieldtext"));
        this.zPos = new EditBox(this.font, width / 2 + 65, height / 2 - 20, 60, this.font.lineHeight + 3, Component.translatable("buildinggadgets2.screen.namefieldtext"));
        updateNameField();

        ExtendedButton buttonSave = new ExtendedButton(width / 2 - 75, height / 2, 120, 16, Component.translatable("justdirethings.screen.save_close"), (button) -> {
            addFavorite();
        });
        addRenderableWidget(buttonSave);

        ExtendedButton buttonCancel = new ExtendedButton(width / 2 + 60, height / 2, 65, 16, Component.translatable("justdirethings.screen.cancel"), (button) -> {
            onClose();
        });
        addRenderableWidget(buttonCancel);

        this.nameField.setMaxLength(15);
        this.nameField.setVisible(true);
        addRenderableWidget(nameField);
        this.xPos.setVisible(true);
        this.xPos.setFilter(doubleInputValidator);
        this.xPos.setEditable(false);
        addRenderableWidget(xPos);
        this.yPos.setVisible(true);
        this.yPos.setFilter(doubleInputValidator);
        this.yPos.setEditable(false);
        addRenderableWidget(yPos);
        this.zPos.setVisible(true);
        this.zPos.setFilter(doubleInputValidator);
        this.zPos.setEditable(false);
        addRenderableWidget(zPos);
    }

    @Override
    protected void setInitialFocus() {
        this.setInitialFocus(this.nameField);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        int keyCode = event.key();
        if (ticksOpened < 400 && keyCode == KeyBindings.toggleTool.getKey().getValue()) return true;
        if (keyCode == 256) {
            onClose();
            return true;
        }
        return super.keyPressed(event);
    }

    @Override
    public void tick() {
        ticksOpened++;
    }

    @Override
    public boolean charTyped(CharacterEvent event) {
        String toggleToolKeybind = KeyBindings.toggleTool.getKey().getName();
        if (ticksOpened < 20 && toggleToolKeybind.charAt(toggleToolKeybind.length() - 1) == event.codepoint())
            return false;
        return this.getFocused() != null && this.getFocused().charTyped(event);
    }

    public void addFavorite() {
        try {
            NBTHelpers.PortalDestination portalDestination = PortalGunV2.getFavorite(portalGun, slotSelected);
            if (portalDestination == null || portalDestination.equals(NBTHelpers.PortalDestination.EMPTY)) {
                Player player = Minecraft.getInstance().player;
                Vec3 position = player.position();
                Direction facing = MiscHelpers.getFacingDirection(player);
                portalDestination = new NBTHelpers.PortalDestination(new NBTHelpers.GlobalVec3(player.level().dimension(), position), facing, "UNNAMED");
            }
            Vec3 coords = portalDestination.globalVec3().position();
            ClientPacketDistributor.sendToServer(new PortalGunFavoriteChangePayload(slotSelected, true, nameField.getValue(), true, coords));
            this.onClose();
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid format for the validFormattedX string");
        }
    }

    public void updateNameField() {
        NBTHelpers.PortalDestination portalDestination = PortalGunV2.getFavorite(portalGun, slotSelected);
        Player player = Minecraft.getInstance().player;
        if (portalDestination == null || portalDestination.equals(NBTHelpers.PortalDestination.EMPTY)) {
            Vec3 position = player.position();
            Direction facing = MiscHelpers.getFacingDirection(player);
            portalDestination = new NBTHelpers.PortalDestination(new NBTHelpers.GlobalVec3(player.level().dimension(), position), facing, "UNNAMED");
        }
        String favoriteName = portalDestination.name();
        this.nameField.setValue(favoriteName);
        Vec3 coords = portalDestination.globalVec3().position();
        this.xPos.setValue(String.format("%.2f", coords.x));
        this.yPos.setValue(String.format("%.2f", coords.y));
        this.zPos.setValue(String.format("%.2f", coords.z));
    }


    private boolean isValidDoubleInput(String input) {
        if (input.isEmpty()) return true;
        try {
            Double.parseDouble(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
