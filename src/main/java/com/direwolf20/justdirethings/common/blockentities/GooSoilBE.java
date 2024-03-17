package com.direwolf20.justdirethings.common.blockentities;

import com.direwolf20.justdirethings.setup.Registration;
import com.direwolf20.justdirethings.util.NBTHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;

public class GooSoilBE extends BlockEntity {
    private NBTHelpers.BoundInventory boundInventory;
    protected BlockCapabilityCache<IItemHandler, Direction> attachedInventory;

    public GooSoilBE(BlockPos pos, BlockState state) {
        super(Registration.GooSoilBE.get(), pos, state);
    }

    public void bindInventory(NBTHelpers.BoundInventory boundInventory) {
        this.boundInventory = boundInventory;
        this.setChanged();
    }

    public IItemHandler getAttachedInventory(ServerLevel serverLevel) {
        if (boundInventory == null) return null;
        if (attachedInventory == null) {
            ServerLevel boundLevel = serverLevel.getServer().getLevel(boundInventory.globalPos().dimension());
            assert boundLevel != null;
            attachedInventory = BlockCapabilityCache.create(
                    Capabilities.ItemHandler.BLOCK, // capability to cache
                    boundLevel, // level
                    boundInventory.globalPos().pos(), // target position
                    boundInventory.direction() // context (The side of the block we're trying to pull/push from?)
            );
        }
        return attachedInventory.getCapability();
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (boundInventory != null) {
            tag.put("boundinventory", NBTHelpers.BoundInventory.toNBT(boundInventory));
        }
    }

    @Override
    public void load(CompoundTag tag) {
        if (tag.contains("boundinventory"))
            boundInventory = NBTHelpers.BoundInventory.fromNBT(tag.getCompound("boundinventory"));
        super.load(tag);
    }
}
