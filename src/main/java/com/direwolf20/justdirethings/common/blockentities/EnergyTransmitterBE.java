package com.direwolf20.justdirethings.common.blockentities;

import com.direwolf20.justdirethings.client.particles.itemparticle.ItemFlowParticleData;
import com.direwolf20.justdirethings.common.blockentities.basebe.*;
import com.direwolf20.justdirethings.common.blocks.EnergyTransmitter;
import com.direwolf20.justdirethings.common.capabilities.EnergyStorageNoReceive;
import com.direwolf20.justdirethings.common.capabilities.MachineEnergyStorage;
import com.direwolf20.justdirethings.common.containers.handlers.FilterBasicHandler;
import com.direwolf20.justdirethings.common.items.PocketGenerator;
import com.direwolf20.justdirethings.setup.Config;
import com.direwolf20.justdirethings.setup.Registration;
import com.direwolf20.justdirethings.util.interfacehelpers.AreaAffectingData;
import com.direwolf20.justdirethings.util.interfacehelpers.FilterData;
import com.direwolf20.justdirethings.util.interfacehelpers.RedstoneControlData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;

import java.util.*;

public class EnergyTransmitterBE extends BaseMachineBE implements RedstoneControlledBE, PoweredMachineBE, AreaAffectingBE, FilterableBE {
    public RedstoneControlData redstoneControlData = new RedstoneControlData();
    public final PoweredMachineContainerData poweredMachineData;
    private final Map<BlockPos, BlockCapabilityCache<IEnergyStorage, Direction>> energyHandlers = new HashMap<>();
    private List<BlockPos> blocksToCharge = new ArrayList<>();
    private List<BlockPos> transmittersToBalance = new ArrayList<>();
    public AreaAffectingData areaAffectingData = new AreaAffectingData();
    public FilterData filterData = new FilterData();

    public EnergyTransmitterBE(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
        MACHINE_SLOTS = 1;
        poweredMachineData = new PoweredMachineContainerData(this);
        tickSpeed = 100; //We use this to check how often to rescan the area
    }

    public EnergyTransmitterBE(BlockPos pPos, BlockState pBlockState) {
        this(Registration.EnergyTransmitterBE.get(), pPos, pBlockState);
    }

    @Override
    public FilterBasicHandler getFilterHandler() {
        return getData(Registration.HANDLER_BASIC_FILTER);
    }

    @Override
    public FilterData getFilterData() {
        return filterData;
    }

    @Override
    public AreaAffectingData getAreaAffectingData() {
        return areaAffectingData;
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
        if (isActiveRedstone()) {
            if (canRun())
                getBlocksToCharge();
            drainFromSlot();
            providePower();
        }
    }

    public void doParticles(BlockPos sourcePos, BlockPos targetPos) {
        Direction sourceFacing = level.getBlockState(sourcePos).getValue(BlockStateProperties.FACING);
        Vec3 sourceVec = new Vec3(sourcePos.getX() + 0.5f - (0.3 * sourceFacing.getStepX()), sourcePos.getY() + 0.5f - (0.3 * sourceFacing.getStepY()), sourcePos.getZ() + 0.5f - (0.3 * sourceFacing.getStepZ()));
        BlockState targetState = level.getBlockState(targetPos);
        Vec3 targetVec = new Vec3(0, 0, 0);
        if (targetState.getBlock() instanceof EnergyTransmitter) {
            Direction targetFacing = targetState.getValue(BlockStateProperties.FACING);
            targetVec = new Vec3(targetPos.getX() + 0.5f - (0.3 * targetFacing.getStepX()), targetPos.getY() + 0.5f - (0.3 * targetFacing.getStepY()), targetPos.getZ() + 0.5f - (0.3 * targetFacing.getStepZ()));
        } else {
            VoxelShape voxelShape = targetState.getShape(level, targetPos); //Todo maybe use this?
            targetVec = new Vec3(targetPos.getX() + 0.5, targetPos.getY() + 0.5, targetPos.getZ() + 0.5);
        }

        ItemFlowParticleData data = new ItemFlowParticleData(new ItemStack(Items.YELLOW_CONCRETE), targetVec.x, targetVec.y, targetVec.z, 2);
        double d0 = sourceVec.x();
        double d1 = sourceVec.y();
        double d2 = sourceVec.z();
        ((ServerLevel) level).sendParticles(data, d0, d1, d2, 1, 0, 0, 0, 0);
    }

    public void drainFromSlot() {
        if (getEnergyStorage().getEnergyStored() >= getMaxEnergy()) return; //Don't do anything if already full...
        ItemStack itemStack = getMachineHandler().getStackInSlot(0);
        if (itemStack.isEmpty()) return;
        IEnergyStorage energyStorage = itemStack.getCapability(Capabilities.EnergyStorage.ITEM);
        if (energyStorage == null) return;
        if (itemStack.getItem() instanceof PocketGenerator pocketGenerator) {
            pocketGenerator.tryBurn((EnergyStorageNoReceive) energyStorage, itemStack);
        }
        transmitPower(energyStorage, getEnergyStorage(), fePerTick());
    }

    public IEnergyStorage getHandler(BlockPos blockPos) {
        var tempStorage = energyHandlers.get(blockPos);
        if (tempStorage == null) {
            boolean foundAcceptableSide = false;
            for (Direction direction : Direction.values()) {
                tempStorage = BlockCapabilityCache.create(
                        Capabilities.EnergyStorage.BLOCK, // capability to cache
                        (ServerLevel) level, // level
                        blockPos, // target position
                        direction // context (The side of the block we're trying to pull/push from?)
                );
                if (tempStorage.getCapability() != null && tempStorage.getCapability().canReceive()) {
                    energyHandlers.put(blockPos, tempStorage);
                    foundAcceptableSide = true;
                    break;
                }
            }
            if (!foundAcceptableSide)
                energyHandlers.put(blockPos, tempStorage); //Put the last one we checked, even if it can't receive!
        }
        return tempStorage.getCapability();
    }

    public void providePower() {
        if (getEnergyStorage().getEnergyStored() <= 0) return; //Don't bother if we're empty!
        for (BlockPos blockPos : blocksToCharge) {
            IEnergyStorage iEnergyStorage = getHandler(blockPos);
            if (iEnergyStorage == null) continue;
            int sentAmt = transmitPowerWithLoss(getEnergyStorage(), iEnergyStorage, fePerTick(), blockPos);
            if (sentAmt > 0)
                doParticles(getBlockPos(), blockPos);
        }
        int networkEnergy = getEnergyStored();
        for (BlockPos blockPos : transmittersToBalance) {
            IEnergyStorage iEnergyStorage = getHandler(blockPos);
            if (iEnergyStorage == null) continue;
            networkEnergy = networkEnergy + iEnergyStorage.getEnergyStored();
        }
        int balanceEnergy = networkEnergy / (transmittersToBalance.size() + 1);
        for (BlockPos blockPos : transmittersToBalance) {
            IEnergyStorage iEnergyStorage = getHandler(blockPos);
            if (iEnergyStorage == null) continue;
            int myEnergy = getEnergyStored();
            int targetEnergy = iEnergyStorage.getEnergyStored();
            if (myEnergy == targetEnergy) continue;
            if (myEnergy == getMaxEnergy() && (iEnergyStorage.getMaxEnergyStored() - targetEnergy <= 5)) {
                int sentAmt = iEnergyStorage.receiveEnergy(iEnergyStorage.getMaxEnergyStored() - targetEnergy, false); //FREE ENERGY!
                if (sentAmt > 0)
                    doParticles(getBlockPos(), blockPos);
                continue;
            }
            //int targetAmount = ((myEnergy + targetEnergy) / 2);
            if (myEnergy > targetEnergy) {
                int amtToSend = Math.min(fePerTick(), balanceEnergy - targetEnergy);
                if (amtToSend <= 0) continue; //Can happen due to integer division
                // System.out.println(getBlockPos() + " sending: " + amtToSend);
                int sentAmt = transmitPower(getEnergyStorage(), iEnergyStorage, amtToSend);
                if (sentAmt > 0)
                    doParticles(getBlockPos(), blockPos);
            } else {
                int amtToSend = Math.min(fePerTick(), balanceEnergy - myEnergy);
                if (amtToSend <= 0) continue; //Can happen due to integer division
                //System.out.println(getBlockPos() + " receiving: " + amtToSend);
                int sentAmt = transmitPower(iEnergyStorage, getEnergyStorage(), amtToSend);
                if (sentAmt > 0)
                    doParticles(blockPos, getBlockPos());
            }
        }
    }

    public int calculateLoss(int amtToSend, BlockPos remotePosition) {
        double energyLoss = (Config.ENERGY_TRANSMITTER_T1_LOSS_PER_BLOCK.get() * Math.abs(getBlockPos().distManhattan(remotePosition))) / 100;
        //System.out.println("Distance: " + Math.abs(getBlockPos().distManhattan(remotePosition)) + ".  Energy Loss: " + energyLoss + ". Send vs receive: " + amtToSend + " : " + (amtToSend - (int) (Math.ceil(amtToSend * energyLoss))));
        return amtToSend - (int) (Math.floor(amtToSend * energyLoss));
    }

    public int transmitPowerWithLoss(IEnergyStorage sender, IEnergyStorage receiver, int amtToSend, BlockPos remotePosition) {
        int amtFit = receiver.receiveEnergy(amtToSend, true);
        if (amtFit <= 0) return 0;
        int extractAmt = sender.extractEnergy(amtFit, false);
        return receiver.receiveEnergy(calculateLoss(extractAmt, remotePosition), false);
    }

    public int transmitPower(IEnergyStorage sender, IEnergyStorage receiver, int amtToSend) {
        int amtFit = receiver.receiveEnergy(amtToSend, true);
        if (amtFit <= 0) return 0;
        int extractAmt = sender.extractEnergy(amtFit, false);
        return receiver.receiveEnergy(extractAmt, false);
    }

    public void getBlocksToCharge() {
        transmittersToBalance.clear();
        blocksToCharge.clear();
        AABB area = getAABB(getBlockPos());
        BlockPos.betweenClosedStream((int) area.minX, (int) area.minY, (int) area.minZ, (int) area.maxX - 1, (int) area.maxY - 1, (int) area.maxZ - 1)
                .map(BlockPos::immutable)
                .sorted(Comparator.comparingDouble(x -> x.distSqr(getBlockPos())))
                .forEach(blockPos -> {
                    if (blockPos.equals(getBlockPos())) return; //No charging yourself!
                    BlockState blockState = level.getBlockState(blockPos);
                    if (blockState.isAir() || level.getBlockEntity(blockPos) == null) return;

                    boolean foundAcceptableSide = false;
                    for (Direction direction : Direction.values()) {
                        var cap = level.getCapability(Capabilities.EnergyStorage.BLOCK, blockPos, direction);
                        if (cap != null && cap.canReceive()) {
                            foundAcceptableSide = true;
                            break;
                        }
                    }
                    if (!foundAcceptableSide)
                        return;

                    ItemStack blockItemStack = blockState.getBlock().getCloneItemStack(level, blockPos, blockState);
                    if (!isStackValidFilter(blockItemStack)) return;

                    if (blockState.getBlock() instanceof EnergyTransmitter)
                        transmittersToBalance.add(blockPos);
                    else
                        blocksToCharge.add(blockPos);
                });
        energyHandlers.entrySet().removeIf(entry -> !transmittersToBalance.contains(entry.getKey()) && !(blocksToCharge.contains(entry.getKey())));
    }

    public int fePerTick() {
        return Config.ENERGY_TRANSMITTER_T1_RF_PER_TICK.get();
    }

    @Override
    public int getMaxEnergy() {
        return Config.ENERGY_TRANSMITTER_T1_MAX_RF.get();
    }

    @Override
    public boolean isDefaultSettings() {
        if (!super.isDefaultSettings())
            return false;
        return true;
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
    }
}
