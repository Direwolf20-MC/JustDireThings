package com.direwolf20.justdirethings.common.items.tools.basetools;

import com.direwolf20.justdirethings.common.items.tools.utils.*;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

public class BaseShovel extends ShovelItem implements TieredGooItem, ToggleableTool {
    protected final EnumSet<Ability> abilities = EnumSet.noneOf(Ability.class);
    protected final Map<Ability, AbilityParams> abilityParams = new EnumMap<>(Ability.class);

    public BaseShovel(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Item.Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    @Override
    public boolean mineBlock(ItemStack pStack, Level pLevel, BlockState pState, BlockPos pPos, LivingEntity pEntityLiving) {
        return true; //We handle damage in the BlockEvent.BreakEvent
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
        if (stack.getItem() instanceof ToggleableTool toggleableTool) {
            PoweredItem.appendFEText(stack, level, tooltip, flagIn);
            if (sneakPressed) {
                if (ToggleableTool.getEnabled(stack))
                    tooltip.add(Component.translatable("justdirethings.enabled").withStyle(ChatFormatting.GREEN));
                else
                    tooltip.add(Component.translatable("justdirethings.disabled").withStyle(ChatFormatting.DARK_RED));
                for (Ability ability : toggleableTool.getAbilities()) {
                    boolean active = ToggleableTool.getSetting(stack, ability.getName());
                    ChatFormatting chatFormatting = active ? ChatFormatting.GREEN : ChatFormatting.DARK_RED;
                    tooltip.add(Component.translatable(ability.getLocalization()).withStyle(chatFormatting));
                }
            } else {
                if (ToggleableTool.getEnabled(stack))
                    tooltip.add(Component.translatable("justdirethings.enabled").withStyle(ChatFormatting.GREEN));
                else
                    tooltip.add(Component.translatable("justdirethings.disabled").withStyle(ChatFormatting.DARK_RED));
                tooltip.add(Component.translatable("justdirethings.shiftmoreinfo").withStyle(ChatFormatting.GRAY));
            }
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
        return super.use(level, player, hand);
    }
}
