package com.direwolf20.justdirethings.common.blockentities.basebe;

import com.direwolf20.justdirethings.common.blocks.BlockBreakerT1;
import com.direwolf20.justdirethings.util.MiscHelpers;
import com.direwolf20.justdirethings.util.interfacehelpers.RedstoneControlData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public interface RedstoneControlledBE {
    RedstoneControlData getRedstoneControlData();

    BlockEntity getBlockEntity();

    default void setRedstoneSettings(int redstoneMode) {
        getRedstoneControlData().redstoneMode = MiscHelpers.RedstoneMode.values()[redstoneMode];
        if (getBlockEntity() instanceof BaseMachineBE baseMachineBE)
            baseMachineBE.markDirtyClient();
        BlockState blockState = getBlockEntity().getBlockState();
        if (blockState.hasProperty(BlockBreakerT1.ACTIVE)) {
            getBlockEntity().getLevel().setBlockAndUpdate(getBlockEntity().getBlockPos(), blockState.setValue(BlockBreakerT1.ACTIVE, isActiveRedstoneTestOnly()));
        }
    }

    default void evaluateRedstone() {
        if (!getRedstoneControlData().checkedRedstone) {
            boolean newRedstoneSignal = getBlockEntity().getLevel().hasNeighborSignal(getBlockEntity().getBlockPos());
            if (getRedstoneControlData().redstoneMode.equals(MiscHelpers.RedstoneMode.PULSE) && !getRedstoneControlData().receivingRedstone && newRedstoneSignal)
                getRedstoneControlData().pulsed = true;
            getRedstoneControlData().receivingRedstone = newRedstoneSignal;
            getRedstoneControlData().checkedRedstone = true;
            BlockState blockState = getBlockEntity().getBlockState();
            if (blockState.hasProperty(BlockBreakerT1.ACTIVE)) {
                getBlockEntity().getLevel().setBlockAndUpdate(getBlockEntity().getBlockPos(), blockState.setValue(BlockBreakerT1.ACTIVE, isActiveRedstoneTestOnly()));
            }
        }
    }

    default boolean isActiveRedstoneTestOnly() {
        if (getRedstoneControlData().redstoneMode.equals(MiscHelpers.RedstoneMode.IGNORED))
            return true;
        if (getRedstoneControlData().redstoneMode.equals(MiscHelpers.RedstoneMode.LOW))
            return !getRedstoneControlData().receivingRedstone;
        if (getRedstoneControlData().redstoneMode.equals(MiscHelpers.RedstoneMode.HIGH))
            return getRedstoneControlData().receivingRedstone;
        if (getRedstoneControlData().redstoneMode.equals(MiscHelpers.RedstoneMode.PULSE) && getRedstoneControlData().pulsed) {
            return true;
        }
        return false;
    }

    default boolean isActiveRedstone() {
        if (getRedstoneControlData().redstoneMode.equals(MiscHelpers.RedstoneMode.IGNORED))
            return true;
        if (getRedstoneControlData().redstoneMode.equals(MiscHelpers.RedstoneMode.LOW))
            return !getRedstoneControlData().receivingRedstone;
        if (getRedstoneControlData().redstoneMode.equals(MiscHelpers.RedstoneMode.HIGH))
            return getRedstoneControlData().receivingRedstone;
        if (getRedstoneControlData().redstoneMode.equals(MiscHelpers.RedstoneMode.PULSE) && getRedstoneControlData().pulsed) {
            getRedstoneControlData().pulsed = false;
            return true;
        }
        return false;
    }

    default void saveRedstoneSettings(ValueOutput output) {
        output.putInt("redstoneMode", getRedstoneControlData().redstoneMode.ordinal());
        output.putBoolean("pulsed", getRedstoneControlData().pulsed);
        output.putBoolean("receivingRedstone", getRedstoneControlData().receivingRedstone);
    }

    default void loadRedstoneSettings(ValueInput input) {
        input.getInt("redstoneMode").ifPresent(mode -> {
            RedstoneControlData data = getRedstoneControlData();
            data.redstoneMode = MiscHelpers.RedstoneMode.values()[mode];
            data.pulsed = input.getBooleanOr("pulsed", data.pulsed);
            data.receivingRedstone = input.getBooleanOr("receivingRedstone", data.receivingRedstone);
        });
    }
}
