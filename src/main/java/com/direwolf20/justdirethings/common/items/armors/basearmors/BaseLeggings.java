package com.direwolf20.justdirethings.common.items.armors.basearmors;

import com.direwolf20.justdirethings.common.items.interfaces.*;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.energy.IEnergyStorage;

import java.util.*;
import java.util.function.Consumer;

import static com.direwolf20.justdirethings.util.TooltipHelpers.*;

public class BaseLeggings extends ArmorItem implements ToggleableTool, LeftClickableTool {
    public static final UUID WALKSPEEDUUID = UUID.fromString("dac96a09-4758-419d-aa1b-83a27d266484");
    public static final UUID RUNSPEEDUUID = UUID.fromString("5863a2c2-bbc4-4baa-8e0f-15e2640e7f2f");
    protected final EnumSet<Ability> abilities = EnumSet.noneOf(Ability.class);
    protected final Map<Ability, AbilityParams> abilityParams = new EnumMap<>(Ability.class);

    public BaseLeggings(ArmorMaterial pMaterial, Properties pProperties) {
        super(pMaterial, Type.LEGGINGS, pProperties);
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
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> modifiers = super.getAttributeModifiers(slot, stack);
        if (slot == EquipmentSlot.LEGS) {
            Multimap<Attribute, AttributeModifier> modifiers2 = ArrayListMultimap.create(modifiers);
            modifiers2.put(NeoForgeMod.STEP_HEIGHT.value(), new AttributeModifier(WALKSPEEDUUID, "JustDireStepAssist", 1.0, AttributeModifier.Operation.ADDITION));

            modifiers2.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(RUNSPEEDUUID, "JustDireSpeed", 3f, AttributeModifier.Operation.MULTIPLY_TOTAL));
            return modifiers2;
        }
        if (!(stack.getItem() instanceof PoweredTool poweredTool))
            return modifiers;

        return poweredTool.getPoweredAttributeModifiers(slot, stack, modifiers);
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        if (stack.getItem() instanceof PoweredTool poweredTool) {
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
        if (stack.getItem() instanceof PoweredTool)
            return super.isBookEnchantable(stack, book) && canAcceptEnchantments(book);
        return super.isBookEnchantable(stack, book);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if (stack.getItem() instanceof PoweredTool)
            return super.canApplyAtEnchantingTable(stack, enchantment) && canAcceptEnchantments(enchantment);
        return super.canApplyAtEnchantingTable(stack, enchantment);
    }

    private boolean canAcceptEnchantments(ItemStack book) {
        return !EnchantmentHelper.getEnchantments(book).containsKey(Enchantments.MENDING);
    }

    private boolean canAcceptEnchantments(Enchantment enchantment) {
        return enchantment != Enchantments.MENDING;
    }

    public float getAdjustedFOV(ItemStack itemStack) {
        return 0.2f * 5f;
    }

}
