package com.direwolf20.justdirethings.common.items.tools.basetools;

import com.direwolf20.justdirethings.common.blockentities.GooSoilBE;
import com.direwolf20.justdirethings.common.blocks.soil.GooSoilBase;
import com.direwolf20.justdirethings.common.items.tools.utils.*;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import static com.direwolf20.justdirethings.util.TooltipHelpers.*;

public class BaseHoe extends HoeItem implements ToggleableTool {
    protected final EnumSet<Ability> abilities = EnumSet.noneOf(Ability.class);
    protected final Map<Ability, AbilityParams> abilityParams = new EnumMap<>(Ability.class);

    public BaseHoe(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Item.Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        if (bindDrops(pContext))
            return InteractionResult.SUCCESS;
        InteractionResult interactionResult = super.useOn(pContext);
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
                            GlobalPos boundPos = ToggleableTool.getBoundInventory(heldItem);
                            Direction direction = ToggleableTool.getBoundInventorySide(heldItem);
                            if (boundPos != null) {
                                BlockEntity blockEntity = pLevel.getBlockEntity(clickedPos);
                                if (blockEntity != null && blockEntity instanceof GooSoilBE gooSoilBE) {
                                    gooSoilBE.bindInventory(boundPos, direction);
                                    pContext.getPlayer().displayClientMessage(Component.translatable("justdirethings.boundto", I18n.get(boundPos.dimension().location().getPath()), "[" + boundPos.pos().toShortString() + "]"), true);
                                    player.playNotifySound(SoundEvents.ENDER_EYE_DEATH, SoundSource.PLAYERS, 1.0F, 1.0F);
                                    Helpers.damageTool(heldItem, player, Ability.DROPTELEPORT, 10);
                                    bindingSuccess = true;
                                }
                            }
                        }
                    }
                }
            }
            if (!bindingSuccess) {
                pContext.getPlayer().displayClientMessage(Component.translatable("justdirethings.bindfailed").withStyle(ChatFormatting.RED), true);
                player.playNotifySound(SoundEvents.ANVIL_BREAK, SoundSource.PLAYERS, 1.0F, 1.0F);
            }
        }
        useOnAbility(pContext);
        return interactionResult;
    }

    @Override
    public boolean mineBlock(ItemStack pStack, Level pLevel, BlockState pState, BlockPos pPos, LivingEntity pEntityLiving) {
        return true;  //We handle damage in the BlockEvent.BreakEvent
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
}
