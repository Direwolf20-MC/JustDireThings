package com.direwolf20.justdirethings.client.overlays;

import com.direwolf20.justdirethings.common.items.datacomponents.JustDireDataComponents;
import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.common.items.interfaces.AbilityParams;
import com.direwolf20.justdirethings.common.items.interfaces.ToggleableTool;
import com.direwolf20.justdirethings.common.items.interfaces.ToolRecords;
import com.direwolf20.justdirethings.setup.Config;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public class AbilityCooldownOverlay implements LayeredDraw.Layer {
    public static final AbilityCooldownOverlay INSTANCE = new AbilityCooldownOverlay();
    private static final EquipmentSlot[] EQUIPMENT_ORDER = {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND};

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) return;
        int screenWidth = guiGraphics.guiWidth();
        int screenHeight = guiGraphics.guiHeight();
        int renderedIcons = 0;
        double scaleFactor = mc.getWindow().getGuiScale();
        //int scaleFactorHeight = mc.getWindow().getGuiScaledHeight();

        for (EquipmentSlot slot : EQUIPMENT_ORDER) {
            ItemStack itemStack = player.getItemBySlot(slot);
            if (!(itemStack.getItem() instanceof ToggleableTool toggleableTool)) continue;
            if (!itemStack.has(JustDireDataComponents.ABILITY_COOLDOWNS)) continue;
            List<ToolRecords.AbilityCooldown> abilityCooldowns = itemStack.get(JustDireDataComponents.ABILITY_COOLDOWNS);
            if (abilityCooldowns == null) continue;

            for (ToolRecords.AbilityCooldown abilityCooldown : abilityCooldowns) {
                //int xPosition = (int)(684/scaleFactor) + ((renderedIcons % 7) * 11);
                //int yPosition = (int)(885/scaleFactor) + ((renderedIcons / 7) * 11);
                int xPosition = screenWidth / 2 - Config.OVERLAY_X.get() + ((renderedIcons % 7) * 11);
                int yPosition = screenHeight - Config.OVERLAY_Y.get() - ((renderedIcons / 7) * 11);
                Ability ability = Ability.byName(abilityCooldown.abilityName());
                int cooldown = abilityCooldown.cooldownTicks();
                boolean active = abilityCooldown.isactive();
                if (active) {
                    AbilityParams abilityParams = toggleableTool.getAbilityParams(ability);
                    int activeMax = abilityParams.activeCooldown;
                    int iconHeight = ((cooldown * 8) / activeMax) + 1;
                    int blitYPosition = yPosition + (18 - iconHeight);  // This positions the blit starting point to the bottom of the intended icon segment
                    int textureYOffset = 9 - iconHeight; // This ensures the texture is sliced from the bottom upwards
                    guiGraphics.blit(ability.getCooldownIcon(), xPosition, blitYPosition, 0, textureYOffset, 9, iconHeight, 9, 9);
                    RenderSystem.enableBlend();
                    RenderSystem.defaultBlendFunc(); // Sets up standard alpha blending
                    RenderSystem.setShaderColor(1f, 1f, 1f, 0.25f);
                    guiGraphics.blit(ability.getCooldownIcon(), xPosition, yPosition + 9, 0, 0, 9, 9, 9, 9);
                    RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
                } else {
                    AbilityParams abilityParams = toggleableTool.getAbilityParams(ability);
                    int cooldownMax = abilityParams.cooldown;
                    int iconHeight = 9 - ((cooldown * 9) / cooldownMax);
                    int blitYPosition = yPosition + (18 - iconHeight);  // This positions the blit starting point to the bottom of the intended icon segment
                    int textureYOffset = 9 - iconHeight; // This ensures the texture is sliced from the bottom upwards
                    RenderSystem.setShaderColor(1f, 0.5f, 0.5f, 1.0f);
                    guiGraphics.blit(ability.getCooldownIcon(), xPosition, blitYPosition, 0, textureYOffset, 9, iconHeight, 9, 9);
                    RenderSystem.enableBlend();
                    RenderSystem.defaultBlendFunc(); // Sets up standard alpha blending
                    RenderSystem.setShaderColor(1f, 0.5f, 0.5f, 0.25f);
                    guiGraphics.blit(ability.getCooldownIcon(), xPosition, yPosition + 9, 0, 0, 9, 9, 9, 9);
                    RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
                }
                renderedIcons++;
            }
        }
    }
}
