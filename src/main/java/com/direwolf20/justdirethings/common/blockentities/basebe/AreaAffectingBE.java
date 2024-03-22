package com.direwolf20.justdirethings.common.blockentities.basebe;

import com.direwolf20.justdirethings.util.MiscHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class AreaAffectingBE extends BlockEntity {
    public static int maxRadius = 5;
    public static int maxOffset = 9;
    public int xRadius = 3, yRadius = 3, zRadius = 3;
    public int xOffset = 0, yOffset = 0, zOffset = 0;
    public boolean renderArea = false;
    public boolean receivingRedstone = false;
    public boolean checkedRedstone = false;
    public boolean pulsed = false;
    public MiscHelpers.RedstoneMode redstoneMode = MiscHelpers.RedstoneMode.IGNORED;
    public AABB area;

    public AreaAffectingBE(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    public AABB getAABB() {
        return getAABB(getBlockPos());
    }

    public AABB getAABB(BlockPos relativePos) {
        if (area == null)
            area = new AABB(relativePos.offset(xOffset, yOffset, zOffset)).inflate(xRadius, yRadius, zRadius);
        return area;
    }

    public void setAreaSettings(int x, int y, int z, int xo, int yo, int zo, boolean renderArea, int redstoneMode) {
        this.xRadius = Math.max(0, Math.min(x, maxRadius));
        this.yRadius = Math.max(0, Math.min(y, maxRadius));
        this.zRadius = Math.max(0, Math.min(z, maxRadius));
        this.xOffset = Math.max(-maxOffset, Math.min(xo, maxOffset));
        this.yOffset = Math.max(-maxOffset, Math.min(yo, maxOffset));
        this.zOffset = Math.max(-maxOffset, Math.min(zo, maxOffset));
        this.renderArea = renderArea;
        this.redstoneMode = MiscHelpers.RedstoneMode.values()[redstoneMode];
        area = null;
        markDirtyClient();
    }

    public void tickClient() {
    }

    public void tickServer() {
        if (!checkedRedstone)
            evaluateRedstone();
    }

    public void evaluateRedstone() {
        boolean newRedstoneSignal = this.level.hasNeighborSignal(this.getBlockPos());
        if (redstoneMode.equals(MiscHelpers.RedstoneMode.PULSE) && !receivingRedstone && newRedstoneSignal)
            this.pulsed = true;
        this.receivingRedstone = newRedstoneSignal;
        this.checkedRedstone = true;
    }

    public boolean isActive() {
        if (redstoneMode.equals(MiscHelpers.RedstoneMode.IGNORED))
            return true;
        if (redstoneMode.equals(MiscHelpers.RedstoneMode.LOW))
            return !receivingRedstone;
        if (redstoneMode.equals(MiscHelpers.RedstoneMode.HIGH))
            return receivingRedstone;
        if (redstoneMode.equals(MiscHelpers.RedstoneMode.PULSE) && pulsed) {
            pulsed = false;
            return true;
        }
        return false;
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("xRadius", xRadius);
        tag.putInt("yRadius", yRadius);
        tag.putInt("zRadius", zRadius);
        tag.putInt("xOffset", xOffset);
        tag.putInt("yOffset", yOffset);
        tag.putInt("zOffset", zOffset);
        tag.putBoolean("renderArea", renderArea);
        tag.putInt("redstoneMode", redstoneMode.ordinal());
    }

    @Override
    public void load(CompoundTag tag) {
        if (tag.contains("xRadius")) { //Assume all the others are there too...
            xRadius = tag.getInt("xRadius");
            yRadius = tag.getInt("yRadius");
            zRadius = tag.getInt("zRadius");
            xOffset = tag.getInt("xOffset");
            yOffset = tag.getInt("yOffset");
            zOffset = tag.getInt("zOffset");
            renderArea = tag.getBoolean("renderArea");
            redstoneMode = MiscHelpers.RedstoneMode.values()[tag.getInt("redstoneMode")];
        }
        super.load(tag);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        // Vanilla uses the type parameter to indicate which type of tile entity (command block, skull, or beacon?) is receiving the packet, but it seems like Forge has overridden this behavior
        return ClientboundBlockEntityDataPacket.create(this);
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
        this.load(pkt.getTag());
        this.area = null; //Clear this cache when a packet comes in, so it can redraw properly if the area was changed
    }

    public void markDirtyClient() {
        this.setChanged();
        if (this.getLevel() != null) {
            BlockState state = this.getLevel().getBlockState(this.getBlockPos());
            this.getLevel().sendBlockUpdated(this.getBlockPos(), state, state, 3);
        }
    }
}
