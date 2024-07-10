package com.direwolf20.justdirethings.mixin;

import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.common.items.interfaces.ToggleableTool;
import com.direwolf20.justdirethings.datagen.JustDireBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockCollisions;
import net.minecraft.world.level.CollisionGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
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
                    .filter(shape -> isVerticalCollision(shape, collisionBox, player))
                    .collect(Collectors.toList());

            cir.setReturnValue(filteredBlockCollisions);
        }
    }

    private Iterable<VoxelShape> getOriginalBlockCollisions(Entity entity, AABB collisionBox) {
        // This method should call the original implementation of getBlockCollisions
        return () -> new BlockCollisions<>((CollisionGetter) (Object) this, entity, collisionBox, false, (p_286215_, p_286216_) -> p_286216_);
    }

    default boolean isVerticalCollision(VoxelShape shape, AABB collisionBox, Player player) {
        Level level = player.level();
        BlockPos blockPos = new BlockPos((int) shape.min(Direction.Axis.X), (int) shape.min(Direction.Axis.Y), (int) shape.min(Direction.Axis.Z));
        BlockState blockState = level.getBlockState(blockPos);
        if (blockState.getDestroySpeed(level, blockPos) < 0 || blockState.is(JustDireBlockTags.PHASEDENY))
            return true;
        double maxY = shape.max(Direction.Axis.Y);
        double minY = collisionBox.minY;

        boolean isDifferenceLessThanPointTwo = Math.abs(maxY - minY) < 0.75;
        return isDifferenceLessThanPointTwo; // Ignore vertical collisions
    }

    default boolean shouldPassThroughWalls(Player player) {
        ItemStack leggings = player.getItemBySlot(EquipmentSlot.LEGS);
        if (leggings.getItem() instanceof ToggleableTool toggleableTool && toggleableTool.canUseAbilityAndDurability(leggings, Ability.PHASE))
            return true;
        return false;
    }
}
