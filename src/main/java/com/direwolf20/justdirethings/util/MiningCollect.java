package com.direwolf20.justdirethings.util;

import com.direwolf20.justdirethings.JustDireThings;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Handles collecting the blocks for the mining action.
 */
public class MiningCollect {
    public enum SizeMode {
        AUTO("auto"),
        NORMAL("normal"),
        PATHWAY("pathway");

        private final String baseName;

        SizeMode(String baseName) {
            this.baseName = baseName;
        }

        public Component getTooltip() {
            return Component.translatable(JustDireThings.MODID + ".tooltip.screen.sizemode." + baseName);
        }
    }

    public static List<BlockPos> collect(Player player, BlockHitResult startBlock, Level world, int range, SizeMode sizeMode, ItemStack tool) {
        List<BlockPos> coordinates = new ArrayList<>();
        BlockPos startPos = startBlock.getBlockPos();

        if (range == 1) {
            if (!isValid(player, startBlock.getBlockPos(), world, tool))
                return coordinates;

            coordinates.add(startBlock.getBlockPos());
            return coordinates;
        }

        Direction side = startBlock.getDirection();
        boolean vertical = side.getAxis().isVertical();
        Direction up = vertical ? player.getDirection() : Direction.UP;
        Direction down = up.getOpposite();
        Direction right = vertical ? up.getClockWise() : side.getCounterClockWise();
        Direction left = right.getOpposite();

        int midRange = ((range - 1) / 2);
        int upRange = midRange;
        int downRange = midRange;

        if (!vertical && range > 3) {

            if (sizeMode == SizeMode.AUTO) {
                double myYPos = player.position().get(Direction.UP.getAxis());
                double hitBlockPos = startBlock.getBlockPos().get(Direction.UP.getAxis());

                if (Math.abs(myYPos - hitBlockPos) < 2) {
                    downRange = 1;
                    upRange = range - 2;
                }
            } else if (sizeMode == SizeMode.PATHWAY) {
                downRange = 1;
                upRange = range - 2;
            }
        }

        BlockPos topLeft = startPos.relative(up, upRange).relative(left, midRange);
        BlockPos bottomRight = startPos.relative(down, downRange).relative(right, midRange);

        return BlockPos
                .betweenClosedStream(topLeft, bottomRight)
                .map(BlockPos::immutable)
                .filter(e -> isValid(player, e, world, tool))
                .collect(Collectors.toList());
    }

    private static boolean isValid(Player player, BlockPos pos, Level level, ItemStack tool) {
        BlockState state = level.getBlockState(pos);
        return tool.isCorrectToolForDrops(state);
    }
}
