package com.direwolf20.justdirethings.mixin;

/*@Mixin(CollisionGetter.class)
public interface CollisionMixin {

    @Inject(method = "noCollision(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;)Z", at = @At("HEAD"), cancellable = true)
    private void noCollision(Entity entity, AABB collisionBox, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof Player player && shouldPassThroughWalls(player)) {
            // Check if the entity is colliding with the sides of the block
            VoxelShape shape = Shapes.create(collisionBox);
            //if (isSideCollisionOnly(shape)) {
                cir.setReturnValue(true);
            //}
        }
    }

    @Inject(method = "collidesWithSuffocatingBlock(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;)Z", at = @At("HEAD"), cancellable = true)
    private void collidesWithSuffocatingBlock(Entity entity, AABB box, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof Player && shouldPassThroughWalls((Player) entity)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "getCollisions", at = @At("HEAD"), cancellable = true)
    default void getCollisions(@Nullable Entity entity, AABB collisionBox, CallbackInfoReturnable<Iterable<VoxelShape>> cir) {
        if (entity instanceof Player player && shouldPassThroughWalls(player)) {
            List<VoxelShape> entityCollisions = ((CollisionGetter) this).getEntityCollisions(entity, collisionBox);
            Iterable<VoxelShape> blockCollisions = ((CollisionGetter) this).getBlockCollisions(entity, collisionBox);
            Iterable<VoxelShape> combinedCollisions = entityCollisions.isEmpty() ? blockCollisions : Iterables.concat(entityCollisions, blockCollisions);

            List<VoxelShape> filteredCollisions = StreamSupport.stream(combinedCollisions.spliterator(), false)
                    .filter(shape -> isVerticalCollision(shape, collisionBox))
                    .collect(Collectors.toList());

            cir.setReturnValue(filteredCollisions);
        }
    }

    default boolean isVerticalCollision(VoxelShape shape, AABB collisionBox) {
        double maxY = shape.max(Direction.Axis.Y);
        double minY = collisionBox.minY;

        boolean isDifferenceLessThanPointTwo = Math.abs(maxY - minY) < 0.2;
        return isDifferenceLessThanPointTwo; // Ignore vertical collisions
    }

    default boolean shouldPassThroughWalls(Player player) {
        ItemStack leggings = player.getItemBySlot(EquipmentSlot.LEGS);
        if (leggings.getItem() instanceof ToggleableTool toggleableTool && toggleableTool.canUseAbilityAndDurability(leggings, Ability.PHASE))
            return true;
        return false;
    }
}*/
