package com.direwolf20.justdirethings.client.screens;

import com.direwolf20.justdirethings.client.KeyBindings;
import com.direwolf20.justdirethings.common.items.PortalGunV2;
import com.direwolf20.justdirethings.common.network.data.PortalGunFavoriteChangePayload;
import com.direwolf20.justdirethings.util.MiscHelpers;
import com.direwolf20.justdirethings.util.NBTHelpers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.gui.widget.ExtendedButton;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.function.Predicate;

public class AdvPortalEditMenu extends Screen {
    private int slotSelected = 0;
    private int ticksOpened = 0;
    private ItemStack portalGun;
    private EditBox nameField;
    private EditBox xPos;
    private EditBox yPos;
    private EditBox zPos;
    // Define the predicate as a class-level variable
    private final Predicate<String> doubleInputValidator = this::isValidDoubleInput;

    protected AdvPortalEditMenu(ItemStack itemStack, int favoritePosition) {
        super(Component.literal(""));
        this.portalGun = itemStack;
        this.slotSelected = favoritePosition;
    }

    @Override
    public void renderBackground(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
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
    public void render(GuiGraphics guiGraphics, int mx, int my, float partialTicks) {
        super.render(guiGraphics, mx, my, partialTicks);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {
        if (ticksOpened < 400 && p_keyPressed_1_ == KeyBindings.toggleTool.getKey().getValue()) return true;
        if (p_keyPressed_1_ == 256) {
            onClose();
            return true;
        }

        return super.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
    }

    @Override
    public void tick() {
        ticksOpened++;
    }

    @Override
    public boolean charTyped(char pCodePoint, int pModifiers) {
        String toggleToolKeybind = KeyBindings.toggleTool.getKey().getName();
        if (ticksOpened < 20 && toggleToolKeybind.charAt(toggleToolKeybind.length() - 1) == pCodePoint)
            return false;
        return this.getFocused() != null && this.getFocused().charTyped(pCodePoint, pModifiers);
    }

    public void addFavorite() {
        // Convert the valid string to a double
        try {
            //TODO Bring this back when/if I allow editing the Positions. Should fix bug #71
            //double xValue = Double.parseDouble(xPos.getValue());
            //double yValue = Double.parseDouble(yPos.getValue());
            //double zValue = Double.parseDouble(zPos.getValue());
            NBTHelpers.PortalDestination portalDestination = PortalGunV2.getFavorite(portalGun, slotSelected);
            if (portalDestination == null || portalDestination.equals(NBTHelpers.PortalDestination.EMPTY)) {
                Player player = Minecraft.getInstance().player;
                Vec3 position = player.position();
                Direction facing = MiscHelpers.getFacingDirection(player);
                portalDestination = new NBTHelpers.PortalDestination(new NBTHelpers.GlobalVec3(player.level().dimension(), position), facing, "UNNAMED");
            }
            Vec3 coords = portalDestination.globalVec3().position();
            PacketDistributor.sendToServer(new PortalGunFavoriteChangePayload(slotSelected, true, nameField.getValue(), true, coords));
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
        if (input.isEmpty()) return true; // Allow empty input
        try {
            // Check if the input can be parsed to a double
            Double.parseDouble(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
