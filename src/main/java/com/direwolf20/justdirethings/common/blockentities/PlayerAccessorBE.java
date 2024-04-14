package com.direwolf20.justdirethings.common.blockentities;

import com.direwolf20.justdirethings.common.blockentities.basebe.BaseMachineBE;
import com.direwolf20.justdirethings.common.containers.handlers.PlayerHandler;
import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;

public class PlayerAccessorBE extends BaseMachineBE {
    public ServerPlayer serverPlayer;
    public HashMap<Direction, PlayerHandler> playerHandlers = new HashMap<>();
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

    public PlayerAccessorBE(BlockPos pPos, BlockState pBlockState) {
        this(Registration.PlayerAccessorBE.get(), pPos, pBlockState);
    }

    public void clearCache() {
        //System.out.println("Clearing Cache!");
        this.playerHandlers.clear();
        this.serverPlayer = null;
        if (level != null) {
            level.invalidateCapabilities(getBlockPos());
            updateServerPlayer(); //Since we cleared out the old player, check to see if they still exist, like a dimension change or something
        }
    }

    /**
     * Clear the caches, and re-locate the player object if necessary (Like player logged out, etc)
     */
    public void validatePlayer() {
        if (level == null || level.getServer() == null || this.placedByUUID == null) {
            clearCache();
            return;
        }
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
        }
    }

    /**
     * Return the player handler for the specified side of this block
     */
    public PlayerHandler getPlayerHandler(Direction side) {
        if (getServerPlayer() == null) //If our player does not exist
            return null;
        if (!playerHandlers.containsKey(side)) { //If we don't have a cache of the side, create one using the player we have cached
            playerHandlers.put(side, new PlayerHandler(serverPlayer, PlayerHandler.InventoryType.Inventory)); //Todo Sides
        } else {
            if (playerHandlers.get(side).player.isRemoved()) { //If the player handler's player has been removed (Logged off) re-validate
                validatePlayer();
                if (serverPlayer == null)
                    return null;
                playerHandlers.put(side, new PlayerHandler(serverPlayer, PlayerHandler.InventoryType.Inventory)); //Todo Sides
            }
        }
        return playerHandlers.get(side);
    }

}
