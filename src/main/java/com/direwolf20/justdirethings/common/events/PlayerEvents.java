package com.direwolf20.justdirethings.common.events;

import com.direwolf20.justdirethings.common.items.tools.utils.Ability;
import com.direwolf20.justdirethings.common.items.tools.utils.ToggleableTool;
import com.direwolf20.justdirethings.util.MiningCollect;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.HashSet;
import java.util.Set;

import static com.direwolf20.justdirethings.common.items.tools.utils.ToggleableTool.getTargetLookDirection;
import static com.direwolf20.justdirethings.common.items.tools.utils.ToggleableTool.getToolValue;

public class PlayerEvents {
    @SubscribeEvent
    public static void BreakSpeed(PlayerEvent.BreakSpeed event) {
        Player player = event.getEntity();
        ItemStack stack = player.getMainHandItem(); // Assuming the tool is in the main hand
        if (stack.getItem() instanceof ToggleableTool toggleableTool && stack.isCorrectToolForDrops(event.getState())) {
            if (toggleableTool.canUseAbility(stack, Ability.HAMMER)) {
                Level level = player.level();
                BlockPos originalPos = event.getPosition().get();
                BlockState originalState = level.getBlockState(event.getPosition().get());
                float originalDestroySpeed = originalState.getDestroySpeed(level, originalPos);
                if (originalDestroySpeed <= 0) return;
                Set<BlockPos> breakBlockPositions = new HashSet<>();
                int radius = getToolValue(stack, Ability.HAMMER.getName());
                breakBlockPositions.addAll(MiningCollect.collect(player, event.getPosition().get(), getTargetLookDirection(player), level, radius, MiningCollect.SizeMode.AUTO, stack));
                if (breakBlockPositions.isEmpty()) return; //Avoid potential divide by zero
                float cumulativeDestroy = 0;
                for (BlockPos pos : breakBlockPositions) {
                    BlockState blockState = level.getBlockState(pos);
                    float destroySpeedTarget = blockState.getDestroySpeed(level, pos);
                    cumulativeDestroy = cumulativeDestroy + destroySpeedTarget;
                }
                float modifier = ((float) breakBlockPositions.size() / radius) < 1 ? 1 : ((float) breakBlockPositions.size() / radius);
                cumulativeDestroy = (cumulativeDestroy / breakBlockPositions.size()) * modifier; //Up to 3 times slower
                float relative = originalDestroySpeed / cumulativeDestroy;
                float targetSpeed = event.getOriginalSpeed() * relative;
                event.setNewSpeed(targetSpeed);
            }
        }
    }
}
