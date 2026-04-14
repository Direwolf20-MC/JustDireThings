package com.direwolf20.justdirethings.common.items.tools.basetools;

import com.direwolf20.justdirethings.common.items.interfaces.*;
import com.direwolf20.justdirethings.setup.Config;
import com.direwolf20.justdirethings.util.MiningCollect;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BaseHoe extends HoeItem implements ToggleableTool, LeftClickableTool {
    protected final EnumSet<Ability> abilities = EnumSet.noneOf(Ability.class);
    protected final Map<Ability, AbilityParams> abilityParams = new EnumMap<>(Ability.class);

    public BaseHoe(ToolMaterial material, float attackDamageBaseline, float attackSpeedBaseline, Properties pProperties) {
        super(material, attackDamageBaseline, attackSpeedBaseline, pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        if (bindDrops(pContext))
            return InteractionResult.SUCCESS;
        ItemStack useStack = pContext.getItemInHand();
        Player player = pContext.getPlayer();
        Level level = pContext.getLevel();
        if (player != null && useStack.getItem() instanceof ToggleableTool toggleableTool && toggleableTool.canUseAbility(useStack, Ability.HAMMER)) {
            List<BlockPos> affectedBlocks = MiningCollect.collect(player, pContext.getClickedPos(), ToggleableTool.getTargetLookDirection(player), player.level(), ToggleableTool.getToolValue(useStack, Ability.HAMMER.getName()), MiningCollect.SizeMode.AUTO, useStack);
            for (BlockPos blockPos : affectedBlocks) {
                BlockState oldState = level.getBlockState(blockPos);
                UseOnContext useOnContext = new UseOnContext(pContext.getLevel(), player, pContext.getHand(), useStack, new BlockHitResult(blockPos.getCenter(), pContext.getClickedFace(), blockPos, pContext.isInside()));
                super.useOn(useOnContext);
                bindSoil(useOnContext);
                if (!level.isClientSide())
                    level.sendBlockUpdated(blockPos, oldState, level.getBlockState(blockPos), 3);
            }
            useOnAbility(pContext);
            return InteractionResult.PASS;
        } else {
            InteractionResult interactionResult = super.useOn(pContext);
            bindSoil(pContext);
            useOnAbility(pContext);
            return interactionResult;
        }
    }

    @Override
    public boolean mineBlock(ItemStack pStack, Level pLevel, BlockState pState, BlockPos pPos, LivingEntity pEntityLiving) {
        return true;  //We handle damage in the BlockEvent.BreakEvent
    }

    @Override
    public void hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        hurtEnemyAbility(pStack, pTarget, pAttacker);
    }

    // TODO(port, stage-4): restore appendHoverText against new signature.

    @Override
    public EnumSet<Ability> getAllAbilities() {
        return abilities;
    }

    @Override
    public EnumSet<Ability> getAbilities() {
        return abilities.stream()
                .filter(ability -> Config.AVAILABLE_ABILITY_MAP.get(ability).get())
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(Ability.class)));
    }

    @Override
    public Map<Ability, AbilityParams> getAbilityParamsMap() {
        return abilityParams;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide() && player.isShiftKeyDown())
            openSettings(player);
        useAbility(level, player, hand);
        return super.use(level, player, hand);
    }

    // TODO(port, stage-5): re-wire damageItem against new EnergyHandler/ItemAccess.

    @Override
    public boolean isPrimaryItemFor(ItemStack stack, Holder<Enchantment> enchantment) {
        if (stack.getItem() instanceof PoweredTool)
            return super.isPrimaryItemFor(stack, enchantment) && canAcceptEnchantments(enchantment);
        return super.isPrimaryItemFor(stack, enchantment);
    }

    private boolean canAcceptEnchantments(Holder<Enchantment> enchantment) {
        return !enchantment.value().effects().has(EnchantmentEffectComponents.REPAIR_WITH_XP);
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return ItemUseAnimation.NONE;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    @Override
    public boolean shouldCauseBlockBreakReset(ItemStack oldStack, ItemStack newStack) {
        if (oldStack.is(newStack.getItem())) return false;
        return super.shouldCauseBlockBreakReset(oldStack, newStack);
    }
}
