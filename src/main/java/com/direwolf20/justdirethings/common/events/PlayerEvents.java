package com.direwolf20.justdirethings.common.events;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.common.items.interfaces.PoweredItem;
import com.direwolf20.justdirethings.common.items.interfaces.PoweredTool;
import com.direwolf20.justdirethings.common.items.interfaces.ToggleableTool;
import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.Set;

import static com.direwolf20.justdirethings.common.items.interfaces.ToggleableTool.getInstantRFCost;
import static com.direwolf20.justdirethings.common.items.interfaces.ToggleableTool.getToolValue;

public class PlayerEvents {
    public static final AttributeModifier stepHeight = new AttributeModifier(ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "justdirestepassist"), 1.0, AttributeModifier.Operation.ADD_VALUE);
    public static final AttributeModifier creativeFlight = new AttributeModifier(ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "justdireflight"), 1.0, AttributeModifier.Operation.ADD_VALUE);
    public static final AttributeModifier phase = new AttributeModifier(ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "justdirephase"), 1.0, AttributeModifier.Operation.ADD_VALUE);

    @SubscribeEvent
    public static void ItemAttributes(ItemAttributeModifierEvent event) {
        ItemStack itemStack = event.getItemStack();
        if (itemStack.getItem() instanceof ToggleableTool toggleableTool) {
            if (toggleableTool.canUseAbility(itemStack, Ability.STEPHEIGHT))
                event.addModifier(Attributes.STEP_HEIGHT, stepHeight, EquipmentSlotGroup.FEET);
            if (toggleableTool.canUseAbilityAndDurability(itemStack, Ability.FLIGHT))
                event.addModifier(NeoForgeMod.CREATIVE_FLIGHT, creativeFlight, EquipmentSlotGroup.CHEST);
            if (toggleableTool.canUseAbility(itemStack, Ability.PHASE))
                event.addModifier(Registration.PHASE, phase, EquipmentSlotGroup.LEGS);
        }
        //A catch all for unpowered itemse
        if (itemStack.getItem() instanceof PoweredTool poweredTool && PoweredItem.getAvailableEnergy(itemStack) < poweredTool.getBlockBreakFECost()) {
            event.clearModifiers();
        }
    }


    @SubscribeEvent
    public static void BreakSpeed(PlayerEvent.BreakSpeed event) {
        Player player = event.getEntity();
        float targetSpeed = event.getOriginalSpeed();
        if (player.getAbilities().flying) {
            ItemStack chestPlate = player.getItemBySlot(EquipmentSlot.CHEST);
            if (chestPlate.getItem() instanceof ToggleableTool toggleableTool && toggleableTool.canUseAbilityAndDurability(chestPlate, Ability.FLIGHT))
                targetSpeed = targetSpeed * 5; //Vanilla divides by 5 if you're flying
        }
        ItemStack stack = player.getMainHandItem(); // Assuming the tool is in the main hand
        int rfCost = 0;
        if (stack.getItem() instanceof ToggleableTool toggleableTool && stack.isCorrectToolForDrops(event.getState())) {
            if (stack.getItem() instanceof PoweredTool poweredTool) {
                if (PoweredItem.getAvailableEnergy(stack) < poweredTool.getBlockBreakFECost()) {
                    event.setNewSpeed(0.1f);
                    return;
                }
            }
            Level level = player.level();
            BlockPos originalPos = event.getPosition().get();
            BlockState originalState = level.getBlockState(event.getPosition().get());
            float originalDestroySpeed = originalState.getDestroySpeed(level, originalPos);
            float cumulativeDestroy = 0;
            if (originalDestroySpeed <= 0) return;
            Set<BlockPos> breakBlockPositions = toggleableTool.getBreakBlockPositions(stack, level, originalPos, player, originalState);
            if (!breakBlockPositions.isEmpty()) { //Avoid potential divide by zero
                int radius = toggleableTool.canUseAbility(stack, Ability.HAMMER) ? getToolValue(stack, Ability.HAMMER.getName()) : 1;
                for (BlockPos pos : breakBlockPositions) {
                    BlockState blockState = level.getBlockState(pos);
                    float destroySpeedTarget = blockState.getDestroySpeed(level, pos);
                    cumulativeDestroy = cumulativeDestroy + destroySpeedTarget;
                }
                rfCost = getInstantRFCost(cumulativeDestroy, level, stack);
                float modifier = ((float) breakBlockPositions.size() / radius) < 1 ? 1 : ((float) breakBlockPositions.size() / radius);
                cumulativeDestroy = (cumulativeDestroy / breakBlockPositions.size()) * modifier; //Up to 3 times slower
                float relative = originalDestroySpeed / cumulativeDestroy;
                targetSpeed = targetSpeed * relative;
            }
            if (toggleableTool.canUseAbility(stack, Ability.INSTABREAK) && stack.getItem() instanceof PoweredItem && PoweredItem.getAvailableEnergy(stack) >= rfCost) {
                targetSpeed = 10000f;
            }
        }
        if (targetSpeed != event.getOriginalSpeed())
            event.setNewSpeed(targetSpeed);
    }
}