package com.direwolf20.justdirethings.mixin;

import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Inject(method = "isInWall", at = @At("HEAD"), cancellable = true)
    private void onIsInWall(CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this instanceof Player player && shouldPassThroughWalls(player)) {
            cir.setReturnValue(false);
        }
    }

    private boolean shouldPassThroughWalls(Player player) {
        return player.getAttributeValue(Registration.PHASE) > 0;
    }
}
