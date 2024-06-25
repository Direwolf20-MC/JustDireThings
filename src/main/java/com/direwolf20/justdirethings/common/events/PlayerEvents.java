package com.direwolf20.justdirethings.common.events;

import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.common.items.interfaces.PoweredItem;
import com.direwolf20.justdirethings.common.items.interfaces.PoweredTool;
import com.direwolf20.justdirethings.common.items.interfaces.ToggleableTool;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.Set;

import static com.direwolf20.justdirethings.common.items.interfaces.ToggleableTool.getInstantRFCost;
import static com.direwolf20.justdirethings.common.items.interfaces.ToggleableTool.getToolValue;

public class PlayerEvents {
    @SubscribeEvent
    public static void BreakSpeed(PlayerEvent.BreakSpeed event) {
        Player player = event.getEntity();
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
            float targetSpeed = event.getOriginalSpeed();
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
                rfCost = getInstantRFCost(cumulativeDestroy);
                float modifier = ((float) breakBlockPositions.size() / radius) < 1 ? 1 : ((float) breakBlockPositions.size() / radius);
                cumulativeDestroy = (cumulativeDestroy / breakBlockPositions.size()) * modifier; //Up to 3 times slower
                float relative = originalDestroySpeed / cumulativeDestroy;
                targetSpeed = event.getOriginalSpeed() * relative;
            }
            if (toggleableTool.canUseAbility(stack, Ability.INSTABREAK) && stack.getItem() instanceof PoweredItem && PoweredItem.getAvailableEnergy(stack) >= rfCost) {
                targetSpeed = 10000f;
            }
            if (targetSpeed != event.getOriginalSpeed())
                event.setNewSpeed(targetSpeed);
        }
    }
}
