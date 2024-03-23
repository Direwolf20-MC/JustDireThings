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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.event.level.BlockEvent;

public class BlockBreakerT1BE extends BaseMachineBE implements RedstoneControlledBE {
    public RedstoneControlData redstoneControlData = new RedstoneControlData();

    public BlockBreakerT1BE(BlockPos pPos, BlockState pBlockState) {
        super(Registration.BlockBreakerT1BE.get(), pPos, pBlockState);
        MACHINE_SLOTS = 1; //Slot for a pickaxe
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

    @Override
    public void tickServer() {
        super.tickServer();
        if (isActive()) {
            tryBreakBlock();
        }
    }

    public void tryBreakBlock() {
        FakePlayer fakePlayer = getFakePlayer((ServerLevel) level);
        Direction direction = getBlockState().getValue(BlockStateProperties.FACING);
        float xRot = direction == Direction.DOWN ? -90 : direction == Direction.UP ? -90 : 0;
        fakePlayer.setXRot(xRot);
        fakePlayer.setYRot(direction.toYRot());
        BlockPos breakPos = getBlockPos().relative(direction);
        BlockState blockState = level.getBlockState(breakPos);
        ItemStack itemStack = getMachineHandler().getStackInSlot(0);
        fakePlayer.setItemInHand(InteractionHand.MAIN_HAND, itemStack);
        if (itemStack.isEmpty()) return;
        if (!itemStack.isCorrectToolForDrops(blockState)) {
            return;
        }
        BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(level, breakPos, level.getBlockState(breakPos), fakePlayer);
        if (NeoForge.EVENT_BUS.post(event).isCanceled()) return;
        breakBlock(fakePlayer, breakPos, itemStack, blockState);
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
