package com.direwolf20.justdirethings.common.items.tools.basetools;

import com.direwolf20.justdirethings.common.items.interfaces.*;
import com.direwolf20.justdirethings.setup.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.direwolf20.justdirethings.util.TooltipHelpers.*;

public class BaseSword extends Item implements ToggleableTool, LeftClickableTool {
    protected final EnumSet<Ability> abilities = EnumSet.noneOf(Ability.class);
    protected final Map<Ability, AbilityParams> abilityParams = new EnumMap<>(Ability.class);

    public BaseSword(Item.Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean mineBlock(ItemStack pStack, Level pLevel, BlockState pState, BlockPos pPos, LivingEntity pEntityLiving) {
        return true; //We handle damage in the BlockEvent.BreakEvent
    }

    @Override
    public void inventoryTick(ItemStack itemStack, ServerLevel level, Entity entity, @Nullable EquipmentSlot slot) {
        if (!getCooldownAbilities().isEmpty() && entity instanceof Player player) {
            ToggleableTool.tickCooldowns(level, itemStack, player);
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        if (bindDrops(pContext))
            return InteractionResult.SUCCESS;
        useOnAbility(pContext);
        return super.useOn(pContext);
    }

    @Override
    public void hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        hurtEnemyAbility(pStack, pTarget, pAttacker);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, context, display, tooltip, flagIn);
        Level level = context.level();
        if (level == null) {
            return;
        }
        List<Component> buffer = new ArrayList<>();
        boolean sneakPressed = Minecraft.getInstance().hasShiftDown();
        appendFEText(stack, buffer);
        if (sneakPressed) {
            appendToolEnabled(stack, buffer);
            appendAbilityList(stack, buffer);
        } else {
            appendToolEnabled(stack, buffer);
            appendShiftForInfo(stack, buffer);
        }
        buffer.forEach(tooltip);
    }

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

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, @Nullable T entity, Consumer<Item> onBroken) {
        if (stack.getItem() instanceof PoweredTool) {
            EnergyHandler energyStorage = stack.getCapability(Capabilities.Energy.ITEM, ItemAccess.forStack(stack));
            if (energyStorage == null) return amount;
            double reductionFactor = 0;
            if (entity != null && entity.level().getServer() != null) {
                HolderLookup.RegistryLookup<Enchantment> registrylookup = entity.level().getServer().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
                int unbreakingLevel = stack.getEnchantmentLevel(registrylookup.getOrThrow(Enchantments.UNBREAKING));
                reductionFactor = Math.min(1.0, unbreakingLevel * 0.1);
            }
            int finalEnergyCost = (int) Math.max(0, amount - (amount * reductionFactor));
            try (Transaction tx = Transaction.openRoot()) {
                energyStorage.extract(finalEnergyCost, tx);
                tx.commit();
            }
            return 0;
        }
        return amount;
    }

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
