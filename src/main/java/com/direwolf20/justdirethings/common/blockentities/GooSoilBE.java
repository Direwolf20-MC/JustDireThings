package com.direwolf20.justdirethings.common.blockentities;

import com.direwolf20.justdirethings.setup.JDTRegistration;
import com.direwolf20.justdirethings.util.NBTHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;

public class GooSoilBE extends BlockEntity {
    private NBTHelpers.BoundInventory boundInventory;
    protected BlockCapabilityCache<ResourceHandler<ItemResource>, Direction> attachedInventory;

    public GooSoilBE(BlockPos pos, BlockState state) {
        super(JDTRegistration.GooSoilBE.get(), pos, state);
    }

    public void bindInventory(NBTHelpers.BoundInventory boundInventory) {
        this.boundInventory = boundInventory;
        this.setChanged();
    }

    public ResourceHandler<ItemResource> getAttachedInventory(ServerLevel serverLevel) {
        if (boundInventory == null) return null;
        if (attachedInventory == null) {
            ServerLevel boundLevel = serverLevel.getServer().getLevel(boundInventory.globalPos().dimension());
            if (boundLevel == null) return null;
            attachedInventory = BlockCapabilityCache.create(
                    Capabilities.Item.BLOCK,
                    boundLevel,
                    boundInventory.globalPos().pos(),
                    boundInventory.direction()
            );
        }
        return attachedInventory.getCapability();
    }

    public void invalidateHandler() {
        attachedInventory = null;
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        if (boundInventory != null) {
            output.store("boundinventory", CompoundTag.CODEC, NBTHelpers.BoundInventory.toNBT(boundInventory));
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        input.read("boundinventory", CompoundTag.CODEC).ifPresent(tag -> {
            boundInventory = NBTHelpers.BoundInventory.fromNBT(tag);
        });
        super.loadAdditional(input);
    }
}
