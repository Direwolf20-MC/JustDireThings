package com.direwolf20.justdirethings.mixin;

/*@Mixin(Entity.class)
public abstract class EntityMixin {

    @Inject(method = "collide", at = @At("HEAD"), cancellable = true)
    private void onCollide(Vec3 vec, CallbackInfoReturnable<Vec3> cir) {
        Entity entity = (Entity) (Object) this;
        if (entity instanceof Player player && shouldPassThroughWalls(player)) {
            // Return the same vector, indicating no collision
            cir.setReturnValue(vec);
        }
    }

    private boolean shouldPassThroughWalls(Player player) {
        ItemStack leggings = player.getItemBySlot(EquipmentSlot.LEGS);
        if (leggings.getItem() instanceof ToggleableTool toggleableTool) {
            return toggleableTool.canUseAbilityAndDurability(leggings, Ability.PHASE);
        }
        return false;
    }
}*/
