package com.direwolf20.justdirethings.common.blockentities;

import com.direwolf20.justdirethings.common.blockentities.basebe.*;
import com.direwolf20.justdirethings.common.capabilities.EnergyStorageNoReceive;
import com.direwolf20.justdirethings.common.capabilities.GeneratorFluidItemHandler;
import com.direwolf20.justdirethings.common.capabilities.JustDireFluidTank;
import com.direwolf20.justdirethings.common.capabilities.MachineEnergyStorage;
import com.direwolf20.justdirethings.common.fluids.basefluids.RefinedFuel;
import com.direwolf20.justdirethings.setup.Config;
import com.direwolf20.justdirethings.setup.JDTRegistration;
import com.direwolf20.justdirethings.util.interfacehelpers.RedstoneControlData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

import java.util.HashMap;
import java.util.Map;

public class GeneratorFluidT1BE extends BaseMachineBE implements RedstoneControlledBE, PoweredMachineBE, FluidMachineBE {
    public RedstoneControlData redstoneControlData = new RedstoneControlData();
    public final PoweredMachineContainerData poweredMachineData;
    public final FluidContainerData fluidContainerData;
    private final Map<Direction, BlockCapabilityCache<EnergyHandler, Direction>> energyHandlers = new HashMap<>();

    public GeneratorFluidT1BE(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
        MACHINE_SLOTS = 1;
        fluidContainerData = new FluidContainerData(this);
        poweredMachineData = new PoweredMachineContainerData(this);
    }

    public GeneratorFluidT1BE(BlockPos pPos, BlockState pBlockState) {
        this(JDTRegistration.GeneratorFluidT1BE.get(), pPos, pBlockState);
    }

    @Override
    public int getMaxMB() {
        return 4000;
    }

    public JustDireFluidTank getFluidTank() {
        return getData(JDTRegistration.GENERATOR_FLUID_HANDLER);
    }

    @Override
    public ContainerData getFluidContainerData() {
        return fluidContainerData;
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
        return getData(JDTRegistration.ENERGYSTORAGE_GENERATORS);
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
        handleItemStack();
        doGenerate();
        providePowerAdjacent();
    }

    public ItemStack getItemStack() {
        return getMachineHandler().getResource(0).toStack(getMachineHandler().getAmountAsInt(0));
    }

    @Override
    public GeneratorFluidItemHandler getMachineHandler() {
        return getData(JDTRegistration.GENERATOR_FLUID_ITEM_HANDLER);
    }

    public boolean isStackValid(ItemStack itemStack) {
        if (itemStack.isEmpty())
            return false;
        ResourceHandler<FluidResource> fluidHandler = ItemAccess.forStack(itemStack).getCapability(Capabilities.Fluid.ITEM);
        if (fluidHandler == null)
            return false;
        // Probe for any non-empty fluid in the item
        FluidResource probedResource = null;
        int probedAmount = 0;
        try (Transaction tx = Transaction.openRoot()) {
            for (int i = 0; i < fluidHandler.size(); i++) {
                FluidResource r = fluidHandler.getResource(i);
                if (r.isEmpty()) continue;
                int extracted = fluidHandler.extract(i, r, 1000, tx);
                if (extracted > 0) {
                    probedResource = r;
                    probedAmount = extracted;
                    break;
                }
            }
        }
        if (probedResource == null || probedAmount == 0)
            return false;
        if (!getFluidStack().isEmpty() && !getFluidStack().is(probedResource.getFluid()))
            return false;
        if (!getFluidTank().isValid(0, probedResource))
            return false;
        return true;
    }

    public void handleItemStack() {
        if (isFull()) return;
        ItemStack itemStack = getItemStack();
        if (!isStackValid(itemStack)) return;
        ItemAccess itemAccess = ItemAccess.forHandlerIndex(getMachineHandler(), 0);
        ResourceHandler<FluidResource> fluidHandler = itemAccess.getCapability(Capabilities.Fluid.ITEM);
        if (fluidHandler == null) return;

        // Find the non-empty fluid slot and transfer
        try (Transaction tx = Transaction.openRoot()) {
            for (int i = 0; i < fluidHandler.size(); i++) {
                FluidResource resource = fluidHandler.getResource(i);
                if (resource.isEmpty()) continue;
                int amountAvail = fluidHandler.getAmountAsInt(i);
                if (amountAvail <= 0) continue;
                int capacityFit;
                try (Transaction simTx = Transaction.open(tx)) {
                    capacityFit = getFluidTank().insert(0, resource, Math.min(amountAvail, 1000), simTx);
                }
                if (capacityFit <= 0) continue;
                int extracted = fluidHandler.extract(i, resource, capacityFit, tx);
                getFluidTank().insert(0, resource, extracted, tx);
                tx.commit();
                if (itemStack.getItem() instanceof BucketItem) {
                    // For buckets, the drained container should become an empty bucket.
                    // Since ItemAccess.exchange handles this via the fluid capability provider,
                    // the slot should already reflect the new state. No manual swap needed.
                }
                return;
            }
        }
    }

    @Override
    public int insertEnergy(int power, boolean simulate) {
        MachineEnergyStorage energyStorage = getEnergyStorage();
        if (energyStorage instanceof EnergyStorageNoReceive energyStorageNoReceive)
            return energyStorageNoReceive.forceReceiveEnergy(power, simulate);
        return 0;
    }

    public EnergyHandler getHandler(Direction direction) {
        var tempStorage = energyHandlers.get(direction);
        if (tempStorage == null) {
            BlockPos targetPos = getBlockPos().relative(direction);
            tempStorage = BlockCapabilityCache.create(
                    Capabilities.Energy.BLOCK,
                    (ServerLevel) level,
                    targetPos,
                    direction.getOpposite()
            );
            energyHandlers.put(direction, tempStorage);
        }
        return tempStorage.getCapability();
    }

    public void providePowerAdjacent() {
        if (getEnergyStorage().getEnergyStored() <= 0) return;
        for (Direction direction : Direction.values()) {
            EnergyHandler energyHandler = getHandler(direction);
            if (energyHandler == null) continue;
            int amtFit;
            try (Transaction simTx = Transaction.openRoot()) {
                amtFit = energyHandler.insert(getFEOutputPerTick() * 10, simTx);
            }
            if (amtFit <= 0) continue;
            try (Transaction tx = Transaction.openRoot()) {
                int extractAmt = getEnergyStorage().extract(amtFit, tx);
                energyHandler.insert(extractAmt, tx);
                tx.commit();
            }
        }
    }

    public void doGenerate() {
        if (!isActiveRedstone() || getFluidStack().isEmpty()) return;
        int fePerFuelTick = getFePerFuelTick();
        boolean canInsertEnergy = insertEnergy(fePerFuelTick, true) == fePerFuelTick;
        if (fePerFuelTick == 0 || !canInsertEnergy) return;

        FluidResource tankResource = getFluidTank().getResource(0);
        if (tankResource.isEmpty()) return;
        try (Transaction tx = Transaction.openRoot()) {
            int extracted = getFluidTank().extract(0, tankResource, 1, tx);
            if (extracted == 0) return;
            tx.commit();
        }

        insertEnergy(fePerFuelTick, false);

        setChanged();
    }

    @Override
    public void handleTicks() {
        //NoOp
    }

    @Override
    public boolean canRun() {
        return true;
    }

    @Override
    public int getMaxEnergy() {
        return Config.GENERATOR_FLUID_T1_MAX_FE.get();
    }

    public int getFEOutputPerTick() {
        return Config.GENERATOR_FLUID_T1_FE_PER_TICK.get();
    }

    public int getFePerFuelTick() {
        FluidResource resource = getFluidTank().getResource(0);
        if (resource.isEmpty()) return 0;
        return resource.getFluid() instanceof RefinedFuel refinedFuel ? refinedFuel.fePerMb() : 0;
    }
}
