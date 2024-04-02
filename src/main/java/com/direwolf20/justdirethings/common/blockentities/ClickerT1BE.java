package com.direwolf20.justdirethings.common.blockentities;

import com.direwolf20.justdirethings.client.particles.itemparticle.ItemFlowParticleData;
import com.direwolf20.justdirethings.common.blockentities.basebe.BaseMachineBE;
import com.direwolf20.justdirethings.common.blockentities.basebe.RedstoneControlledBE;
import com.direwolf20.justdirethings.common.blocks.ClickerT1;
import com.direwolf20.justdirethings.setup.Registration;
import com.direwolf20.justdirethings.util.FakePlayerUtil;
import com.direwolf20.justdirethings.util.MiscHelpers;
import com.direwolf20.justdirethings.util.interfacehelpers.RedstoneControlData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.FakePlayer;

import java.util.ArrayList;
import java.util.List;

public class ClickerT1BE extends BaseMachineBE implements RedstoneControlledBE {
    public RedstoneControlData redstoneControlData = new RedstoneControlData();
    protected Direction FACING = Direction.DOWN; //To avoid nulls
    List<BlockPos> positionsToClick = new ArrayList<>();
    public int clickType = 0; //RightClick, 1 == Left Click
    public CLICK_TARGET clickTarget = CLICK_TARGET.BLOCK;
    public boolean sneaking = false;

    public enum CLICK_TARGET {
        BLOCK,
        AIR,
        HOSTILE,
        PASSIVE,
        PLAYER,
        LIVING;

        public CLICK_TARGET next() {
            CLICK_TARGET[] values = values();
            int nextOrdinal = (this.ordinal() + 1) % values.length;
            return values[nextOrdinal];
        }
    }

    public ClickerT1BE(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
        MACHINE_SLOTS = 1; //Slot for tool
        if (pBlockState.getBlock() instanceof ClickerT1) //Only do this for the Tier 1, as its the only one with a facing....
            FACING = getBlockState().getValue(BlockStateProperties.FACING);
    }

    public ClickerT1BE(BlockPos pPos, BlockState pBlockState) {
        this(Registration.ClickerT1BE.get(), pPos, pBlockState);
    }

    public void setClickerSettings(int clickType, int clickTarget, boolean sneaking) {
        this.clickType = clickType;
        this.clickTarget = CLICK_TARGET.values()[clickTarget];
        this.sneaking = sneaking;
        markDirtyClient();
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
        doClick();
    }

    public ItemStack getClickStack() {
        return getMachineHandler().getStackInSlot(0);
    }

    public void setClickStack(ItemStack stack) {
        getMachineHandler().setStackInSlot(0, stack);
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
        if (isActiveRedstone() && (canRun() || redstoneControlData.redstoneMode.equals(MiscHelpers.RedstoneMode.PULSE)) && positionsToClick.isEmpty())
            positionsToClick = findSpotsToClick(fakePlayer);
        if (positionsToClick.isEmpty())
            return;
        BlockPos blockPos = positionsToClick.remove(0);
        setFakePlayerData(placeStack, fakePlayer, blockPos, getDirectionValue());
        if (clickTarget.equals(CLICK_TARGET.BLOCK) || clickTarget.equals(CLICK_TARGET.AIR))
            clickBlock(placeStack, fakePlayer, blockPos);
    }

    public void clickBlock(ItemStack itemStack, FakePlayer fakePlayer, BlockPos blockPos) {
        Direction placing = Direction.values()[direction];
        FakePlayerUtil.setupFakePlayerForUse(fakePlayer, blockPos, placing, itemStack.copy(), sneaking);
        if (level instanceof ServerLevel serverLevel) { //Temp (maybe) for showing where the fake player is...
            Vec3 base = new Vec3(fakePlayer.getX(), fakePlayer.getEyeY(), fakePlayer.getZ());
            Vec3 look = fakePlayer.getLookAngle();
            Vec3 target = base.add(look.x * 0.9, look.y * 0.9, look.z * 0.9);
            ItemFlowParticleData data = new ItemFlowParticleData(itemStack, target.x, target.y, target.z, 5);
            double d0 = base.x();
            double d1 = base.y();
            double d2 = base.z();
            serverLevel.sendParticles(data, d0, d1, d2, 10, 0, 0, 0, 0);
        }
        ItemStack resultStack = itemStack.copy();
        if (!level.getBlockState(blockPos).isAir() && clickTarget.equals(CLICK_TARGET.BLOCK))
            resultStack = FakePlayerUtil.clickBlockInDirection(fakePlayer, level, blockPos, placing.getOpposite(), level.getBlockState(blockPos), clickType);
        else if (level.getBlockState(blockPos).isAir() && clickTarget.equals(CLICK_TARGET.AIR))
            resultStack = FakePlayerUtil.rightClickAirInDirection(fakePlayer, level, blockPos, placing.getOpposite(), level.getBlockState(blockPos));
        setClickStack(resultStack);
        FakePlayerUtil.cleanupFakePlayerFromUse(fakePlayer, getClickStack());
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

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("clickType", clickType);
        tag.putInt("clickTarget", clickTarget.ordinal());
        tag.putBoolean("sneaking", sneaking);
    }

    @Override
    public void load(CompoundTag tag) {
        this.clickType = tag.getInt("clickType");
        this.clickTarget = CLICK_TARGET.values()[tag.getInt("clickTarget")];
        this.sneaking = tag.getBoolean("sneaking");
        super.load(tag);
    }
}
