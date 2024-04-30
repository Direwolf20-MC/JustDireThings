package com.direwolf20.justdirethings.client.overlays;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.common.items.interfaces.AbilityParams;
import com.direwolf20.justdirethings.common.items.interfaces.ToggleableTool;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;


public class AbilityCooldownOverlay implements LayeredDraw.Layer {
    public static final AbilityCooldownOverlay INSTANCE = new AbilityCooldownOverlay();
    private static final EquipmentSlot[] EQUIPMENT_ORDER = {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
    protected final ResourceLocation INVULNERABILITY_ICON = new ResourceLocation(JustDireThings.MODID, "textures/gui/overlay/invulnerability.png");

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        int screenWidth = guiGraphics.guiWidth();
        int screenHeight = guiGraphics.guiHeight();
        int xPosition = screenWidth / 2 - 91;
        int yPosition = screenHeight - 30 - 30; //TODO Review

        ItemStack chestplate = player.getItemBySlot(EquipmentSlot.CHEST);
        if (chestplate.getItem() instanceof ToggleableTool toggleableTool && toggleableTool.hasAbility(Ability.INVULNERABILITY)) {
            int activeCooldown = ToggleableTool.getCooldown(chestplate, Ability.INVULNERABILITY, true);
            if (activeCooldown > -1) {
                AbilityParams abilityParams = toggleableTool.getAbilityParams(Ability.INVULNERABILITY);
                int activeMax = abilityParams.activeCooldown;
                int iconHeight = ((activeCooldown * 17) / activeMax) + 1;
                int blitYPosition = yPosition + (18 - iconHeight);  // This positions the blit starting point to the bottom of the intended icon segment
                int textureYOffset = 18 - iconHeight; // This ensures the texture is sliced from the bottom upwards
                guiGraphics.blit(INVULNERABILITY_ICON, xPosition, blitYPosition, 0, textureYOffset, 18, iconHeight, 18, 18);
            }
            int cooldown = ToggleableTool.getCooldown(chestplate, Ability.INVULNERABILITY, false);
            if (cooldown > -1) {
                AbilityParams abilityParams = toggleableTool.getAbilityParams(Ability.INVULNERABILITY);
                int cooldownMax = abilityParams.cooldown;
                int iconHeight = 18 - ((cooldown * 18) / cooldownMax);
                int blitYPosition = yPosition + (18 - iconHeight);  // This positions the blit starting point to the bottom of the intended icon segment
                int textureYOffset = 18 - iconHeight; // This ensures the texture is sliced from the bottom upwards
                RenderSystem.setShaderColor(1f, 0.5f, 0.5f, 1.0f);
                guiGraphics.blit(INVULNERABILITY_ICON, xPosition, blitYPosition, 0, textureYOffset, 18, iconHeight, 18, 18);
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc(); // Sets up standard alpha blending
                RenderSystem.setShaderColor(1f, 0.5f, 0.5f, 0.25f);
                guiGraphics.blit(INVULNERABILITY_ICON, xPosition, yPosition, 0, 0, 18, 18, 18, 18);
            }
        }
    }
}
