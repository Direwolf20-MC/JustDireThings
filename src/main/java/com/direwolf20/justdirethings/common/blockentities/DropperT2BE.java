package com.direwolf20.justdirethings.common.blockentities;

import com.direwolf20.justdirethings.common.blockentities.basebe.AreaAffectingBE;
import com.direwolf20.justdirethings.common.blockentities.basebe.FilterableBE;
import com.direwolf20.justdirethings.common.blockentities.basebe.PoweredMachineBE;
import com.direwolf20.justdirethings.common.blockentities.basebe.PoweredMachineContainerData;
import com.direwolf20.justdirethings.common.capabilities.MachineEnergyStorage;
import com.direwolf20.justdirethings.common.containers.handlers.FilterBasicHandler;
import com.direwolf20.justdirethings.setup.Registration;
import com.direwolf20.justdirethings.util.ItemStackKey;
import com.direwolf20.justdirethings.util.interfacehelpers.AreaAffectingData;
import com.direwolf20.justdirethings.util.interfacehelpers.FilterData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class DropperT2BE extends DropperT1BE implements AreaAffectingBE, PoweredMachineBE, FilterableBE {
    public FilterData filterData = new FilterData();
    public AreaAffectingData areaAffectingData = new AreaAffectingData(getBlockState().getValue(BlockStateProperties.FACING));
    public final PoweredMachineContainerData poweredMachineData;
    public List<ItemStack> filteredList;

    public DropperT2BE(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
        MACHINE_SLOTS = 9; //Slot for dropping
        poweredMachineData = new PoweredMachineContainerData(this);
    }

    public DropperT2BE(BlockPos pPos, BlockState pBlockState) {
        this(Registration.DropperT2BE.get(), pPos, pBlockState);
    }

    @Override
    public PoweredMachineContainerData getContainerData() {
        return poweredMachineData;
    }

    @Override
    public MachineEnergyStorage getEnergyStorage() {
        return getData(Registration.ENERGYSTORAGE_MACHINES);
    }

    @Override
    public int getStandardEnergyCost() {
        return 25;
    }

    @Override
    public AreaAffectingData getAreaAffectingData() {
        return areaAffectingData;
    }

    @Override
    public FilterBasicHandler getFilterHandler() {
        return getData(Registration.HANDLER_BASIC_FILTER);
    }

    @Override
    public FilterData getFilterData() {
        return filterData;
    }

    public List<ItemStack> getFilteredList() {
        if (filteredList == null) {
            filteredList = new ArrayList<>();
            for (int slot = 0; slot < getFilterHandler().getSlots(); slot++) {
                ItemStack itemStack = getFilterHandler().getStackInSlot(slot);
                if (!itemStack.isEmpty()) filteredList.add(itemStack);
            }
        }
        return filteredList;
    }

    public void populateFilteredDropSlots() {
        for (ItemStack filterStack : getFilteredList()) {
            ItemStackKey filterKey = new ItemStackKey(filterStack, getFilterData().compareNBT);
            boolean foundThisOne = false;
            for (int machineSlot = 0; machineSlot < getMachineHandler().getSlots(); machineSlot++) {
                ItemStack itemStack = getMachineHandler().getStackInSlot(machineSlot);
                if (itemStack.isEmpty()) continue;
                if (filterKey.equals(new ItemStackKey(itemStack, getFilterData().compareNBT))) {
                    foundThisOne = true;
                    slotsToDropList.add(machineSlot);
                    break;
                }
            }
            if (!foundThisOne) { //If we didn't find this filtered item in the machine, clear the list and leave!
                slotsToDropList.clear();
                return;
            }
        }
    }

    public void populateDropSlots() {
        if (!getFilteredList().isEmpty()) {
            populateFilteredDropSlots();
            return;
        }
        super.populateDropSlots();
    }

    public boolean canDrop() {
        int cost = getStandardEnergyCost();
        return hasEnoughPower(cost); //Really extract because if we pass this we'll be scanning!
    }

    public BlockPos getDropPos() {
        BlockPos blockPos = getBlockPos().offset(getAreaAffectingData().xOffset, getAreaAffectingData().yOffset, getAreaAffectingData().zOffset);
        if (isBlockPosValid(blockPos))
            return blockPos;
        return null;
    }

    @Override
    public void spawnItem(Level level, ItemStack stack, double speed, Direction direction, Vec3 position) {
        super.spawnItem(level, stack, speed, direction, position);
        extractEnergy(getStandardEnergyCost(), false);
    }

    @Override
    public void setChanged() {
        super.setChanged();
        this.filteredList = null; //Null it out, so it regenerates if needed
    }
}
