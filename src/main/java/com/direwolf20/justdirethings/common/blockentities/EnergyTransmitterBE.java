package com.direwolf20.justdirethings.common.blockentities;

import com.direwolf20.justdirethings.client.particles.itemparticle.ItemFlowParticleData;
import com.direwolf20.justdirethings.common.blockentities.basebe.*;
import com.direwolf20.justdirethings.common.blocks.EnergyTransmitter;
import com.direwolf20.justdirethings.common.capabilities.EnergyStorageItemStackNoReceive;
import com.direwolf20.justdirethings.common.capabilities.TransmitterEnergyStorage;
import com.direwolf20.justdirethings.common.containers.handlers.FilterBasicHandler;
import com.direwolf20.justdirethings.common.items.PocketGenerator;
import com.direwolf20.justdirethings.setup.Config;
import com.direwolf20.justdirethings.setup.JDTRegistration;
import com.direwolf20.justdirethings.util.interfacehelpers.AreaAffectingData;
import com.direwolf20.justdirethings.util.interfacehelpers.FilterData;
import com.direwolf20.justdirethings.util.interfacehelpers.RedstoneControlData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;

import java.util.*;
import java.util.stream.Collectors;

public class EnergyTransmitterBE extends BaseMachineBE implements RedstoneControlledBE, PoweredMachineBE, AreaAffectingBE, FilterableBE {
    public RedstoneControlData redstoneControlData = new RedstoneControlData();
    public final PoweredMachineContainerData poweredMachineData;
    private final Map<BlockPos, BlockCapabilityCache<EnergyHandler, Direction>> energyHandlers = new HashMap<>();
    private final Map<BlockPos, BlockCapabilityCache<EnergyHandler, Direction>> transmitterHandlers = new HashMap<>();
    private final Set<BlockPos> blocksToCharge = new HashSet<>();
    private final Set<BlockPos> transmitters = new HashSet<>();
    public AreaAffectingData areaAffectingData = new AreaAffectingData();
    public FilterData filterData = new FilterData();
    public boolean showParticles = true;

    public EnergyTransmitterBE(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
        MACHINE_SLOTS = 1;
        poweredMachineData = new PoweredMachineContainerData(this);
        tickSpeed = 50;
    }

    public EnergyTransmitterBE(BlockPos pPos, BlockState pBlockState) {
        this(JDTRegistration.EnergyTransmitterBE.get(), pPos, pBlockState);
    }

    public void setEnergyTransmitterSettings(boolean showParticles) {
        this.showParticles = showParticles;
        markDirtyClient();
    }

    @Override
    public FilterBasicHandler getFilterHandler() {
        return getData(JDTRegistration.HANDLER_BASIC_FILTER);
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
    public TransmitterEnergyStorage getEnergyStorage() {
        return getData(JDTRegistration.ENERGYSTORAGE_TRANSMITTERS);
    }

    @Override
    public int getEnergyStored() {
        return getEnergyStorage().getRealEnergyStored();
    }

    @Override
    public int getStandardEnergyCost() {
        return 0;
    }

    @Override
    public void tickClient() {
    }

    public Map<BlockPos, TransmitterEnergyStorage> getTransmitterEnergyStorages() {
        return transmitters.stream()
                .map(pos -> new AbstractMap.SimpleEntry<>(pos, getTransmitterEnergyHandler(pos)))
                .filter(entry -> entry.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public void balanceEnergy() {
        if (transmitters.isEmpty() || transmitters.size() == 1) return;
        Map<BlockPos, TransmitterEnergyStorage> transmitterEnergyStorages = getTransmitterEnergyStorages();

        int totalEnergy = transmitterEnergyStorages.values().stream()
                .mapToInt(TransmitterEnergyStorage::getRealEnergyStored)
                .sum();
        int count = transmitterEnergyStorages.size();
        int averageEnergy = totalEnergy / count;
        int remainder = totalEnergy % count;

        if (isAlreadyBalanced(transmitterEnergyStorages, averageEnergy, remainder)) {
            return;
        }

        int i = 0;
        for (Map.Entry<BlockPos, TransmitterEnergyStorage> entry : transmitterEnergyStorages.entrySet()) {
            if (i < remainder) {
                entry.getValue().setEnergy(averageEnergy + 1);
            } else {
                entry.getValue().setEnergy(averageEnergy);
            }
            i++;
            doParticles(getBlockPos(), entry.getKey());
        }
    }

    private boolean isAlreadyBalanced(Map<BlockPos, TransmitterEnergyStorage> transmitterEnergyStorages, int averageEnergy, int remainder) {
        int minEnergy = averageEnergy;
        int maxEnergy = averageEnergy + (remainder > 0 ? 1 : 0);

        return transmitterEnergyStorages.values().stream().allMatch((transmitterEnergyStorage) -> {
            int energy = transmitterEnergyStorage.getRealEnergyStored();
            return energy == minEnergy || energy == maxEnergy;
        });
    }

    public int getTotalEnergyStored() {
        return getTransmitterEnergyStorages().values().stream()
                .mapToInt(TransmitterEnergyStorage::getRealEnergyStored)
                .sum();
    }

    public int getTotalMaxEnergyStored() {
        return getTransmitterEnergyStorages().values().stream()
                .mapToInt(TransmitterEnergyStorage::getRealMaxEnergyStored)
                .sum();
    }

    public int distributeEnergy(int energy) {
        int energyInserted = 0;
        for (TransmitterEnergyStorage transmitterEnergyStorage : getTransmitterEnergyStorages().values()) {
            int insertedEnergy = transmitterEnergyStorage.realReceiveEnergy(energy, false);
            energy = energy - insertedEnergy;
            energyInserted = energyInserted + insertedEnergy;
            if (energy <= 0) break;
        }
        return energyInserted;
    }

    public int extractEnergy(int energy) {
        int energyExtracted = 0;
        for (TransmitterEnergyStorage transmitterEnergyStorage : getTransmitterEnergyStorages().values()) {
            int extractedEnergy = transmitterEnergyStorage.realExtractEnergy(energy, false);
            energy = energy - extractedEnergy;
            energyExtracted = energyExtracted + extractedEnergy;
            if (energy <= 0) break;
        }
        return energyExtracted;
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
        if (!showParticles) return;
        Direction sourceFacing = level.getBlockState(sourcePos).getValue(BlockStateProperties.FACING);
        Vec3 sourceVec = new Vec3(sourcePos.getX() + 0.5f - (0.3 * sourceFacing.getStepX()), sourcePos.getY() + 0.5f - (0.3 * sourceFacing.getStepY()), sourcePos.getZ() + 0.5f - (0.3 * sourceFacing.getStepZ()));
        BlockState targetState = level.getBlockState(targetPos);
        Vec3 targetVec = new Vec3(0, 0, 0);
        if (targetState.getBlock() instanceof EnergyTransmitter) {
            Direction targetFacing = targetState.getValue(BlockStateProperties.FACING);
            targetVec = new Vec3(targetPos.getX() + 0.5f - (0.3 * targetFacing.getStepX()), targetPos.getY() + 0.5f - (0.3 * targetFacing.getStepY()), targetPos.getZ() + 0.5f - (0.3 * targetFacing.getStepZ()));
        } else {
            VoxelShape voxelShape = targetState.getShape(level, targetPos);
            targetVec = new Vec3(targetPos.getX() + 0.5, targetPos.getY() + 0.5, targetPos.getZ() + 0.5);
        }

        ItemFlowParticleData data = new ItemFlowParticleData(new ItemStack(Items.YELLOW_CONCRETE), targetVec.x, targetVec.y, targetVec.z, 2);
        double d0 = sourceVec.x();
        double d1 = sourceVec.y();
        double d2 = sourceVec.z();
        ((ServerLevel) level).sendParticles(data, d0, d1, d2, 1, 0, 0, 0, 0);
    }

    public void drainFromSlot() {
        ItemStack itemStack = getMachineHandler().getResource(0).toStack(getMachineHandler().getAmountAsInt(0));
        if (itemStack.isEmpty()) return;
        ItemAccess itemAccess = ItemAccess.forHandlerIndex(getMachineHandler(), 0);
        EnergyHandler energyStorage = itemAccess.getCapability(Capabilities.Energy.ITEM);
        if (energyStorage == null) return;
        if (itemStack.getItem() instanceof PocketGenerator pocketGenerator) {
            if (energyStorage instanceof EnergyStorageItemStackNoReceive noReceive) {
                pocketGenerator.tryBurn(noReceive, itemStack, (ServerLevel) level);
            }
        }
        if (getEnergyStorage().getEnergyStored() >= getEnergyStorage().getMaxEnergyStored())
            return;
        transmitPower(energyStorage, getEnergyStorage(), fePerTick());
    }

    public EnergyHandler getHandler(BlockPos blockPos) {
        var tempStorage = energyHandlers.get(blockPos);
        if (tempStorage == null) {
            boolean foundAcceptableSide = false;
            for (Direction direction : Direction.values()) {
                tempStorage = BlockCapabilityCache.create(
                        Capabilities.Energy.BLOCK,
                        (ServerLevel) level,
                        blockPos,
                        direction
                );
                EnergyHandler cap = tempStorage.getCapability();
                if (cap != null && cap.getCapacityAsLong() - cap.getAmountAsLong() > 0) {
                    energyHandlers.put(blockPos, tempStorage);
                    foundAcceptableSide = true;
                    break;
                }
            }
            if (!foundAcceptableSide)
                energyHandlers.put(blockPos, tempStorage);
        }
        return tempStorage.getCapability();
    }

    public TransmitterEnergyStorage getTransmitterEnergyHandler(BlockPos blockPos) {
        EnergyHandler energyHandler = getTransmitterHandler(blockPos);
        if (energyHandler instanceof TransmitterEnergyStorage transmitterEnergyStorage)
            return transmitterEnergyStorage;
        return null;
    }

    public EnergyHandler getTransmitterHandler(BlockPos blockPos) {
        var tempStorage = transmitterHandlers.get(blockPos);
        if (tempStorage == null) {
            BlockState blockState = level.getBlockState(blockPos);
            if (blockState.is(JDTRegistration.EnergyTransmitter.get())) {
                tempStorage = BlockCapabilityCache.create(
                        Capabilities.Energy.BLOCK,
                        (ServerLevel) level,
                        blockPos,
                        blockState.getValue(BlockStateProperties.FACING)
                );
                if (tempStorage.getCapability() != null) {
                    transmitterHandlers.put(blockPos, tempStorage);
                    return tempStorage.getCapability();
                }
            }
            energyHandlers.put(blockPos, tempStorage);
        }
        return tempStorage.getCapability();
    }

    public void providePower() {
        if (getEnergyStorage().getEnergyStored() <= 0) return;
        for (BlockPos blockPos : blocksToCharge) {
            EnergyHandler energyHandler = getHandler(blockPos);
            if (energyHandler == null) continue;
            int sentAmt = transmitPowerWithLoss(getEnergyStorage(), energyHandler, fePerTick(), blockPos);
            if (sentAmt > 0)
                doParticles(getBlockPos(), blockPos);
        }
        balanceEnergy();
    }

    public int calculateLoss(int amtToSend, BlockPos remotePosition) {
        double energyLoss = (Config.ENERGY_TRANSMITTER_T1_LOSS_PER_BLOCK.get() * Math.abs(getBlockPos().distManhattan(remotePosition))) / 100;
        return amtToSend - (int) (Math.floor(amtToSend * energyLoss));
    }

    public int transmitPowerWithLoss(EnergyHandler sender, EnergyHandler receiver, int amtToSend, BlockPos remotePosition) {
        int amtFit;
        try (Transaction simTx = Transaction.openRoot()) {
            amtFit = receiver.insert(amtToSend, simTx);
        }
        if (amtFit <= 0) return 0;
        int extractAmt;
        try (Transaction tx = Transaction.openRoot()) {
            extractAmt = sender.extract(amtFit, tx);
            tx.commit();
        }
        if (extractAmt <= 0) return 0;
        try (Transaction tx = Transaction.openRoot()) {
            int inserted = receiver.insert(calculateLoss(extractAmt, remotePosition), tx);
            tx.commit();
            return inserted;
        }
    }

    public int transmitPower(EnergyHandler sender, EnergyHandler receiver, int amtToSend) {
        int amtFit;
        try (Transaction simTx = Transaction.openRoot()) {
            amtFit = receiver.insert(amtToSend, simTx);
        }
        if (amtFit <= 0) return 0;
        int extractAmt;
        try (Transaction tx = Transaction.openRoot()) {
            extractAmt = sender.extract(amtFit, tx);
            tx.commit();
        }
        if (extractAmt <= 0) return 0;
        try (Transaction tx = Transaction.openRoot()) {
            int inserted = receiver.insert(extractAmt, tx);
            tx.commit();
            return inserted;
        }
    }

    public void getBlocksToCharge() {
        transmitters.clear();
        blocksToCharge.clear();
        transmitters.add(getBlockPos());
        AABB area = getAABB(getBlockPos());
        BlockPos.betweenClosedStream((int) area.minX, (int) area.minY, (int) area.minZ, (int) area.maxX - 1, (int) area.maxY - 1, (int) area.maxZ - 1)
                .map(BlockPos::immutable)
                .sorted(Comparator.comparingDouble(x -> x.distSqr(getBlockPos())))
                .forEach(blockPos -> {
                    if (blockPos.equals(getBlockPos())) return;
                    BlockState blockState = level.getBlockState(blockPos);
                    if (blockState.isAir() || level.getBlockEntity(blockPos) == null) return;

                    boolean foundAcceptableSide = false;
                    for (Direction direction : Direction.values()) {
                        var cap = level.getCapability(Capabilities.Energy.BLOCK, blockPos, direction);
                        if (cap != null) {
                            // Simulate a 1-unit insert to check if the target can receive energy.
                            try (Transaction tx = Transaction.openRoot()) {
                                if (cap.insert(1, tx) > 0) {
                                    foundAcceptableSide = true;
                                    break;
                                }
                            }
                        }
                    }
                    if (!foundAcceptableSide)
                        return;

                    ItemStack blockItemStack = blockState.getCloneItemStack(blockPos, level, false, getUsefulFakePlayer((ServerLevel) level));
                    if (!isStackValidFilter(blockItemStack)) return;

                    if (blockState.getBlock() instanceof EnergyTransmitter)
                        transmitters.add(blockPos);
                    else
                        blocksToCharge.add(blockPos);
                });
        energyHandlers.entrySet().removeIf(entry -> !blocksToCharge.contains(entry.getKey()));
        transmitterHandlers.entrySet().removeIf(entry -> !transmitters.contains(entry.getKey()));
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
        if (!showParticles)
            return false;
        return true;
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putBoolean("showParticles", showParticles);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        showParticles = input.getBooleanOr("showParticles", showParticles);
    }
}
