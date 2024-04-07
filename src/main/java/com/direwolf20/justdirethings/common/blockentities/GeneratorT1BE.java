package com.direwolf20.justdirethings.common.blockentities;

import com.direwolf20.justdirethings.common.blockentities.basebe.BaseMachineBE;
import com.direwolf20.justdirethings.common.blockentities.basebe.PoweredMachineBE;
import com.direwolf20.justdirethings.common.blockentities.basebe.RedstoneControlledBE;
import com.direwolf20.justdirethings.common.capabilities.MachineEnergyStorage;
import com.direwolf20.justdirethings.setup.Config;
import com.direwolf20.justdirethings.setup.Registration;
import com.direwolf20.justdirethings.util.interfacehelpers.RedstoneControlData;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.CommonHooks;

public class GeneratorT1BE extends BaseMachineBE implements RedstoneControlledBE, PoweredMachineBE {
    public RedstoneControlData redstoneControlData = new RedstoneControlData();
    public final ContainerData poweredMachineData;
    //private boolean isBurning = false;
    public int maxBurn = 0;
    public int burnRemaining = 0;
    public int feRemaining = 0;

    public GeneratorT1BE(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
        MACHINE_SLOTS = 1;
        poweredMachineData = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> getEnergyStored() & 0xFFFF;
                    case 1 -> getEnergyStored() >> 16;
                    case 2 -> burnRemaining;
                    case 3 -> maxBurn;
                    default -> throw new IllegalArgumentException("Invalid index: " + index);
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> setEnergyStored((getEnergyStored() & 0xFFFF0000) | (value & 0xFFFF));
                    case 1 -> setEnergyStored((getEnergyStored() & 0xFFFF) | (value << 16));
                    case 2 -> burnRemaining = value;
                    case 3 -> maxBurn = value;
                    default -> throw new IllegalArgumentException("Invalid index: " + index);
                }
            }

            @Override
            public int getCount() {
                return 4;
            }
        };
    }

    public GeneratorT1BE(BlockPos pPos, BlockState pBlockState) {
        this(Registration.GeneratorT1BE.get(), pPos, pBlockState);
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
    public ContainerData getContainerData() {
        return poweredMachineData;
    }

    @Override
    public MachineEnergyStorage getEnergyStorage() {
        return getData(Registration.ENERGYSTORAGE_MACHINES);
    }

    @Override
    public int getStandardEnergyCost() {
        return 0;
    }

    @Override
    public void tickClient() {
    }

    @Override
    public void tickServer() {
        super.tickServer();
        doGenerate();
    }

    public void doBurn() {
        if (feRemaining > 0) return; //Don't burn fuel if we're already burning
        maxBurn = 0; //If not already burning, we must have finished burning something (or never did), so set maxBurn to 0
        burnRemaining = 0;
        boolean canInsertEnergy = insertEnergy(fePerTick(), true) > 0;
        if (!canInsertEnergy) return; //Don't burn if the buffer is full
        ItemStack fuelStack = getMachineHandler().getStackInSlot(0);
        if (fuelStack.isEmpty())
            return; //Stop if we have no fuel! The slot only accepts burnables, so this should be a good enough check

        int burnTime = CommonHooks.getBurnTime(fuelStack, RecipeType.SMELTING);
        if (burnTime <= 0) return; //Should be impossible, but lets be sure!
        if (fuelStack.hasCraftingRemainingItem())
            getMachineHandler().setStackInSlot(0, fuelStack.getCraftingRemainingItem());
        else
            fuelStack.shrink(1);

        feRemaining = burnTime * getFePerFuelTick();
        maxBurn = (int) (Math.floor(burnTime) / getBurnSpeedMultiplier());
        burnRemaining = maxBurn + 1; //1 extra tick just in case we have a rounding error.  It'll get removed above
    }

    public void doGenerate() {
        if (isActiveRedstone() && feRemaining == 0)
            doBurn(); //Only burn if redstone is active and its not already burning
        if (feRemaining == 0) return; //If we failed to burn anything
        int insertAmt = Math.min(fePerTick(), feRemaining);
        insertEnergy(insertAmt, false); //Receive energy
        feRemaining = feRemaining - insertAmt;
        burnRemaining--;
        if (isActiveRedstone() && feRemaining == 0)
            doBurn(); //Kickoff the next burn
        setChanged(); // Mark Dirty Client?
    }

    @Override
    public void handleTicks() {
        //NoOp
    }

    @Override
    public boolean canRun() {
        return true;
    }

    public int fePerTick() {
        return (int) (getFePerFuelTick() * getBurnSpeedMultiplier());
    }

    @Override
    public int getMaxEnergy() {
        return Config.GENERATOR_T1_MAX_FE.get();
    }

    public int getFEPerTick() {
        return Config.GENERATOR_T1_FE_PER_TICK.get();
    }

    public int getFePerFuelTick() {
        return Config.GENERATOR_T1_FE_PER_FUEL_TICK.get();
    }

    public int getBurnSpeedMultiplier() {
        return Config.GENERATOR_T1_BURN_SPEED_MULTIPLIER.get();
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("burnRemaining", burnRemaining);
        tag.putInt("maxBurn", maxBurn);
        tag.putInt("feRemaining", feRemaining);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.burnRemaining = tag.getInt("burnRemaining");
        this.maxBurn = tag.getInt("maxBurn");
        this.feRemaining = tag.getInt("feRemaining");
    }
}
