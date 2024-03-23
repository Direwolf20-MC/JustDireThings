package com.direwolf20.justdirethings.common.blockentities;

import com.direwolf20.justdirethings.common.blockentities.basebe.BaseMachineBE;
import com.direwolf20.justdirethings.common.blockentities.basebe.RedstoneControlledBE;
import com.direwolf20.justdirethings.setup.Registration;
import com.direwolf20.justdirethings.util.interfacehelpers.RedstoneControlData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.event.level.BlockEvent;

import java.util.HashMap;

public class BlockBreakerT1BE extends BaseMachineBE implements RedstoneControlledBE {
    private record BlockBreakingProgress(BlockState blockState, int ticks) {
    }

    HashMap<BlockPos, BlockBreakingProgress> blockBreakingTracker = new HashMap<>();
    public RedstoneControlData redstoneControlData = new RedstoneControlData();
    private final Direction direction;

    public BlockBreakerT1BE(BlockPos pPos, BlockState pBlockState) {
        super(Registration.BlockBreakerT1BE.get(), pPos, pBlockState);
        MACHINE_SLOTS = 1; //Slot for a pickaxe
        direction = getBlockState().getValue(BlockStateProperties.FACING);
    }

    @Override
    public RedstoneControlData getRedstoneControlData() {
        return redstoneControlData;
    }

    @Override
    public BlockEntity getBlockEntity() {
        return this;
    }

    @Override
    public void tickClient() {
    }

    public void validateTracker(ItemStack tool) {

    }

    @Override
    public void tickServer() {
        super.tickServer();
        ItemStack tool = getTool();
        if (tool.isEmpty()) { //Todo handle redstone states
            if (!blockBreakingTracker.isEmpty())
                blockBreakingTracker.clear(); //If we were breaking blocks before, and removed the tool, clear the progress!
            return;
        }
        BlockPos blockPos = getBlockPos().relative(direction);
        FakePlayer fakePlayer = getFakePlayer((ServerLevel) level);
        if (!level.mayInteract(fakePlayer, blockPos)) return;
        BlockState blockState = level.getBlockState(blockPos);
        if (!tool.isCorrectToolForDrops(blockState)) {
            return;
        }
        if (isActive()) {
            if (!blockState.isAir() && !blockBreakingTracker.containsKey(blockPos)) //Start tracking the mine!
                startMining(blockPos, blockState);
        }
        for (BlockPos mineBlocks : blockBreakingTracker.keySet()) {
            mineBlock(mineBlocks, tool, fakePlayer, blockState);
        }
    }

    public ItemStack getTool() {
        return getMachineHandler().getStackInSlot(0);
    }

    public void startMining(BlockPos blockPos, BlockState blockState) {
        blockBreakingTracker.put(blockPos, new BlockBreakingProgress(blockState, 0));
    }

    public void mineBlock(BlockPos blockPos, ItemStack tool, FakePlayer player, BlockState blockState) {
        if (blockState.isAir()) { //If we got here, and the block is air, it means we had a block there before and it has since been removed
            blockBreakingTracker.remove(blockPos);
            return;
        }
        BlockBreakingProgress progress = blockBreakingTracker.compute(blockPos, (pos, oldProgress) -> {
            if (oldProgress != null && oldProgress.blockState().equals(blockState)) {
                return new BlockBreakingProgress(blockState, oldProgress.ticks + 1);
            }
            return new BlockBreakingProgress(blockState, 1);
        });
        System.out.println(progress.ticks + ":" + (getDestroyProgress(blockPos, tool, player, blockState) * progress.ticks));
        if ((getDestroyProgress(blockPos, tool, player, blockState) * progress.ticks) >= 1.0f) {
            tryBreakBlock(tool, player, blockPos, blockState);
            blockBreakingTracker.remove(blockPos); //Remove it from the tracker whether we successfully broke or not!
        }
    }

    public float getDestroyProgress(BlockPos blockPos, ItemStack tool, FakePlayer player, BlockState blockState) {
        float hardness = blockState.getDestroySpeed(level, blockPos);
        if (hardness == -1.0F) {
            return -1.0F;
        } else {
            return getDestroySpeed(blockPos, tool, player, blockState) / hardness / (float) 30; //Always the correct tool for drop!
        }
    }

    public float getDestroySpeed(BlockPos blockPos, ItemStack tool, FakePlayer player, BlockState blockState) {
        float toolDestroySpeed = tool.getDestroySpeed(blockState);
        if (toolDestroySpeed > 1.0F) {
            int efficiency = EnchantmentHelper.getTagEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY, tool);
            if (efficiency > 0) {
                toolDestroySpeed += (float) (efficiency * efficiency + 1);
            }
        }
        toolDestroySpeed = net.neoforged.neoforge.event.EventHooks.getBreakSpeed(player, blockState, toolDestroySpeed, blockPos);
        return toolDestroySpeed;
    }

    public boolean tryBreakBlock(ItemStack tool, FakePlayer fakePlayer, BlockPos breakPos, BlockState blockState) {
        float xRot = direction == Direction.DOWN ? -90 : direction == Direction.UP ? -90 : 0;
        fakePlayer.setXRot(xRot);
        fakePlayer.setYRot(direction.toYRot());
        fakePlayer.setItemInHand(InteractionHand.MAIN_HAND, tool);
        BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(level, breakPos, level.getBlockState(breakPos), fakePlayer);
        if (NeoForge.EVENT_BUS.post(event).isCanceled()) return false;
        breakBlock(fakePlayer, breakPos, tool, blockState);
        return true;
    }

    public void breakBlock(FakePlayer player, BlockPos breakPos, ItemStack itemStack, BlockState state) {
        itemStack.onBlockStartBreak(breakPos, player);
        boolean success = level.destroyBlock(breakPos, false, player);
        if (success) {
            Block.dropResources(state, level, breakPos, level.getBlockEntity(breakPos), player, itemStack);
            if (state.getDestroySpeed(level, breakPos) != 0.0F)
                itemStack.hurtAndBreak(1, player, pOnBroken -> {
                });
        }
    }
}
