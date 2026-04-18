package com.direwolf20.justdirethings.common.blockentities;

import com.direwolf20.justdirethings.client.particles.glitterparticle.GlitterParticleData;
import com.direwolf20.justdirethings.common.blockentities.basebe.*;
import com.direwolf20.justdirethings.common.capabilities.JustDireFluidTank;
import com.direwolf20.justdirethings.common.capabilities.MachineEnergyStorage;
import com.direwolf20.justdirethings.common.entities.ParadoxEntity;
import com.direwolf20.justdirethings.common.network.data.ParadoxSyncPayload;
import com.direwolf20.justdirethings.setup.Config;
import com.direwolf20.justdirethings.setup.JDTRegistration;
import com.direwolf20.justdirethings.util.MiscHelpers;
import com.direwolf20.justdirethings.util.ModTags;
import com.direwolf20.justdirethings.util.NBTHelpers;
import com.direwolf20.justdirethings.util.UsefulFakePlayer;
import com.direwolf20.justdirethings.util.interfacehelpers.AreaAffectingData;
import com.direwolf20.justdirethings.util.interfacehelpers.RedstoneControlData;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.entity.PartEntity;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

import java.util.*;
import java.util.stream.Collectors;

public class ParadoxMachineBE extends BaseMachineBE implements PoweredMachineBE, AreaAffectingBE, RedstoneControlledBE, FluidMachineBE {
    public RedstoneControlData redstoneControlData = getDefaultRedstoneData();
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
    public float paradoxEnergy = 0;
    public Map<BlockPos, BlockState> restoringBlocks = new HashMap<>();
    public List<Vec3> restoringEntites = new ArrayList<>();
    private final static Random random = new Random();

    public ParadoxMachineBE(BlockPos pPos, BlockState pBlockState) {
        super(JDTRegistration.ParadoxMachineBE.get(), pPos, pBlockState);
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

        double offsetX = random.nextBoolean() ? -0.5 + random.nextDouble() * 0.5 : 1.0 + random.nextDouble() * 0.5;
        double offsetY = random.nextBoolean() ? -0.5 + random.nextDouble() * 0.5 : 1.0 + random.nextDouble() * 0.5;
        double offsetZ = random.nextBoolean() ? -0.5 + random.nextDouble() * 0.5 : 1.0 + random.nextDouble() * 0.5;

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
        if (paradoxEnergy >= getMaxParadoxEnergy())
            spawnParadox();
    }

    public int getRunTime() {
        if (!isRunning) {
            return 300;
        }
        return (restoringBlocks.size() + restoringEntites.size()) * 10;
    }

    public void receiveRunTime(int runtime) {
        this.timeRunning = runtime;
    }

    public float getParadoxEnergyPerBlock() {
        return Config.PARADOX_ENERGY_PER_BLOCK.get().floatValue();
    }

    public float getParadoxEnergyPerEntity() {
        return Config.PARADOX_ENERGY_PER_ENTITY.get().floatValue();
    }

    public float getMaxParadoxEnergy() {
        return Config.PARADOX_ENERGY_MAX.get().floatValue();
    }

    public void addParadoxEnergy(float amt) {
        this.paradoxEnergy = Math.min(getMaxParadoxEnergy(), paradoxEnergy + amt);
        markDirtyClient();
    }

    public void resetParadoxEnergy() {
        this.paradoxEnergy = 0;
        markDirtyClient();
    }

    public void spawnParadox() {
        if (level == null) return;
        ParadoxEntity paradoxEntity = new ParadoxEntity(level, getStartingPoint());
        level.addFreshEntity(paradoxEntity);
        resetParadoxEnergy();
    }

    public boolean paradoxExists() {
        if (level == null) return true;
        List<ParadoxEntity> paradoxEntities = level.getEntitiesOfClass(ParadoxEntity.class, new AABB(getStartingPoint()));
        return !paradoxEntities.isEmpty();
    }

    public void startParadox() {
        if (!(isActiveRedstone() && canRun())) return;
        if (!canParadox()) return;
        if (paradoxExists()) return;
        if (!isRunning) {
            UsefulFakePlayer fakePlayer = getUsefulFakePlayer((ServerLevel) level);
            restoringBlocks = testRestoreBlocks(fakePlayer);
            restoringEntites = new ArrayList<>(getEntitiesFromNBT().keySet());
            if (restoringBlocks.isEmpty() && restoringEntites.isEmpty()) return;
            isRunning = true;
            fePerTick = getEnergyCostPerTick(getEnergyCost(restoringBlocks.size(), restoringEntites.size()));
            fluidPerTick = getFluidCostPerTick(getFluidCost(restoringBlocks.size(), restoringEntites.size()));
            level.playSound(null, getBlockPos(), SoundEvents.PORTAL_AMBIENT, SoundSource.BLOCKS, 0.5F, 0.25F);
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
                level.playSound(null, getBlockPos(), SoundEvents.PORTAL_AMBIENT, SoundSource.BLOCKS, 0.5F, 0.25F);
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
                addParadoxEnergy(getParadoxEnergyPerBlock() * restoringBlocks.size());
                restoreEntities(fakePlayer);
                addParadoxEnergy(getParadoxEnergyPerEntity() * restoringEntites.size());
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

    //The below two are for client side checking - skips the fake player checks
    public boolean canPlace(BlockPos blockPos) {
        if (!level.getBlockState(blockPos).canBeReplaced())
            return false;
        return true;
    }

    public Map<BlockPos, BlockState> testRestoreBlocks() {
        Map<BlockPos, BlockState> blocksToRestore = getBlocksFromNBT();
        Map<BlockPos, BlockState> returnMap = new HashMap<>();

        for (Map.Entry<BlockPos, BlockState> entry : blocksToRestore.entrySet()) {
            BlockPos blockPos = entry.getKey();
            BlockState blockState = entry.getValue();
            if (!canPlace(blockPos))
                continue;
            returnMap.put(blockPos, blockState);
        }
        return returnMap;
    }

    private void restoreBlocks(FakePlayer fakePlayer) {
        int restoredCount = 0;

        for (Map.Entry<BlockPos, BlockState> entry : restoringBlocks.entrySet()) {
            BlockPos blockPos = entry.getKey();
            BlockState blockState = entry.getValue();
            if (!canPlace(fakePlayer, blockPos))
                continue;
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

            entity.snapTo(entityPos.x, entityPos.y, entityPos.z, entity.getYRot(), entity.getXRot());
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
        return Config.PARADOX_TOTAL_FLUID_CAPACITY.get();
    }

    @Override
    public JustDireFluidTank getFluidTank() {
        return getData(JDTRegistration.PARADOX_FLUID_HANDLER);
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
        return getData(JDTRegistration.ENERGYSTORAGE_MACHINES);
    }

    @Override
    public int getMaxEnergy() {
        return Config.PARADOX_TOTAL_RF_CAPACITY.get();
    }

    @Override
    public int getStandardEnergyCost() {
        return getBlockEnergyCost();
    }

    public int getBlockEnergyCost() {
        return Config.PARADOX_RF_PER_BLOCK.get();
    }

    public int getEntityEnergyCost() {
        return Config.PARADOX_RF_PER_ENTITY.get();
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
        if (getFluidStack().isEmpty())
            return false;
        return true;
    }

    public boolean hasEnoughFluid(int fluidCost) {
        JustDireFluidTank tank = getFluidTank();
        FluidResource resource = tank.getResource(0);
        if (resource.isEmpty()) return fluidCost == 0;
        try (Transaction tx = Transaction.openRoot()) {
            int extracted = tank.extract(0, resource, fluidCost, tx);
            return extracted == fluidCost;
        }
    }

    public int extractFluid(int fluidCost) {
        if (fluidCost <= 0) return 0;
        JustDireFluidTank tank = getFluidTank();
        FluidResource resource = tank.getResource(0);
        if (resource.isEmpty()) return 0;
        try (Transaction tx = Transaction.openRoot()) {
            int extracted = tank.extract(0, resource, fluidCost, tx);
            tx.commit();
            return extracted;
        }
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
        return Config.PARADOX_FLUID_PER_BLOCK.get();
    }

    public int getEntityFluidCost() {
        return Config.PARADOX_FLUID_PER_ENTITY.get();
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
        return blockState.is(ModTags.Blocks.PARADOX_ALLOW);
    }

    public Map<BlockPos, BlockState> getBlocksFromNBT() {
        BlockPos machinePos = getBlockPos();
        Map<BlockPos, BlockState> blockMap = new HashMap<>();
        if (targetType == 2 || !snapshotData.contains("blocks")) {
            return blockMap;
        }

        ListTag blockDataList = snapshotData.getListOrEmpty("blocks");
        for (int i = 0; i < blockDataList.size(); i++) {
            CompoundTag blockTag = blockDataList.getCompoundOrEmpty(i);
            long packedPos = blockTag.getLongOr("pos", Long.MIN_VALUE);
            if (packedPos == Long.MIN_VALUE) continue;
            BlockPos relativePos = BlockPos.of(packedPos);
            BlockPos worldPos = relativePos.offset(machinePos);
            BlockState blockState = BlockState.CODEC.parse(net.minecraft.nbt.NbtOps.INSTANCE, blockTag.get("state"))
                    .result()
                    .orElse(net.minecraft.world.level.block.Blocks.AIR.defaultBlockState());
            blockMap.put(worldPos, blockState);
        }

        return blockMap;
    }

    public Map<Vec3, LivingEntity> getEntitiesFromNBT() {
        BlockPos machinePos = getBlockPos();
        Map<Vec3, LivingEntity> entityMap = new HashMap<>();
        if (targetType == 1 || !snapshotData.contains("entities")) {
            return entityMap;
        }

        ListTag entityDataList = snapshotData.getListOrEmpty("entities");
        for (int i = 0; i < entityDataList.size(); i++) {
            CompoundTag entityTag = entityDataList.getCompoundOrEmpty(i);
            Vec3 relativePos = NBTHelpers.nbtToVec3(entityTag.getCompoundOrEmpty("relativePos"));
            Vec3 worldPos = relativePos.add(Vec3.atCenterOf(machinePos));

            CompoundTag entityData = entityTag.getCompoundOrEmpty("data");
            LivingEntity entity = restoreEntityFromNBT(entityData, worldPos);

            if (entity != null) {
                entityMap.put(worldPos, entity);
            }
        }

        return entityMap;
    }

    private LivingEntity restoreEntityFromNBT(CompoundTag entityData, Vec3 worldPos) {
        if (entityData == null || !entityData.contains("id")) {
            return null;
        }

        EntityType<?> entityType = EntityType.byString(entityData.getString("id").orElse("")).orElse(null);
        if (entityType == null) {
            return null;
        }

        if (entityData.contains("UUID")) {
            UUID entityUUID = UUIDUtil.uuidFromIntArray(entityData.getIntArray("UUID").orElse(new int[]{0, 0, 0, 0}));
            if (!level.isClientSide()) {
                Entity existingEntity = ((ServerLevel) level).getEntity(entityUUID);
                if (existingEntity != null) {
                    return null;
                }
            }
        }

        Entity entity = entityType.create(level, EntitySpawnReason.SPAWNER);
        if (!(entity instanceof LivingEntity)) {
            return null;
        }

        CompoundTag santizedData = Config.PARADOX_RESTRICTED_MOBS.get() ? sanitizeEntityData(entityData) : sanitizeEntityDataDeny(entityData);
        net.minecraft.world.level.storage.ValueInput wrappedInput = net.minecraft.world.level.storage.TagValueInput.create(
                net.minecraft.util.ProblemReporter.DISCARDING, level.registryAccess(), santizedData);
        entity.load(wrappedInput);
        if (entity instanceof Mob mob && level instanceof ServerLevel serverLevel)
            EventHooks.finalizeMobSpawn(mob, serverLevel, serverLevel.getCurrentDifficultyAt(getBlockPos()), EntitySpawnReason.SPAWNER, null);

        entity.snapTo(worldPos.x, worldPos.y, worldPos.z, entity.getYRot(), entity.getXRot());

        return (LivingEntity) entity;
    }

    public CompoundTag sanitizeEntityData(CompoundTag entityData) {
        CompoundTag compoundTag = new CompoundTag();
        String[] fieldsToCopy = {"IsBaby", "UUID", "Health", "Motion", "Rotation", "Fire", "CustomName", "NoAI", "PersistenceRequired", "Silent",
                "Color", "Sheared", "Variant", "FromBucket", "Age", "VillagerData", "Xp",
                "LastRestock", "RestocksToday", "Offers", "EggLayTime"};

        for (String field : fieldsToCopy) {
            if (entityData.contains(field)) {
                compoundTag.put(field, entityData.get(field));
            }
        }
        return compoundTag;
    }

    public CompoundTag sanitizeEntityDataDeny(CompoundTag entityData) {
        CompoundTag compoundTag = new CompoundTag();
        String[] fieldsToRemove = {"ArmorItems", "HandItems", "Items", "SaddleItem", "Inventory"};

        for (String key : entityData.keySet()) {
            boolean shouldRemove = false;
            for (String field : fieldsToRemove) {
                if (key.equals(field)) {
                    shouldRemove = true;
                    break;
                }
            }

            if (!shouldRemove && entityData.get(key) != null) {
                compoundTag.put(key, entityData.get(key));
            }
        }
        return compoundTag;
    }

    public void setRenderParadox(boolean render, int targetType) {
        this.renderParadox = render;
        this.targetType = targetType;
        markDirtyClient();
    }

    public void snapshotArea() {
        if (level == null) return;
        BlockPos machinePos = getBlockEntity().getBlockPos();
        AABB area = getAABB(machinePos);

        List<CompoundTag> blockDataList = new ArrayList<>();
        List<CompoundTag> entityDataList = new ArrayList<>();

        // Capture blocks in the area
        for (BlockPos pos : findBlocksToSave()) {
            BlockState blockState = level.getBlockState(pos);
            if (!blockState.isAir()) {
                CompoundTag blockTag = new CompoundTag();
                blockTag.putLong("pos", pos.subtract(machinePos).asLong());
                BlockState.CODEC.encodeStart(net.minecraft.nbt.NbtOps.INSTANCE, blockState)
                        .result()
                        .ifPresent(tag -> blockTag.put("state", tag));
                blockDataList.add(blockTag);
            }
        }

        // Capture entities in the area
        List<LivingEntity> entities = findEntitiesToSave(area);
        for (LivingEntity entity : entities) {
            CompoundTag entityTag = new CompoundTag();
            CompoundTag entityData = saveEntityToNBT(entity);
            Vec3 entityRelativePos = entity.position().subtract(Vec3.atCenterOf(machinePos));
            entityTag.put("relativePos", NBTHelpers.vec3ToNBT(entityRelativePos));
            entityTag.put("data", entityData);
            entityDataList.add(entityTag);
        }

        snapshotData = new CompoundTag();
        snapshotData.put("blocks", writeBlockDataList(blockDataList));
        snapshotData.put("entities", writeEntityDataList(entityDataList));
        clearSnapshotCache();
        markDirtyClient();
    }

    private CompoundTag saveEntityToNBT(LivingEntity entity) {
        // Entity#save now writes via ValueOutput; round-trip via TagValueOutput to get a CompoundTag.
        net.minecraft.world.level.storage.TagValueOutput output = net.minecraft.world.level.storage.TagValueOutput.createWithContext(
                net.minecraft.util.ProblemReporter.DISCARDING, level.registryAccess());
        entity.save(output);
        return output.buildResult();
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
        if (entity.getType().builtInRegistryHolder().is(Tags.EntityTypes.TELEPORTING_NOT_SUPPORTED))
            return false;
        if (entity.getType().builtInRegistryHolder().is(ModTags.Entities.PARADOX_DENY))
            return false;
        if (entity instanceof Player)
            return false;
        return true;
    }

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
    public void onDataPacket(Connection net, ValueInput input) {
        boolean incomingRunning = input.getBooleanOr("isRunning", false);
        if (level.isClientSide() && isRunning && !incomingRunning) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level != null) {
                mc.getSoundManager().stop(SoundEvents.PORTAL_AMBIENT.location(), SoundSource.BLOCKS);
            }
        }
        super.onDataPacket(net, input);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.store("snapshotData", CompoundTag.CODEC, snapshotData);
        output.putBoolean("renderParadox", renderParadox);
        output.putInt("targetType", targetType);
        output.putBoolean("isRunning", isRunning);
        output.putInt("timeRunning", timeRunning);
        output.putInt("fePerTick", fePerTick);
        output.putInt("fluidPerTick", fluidPerTick);
        output.putFloat("paradoxEnergy", paradoxEnergy);

        // Save restoringBlocks map as packed long + codec state
        ValueOutput.ValueOutputList restoringBlocksList = output.childrenList("restoringBlocks");
        for (Map.Entry<BlockPos, BlockState> entry : restoringBlocks.entrySet()) {
            ValueOutput blockData = restoringBlocksList.addChild();
            blockData.putLong("pos", entry.getKey().asLong());
            blockData.store("state", BlockState.CODEC, entry.getValue());
        }

        // Save restoringEntities list
        ValueOutput.ValueOutputList restoringEntitiesList = output.childrenList("restoringEntities");
        for (Vec3 vec : restoringEntites) {
            ValueOutput entityData = restoringEntitiesList.addChild();
            entityData.putDouble("x", vec.x);
            entityData.putDouble("y", vec.y);
            entityData.putDouble("z", vec.z);
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        snapshotData = input.read("snapshotData", CompoundTag.CODEC).orElse(snapshotData);
        renderParadox = input.getBooleanOr("renderParadox", renderParadox);
        targetType = input.getIntOr("targetType", targetType);
        isRunning = input.getBooleanOr("isRunning", isRunning);
        timeRunning = input.getIntOr("timeRunning", timeRunning);
        paradoxEnergy = input.getFloatOr("paradoxEnergy", paradoxEnergy);

        // Load restoringBlocks map
        restoringBlocks.clear();
        input.childrenList("restoringBlocks").ifPresent(list -> {
            for (ValueInput child : list) {
                long packed = child.getLongOr("pos", Long.MIN_VALUE);
                if (packed == Long.MIN_VALUE) continue;
                BlockPos pos = BlockPos.of(packed);
                BlockState state = child.read("state", BlockState.CODEC).orElse(null);
                if (state != null)
                    restoringBlocks.put(pos, state);
            }
        });

        // Load restoringEntities list
        restoringEntites.clear();
        input.childrenList("restoringEntities").ifPresent(list -> {
            for (ValueInput child : list) {
                double x = child.getDoubleOr("x", 0);
                double y = child.getDoubleOr("y", 0);
                double z = child.getDoubleOr("z", 0);
                restoringEntites.add(new Vec3(x, y, z));
            }
        });
    }
}
