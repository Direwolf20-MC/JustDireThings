package com.direwolf20.justdirethings.common.blockentities;

import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;


public class EclipseGateBE extends BlockEntity {
    public byte lifetime;
    public BlockState sourceBlock;

    public EclipseGateBE(BlockPos pos, BlockState state) {
        super(Registration.EclipseGateBE.get(), pos, state);
    }

    public void tickClient() {

    }

    public void tickServer() {
        tickLifetime();
        if (lifetime >= getMaxLife()) {
            setRealBlock(sourceBlock);
        }
    }

    public void setSourceBlock(BlockState blockState) {
        this.sourceBlock = blockState;
    }

    public byte getMaxLife() {
        return 100;
    }

    public void setRealBlock(BlockState realBlock) {
        if (realBlock == null) { //This should never happen in theory, defensive coding
            level.setBlockAndUpdate(this.getBlockPos(), Blocks.AIR.defaultBlockState());
            return;
        }
        if (!realBlock.canSurvive(level, getBlockPos())) {
            List<ItemStack> drops = Block.getDrops(realBlock, (ServerLevel) level, getBlockPos(), null);
            for (ItemStack returnedItem : drops) {
                ItemEntity itementity = new ItemEntity(level, getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(), returnedItem);
                itementity.setPickUpDelay(40);
                level.addFreshEntity(itementity);
                level.setBlockAndUpdate(this.getBlockPos(), Blocks.AIR.defaultBlockState());
                return;
            }
        }
        BlockState adjustedState = Block.updateFromNeighbourShapes(realBlock, level, getBlockPos()); //Ensure double chests are placed as single chests if only 1 chest available in copy/paste, for example, or fixes fences
        level.setBlockAndUpdate(this.getBlockPos(), adjustedState);
    }

    public void tickLifetime() {
        lifetime++;
        if (lifetime >= getMaxLife())
            lifetime = getMaxLife();
    }

    /** Misc Methods for TE's */
    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.sourceBlock = NbtUtils.readBlockState(BuiltInRegistries.BLOCK.asLookup(), tag.getCompound("sourceBlock"));
        this.lifetime = tag.getByte("lifetime");
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (this.sourceBlock != null) {
            tag.put("sourceBlock", NbtUtils.writeBlockState(this.sourceBlock));
            tag.putByte("lifetime", this.lifetime);
        }
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
        if (pkt.getTag() == null) return;
        this.load(pkt.getTag());
    }

    public void markDirtyClient() {
        this.setChanged();
        if (this.getLevel() != null) {
            BlockState state = this.getLevel().getBlockState(this.getBlockPos());
            this.getLevel().sendBlockUpdated(this.getBlockPos(), state, state, 3);
        }
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
    }

    @Override
    public void clearRemoved() {
        super.clearRemoved();
    }
}
