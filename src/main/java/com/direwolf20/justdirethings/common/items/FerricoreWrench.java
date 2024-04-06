package com.direwolf20.justdirethings.common.items;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.List;

public class FerricoreWrench extends Item {
    public FerricoreWrench() {
        super(new Properties()
                .stacksTo(1));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();

        if (!level.isClientSide && player != null) {
            BlockState state = level.getBlockState(pos);
            for (Property<?> prop : state.getProperties()) {
                if (prop instanceof DirectionProperty && prop.getName().equals("facing")) {
                    // Rotate the block using the improved logic.
                    BlockState rotatedState = rotateBlock(state, (DirectionProperty) prop, state.getValue(prop));
                    level.setBlock(pos, rotatedState, 3);
                    level.playSound(null, pos, SoundEvents.ITEM_FRAME_ROTATE_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }

    private BlockState rotateBlock(BlockState state, DirectionProperty prop, Comparable<?> currentValue) {
        List<Direction> directions = prop.getPossibleValues().stream().toList();
        int currentDirectionIndex = directions.indexOf(currentValue);
        int nextDirectionIndex = (currentDirectionIndex + 1) % directions.size();
        Direction nextDirection = directions.get(nextDirectionIndex);
        return state.setValue(prop, nextDirection);
    }
}
