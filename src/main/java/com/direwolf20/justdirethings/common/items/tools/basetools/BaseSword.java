package com.direwolf20.justdirethings.common.items.tools.basetools;

import com.direwolf20.justdirethings.common.items.tools.utils.*;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

public class BaseSword extends SwordItem implements TieredGooItem, ToggleableTool {
    protected final EnumSet<Ability> abilities = EnumSet.noneOf(Ability.class);
    protected final Map<Ability, AbilityParams> abilityParams = new EnumMap<>(Ability.class);

    public BaseSword(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Item.Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
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
