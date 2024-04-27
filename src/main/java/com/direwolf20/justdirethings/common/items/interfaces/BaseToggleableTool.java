package com.direwolf20.justdirethings.common.items.interfaces;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static com.direwolf20.justdirethings.util.TooltipHelpers.*;

public abstract class BaseToggleableTool extends Item implements ToggleableTool {
    protected final EnumSet<Ability> abilities = EnumSet.noneOf(Ability.class);
    protected final Map<Ability, AbilityParams> abilityParams = new EnumMap<>(Ability.class);

    public BaseToggleableTool(Properties pProperties) {
        super(pProperties);
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
    public InteractionResult useOn(UseOnContext pContext) {
        if (bindDrops(pContext))
            return InteractionResult.SUCCESS;
        if (useOnAbility(pContext))
            return InteractionResult.SUCCESS;
        return super.useOn(pContext);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide && player.isShiftKeyDown())
            openSettings(player);
        useAbility(level, player, hand);
        return super.use(level, player, hand);
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

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        if (stack.getItem() instanceof PoweredItem) {
            IEnergyStorage energyStorage = stack.getCapability(Capabilities.EnergyStorage.ITEM);
            if (energyStorage == null) return amount;
            int unbreakingLevel = stack.getEnchantmentLevel(Enchantments.UNBREAKING);
            double reductionFactor = Math.min(1.0, unbreakingLevel * 0.1);
            int finalEnergyCost = (int) Math.max(0, amount - (amount * reductionFactor));
            energyStorage.extractEnergy(finalEnergyCost, false);
            return 0;
        }
        return super.damageItem(stack, amount, entity, onBroken);
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

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return super.getUseAnimation(stack);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        if (canUseAbility(stack, Ability.LAVAREPAIR))
            return Helpers.doLavaRepair(stack, entity);
        return false;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        if (stack.getItem() instanceof PoweredItem poweredItem) {
            return poweredItem.isPowerBarVisible(stack);
        }
        return super.isBarVisible(stack);
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        if (stack.getItem() instanceof PoweredItem poweredItem) {
            return poweredItem.getPowerBarWidth(stack);
        }
        return super.getBarWidth(stack);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        if (stack.getItem() instanceof PoweredItem poweredItem) {
            int color = poweredItem.getPowerBarColor(stack);
            if (color == -1)
                return super.getBarColor(stack);
            return color;
        }
        return super.getBarColor(stack);
    }

}
