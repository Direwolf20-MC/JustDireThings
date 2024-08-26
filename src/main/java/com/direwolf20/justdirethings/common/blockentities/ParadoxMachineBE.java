package com.direwolf20.justdirethings.common.blockentities;

import com.direwolf20.justdirethings.common.blockentities.basebe.*;
import com.direwolf20.justdirethings.common.capabilities.JustDireFluidTank;
import com.direwolf20.justdirethings.common.capabilities.MachineEnergyStorage;
import com.direwolf20.justdirethings.common.containers.handlers.FilterBasicHandler;
import com.direwolf20.justdirethings.datagen.JustDireBlockTags;
import com.direwolf20.justdirethings.setup.Registration;
import com.direwolf20.justdirethings.util.MiscHelpers;
import com.direwolf20.justdirethings.util.NBTHelpers;
import com.direwolf20.justdirethings.util.UsefulFakePlayer;
import com.direwolf20.justdirethings.util.interfacehelpers.AreaAffectingData;
import com.direwolf20.justdirethings.util.interfacehelpers.FilterData;
import com.direwolf20.justdirethings.util.interfacehelpers.RedstoneControlData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.entity.PartEntity;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.util.*;
import java.util.stream.Collectors;

public class ParadoxMachineBE extends BaseMachineBE implements PoweredMachineBE, AreaAffectingBE, FilterableBE, RedstoneControlledBE, FluidMachineBE {
    public RedstoneControlData redstoneControlData = getDefaultRedstoneData();
    public FilterData filterData = new FilterData();
    public final FluidContainerData fluidContainerData;
    public AreaAffectingData areaAffectingData = new AreaAffectingData(getBlockState().getValue(BlockStateProperties.FACING));
    public final PoweredMachineContainerData poweredMachineData;
    public CompoundTag snapshotData = new CompoundTag();
    public int savedBlocks = -1;
    public int savedEntities = -1;
    public int numBlocks = -1;
    public int numEntities = -1;
    public boolean renderParadox = false;
    public int targetType = 0;

    public ParadoxMachineBE(BlockPos pPos, BlockState pBlockState) {
        super(Registration.ParadoxMachineBE.get(), pPos, pBlockState);
        poweredMachineData = new PoweredMachineContainerData(this);
        fluidContainerData = new FluidContainerData(this);
    }

    @Override
    public void tickClient() {
    }

    @Override
    public void tickServer() {
        super.tickServer();
        doParadox();
    }

    public void doParadox() {
        if (!(isActiveRedstone() && canRun())) return;
        if (!canParadox()) return;
        if (level == null) return;

        UsefulFakePlayer fakePlayer = getUsefulFakePlayer((ServerLevel) level);
        if (targetType == 0 || targetType == 1)
            restoreBlocks(fakePlayer);
        if (targetType == 0 || targetType == 2)
            restoreEntities(fakePlayer);
        postRun();
    }

    public boolean canPlace(FakePlayer fakePlayer, BlockPos blockPos) {
        if (!level.mayInteract(fakePlayer, blockPos))
            return false;
        if (!level.getBlockState(blockPos).canBeReplaced())
            return false;
        if (!canPlaceAt(level, blockPos, fakePlayer))
            return false;
        return true;
    }

    private void restoreBlocks(FakePlayer fakePlayer) {
        Map<BlockPos, BlockState> blocksToRestore = getBlocksFromNBT();
        int restoredCount = 0;

        for (Map.Entry<BlockPos, BlockState> entry : blocksToRestore.entrySet()) {
            BlockPos blockPos = entry.getKey();
            BlockState blockState = entry.getValue();
            if (!canPlace(fakePlayer, blockPos))
                continue;
            // Place the block in the world
            if (level.setBlock(blockPos, blockState, 3)) {
                restoredCount++;
            }
        }
        numBlocks = restoredCount;
    }

    private void restoreEntities(FakePlayer fakePlayer) {
        Map<Vec3, LivingEntity> entitiesToRestore = getEntitiesFromNBT();
        int restoredCount = 0;

        for (Map.Entry<Vec3, LivingEntity> entry : entitiesToRestore.entrySet()) {
            Vec3 entityPos = entry.getKey();
            LivingEntity entity = entry.getValue();

            // Place the entity in the world
            entity.moveTo(entityPos.x, entityPos.y, entityPos.z, entity.getYRot(), entity.getXRot());
            if (level.addFreshEntity(entity)) {
                restoredCount++;
            }
        }
        numEntities = restoredCount;
    }

    public boolean hasSnapshotData() {
        return !snapshotData.isEmpty();
    }

    @Override
    public int getMaxMB() {
        return 16000;
    }

    @Override
    public JustDireFluidTank getFluidTank() {
        return getData(Registration.PARADOX_FLUID_HANDLER);
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
    public RedstoneControlData getDefaultRedstoneData() {
        return new RedstoneControlData(MiscHelpers.RedstoneMode.PULSE);
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
    public int getMaxEnergy() {
        return 10000000;
    }

    @Override
    public int getStandardEnergyCost() {
        return 5000;
    }

    public int getBlockEnergyCost() {
        return 5000; //TODO Balance and Config
    }

    public int getEntityEnergyCost() {
        return 5000; //TODO Balance and Config
    }

    public int getEnergyCost(int blocks, int entities) {
        int blockCost = blocks * getBlockEnergyCost();
        int entityCost = entities * getEntityEnergyCost();
        return blockCost + entityCost;
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

    public void setAreaOnly(double x, double y, double z) {
        getAreaAffectingData().xRadius = Math.max(0, Math.min(x, maxRadius));
        getAreaAffectingData().yRadius = Math.max(0, Math.min(y, maxRadius));
        getAreaAffectingData().zRadius = Math.max(0, Math.min(z, maxRadius));
        getAreaAffectingData().area = null;
        if (getBlockEntity() instanceof BaseMachineBE baseMachineBE)
            baseMachineBE.markDirtyClient();
    }

    public boolean canParadox() {
        if (!hasSnapshotData())
            return false;
        return hasEnoughPower(getEnergyCost(getSavedBlocksCount(), getSavedEntitiesCount())) && hasEnoughFluid(getFluidCost(getSavedBlocksCount(), getSavedEntitiesCount()));
    }

    public boolean hasEnoughFluid(int fluidCost) {
        FluidStack extractedStack = getFluidTank().drain(fluidCost, IFluidHandler.FluidAction.SIMULATE);
        return extractedStack.getAmount() == fluidCost;
    }

    public void extractFluid(int fluidCost) {
        getFluidTank().drain(fluidCost, IFluidHandler.FluidAction.EXECUTE);
    }

    public int getFluidCost(int blocks, int entities) {
        int blockCost = blocks * getBlockFluidCost();
        int entityCost = entities * getEntityFluidCost();
        return blockCost + entityCost;
    }

    public int getBlockFluidCost() {
        return 1; //TODO Balance and Config
    }

    public int getEntityFluidCost() {
        return 1; //TODO Balance and Config
    }

    public void postRun() {
        if (numBlocks == -1 || numEntities == -1) return;
        extractEnergy(getEnergyCost(numBlocks, numEntities), false);
        extractFluid(getFluidCost(numBlocks, numEntities));
        numBlocks = -1;
        numEntities = -1;
    }

    public BlockPos getStartingPoint() {
        return getBlockPos().offset(getAreaAffectingData().xOffset, getAreaAffectingData().yOffset, getAreaAffectingData().zOffset);
    }

    public boolean isBlockPosValid(ServerLevel serverLevel, BlockPos blockPos) {
        BlockState blockState = serverLevel.getBlockState(blockPos);
        return blockState.is(JustDireBlockTags.PARADOX_ALLOW);
    }

    public Map<BlockPos, BlockState> getBlocksFromNBT() {
        BlockPos machinePos = getBlockPos();
        Map<BlockPos, BlockState> blockMap = new HashMap<>();
        if (!snapshotData.contains("blocks")) {
            return blockMap; // Return empty list if no block data is found
        }

        ListTag blockDataList = snapshotData.getList("blocks", 10); // 10 indicates a CompoundTag
        for (int i = 0; i < blockDataList.size(); i++) {
            CompoundTag blockTag = blockDataList.getCompound(i);
            BlockPos relativePos = NbtUtils.readBlockPos(blockTag, "pos").orElse(null);
            if (relativePos == null) continue;
            BlockPos worldPos = relativePos.offset(machinePos); // Convert relative to world position
            BlockState blockState = NbtUtils.readBlockState(BuiltInRegistries.BLOCK.asLookup(), blockTag.getCompound("state"));
            blockMap.put(worldPos, blockState); // Add the block's position and state to the list
        }

        return blockMap;
    }

    public Map<Vec3, LivingEntity> getEntitiesFromNBT() {
        BlockPos machinePos = getBlockPos();
        Map<Vec3, LivingEntity> entityMap = new HashMap<>();
        if (!snapshotData.contains("entities")) {
            return entityMap; // Return empty list if no entity data is found
        }

        ListTag entityDataList = snapshotData.getList("entities", 10); // 10 indicates a CompoundTag
        for (int i = 0; i < entityDataList.size(); i++) {
            CompoundTag entityTag = entityDataList.getCompound(i);
            Vec3 relativePos = NBTHelpers.nbtToVec3(entityTag.getCompound("relativePos"));
            Vec3 worldPos = relativePos.add(Vec3.atCenterOf(machinePos)); // Convert relative to world position

            // Restore the LivingEntity from the NBT data
            CompoundTag entityData = entityTag.getCompound("data");
            LivingEntity entity = restoreEntityFromNBT(entityData, worldPos);

            if (entity != null) {
                entityMap.put(worldPos, entity);
            }
        }

        return entityMap;
    }

    private LivingEntity restoreEntityFromNBT(CompoundTag entityData, Vec3 worldPos) {
        if (entityData == null || !entityData.contains("id")) {
            return null; // Return null if the entity data is invalid
        }

        // Retrieve the EntityType from the saved NBT data
        EntityType<?> entityType = EntityType.byString(entityData.getString("id")).orElse(null);
        if (entityType == null) {
            return null; // Return null if the entity type is invalid
        }

        if (entityData.contains("UUID")) {
            UUID entityUUID = entityData.getUUID("UUID");
            if (level.isClientSide) {
                /*Entity existingEntity = Minecraft.getInstance().level.getEntity(entityUUID);
                if (existingEntity != null) {
                    // Skip restoring the entity as it already exists on the client
                    return null;
                }*/
            } else {
                Entity existingEntity = ((ServerLevel) level).getEntity(entityUUID);
                if (existingEntity != null) {
                    // Skip restoring the entity as it already exists
                    return null;
                }
            }
        }

        // Create the entity instance
        Entity entity = entityType.create(level);
        if (!(entity instanceof LivingEntity)) {
            return null; // Return null if the entity is not a LivingEntity
        }

        // Load the entity data and set its position
        entity.load(entityData);
        entity.moveTo(worldPos.x, worldPos.y, worldPos.z, entity.getYRot(), entity.getXRot());

        return (LivingEntity) entity; // Return the restored LivingEntity
    }

    public void setRenderParadox(boolean render, int targetType) {
        this.renderParadox = render;
        this.targetType = targetType;
        markDirtyClient();
    }

    public void snapshotArea() {
        if (level == null) return;
        BlockPos machinePos = getBlockEntity().getBlockPos(); // Get the machine's position
        AABB area = getAABB(machinePos); // Get the affected area

        List<CompoundTag> blockDataList = new ArrayList<>();
        List<CompoundTag> entityDataList = new ArrayList<>();

        // Capture blocks in the area
        for (BlockPos pos : findBlocksToSave()) {
            BlockState blockState = level.getBlockState(pos);
            if (!blockState.isAir()) {
                CompoundTag blockTag = new CompoundTag();
                blockTag.put("pos", NbtUtils.writeBlockPos(pos.subtract(machinePos))); // Relative position
                blockTag.put("state", NbtUtils.writeBlockState(blockState)); // Block state
                blockDataList.add(blockTag);
            }
        }

        // Capture entities in the area
        List<LivingEntity> entities = findEntitiesToSave(area);
        for (LivingEntity entity : entities) {
            CompoundTag entityTag = new CompoundTag();
            CompoundTag entityData = new CompoundTag();
            entity.save(entityData);
            Vec3 entityRelativePos = entity.position().subtract(Vec3.atCenterOf(machinePos)); // Relative position as Vec3
            entityTag.put("relativePos", NBTHelpers.vec3ToNBT(entityRelativePos)); // Save Vec3 position
            entityTag.put("data", entityData);
            entityDataList.add(entityTag);
        }

        snapshotData = new CompoundTag();
        snapshotData.put("blocks", writeBlockDataList(blockDataList)); // Store blocks
        snapshotData.put("entities", writeEntityDataList(entityDataList)); // Store entities
        clearSnapshotCache();
        markDirtyClient();
    }

    public void clearSnapshotCache() {
        this.savedBlocks = -1;
        this.savedEntities = -1;
    }

    public int getSavedBlocksCount() {
        if (savedBlocks == -1)
            savedBlocks = getBlocksFromNBT().size();
        return savedBlocks;
    }

    public int getSavedEntitiesCount() {
        if (savedEntities == -1)
            savedEntities = getEntitiesFromNBT().size();
        return savedEntities;
    }

    public List<BlockPos> findBlocksToSave() {
        AABB area = getAABB(getBlockPos());
        return BlockPos.betweenClosedStream((int) area.minX, (int) area.minY, (int) area.minZ, (int) area.maxX - 1, (int) area.maxY - 1, (int) area.maxZ - 1)
                .filter(blockPos -> isBlockPosValid((ServerLevel) level, blockPos))
                .map(BlockPos::immutable)
                .collect(Collectors.toList());
    }

    public List<LivingEntity> findEntitiesToSave(AABB aabb) {
        return new ArrayList<>(level.getEntitiesOfClass(LivingEntity.class, aabb, this::isValidEntity));
    }

    public boolean isValidEntity(Entity entity) {
        if (entity.isMultipartEntity())
            return false;
        if (entity instanceof PartEntity<?>)
            return false;
        if (entity.getType().is(Tags.EntityTypes.TELEPORTING_NOT_SUPPORTED))
            return false;
        if (entity instanceof Player)
            return false;
        return isEntityValidFilter(entity, this.level);
    }

    // Helper methods to write lists to NBT
    private ListTag writeBlockDataList(List<CompoundTag> blockDataList) {
        ListTag listTag = new ListTag();
        listTag.addAll(blockDataList);
        return listTag;
    }

    private ListTag writeEntityDataList(List<CompoundTag> entityDataList) {
        ListTag listTag = new ListTag();
        listTag.addAll(entityDataList);
        return listTag;
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.put("snapshotData", snapshotData);
        tag.putBoolean("renderParadox", renderParadox);
        tag.putInt("targetType", targetType);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        if (tag.contains("snapshotData"))
            snapshotData = tag.getCompound("snapshotData");
        if (tag.contains("renderParadox"))
            renderParadox = tag.getBoolean("renderParadox");
        if (tag.contains("targetType"))
            targetType = tag.getInt("targetType");
    }
}
