package com.direwolf20.justdirethings.common.items.tools.basetools;

import com.direwolf20.justdirethings.common.items.tools.utils.*;
import com.google.common.collect.Multimap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static com.direwolf20.justdirethings.util.TooltipHelpers.*;

public class BasePickaxe extends PickaxeItem implements ToggleableTool {
    protected final EnumSet<Ability> abilities = EnumSet.noneOf(Ability.class);
    protected final Map<Ability, AbilityParams> abilityParams = new EnumMap<>(Ability.class);


    public BasePickaxe(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Item.Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        if (bindDrops(pContext))
            return InteractionResult.SUCCESS;
        useOnAbility(pContext);
        return super.useOn(pContext);
    }

    @Override
    public boolean mineBlock(ItemStack pStack, Level pLevel, BlockState pState, BlockPos pPos, LivingEntity pEntityLiving) {
        return true;  //We handle damage in the BlockEvent.BreakEvent
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, Player player) {
        BlockState blockState = player.level().getBlockState(pos);
        if (itemstack.getItem() instanceof ToggleableTool toggleableTool && itemstack.isCorrectToolForDrops(blockState)) {
            toggleableTool.mineBlocksAbility(itemstack, player.level(), pos, player);
        }
        return false;
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        return hurtEnemyAbility(pStack, pTarget, pAttacker);
    }

    @Override
    public void appendHoverText(ItemStack stack, @javax.annotation.Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, level, tooltip, flagIn);
        Minecraft mc = Minecraft.getInstance();
        if (level == null || mc.player == null) {
            return;
        }

        boolean sneakPressed = Screen.hasShiftDown();
        appendFEText(stack, tooltip);
        if (sneakPressed) {
            appendToolEnabled(stack, tooltip);
            appendAbilityList(stack, tooltip);
        } else {
            appendToolEnabled(stack, tooltip);
            appendShiftForInfo(stack, tooltip);
        }
    }

    /**
     * Reduces the attack damage of a tool when unpowered
     */
    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> modifiers = super.getAttributeModifiers(slot, stack);
        if (!(stack.getItem() instanceof PoweredItem poweredItem))
            return modifiers;

        return poweredItem.getPoweredAttributeModifiers(slot, stack, modifiers);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if (!(stack.getItem() instanceof PoweredItem poweredItem))
            return super.getDestroySpeed(stack, state);
        float defaultSpeed = super.getDestroySpeed(stack, state);
        if (poweredItem.getAvailableEnergy(stack) < poweredItem.getBlockBreakFECost()) {
            return defaultSpeed * 0.01f;
        }
        return defaultSpeed;
    }

    @Override
    public EnumSet<Ability> getAbilities() {
        return abilities;
    }

    @Override
    public Map<Ability, AbilityParams> getAbilityParamsMap() {
        return abilityParams;
    }


    @Override
    public GooTier gooTier() {
        return (GooTier) this.getTier();
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide && player.isShiftKeyDown())
            openSettings(player);
        useAbility(level, player, hand);
        return super.use(level, player, hand);
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        if (stack.getItem() instanceof PoweredItem poweredItem) {
            IEnergyStorage energyStorage = stack.getCapability(Capabilities.EnergyStorage.ITEM);
            if (energyStorage == null) return amount;
            int unbreakingLevel = stack.getEnchantmentLevel(Enchantments.UNBREAKING);
            double reductionFactor = Math.min(1.0, unbreakingLevel * 0.1);
            int finalEnergyCost = (int) Math.max(0, amount - (amount * reductionFactor));
            energyStorage.extractEnergy(finalEnergyCost, false);
            return 0;
        }
        return amount;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        if (stack.getItem() instanceof PoweredItem)
            return super.isBookEnchantable(stack, book) && canAcceptEnchantments(book);
        return super.isBookEnchantable(stack, book);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if (stack.getItem() instanceof PoweredItem)
            return super.canApplyAtEnchantingTable(stack, enchantment) && canAcceptEnchantments(enchantment);
        return super.canApplyAtEnchantingTable(stack, enchantment);
    }

    private boolean canAcceptEnchantments(ItemStack book) {
        return !EnchantmentHelper.getEnchantments(book).containsKey(Enchantments.MENDING);
    }

    private boolean canAcceptEnchantments(Enchantment enchantment) {
        return enchantment != Enchantments.MENDING;
    }
}
