package com.direwolf20.justdirethings.common.items.tools.utils;

import com.direwolf20.justdirethings.client.renderactions.ThingFinder;
import com.direwolf20.justdirethings.common.blockentities.GooSoilBE;
import com.direwolf20.justdirethings.common.blocks.soil.GooSoilBase;
import com.direwolf20.justdirethings.common.containers.ToolSettingContainer;
import com.direwolf20.justdirethings.common.items.interfaces.ToggleableItem;
import com.direwolf20.justdirethings.datagen.JustDireBlockTags;
import com.direwolf20.justdirethings.util.MiningCollect;
import com.direwolf20.justdirethings.util.MiscHelpers;
import com.direwolf20.justdirethings.util.NBTHelpers;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.direwolf20.justdirethings.common.items.tools.utils.Helpers.*;

public interface ToggleableTool extends ToggleableItem {
    GooTier gooTier();

    default int getGooTier() {
        return gooTier().getGooTier();
    }

    EnumSet<Ability> getAbilities();

    Map<Ability, AbilityParams> getAbilityParamsMap();

    default AbilityParams getAbilityParams(Ability toolAbility) {
        return getAbilityParamsMap().getOrDefault(toolAbility, new AbilityParams(-1, -1, -1));
    }

    default void registerAbility(Ability ability) {
        getAbilities().add(ability);
    }

    default void registerAbility(Ability ability, AbilityParams abilityParams) {
        getAbilities().add(ability);
        getAbilityParamsMap().put(ability, abilityParams);
    }

    default boolean hasAbility(Ability ability) {
        return getAbilities().contains(ability);
    }

    default boolean canUseAbility(ItemStack itemStack, Ability toolAbility) {
        return hasAbility(toolAbility) && getEnabled(itemStack) && getSetting(itemStack, toolAbility.getName());
    }

    default boolean canUseAbilityAndDurabiltiy(ItemStack itemStack, Ability toolAbility) {
        return canUseAbility(itemStack, toolAbility) && (testUseTool(itemStack, toolAbility) > 0);
    }

    default boolean canUseAbilityAndDurabiltiy(ItemStack itemStack, Ability toolAbility, int multiplier) {
        return canUseAbility(itemStack, toolAbility) && (testUseTool(itemStack, toolAbility, multiplier) > 0);
    }

    default void openSettings(Player player) {
        player.openMenu(new SimpleMenuProvider(
                (windowId, playerInventory, playerEntity) -> new ToolSettingContainer(windowId, playerInventory, player), Component.translatable("")));

    }

    //Abilities
    default boolean hurtEnemyAbility(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        damageTool(pStack, pAttacker);
        return true;
    }

    default Set<BlockPos> getBreakBlockPositions(ItemStack pStack, Level pLevel, BlockPos pPos, LivingEntity pEntityLiving, BlockState pState) {
        Set<BlockPos> breakBlockPositions = new HashSet<>();
        if (canUseAbility(pStack, Ability.OREMINER) && oreCondition.test(pState) && pStack.isCorrectToolForDrops(pState)) {
            breakBlockPositions.addAll(findLikeBlocks(pLevel, pState, pPos, null, 64, 2)); //Todo: Balance and Config?
        }
        if (canUseAbility(pStack, Ability.TREEFELLER) && logCondition.test(pState) && pStack.isCorrectToolForDrops(pState)) {
            breakBlockPositions.addAll(findLikeBlocks(pLevel, pState, pPos, null, 64, 2)); //Todo: Balance and Config?
        }
        if (canUseAbility(pStack, Ability.SKYSWEEPER) && fallingBlockCondition.test(pState) && pStack.isCorrectToolForDrops(pState)) {
            breakBlockPositions.addAll(findLikeBlocks(pLevel, pState, pPos, Direction.UP, 64, 2)); //Todo: Balance and Config?
        }
        if (canUseAbility(pStack, Ability.HAMMER)) {
            breakBlockPositions.addAll(MiningCollect.collect(pEntityLiving, pPos, getTargetLookDirection(pEntityLiving), pLevel, getToolValue(pStack, Ability.HAMMER.getName()), MiningCollect.SizeMode.AUTO, pStack));
        }
        breakBlockPositions.add(pPos);
        return breakBlockPositions;
    }

    default boolean canInstaBreak(ItemStack pStack, Level pLevel, Set<BlockPos> breakBlockPositions) {
        boolean instaBreak = false;
        if (canUseAbility(pStack, Ability.INSTABREAK)) { //Only Instabreak if we can instabreak ALL blocks in the area!
            float cumulativeDestroy = 0;
            for (BlockPos pos : breakBlockPositions) {
                BlockState blockState = pLevel.getBlockState(pos);
                float destroySpeedTarget = blockState.getDestroySpeed(pLevel, pos);
                cumulativeDestroy = cumulativeDestroy + destroySpeedTarget;
            }
            int rfCostInstaBreak = getInstantRFCost(cumulativeDestroy);
            System.out.println(rfCostInstaBreak);
            if (testUseTool(pStack, rfCostInstaBreak) > 0)
                instaBreak = true;
        }
        return instaBreak;
    }

    default void mineBlocksAbility(ItemStack pStack, Level pLevel, BlockPos pPos, LivingEntity pEntityLiving) {
        BlockState pState = pLevel.getBlockState(pPos);
        List<ItemStack> drops = new ArrayList<>();
        int totalExp = 0;
        int fortuneLevel = pEntityLiving.getMainHandItem().getEnchantmentLevel(Enchantments.BLOCK_FORTUNE);
        int silkTouchLevel = pEntityLiving.getMainHandItem().getEnchantmentLevel(Enchantments.SILK_TOUCH);
        Set<BlockPos> breakBlockPositions = getBreakBlockPositions(pStack, pLevel, pPos, pEntityLiving, pState);
        boolean instaBreak = canInstaBreak(pStack, pLevel, breakBlockPositions);
        for (BlockPos breakPos : breakBlockPositions) {
            if (testUseTool(pStack) < 0)
                break;
            int exp = pLevel.getBlockState(breakPos).getExpDrop(pLevel, pLevel.random, pPos, fortuneLevel, silkTouchLevel);
            totalExp = totalExp + exp;
            Helpers.combineDrops(drops, breakBlocks(pLevel, breakPos, pEntityLiving, pStack, true, instaBreak));
        }
        if (!pLevel.isClientSide)
            handleDrops(pStack, (ServerLevel) pLevel, pPos, pEntityLiving, breakBlockPositions, drops, pState, totalExp);
    }

    default void handleDrops(ItemStack pStack, ServerLevel serverLevel, BlockPos pPos, LivingEntity pEntityLiving, Set<BlockPos> breakBlockPositions, List<ItemStack> drops, BlockState pState, int totalExp) {
        if (canUseAbility(pStack, Ability.SMELTER) && pStack.getDamageValue() < pStack.getMaxDamage()) {
            boolean[] smeltedItemsFlag = new boolean[1]; // Array to hold the smelting flag
            drops = smeltDrops(serverLevel, drops, pStack, pEntityLiving, smeltedItemsFlag);
            if (smeltedItemsFlag[0])
                smelterParticles(serverLevel, breakBlockPositions);
        }
        if (!drops.isEmpty() && canUseAbility(pStack, Ability.DROPTELEPORT)) {
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

    static int getInstantRFCost(float cumulativeDestroy) {
        return Math.max(Ability.INSTABREAK.getFeCost(), Ability.INSTABREAK.getFeCost() * (int) cumulativeDestroy);
    }

    default void smelterParticles(ServerLevel level, Set<BlockPos> oreBlocksList) {
        Random random = new Random();
        int iterations = oreBlocksList.size() > 10 ? 1 : 5;
        for (int i = 0; i < iterations; i++) {
            for (BlockPos pos : oreBlocksList) {
                double d0 = (double) pos.getX() + random.nextDouble();
                double d1 = (double) pos.getY() + random.nextDouble();
                double d2 = (double) pos.getZ() + random.nextDouble();
                level.sendParticles(ParticleTypes.FLAME, d0, d1, d2, 1, 0.0, 0.0, 0.0, 0);
            }
        }
    }

    static void teleportParticles(ServerLevel level, Vec3 pos) {
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            double d0 = pos.x() + random.nextDouble();
            double d1 = pos.y() - 0.5d + random.nextDouble();
            double d2 = pos.z() + random.nextDouble();
            level.sendParticles(ParticleTypes.PORTAL, d0, d1, d2, 1, 0.0, 0.0, 0.0, 0);
        }
    }

    static void teleportParticles(ServerLevel level, BlockPos pos, int iterations) {
        Random random = new Random();
                /*// Generate random positions within the block
                double xOffset = random.nextDouble(); // Random offset within the block
                double yOffset = random.nextDouble(); // Random offset within the block
                double zOffset = random.nextDouble(); // Random offset within the block

                // Calculate the position to spawn each particle, adding random offset to the block's position
                double spawnX = pos.getX() + xOffset;
                double spawnY = pos.getY() + yOffset;
                double spawnZ = pos.getZ() + zOffset;

                // Calculate velocity to make the particle move towards the center of the block
                // Since we want them to collapse to the center, we calculate the difference from the center (0.5 offset) and use a negative multiplier
                double xVelocity = (0.5 - xOffset) * 0.2; // Adjust multiplier for speed
                double yVelocity = (0.5 - yOffset) * 0.2; // Adjust multiplier for speed
                double zVelocity = (0.5 - zOffset) * 0.2; // Adjust multiplier for speed

                // Spawn a particle with calculated velocity to move it towards the center of the block
                level.sendParticles(ParticleTypes.PORTAL, spawnX, spawnY, spawnZ, 1, xVelocity, yVelocity, zVelocity, 0.0);
                */
        for (int i = 0; i < iterations; i++) {
            double d0 = (double) pos.getX() + random.nextDouble();
            double d1 = (double) pos.getY() - 0.5d + random.nextDouble();
            double d2 = (double) pos.getZ() + random.nextDouble();
            level.sendParticles(ParticleTypes.PORTAL, d0, d1, d2, 1, 0.0, 0.0, 0.0, 0);
        }
    }

    static void teleportParticles(ServerLevel level, Set<BlockPos> oreBlocksList) {
        int iterations = oreBlocksList.size() > 10 ? 1 : 5;
        for (BlockPos pos : oreBlocksList) {
            teleportParticles(level, pos, iterations);
        }
    }

    default void useAbility(Level level, Player player, InteractionHand hand) {
        if (player.isShiftKeyDown()) return;
        ItemStack itemStack = player.getItemInHand(hand);
        if (canUseAbilityAndDurabiltiy(itemStack, Ability.MOBSCANNER))
            scanFor(level, player, itemStack, Ability.MOBSCANNER);
        if (canUseAbilityAndDurabiltiy(itemStack, Ability.GLOWING))
            glowing(level, player, itemStack, Ability.GLOWING);
        if (canUseAbilityAndDurabiltiy(itemStack, Ability.ORESCANNER))
            scanFor(level, player, itemStack, Ability.ORESCANNER);
        if (canUseAbilityAndDurabiltiy(itemStack, Ability.OREXRAY))
            scanFor(level, player, itemStack, Ability.OREXRAY);
        if (canUseAbilityAndDurabiltiy(itemStack, Ability.LAWNMOWER))
            lawnmower(level, player, itemStack);
    }

    default void useOnAbility(UseOnContext pContext) {
        if (pContext.getPlayer().isShiftKeyDown()) return;
        ItemStack itemStack = pContext.getItemInHand();
        if (canUseAbilityAndDurabiltiy(itemStack, Ability.LEAFBREAKER))
            leafbreaker(pContext);
    }

    default void glowing(Level level, Player player, ItemStack itemStack, Ability toolAbility) {
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
        damageTool(itemStack, player, toolAbility);
    }

    default boolean scanFor(Level level, Player player, ItemStack itemStack, Ability toolAbility) {
        if (level.isClientSide) {
            ThingFinder.discover(player, toolAbility);
            if (toolAbility.equals(Ability.OREXRAY))
                player.playNotifySound(SoundEvents.SCULK_CLICKING, SoundSource.PLAYERS, 1.0F, 1.0F);
            else
                player.playNotifySound(SoundEvents.END_PORTAL_FRAME_FILL, SoundSource.PLAYERS, 1.0F, 1.0F);
        } else { //ServerSide
            damageTool(itemStack, player, toolAbility);
        }
        return false;
    }

    default boolean leafbreaker(UseOnContext pContext) {
        Level pLevel = pContext.getLevel();
        BlockPos pPos = pContext.getClickedPos();
        BlockState pState = pLevel.getBlockState(pPos);
        LivingEntity pEntityLiving = pContext.getPlayer();
        ItemStack pStack = pContext.getItemInHand();
        if (pState.getTags().anyMatch(tag -> tag.equals(BlockTags.LEAVES))) {
            Set<BlockPos> alsoBreakSet = findLikeBlocks(pLevel, pState, pPos, null, 64, 2); //Todo: Balance and Config?
            List<ItemStack> drops = new ArrayList<>();
            for (BlockPos breakPos : alsoBreakSet) {
                if (testUseTool(pStack, Ability.LEAFBREAKER) < 0)
                    break;
                Helpers.combineDrops(drops, breakBlocks(pLevel, breakPos, pEntityLiving, pStack, false, false));
                pLevel.sendBlockUpdated(breakPos, pState, pLevel.getBlockState(breakPos), 3); // I have NO IDEA why this is necessary
                if (Math.random() < 0.1) //10% chance to damage tool
                    damageTool(pStack, pEntityLiving, Ability.LEAFBREAKER);
            }
            if (!pLevel.isClientSide)
                handleDrops(pStack, (ServerLevel) pLevel, pPos, pEntityLiving, alsoBreakSet, drops, pState, 0);
            return true;
        }
        return false;
    }

    default boolean lawnmower(Level level, Player player, ItemStack itemStack) {
        if (!level.isClientSide && canUseAbility(itemStack, Ability.LAWNMOWER)) {
            List<TagKey<Block>> tags = new ArrayList<>();
            tags.add(JustDireBlockTags.LAWNMOWERABLE);
            Set<BlockPos> breakBlocks = findTaggedBlocks(level, tags, player.getOnPos(), 64, 5); //TODO Balance/Config?
            for (BlockPos breakPos : breakBlocks) {
                if (testUseTool(itemStack, Ability.LAWNMOWER) < 0)
                    break;
                breakBlocks((ServerLevel) level, breakPos);
                if (Math.random() < 0.1) //10% chance to damage tool
                    damageTool(itemStack, player, Ability.LAWNMOWER);
            }
            return true;
        }
        return false;
    }

    default boolean bindDrops(UseOnContext pContext) {
        Player player = pContext.getPlayer();
        if (player == null) return false;
        if (!player.isShiftKeyDown()) return false;
        ItemStack pStack = pContext.getItemInHand();
        if (!(pStack.getItem() instanceof ToggleableTool toggleableTool)) return false;
        if (!toggleableTool.hasAbility(Ability.DROPTELEPORT)) return false;
        Level pLevel = pContext.getLevel();
        BlockPos pPos = pContext.getClickedPos();
        BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
        if (blockEntity == null) return false;
        IItemHandler handler = pLevel.getCapability(Capabilities.ItemHandler.BLOCK, pPos, pContext.getClickedFace());
        if (handler == null) return false;
        setBoundInventory(pStack, new NBTHelpers.BoundInventory(GlobalPos.of(pLevel.dimension(), pPos), pContext.getClickedFace()));
        pContext.getPlayer().displayClientMessage(Component.translatable("justdirethings.boundto", I18n.get(pLevel.dimension().location().getPath()), "[" + pPos.toShortString() + "]"), true);
        player.playNotifySound(SoundEvents.END_PORTAL_FRAME_FILL, SoundSource.PLAYERS, 1.0F, 1.0F);
        return true;
    }

    default boolean bindSoil(UseOnContext pContext) {
        boolean bindingSuccess = false;
        if (!pContext.getPlayer().level().isClientSide) {
            Level pLevel = pContext.getLevel();
            Player player = pContext.getPlayer();
            ItemStack heldItem = pContext.getItemInHand();
            BlockPos clickedPos = pContext.getClickedPos();
            BlockState blockState = pLevel.getBlockState(clickedPos);
            if (blockState.getBlock() instanceof GooSoilBase) {
                if (heldItem.getItem() instanceof ToggleableTool toggleableTool) {
                    if (toggleableTool.canUseAbility(heldItem, Ability.DROPTELEPORT)) {
                        if (Helpers.testUseTool(heldItem, Ability.DROPTELEPORT, 10) > 0) {
                            NBTHelpers.BoundInventory boundInventory = ToggleableTool.getBoundInventory(heldItem);
                            if (boundInventory != null) {
                                BlockEntity blockEntity = pLevel.getBlockEntity(clickedPos);
                                if (blockEntity instanceof GooSoilBE gooSoilBE) {
                                    gooSoilBE.bindInventory(boundInventory);
                                    pContext.getPlayer().displayClientMessage(Component.translatable("justdirethings.boundto", Component.translatable(boundInventory.globalPos().dimension().location().getPath()), "[" + boundInventory.globalPos().pos().toShortString() + "]"), true);
                                    player.playNotifySound(SoundEvents.ENDER_EYE_DEATH, SoundSource.PLAYERS, 1.0F, 1.0F);
                                    Helpers.damageTool(heldItem, player, Ability.DROPTELEPORT, 10);
                                    bindingSuccess = true;
                                }
                            }
                        }
                        if (!bindingSuccess) {
                            pContext.getPlayer().displayClientMessage(Component.translatable("justdirethings.bindfailed").withStyle(ChatFormatting.RED), true);
                            player.playNotifySound(SoundEvents.ANVIL_BREAK, SoundSource.PLAYERS, 1.0F, 1.0F);
                        }
                    }
                }
            }
        }
        return bindingSuccess;
    }

    //Thanks Soaryn!
    @NotNull
    static Direction getTargetLookDirection(LivingEntity livingEntity) {
        var playerLook = new Vec3(livingEntity.getX(), livingEntity.getY() + livingEntity.getEyeHeight(), livingEntity.getZ());
        var lookVec = livingEntity.getViewVector(1.0F);
        var reach = livingEntity instanceof Player player ? player.getBlockReach() : 1; //Todo check if this is good
        var endLook = playerLook.add(lookVec.x * reach, lookVec.y * reach, lookVec.z * reach);
        var hitResult = livingEntity.level().clip(new ClipContext(playerLook, endLook, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, livingEntity));
        return hitResult.getDirection().getOpposite();
    }

    static ItemStack getToggleableTool(Player player) {
        ItemStack mainHand = player.getMainHandItem();
        if (mainHand.getItem() instanceof ToggleableTool)
            return mainHand;
        ItemStack offHand = player.getOffhandItem();
        if (offHand.getItem() instanceof ToggleableTool)
            return offHand;
        return ItemStack.EMPTY;
    }

    static boolean toggleSetting(ItemStack stack, String setting) {
        CompoundTag tagCompound = stack.getOrCreateTag();
        tagCompound.putBoolean(setting, !getSetting(stack, setting));
        return tagCompound.getBoolean(setting);
    }

    /**
     * Cycle will move through each possible value of a tool ability, incrementing by the increment value.
     * When it reaches the end, it'll next disable the ability entirely. Next cycle it will re-enable it at the min value
     */
    static boolean cycleSetting(ItemStack stack, String setting) {
        Ability toolAbility = Ability.valueOf(setting.toUpperCase(Locale.ROOT));
        AbilityParams abilityParams = ((ToggleableTool) stack.getItem()).getAbilityParams(toolAbility);
        CompoundTag tagCompound = stack.getOrCreateTag();
        int currentValue = getToolValue(stack, setting);
        int nextValue = Math.min(abilityParams.maxSlider, currentValue + abilityParams.increment);
        if (nextValue == currentValue && getSetting(stack, setting)) { //If the next value is equal to the current one, its because we max'd out, so toggle it off
            setSetting(stack, setting, false);
            nextValue = abilityParams.minSlider;
        } else if (currentValue == abilityParams.minSlider && !getSetting(stack, setting)) {
            nextValue = abilityParams.minSlider;
            setSetting(stack, setting, true);
        }
        setToolValue(stack, setting, nextValue);
        return tagCompound.getBoolean(setting);
    }

    static void setBoundInventory(ItemStack stack, NBTHelpers.BoundInventory boundInventory) {
        CompoundTag tagCompound = stack.getOrCreateTag();
        tagCompound.put("boundinventory", NBTHelpers.BoundInventory.toNBT(boundInventory));
    }

    static NBTHelpers.BoundInventory getBoundInventory(ItemStack stack) {
        CompoundTag tagCompound = stack.getOrCreateTag();
        if (tagCompound.contains("boundinventory"))
            return NBTHelpers.BoundInventory.fromNBT(tagCompound.getCompound("boundinventory"));
        return null;
    }

    static IItemHandler getBoundHandler(ServerLevel serverLevel, ItemStack stack) {
        NBTHelpers.BoundInventory boundInventory = getBoundInventory(stack);
        if (boundInventory != null)
            return MiscHelpers.getAttachedInventory(serverLevel.getServer().getLevel(boundInventory.globalPos().dimension()), boundInventory.globalPos().pos(), boundInventory.direction());
        return null;
    }

    static boolean setSetting(ItemStack stack, String setting, boolean value) {
        CompoundTag tagCompound = stack.getOrCreateTag();
        tagCompound.putBoolean(setting, value);
        return tagCompound.getBoolean(setting);
    }

    static boolean getSetting(ItemStack stack, String setting) {
        CompoundTag tagCompound = stack.getOrCreateTag();
        return !tagCompound.contains(setting) || tagCompound.getBoolean(setting); //Enabled by default
    }

    @Override
    default boolean getEnabled(ItemStack stack) {
        return getSetting(stack, "enabled");
    }

    static void setToolValue(ItemStack stack, String valueName, int value) {
        Ability toolAbility = Ability.valueOf(valueName.toUpperCase(Locale.ROOT));
        AbilityParams abilityParams = ((ToggleableTool) stack.getItem()).getAbilityParams(toolAbility);
        int min = abilityParams.minSlider;
        int max = abilityParams.maxSlider;
        int setValue = Math.max(min, Math.min(max, value));
        stack.getOrCreateTag().putInt(valueName + "_value", setValue);
    }

    static int getToolValue(ItemStack stack, String valueName) {
        Ability toolAbility = Ability.valueOf(valueName.toUpperCase(Locale.ROOT));
        AbilityParams abilityParams = ((ToggleableTool) stack.getItem()).getAbilityParams(toolAbility);
        int min = abilityParams.minSlider;
        int max = abilityParams.maxSlider;
        if (stack.getOrCreateTag().contains(valueName + "_value"))
            return Math.max(min, Math.min(max, stack.getOrCreateTag().getInt(valueName + "_value")));
        return max; //By default, new tools have their max ability enabled, like hammer on celestigem starts out with 5.
    }
}
