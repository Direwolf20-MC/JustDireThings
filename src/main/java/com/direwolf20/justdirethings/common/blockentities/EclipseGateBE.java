package com.direwolf20.justdirethings.common.blockentities;

import com.direwolf20.justdirethings.setup.JDTRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.List;


public class EclipseGateBE extends BlockEntity {
    public byte lifetime;
    public BlockState sourceBlock;

    public EclipseGateBE(BlockPos pos, BlockState state) {
        super(JDTRegistration.EclipseGateBE.get(), pos, state);
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
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.sourceBlock = input.read("sourceBlock", BlockState.CODEC).orElse(null);
        this.lifetime = input.getByteOr("lifetime", lifetime);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        if (this.sourceBlock != null) {
            output.store("sourceBlock", BlockState.CODEC, this.sourceBlock);
            output.putByte("lifetime", this.lifetime);
        }
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void handleUpdateTag(ValueInput input) {
        this.loadWithComponents(input);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        return saveCustomOnly(provider);
    }

    @Override
    public void onDataPacket(Connection net, ValueInput input) {
        this.loadWithComponents(input);
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
