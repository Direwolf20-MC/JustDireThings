package com.direwolf20.justdirethings.common.items.interfaces;

import com.direwolf20.justdirethings.common.fluids.portalfluid.PortalFluidBlock;
import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

public interface FluidContainingItem {
    default int getMaxMB() {
        return 8000;
    }

    static int getAvailableFluid(ItemStack stack) {
        IFluidHandlerItem fluidHandler = stack.getCapability(Capabilities.FluidHandler.ITEM);
        if (fluidHandler == null) {
            return -1;
        }
        return fluidHandler.getFluidInTank(0).getAmount();
    }

    default boolean isFluidBarVisible(ItemStack stack) {
        IFluidHandlerItem fluidHandler = stack.getCapability(Capabilities.FluidHandler.ITEM);
        if (fluidHandler == null) {
            return false;
        }
        return (fluidHandler.getFluidInTank(0).getAmount() < fluidHandler.getTankCapacity(0));
    }

    default int getFluidBarWidth(ItemStack stack) {
        IFluidHandlerItem fluidHandler = stack.getCapability(Capabilities.FluidHandler.ITEM);
        if (fluidHandler == null) {
            return 13;
        }
        return Math.min(Math.round((float) fluidHandler.getFluidInTank(0).getAmount() * 13.0F / (float) fluidHandler.getTankCapacity(0)), 13);
        //return Math.min(13 * fluidHandler.getFluidInTank(0).getAmount() / fluidHandler.getTankCapacity(0), 13);
    }

    default int getFluidBarColor(ItemStack stack) {
        IFluidHandlerItem fluidHandler = stack.getCapability(Capabilities.FluidHandler.ITEM);
        if (fluidHandler == null) {
            return -1; //Tell caller to call super
        }
        float hue = 0.55F; // Starting from a deeper blue, moving slightly darker as it fills
        float saturation = 1.0F; // Slightly reduce saturation as it fills

        return Mth.hsvToRgb(hue, saturation, 1.0F);
    }

    static boolean hasEnoughFluid(ItemStack itemStack, int amt) {
        IFluidHandlerItem fluidHandler = itemStack.getCapability(Capabilities.FluidHandler.ITEM);
        if (fluidHandler == null) {
            return false;
        }
        return fluidHandler.getFluidInTank(0).getAmount() >= amt;
    }

    static void consumeFluid(ItemStack itemStack, int amt) {
        IFluidHandlerItem fluidHandler = itemStack.getCapability(Capabilities.FluidHandler.ITEM);
        if (fluidHandler == null) {
            return;
        }
        fluidHandler.drain(amt, IFluidHandler.FluidAction.EXECUTE);
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
        IFluidHandlerItem fluidHandler = itemStack.getCapability(Capabilities.FluidHandler.ITEM);
        if (fluidHandler == null) return false;
        int filledAmt = fluidHandler.fill(new FluidStack(liquidBlock.fluid, 1000), IFluidHandler.FluidAction.SIMULATE);
        if (filledAmt == 1000) {
            ItemStack itemstack2 = liquidBlock.pickupBlock(player, level, blockpos, blockstate1);
            fluidHandler.fill(new FluidStack(liquidBlock.fluid, 1000), IFluidHandler.FluidAction.EXECUTE);
            liquidBlock.getPickupSound(blockstate1).ifPresent(p_150709_ -> player.playSound(p_150709_, 1.0F, 1.0F));
            if (!level.isClientSide) {
                CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer) player, itemstack2);
            }
            return true;
        }
        return false;
    }
}
