package com.direwolf20.justdirethings.mixin;

import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.common.items.interfaces.ToggleableTool;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockCollisions;
import net.minecraft.world.level.CollisionGetter;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Mixin(CollisionGetter.class)
public interface CollisionMixin {

    @Inject(method = "collidesWithSuffocatingBlock(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;)Z", at = @At("HEAD"), cancellable = true)
    private void collidesWithSuffocatingBlock(Entity entity, AABB box, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof Player && shouldPassThroughWalls((Player) entity)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "getBlockCollisions", at = @At("HEAD"), cancellable = true)
    private void onGetBlockCollisions(Entity entity, AABB collisionBox, CallbackInfoReturnable<Iterable<VoxelShape>> cir) {
        if (entity instanceof Player player && shouldPassThroughWalls(player)) {
            Iterable<VoxelShape> originalBlockCollisions = getOriginalBlockCollisions(entity, collisionBox);

            List<VoxelShape> filteredBlockCollisions = StreamSupport.stream(originalBlockCollisions.spliterator(), false)
                    .filter(shape -> isVerticalCollision(shape, collisionBox))
                    .collect(Collectors.toList());

            cir.setReturnValue(filteredBlockCollisions);
        }
    }

    private Iterable<VoxelShape> getOriginalBlockCollisions(Entity entity, AABB collisionBox) {
        // This method should call the original implementation of getBlockCollisions
        return () -> new BlockCollisions<>((CollisionGetter) (Object) this, entity, collisionBox, false, (p_286215_, p_286216_) -> p_286216_);
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
}
