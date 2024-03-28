package com.direwolf20.justdirethings.common.blockentities;

import com.direwolf20.justdirethings.common.blockentities.basebe.BaseMachineBE;
import com.direwolf20.justdirethings.common.blockentities.basebe.RedstoneControlledBE;
import com.direwolf20.justdirethings.common.blocks.BlockPlacerT1;
import com.direwolf20.justdirethings.setup.Registration;
import com.direwolf20.justdirethings.util.interfacehelpers.RedstoneControlData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class BlockPlacerT1BE extends BaseMachineBE implements RedstoneControlledBE {
    public RedstoneControlData redstoneControlData = new RedstoneControlData();
    protected Direction direction = Direction.DOWN; //To avoid nulls

    public BlockPlacerT1BE(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
        MACHINE_SLOTS = 1; //Slot for a pickaxe
        if (pBlockState.getBlock() instanceof BlockPlacerT1) //Only do this for the Tier 1, as its the only one with a facing....
            direction = getBlockState().getValue(BlockStateProperties.FACING);
    }

    public BlockPlacerT1BE(BlockPos pPos, BlockState pBlockState) {
        this(Registration.BlockPlacerT1BE.get(), pPos, pBlockState);
    }


    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
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
}
