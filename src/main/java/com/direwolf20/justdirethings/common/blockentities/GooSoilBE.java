package com.direwolf20.justdirethings.common.blockentities;

import com.direwolf20.justdirethings.setup.Registration;
import com.direwolf20.justdirethings.util.NBTUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class GooSoilBE extends BlockEntity {
    private GlobalPos boundInventory;
    private Direction inventorySide;

    public GooSoilBE(BlockPos pos, BlockState state) {
        super(Registration.GooSoilBE.get(), pos, state);
    }

    public void bindInventory(GlobalPos inventoryPos, Direction inventorySide) {
        this.boundInventory = inventoryPos;
        this.inventorySide = inventorySide;
        this.setChanged();
    }

    public GlobalPos getBoundInventory() {
        return boundInventory;
    }

    public Direction getInventorySide() {
        return inventorySide;
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (boundInventory != null && inventorySide != null) {
            tag.put("boundinventory", NBTUtils.globalPosToNBT(boundInventory));
            tag.putInt("boundinventory_side", inventorySide.ordinal());
        }
    }

    @Override
    public void load(CompoundTag tag) {
        if (tag.contains("boundinventory"))
            boundInventory = NBTUtils.nbtToGlobalPos(tag.getCompound("boundinventory"));
        if (tag.contains("boundinventory_side"))
            inventorySide = Direction.values()[tag.getInt("boundinventory_side")];
        super.load(tag);
    }
}
