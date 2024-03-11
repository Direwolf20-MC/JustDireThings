package com.direwolf20.justdirethings.common.items.tools.utils;

import com.direwolf20.justdirethings.client.renderactions.ThingFinder;
import com.direwolf20.justdirethings.common.containers.ToolSettingContainer;
import com.direwolf20.justdirethings.common.events.BlockToolModification;
import com.direwolf20.justdirethings.datagen.JustDireBlockTags;
import com.direwolf20.justdirethings.util.MiningCollect;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.direwolf20.justdirethings.common.items.tools.utils.Helpers.*;

public interface ToggleableTool {
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
        return hasAbility(toolAbility) && getEnabled(itemStack) && getSetting(itemStack, toolAbility.name);
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

    default void mineBlocksAbility(ItemStack pStack, Level pLevel, BlockPos pPos, LivingEntity pEntityLiving) {
        BlockState pState = pLevel.getBlockState(pPos);
        Set<BlockPos> breakBlockPositions = new HashSet<>();
        List<ItemStack> drops = new ArrayList<>();
        int totalExp = 0;
        int fortuneLevel = pEntityLiving.getMainHandItem().getEnchantmentLevel(Enchantments.BLOCK_FORTUNE);
        int silkTouchLevel = pEntityLiving.getMainHandItem().getEnchantmentLevel(Enchantments.SILK_TOUCH);
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
        BlockToolModification.addAllToIgnoreList(breakBlockPositions); //All these blocks to the list of blocks we ignore in the BlockBreakEvent
        for (BlockPos breakPos : breakBlockPositions) {
            if (testUseTool(pStack) < 0)
                break;
            int exp = pLevel.getBlockState(breakPos).getExpDrop(pLevel, pLevel.random, pPos, fortuneLevel, silkTouchLevel);
            totalExp = totalExp + exp;
            Helpers.combineDrops(drops, breakBlocks((ServerLevel) pLevel, breakPos, pEntityLiving, pStack));
        }
        if (canUseAbility(pStack, Ability.SMELTER) && pStack.getDamageValue() < pStack.getMaxDamage()) {
            boolean[] smeltedItemsFlag = new boolean[1]; // Array to hold the smelting flag
            drops = smeltDrops((ServerLevel) pLevel, drops, pStack, pEntityLiving, smeltedItemsFlag);
            if (smeltedItemsFlag[0])
                smelterParticles((ServerLevel) pLevel, breakBlockPositions);
        }
        if (!drops.isEmpty()) {
            Helpers.dropDrops(drops, (ServerLevel) pLevel, pPos);
            pState.getBlock().popExperience((ServerLevel) pLevel, pPos, totalExp);
        }
    }

    default void smelterParticles(ServerLevel level, Set<BlockPos> oreBlocksList) {
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            for (BlockPos pos : oreBlocksList) {
                double d0 = (double) pos.getX() + random.nextDouble();
                double d1 = (double) pos.getY() + random.nextDouble();
                double d2 = (double) pos.getZ() + random.nextDouble();
                level.sendParticles(ParticleTypes.FLAME, d0, d1, d2, 1, 0.0, 0.0, 0.0, 0);
            }
        }
    }

    default boolean scanFor(Level level, Player player, InteractionHand hand, Ability toolAbility) {
        if (!player.isShiftKeyDown()) {
            ItemStack itemStack = player.getItemInHand(hand);
            if (canUseAbility(itemStack, toolAbility) && (testUseTool(itemStack, toolAbility) >= 0)) {
                if (level.isClientSide) {
                    if (itemStack.getItem() instanceof TieredGooItem tieredGooItem) {
                        ThingFinder.discover(player, tieredGooItem.getGooTier(), toolAbility);
                    }
                } else { //ServerSide
                    damageTool(itemStack, player, toolAbility);
                }
            }
        }
        return false;
    }

    default boolean leafbreaker(UseOnContext pContext) {
        Level pLevel = pContext.getLevel();
        BlockPos pPos = pContext.getClickedPos();
        BlockState pState = pLevel.getBlockState(pPos);
        LivingEntity pEntityLiving = pContext.getPlayer();
        ItemStack pStack = pContext.getItemInHand();
        if (!pLevel.isClientSide && canUseAbility(pStack, Ability.LEAFBREAKER)) { //TODO MineBlocks Method Above?
            if (pState.getTags().anyMatch(tag -> tag.equals(BlockTags.LEAVES))) {
                Set<BlockPos> alsoBreakSet = findLikeBlocks(pLevel, pState, pPos, null, 64, 2); //Todo: Balance and Config?
                List<ItemStack> drops = new ArrayList<>();
                for (BlockPos breakPos : alsoBreakSet) {
                    if (testUseTool(pStack, Ability.LEAFBREAKER) < 0)
                        break;
                    Helpers.combineDrops(drops, breakBlocks((ServerLevel) pLevel, breakPos, pEntityLiving, pStack));
                    pLevel.sendBlockUpdated(breakPos, pState, pLevel.getBlockState(breakPos), 3); // I have NO IDEA why this is necessary
                    if (Math.random() < 0.1) //10% chance to damage tool
                        damageTool(pStack, pEntityLiving, Ability.LEAFBREAKER);
                }
                if (!drops.isEmpty()) {
                    Helpers.dropDrops(drops, (ServerLevel) pLevel, pPos);
                }
                return true;
            }
        }
        return false;
    }

    default boolean lawnmower(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
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

    static boolean setSetting(ItemStack stack, String setting, boolean value) {
        CompoundTag tagCompound = stack.getOrCreateTag();
        tagCompound.putBoolean(setting, value);
        return tagCompound.getBoolean(setting);
    }

    static boolean getSetting(ItemStack stack, String setting) {
        CompoundTag tagCompound = stack.getOrCreateTag();
        return !tagCompound.contains(setting) || tagCompound.getBoolean(setting); //Enabled by default
    }

    static boolean getEnabled(ItemStack stack) {
        return getSetting(stack, "enabled");
    }

    static boolean toggleEnabled(ItemStack stack) {
        return toggleSetting(stack, "enabled");
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
        return Math.max(min, Math.min(max, stack.getOrCreateTag().getInt(valueName + "_value")));
    }
}
