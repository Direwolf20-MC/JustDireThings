package com.direwolf20.justdirethings.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.Random;

public class MiscHelpers {
    private static final Random rand = new Random();

    public static double nextDouble(double min, double max) {
        return min + (max - min) * rand.nextDouble();
    }

    public static IItemHandler getAttachedInventory(Level level, BlockPos blockPos, Direction side) {
        assert level != null;
        BlockEntity be = level.getBlockEntity(blockPos);
        // if we have a TE and its an item handler, try extracting from that
        if (be != null) {
            IItemHandler handler = level.getCapability(Capabilities.ItemHandler.BLOCK, blockPos, side);
            return handler;
        }
        return null;
    }
}
