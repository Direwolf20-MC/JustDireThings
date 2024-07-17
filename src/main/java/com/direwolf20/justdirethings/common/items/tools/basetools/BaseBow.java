package com.direwolf20.justdirethings.common.items.tools.basetools;

import com.direwolf20.justdirethings.common.entities.JustDireArrow;
import com.direwolf20.justdirethings.common.items.PotionCanister;
import com.direwolf20.justdirethings.common.items.interfaces.*;
import com.direwolf20.justdirethings.setup.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.ComponentItemHandler;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.direwolf20.justdirethings.util.TooltipHelpers.*;

public class BaseBow extends BowItem implements ToggleableTool, LeftClickableTool {
    protected final EnumSet<Ability> abilities = EnumSet.noneOf(Ability.class);
    protected final Map<Ability, AbilityParams> abilityParams = new EnumMap<>(Ability.class);

    public BaseBow(Properties properties) {
        super(properties);
    }

    public float getMaxDraw() {
        return 20;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide) return InteractionResultHolder.pass(stack);
        if (player.isShiftKeyDown()) {
            openSettings(player);
            return InteractionResultHolder.success(player.getItemInHand(hand));
        }
        if (stack.getItem() instanceof PoweredTool poweredTool && PoweredItem.getAvailableEnergy(stack) < poweredTool.getBlockBreakFECost())
            return InteractionResultHolder.pass(stack);
        return super.use(level, player, hand);
    }

    protected Projectile createProjectile(Level level, LivingEntity livingEntity, ItemStack itemStack, ItemStack stack, boolean crit) {
        ArrowItem arrowitem = stack.getItem() instanceof ArrowItem arrowitem1 ? arrowitem1 : (ArrowItem) Items.ARROW;
        ToggleableTool toggleableTool = (ToggleableTool) itemStack.getItem();
        if (arrowitem.equals(Items.ARROW)) {
            JustDireArrow justDireArrow = new JustDireArrow(level, livingEntity, stack, itemStack);
            if (crit) {
                justDireArrow.setCritArrow(true);
            }
            if (!toggleableTool.getEnabled(itemStack))
                return customArrow(justDireArrow, stack, itemStack);

            if (canUseAbilityAndDurability(itemStack, Ability.HOMING)) {
                justDireArrow.setHoming(true);
                Helpers.damageTool(itemStack, livingEntity, Ability.HOMING);
            }

            if (noPotionAbilitiesActive(itemStack))
                return customArrow(justDireArrow, stack, itemStack);

            IItemHandler itemHandler = itemStack.getCapability(Capabilities.ItemHandler.ITEM);
            if (itemHandler instanceof ComponentItemHandler componentItemHandler) {
                PotionContents potionContents = PotionContents.EMPTY;
                for (int slot = 0; slot < componentItemHandler.getSlots(); slot++) {
                    ItemStack potionCanister = componentItemHandler.getStackInSlot(slot);
                    if (potionCanister.getItem() instanceof PotionCanister) {
                        int potionAmt = PotionCanister.getPotionAmount(potionCanister);
                        PotionContents slotPotionContents = PotionCanister.getPotionContents(potionCanister);
                        if (!slotPotionContents.equals(PotionContents.EMPTY)) {
                            int neededAmt = 0;
                            if (canUseAbilityAndDurability(itemStack, Ability.POTIONARROW))
                                neededAmt = neededAmt + 25;
                            if (canUseAbilityAndDurability(itemStack, Ability.SPLASH))
                                neededAmt = neededAmt + 25;
                            if (canUseAbilityAndDurability(itemStack, Ability.LINGERING))
                                neededAmt = neededAmt + 50;
                            if (potionAmt >= neededAmt) {
                                for (MobEffectInstance mobEffectInstance : slotPotionContents.getAllEffects())
                                    potionContents = potionContents.withEffectAdded(mobEffectInstance);
                                PotionCanister.setPotionAmount(potionCanister, potionAmt - neededAmt);
                                componentItemHandler.setStackInSlot(slot, potionCanister);
                            }
                        }
                    }
                }
                if (!potionContents.equals(PotionContents.EMPTY)) {
                    justDireArrow.setPotionContents(potionContents);
                    if (canUseAbilityAndDurability(itemStack, Ability.POTIONARROW)) {
                        justDireArrow.setPotionArrow(true);
                        Helpers.damageTool(itemStack, livingEntity, Ability.POTIONARROW);
                    }
                    if (canUseAbilityAndDurability(itemStack, Ability.SPLASH)) {
                        justDireArrow.setSplash(true);
                        Helpers.damageTool(itemStack, livingEntity, Ability.SPLASH);
                    }
                    if (canUseAbilityAndDurability(itemStack, Ability.LINGERING)) {
                        justDireArrow.setLingering(true);
                        Helpers.damageTool(itemStack, livingEntity, Ability.LINGERING);
                    }
                }
            }

            return customArrow(justDireArrow, stack, itemStack);
        }
        return super.createProjectile(level, livingEntity, itemStack, stack, crit);
    }

    public boolean noPotionAbilitiesActive(ItemStack itemStack) {
        if (canUseAbilityAndDurability(itemStack, Ability.POTIONARROW))
            return false;
        if (canUseAbilityAndDurability(itemStack, Ability.SPLASH))
            return false;
        if (canUseAbilityAndDurability(itemStack, Ability.LINGERING))
            return false;
        return true;

    }

    @Override
    protected int getDurabilityUse(ItemStack itemStack) {
        return this instanceof PoweredTool poweredTool ? poweredTool.getBlockBreakFECost() : 1;
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
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, context, tooltip, flagIn);
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) {
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
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, @Nullable T entity, Consumer<Item> onBroken) {
        if (stack.getItem() instanceof PoweredTool poweredTool) {
            IEnergyStorage energyStorage = stack.getCapability(Capabilities.EnergyStorage.ITEM);
            if (energyStorage == null) return amount;
            double reductionFactor = 0;
            if (entity != null) {
                HolderLookup.RegistryLookup<Enchantment> registrylookup = entity.level().getServer().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
                int unbreakingLevel = stack.getEnchantmentLevel(registrylookup.getOrThrow(Enchantments.UNBREAKING));
                reductionFactor = Math.min(1.0, unbreakingLevel * 0.1);
            }
            int finalEnergyCost = (int) Math.max(0, amount - (amount * reductionFactor));
            energyStorage.extractEnergy(finalEnergyCost, false);
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
}
