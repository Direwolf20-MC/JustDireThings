/**
 * This class was adapted from code written by Vazkii for the PSI mod: https://github.com/Vazkii/Psi
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 */
package com.direwolf20.justdirethings.client.screens;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.client.KeyBindings;
import com.direwolf20.justdirethings.client.OurSounds;
import com.direwolf20.justdirethings.client.renderers.OurRenderTypes;
import com.direwolf20.justdirethings.client.screens.standardbuttons.ToggleButtonFactory;
import com.direwolf20.justdirethings.client.screens.widgets.BaseButton;
import com.direwolf20.justdirethings.client.screens.widgets.GrayscaleButton;
import com.direwolf20.justdirethings.common.items.PortalGunV2;
import com.direwolf20.justdirethings.common.network.data.PortalGunFavoriteChangePayload;
import com.direwolf20.justdirethings.common.network.data.PortalGunFavoritePayload;
import com.direwolf20.justdirethings.setup.Registration;
import com.direwolf20.justdirethings.util.NBTHelpers;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import org.joml.Matrix4f;

import java.awt.*;

public class AdvPortalRadialMenu extends Screen {
    ToggleButtonFactory.TextureLocalization ADD_BUTTON = new ToggleButtonFactory.TextureLocalization(ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "textures/gui/buttons/add.png"), Component.translatable("justdirethings.screen.add_favorite"));
    ToggleButtonFactory.TextureLocalization REMOVE_BUTTON = new ToggleButtonFactory.TextureLocalization(ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "textures/gui/buttons/remove.png"), Component.translatable("justdirethings.screen.remove_favorite"));
    ToggleButtonFactory.TextureLocalization EDIT_BUTTON = new ToggleButtonFactory.TextureLocalization(ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "textures/gui/buttons/matchnbttrue.png"), Component.translatable("justdirethings.screen.edit_favorite"));
    ToggleButtonFactory.TextureLocalization STAYOPEN_BUTTON = new ToggleButtonFactory.TextureLocalization(ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "textures/gui/buttons/area.png"), Component.translatable("justdirethings.screen.stay_open"));
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
    public void renderBackground(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
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
    public void render(GuiGraphics guiGraphics, int mx, int my, float partialTicks) {
        renderTooltip(guiGraphics, mx, my);
        portalGun = PortalGunV2.getPortalGunv2(Minecraft.getInstance().player);
        PoseStack matrices = guiGraphics.pose();
        float speedOfButtonGrowth = 5f; // How fast the buttons move during initial window opening
        float fract = Math.min(speedOfButtonGrowth, this.timeIn + partialTicks) / speedOfButtonGrowth;
        int x = this.width / 2;
        int y = this.height / 2;

        boolean inRange = isInRange(mx, my);


        // This triggers the animation on creation - only affects side buttons and slider(s)
        matrices.pushPose();
        matrices.translate((1 - fract) * x, (1 - fract) * y, 0);
        matrices.scale(fract, fract, fract);
        super.render(guiGraphics, mx, my, partialTicks);
        matrices.popPose();

        float angle = mouseAngle(x, y, mx, my);
        float totalDeg = 0;
        float degPer = 360F / SEGMENTS;

        for (int seg = 0; seg < SEGMENTS; seg++) {
            NBTHelpers.PortalDestination favorite = getFavorite(seg);
            String favoriteName = favorite != null ? favorite.name() : "Empty";
            String dimension = favorite != null && !favorite.equals(NBTHelpers.PortalDestination.EMPTY) ? favorite.globalVec3().dimension().location().getPath().toString() : "";
            String coordinates = favorite != null && !favorite.equals(NBTHelpers.PortalDestination.EMPTY) ? String.format("(%d, %d, %d)",
                    (int) favorite.globalVec3().position().x(),
                    (int) favorite.globalVec3().position().y(),
                    (int) favorite.globalVec3().position().z()) : "";
            boolean mouseInSector = this.isCursorInSlice(angle, totalDeg, degPer, inRange);
            float delayBetweenSegments = 1f;
            float speedOfSegmentGrowth = 25f;
            float radius = Math.max(0F, Math.min((this.timeIn + partialTicks - seg * delayBetweenSegments / SEGMENTS) * speedOfSegmentGrowth, radiusMax));
            float gs = 0.25F;
            if (seg % 2 == 0) {
                gs += 0.1F;
            }

            float r = gs;
            float g = gs;
            float b = gs;
            float a = 0.4F;
            if (mouseInSector) {
                this.slotHovered = seg;
                r = g = b = 1F;
            }
            if (seg == slotSelected) {
                r = g = 1F;
                a = 0.6f;
            }

            MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
            VertexConsumer buffer = bufferSource.getBuffer(OurRenderTypes.TRIANGLE_STRIP);

            for (float i = degPer; i >= 0; i--) {
                float rad = (float) ((i + totalDeg) / 180F * Math.PI);
                float xp = (float) (x + Math.cos(rad) * radius);
                float yp = (float) (y + Math.sin(rad) * radius);

                Matrix4f pose = matrices.last().pose();
                buffer.addVertex(pose, (float) (x + Math.cos(rad) * radius / 2.3F), (float) (y + Math.sin(rad) * radius / 2.3F), 0).setColor(r, g, b, a);
                buffer.addVertex(xp, yp, 0).setColor(r, g, b, a);
            }

            bufferSource.endBatch(OurRenderTypes.TRIANGLE_STRIP);
            totalDeg += degPer;

            // Draw the name of the favorite destination, centered within each slice
            float nameAngle = (totalDeg - degPer / 2) * (float) Math.PI / 180F;
            float nameX = x + (float) (Math.cos(nameAngle) * (radiusMax / 1.4));
            float nameY = y + (float) (Math.sin(nameAngle) * (radiusMax / 1.4));
            int textWidth = this.font.width(favoriteName);
            int dimensionWidth = this.font.width(dimension);
            int coordinatesWidth = this.font.width(coordinates);

            matrices.pushPose();
            matrices.translate(nameX, nameY, 0);
            matrices.scale(0.85f, 0.85f, 0.85f); // Scale down to simulate a smaller font
            // Determine if the text is upside down and adjust the rotation
            if (nameAngle > Math.PI / 2 && nameAngle < 3 * Math.PI / 2) {
                matrices.mulPose(Axis.ZP.rotation(nameAngle + (float) Math.PI)); // Rotate the text upside down
            } else {
                matrices.mulPose(Axis.ZP.rotation(nameAngle)); // Rotate the text normally
            }
            guiGraphics.drawString(this.font, favoriteName, -textWidth / 2, -15, Color.WHITE.getRGB());
            matrices.popPose();

            matrices.pushPose();
            matrices.translate(nameX, nameY, 0);
            // Draw the dimension and coordinates underneath the name in a smaller font
            matrices.scale(0.7f, 0.7f, 0.7f); // Scale down to simulate a smaller font
            if (nameAngle > Math.PI / 2 && nameAngle < 3 * Math.PI / 2) {
                matrices.mulPose(Axis.ZP.rotation(nameAngle + (float) Math.PI)); // Rotate the text upside down
            } else {
                matrices.mulPose(Axis.ZP.rotation(nameAngle)); // Rotate the text normally
            }
            guiGraphics.drawString(this.font, dimension, -dimensionWidth / 2, -5, Color.LIGHT_GRAY.getRGB());
            guiGraphics.drawString(this.font, coordinates, -coordinatesWidth / 2, 10, Color.LIGHT_GRAY.getRGB());
            matrices.popPose();
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
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if (isInRange(mouseX, mouseY))
            saveFavorite();
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {
        if (staysOpen && (p_keyPressed_1_ == 256 || p_keyPressed_1_ == KeyBindings.toggleTool.getKey().getValue())) {
            onClose();
            return true;
        }

        return super.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
    }

    @Override
    public void tick() {
        if (!staysOpen && !InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), KeyBindings.toggleTool.getKey().getValue())) {
            onClose();
        }

        this.timeIn++;
    }

    protected void renderTooltip(GuiGraphics pGuiGraphics, int pX, int pY) {
        for (Renderable renderable : this.renderables) {
            if (renderable instanceof BaseButton button && !button.getLocalization(pX, pY).equals(Component.empty())) {
                pGuiGraphics.renderTooltip(font, button.getLocalization(), pX, pY);
            }
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public void saveFavorite() {
        slotSelected = slotHovered;
        PacketDistributor.sendToServer(new PortalGunFavoritePayload(slotSelected, staysOpen));
        OurSounds.playSound(Registration.BEEP.get());
    }

    public void addFavorite() {
        PacketDistributor.sendToServer(new PortalGunFavoriteChangePayload(slotSelected, true, "UNNAMED", false, Vec3.ZERO));
    }

    public void removeFavorite() {
        PacketDistributor.sendToServer(new PortalGunFavoriteChangePayload(slotSelected, false, "NOTNEEDED", false, Vec3.ZERO));
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
