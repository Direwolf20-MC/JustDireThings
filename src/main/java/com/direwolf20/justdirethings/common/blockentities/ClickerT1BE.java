package com.direwolf20.justdirethings.common.blockentities;

import com.direwolf20.justdirethings.common.blockentities.basebe.BaseMachineBE;
import com.direwolf20.justdirethings.common.blockentities.basebe.RedstoneControlledBE;
import com.direwolf20.justdirethings.common.blocks.ClickerT1;
import com.direwolf20.justdirethings.setup.Registration;
import com.direwolf20.justdirethings.util.MiscHelpers;
import com.direwolf20.justdirethings.util.interfacehelpers.RedstoneControlData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.common.util.FakePlayer;

import java.util.ArrayList;
import java.util.List;

public class ClickerT1BE extends BaseMachineBE implements RedstoneControlledBE {
    public RedstoneControlData redstoneControlData = new RedstoneControlData();
    protected Direction FACING = Direction.DOWN; //To avoid nulls
    List<BlockPos> positionsToClick = new ArrayList<>();

    public ClickerT1BE(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
        MACHINE_SLOTS = 1; //Slot for tool
        if (pBlockState.getBlock() instanceof ClickerT1) //Only do this for the Tier 1, as its the only one with a facing....
            FACING = getBlockState().getValue(BlockStateProperties.FACING);
    }

    public ClickerT1BE(BlockPos pPos, BlockState pBlockState) {
        this(Registration.ClickerT1BE.get(), pPos, pBlockState);
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
    }

    public ItemStack getClickStack() {
        return getMachineHandler().getStackInSlot(0);
    }

    public boolean isStackValid(ItemStack itemStack) {
        return true;
    }

    public boolean canClick() {
        return true;
    }

    public boolean clearTrackerIfNeeded(ItemStack itemStack) {
        if (positionsToClick.isEmpty())
            return false;
        if (!isStackValid(itemStack))
            return true;
        if (!canClick())
            return true;
        if (!isActiveRedstone() && !redstoneControlData.redstoneMode.equals(MiscHelpers.RedstoneMode.PULSE))
            return true;
        return false;
    }

    public void doClick() {
        ItemStack placeStack = getClickStack();
        if (clearTrackerIfNeeded(placeStack)) {
            positionsToClick.clear();
            return;
        }
        FakePlayer fakePlayer = getFakePlayer((ServerLevel) level);
        if (isActiveRedstone() && canRun() && positionsToClick.isEmpty())
            positionsToClick = findSpotsToClick(fakePlayer);
        if (positionsToClick.isEmpty())
            return;
        BlockPos blockPos = positionsToClick.remove(0);
        setFakePlayerData(placeStack, fakePlayer, blockPos, getDirectionValue().getOpposite());
        click(placeStack, fakePlayer, blockPos);
    }

    public void click(ItemStack itemStack, FakePlayer fakePlayer, BlockPos blockPos) {
        /*Direction placing = Direction.values()[direction];
        Vec3 hitVec = Vec3.atCenterOf(blockPos); // Center of the block where we want to place the new block
        BlockHitResult hitResult = new BlockHitResult(hitVec, placing.getOpposite(), blockPos, false);
        UseOnContext useoncontext = new UseOnContext(fakePlayer, InteractionHand.MAIN_HAND, hitResult);
        itemStack.useOn(useoncontext);*/
    }

    public boolean isBlockPosValid(FakePlayer fakePlayer, BlockPos blockPos) {
        if (!level.mayInteract(fakePlayer, blockPos))
            return false;
        return true;
    }

    public List<BlockPos> findSpotsToClick(FakePlayer fakePlayer) {
        List<BlockPos> returnList = new ArrayList<>();
        BlockPos blockPos = getBlockPos().relative(FACING);
        if (isBlockPosValid(fakePlayer, blockPos))
            returnList.add(blockPos);
        return returnList;
    }
}
