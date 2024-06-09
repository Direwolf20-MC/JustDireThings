package com.direwolf20.justdirethings.common.items;

import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;

import java.util.List;

import static com.direwolf20.justdirethings.util.TooltipHelpers.appendShiftForInfo;

public class PortalFluidCatalyst extends Item {
    public PortalFluidCatalyst() {
        super(new Properties());
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, context, tooltip, flagIn);
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) {
            return;
        }
        if (Screen.hasShiftDown())
            tooltip.add(Component.translatable("justdirethings.hint.dropinwater").withStyle(ChatFormatting.LIGHT_PURPLE));
        else
            appendShiftForInfo(stack, tooltip);
    }

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        if (entity.level().isClientSide) return false;
        BlockState blockState = entity.getInBlockState();
        if (blockState.getBlock() instanceof LiquidBlock liquidBlock && liquidBlock.fluid.isSame(Fluids.WATER) && blockState.getFluidState().isSource()) {
            Level level = entity.level();
            BlockPos blockPos = entity.blockPosition();
            if (convertToUnstableFluid(level, blockPos)) {
                FluidType fluidType = Registration.UNSTABLE_PORTAL_FLUID_TYPE.get();
                FluidStack fluidStack = new FluidStack(Registration.UNSTABLE_PORTAL_FLUID_SOURCE.get(), 1000);
                if (fluidType.isVaporizedOnPlacement(level, blockPos, fluidStack)) {
                    level.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
                    fluidType.onVaporize(null, level, blockPos, fluidStack);
                }
                entity.discard();
                return true;
            }
        }
        return false;
    }

    public boolean convertToUnstableFluid(Level level, BlockPos blockPos) {
        if (level.setBlockAndUpdate(blockPos, Registration.UNSTABLE_PORTAL_FLUID_BLOCK.get().defaultBlockState()))
            return true;
        return false;
    }
}
