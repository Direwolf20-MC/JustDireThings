package com.direwolf20.justdirethings.common.items.interfaces;

import com.direwolf20.justdirethings.client.renderactions.ThingFinder;
import com.direwolf20.justdirethings.common.blockentities.EclipseGateBE;
import com.direwolf20.justdirethings.common.entities.DecoyEntity;
import com.direwolf20.justdirethings.common.events.BlockEvents;
import com.direwolf20.justdirethings.common.items.datacomponents.JustDireDataComponents;
import com.direwolf20.justdirethings.common.items.tools.utils.GooTier;
import com.direwolf20.justdirethings.common.network.data.ClientSoundPayload;
import com.direwolf20.justdirethings.datagen.JustDireBlockTags;
import com.direwolf20.justdirethings.datagen.JustDireEntityTags;
import com.direwolf20.justdirethings.setup.Registration;
import com.direwolf20.justdirethings.util.MiscTools;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.entity.PartEntity;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.*;
import java.util.stream.Collectors;

import static com.direwolf20.justdirethings.common.items.interfaces.Helpers.*;
import static com.direwolf20.justdirethings.common.items.interfaces.ToggleableTool.*;

public class AbilityMethods {
    public static boolean glowing(Level level, Player player, ItemStack itemStack) {
        if (testUseTool(itemStack, Ability.GLOWING) < 0)
            return false;
        BlockPos playerPos = player.getOnPos();
        int radius = 20;
        // Define the search area
        AABB searchArea = new AABB(playerPos).inflate(radius, radius, radius);

        List<Mob> entityList = level.getEntitiesOfClass(Mob.class, searchArea, entity -> true)
                .stream().toList();

        for (Mob entity : entityList) {
            entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 200, 0)); // 200 ticks = 10 seconds
        }
        player.playNotifySound(SoundEvents.SCULK_CLICKING, SoundSource.PLAYERS, 1.0F, 1.0F);
        damageTool(itemStack, player, Ability.GLOWING);
        return true;
    }

    public static boolean scanForMobScanner(Level level, Player player, ItemStack itemStack) {
        if (testUseTool(itemStack, Ability.MOBSCANNER) < 0)
            return false;
        return scanFor(level, player, itemStack, Ability.MOBSCANNER);
    }

    public static boolean scanForOreScanner(Level level, Player player, ItemStack itemStack) {
        if (testUseTool(itemStack, Ability.ORESCANNER) < 0)
            return false;
        return scanFor(level, player, itemStack, Ability.ORESCANNER);
    }

    public static boolean scanForOreXRAY(Level level, Player player, ItemStack itemStack) {
        if (testUseTool(itemStack, Ability.OREXRAY) < 0)
            return false;
        return scanFor(level, player, itemStack, Ability.OREXRAY);
    }

    public static boolean scanFor(Level level, Player player, ItemStack itemStack, Ability toolAbility) {
        if (level.isClientSide) {
            ThingFinder.discover(player, toolAbility, itemStack);
            if (toolAbility.equals(Ability.OREXRAY))
                player.playNotifySound(SoundEvents.SCULK_CLICKING, SoundSource.PLAYERS, 1.0F, 1.0F);
            else
                player.playNotifySound(SoundEvents.END_PORTAL_FRAME_FILL, SoundSource.PLAYERS, 1.0F, 1.0F);
        } else { //ServerSide
            damageTool(itemStack, player, toolAbility);
        }
        return false;
    }

    public static boolean leafbreaker(UseOnContext pContext) {
        ItemStack pStack = pContext.getItemInHand();
        if (testUseTool(pStack, Ability.LEAFBREAKER) < 0)
            return false;
        Level pLevel = pContext.getLevel();
        BlockPos pPos = pContext.getClickedPos();
        BlockState pState = pLevel.getBlockState(pPos);
        LivingEntity pEntityLiving = pContext.getPlayer();

        if (pState.getTags().anyMatch(tag -> tag.equals(BlockTags.LEAVES))) {
            int maxBreak = 64;
            if (pStack.getItem() instanceof TieredItem tieredItem) {
                if (tieredItem.getTier().equals(GooTier.BLAZEGOLD))
                    maxBreak = 128;
                else if (tieredItem.getTier().equals(GooTier.CELESTIGEM))
                    maxBreak = 192;
                else if (tieredItem.getTier().equals(GooTier.ECLIPSEALLOY))
                    maxBreak = 256;
            }
            Set<BlockPos> alsoBreakSet = findLikeBlocks(pLevel, pState, pPos, maxBreak, 2);
            BlockEvents.alreadyBreaking = true;
            BlockEvents.spawnDropsAtPos = pPos;
            for (BlockPos breakPos : alsoBreakSet) {
                if (testUseTool(pStack, Ability.LEAFBREAKER) < 0)
                    break;
                breakBlocksNew(pLevel, breakPos, pEntityLiving, pStack, false, false);
                pLevel.sendBlockUpdated(breakPos, pState, pLevel.getBlockState(breakPos), 3); // I have NO IDEA why this is necessary
                if (Math.random() < 0.1) //10% chance to damage tool
                    damageTool(pStack, pEntityLiving, Ability.LEAFBREAKER);
            }
            BlockEvents.alreadyBreaking = false;
            BlockEvents.spawnDropsAtPos = BlockPos.ZERO;
            return true;
        }
        return false;
    }

    public static boolean eclipseGate(UseOnContext pContext) {
        ItemStack pStack = pContext.getItemInHand();
        Level level = pContext.getLevel();
        if (level.isClientSide) return true;
        int distance = ToggleableTool.getToolValue(pStack, Ability.ECLIPSEGATE.getName());
        Set<BlockPos> posList = getEclipseGateBlocks(pContext, distance);
        boolean anyWorked = false;
        for (BlockPos blockPos : posList) {
            if (testUseTool(pStack, Ability.ECLIPSEGATE) < 0)
                break;
            BlockState blockState = level.getBlockState(blockPos);
            boolean placed = level.setBlockAndUpdate(blockPos, Registration.EclipseGateBlock.get().defaultBlockState());
            if (!placed) continue;
            level.sendBlockUpdated(blockPos, blockState, Registration.EclipseGateBlock.get().defaultBlockState(), 3);
            BlockEntity be = level.getBlockEntity(blockPos);
            if (be instanceof EclipseGateBE eclipseGateBE) {
                eclipseGateBE.setSourceBlock(blockState);
            }
            damageTool(pStack, pContext.getPlayer(), Ability.ECLIPSEGATE);
            anyWorked = true;
        }
        if (anyWorked) {
            level.playSound(null, pContext.getClickedPos(), SoundEvents.ENDER_EYE_DEATH, SoundSource.PLAYERS, 1F, 1.0F);
            teleportParticles((ServerLevel) level, posList);
            return true;
        }
        return false;
    }

    public static Set<BlockPos> getEclipseGateBlocks(UseOnContext pContext, int distance) {
        BlockPos clickedPos = pContext.getClickedPos();
        Direction direction = pContext.getClickedFace().getOpposite();
        BlockPos startPos = clickedPos.offset(
                direction.getAxis() == Direction.Axis.X ? 0 : -1,
                direction.getAxis() == Direction.Axis.Y ? 0 : -1,
                direction.getAxis() == Direction.Axis.Z ? 0 : -1
        );

        BlockPos endPos = clickedPos.relative(direction, distance - 1).offset(
                direction.getAxis() == Direction.Axis.X ? 0 : 1,
                direction.getAxis() == Direction.Axis.Y ? 0 : 1,
                direction.getAxis() == Direction.Axis.Z ? 0 : 1
        );
        Set<BlockPos> posSet = BlockPos.betweenClosedStream(startPos, endPos)
                .filter(blockPos -> isValidGateBlock((ServerLevel) pContext.getLevel(), blockPos, pContext.getPlayer()))
                .map(BlockPos::immutable)
                .collect(Collectors.toSet());

        return posSet;
    }

    public static boolean isValidGateBlock(ServerLevel serverLevel, BlockPos blockPos, Player player) {
        if (serverLevel.getBlockEntity(blockPos) != null) return false;
        BlockState blockState = serverLevel.getBlockState(blockPos);
        if (blockState.is(Registration.EclipseGateBlock.get())) return false;
        if (blockState.isAir()) return false;
        if (blockState.getDestroySpeed(serverLevel, blockPos) < 0) return false;
        if (blockState.is(JustDireBlockTags.ECLISEGATEDENY)) return false;
        if (blockState.is(Tags.Blocks.RELOCATION_NOT_SUPPORTED)) return false;
        return true;
    }

    public static boolean voidShift(Level level, Player player, ItemStack itemStack) {
        if (level.isClientSide) return false;
        Vec3 shiftPosition = getShiftPosition(level, player, itemStack);
        if (!shiftPosition.equals(Vec3.ZERO)) {
            WorldBorder worldBorder = level.getWorldBorder();
            if (!worldBorder.isWithinBounds(shiftPosition)) {
                // Notify player or take any other action if the position is outside the world border
                return false;
            }
            int distanceTraveled = (int) player.position().distanceTo(shiftPosition);
            if (testUseTool(itemStack, Ability.VOIDSHIFT, distanceTraveled) < 0)
                return false;
            if (player.isPassenger()) {
                player.dismountTo(shiftPosition.x, shiftPosition.y, shiftPosition.z);
            } else {
                player.teleportTo(shiftPosition.x, shiftPosition.y, shiftPosition.z);
            }
            player.resetFallDistance();
            PacketDistributor.sendToPlayer((ServerPlayer) player, new ClientSoundPayload(SoundEvents.PLAYER_TELEPORT.getLocation(), 1f, 1f));
            level.playSound(player, BlockPos.containing(shiftPosition), SoundEvents.PLAYER_TELEPORT, SoundSource.PLAYERS, 1F, 1.0F);
            damageTool(itemStack, player, Ability.VOIDSHIFT, distanceTraveled);
        }
        return false;
    }

    public static Vec3 getShiftPosition(Level level, Player player, ItemStack itemStack) {
        Vec3 returnVec;
        int distance = ToggleableTool.getToolValue(itemStack, Ability.VOIDSHIFT.getName());
        BlockHitResult result = (BlockHitResult) player.pick(distance, 0f, false);
        if (result.getType().equals(HitResult.Type.MISS)) {
            returnVec = getShapeAdjustedPosition(level, player, result, result.getDirection().getOpposite()); //If we miss hitting a block, go 1 further to match the distance specified
        } else {
            returnVec = getShapeAdjustedPosition(level, player, result); //If we miss hitting a block, go 1 further to match the distance specified
        }
        return returnVec;
    }

    public static Vec3 getShapeAdjustedPosition(Level level, Player player, BlockHitResult result) {
        BlockPos landingPos = result.getBlockPos().below().relative(result.getDirection());
        BlockState blockState = level.getBlockState(landingPos);
        VoxelShape voxelShape = blockState.getCollisionShape(level, landingPos, CollisionContext.of(player));
        if (voxelShape.isEmpty())
            return Vec3.atBottomCenterOf(landingPos);
        else
            return Vec3.atBottomCenterOf(landingPos).add(0, voxelShape.max(Direction.Axis.Y), 0);
    }

    public static Vec3 getShapeAdjustedPosition(Level level, Player player, BlockHitResult result, Direction dir) {
        BlockPos landingPos = result.getBlockPos().below().relative(result.getDirection()).relative(dir);
        BlockState blockState = level.getBlockState(landingPos);
        VoxelShape voxelShape = blockState.getCollisionShape(level, landingPos, CollisionContext.of(player));
        if (voxelShape.isEmpty())
            return Vec3.atBottomCenterOf(landingPos);
        else
            return Vec3.atBottomCenterOf(landingPos).add(0, voxelShape.max(Direction.Axis.Y), 0);
    }

    public static boolean airBurst(Level level, Player player, ItemStack itemStack) {
        int multiplier = ToggleableTool.getToolValue(itemStack, Ability.AIRBURST.getName());
        if (testUseTool(itemStack, Ability.AIRBURST, multiplier) < 0)
            return false;
        if (!level.isClientSide) {
            // Get the player's looking direction as a vector
            Vec3 lookDirection = player.getViewVector(1.0F);
            // Define the strength of the burst, adjust this value to change how strong the burst should be
            double addedStrength = (double) multiplier / 2;
            double burstStrength = 1.5 + addedStrength;
            // Set the player's motion based on the look direction and burst strength
            player.setDeltaMovement(lookDirection.x * burstStrength, lookDirection.y * burstStrength, lookDirection.z * burstStrength);
            ((ServerPlayer) player).connection.send(new ClientboundSetEntityMotionPacket(player));
            //player.hurtMarked = true; //This tells the server to move the client
            player.resetFallDistance();
            // Optionally, you could add some effects or sounds here
            damageTool(itemStack, player, Ability.AIRBURST, multiplier);
            PacketDistributor.sendToPlayer((ServerPlayer) player, new ClientSoundPayload(SoundEvents.FIRECHARGE_USE.getLocation(), 0.5f, 0.125f));
            level.playSound(player, player.getOnPos(), SoundEvents.FIRECHARGE_USE, SoundSource.PLAYERS, 0.5f, 0.125f);
            return true;
        }
        return true;
    }

    public static boolean cauterizeWounds(Level level, Player player, ItemStack itemStack) {
        if (testUseTool(itemStack, Ability.CAUTERIZEWOUNDS) < 0)
            return false;
        if (player.getHealth() >= player.getMaxHealth()) return false;
        if (!level.isClientSide) {
            int currentCooldown = ToggleableTool.getAnyCooldown(itemStack, Ability.CAUTERIZEWOUNDS);
            if (currentCooldown != -1) return false;
            if (itemStack.getItem() instanceof ToggleableTool toggleableTool && toggleableTool.canUseAbilityAndDurability(itemStack, Ability.CAUTERIZEWOUNDS)) {
                AbilityParams abilityParams = toggleableTool.getAbilityParams(Ability.CAUTERIZEWOUNDS);
                ToggleableTool.addCooldown(itemStack, Ability.CAUTERIZEWOUNDS, abilityParams.cooldown, false);
                player.heal(6f);
                player.playNotifySound(SoundEvents.LAVA_EXTINGUISH, SoundSource.PLAYERS, 1.0F, 1.0F);
                Random random = new Random();
                Vec3 pos = player.getEyePosition();
                for (int i = 0; i < 10; i++) {
                    double d0 = pos.x() + random.nextDouble();
                    double d1 = pos.y() + random.nextDouble();
                    double d2 = pos.z() + random.nextDouble();
                    ((ServerLevel) level).sendParticles(ParticleTypes.FLAME, d0, d1, d2, 1, 0.0, 0.0, 0.0, 0);
                }
                damageTool(itemStack, player, Ability.CAUTERIZEWOUNDS);
                return true;
            }
        }
        return false;
    }

    public static boolean lawnmower(Level level, Player player, ItemStack itemStack) {
        if (!level.isClientSide) {
            List<TagKey<Block>> tags = new ArrayList<>();
            tags.add(JustDireBlockTags.LAWNMOWERABLE);
            Set<BlockPos> breakBlocks = findTaggedBlocks(level, tags, player.getOnPos(), 64, 5);
            List<ItemStack> drops = new ArrayList<>();
            for (BlockPos breakPos : breakBlocks) {
                if (testUseTool(itemStack, Ability.LAWNMOWER) < 0)
                    break;
                BlockState state = level.getBlockState(breakPos);
                BlockEntity blockEntity = level.getBlockEntity(breakPos);
                if (level instanceof ServerLevel serverLevel) {
                    boolean removed = level.destroyBlock(breakPos, false);
                    if (removed)
                        drops.addAll(Block.getDrops(state, serverLevel, breakPos, blockEntity, player, itemStack)); //This isn't perfect, but should be 'good enough' for lawnmowerables
                }
                if (Math.random() < 0.1) //10% chance to damage tool
                    damageTool(itemStack, player, Ability.LAWNMOWER);
            }
            if (!breakBlocks.isEmpty()) {
                BlockPos firstPos = breakBlocks.iterator().next();
                handleDrops(itemStack, (ServerLevel) level, firstPos, player, breakBlocks, drops, level.getBlockState(firstPos), 0);
            }

            return true;
        }
        return false;
    }

    public static void handleDrops(ItemStack pStack, ServerLevel serverLevel, BlockPos pPos, LivingEntity pEntityLiving, BlockPos breakBlockPosition, List<ItemStack> drops, BlockState pState, int totalExp) {
        Set<BlockPos> positions = new HashSet<>();
        positions.add(breakBlockPosition);
        handleDrops(pStack, serverLevel, pPos, pEntityLiving, positions, drops, pState, totalExp);
    }


    public static void handleDrops(ItemStack pStack, ServerLevel serverLevel, BlockPos pPos, LivingEntity pEntityLiving, Set<BlockPos> breakBlockPositions, List<ItemStack> drops, BlockState pState, int totalExp) {
        if (pStack.getItem() instanceof ToggleableTool toggleableTool) {
            if (toggleableTool.canUseAbility(pStack, Ability.SMELTER) && pStack.getDamageValue() < pStack.getMaxDamage()) {
                boolean[] smeltedItemsFlag = new boolean[1]; // Array to hold the smelting flag
                drops = smeltDrops(serverLevel, drops, pStack, pEntityLiving, smeltedItemsFlag);
                if (smeltedItemsFlag[0])
                    smelterParticles(serverLevel, breakBlockPositions);
            }
            if (!drops.isEmpty() && toggleableTool.canUseAbility(pStack, Ability.DROPTELEPORT)) {
                IItemHandler handler = getBoundHandler(serverLevel, pStack);
                if (handler != null && pEntityLiving instanceof Player player) {
                    teleportDrops(drops, handler, pStack, player);
                    if (drops.isEmpty()) //Only spawn particles if we teleported everything - granted this isn't perfect, but way better than exhaustive testing
                        teleportParticles(serverLevel, breakBlockPositions);
                }
            }
            if (!drops.isEmpty())
                Helpers.dropDrops(drops, serverLevel, pPos);
            if (totalExp > 0)
                pState.getBlock().popExperience(serverLevel, pPos, totalExp);
        }
    }

    public static boolean runSpeed(Level level, Player player, ItemStack itemStack) {
        if (player.isSprinting() && !player.isFallFlying() && player.zza > 0F && !player.isInWaterOrBubble()) {
            float speed = (float) ToggleableTool.getToolValue(itemStack, Ability.RUNSPEED.getName()) / 25;
            if (!player.onGround())
                speed = speed / 4;
            player.moveRelative(speed, new Vec3(0, 0, 1));
        }
        return false;
    }

    public static boolean walkSpeed(Level level, Player player, ItemStack itemStack) {
        ItemStack chestItem = player.getItemBySlot(EquipmentSlot.CHEST);
        boolean canBoostElytra = chestItem.getItem() instanceof ToggleableTool toggleableTool && toggleableTool.canUseAbilityAndDurability(itemStack, Ability.ELYTRA);
        boolean isNotFlying = player.fallDistance <= 0 && !player.isFallFlying();
        boolean shouldBoostFlight = canBoostElytra || isNotFlying;
        if (!player.isSprinting() && shouldBoostFlight && player.zza > 0F && !player.isInWaterOrBubble()) {
            float speed = (float) ToggleableTool.getToolValue(itemStack, Ability.WALKSPEED.getName()) / 25;
            if (!player.onGround())
                speed = speed / 4;
            player.moveRelative(speed, new Vec3(0, 0, 1));
        }
        return false;
    }

    public static boolean swimSpeed(Level level, Player player, ItemStack itemStack) {
        if (player.fallDistance <= 0 && !player.isFallFlying() && player.zza > 0F && player.isInWaterOrBubble()) {
            float speed = (float) ToggleableTool.getToolValue(itemStack, Ability.SWIMSPEED.getName()) / 50;
            player.moveRelative(speed, new Vec3(0, 0, 1));
        }
        return false;
    }

    public static boolean waterBreathing(Level level, Player player, ItemStack itemStack) {
        if (player.isInWater() && player.getAirSupply() < (0.5 * player.getMaxAirSupply())) {
            player.setAirSupply(player.getMaxAirSupply());
            Helpers.damageTool(itemStack, player, Ability.WATERBREATHING);
            player.playNotifySound(SoundEvents.PLAYER_BREATH, SoundSource.PLAYERS, .5F, 1.0F);
        }
        return false;
    }

    public static boolean jumpBoost(Level level, Player player, ItemStack itemStack) {
        if (!player.isInWaterOrBubble() && !player.isFallFlying()) {
            float speed = (float) ToggleableTool.getToolValue(itemStack, Ability.JUMPBOOST.getName()) / 7.5f;
            player.moveRelative(speed, new Vec3(0, 1, 0));
        }
        return false;
    }

    public static boolean extinguish(Level level, Player player, ItemStack itemStack) {
        if (level.isClientSide) return false;
        if (player.isOnFire()) {
            int currentCooldown = ToggleableTool.getAnyCooldown(itemStack, Ability.EXTINGUISH);
            if (currentCooldown != -1) return false;
            if (itemStack.getItem() instanceof ToggleableTool toggleableTool && toggleableTool.canUseAbilityAndDurability(itemStack, Ability.EXTINGUISH)) {
                AbilityParams abilityParams = toggleableTool.getAbilityParams(Ability.EXTINGUISH);
                ToggleableTool.addCooldown(itemStack, Ability.EXTINGUISH, abilityParams.cooldown, false);
                player.clearFire();
                player.playNotifySound(SoundEvents.LAVA_EXTINGUISH, SoundSource.PLAYERS, .5F, 1.0F);
                ((ServerLevel) level).sendParticles(ParticleTypes.SOUL_FIRE_FLAME, player.getX(), player.getY(), player.getZ(), 20, 0.5, 1.5, 0.5, 0);
                Helpers.damageTool(itemStack, player, Ability.EXTINGUISH);
            }
        }
        return false;
    }

    public static boolean invulnerability(Level level, Player player, ItemStack itemStack) {
        if (level.isClientSide) return false;
        int currentCooldown = ToggleableTool.getAnyCooldown(itemStack, Ability.INVULNERABILITY);
        if (currentCooldown != -1) return false;
        if (itemStack.getItem() instanceof ToggleableTool toggleableTool && toggleableTool.canUseAbilityAndDurability(itemStack, Ability.INVULNERABILITY)) {
            AbilityParams abilityParams = toggleableTool.getAbilityParams(Ability.INVULNERABILITY);
            ToggleableTool.addCooldown(itemStack, Ability.INVULNERABILITY, abilityParams.activeCooldown, true);
            player.playNotifySound(SoundEvents.CONDUIT_ACTIVATE, SoundSource.PLAYERS, 1.0F, 1.0F);
            Helpers.damageTool(itemStack, player, Ability.INVULNERABILITY);
        }
        return false;
    }

    public static boolean stupefy(Level level, Player player, ItemStack itemStack) {
        if (level.isClientSide) return false;
        int currentCooldown = ToggleableTool.getAnyCooldown(itemStack, Ability.STUPEFY);
        if (currentCooldown != -1) return false;
        if (itemStack.getItem() instanceof ToggleableTool toggleableTool && toggleableTool.canUseAbilityAndDurability(itemStack, Ability.STUPEFY)) {
            Entity entity = MiscTools.getEntityLookedAt(player, 32);
            if (entity instanceof Mob mob) {
                addStupefyTarget(itemStack, entity.getStringUUID());
                mob.setTarget(null);
                AbilityParams abilityParams = toggleableTool.getAbilityParams(Ability.STUPEFY);
                ToggleableTool.addCooldown(itemStack, Ability.STUPEFY, abilityParams.activeCooldown, true);
                player.playNotifySound(SoundEvents.ILLUSIONER_CAST_SPELL, SoundSource.PLAYERS, 0.5F, 0.75F);
                ((ServerLevel) level).sendParticles(ParticleTypes.WHITE_SMOKE, mob.getX(), mob.getEyeY(), mob.getZ(), 20, 0.25, 0.2, 0.25, 0);
                Helpers.damageTool(itemStack, player, Ability.STUPEFY);
            }
        }
        return false;
    }

    public static boolean groundstomp(Level level, Player player, ItemStack itemStack) {
        if (level.isClientSide) return false;
        int currentCooldown = ToggleableTool.getAnyCooldown(itemStack, Ability.GROUNDSTOMP);
        if (currentCooldown != -1) return false;
        if (itemStack.getItem() instanceof ToggleableTool toggleableTool && toggleableTool.canUseAbilityAndDurability(itemStack, Ability.GROUNDSTOMP)) {
            AbilityParams abilityParams = toggleableTool.getAbilityParams(Ability.GROUNDSTOMP);
            ToggleableTool.addCooldown(itemStack, Ability.GROUNDSTOMP, abilityParams.cooldown, false);
            int radius = 3;
            AABB aabb = new AABB(player.getX() - radius, player.getY() - radius, player.getZ() - radius,
                    player.getX() + radius, player.getY() + radius, player.getZ() + radius);

            List<Mob> stompList = new ArrayList<>(level.getEntitiesOfClass(Mob.class, aabb, AbilityMethods::isValidStompEntity));
            double strength = ToggleableTool.getToolValue(itemStack, Ability.GROUNDSTOMP.getName());

            for (Mob mob : stompList) {
                double dx = mob.getX() - player.getX();
                double dz = mob.getZ() - player.getZ();
                double distance = Math.sqrt(dx * dx + dz * dz);

                // Normalize the direction vector and apply knockback
                if (distance != 0) {
                    dx /= distance;
                    dz /= distance;
                    mob.knockback(strength, -dx, -dz);
                }
            }
            player.playNotifySound(SoundEvents.MACE_SMASH_GROUND, SoundSource.PLAYERS, .5F, 1.0F);
            ((ServerLevel) level).sendParticles(ParticleTypes.DUST_PLUME, player.getX(), player.getY(), player.getZ(), 20, 0.5, 0.2, 0.5, 0);
            Helpers.damageTool(itemStack, player, Ability.GROUNDSTOMP);
        }
        return false;
    }

    public static boolean decoy(Level level, Player player, ItemStack itemStack) {
        if (level.isClientSide) return false;
        int currentCooldown = ToggleableTool.getAnyCooldown(itemStack, Ability.DECOY);
        if (currentCooldown != -1) return false;
        if (itemStack.getItem() instanceof ToggleableTool toggleableTool && toggleableTool.canUseAbilityAndDurability(itemStack, Ability.DECOY)) {
            AbilityParams abilityParams = toggleableTool.getAbilityParams(Ability.DECOY);
            DecoyEntity decoy = new DecoyEntity(level);
            decoy.setPos(player.position());
            decoy.setSummonerName(player.getName().getString());
            decoy.setOwnerUUID(player.getUUID());
            level.addFreshEntity(decoy);
            ToggleableTool.addCooldown(itemStack, Ability.DECOY, abilityParams.activeCooldown, true);
            player.playNotifySound(SoundEvents.EVOKER_PREPARE_SUMMON, SoundSource.PLAYERS, 1.0F, 1.0F);
            Helpers.damageTool(itemStack, player, Ability.DECOY);

        }
        return false;
    }

    public static boolean debuffRemover(Level level, Player player, ItemStack itemStack) {
        if (level.isClientSide) return false;
        int currentCooldown = ToggleableTool.getAnyCooldown(itemStack, Ability.DEBUFFREMOVER);
        if (currentCooldown != -1) return false;
        if (itemStack.getItem() instanceof ToggleableTool toggleableTool && toggleableTool.canUseAbilityAndDurability(itemStack, Ability.DEBUFFREMOVER)) {
            AbilityParams abilityParams = toggleableTool.getAbilityParams(Ability.DEBUFFREMOVER);
            ToggleableTool.addCooldown(itemStack, Ability.DEBUFFREMOVER, abilityParams.cooldown, false);
            player.playNotifySound(SoundEvents.WANDERING_TRADER_DRINK_MILK, SoundSource.PLAYERS, 1.0F, 1.0F);
            List<Holder<MobEffect>> negativeEffects = new ArrayList<>();
            for (Holder<MobEffect> mobEffect : player.getActiveEffectsMap().keySet()) {
                if (mobEffect.value().getCategory() == MobEffectCategory.HARMFUL)
                    negativeEffects.add(mobEffect);
            }
            for (Holder<MobEffect> mobEffectHolder : negativeEffects) {
                if (toggleableTool.canUseAbilityAndDurability(itemStack, Ability.DEBUFFREMOVER)) {
                    player.removeEffect(mobEffectHolder);
                    Helpers.damageTool(itemStack, player, Ability.DEBUFFREMOVER);
                }
            }
        }
        return false;
    }

    public static boolean earthquake(Level level, Player player, ItemStack itemStack) {
        if (level.isClientSide) return false;
        int currentCooldown = ToggleableTool.getAnyCooldown(itemStack, Ability.EARTHQUAKE);
        if (currentCooldown != -1) return false;
        if (itemStack.getItem() instanceof ToggleableTool toggleableTool && toggleableTool.canUseAbilityAndDurability(itemStack, Ability.EARTHQUAKE)) {
            AbilityParams abilityParams = toggleableTool.getAbilityParams(Ability.EARTHQUAKE);
            ToggleableTool.addCooldown(itemStack, Ability.EARTHQUAKE, abilityParams.activeCooldown, true);
            player.playNotifySound(SoundEvents.MACE_SMASH_GROUND_HEAVY, SoundSource.PLAYERS, 1.0F, 0.5F);
            int radius = 5;
            AABB aabb = new AABB(player.getX() - radius, player.getY() - radius, player.getZ() - radius,
                    player.getX() + radius, player.getY() + radius, player.getZ() + radius);

            List<Mob> earthquakeList = new ArrayList<>(level.getEntitiesOfClass(Mob.class, aabb, AbilityMethods::isValidEarthquake));

            for (Mob mob : earthquakeList) {
                if (toggleableTool.canUseAbilityAndDurability(itemStack, Ability.EARTHQUAKE)) {
                    mob.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 3), mob);
                    ((ServerLevel) level).sendParticles(ParticleTypes.END_ROD, mob.getX(), mob.getY(), mob.getZ(), 20, 0.25, 0.2, 0.25, 0);
                    ((ServerLevel) level).sendParticles(ParticleTypes.ENCHANT, mob.getX(), mob.getY(), mob.getZ(), 20, 0.5, 0.2, 0.5, 0);
                    Helpers.damageTool(itemStack, player, Ability.EARTHQUAKE);
                }
            }
        }
        return false;
    }

    public static boolean noAI(Level level, Player player, ItemStack itemStack) {
        if (level.isClientSide) return false;
        int currentCooldown = ToggleableTool.getAnyCooldown(itemStack, Ability.NOAI);
        if (currentCooldown != -1) return false;
        if (itemStack.getItem() instanceof ToggleableTool toggleableTool && toggleableTool.canUseAbilityAndDurability(itemStack, Ability.NOAI)) {
            AbilityParams abilityParams = toggleableTool.getAbilityParams(Ability.NOAI);
            ToggleableTool.addCooldown(itemStack, Ability.NOAI, abilityParams.cooldown, false);
            int radius = 5;
            AABB aabb = new AABB(player.getX() - radius, player.getY() - radius, player.getZ() - radius,
                    player.getX() + radius, player.getY() + radius, player.getZ() + radius);

            List<Mob> AIList = new ArrayList<>(level.getEntitiesOfClass(Mob.class, aabb, AbilityMethods::isValidNOAIEntity));

            for (Mob mob : AIList) {
                if (toggleableTool.canUseAbilityAndDurability(itemStack, Ability.NOAI)) {
                    mob.setNoAi(true);
                    ((ServerLevel) level).sendParticles(ParticleTypes.END_ROD, mob.getX(), mob.getEyeY(), mob.getZ(), 20, 0.25, 0.2, 0.25, 0);
                    ((ServerLevel) level).sendParticles(ParticleTypes.ENCHANT, mob.getX(), mob.getEyeY(), mob.getZ(), 20, 0.5, 0.2, 0.5, 0);
                    Helpers.damageTool(itemStack, player, Ability.NOAI);
                }
            }
            player.playNotifySound(SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 1F, 0.5F);
            player.playNotifySound(SoundEvents.SCULK_SHRIEKER_SHRIEK, SoundSource.PLAYERS, 1F, 0.25F);
        }
        return false;
    }

    public static boolean epicArrow(Level level, Player player, ItemStack itemStack) {
        if (level.isClientSide) return false;
        int currentCooldown = ToggleableTool.getAnyCooldown(itemStack, Ability.EPICARROW);
        if (currentCooldown != -1) return false;
        if (itemStack.getItem() instanceof ToggleableTool toggleableTool && toggleableTool.canUseAbilityAndDurability(itemStack, Ability.EPICARROW) && !itemStack.getOrDefault(JustDireDataComponents.EPIC_ARROW, false)) {
            itemStack.set(JustDireDataComponents.EPIC_ARROW, true);
            player.playNotifySound(SoundEvents.EVOKER_PREPARE_SUMMON, SoundSource.PLAYERS, 1F, 0.5F);
            Helpers.damageTool(itemStack, player, Ability.EPICARROW);
        }
        return false;
    }

    public static boolean flight(Level level, Player player, ItemStack itemStack) {
        if (level.isClientSide) return false;
        if (player.getAbilities().flying)
            Helpers.damageTool(itemStack, player, Ability.FLIGHT);
        return false;
    }

    public static boolean isValidStompEntity(Entity entity) {
        if (entity.isMultipartEntity())
            return false;
        if (entity instanceof PartEntity<?>)
            return false;
        return true;
    }

    public static boolean isValidNOAIEntity(Entity entity) {
        if (entity.isMultipartEntity())
            return false;
        if (entity instanceof PartEntity<?>)
            return false;
        if (entity.getType().is(JustDireEntityTags.NO_AI_DENY))
            return false;
        return true;
    }

    public static boolean isValidEarthquake(Entity entity) {
        if (!entity.onGround())
            return false;
        if (entity.isMultipartEntity())
            return false;
        if (entity instanceof PartEntity<?>)
            return false;
        if (entity.getType().is(JustDireEntityTags.NO_EARTHQUAKE))
            return false;
        return true;
    }

    public static List<String> getStupefyTargets(ItemStack itemStack) {
        return itemStack.getOrDefault(JustDireDataComponents.STUPEFY_TARGETS.get(), new ArrayList<>());
    }

    public static void addStupefyTarget(ItemStack itemStack, String entityUUID) {
        List<String> stupefyTargets = new ArrayList<>(getStupefyTargets(itemStack));
        stupefyTargets.add(entityUUID);
        itemStack.set(JustDireDataComponents.STUPEFY_TARGETS, stupefyTargets);
    }

    public static void clearStupefyTargets(ItemStack itemStack) {
        itemStack.set(JustDireDataComponents.STUPEFY_TARGETS, new ArrayList<>());
    }

}
