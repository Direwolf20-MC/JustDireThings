package com.direwolf20.justdirethings.common.blockentities;

import com.direwolf20.justdirethings.common.blockentities.basebe.BaseMachineBE;
import com.direwolf20.justdirethings.common.blockentities.basebe.RedstoneControlledBE;
import com.direwolf20.justdirethings.common.blocks.BlockPlacerT1;
import com.direwolf20.justdirethings.setup.Registration;
import com.direwolf20.justdirethings.util.MiscHelpers;
import com.direwolf20.justdirethings.util.interfacehelpers.RedstoneControlData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.FakePlayer;

import java.util.ArrayList;
import java.util.List;

public class BlockPlacerT1BE extends BaseMachineBE implements RedstoneControlledBE {
    public RedstoneControlData redstoneControlData = new RedstoneControlData();
    protected Direction FACING = Direction.DOWN; //To avoid nulls
    List<BlockPos> positionsToPlace = new ArrayList<>();

    public BlockPlacerT1BE(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
        MACHINE_SLOTS = 1; //Slot for a pickaxe
        if (pBlockState.getBlock() instanceof BlockPlacerT1) //Only do this for the Tier 1, as its the only one with a facing....
            FACING = getBlockState().getValue(BlockStateProperties.FACING);
    }

    public BlockPlacerT1BE(BlockPos pPos, BlockState pBlockState) {
        this(Registration.BlockPlacerT1BE.get(), pPos, pBlockState);
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
        doBlockPlace();
    }

    public ItemStack getPlaceStack() {
        return getMachineHandler().getStackInSlot(0);
    }

    public boolean isStackValid(ItemStack itemStack) {
        if (itemStack.isEmpty())
            return false;
        if (!(itemStack.getItem() instanceof BlockItem))
            return false;
        return true;
    }

    public boolean canPlace() {
        return true;
    }

    public boolean clearTrackerIfNeeded(ItemStack itemStack) {
        if (positionsToPlace.isEmpty())
            return false;
        if (!isStackValid(itemStack))
            return true;
        if (!canPlace())
            return true;
        if (!isActiveRedstone() && !redstoneControlData.redstoneMode.equals(MiscHelpers.RedstoneMode.PULSE))
            return true;
        return false;
    }

    public void doBlockPlace() {
        ItemStack placeStack = getPlaceStack();
        if (!isStackValid(placeStack)) return;
        if (clearTrackerIfNeeded(placeStack)) {
            positionsToPlace.clear();
            return;
        }
        FakePlayer fakePlayer = getFakePlayer((ServerLevel) level);
        if (isActiveRedstone() && canRun() && positionsToPlace.isEmpty())
            positionsToPlace = findSpotsToPlace(fakePlayer);
        if (positionsToPlace.isEmpty())
            return;
        if (canRun()) {
            BlockPos blockPos = positionsToPlace.remove(0);
            setFakePlayerData(placeStack, fakePlayer, blockPos, getDirectionValue().getOpposite());
            placeBlock(placeStack, fakePlayer, blockPos);
        }
    }

    public InteractionResult placeBlock(ItemStack itemStack, FakePlayer fakePlayer, BlockPos blockPos) {
        Direction placing = Direction.values()[direction];
        Vec3 hitVec = Vec3.atCenterOf(blockPos); // Center of the block where we want to place the new block
        BlockHitResult hitResult = new BlockHitResult(hitVec, placing.getOpposite(), blockPos, false);
        UseOnContext useoncontext = new UseOnContext(fakePlayer, InteractionHand.MAIN_HAND, hitResult);
        return itemStack.useOn(useoncontext);
    }

    public boolean isBlockPosValid(FakePlayer fakePlayer, BlockPos blockPos) {
        if (!level.mayInteract(fakePlayer, blockPos))
            return false;
        if (!level.getBlockState(blockPos).canBeReplaced())
            return false;
        return true;
    }

    public List<BlockPos> findSpotsToPlace(FakePlayer fakePlayer) {
        List<BlockPos> returnList = new ArrayList<>();
        BlockPos blockPos = getBlockPos().relative(FACING);
        if (isBlockPosValid(fakePlayer, blockPos))
            returnList.add(blockPos);
        return returnList;
    }
}
