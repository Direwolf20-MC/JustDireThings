package com.direwolf20.justdirethings.client.overlays;

import com.direwolf20.justdirethings.common.items.datacomponents.JustDireDataComponents;
import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.common.items.interfaces.AbilityParams;
import com.direwolf20.justdirethings.common.items.interfaces.ToggleableTool;
import com.direwolf20.justdirethings.common.items.interfaces.ToolRecords;
import com.direwolf20.justdirethings.setup.Config;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.gui.GuiLayer;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public class AbilityCooldownOverlay implements GuiLayer {
    public static final AbilityCooldownOverlay INSTANCE = new AbilityCooldownOverlay();
    private static final EquipmentSlot[] EQUIPMENT_ORDER = {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND};

    private static final int COLOR_WHITE = 0xFFFFFFFF;
    private static final int COLOR_WHITE_QUARTER = 0x40FFFFFF;
    private static final int COLOR_RED_TINT = 0xFFFF8080;
    private static final int COLOR_RED_TINT_QUARTER = 0x40FF8080;

    @Override
    public void render(@NotNull GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.options.hideGui) return;
        Player player = mc.player;
        if (player == null) return;
        int screenWidth = guiGraphics.guiWidth();
        int screenHeight = guiGraphics.guiHeight();
        int renderedIcons = 0;

        for (EquipmentSlot slot : EQUIPMENT_ORDER) {
            ItemStack itemStack = player.getItemBySlot(slot);
            if (!(itemStack.getItem() instanceof ToggleableTool toggleableTool)) continue;
            if (!itemStack.has(JustDireDataComponents.ABILITY_COOLDOWNS)) continue;
            List<ToolRecords.AbilityCooldown> abilityCooldowns = itemStack.get(JustDireDataComponents.ABILITY_COOLDOWNS);
            if (abilityCooldowns == null) continue;

            for (ToolRecords.AbilityCooldown abilityCooldown : abilityCooldowns) {
                int xPosition = screenWidth / 2 - Config.OVERLAY_X.get() + ((renderedIcons % 7) * 11);
                int yPosition = screenHeight - Config.OVERLAY_Y.get() - ((renderedIcons / 7) * 11);
                Ability ability = Ability.byName(abilityCooldown.abilityName());
                int cooldown = abilityCooldown.cooldownTicks();
                boolean active = abilityCooldown.isactive();
                Identifier icon = ability.getCooldownIcon();
                if (active) {
                    AbilityParams abilityParams = toggleableTool.getAbilityParams(ability);
                    int activeMax = abilityParams.activeCooldown;
                    int iconHeight = ((cooldown * 8) / activeMax) + 1;
                    int blitYPosition = yPosition + (18 - iconHeight);
                    int textureYOffset = 9 - iconHeight;
                    guiGraphics.blit(RenderPipelines.GUI_TEXTURED, icon, xPosition, blitYPosition,
                            0, textureYOffset, 9, iconHeight, 9, iconHeight, 9, 9, COLOR_WHITE);
                    guiGraphics.blit(RenderPipelines.GUI_TEXTURED, icon, xPosition, yPosition + 9,
                            0, 0, 9, 9, 9, 9, 9, 9, COLOR_WHITE_QUARTER);
                } else {
                    AbilityParams abilityParams = toggleableTool.getAbilityParams(ability);
                    int cooldownMax = abilityParams.cooldown;
                    int iconHeight = 9 - ((cooldown * 9) / cooldownMax);
                    int blitYPosition = yPosition + (18 - iconHeight);
                    int textureYOffset = 9 - iconHeight;
                    guiGraphics.blit(RenderPipelines.GUI_TEXTURED, icon, xPosition, blitYPosition,
                            0, textureYOffset, 9, iconHeight, 9, iconHeight, 9, 9, COLOR_RED_TINT);
                    guiGraphics.blit(RenderPipelines.GUI_TEXTURED, icon, xPosition, yPosition + 9,
                            0, 0, 9, 9, 9, 9, 9, 9, COLOR_RED_TINT_QUARTER);
                }
                renderedIcons++;
            }
        }
    }
}
