package com.direwolf20.justdirethings.common.blockentities;

import com.direwolf20.justdirethings.common.blockentities.basebe.BaseMachineBE;
import com.direwolf20.justdirethings.common.containers.handlers.PlayerHandler;
import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;

public class PlayerAccessorBE extends BaseMachineBE {
    public ServerPlayer serverPlayer;
    public HashMap<Direction, PlayerHandler> playerHandlers = new HashMap<>();
    public HashMap<Direction, Integer> sidedInventoryTypes = new HashMap<>();
    boolean checkedPlayer = false;

    public PlayerAccessorBE(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
        tickSpeed = 100; //This controls how often validatePlayer() runs.  Don't worry if the player leaves before this checks, because the itemhandler will prevent insertion if they do!
    }

    @Override
    public void tickServer() {
        super.tickServer();
        if (canRun()) { //Do this every 100 ticks
            checkedPlayer = false;
            validatePlayer();
        }
    }

    public void updateSidedInventory(Direction direction, int type) {
        sidedInventoryTypes.put(direction, type);
        clearCache();
        markDirtyClient();
    }

    public PlayerAccessorBE(BlockPos pPos, BlockState pBlockState) {
        this(Registration.PlayerAccessorBE.get(), pPos, pBlockState);
    }

    public void clearCache() {
        //System.out.println("Clearing Cache!");
        this.playerHandlers.clear();
        this.serverPlayer = null;
        if (level != null) {
            updateServerPlayer(); //Since we cleared out the old player, check to see if they still exist, like a dimension change or something
        }
        markDirtyClient();
    }

    /**
     * Clear the caches, and re-locate the player object if necessary (Like player logged out, etc)
     */
    public void validatePlayer() {
        if (getServerPlayer() != null && serverPlayer.isRemoved()) {
            clearCache();
        }
    }

    /**
     * If we have a cached player, return it, otherwise look for the player object on the server
     */
    public ServerPlayer getServerPlayer() {
        if (serverPlayer == null && !checkedPlayer) //Only allow this to recheck every 100 ticks
            updateServerPlayer();
        return serverPlayer;
    }

    /**
     * Find the player on the server, if the one we have cached is either null or removed.
     */
    public void updateServerPlayer() {
        //System.out.println("Updating Player");
        checkedPlayer = true;
        if (this.placedByUUID == null) {
            serverPlayer = null;
            return;
        }
        if (serverPlayer == null || serverPlayer.isRemoved()) {
            if (level == null || level.getServer() == null)
                serverPlayer = null;
            else
                serverPlayer = level.getServer().getPlayerList().getPlayer(this.placedByUUID);
            markDirtyClient();
        }
    }

    /**
     * Return the player handler for the specified side of this block
     */
    public PlayerHandler getPlayerHandler(Direction side) {
        if (getServerPlayer() == null) //If our player does not exist
            return null;
        if (!playerHandlers.containsKey(side)) { //If we don't have a cache of the side, create one using the player we have cached
            playerHandlers.put(side, new PlayerHandler(serverPlayer, PlayerHandler.InventoryType.values()[sidedInventoryTypes.getOrDefault(side, 0)]));
        } else {
            if (playerHandlers.get(side).player.isRemoved()) { //If the player handler's player has been removed (Logged off) re-validate
                validatePlayer();
                if (serverPlayer == null)
                    return null;
                playerHandlers.put(side, new PlayerHandler(serverPlayer, PlayerHandler.InventoryType.values()[sidedInventoryTypes.getOrDefault(side, 0)]));
            }
        }
        return playerHandlers.get(side);
    }

    @Override
    public boolean isDefaultSettings() {
        return true;
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        for (Direction direction : Direction.values()) {
            tag.putInt("sidedInventory_" + direction.ordinal(), sidedInventoryTypes.getOrDefault(direction, 0));
        }
    }

    @Override
    public void load(CompoundTag tag) {
        for (Direction direction : Direction.values()) {
            sidedInventoryTypes.put(direction, tag.getInt("sidedInventory_" + direction.ordinal()));
        }
        super.load(tag);
    }

    @Override
    public void markDirtyClient() {
        //System.out.println("Marking Dirty Client!");
        if (level != null) {
            level.invalidateCapabilities(getBlockPos());
        }
        super.markDirtyClient();
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        super.onDataPacket(net, pkt);
    }
}
