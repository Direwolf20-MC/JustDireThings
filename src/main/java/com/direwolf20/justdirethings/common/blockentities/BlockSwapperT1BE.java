package com.direwolf20.justdirethings.common.blockentities;

import com.direwolf20.justdirethings.common.blockentities.basebe.BaseMachineBE;
import com.direwolf20.justdirethings.common.blockentities.basebe.RedstoneControlledBE;
import com.direwolf20.justdirethings.common.blocks.BlockSwapperT1;
import com.direwolf20.justdirethings.datagen.JustDireBlockTags;
import com.direwolf20.justdirethings.setup.Registration;
import com.direwolf20.justdirethings.util.MiscHelpers;
import com.direwolf20.justdirethings.util.interfacehelpers.RedstoneControlData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BlockSwapperT1BE extends BaseMachineBE implements RedstoneControlledBE {
    public RedstoneControlData redstoneControlData = new RedstoneControlData();
    protected Direction FACING = Direction.DOWN; //To avoid nulls
    List<BlockPos> positions = new ArrayList<>();

    public BlockSwapperT1BE(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
        if (pBlockState.getBlock() instanceof BlockSwapperT1) //Only do this for the Tier 1, as its the only one with a facing....
            FACING = getBlockState().getValue(BlockStateProperties.FACING);
    }

    public BlockSwapperT1BE(BlockPos pPos, BlockState pBlockState) {
        this(Registration.BlockSwapperT1BE.get(), pPos, pBlockState);
    }

    @Override
    public RedstoneControlData getRedstoneControlData() {
        return redstoneControlData;
    }

    @Override
    public BlockEntity getBlockEntity() {
        return this;
    }

    @Override
    public void tickClient() {
    }

    @Override
    public void tickServer() {
        super.tickServer();
    }

    public boolean canSwap() {
        return true;
    }

    public boolean clearTrackerIfNeeded() {
        if (positions.isEmpty())
            return false;
        if (!canSwap())
            return true;
        if (!isActiveRedstone() && !redstoneControlData.redstoneMode.equals(MiscHelpers.RedstoneMode.PULSE))
            return true;
        return false;
    }

    public void doSwap() {
        if (clearTrackerIfNeeded()) {
            positions.clear();
            return;
        }
        if (!canSwap()) return;
        if (isActiveRedstone() && canRun() && positions.isEmpty())
            positions = findSpotsToSwap();
        if (positions.isEmpty())
            return;
        Iterator<BlockPos> iterator = positions.iterator();
        while (iterator.hasNext()) {
            swapBlock(iterator.next());
            iterator.remove();
        }
    }

    public void swapBlock(BlockPos blockPos) {
        Direction placing = Direction.values()[direction];
        //TODO
    }

    public boolean isBlockPosValid(BlockPos blockPos) {
        if (level.getBlockState(blockPos).is(JustDireBlockTags.NO_MOVE))
            return false;
        return true;
    }

    public List<BlockPos> findSpotsToSwap() {
        List<BlockPos> returnList = new ArrayList<>();
        BlockPos blockPos = getBlockPos().relative(FACING);
        if (isBlockPosValid(blockPos))
            returnList.add(blockPos);
        return returnList;
    }
}
