package com.direwolf20.justdirethings.common.blockentities;

import com.direwolf20.justdirethings.common.blockentities.basebe.BaseMachineBE;
import com.direwolf20.justdirethings.common.blockentities.basebe.RedstoneControlledBE;
import com.direwolf20.justdirethings.setup.Registration;
import com.direwolf20.justdirethings.util.MiscHelpers;
import com.direwolf20.justdirethings.util.interfacehelpers.RedstoneControlData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundBlockDestructionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.event.level.BlockEvent;

import java.util.*;

public class BlockBreakerT1BE extends BaseMachineBE implements RedstoneControlledBE {
    //BlockState we're breaking, ticks since we started, lastpacket progress we sent, and an iterator, because each packet needs a unique breaker ID
    private record BlockBreakingProgress(BlockState blockState, int ticks, int lastSentProgress, int iterator) {
        public BlockBreakingProgress(BlockState blockState, int ticks, int iterator) {
            this(blockState, ticks, -1, iterator); // Initialize with -1 to indicate no progress sent yet
        }
    }

    HashMap<BlockPos, BlockBreakingProgress> blockBreakingTracker = new HashMap<>();
    public RedstoneControlData redstoneControlData = new RedstoneControlData();
    private final Direction direction;

    public BlockBreakerT1BE(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
        MACHINE_SLOTS = 1; //Slot for a pickaxe
        direction = getBlockState().getValue(BlockStateProperties.FACING);
    }

    public BlockBreakerT1BE(BlockPos pPos, BlockState pBlockState) {
        this(Registration.BlockBreakerT1BE.get(), pPos, pBlockState);
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

    public void clearTracker(FakePlayer fakePlayer) {
        Iterator<Map.Entry<BlockPos, BlockBreakingProgress>> iterator = blockBreakingTracker.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<BlockPos, BlockBreakingProgress> entry = iterator.next();
            BlockPos pos = entry.getKey();
            removeTrackerEntry(pos, fakePlayer.getId() + entry.getValue().iterator);
        }
    }

    public void sendClearPacket(BlockPos blockPos, int pBreakerId) {
        sendPackets(pBreakerId, blockPos, -1);
    }

    public void removeTrackerEntry(BlockPos blockPos, int pBreakerId) {
        sendClearPacket(blockPos, pBreakerId);
        blockBreakingTracker.remove(blockPos);
    }

    public void clearTrackerIfNeeded(ItemStack tool, FakePlayer fakePlayer) {
        if (blockBreakingTracker.isEmpty())
            return;
        if (tool.isEmpty())
            clearTracker(fakePlayer); //If we were breaking blocks before, and removed the tool, clear the progress
        if (!isActive() && !redstoneControlData.redstoneMode.equals(MiscHelpers.RedstoneMode.PULSE))
            clearTracker(fakePlayer); //If we are in Pulse Mode, don't clear, but otherwise, do clear on redstone signal turned off
    }

    @Override
    public void tickServer() {
        super.tickServer();
        doBlockBreak();
    }

    public void doBlockBreak() {
        ItemStack tool = getTool();
        Set<BlockPos> blocksToMine = findBlocksToMine();
        FakePlayer fakePlayer = getFakePlayer((ServerLevel) level);
        clearTrackerIfNeeded(tool, fakePlayer);
        if (tool.isEmpty()) return;
        if (isActive()) {
            for (BlockPos blockPos : blocksToMine) {
                BlockState blockState = level.getBlockState(blockPos);
                if (!blockState.isAir() && !blockBreakingTracker.containsKey(blockPos)) //Start tracking the mine!
                    startMining(fakePlayer, blockPos, blockState, tool);
            }
        }
        Iterator<Map.Entry<BlockPos, BlockBreakingProgress>> iterator = blockBreakingTracker.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<BlockPos, BlockBreakingProgress> entry = iterator.next();
            if (mineBlock(entry.getKey(), tool, fakePlayer)) {
                sendClearPacket(entry.getKey(), fakePlayer.getId() + entry.getValue().iterator);
                iterator.remove();
            }
        }
    }

    public Set<BlockPos> findBlocksToMine() {
        Set<BlockPos> returnList = new HashSet<>();
        BlockPos blockPos = getBlockPos().relative(direction);
        if (!blockBreakingTracker.containsKey(blockPos))
            returnList.add(blockPos);
        return returnList;
    }

    public ItemStack getTool() {
        return getMachineHandler().getStackInSlot(0);
    }

    public int generatePosHash() {
        BlockPos blockPos = getBlockPos();
        return blockPos.getX() + blockPos.getY() + blockPos.getZ(); //For now this is probably good enough, will add more randomness if needed
    }

    public void startMining(FakePlayer fakePlayer, BlockPos blockPos, BlockState blockState, ItemStack tool) {
        if (!tool.isCorrectToolForDrops(blockState)) return;
        if (!level.mayInteract(fakePlayer, blockPos)) return;
        blockBreakingTracker.put(blockPos, new BlockBreakingProgress(blockState, 0, blockBreakingTracker.size() + generatePosHash()));
    }

    /**
     * @return true if we should remove the block from the map (Via the iterator - above)
     */
    public boolean mineBlock(BlockPos blockPos, ItemStack tool, FakePlayer player) {
        BlockState blockState = level.getBlockState(blockPos);
        if (blockState.isAir() || !tool.isCorrectToolForDrops(blockState)) { //If we got here, and the block is air, it means we had a block there before and it has since been removed
            return true;
        }
        if (blockBreakingTracker.containsKey(blockPos) && !level.getBlockState(blockPos).equals(blockBreakingTracker.get(blockPos).blockState)) {
            return true;
        }
        BlockBreakingProgress progress = blockBreakingTracker.compute(blockPos, (pos, oldProgress) -> {
            int updatedTicks = oldProgress == null ? 1 : oldProgress.ticks + 1;
            return new BlockBreakingProgress(blockState, updatedTicks, oldProgress == null ? -1 : oldProgress.lastSentProgress, oldProgress.iterator);
        });
        float destroyProgress = (getDestroyProgress(blockPos, tool, player, blockState) * progress.ticks);
        int currentProgress = (int) (destroyProgress * 10.0F);
        if (currentProgress != progress.lastSentProgress && currentProgress < 10) {
            sendPackets(player.getId() + progress.iterator, blockPos, currentProgress);
            blockBreakingTracker.put(blockPos, new BlockBreakingProgress(blockState, progress.ticks, currentProgress, progress.iterator));
        }
        if (destroyProgress >= 1.0f) {
            tryBreakBlock(tool, player, blockPos, blockState);
            return true;
        }
        return false;
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

    public void sendPackets(int pBreakerId, BlockPos pPos, int pProgress) {
        for (ServerPlayer serverplayer : level.getServer().getPlayerList().getPlayers()) {
            if (serverplayer != null && serverplayer.level() == level && serverplayer.getId() != pBreakerId) {
                double d0 = (double) pPos.getX() - serverplayer.getX();
                double d1 = (double) pPos.getY() - serverplayer.getY();
                double d2 = (double) pPos.getZ() - serverplayer.getZ();
                if (d0 * d0 + d1 * d1 + d2 * d2 < 1024.0) {
                    serverplayer.connection.send(new ClientboundBlockDestructionPacket(pBreakerId, pPos, pProgress));
                }
            }
        }
    }
}
