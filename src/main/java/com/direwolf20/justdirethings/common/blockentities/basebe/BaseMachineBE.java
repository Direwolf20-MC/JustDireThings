package com.direwolf20.justdirethings.common.blockentities.basebe;

import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;

public class BaseMachineBE extends BlockEntity {
    public int MACHINE_SLOTS = 0;

    public BaseMachineBE(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        // Vanilla uses the type parameter to indicate which type of tile entity (command block, skull, or beacon?) is receiving the packet, but it seems like Forge has overridden this behavior
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public ItemStackHandler getMachineHandler() {
        return getData(Registration.MACHINE_HANDLER);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        this.load(tag);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        super.onDataPacket(net, pkt);
        if (this instanceof AreaAffectingBE areaAffectingBE)
            areaAffectingBE.getAreaAffectingData().area = null; //Clear this cache when a packet comes in, so it can redraw properly if the area was changed
    }

    public void markDirtyClient() {
        setChanged();
        if (level != null) {
            BlockState state = level.getBlockState(getBlockPos());
            level.sendBlockUpdated(getBlockPos(), state, state, 3);
        }
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (this instanceof FilterableBE filterableBE)
            filterableBE.getFilterData().filterCache.clear();
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (this instanceof AreaAffectingBE areaAffectingBE)
            areaAffectingBE.saveAreaSettings(tag);
        if (this instanceof FilterableBE filterableBE)
            filterableBE.saveFilterSettings(tag);
        if (this instanceof RedstoneControlledBE redstoneControlledBE)
            redstoneControlledBE.saveRedstoneSettings(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        if (this instanceof AreaAffectingBE areaAffectingBE)
            areaAffectingBE.loadAreaSettings(tag);
        if (this instanceof FilterableBE filterableBE)
            filterableBE.loadFilterSettings(tag);
        if (this instanceof RedstoneControlledBE redstoneControlledBE)
            redstoneControlledBE.loadRedstoneSettings(tag);
        super.load(tag);
    }
}
