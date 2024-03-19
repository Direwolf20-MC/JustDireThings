package com.direwolf20.justdirethings.common.blockentities;

import com.direwolf20.justdirethings.client.particles.itemparticle.ItemFlowParticleData;
import com.direwolf20.justdirethings.common.containers.handlers.FilterBasicHandler;
import com.direwolf20.justdirethings.setup.Registration;
import com.direwolf20.justdirethings.util.MiscHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;

import java.util.List;

import static net.minecraft.world.entity.Entity.RemovalReason.DISCARDED;

public class ItemCollectorBE extends BlockEntity {
    protected BlockCapabilityCache<IItemHandler, Direction> attachedInventory;
    public static final int maxRadius = 9;
    public static final int maxOffset = 16;
    public int xRadius = 3, yRadius = 3, zRadius = 3;
    public int xOffset = 0, yOffset = 0, zOffset = 0;
    public boolean allowlist = false, compareNBT = false, renderArea = false;
    public MiscHelpers.RedstoneMode redstoneMode = MiscHelpers.RedstoneMode.IGNORED;

    public ItemCollectorBE(BlockPos pPos, BlockState pBlockState) {
        super(Registration.ItemCollectorBE.get(), pPos, pBlockState);
    }

    public void setSettings(int x, int y, int z, int xo, int yo, int zo, boolean allowlist, boolean compareNBT, boolean renderArea, int redstoneMode) {
        this.xRadius = Math.min(x, maxRadius);
        this.yRadius = Math.min(y, maxRadius);
        this.zRadius = Math.min(z, maxRadius);
        this.xOffset = Math.min(xo, maxOffset);
        this.yOffset = Math.min(yo, maxOffset);
        this.zOffset = Math.min(zo, maxOffset);
        this.allowlist = allowlist;
        this.compareNBT = compareNBT;
        this.renderArea = renderArea;
        this.redstoneMode = MiscHelpers.RedstoneMode.values()[redstoneMode];
        markDirtyClient();
    }

    public void tickClient() {
    }

    public void tickServer() {
        findItemsAndStore();
    }

    public FilterBasicHandler getHandler() {
        return getData(Registration.HANDLER_ITEM_COLLECTOR);
    }

    public void doParticles(ItemStack itemStack, Vec3 sourcePos) {
        BlockPos blockPos = getBlockPos();
        ItemFlowParticleData data = new ItemFlowParticleData(itemStack, blockPos.getX() + 0.5f, blockPos.getY() + 0.5f, blockPos.getZ() + 0.5f, 5);
        double d0 = sourcePos.x();
        double d1 = sourcePos.y();
        double d2 = sourcePos.z();
        ((ServerLevel) level).sendParticles(data, d0, d1, d2, 10, 0, 0, 0, 0);
    }

    private void findItemsAndStore() {
        assert level != null;
        AABB searchArea = new AABB(getBlockPos().offset(xOffset, yOffset, zOffset)).inflate(xRadius, yRadius, zRadius);

        List<ItemEntity> entityList = level.getEntitiesOfClass(ItemEntity.class, searchArea, entity -> true)
                .stream().toList();

        if (entityList.isEmpty()) return;

        IItemHandler handler = getAttachedInventory();

        if (handler == null) return;

        for (ItemEntity itemEntity : entityList) {
            ItemStack stack = itemEntity.getItem();
            ItemStack leftover = ItemHandlerHelper.insertItemStacked(handler, stack, false);
            if (leftover.isEmpty()) {
                // If the stack is now empty, remove the ItemEntity from the collection
                doParticles(itemEntity.getItem(), itemEntity.getPosition(0));
                itemEntity.remove(DISCARDED);
            } else {
                // Otherwise, update the ItemEntity with the modified stack
                itemEntity.setItem(leftover);
            }
        }
    }

    private IItemHandler getAttachedInventory() {
        if (attachedInventory == null) {
            assert this.level != null;
            BlockState state = level.getBlockState(getBlockPos());
            Direction facing = state.getValue(BlockStateProperties.FACING);
            BlockPos inventoryPos = getBlockPos().relative(facing);
            attachedInventory = BlockCapabilityCache.create(
                    Capabilities.ItemHandler.BLOCK, // capability to cache
                    (ServerLevel) this.level, // level
                    inventoryPos, // target position
                    facing.getOpposite() // context (The side of the block we're trying to pull/push from?)
            );
        }
        return attachedInventory.getCapability();
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
        tag.putBoolean("allowlist", allowlist);
        tag.putBoolean("compareNBT", compareNBT);
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
            allowlist = tag.getBoolean("allowlist");
            compareNBT = tag.getBoolean("compareNBT");
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
    }

    public void markDirtyClient() {
        this.setChanged();
        if (this.getLevel() != null) {
            BlockState state = this.getLevel().getBlockState(this.getBlockPos());
            this.getLevel().sendBlockUpdated(this.getBlockPos(), state, state, 3);
        }
    }

}
