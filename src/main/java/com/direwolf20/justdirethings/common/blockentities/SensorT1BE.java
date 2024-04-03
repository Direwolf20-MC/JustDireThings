package com.direwolf20.justdirethings.common.blockentities;

import com.direwolf20.justdirethings.common.blockentities.basebe.BaseMachineBE;
import com.direwolf20.justdirethings.common.blockentities.basebe.FilterableBE;
import com.direwolf20.justdirethings.common.blocks.SensorT1;
import com.direwolf20.justdirethings.common.containers.handlers.FilterBasicHandler;
import com.direwolf20.justdirethings.setup.Registration;
import com.direwolf20.justdirethings.util.interfacehelpers.FilterData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.ArrayList;
import java.util.List;

public class SensorT1BE extends BaseMachineBE implements FilterableBE {
    protected Direction FACING = Direction.DOWN; //To avoid nulls
    public FilterData filterData = new FilterData();
    protected List<BlockPos> positions = new ArrayList<>();
    public SENSE_TARGET sense_target = SENSE_TARGET.BLOCK;
    public boolean emitRedstone = false;
    public boolean strongSignal = false;

    public enum SENSE_TARGET {
        BLOCK,
        AIR,
        HOSTILE,
        PASSIVE,
        ADULT,
        CHILD,
        PLAYER,
        LIVING,
        ITEM;

        public SENSE_TARGET next() {
            SENSE_TARGET[] values = values();
            int nextOrdinal = (this.ordinal() + 1) % values.length;
            return values[nextOrdinal];
        }
    }

    public SensorT1BE(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
        ANYSIZE_FILTER_SLOTS = 1;
        if (pBlockState.getBlock() instanceof SensorT1) //Only do this for the Tier 1, as its the only one with a facing....
            FACING = getBlockState().getValue(BlockStateProperties.FACING);
    }

    public SensorT1BE(BlockPos pPos, BlockState pBlockState) {
        this(Registration.SensorT1BE.get(), pPos, pBlockState);
    }

    @Override
    public FilterBasicHandler getFilterHandler() {
        return getData(Registration.HANDLER_BASIC_FILTER_ANYSIZE);
    }

    @Override
    public FilterData getFilterData() {
        return filterData;
    }

    @Override
    public BlockEntity getBlockEntity() {
        return this;
    }

    public void setSensorSettings(int senseTarget, boolean strongSignal) {
        this.sense_target = SENSE_TARGET.values()[senseTarget];
        setRedstone(emitRedstone, strongSignal); //Gonna wanna update the neighbors if strongSignal changed
        positions.clear(); //Clear any clicks we have queue'd up
        markDirtyClient();
    }

    @Override
    public void tickClient() {
    }

    @Override
    public void tickServer() {
        super.tickServer();
        sense();
    }

    public boolean canSense() {
        return true;
    }

    public boolean clearTrackerIfNeeded() {
        return false;
    }

    public void setRedstone(boolean emit, boolean strong) {
        if (emit != emitRedstone || strong != strongSignal) {
            emitRedstone = emit;
            strongSignal = strong;
            for (Direction direction : Direction.values()) {
                level.neighborChanged(getBlockPos().relative(direction), this.getBlockState().getBlock(), getBlockPos());
                level.updateNeighborsAtExceptFromFacing(getBlockPos().relative(direction), this.getBlockState().getBlock(), direction.getOpposite());
            }
        }
    }

    public void sense() {
        if (clearTrackerIfNeeded()) {
            positions.clear();
            return;
        }
        if (!canSense()) return;
        if ((sense_target.equals(SENSE_TARGET.BLOCK) || sense_target.equals(SENSE_TARGET.AIR))) {
            if (canRun() && positions.isEmpty())
                positions = findPositions();
            if (positions.isEmpty())
                return;
            if (canRun()) {
                BlockPos blockPos = positions.remove(0);
                setRedstone(senseBlock(blockPos), strongSignal);
            }
        } else { //Todo Entities
            /*if (isActiveRedstone() && canRun() && entitiesToClick.isEmpty())
                entitiesToClick = findEntitiesToClick(getAABB());
            if (entitiesToClick.isEmpty())
                return;
            if (canRun()) {
                LivingEntity entity = entitiesToClick.remove(0);
                clickEntity(placeStack, fakePlayer, entity);
            }*/
        }
    }

    public boolean senseBlock(BlockPos blockPos) {
        BlockState blockState = level.getBlockState(blockPos);
        if (sense_target.equals(SENSE_TARGET.AIR))
            return blockState.isAir();
        if (blockState.isAir()) //Checked above if we're sensing for air, so if its air, false!
            return false;
        ItemStack blockItemStack = blockState.getBlock().getCloneItemStack(level, blockPos, blockState);
        boolean doesBlockMatch = isStackValidFilter(blockItemStack);
        if (!doesBlockMatch)
            return false;
        return handleBlockStates(blockState);
    }

    public boolean handleBlockStates(BlockState blockState) {
        return true; //Todo
    }

    public boolean isBlockPosValid(BlockPos blockPos) {
        return true;
    }

    public List<BlockPos> findPositions() {
        List<BlockPos> returnList = new ArrayList<>();
        BlockPos blockPos = getBlockPos().relative(FACING);
        if (isBlockPosValid(blockPos))
            returnList.add(blockPos);
        return returnList;
    }

    @Override
    public boolean isDefaultSettings() {
        if (!super.isDefaultSettings())
            return false;
        if (!sense_target.equals(SENSE_TARGET.BLOCK))
            return false;
        if (strongSignal)
            return false;
        return true;
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("senseTarget", sense_target.ordinal());
        tag.putBoolean("strongSignal", strongSignal);
    }

    @Override
    public void load(CompoundTag tag) {
        this.sense_target = SENSE_TARGET.values()[tag.getInt("senseTarget")];
        this.strongSignal = tag.getBoolean("strongSignal");
        super.load(tag);
    }
}
