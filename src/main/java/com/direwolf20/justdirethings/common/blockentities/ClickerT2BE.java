package com.direwolf20.justdirethings.common.blockentities;

import com.direwolf20.justdirethings.common.blockentities.basebe.AreaAffectingBE;
import com.direwolf20.justdirethings.common.blockentities.basebe.FilterableBE;
import com.direwolf20.justdirethings.common.blockentities.basebe.PoweredMachineBE;
import com.direwolf20.justdirethings.common.blockentities.basebe.PoweredMachineContainerData;
import com.direwolf20.justdirethings.common.capabilities.MachineEnergyStorage;
import com.direwolf20.justdirethings.common.containers.handlers.FilterBasicHandler;
import com.direwolf20.justdirethings.setup.Registration;
import com.direwolf20.justdirethings.util.UsefulFakePlayer;
import com.direwolf20.justdirethings.util.interfacehelpers.AreaAffectingData;
import com.direwolf20.justdirethings.util.interfacehelpers.FilterData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.FakePlayer;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ClickerT2BE extends ClickerT1BE implements PoweredMachineBE, AreaAffectingBE, FilterableBE {
    public FilterData filterData = new FilterData();
    public AreaAffectingData areaAffectingData = new AreaAffectingData();
    public final PoweredMachineContainerData poweredMachineData;

    public ClickerT2BE(BlockPos pPos, BlockState pBlockState) {
        super(Registration.ClickerT2BE.get(), pPos, pBlockState);
        poweredMachineData = new PoweredMachineContainerData(this);
    }

    @Override
    public PoweredMachineContainerData getContainerData() {
        return poweredMachineData;
    }

    @Override
    public MachineEnergyStorage getEnergyStorage() {
        return getData(Registration.ENERGYSTORAGE_MACHINES);
    }

    @Override
    public int getStandardEnergyCost() {
        return 250;
    }

    @Override
    public AreaAffectingData getAreaAffectingData() {
        return areaAffectingData;
    }

    @Override
    public FilterBasicHandler getFilterHandler() {
        return getData(Registration.HANDLER_BASIC_FILTER);
    }

    @Override
    public FilterData getFilterData() {
        return filterData;
    }

    @Override
    public void tickServer() {
        super.tickServer();
        chargeItemStack(getClickStack());
    }

    @Override
    public boolean canClick() {
        return hasEnoughPower(getStandardEnergyCost());
    }

    @Override
    public InteractionResult clickEntity(ItemStack itemStack, UsefulFakePlayer fakePlayer, LivingEntity entity) {
        InteractionResult interactionResult = super.clickEntity(itemStack, fakePlayer, entity);
        if (interactionResult.equals(InteractionResult.SUCCESS))
            extractEnergy(getStandardEnergyCost(), false);
        return interactionResult;
    }

    @Override
    public InteractionResult clickBlock(ItemStack itemStack, UsefulFakePlayer fakePlayer, BlockPos blockPos) {
        InteractionResult interactionResult = super.clickBlock(itemStack, fakePlayer, blockPos);
        if (interactionResult.equals(InteractionResult.SUCCESS))
            extractEnergy(getStandardEnergyCost(), false);
        return interactionResult;
    }

    @Override
    public boolean isBlockPosValid(FakePlayer fakePlayer, BlockPos blockPos) {
        if (!super.isBlockPosValid(fakePlayer, blockPos))
            return false; //Do the same checks as normal, then check the filters
        ItemStack blockItemStack = level.getBlockState(blockPos).getCloneItemStack(new BlockHitResult(Vec3.ZERO, getDirectionValue(), blockPos, false), level, blockPos, fakePlayer);
        return isStackValidFilter(blockItemStack);
    }

    @Override
    public List<BlockPos> findSpotsToClick(FakePlayer fakePlayer) {
        AABB area = getAABB(getBlockPos());
        return BlockPos.betweenClosedStream((int) area.minX, (int) area.minY, (int) area.minZ, (int) area.maxX - 1, (int) area.maxY - 1, (int) area.maxZ - 1)
                .filter(blockPos -> isBlockPosValid(fakePlayer, blockPos))
                .map(BlockPos::immutable)
                .sorted(Comparator.comparingDouble(x -> x.distSqr(getBlockPos())))
                .collect(Collectors.toList());
    }

    @Override
    public AABB getAABB() {
        return getAABB(getBlockPos());
    }

    @Override
    public boolean isValidEntity(Entity entity) {
        if (!super.isValidEntity(entity))
            return false; //Do the same checks as normal, then check the filters
        return isEntityValidFilter(entity, this.level);
    }
}
