package com.direwolf20.justdirethings.common.blockentities;

import com.direwolf20.justdirethings.client.particles.glitterparticle.GlitterParticleData;
import com.direwolf20.justdirethings.common.blockentities.basebe.*;
import com.direwolf20.justdirethings.common.capabilities.JustDireFluidTank;
import com.direwolf20.justdirethings.common.capabilities.MachineEnergyStorage;
import com.direwolf20.justdirethings.common.containers.handlers.FilterBasicHandler;
import com.direwolf20.justdirethings.common.network.data.ParadoxSyncPayload;
import com.direwolf20.justdirethings.datagen.JustDireBlockTags;
import com.direwolf20.justdirethings.setup.Registration;
import com.direwolf20.justdirethings.util.MiscHelpers;
import com.direwolf20.justdirethings.util.NBTHelpers;
import com.direwolf20.justdirethings.util.UsefulFakePlayer;
import com.direwolf20.justdirethings.util.interfacehelpers.AreaAffectingData;
import com.direwolf20.justdirethings.util.interfacehelpers.FilterData;
import com.direwolf20.justdirethings.util.interfacehelpers.RedstoneControlData;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
import net.neoforged.neoforge.network.PacketDistributor;

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
    public boolean isRunning = false;
    public int timeRunning = 0;
    public int fePerTick = 0;
    public int fluidPerTick = 0;
    public Map<BlockPos, BlockState> restoringBlocks = new HashMap<>();
    public List<Vec3> restoringEntites = new ArrayList<>();
    private final static Random random = new Random();

    public ParadoxMachineBE(BlockPos pPos, BlockState pBlockState) {
        super(Registration.ParadoxMachineBE.get(), pPos, pBlockState);
        poweredMachineData = new PoweredMachineContainerData(this);
        fluidContainerData = new FluidContainerData(this);
    }

    @Override
    public void tickClient() {
        if (isRunning) {
            if (level == null) return;
            timeRunning++;
            for (Map.Entry<BlockPos, BlockState> entry : restoringBlocks.entrySet()) {
                drawRestoringParticles(entry.getKey().getCenter());
            }
            for (Vec3 vec3 : restoringEntites) {
                drawRestoringParticles(vec3);
            }
        }
    }

    public void drawRestoringParticles(Vec3 vec3) {
        double d0 = vec3.x;
        double d1 = vec3.y;
        double d2 = vec3.z;
        float r, g, b;
        r = 0.4f;
        g = 1.00f;
        b = 0.39f;

        // Helper method to generate random positions outside the block (either -0.5 to 0 or 1 to 1.5)
        double offsetX = random.nextBoolean() ? -0.5 + random.nextDouble() * 0.5 : 1.0 + random.nextDouble() * 0.5;
        double offsetY = random.nextBoolean() ? -0.5 + random.nextDouble() * 0.5 : 1.0 + random.nextDouble() * 0.5;
        double offsetZ = random.nextBoolean() ? -0.5 + random.nextDouble() * 0.5 : 1.0 + random.nextDouble() * 0.5;

        // Calculate the start position with the random offsets
        double startX = d0 - 0.5 + offsetX;
        double startY = d1 - 0.5 + offsetY;
        double startZ = d2 - 0.5 + offsetZ;

        float randomPartSize = 0.05f + (0.025f - 0.05f) * random.nextFloat();
        GlitterParticleData data = GlitterParticleData.playerparticle("glitter", d0, d1, d2, randomPartSize, r, g, b, 1f, 120, false);
        level.addParticle(data, startX, startY, startZ, 0.00025, 0.00025f, 0.00025);
    }

    @Override
    public void tickServer() {
        super.tickServer();
        doParadox();
    }

    public int getRunTime() {
        if (!isRunning) {
            System.out.println("Not Running");
            return 300;
        }
        return (restoringBlocks.size() + restoringEntites.size()) * 10;
    }

    public void receiveRunTime(int runtime) {
        this.timeRunning = runtime;
    }

    public void startParadox() {
        if (!(isActiveRedstone() && canRun())) return;
        if (!canParadox()) return;
        if (!isRunning) {
            UsefulFakePlayer fakePlayer = getUsefulFakePlayer((ServerLevel) level);
            restoringBlocks = testRestoreBlocks(fakePlayer);
            restoringEntites = new ArrayList<>(getEntitiesFromNBT().keySet());
            if (restoringBlocks.isEmpty() && restoringEntites.isEmpty()) return;
            isRunning = true;
            fePerTick = getEnergyCostPerTick(getEnergyCost(restoringBlocks.size(), restoringEntites.size()));
            fluidPerTick = getFluidCostPerTick(getFluidCost(restoringBlocks.size(), restoringEntites.size()));
            markDirtyClient();
        }
    }

    public void doParadox() {
        if (level == null) return;
        startParadox();

        if (!isRunning) {
            return;
        } else {
            if (timeRunning % 20 == 0 && !(timeRunning >= getRunTime())) {
                PacketDistributor.sendToPlayersTrackingChunk((ServerLevel) level, level.getChunk(getBlockPos()).getPos(), new ParadoxSyncPayload(getBlockPos(), timeRunning));
            }
            if (timeRunning % 100 == 0 && !(timeRunning >= getRunTime())) {
                level.playSound(null, getBlockPos(), SoundEvents.PORTAL_AMBIENT, SoundSource.BLOCKS, 0.25F, 0.25F);
            }
            if (extractFluid(fluidPerTick) == fluidPerTick && extractEnergy(fePerTick, false) == fePerTick) {
                timeRunning++;
            } else {
                stopRunning(false);
                return;
            }
        }
        if (timeRunning >= getRunTime()) {
            stopRunning(true);
        }
    }

    public void stopRunning(boolean success) {
        if (success) {
            int finalFluidCost = getFluidCost(restoringBlocks.size(), restoringEntites.size()) - (fluidPerTick * getRunTime());
            int finalEnergyCost = getEnergyCost(restoringBlocks.size(), restoringEntites.size()) - (fePerTick * getRunTime());
            if (extractFluid(finalFluidCost) == finalFluidCost && extractEnergy(finalEnergyCost, false) == finalEnergyCost) {
                UsefulFakePlayer fakePlayer = getUsefulFakePlayer((ServerLevel) level);
                restoreBlocks(fakePlayer);
                restoreEntities(fakePlayer);
                postRun();
                level.playSound(null, getBlockPos(), SoundEvents.EVOKER_PREPARE_SUMMON, SoundSource.BLOCKS, 0.5F, 0.25F);
            } else {
                level.playSound(null, getBlockPos(), SoundEvents.CONDUIT_DEACTIVATE, SoundSource.BLOCKS, 0.5F, 0.25F);
            }
        } else {
            level.playSound(null, getBlockPos(), SoundEvents.CONDUIT_DEACTIVATE, SoundSource.BLOCKS, 0.5F, 0.25F);
        }
        timeRunning = 0;
        isRunning = false;
        restoringBlocks.clear();
        restoringEntites.clear();
        fePerTick = 0;
        fluidPerTick = 0;
        markDirtyClient();
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

    private Map<BlockPos, BlockState> testRestoreBlocks(FakePlayer fakePlayer) {
        Map<BlockPos, BlockState> blocksToRestore = getBlocksFromNBT();
        Map<BlockPos, BlockState> returnMap = new HashMap<>();

        for (Map.Entry<BlockPos, BlockState> entry : blocksToRestore.entrySet()) {
            BlockPos blockPos = entry.getKey();
            BlockState blockState = entry.getValue();
            if (!canPlace(fakePlayer, blockPos))
                continue;
            returnMap.put(blockPos, blockState);
        }
        return returnMap;
    }

    private void restoreBlocks(FakePlayer fakePlayer) {
        //Map<BlockPos, BlockState> blocksToRestore = getBlocksFromNBT();
        int restoredCount = 0;

        for (Map.Entry<BlockPos, BlockState> entry : restoringBlocks.entrySet()) {
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
            if (!restoringEntites.contains(entityPos))
                continue;
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
        return 250000;
    }

    public int getBlockEnergyCost() {
        return 250000; //TODO Balance and Config
    }

    public int getEntityEnergyCost() {
        return 250000; //TODO Balance and Config
    }

    public int getEnergyCostPerTick(int cost) {
        return (int) Math.floor((double) cost / getRunTime());
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
        return true;
    }

    public boolean hasEnoughFluid(int fluidCost) {
        FluidStack extractedStack = getFluidTank().drain(fluidCost, IFluidHandler.FluidAction.SIMULATE);
        return extractedStack.getAmount() == fluidCost;
    }

    public int extractFluid(int fluidCost) {
        return getFluidTank().drain(fluidCost, IFluidHandler.FluidAction.EXECUTE).getAmount();
    }

    public int getFluidCostPerTick(int totalFluidCost) {
        return (int) Math.floor((double) totalFluidCost / getRunTime());
    }

    public int getFluidCost(int blocks, int entities) {
        int blockCost = blocks * getBlockFluidCost();
        int entityCost = entities * getEntityFluidCost();
        return blockCost + entityCost;
    }

    public int getBlockFluidCost() {
        return 50; //TODO Balance and Config
    }

    public int getEntityFluidCost() {
        return 50; //TODO Balance and Config
    }

    public void postRun() {
        if (numBlocks == -1 || numEntities == -1) return;
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
        if (targetType == 2 || !snapshotData.contains("blocks")) {
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
        if (targetType == 1 || !snapshotData.contains("entities")) {
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
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookupProvider) {
        if (level.isClientSide && isRunning && !pkt.getTag().getBoolean("isRunning")) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level != null) {
                // Stop the specific sound using SoundManager
                mc.getSoundManager().stop(SoundEvents.PORTAL_AMBIENT.getLocation(), SoundSource.BLOCKS);
            }
        }
        super.onDataPacket(net, pkt, lookupProvider);

    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.put("snapshotData", snapshotData);
        tag.putBoolean("renderParadox", renderParadox);
        tag.putInt("targetType", targetType);
        tag.putBoolean("isRunning", isRunning);
        tag.putInt("timeRunning", timeRunning);
        tag.putInt("fePerTick", fePerTick);
        tag.putInt("fluidPerTick", fluidPerTick);

        // Save restoringBlocks map
        ListTag restoringBlocksList = new ListTag();
        for (Map.Entry<BlockPos, BlockState> entry : restoringBlocks.entrySet()) {
            CompoundTag blockData = new CompoundTag();
            blockData.put("pos", NbtUtils.writeBlockPos(entry.getKey())); // Write BlockPos
            blockData.put("state", NbtUtils.writeBlockState(entry.getValue())); // Write BlockState
            restoringBlocksList.add(blockData);
        }
        tag.put("restoringBlocks", restoringBlocksList);

        // Save restoringEntities list
        ListTag restoringEntitiesList = new ListTag();
        for (Vec3 vec : restoringEntites) {
            CompoundTag entityData = NBTHelpers.vec3ToNBT(vec);
            restoringEntitiesList.add(entityData);
        }
        tag.put("restoringEntities", restoringEntitiesList);
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
        if (tag.contains("isRunning"))
            isRunning = tag.getBoolean("isRunning");
        if (tag.contains("timeRunning"))
            timeRunning = tag.getInt("timeRunning");

        // Load restoringBlocks map
        restoringBlocks.clear();
        if (tag.contains("restoringBlocks")) {
            ListTag restoringBlocksList = tag.getList("restoringBlocks", 10); // 10 indicates a CompoundTag
            for (int i = 0; i < restoringBlocksList.size(); i++) {
                CompoundTag blockData = restoringBlocksList.getCompound(i);
                BlockPos pos = NbtUtils.readBlockPos(blockData, "pos").orElse(null);
                if (pos == null) continue;
                BlockState state = NbtUtils.readBlockState(BuiltInRegistries.BLOCK.asLookup(), blockData.getCompound("state"));
                restoringBlocks.put(pos, state);
            }
        }

        // Load restoringEntities list
        restoringEntites.clear();
        if (tag.contains("restoringEntities")) {
            ListTag restoringEntitiesList = tag.getList("restoringEntities", 10); // 10 indicates a CompoundTag
            for (int i = 0; i < restoringEntitiesList.size(); i++) {
                CompoundTag entityData = restoringEntitiesList.getCompound(i);
                Vec3 vec = NBTHelpers.nbtToVec3(entityData);
                restoringEntites.add(vec);
            }
        }
    }
}
