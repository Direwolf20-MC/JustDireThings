package com.direwolf20.justdirethings.mixin;

import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.common.items.interfaces.ToggleableTool;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LightTexture.class)
public abstract class LightTextureMixin {

    @WrapOperation(
            method = "updateLightTexture",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;hasEffect(Lnet/minecraft/core/Holder;)Z")
    )
    private boolean wrapHasEffect(LocalPlayer instance, Holder holder, Operation<Boolean> original) {
        ItemStack helmet = instance.getItemBySlot(EquipmentSlot.HEAD);
        if (helmet.getItem() instanceof ToggleableTool toggleableTool && toggleableTool.canUseAbilityAndDurability(helmet, Ability.NIGHTVISION)) {
            return true; // Custom night vision effect
        }

        return original.call(instance, holder); // Continue with the original method call
    }

    @WrapOperation(
            method = "updateLightTexture",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GameRenderer;getNightVisionScale(Lnet/minecraft/world/entity/LivingEntity;F)F")
    )
    private float wrapNightVisionScale(LivingEntity livingEntity, float nanoTime, Operation<Float> original) {
        ItemStack helmet = livingEntity.getItemBySlot(EquipmentSlot.HEAD);
        if (helmet.getItem() instanceof ToggleableTool toggleableTool && toggleableTool.canUseAbilityAndDurability(helmet, Ability.NIGHTVISION)) {
            return 1f; // Custom night vision effect
        }

        return original.call(livingEntity, nanoTime); // Continue with the original method call
    }
}
