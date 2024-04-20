package com.direwolf20.justdirethings.common.blockentities;

import com.direwolf20.justdirethings.common.blockentities.basebe.BaseMachineBE;
import com.direwolf20.justdirethings.common.blockentities.basebe.RedstoneControlledBE;
import com.direwolf20.justdirethings.datagen.JustDireBlockTags;
import com.direwolf20.justdirethings.setup.Registration;
import com.direwolf20.justdirethings.util.MiscHelpers;
import com.direwolf20.justdirethings.util.NBTHelpers;
import com.direwolf20.justdirethings.util.interfacehelpers.RedstoneControlData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.PartEntity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class BlockSwapperT1BE extends BaseMachineBE implements RedstoneControlledBE {
    public RedstoneControlData redstoneControlData = getDefaultRedstoneData();
    List<BlockPos> positions = new ArrayList<>();
    public GlobalPos boundTo;
    public final ContainerData swapperData;
    public boolean doesPartnerExist;
    public List<BlockPos> thisValidationList = new ArrayList<>();
    public List<BlockPos> thatValidationList = new ArrayList<>();
    public boolean swapBlocks = true;
    public SWAP_ENTITY_TYPE swap_entity_type = SWAP_ENTITY_TYPE.NONE;
    public List<Entity> entities = new ArrayList<>();

    public enum SWAP_ENTITY_TYPE {
        NONE,
        HOSTILE,
        PASSIVE,
        ADULT,
        CHILD,
        PLAYER,
        LIVING,
        ITEM,
        ALL;

        public SWAP_ENTITY_TYPE next() {
            SWAP_ENTITY_TYPE[] values = values();
            int nextOrdinal = (this.ordinal() + 1) % values.length;
            return values[nextOrdinal];
        }
    }

    @Override
    public RedstoneControlData getDefaultRedstoneData() {
        return new RedstoneControlData(MiscHelpers.RedstoneMode.PULSE);
    }

    public BlockSwapperT1BE(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
        swapperData = new ContainerData() {
            @Override
            public int get(int index) {
                doesPartnerExist();
                return switch (index) {
                    case 0 -> doesPartnerExist ? 1 : 0;
                    default -> throw new IllegalArgumentException("Invalid index: " + index);
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> setPartnerExist(value);
                    default -> throw new IllegalArgumentException("Invalid index: " + index);
                }
            }

            @Override
            public int getCount() {
                return 1;
            }
        };
    }

    public BlockSwapperT1BE(BlockPos pPos, BlockState pBlockState) {
        this(Registration.BlockSwapperT1BE.get(), pPos, pBlockState);
    }

    public void setSwapperSettings(boolean swapBlocks, int swap_entity_type) {
        this.swapBlocks = swapBlocks;
        this.swap_entity_type = SWAP_ENTITY_TYPE.values()[swap_entity_type];
        markDirtyClient();
    }

    public boolean isPartnerNodeConnected(GlobalPos globalPos) {
        return globalPos != null && globalPos.equals(boundTo);
    }

    public void setBoundTo(GlobalPos globalPos) {
        this.boundTo = globalPos;
        markDirtyClient();
    }

    public ServerLevel getPartnerLevel() {
        if (boundTo == null) return null;
        if (level == null) return null;
        return level.getServer().getLevel(boundTo.dimension());
    }

    public BlockSwapperT1BE getPartnerBE() {
        if (boundTo == null) return null;
        ServerLevel partnerLevel = getPartnerLevel();
        if (partnerLevel == null) return null;
        BlockEntity partnerBE = partnerLevel.getBlockEntity(boundTo.pos());
        if (!(partnerBE instanceof BlockSwapperT1BE blockSwapperT1BE)) return null;
        return blockSwapperT1BE;
    }

    public GlobalPos getGlobalPos() {
        return GlobalPos.of(level.dimension(), getBlockPos());
    }

    public boolean isValidPartner(BlockEntity blockEntity) {
        return blockEntity.getClass() == this.getClass();
    }

    public boolean handleConnection(GlobalPos globalPos) {
        if (level == null) return false;
        if (globalPos.equals(getGlobalPos())) return false;
        ServerLevel partnerLevel = level.getServer().getLevel(globalPos.dimension());
        BlockEntity partnerBE = partnerLevel.getBlockEntity(globalPos.pos());
        if (!isValidPartner(partnerBE)) return false;
        if (!(partnerBE instanceof BlockSwapperT1BE blockSwapperT1BE)) return false;
        if (isPartnerNodeConnected(globalPos)) { //If these nodes are already connected, disconnect them
            removePartnerConnection();
            return false;
        } else {
            addPartnerConnection(globalPos, blockSwapperT1BE);
            return true;
        }
    }

    public GlobalPos getBoundTo() {
        return boundTo;
    }

    /**
     * @param connectingPos The Position in world you're connecting this TE to.
     * @param be            The block entity being connected to this one (And vice versa)
     *                      Connects This Pos -> Target Pos, and connects Target Pos -> This pos
     */

    public void addPartnerConnection(GlobalPos connectingPos, BlockSwapperT1BE be) {
        if (getBoundTo() != null) { //Connections are 1-1
            removePartnerConnection();
        }
        if (be.getBoundTo() != null) { //Connections are 1-1
            be.removePartnerConnection();
        }
        setBoundTo(connectingPos); // Add that to this one
        be.setBoundTo(getGlobalPos()); // Add this to that one
    }

    /**
     * Disconnects This Pos from Target Pos, and disconnects Target Pos from This pos
     */

    public void removePartnerConnection() {
        GlobalPos partnerPos = getBoundTo();
        if (partnerPos != null) {
            ServerLevel partnerLevel = level.getServer().getLevel(partnerPos.dimension());
            BlockEntity partnerBE = partnerLevel.getBlockEntity(partnerPos.pos());
            if (partnerBE instanceof BlockSwapperT1BE be) {
                be.setBoundTo(null); // Remove this from that one
            }
        }
        setBoundTo(null); // Remove that from this one
    }

    /** Validates the connections are still valid -- for use if a block is moved **/
    public void validateConnections() {
        //If we got here, it means the Block Swapper was moved, and so we assume it needs to update its partner
        if (boundTo == null || level == null || level.isClientSide) { //Don't do anything if this wasn't bound to something already
            return;
        }
        //System.out.println("Validating Connections!");
        BlockSwapperT1BE blockSwapperT1BE = getPartnerBE();
        if (blockSwapperT1BE != null) {
            addPartnerConnection(boundTo, blockSwapperT1BE); //If the partner still exists at the old spot, connect them, else remove
        } else {
            //removePartnerConnection(); //TODO What should we do here?
        }
    }

    public boolean setPartnerExist(int value) {
        return doesPartnerExist = (value == 1);
    }

    public void doesPartnerExist() {
        if (boundTo == null || level == null || level.isClientSide) { //Don't do anything if this wasn't bound to something already
            return;
        }
        BlockSwapperT1BE blockSwapperT1BE = getPartnerBE();
        if (blockSwapperT1BE != null)
            doesPartnerExist = true;
        else
            doesPartnerExist = false;
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
        doSwap();
    }

    public boolean canSwap() {
        return true;
    }

    public void postSwap(int numBlocks) {
        //No-Op in Tier 1
    }

    public void doSwap() {
        if (!(isActiveRedstone() && canRun())) return;
        if (!canSwap()) return;
        if (level == null) return;

        BlockSwapperT1BE remoteSwapper = getPartnerBE();
        if (remoteSwapper == null) return;
        ServerLevel partnerLevel = getPartnerLevel();
        if (partnerLevel == null) return;

        if (swapBlocks)
            positions = findSpotsToSwap();
        if (!swap_entity_type.equals(SWAP_ENTITY_TYPE.NONE))
            entities = findEntitiesToSwap(getAABB());
        if (positions.isEmpty() && entities.isEmpty())
            return;
        for (BlockPos blockPos : positions) {
            swapBlock(blockPos, remoteSwapper, partnerLevel);
        }
        for (BlockPos thisPos : thisValidationList) {
            validateBlock((ServerLevel) level, thisPos);
        }
        for (BlockPos thatPos : thatValidationList) {
            validateBlock(partnerLevel, thatPos);
        }
        for (Entity entity : entities) {
            swapEntity(entity, remoteSwapper, partnerLevel);
        }

        int totalSwapped = thisValidationList.size() + thatValidationList.size() + entities.size();
        postSwap(totalSwapped);
        if (totalSwapped > 0) {
            level.playSound(null, getBlockPos(), SoundEvents.SHULKER_TELEPORT, SoundSource.BLOCKS, 0.33F, 1.0F);
            partnerLevel.playSound(null, remoteSwapper.getBlockPos(), SoundEvents.SHULKER_TELEPORT, SoundSource.BLOCKS, 0.33F, 1.0F);
        }
        positions.clear();
        entities.clear();
        thisValidationList.clear();
        thatValidationList.clear();
    }

    public void swapEntity(Entity entity, BlockSwapperT1BE remoteSwapper, ServerLevel partnerLevel) {
        Vec3 remotePosition = remoteSwapper.getWorldPos(getRelativePos(entity.position()));
        entity.teleportTo(partnerLevel, remotePosition.x, remotePosition.y, remotePosition.z, new HashSet<>(), entity.getYRot(), entity.getXRot());
    }

    public AABB getAABB() {
        return new AABB(getBlockPos().relative(getBlockState().getValue(BlockStateProperties.FACING)));
    }

    public List<Entity> findEntitiesToSwap(AABB aabb) {
        List<Entity> returnList = new ArrayList<>(level.getEntitiesOfClass(Entity.class, aabb, this::isValidEntity));

        return returnList;
    }

    public boolean isSameLevel() {
        return level.equals(getPartnerBE().getLevel());
    }


    public boolean isValidEntity(Entity entity) {
        if (entity.isMultipartEntity())
            return false;
        if (entity instanceof PartEntity<?>)
            return false;
        if (!entity.canChangeDimensions() && !isSameLevel())
            return false;
        if (swap_entity_type.equals(SWAP_ENTITY_TYPE.HOSTILE) && !(entity instanceof Monster))
            return false;
        if (((swap_entity_type.equals(SWAP_ENTITY_TYPE.PASSIVE)) || (swap_entity_type.equals(SWAP_ENTITY_TYPE.ADULT)) || (swap_entity_type.equals(SWAP_ENTITY_TYPE.CHILD))) && !(entity instanceof Animal))
            return false;
        if (swap_entity_type.equals(SWAP_ENTITY_TYPE.ADULT) && (entity instanceof Animal animal) && (animal.isBaby()))
            return false;
        if (swap_entity_type.equals(SWAP_ENTITY_TYPE.CHILD) && (entity instanceof Animal animal) && !(animal.isBaby()))
            return false;
        if (swap_entity_type.equals(SWAP_ENTITY_TYPE.PLAYER) && !(entity instanceof Player))
            return false;
        if (swap_entity_type.equals(SWAP_ENTITY_TYPE.ITEM) && !(entity instanceof ItemEntity))
            return false;
        return true;
    }

    public void swapBlock(BlockPos blockPos, BlockSwapperT1BE remoteSwapper, ServerLevel partnerLevel) {
        BlockPos remoteSwapPos = remoteSwapper.getWorldPos(getRelativePos(blockPos));

        if (!isBlockPosValid(partnerLevel, remoteSwapPos)) return;

        BlockState thisBlock = level.getBlockState(blockPos);
        BlockState thatBlock = partnerLevel.getBlockState(remoteSwapPos);

        if (thisBlock.isAir() && thatBlock.isAir()) return;

        BlockEntity thisBE = level.getBlockEntity(blockPos);
        BlockEntity thatBE = partnerLevel.getBlockEntity(remoteSwapPos);

        CompoundTag thisNBT = new CompoundTag();
        CompoundTag thatNBT = new CompoundTag();

        if (thisBE != null) {
            thisNBT = thisBE.saveWithFullMetadata();
        }

        if (thatBE != null) {
            thatNBT = thatBE.saveWithFullMetadata();
        }

        //Remove existing blocks and BE's
        level.removeBlockEntity(blockPos); //Calling this prevents chests from dropping their contents
        partnerLevel.removeBlockEntity(remoteSwapPos); //Calling this prevents chests from dropping their contents

        //BlockState adjustedthisBlock = Block.updateFromNeighbourShapes(thisBlock, partnerLevel, remoteSwapPos); //Ensure double chests are placed as single chests if only 1 chest available in copy/paste, for example, or fixes fences
        boolean placedThat = partnerLevel.setBlock(remoteSwapPos, thisBlock, 51); //16 + 32 + 3 -- Validate blocks after this methods done
        if (placedThat) {
            if (!thisNBT.isEmpty()) {
                BlockEntity newBE = partnerLevel.getBlockEntity(remoteSwapPos);
                if (newBE != null) {
                    try {
                        newBE.load(thisNBT);
                    } catch (Exception e) {
                        System.out.println("Failed to restore tile data for block at: " + remoteSwapPos + " with NBT: " + thisNBT + ". Consider adding it to the blacklist");
                    }
                }
            }
        }
        //if (!thisBlock.isAir())
        thatValidationList.add(remoteSwapPos);

        //BlockState adjustedthatBlock = Block.updateFromNeighbourShapes(thatBlock, level, blockPos); //Ensure double chests are placed as single chests if only 1 chest available in copy/paste, for example, or fixes fences
        boolean placedThis = level.setBlock(blockPos, thatBlock, 51); //16 + 32 + 3 -- Validate blocks after this methods done
        if (placedThis) {
            if (!thatNBT.isEmpty()) {
                BlockEntity newBE = level.getBlockEntity(blockPos);
                if (newBE != null) {
                    try {
                        newBE.load(thatNBT);
                    } catch (Exception e) {
                        System.out.println("Failed to restore tile data for block at: " + blockPos + " with NBT: " + thatNBT + ". Consider adding it to the blacklist");
                    }
                }
            }
        }
        //if (!thatBlock.isAir())
        thisValidationList.add(blockPos);

        teleportParticles((ServerLevel) level, blockPos);
        teleportParticles(partnerLevel, remoteSwapPos);
    }

    public void validateBlock(ServerLevel level, BlockPos blockPos) {
        BlockState blockState = level.getBlockState(blockPos);
        if (!blockState.canSurvive(level, blockPos)) {
            level.destroyBlock(blockPos, true);
            return;
        }
        BlockState adjustedBlockState = Block.updateFromNeighbourShapes(blockState, level, blockPos); //Ensure double chests are placed as single chests if only 1 chest available in copy/paste, for example, or fixes fences
        if (!adjustedBlockState.equals(blockState)) //SetBlockAndUpdate does the markAndNotify (below)
            level.setBlockAndUpdate(blockPos, adjustedBlockState);
        else { //Do this to ensure buttons/torches pop off if we swapped air into a spot
            LevelChunk levelchunk = level.getChunkAt(blockPos);
            level.markAndNotifyBlock(blockPos, levelchunk, blockState, blockState, 3, 512);
        }
    }

    public static void teleportParticles(ServerLevel level, BlockPos pos) {
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            double d0 = pos.getX() + random.nextDouble();
            double d1 = pos.getY() - 0.5d + random.nextDouble();
            double d2 = pos.getZ() + random.nextDouble();
            level.sendParticles(ParticleTypes.PORTAL, d0, d1, d2, 1, 0.0, 0.0, 0.0, 0);
        }
    }

    public boolean isBlockPosValid(ServerLevel serverLevel, BlockPos blockPos) {
        if (serverLevel.getBlockState(blockPos).is(JustDireBlockTags.NO_MOVE) || serverLevel.getBlockState(blockPos).is(JustDireBlockTags.SWAPPERDENY))
            return false;
        GlobalPos targetGlobalPos = GlobalPos.of(serverLevel.dimension(), blockPos);
        if (targetGlobalPos.equals(getGlobalPos()) || targetGlobalPos.equals(boundTo))
            return false;
        return true;
    }

    public BlockPos getStartingPoint() {
        return getBlockPos().relative(getBlockState().getValue(BlockStateProperties.FACING)); //Tier 2 would use Offset
    }

    public List<BlockPos> findSpotsToSwap() {
        List<BlockPos> returnList = new ArrayList<>();
        BlockPos blockPos = getBlockPos().relative(getBlockState().getValue(BlockStateProperties.FACING));
        if (isBlockPosValid((ServerLevel) level, blockPos))
            returnList.add(blockPos);
        return returnList;
    }

    /** Helpers to translate between relative/world pos */
    public BlockPos getWorldPos(BlockPos relativePos) {
        return getStartingPoint().offset(relativePos);
    }

    public BlockPos getRelativePos(BlockPos worldPos) {
        return worldPos.subtract(getStartingPoint());
    }

    public Vec3 getWorldPos(Vec3 relativePos) {
        return new Vec3(getStartingPoint().getX() + relativePos.x, getStartingPoint().getY() + relativePos.y, getStartingPoint().getZ() + relativePos.z);
    }

    public Vec3 getRelativePos(Vec3 worldPos) {
        return new Vec3(worldPos.x - getStartingPoint().getX(), worldPos.y - getStartingPoint().getY(), worldPos.z - getStartingPoint().getZ());
    }

    @Override
    public boolean isDefaultSettings() {
        if (!super.isDefaultSettings())
            return false;
        if (boundTo != null)
            return false;
        if (!swapBlocks)
            return false;
        if (!swap_entity_type.equals(SWAP_ENTITY_TYPE.NONE))
            return false;
        return true;
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (boundTo != null)
            tag.put("boundTo", NBTHelpers.globalPosToNBT(boundTo));
        tag.putBoolean("swapBlocks", swapBlocks);
        tag.putInt("swap_entity_type", swap_entity_type.ordinal());
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("boundTo")) {
            GlobalPos newBoundTo = NBTHelpers.nbtToGlobalPos(tag.getCompound("boundTo"));
            boolean same = newBoundTo.equals(boundTo);
            this.boundTo = newBoundTo;
            if (!same)
                validateConnections();
        } else {
            this.boundTo = null;
        }
        if (tag.contains("swapBlocks"))
            swapBlocks = tag.getBoolean("swapBlocks");
        swap_entity_type = SWAP_ENTITY_TYPE.values()[tag.getInt("swap_entity_type")];
    }
}
