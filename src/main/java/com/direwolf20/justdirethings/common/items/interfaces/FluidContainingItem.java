package com.direwolf20.justdirethings.common.items.interfaces;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

public interface FluidContainingItem {
    default int getMaxMB() {
        return 8000;
    }

    static int getAvailableFluid(ItemStack stack) {
        ResourceHandler<FluidResource> fluidHandler = stack.getCapability(Capabilities.Fluid.ITEM, null);
        if (fluidHandler == null) {
            return -1;
        }
        return fluidHandler.getAmountAsInt(0);
    }

    default boolean isFluidBarVisible(ItemStack stack) {
        ResourceHandler<FluidResource> fluidHandler = stack.getCapability(Capabilities.Fluid.ITEM, null);
        if (fluidHandler == null) {
            return false;
        }
        return (fluidHandler.getAmountAsInt(0) < fluidHandler.getCapacityAsInt(0, fluidHandler.getResource(0)));
    }

    default int getFluidBarWidth(ItemStack stack) {
        ResourceHandler<FluidResource> fluidHandler = stack.getCapability(Capabilities.Fluid.ITEM, null);
        if (fluidHandler == null) {
            return 13;
        }
        int cap = fluidHandler.getCapacityAsInt(0, fluidHandler.getResource(0));
        if (cap <= 0) return 13;
        return Math.min(Math.round((float) fluidHandler.getAmountAsInt(0) * 13.0F / (float) cap), 13);
    }

    default int getFluidBarColor(ItemStack stack) {
        ResourceHandler<FluidResource> fluidHandler = stack.getCapability(Capabilities.Fluid.ITEM, null);
        if (fluidHandler == null) {
            return -1; //Tell caller to call super
        }
        float hue = 0.55F;
        float saturation = 1.0F;

        return Mth.hsvToRgb(hue, saturation, 1.0F);
    }

    static boolean hasEnoughFluid(ItemStack itemStack, int amt) {
        ResourceHandler<FluidResource> fluidHandler = itemStack.getCapability(Capabilities.Fluid.ITEM, null);
        if (fluidHandler == null) {
            return false;
        }
        return fluidHandler.getAmountAsInt(0) >= amt;
    }

    static void consumeFluid(ItemStack itemStack, int amt) {
        ResourceHandler<FluidResource> fluidHandler = itemStack.getCapability(Capabilities.Fluid.ITEM, null);
        if (fluidHandler == null) {
            return;
        }
        try (Transaction tx = Transaction.openRoot()) {
            fluidHandler.extract(0, fluidHandler.getResource(0), amt, tx);
            tx.commit();
        }
    }

    static LiquidBlock getLiquidBlockAt(Level level, BlockPos blockPos) {
        if (level.getBlockState(blockPos).getBlock() instanceof LiquidBlock liquidBlock)
            return liquidBlock;
        return null;
    }

    static boolean pickupFluid(Level level, Player player, ItemStack itemStack, BlockHitResult blockhitresult) {
        BlockPos blockpos = blockhitresult.getBlockPos();
        BlockState blockstate1 = level.getBlockState(blockpos);
        LiquidBlock liquidBlock = getLiquidBlockAt(player.level(), blockpos);
        if (liquidBlock == null) return false;
        ResourceHandler<FluidResource> fluidHandler = itemStack.getCapability(Capabilities.Fluid.ITEM, null);
        if (fluidHandler == null) return false;
        FluidResource fluidResource = FluidResource.of(liquidBlock.fluid);
        int filledAmt;
        try (Transaction tx = Transaction.openRoot()) {
            filledAmt = fluidHandler.insert(fluidResource, 1000, tx);
            // simulate — do not commit
        }
        if (filledAmt == 1000) {
            ItemStack itemstack2 = liquidBlock.pickupBlock(player, level, blockpos, blockstate1);
            try (Transaction tx = Transaction.openRoot()) {
                fluidHandler.insert(fluidResource, 1000, tx);
                tx.commit();
            }
            liquidBlock.getPickupSound(blockstate1).ifPresent(p_150709_ -> player.playSound(p_150709_, 1.0F, 1.0F));
            if (!level.isClientSide()) {
                CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer) player, itemstack2);
            }
            return true;
        }
        return false;
    }
}
