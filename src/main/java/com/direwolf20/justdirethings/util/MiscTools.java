package com.direwolf20.justdirethings.util;

import com.direwolf20.justdirethings.datagen.JustDireBlockTags;
import com.mojang.math.Axis;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MiscTools {
    public static void doExtraTicks(ServerLevel serverLevel, BlockPos blockPos, double rate) {
        BlockState blockState = serverLevel.getBlockState(blockPos);
        BlockEntity blockEntity = serverLevel.getBlockEntity(blockPos);
        if (!isValidTickAccelBlock(serverLevel, blockState, blockEntity))
            return;
        for (int i = 0; i < rate; i++) {
            if (blockEntity != null) {
                BlockEntityTicker<BlockEntity> ticker = blockEntity.getBlockState().getTicker(serverLevel, (BlockEntityType<BlockEntity>) blockEntity.getType());
                if (ticker != null) {
                    ticker.tick(serverLevel, blockPos, blockEntity.getBlockState(), blockEntity);
                }
            } else if (blockState.isRandomlyTicking()) {
                if (serverLevel.random.nextInt(1365) == 0) { //Average Random Tick Rate
                    blockState.randomTick(serverLevel, blockPos, serverLevel.random);
                }
            }
        }
    }

    public static boolean isValidTickAccelBlock(ServerLevel serverLevel, BlockState blockState, BlockEntity blockEntity) {
        if (blockEntity == null && !blockState.isRandomlyTicking())
            return false;
        BlockEntityTicker<BlockEntity> ticker = blockEntity.getBlockState().getTicker(serverLevel, (BlockEntityType<BlockEntity>) blockEntity.getType());
        if (ticker == null)
            return false;
        if (blockState.is(JustDireBlockTags.TICK_SPEED_DENY))
            return false;
        return true;
    }

    //Thanks Soaryn!
    @NotNull
    public static BlockHitResult getHitResult(Player player) {
        var playerLook = new Vec3(player.getX(), player.getY() + player.getEyeHeight(), player.getZ());
        var lookVec = player.getViewVector(1.0F);
        var reach = player.blockInteractionRange();
        var endLook = playerLook.add(lookVec.x * reach, lookVec.y * reach, lookVec.z * reach);
        BlockHitResult hitResult = player.level().clip(new ClipContext(playerLook, endLook, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
        return hitResult;
    }

    public static Entity getEntityLookedAt(Player player, double maxDistance) {
        Vec3 eyePosition = player.getEyePosition(1.0F);
        Vec3 lookVector = player.getViewVector(1.0F).scale(maxDistance);
        Vec3 endPosition = eyePosition.add(lookVector);

        // Perform ray trace for entities
        HitResult hitResult = player.level().clip(new ClipContext(
                eyePosition, endPosition,
                ClipContext.Block.OUTLINE,
                ClipContext.Fluid.NONE,
                player));

        if (hitResult.getType() != HitResult.Type.MISS) {
            endPosition = hitResult.getLocation();
        }

        EntityHitResult entityHitResult = rayTraceEntities(player, eyePosition, endPosition, player.getBoundingBox().expandTowards(lookVector).inflate(1.0D, 1.0D, 1.0D), maxDistance);

        if (entityHitResult != null) {
            return entityHitResult.getEntity();
        }

        return null;
    }

    private static EntityHitResult rayTraceEntities(Player player, Vec3 start, Vec3 end, AABB boundingBox, double maxDistance) {
        double closestDistance = maxDistance;
        Entity closestEntity = null;
        Vec3 hitLocation = null;

        for (Entity entity : player.level().getEntities(player, boundingBox, e -> e != player && e.isPickable())) {
            AABB entityBoundingBox = entity.getBoundingBox().inflate(entity.getPickRadius());
            Optional<Vec3> optHitVec = entityBoundingBox.clip(start, end);

            if (entityBoundingBox.contains(start)) {
                if (closestDistance >= 0.0D) {
                    closestEntity = entity;
                    hitLocation = optHitVec.orElse(start);
                    closestDistance = 0.0D;
                }
            } else if (optHitVec.isPresent()) {
                Vec3 hitVec = optHitVec.get();
                double distanceToHitVec = start.distanceTo(hitVec);

                if (distanceToHitVec < closestDistance || closestDistance == 0.0D) {
                    if (entity.getRootVehicle() == player.getRootVehicle() && !entity.canRiderInteract()) {
                        if (closestDistance == 0.0D) {
                            closestEntity = entity;
                            hitLocation = hitVec;
                        }
                    } else {
                        closestEntity = entity;
                        hitLocation = hitVec;
                        closestDistance = distanceToHitVec;
                    }
                }
            }
        }

        return closestEntity == null ? null : new EntityHitResult(closestEntity, hitLocation);
    }

    public static boolean inBounds(int x, int y, int w, int h, double ox, double oy) {
        return ox >= x && ox <= x + w && oy >= y && oy <= y + h;
    }

    public static Vector3f findOffset(Direction direction, int slot, Vector3f[] offsets) {
        Vector3f offsetVector = new Vector3f(offsets[slot]);
        switch (direction) {
            case UP -> {
                Quaternionf quaternionf = Axis.XP.rotationDegrees(-270);
                offsetVector = quaternionf.transform(offsetVector);
                //offsetVector.transform(Vector3f.XP.rotationDegrees(-270));
                offsetVector.add(0, 1, 0);
            }
            case DOWN -> {
                Quaternionf quaternionf = Axis.XP.rotationDegrees(-90);
                offsetVector = quaternionf.transform(offsetVector);
                //offsetVector.transform(Vector3f.XP.rotationDegrees(-90));
                offsetVector.add(0, 0, 1);
                //reverse = false;
            }
            //case NORTH -> offsetVector;
            case EAST -> {
                Quaternionf quaternionf = Axis.YP.rotationDegrees(-90);
                offsetVector = quaternionf.transform(offsetVector);
                //offsetVector.transform(Vector3f.YP.rotationDegrees(-90));
                offsetVector.add(1, 0, 0);
            }
            case SOUTH -> {
                Quaternionf quaternionf = Axis.YP.rotationDegrees(-180);
                offsetVector = quaternionf.transform(offsetVector);
                //offsetVector.transform(Vector3f.YP.rotationDegrees(-180));
                offsetVector.add(1, 0, 1);
            }
            case WEST -> {
                Quaternionf quaternionf = Axis.YP.rotationDegrees(-270);
                offsetVector = quaternionf.transform(offsetVector);
                //offsetVector.transform(Vector3f.YP.rotationDegrees(-270));
                offsetVector.add(0, 0, 1);
            }
        }
        return offsetVector;
    }

    public static ListTag stringListToNBT(List<String> list) {
        ListTag nbtList = new ListTag();
        for (String string : list) {
            CompoundTag tag = new CompoundTag();
            tag.putString("list", string);
            nbtList.add(tag);
        }
        return nbtList;
    }

    public static List<String> NBTToStringList(ListTag nbtList) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < nbtList.size(); i++) {
            CompoundTag tag = nbtList.getCompound(i);
            list.add(tag.getString("list"));
        }
        return list;
    }

    public static MutableComponent tooltipMaker(String string, int color) {
        Style style = Style.EMPTY;
        style = style.withColor(color);
        MutableComponent current = Component.translatable(string);
        current.setStyle(style);
        return current;
    }
}
