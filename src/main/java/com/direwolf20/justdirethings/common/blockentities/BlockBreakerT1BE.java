package com.direwolf20.justdirethings.common.blockentities;

import com.direwolf20.justdirethings.common.blockentities.basebe.BaseMachineBE;
import com.direwolf20.justdirethings.common.blockentities.basebe.RedstoneControlledBE;
import com.direwolf20.justdirethings.setup.Registration;
import com.direwolf20.justdirethings.util.MiscHelpers;
import com.direwolf20.justdirethings.util.interfacehelpers.RedstoneControlData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.protocol.game.ClientboundBlockDestructionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
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
    private record BlockBreakingProgress(BlockState blockState, int ticks, int lastSentProgress, int iterator,
                                         float destroyProgress) {
        public BlockBreakingProgress(BlockState blockState, int ticks, int iterator, float destroyProgress) {
            this(blockState, ticks, -1, iterator, destroyProgress); // Initialize with -1 to indicate no progress sent yet
        }
    }

    LinkedHashMap<BlockPos, BlockBreakingProgress> blockBreakingTracker = new LinkedHashMap<>();
    public RedstoneControlData redstoneControlData = new RedstoneControlData();
    Map.Entry<BlockPos, BlockBreakingProgress> currentBlock;

    public BlockBreakerT1BE(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
        MACHINE_SLOTS = 1; //Slot for a pickaxe
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
            sendClearPacket(entry.getKey(), fakePlayer.getId() + entry.getValue().iterator);
            iterator.remove();
        }
    }

    public void sendClearPacket(BlockPos blockPos, int pBreakerId) {
        sendPackets(pBreakerId, blockPos, -1);
    }

    public void removePosFromTracker(BlockPos blockPos, int pBreakerId) {
        sendClearPacket(blockPos, pBreakerId);
        blockBreakingTracker.remove(blockPos);
    }

    public void clearTrackerIfNeeded(ItemStack tool, FakePlayer fakePlayer) {
        if (blockBreakingTracker.isEmpty())
            return;
        if (tool.isEmpty())
            clearTracker(fakePlayer); //If we were breaking blocks before, and removed the tool, clear the progress
        if (!isActiveRedstone() && !redstoneControlData.redstoneMode.equals(MiscHelpers.RedstoneMode.PULSE))
            clearTracker(fakePlayer); //If we are in Pulse Mode, don't clear, but otherwise, do clear on redstone signal turned off
    }

    @Override
    public void tickServer() {
        super.tickServer();
        doBlockBreak();
    }

    public boolean canMine() {
        return true;
    }

    public void doBlockBreak() {
        ItemStack tool = getTool();
        FakePlayer fakePlayer = getFakePlayer((ServerLevel) level);
        clearTrackerIfNeeded(tool, fakePlayer);
        if (tool.isEmpty()) return;
        if (isActiveRedstone() && canRun() && blockBreakingTracker.isEmpty()) {
            List<BlockPos> blocksToMine = findBlocksToMine(fakePlayer);
            for (BlockPos blockPos : blocksToMine) {
                BlockState blockState = level.getBlockState(blockPos);
                if (!blockState.isAir() && !blockBreakingTracker.containsKey(blockPos)) //Start tracking the mine!
                    startMining(fakePlayer, blockPos, blockState, tool);
            }
        }
        if (blockBreakingTracker.isEmpty() || !canMine()) return;
        if (currentBlock == null && canRun())
            currentBlock = blockBreakingTracker.entrySet().iterator().next();
        if (currentBlock != null && (mineBlock(currentBlock.getKey(), tool, fakePlayer))) {
            removePosFromTracker(currentBlock.getKey(), fakePlayer.getId() + currentBlock.getValue().iterator);
            currentBlock = null;
        }
    }

    public boolean isBlockValid(FakePlayer fakePlayer, BlockPos blockPos) {
        BlockState blockState = level.getBlockState(blockPos);
        if (blockState.isAir())
            return false;
        if (blockPos.equals(getBlockPos()))
            return false;
        if (blockBreakingTracker.containsKey(blockPos))
            return false;
        if (blockState.getDestroySpeed(level, blockPos) < 0)
            return false;
        if (!level.mayInteract(fakePlayer, blockPos))
            return false;
        if (!canBreakAndPlaceAt(level, blockPos, fakePlayer)) //TODO Break only once its working
            return false;
        return true;
    }

    public List<BlockPos> findBlocksToMine(FakePlayer fakePlayer) {
        List<BlockPos> returnList = new ArrayList<>();
        BlockPos blockPos = getBlockPos().relative(getBlockState().getValue(BlockStateProperties.FACING));
        if (isBlockValid(fakePlayer, blockPos))
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

    public Direction getFacing() {
        return getBlockState().getValue(BlockStateProperties.FACING);
    }

    public void startMining(FakePlayer fakePlayer, BlockPos blockPos, BlockState blockState, ItemStack tool) {
        //if (!tool.isCorrectToolForDrops(blockState)) return;
        setFakePlayerData(tool, fakePlayer, blockPos, getFacing());
        blockBreakingTracker.put(blockPos, new BlockBreakingProgress(blockState, 0, blockBreakingTracker.size() + generatePosHash(), getDestroyProgress(blockPos, tool, fakePlayer, blockState)));
    }

    /**
     * @return true if we should remove the block from the map (Via the iterator - above)
     */
    public boolean mineBlock(BlockPos blockPos, ItemStack tool, FakePlayer player) {
        BlockState blockState = level.getBlockState(blockPos);
        if (blockState.isAir()) { //If we got here, and the block is air, it means we had a block there before and it has since been removed
            return true;
        }
        if (blockBreakingTracker.containsKey(blockPos) && !level.getBlockState(blockPos).equals(blockBreakingTracker.get(blockPos).blockState)) {
            return true;
        }
        BlockBreakingProgress progress = blockBreakingTracker.compute(blockPos, (pos, oldProgress) -> {
            int updatedTicks = oldProgress == null ? 1 : oldProgress.ticks + 1;
            if (oldProgress == null)
                return new BlockBreakingProgress(blockState, 1, -1, blockBreakingTracker.size() + generatePosHash(), getDestroyProgress(blockPos, tool, player, blockState));
            else
                return new BlockBreakingProgress(blockState, updatedTicks, oldProgress.lastSentProgress, oldProgress.iterator, oldProgress.destroyProgress);
        });
        float destroyProgress = progress.destroyProgress * progress.ticks;
        int currentProgress = (int) (destroyProgress * 10.0F);
        if (currentProgress != progress.lastSentProgress && currentProgress < 10) {
            sendPackets(player.getId() + progress.iterator, blockPos, currentProgress);
            blockBreakingTracker.put(blockPos, new BlockBreakingProgress(blockState, progress.ticks, currentProgress, progress.iterator, progress.destroyProgress));
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
            float modifier = tool.isCorrectToolForDrops(blockState) ? 30 : 100;
            return getDestroySpeed(blockPos, tool, player, blockState) / hardness / modifier; //Always the correct tool for drop!
        }
    }

    public float getDestroySpeed(BlockPos blockPos, ItemStack tool, FakePlayer player, BlockState blockState) {
        float toolDestroySpeed = tool.getDestroySpeed(blockState);
        if (toolDestroySpeed > 1.0F) {
            HolderLookup.RegistryLookup<Enchantment> registrylookup = level.getServer().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
            int efficiency = tool.getEnchantmentLevel(registrylookup.getOrThrow(Enchantments.EFFICIENCY));
            if (efficiency > 0) {
                toolDestroySpeed += (float) (efficiency * efficiency + 1);
            }
        }
        toolDestroySpeed = net.neoforged.neoforge.event.EventHooks.getBreakSpeed(player, blockState, toolDestroySpeed, blockPos);
        return toolDestroySpeed;
    }

    public boolean tryBreakBlock(ItemStack tool, FakePlayer fakePlayer, BlockPos breakPos, BlockState blockState) {
        setFakePlayerData(tool, fakePlayer, breakPos, getFacing());
        BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(level, breakPos, level.getBlockState(breakPos), fakePlayer);
        if (NeoForge.EVENT_BUS.post(event).isCanceled()) return false;
        breakBlock(fakePlayer, breakPos, tool, blockState);
        return true;
    }

    public void breakBlock(FakePlayer player, BlockPos breakPos, ItemStack itemStack, BlockState state) {
        //itemStack.onBlockStartBreak(breakPos, player);
        boolean success = level.destroyBlock(breakPos, false, player);
        if (success) {
            Block.dropResources(state, level, breakPos, level.getBlockEntity(breakPos), player, itemStack);
            if (state.getDestroySpeed(level, breakPos) != 0.0F)
                itemStack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(InteractionHand.MAIN_HAND));
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

    @Override
    public boolean isDefaultSettings() {
        if (!super.isDefaultSettings())
            return false;
        if (tickSpeed != 20)
            return false;
        if (!getRedstoneControlData().equals(getDefaultRedstoneData()))
            return false;
        return true;
    }
}
