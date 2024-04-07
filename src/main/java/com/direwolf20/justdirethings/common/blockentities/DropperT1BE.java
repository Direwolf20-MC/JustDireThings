package com.direwolf20.justdirethings.common.blockentities;

import com.direwolf20.justdirethings.common.blockentities.basebe.BaseMachineBE;
import com.direwolf20.justdirethings.common.blockentities.basebe.RedstoneControlledBE;
import com.direwolf20.justdirethings.common.blocks.DropperT1;
import com.direwolf20.justdirethings.setup.Registration;
import com.direwolf20.justdirethings.util.MiscHelpers;
import com.direwolf20.justdirethings.util.interfacehelpers.RedstoneControlData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class DropperT1BE extends BaseMachineBE implements RedstoneControlledBE {
    public RedstoneControlData redstoneControlData = new RedstoneControlData();
    protected Direction FACING = Direction.DOWN; //To avoid nulls
    public int dropCount = 1;
    public List<Integer> slotsToDropList = new ArrayList<>();

    public DropperT1BE(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
        MACHINE_SLOTS = 1; //Slot for dropping
        if (pBlockState.getBlock() instanceof DropperT1) //Only do this for the Tier 1, as its the only one with a facing....
            FACING = getBlockState().getValue(BlockStateProperties.FACING);
    }

    public DropperT1BE(BlockPos pPos, BlockState pBlockState) {
        this(Registration.DropperT1BE.get(), pPos, pBlockState);
    }

    public void setDropperSettings(int dropCount) {
        this.dropCount = dropCount;
        markDirtyClient();
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
        doDrop();
    }

    public boolean clearTrackerIfNeeded() {
        if (slotsToDropList.isEmpty())
            return false;
        if (!canDrop())
            return true;
        if (!isActiveRedstone() && !redstoneControlData.redstoneMode.equals(MiscHelpers.RedstoneMode.PULSE))
            return true;
        return false;
    }

    public void populateDropSlots() {
        for (int slot = 0; slot < getMachineHandler().getSlots(); slot++) {
            ItemStack itemStack = getMachineHandler().getStackInSlot(slot);
            if (!itemStack.isEmpty()) {
                slotsToDropList.add(slot);
                return;
            }
        }
    }

    public boolean canDrop() {
        return true;
    }

    public void doDrop() {
        if (clearTrackerIfNeeded()) {
            slotsToDropList.clear();
            return;
        }
        if (!canDrop()) return;
        if (isActiveRedstone() && canRun() && slotsToDropList.isEmpty())
            populateDropSlots();
        if (slotsToDropList.isEmpty())
            return;
        if (operationTicks == 0) { //Use this check, because we want to ensure we drop once every <Ticks>
            ItemStack dropStack = getMachineHandler().getStackInSlot(slotsToDropList.remove(0));
            if (dropStack.isEmpty()) { //If this occurs, something changed, so clear the remaining list of things to drop
                slotsToDropList.clear();
                return;
            }
            BlockPos dropPos = getDropPos();
            if (dropPos == null) return; //Happens if the position is invalid - like not air...
            spawnItem(level, dropStack.split(dropCount), 0.3, Direction.values()[this.direction], new Vec3(dropPos.getX() + 0.5, dropPos.getY() + 0.5, dropPos.getZ() + 0.5));
        }
    }

    public void spawnItem(Level level, ItemStack stack, double speed, Direction direction, Vec3 position) {
        double d0 = position.x();
        double d1 = position.y();
        double d2 = position.z();

        ItemEntity itementity = new ItemEntity(level, d0, d1, d2, stack);
        itementity.setDeltaMovement(
                (double) direction.getStepX() * speed,
                (double) direction.getStepY() * speed,
                (double) direction.getStepZ() * speed
        );
        level.addFreshEntity(itementity);
    }

    public boolean isBlockPosValid(BlockPos blockPos) {
        if (!level.getBlockState(blockPos).isAir())
            return false;
        return true;
    }

    public BlockPos getDropPos() {
        BlockPos blockPos = getBlockPos().relative(FACING);
        if (isBlockPosValid(blockPos))
            return blockPos;
        return null;
    }

    @Override
    public boolean isDefaultSettings() {
        if (!super.isDefaultSettings())
            return false;
        if (dropCount != 1)
            return false;
        return true;
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("dropCount", dropCount);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.dropCount = tag.getInt("dropCount");
    }
}
