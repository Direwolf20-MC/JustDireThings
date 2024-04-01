package com.direwolf20.justdirethings.util;

import com.direwolf20.justdirethings.common.blockentities.basebe.BaseMachineBE;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.common.util.ITeleporter;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

public class FakePlayerUtil {
    /**
     * This class completely and shamelessly stolen from Shadows from his mod Click Machine :)
     */
    public static class UsefulFakePlayer extends FakePlayer {

        public UsefulFakePlayer(Level world, GameProfile name) {
            super((ServerLevel) world, name);
        }

        @Override
        public float getEyeHeight(Pose pose) {
            return super.getEyeHeight(pose); // Allows for the position of the player to be the exact source when raytracing.
        }

        @Override
        public void initMenu(AbstractContainerMenu p_143400_) {
        }

        @Override
        public OptionalInt openMenu(MenuProvider p_9033_) {
            return OptionalInt.empty();
        }

        @Override
        public float getAttackStrengthScale(float adjustTicks) {
            return 1; // Prevent the attack strength from always being 0.03 due to not ticking.
        }

        @Override
        public Entity changeDimension(ServerLevel server, ITeleporter teleporter) {
            return createPlayer(server, this.getGameProfile());
        }
    }

    /**
     * Creates a new UsefulFakePlayer. Each {@link BaseMachineBE} needs its own.
     */
    public static UsefulFakePlayer createPlayer(Level world, GameProfile profile) {
        return new UsefulFakePlayer(world, profile);
    }

    /**
     * Sets up for a fake player to be usable to right click things. This player will be put at the center of the using side.
     *
     * @param player    The player.
     * @param pos       The position of the using tile entity.
     * @param direction The direction to use in.
     * @param toHold    The stack the player will be using. Should probably come from an ItemStackHandler or similar.
     */
    public static void setupFakePlayerForUse(UsefulFakePlayer player, BlockPos pos, Direction direction, ItemStack toHold, boolean sneaking) {
        player.getInventory().items.set(player.getInventory().selected, toHold);
        Direction facingDirection = direction.getOpposite(); //If we want to click the 'top' face of a block, we should be facing 'down'
        /*float pitch = direction == Direction.UP ? -90 : direction == Direction.DOWN ? 90 : 0;
        float yaw = direction == Direction.SOUTH ? 0 : direction == Direction.WEST ? 90 : direction == Direction.NORTH ? 180 : -90;
        Vec3i sideVec = direction.getNormal();
        Direction.Axis a = direction.getAxis();
        Direction.AxisDirection ad = direction.getAxisDirection();
        double x = a == Direction.Axis.X && ad == Direction.AxisDirection.NEGATIVE ? -.5 : .5 + sideVec.getX() / 1.9D;
        double y = 0.5 + sideVec.getY() / 1.9D;
        double z = a == Direction.Axis.Z && ad == Direction.AxisDirection.NEGATIVE ? -.5 : .5 + sideVec.getZ() / 1.9D;
        player.moveTo(pos.relative(direction).getX() + x, pos.relative(direction).getY() + y - player.getEyeHeight(), pos.relative(direction).getZ() + z, yaw, pitch);*/
        //float pitch = facingDirection == Direction.UP ? -90 : direction == Direction.DOWN ? 90 : 0;
        //float yaw = direction == Direction.SOUTH ? 0 : direction == Direction.WEST ? 90 : direction == Direction.NORTH ? 180 : -90;
        float xRot = facingDirection == Direction.DOWN ? 90 : facingDirection == Direction.UP ? -90 : 0;
        player.setXRot(xRot);
        player.setYRot(facingDirection.toYRot());
        player.setYHeadRot(facingDirection.toYRot());
        Vec3i sideVec = facingDirection.getNormal();
        Direction.Axis a = facingDirection.getAxis();
        Direction.AxisDirection ad = facingDirection.getAxisDirection();
        double x = a == Direction.Axis.X ? ad == Direction.AxisDirection.NEGATIVE ? 0.95 : 0.05 : 0.5;
        double y = a == Direction.Axis.Y ? ad == Direction.AxisDirection.NEGATIVE ? 0.95 : 0.05 : 0.5;
        double z = a == Direction.Axis.Z ? ad == Direction.AxisDirection.NEGATIVE ? 0.95 : 0.05 : 0.5;
        player.setPos(pos.getX() + x, pos.getY() + y - player.getEyeHeight(), pos.getZ() + z);
        if (!toHold.isEmpty())
            player.getAttributes().addTransientAttributeModifiers(toHold.getAttributeModifiers(EquipmentSlot.MAINHAND));
        player.setShiftKeyDown(sneaking);
    }

    /**
     * Cleans up the fake player after use.
     *
     * @param player   The player.
     * @param oldStack The previous stack, from before use.
     */
    public static void cleanupFakePlayerFromUse(UsefulFakePlayer player, ItemStack oldStack) {
        if (!oldStack.isEmpty())
            player.getAttributes().removeAttributeModifiers(oldStack.getAttributeModifiers(EquipmentSlot.MAINHAND));
        player.getInventory().items.set(player.getInventory().selected, ItemStack.EMPTY);
        if (!player.getInventory().isEmpty()) player.getInventory().dropAll();
        player.setShiftKeyDown(false);
    }

    /**
     * Uses whatever the player happens to be holding in the given direction.
     *
     * @param player      The player.
     * @param world       The world of the calling tile entity. It may be a bad idea to use {FakePlayer#getEntityWorld()}.
     * @param pos         The pos of the calling tile entity.
     * @param side        The direction to use in.
     * @param sourceState The state of the calling tile entity, so we don't click ourselves.
     * @return The remainder of whatever the player was holding. This should be set back into the tile's stack handler or similar.
     */
    public static ItemStack rightClickBlockInDirection(UsefulFakePlayer player, Level world, BlockPos pos, Direction side, BlockState sourceState) {
        HitResult toUse = rayTraceBlock(player, world, 0.9);
        if (toUse == null) return player.getMainHandItem();

        ItemStack itemstack = player.getMainHandItem();
        if (toUse.getType() == HitResult.Type.BLOCK) {
            BlockPos blockpos = ((BlockHitResult) toUse).getBlockPos();
            BlockState state = world.getBlockState(blockpos);
            if (!state.isAir()) {
                InteractionResult type = player.gameMode.useItemOn(player, world, itemstack, InteractionHand.MAIN_HAND, (BlockHitResult) toUse);
                if (type == InteractionResult.SUCCESS) return player.getMainHandItem();
            }
        }


        if (toUse.getType() == HitResult.Type.MISS) {
            /*MutableObject<InteractionResult> mutableobject = new MutableObject<>();
            InteractionResult cancelResult = net.neoforged.neoforge.common.CommonHooks.onItemRightClick(player, InteractionHand.MAIN_HAND);
            if (cancelResult != null) {
                mutableobject.setValue(cancelResult);
                return itemstack;
            }
            InteractionResultHolder<ItemStack> interactionresultholder = itemstack.use(world, player, InteractionHand.MAIN_HAND);
            ItemStack itemstack1 = interactionresultholder.getObject();
            if (itemstack1 != itemstack) {
                player.setItemInHand(InteractionHand.MAIN_HAND, itemstack1);
                return itemstack1;
            }*/
            for (int i = 1; i <= 5; i++) { //This somehow hits levers when it would originally miss them
                BlockState state = world.getBlockState(pos.relative(side, i));
                if (state != sourceState && !state.isAir()) {
                    player.gameMode.useItemOn(player, world, itemstack, InteractionHand.MAIN_HAND, (BlockHitResult) toUse);
                    return player.getMainHandItem();
                }
            }
        }

        if (itemstack.isEmpty() && (toUse == null || toUse.getType() == HitResult.Type.MISS))
            CommonHooks.onEmptyClick(player, InteractionHand.MAIN_HAND); //Empty Hand Click i assume?
        if (!itemstack.isEmpty())
            player.gameMode.useItem(player, world, itemstack, InteractionHand.MAIN_HAND); //Uses the item by itself
        return player.getMainHandItem();
    }

    /**
     * Attacks with whatever the player happens to be holding in the given direction.
     *
     * @param player      The player.
     * @param world       The world of the calling tile entity. It may be a bad idea to use {FakePlayer#getEntityWorld()}.
     * @param pos         The pos of the calling tile entity.
     * @param side        The direction to attack in.
     * @param sourceState The state of the calling tile entity, so we don't click ourselves.
     * @return The remainder of whatever the player was holding. This should be set back into the tile's stack handler or similar.
     */
    public static ItemStack leftClickInDirection(UsefulFakePlayer player, Level world, BlockPos pos, Direction side, BlockState sourceState) {
        HitResult toUse = rayTrace(player, world, player.getAttributeValue(NeoForgeMod.BLOCK_REACH));
        if (toUse == null) return player.getMainHandItem();

        if (toUse.getType() == HitResult.Type.ENTITY) {
            if (processUseEntity(player, world, ((EntityHitResult) toUse).getEntity(), null, InteractionType.ATTACK))
                return player.getMainHandItem();
        } else if (toUse.getType() == HitResult.Type.BLOCK) {
            BlockPos blockpos = ((BlockHitResult) toUse).getBlockPos();
            BlockState state = world.getBlockState(blockpos);
            if (state != sourceState && !state.isAir()) {
                player.gameMode.handleBlockBreakAction(blockpos, ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK, ((BlockHitResult) toUse).getDirection(), player.level().getMaxBuildHeight(), 0);
                return player.getMainHandItem();
            }
        }

        if (toUse == null || toUse.getType() == HitResult.Type.MISS) {
            for (int i = 1; i <= 5; i++) {
                BlockState state = world.getBlockState(pos.relative(side, i));
                if (state != sourceState && !state.isAir()) {
                    player.gameMode.handleBlockBreakAction(pos.relative(side, i), ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK, side.getOpposite(), player.level().getMaxBuildHeight(), 0);
                    return player.getMainHandItem();
                }
            }
        }

        return player.getMainHandItem();
    }

    /**
     * Traces for an entity.
     *
     * @param player The player.
     * @param world  The world of the calling tile entity.
     * @return A ray trace result that will likely be of type entity, but may be type block, or null.
     */
    public static HitResult traceEntities(UsefulFakePlayer player, Vec3 base, Vec3 target, Level world) {
        Entity pointedEntity = null;
        HitResult result = null;
        Vec3 vec3d3 = null;
        AABB search = new AABB(base.x, base.y, base.z, target.x, target.y, target.z).inflate(.5, .5, .5);
        List<Entity> list = world.getEntities(player, search, entity -> EntitySelector.NO_SPECTATORS.test(entity) && entity != null && entity.isPickable());
        double d2 = 5;

        for (int j = 0; j < list.size(); ++j) {
            Entity entity1 = list.get(j);

            AABB aabb = entity1.getBoundingBox().inflate(entity1.getPickRadius());
            Optional<Vec3> optVec = aabb.clip(base, target);

            if (aabb.contains(base)) {
                if (d2 >= 0.0D) {
                    pointedEntity = entity1;
                    vec3d3 = optVec.orElse(base);
                    d2 = 0.0D;
                }
            } else if (optVec.isPresent()) {
                double d3 = base.distanceTo(optVec.get());

                if (d3 < d2 || d2 == 0.0D) {
                    if (entity1.getRootVehicle() == player.getRootVehicle() && !entity1.canRiderInteract()) {
                        if (d2 == 0.0D) {
                            pointedEntity = entity1;
                            vec3d3 = optVec.get();
                        }
                    } else {
                        pointedEntity = entity1;
                        vec3d3 = optVec.get();
                        d2 = d3;
                    }
                }
            }
        }

        if (pointedEntity != null && base.distanceTo(vec3d3) > 5) {
            pointedEntity = null;
            result = BlockHitResult.miss(vec3d3, null, BlockPos.containing(vec3d3));
        }

        if (pointedEntity != null) {
            result = new EntityHitResult(pointedEntity, vec3d3);
        }

        return result;
    }

    /**
     * Processes the using of an entity from the server side.
     *
     * @param player The player.
     * @param world  The world of the calling tile entity.
     * @param entity The entity to interact with.
     * @param result The actual ray trace result, only necessary if using {CUseEntityPacket.Action#INTERACT_AT}
     * @param action The type of interaction to perform.
     * @return If the entity was used.
     */
    public static boolean processUseEntity(UsefulFakePlayer player, Level world, Entity entity, @Nullable HitResult result, InteractionType action) {
        if (entity != null) {
            if (player.distanceToSqr(entity) < 36) {
                if (action == InteractionType.INTERACT) {
                    return player.interactOn(entity, InteractionHand.MAIN_HAND) == InteractionResult.SUCCESS;
                } else if (action == InteractionType.INTERACT_AT) {
                    if (CommonHooks.onInteractEntityAt(player, entity, result.getLocation(), InteractionHand.MAIN_HAND) != null)
                        return false;
                    return entity.interactAt(player, result.getLocation(), InteractionHand.MAIN_HAND) == InteractionResult.SUCCESS;
                } else if (action == InteractionType.ATTACK) {
                    if (entity instanceof ItemEntity || entity instanceof ExperienceOrb || entity instanceof Arrow || entity == player)
                        return false;
                    player.attack(entity);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Util to perform a raytrace for what the fake player is looking at.
     */
    public static HitResult rayTrace(UsefulFakePlayer player, Level level, double reachDist) {
        Vec3 base = new Vec3(player.getX(), player.getEyeY(), player.getZ());
        Vec3 look = player.getLookAngle();
        Vec3 target = base.add(look.x * reachDist, look.y * reachDist, look.z * reachDist);
        HitResult trace = level.clip(new ClipContext(base, target, ClipContext.Block.OUTLINE, ClipContext.Fluid.SOURCE_ONLY, player));
        HitResult traceEntity = traceEntities(player, base, target, level);
        HitResult toUse = trace == null ? traceEntity : trace;

        if (trace != null && traceEntity != null) {
            double d1 = trace.getLocation().distanceTo(base);
            double d2 = traceEntity.getLocation().distanceTo(base);
            toUse = traceEntity.getType() == HitResult.Type.ENTITY && d1 > d2 ? traceEntity : trace;
        }

        return toUse;
    }

    public static HitResult rayTraceBlock(UsefulFakePlayer player, Level level, double reachDist) {
        Vec3 base = new Vec3(player.getX(), player.getEyeY(), player.getZ());
        Vec3 look = player.getLookAngle();
        Vec3 target = base.add(look.x * reachDist, look.y * reachDist, look.z * reachDist);
        HitResult trace = level.clip(new ClipContext(base, target, ClipContext.Block.OUTLINE, ClipContext.Fluid.SOURCE_ONLY, player));

        return trace;
    }

    public static enum InteractionType {
        INTERACT,
        INTERACT_AT,
        ATTACK;
    }
}
