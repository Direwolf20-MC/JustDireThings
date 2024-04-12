package com.direwolf20.justdirethings.common.blockentities;

import com.direwolf20.justdirethings.common.blockentities.basebe.*;
import com.direwolf20.justdirethings.common.capabilities.MachineEnergyStorage;
import com.direwolf20.justdirethings.common.containers.handlers.FilterBasicHandler;
import com.direwolf20.justdirethings.setup.Registration;
import com.direwolf20.justdirethings.util.interfacehelpers.AreaAffectingData;
import com.direwolf20.justdirethings.util.interfacehelpers.FilterData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class BlockSwapperT2BE extends BlockSwapperT1BE implements PoweredMachineBE, AreaAffectingBE, FilterableBE {
    public FilterData filterData = new FilterData();
    public AreaAffectingData areaAffectingData = new AreaAffectingData();
    public final PoweredMachineContainerData poweredMachineData;

    public BlockSwapperT2BE(BlockPos pPos, BlockState pBlockState) {
        super(Registration.BlockSwapperT2BE.get(), pPos, pBlockState);
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
        return 50; // Todo Config?
    }

    public int getEnergyCost(int numBlocks) {
        return numBlocks * getStandardEnergyCost();
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
    public boolean isValidPartner(BlockEntity blockEntity) {
        return blockEntity.getClass() == this.getClass();
    }

    public void setAreaOnly(int x, int y, int z) {
        getAreaAffectingData().xRadius = Math.max(0, Math.min(x, maxRadius));
        getAreaAffectingData().yRadius = Math.max(0, Math.min(y, maxRadius));
        getAreaAffectingData().zRadius = Math.max(0, Math.min(z, maxRadius));
        getAreaAffectingData().area = null;
        if (getBlockEntity() instanceof BaseMachineBE baseMachineBE)
            baseMachineBE.markDirtyClient();
    }

    public void updatePartnerArea() {
        BlockEntity partnerBE = getPartnerBE();
        if (partnerBE instanceof BlockSwapperT2BE blockSwapperT2BE) {
            blockSwapperT2BE.setAreaOnly(getAreaAffectingData().xRadius, getAreaAffectingData().yRadius, getAreaAffectingData().zRadius);
        }
    }

    @Override
    public void addPartnerConnection(GlobalPos connectingPos, BlockSwapperT1BE be) {
        super.addPartnerConnection(connectingPos, be);
        updatePartnerArea();
    }

    @Override
    public void setAreaSettings(int x, int y, int z, int xo, int yo, int zo, boolean renderArea) {
        AreaAffectingBE.super.setAreaSettings(x, y, z, xo, yo, zo, renderArea);
        updatePartnerArea();
    }

    @Override
    public boolean canSwap() {
        AABB aabb = getAABB(getBlockPos());
        int width = (int) Math.abs(Math.floor(aabb.maxX) - Math.floor(aabb.minX));
        int height = (int) Math.abs(Math.floor(aabb.maxY) - Math.floor(aabb.minY));
        int depth = (int) Math.abs(Math.floor(aabb.maxZ) - Math.floor(aabb.minZ));
        return hasEnoughPower(getEnergyCost((width * height * depth)));
    }

    @Override
    public void postSwap(int numBlocks) {
        extractEnergy(getEnergyCost(numBlocks), false);
    }

    public BlockPos getStartingPoint() {
        return getBlockPos().offset(getAreaAffectingData().xOffset, getAreaAffectingData().yOffset, getAreaAffectingData().zOffset);
    }

    public boolean isInBothAreas(BlockPos blockPos) {
        BlockEntity partnerBE = getPartnerBE();
        if (!level.equals(partnerBE.getLevel()))
            return false; //If my level is different than my partners we are not overlapping of course!
        if (partnerBE instanceof BlockSwapperT2BE partnerSwapper) {
            AABB thisAABB = getAABB(getBlockPos());
            AABB thatAABB = partnerSwapper.getAABB(partnerSwapper.getBlockPos());
            return (thisAABB.contains(blockPos.getX(), blockPos.getY(), blockPos.getZ()) && thatAABB.contains(blockPos.getX(), blockPos.getY(), blockPos.getZ()));
        }
        return false;
    }

    public boolean isInBothAreas(Vec3 vec3) {
        BlockEntity partnerBE = getPartnerBE();
        if (!level.equals(partnerBE.getLevel()))
            return false; //If my level is different than my partners we are not overlapping of course!
        if (partnerBE instanceof BlockSwapperT2BE partnerSwapper) {
            AABB thisAABB = getAABB(getBlockPos());
            AABB thatAABB = partnerSwapper.getAABB(partnerSwapper.getBlockPos());
            return (thisAABB.contains(vec3.x(), vec3.y(), vec3.z()) && thatAABB.contains(vec3.x(), vec3.y(), vec3.z()));
        }
        return false;
    }

    @Override
    public boolean isBlockPosValid(ServerLevel serverLevel, BlockPos blockPos) {
        if (!super.isBlockPosValid(serverLevel, blockPos))
            return false; //Do the same checks as normal, then check the filters
        if (isInBothAreas(blockPos))
            return false;
        BlockState blockState = serverLevel.getBlockState(blockPos);
        if (blockState.isAir()) return true; //Don't need to filter AIR either way
        ItemStack blockItemStack = blockState.getBlock().getCloneItemStack(serverLevel, blockPos, blockState);
        return isStackValidFilter(blockItemStack);
    }

    @Override
    public AABB getAABB() {
        return getAABB(getBlockPos());
    }

    @Override
    public List<BlockPos> findSpotsToSwap() {
        AABB area = getAABB(getBlockPos());
        return BlockPos.betweenClosedStream((int) area.minX, (int) area.minY, (int) area.minZ, (int) area.maxX - 1, (int) area.maxY - 1, (int) area.maxZ - 1)
                .filter(blockPos -> isBlockPosValid((ServerLevel) level, blockPos))
                .map(BlockPos::immutable)
                .sorted(Comparator.comparingDouble(x -> x.distSqr(getBlockPos())))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isValidEntity(Entity entity) {
        if (!super.isValidEntity(entity))
            return false;
        if (isInBothAreas(entity.position()))
            return false;
        Item eggItem = SpawnEggItem.byId(entity.getType());
        ItemStack eggItemStack;
        if (eggItem == null)
            eggItemStack = ItemStack.EMPTY;
        else
            eggItemStack = new ItemStack(eggItem);
        return isStackValidFilter(eggItemStack);
    }
}
