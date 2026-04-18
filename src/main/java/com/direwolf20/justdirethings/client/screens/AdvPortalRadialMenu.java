/**
 * This class was adapted from code written by Vazkii for the PSI mod: https://github.com/Vazkii/Psi
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 */
package com.direwolf20.justdirethings.client.screens;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.client.KeyBindings;
import com.direwolf20.justdirethings.client.OurSounds;
import com.direwolf20.justdirethings.client.screens.standardbuttons.ToggleButtonFactory;
import com.direwolf20.justdirethings.client.screens.widgets.BaseButton;
import com.direwolf20.justdirethings.client.screens.widgets.GrayscaleButton;
import com.direwolf20.justdirethings.common.items.PortalGunV2;
import com.direwolf20.justdirethings.common.network.data.PortalGunFavoriteChangePayload;
import com.direwolf20.justdirethings.common.network.data.PortalGunFavoritePayload;
import com.direwolf20.justdirethings.setup.JDTRegistration;
import com.direwolf20.justdirethings.util.NBTHelpers;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import org.joml.Matrix3x2fStack;

public class AdvPortalRadialMenu extends Screen {
    private static final int WHITE_ARGB = 0xFFFFFFFF;
    private static final int LIGHT_GRAY_ARGB = 0xFFC0C0C0;

    ToggleButtonFactory.TextureLocalization ADD_BUTTON = new ToggleButtonFactory.TextureLocalization(Identifier.fromNamespaceAndPath(JustDireThings.MODID, "textures/gui/buttons/add.png"), Component.translatable("justdirethings.screen.add_favorite"));
    ToggleButtonFactory.TextureLocalization REMOVE_BUTTON = new ToggleButtonFactory.TextureLocalization(Identifier.fromNamespaceAndPath(JustDireThings.MODID, "textures/gui/buttons/remove.png"), Component.translatable("justdirethings.screen.remove_favorite"));
    ToggleButtonFactory.TextureLocalization EDIT_BUTTON = new ToggleButtonFactory.TextureLocalization(Identifier.fromNamespaceAndPath(JustDireThings.MODID, "textures/gui/buttons/matchnbttrue.png"), Component.translatable("justdirethings.screen.edit_favorite"));
    ToggleButtonFactory.TextureLocalization STAYOPEN_BUTTON = new ToggleButtonFactory.TextureLocalization(Identifier.fromNamespaceAndPath(JustDireThings.MODID, "textures/gui/buttons/area.png"), Component.translatable("justdirethings.screen.stay_open"));
    private static final int SEGMENTS = PortalGunV2.MAX_FAVORITES;

    private int timeIn = 0;
    private int slotHovered = -1;
    private int slotSelected = 0;
    private ItemStack portalGun;
    private final static int radiusMin = 26;
    private final static int radiusMax = 120;
    private boolean staysOpen = false;

    public AdvPortalRadialMenu(ItemStack stack) {
        super(Component.literal(""));
        portalGun = stack;
        slotSelected = PortalGunV2.getFavoritePosition(portalGun);
        this.staysOpen = PortalGunV2.getStayOpen(portalGun);
    }

    private static float mouseAngle(int x, int y, int mx, int my) {
        Vector2f baseVec = new Vector2f(1F, 0F);
        Vector2f mouseVec = new Vector2f(mx - x, my - y);

        float ang = (float) (Math.acos(baseVec.dot(mouseVec) / (baseVec.length() * mouseVec.length())) * (180F / Math.PI));
        return my < y ? 360F - ang : ang;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        // Suppress default background.
    }

    @Override
    public void init() {
        GrayscaleButton addButton = new GrayscaleButton(width / 2 - 150, height / 2 - 20, 16, 16, ADD_BUTTON.texture(), ADD_BUTTON.localization(), true, (clicked) -> {
            addFavorite();
        });
        addRenderableWidget(addButton);

        GrayscaleButton removeButton = new GrayscaleButton(width / 2 + 140, height / 2 - 20, 16, 16, REMOVE_BUTTON.texture(), REMOVE_BUTTON.localization(), true, (clicked) -> {
            removeFavorite();
        });
        addRenderableWidget(removeButton);

        GrayscaleButton editButton = new GrayscaleButton(width / 2 - 150, height / 2 + 20, 16, 16, EDIT_BUTTON.texture(), EDIT_BUTTON.localization(), true, (clicked) -> {
            editFavorite();
        });
        addRenderableWidget(editButton);

        GrayscaleButton stayOpenButton = new GrayscaleButton(width / 2 + 140, height / 2 + 20, 16, 16, STAYOPEN_BUTTON.texture(), STAYOPEN_BUTTON.localization(), staysOpen, (clicked) -> {
            staysOpen = !staysOpen;
            saveFavorite();
            ((GrayscaleButton) clicked).toggleActive();
        });
        addRenderableWidget(stayOpenButton);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        portalGun = PortalGunV2.getPortalGunv2(Minecraft.getInstance().player);
        super.extractRenderState(graphics, mouseX, mouseY, partialTicks);

        boolean inRange = isInRange(mouseX, mouseY);
        float speedOfButtonGrowth = 5f;
        float fract = Math.min(speedOfButtonGrowth, this.timeIn + partialTicks) / speedOfButtonGrowth;
        int x = this.width / 2;
        int y = this.height / 2;

        float angle = mouseAngle(x, y, mouseX, mouseY);
        float totalDeg = 0;
        float degPer = 360F / SEGMENTS;
        for (int seg = 0; seg < SEGMENTS; seg++) {
            if (this.isCursorInSlice(angle, totalDeg, degPer, inRange)) {
                this.slotHovered = seg;
            }
            totalDeg += degPer;
        }

        totalDeg = 0;
        float delayBetweenSegments = 1f;
        float speedOfSegmentGrowth = 25f;
        float innerRatio = 1F / 2.3F;
        for (int seg = 0; seg < SEGMENTS; seg++) {
            boolean mouseInSector = this.isCursorInSlice(angle, totalDeg, degPer, inRange);
            float radius = Math.max(0F, Math.min((this.timeIn + partialTicks - seg * delayBetweenSegments / SEGMENTS) * speedOfSegmentGrowth, radiusMax));
            float gs = 0.25F;
            if (seg % 2 == 0) gs += 0.1F;
            int r = (int) (gs * 255);
            int g = (int) (gs * 255);
            int b = (int) (gs * 255);
            int a = (int) (0.4F * 255);
            if (mouseInSector) {
                r = g = b = 255;
            }
            if (seg == slotSelected) {
                r = g = 255;
                a = (int) (0.6F * 255);
            }
            int sliceColor = (a << 24) | (r << 16) | (g << 8) | b;
            drawPieSlice(graphics, x, y, totalDeg, degPer, radius * innerRatio, radius, sliceColor);
            totalDeg += degPer;
        }

        totalDeg = 0;
        for (int seg = 0; seg < SEGMENTS; seg++) {
            NBTHelpers.PortalDestination favorite = getFavorite(seg);
            String favoriteName = favorite != null ? favorite.name() : "Empty";
            String dimension = favorite != null && !favorite.equals(NBTHelpers.PortalDestination.EMPTY) ? favorite.globalVec3().dimension().identifier().getPath().toString() : "";
            String coordinates = favorite != null && !favorite.equals(NBTHelpers.PortalDestination.EMPTY) ? String.format("(%d, %d, %d)",
                    (int) favorite.globalVec3().position().x(),
                    (int) favorite.globalVec3().position().y(),
                    (int) favorite.globalVec3().position().z()) : "";
            float nameAngleDeg = totalDeg + degPer / 2F;
            float nameAngleRad = nameAngleDeg * (float) Math.PI / 180F;
            float nameX = x + (float) (Math.cos(nameAngleRad) * (radiusMax / 1.4)) * fract;
            float nameY = y + (float) (Math.sin(nameAngleRad) * (radiusMax / 1.4)) * fract;
            int textWidth = this.font.width(favoriteName);
            int dimensionWidth = this.font.width(dimension);
            int coordinatesWidth = this.font.width(coordinates);

            boolean upsideDown = nameAngleRad > Math.PI / 2 && nameAngleRad < 3 * Math.PI / 2;
            float rotation = upsideDown ? nameAngleRad + (float) Math.PI : nameAngleRad;

            Matrix3x2fStack pose = graphics.pose();
            pose.pushMatrix();
            pose.translate(nameX, nameY);
            pose.rotate(rotation);
            pose.scale(0.85F, 0.85F);
            graphics.text(this.font, favoriteName, -textWidth / 2, -15, WHITE_ARGB, false);
            pose.popMatrix();

            pose.pushMatrix();
            pose.translate(nameX, nameY);
            pose.rotate(rotation);
            pose.scale(0.7F, 0.7F);
            graphics.text(this.font, dimension, -dimensionWidth / 2, -5, LIGHT_GRAY_ARGB, false);
            graphics.text(this.font, coordinates, -coordinatesWidth / 2, 10, LIGHT_GRAY_ARGB, false);
            pose.popMatrix();

            totalDeg += degPer;
        }

        for (Renderable renderable : this.renderables) {
            if (renderable instanceof BaseButton button && !button.getLocalization(mouseX, mouseY).equals(Component.empty())) {
                graphics.setTooltipForNextFrame(font, button.getLocalization(), mouseX, mouseY);
            }
        }
    }

    private void drawPieSlice(GuiGraphicsExtractor graphics, int cx, int cy, float startDeg, float spanDeg, float innerRadius, float outerRadius, int color) {
        if (outerRadius <= 0F || spanDeg <= 0F) return;
        float thickness = outerRadius - innerRadius;
        if (thickness <= 0F) return;

        int steps = Math.max(8, (int) Math.ceil(spanDeg));
        float stepRad = (spanDeg * (float) Math.PI / 180F) / steps;
        float startRad = startDeg * (float) Math.PI / 180F;
        float midRadius = (innerRadius + outerRadius) * 0.5F;
        float chord = 2F * midRadius * (float) Math.tan(stepRad * 0.5F);
        int quadHeight = Math.max(1, (int) Math.ceil(chord));

        Matrix3x2fStack pose = graphics.pose();
        for (int i = 0; i < steps; i++) {
            float segCenterRad = startRad + (i + 0.5F) * stepRad;
            pose.pushMatrix();
            pose.translate(cx, cy);
            pose.rotate(segCenterRad);
            int y0 = -quadHeight / 2;
            int y1 = y0 + quadHeight;
            graphics.fill((int) innerRadius, y0, (int) Math.ceil(outerRadius), y1, color);
            pose.popMatrix();
        }
    }

    private boolean isCursorInSlice(float angle, float totalDeg, float degPer, boolean inRange) {
        return inRange && angle > totalDeg && angle < totalDeg + degPer;
    }

    public boolean isInRange(double mouseX, double mouseY) {
        int x = this.width / 2;
        int y = this.height / 2;

        double dist = new Vec3(x, y, 0).distanceTo(new Vec3(mouseX, mouseY, 0));
        return dist > radiusMin && dist < radiusMax;
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        if (isInRange(event.x(), event.y()))
            saveFavorite();
        return super.mouseClicked(event, doubleClick);
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        int keyCode = event.key();
        if (staysOpen && (keyCode == 256 || keyCode == KeyBindings.toggleTool.getKey().getValue())) {
            onClose();
            return true;
        }
        return super.keyPressed(event);
    }

    @Override
    public void tick() {
        if (!staysOpen && !InputConstants.isKeyDown(Minecraft.getInstance().getWindow(), KeyBindings.toggleTool.getKey().getValue())) {
            onClose();
        }
        this.timeIn++;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public void saveFavorite() {
        slotSelected = slotHovered;
        ClientPacketDistributor.sendToServer(new PortalGunFavoritePayload(slotSelected, staysOpen));
        OurSounds.playSound(JDTRegistration.BEEP.get());
    }

    public void addFavorite() {
        ClientPacketDistributor.sendToServer(new PortalGunFavoriteChangePayload(slotSelected, true, "UNNAMED", false, Vec3.ZERO));
    }

    public void removeFavorite() {
        ClientPacketDistributor.sendToServer(new PortalGunFavoriteChangePayload(slotSelected, false, "NOTNEEDED", false, Vec3.ZERO));
    }

    public void editFavorite() {
        Minecraft.getInstance().setScreen(new AdvPortalEditMenu(portalGun, slotSelected));
    }

    public NBTHelpers.PortalDestination getFavorite(int slot) {
        return PortalGunV2.getFavorite(portalGun, slot);
    }

    private static class Vector2f {
        public float x;
        public float y;

        public Vector2f(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public final float dot(Vector2f v1) {
            return (this.x * v1.x + this.y * v1.y);
        }

        public final float length() {
            return (float) Math.sqrt(this.x * this.x + this.y * this.y);
        }
    }
}
