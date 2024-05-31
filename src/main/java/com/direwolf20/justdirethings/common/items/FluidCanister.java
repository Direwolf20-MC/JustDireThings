package com.direwolf20.justdirethings.common.items;

import com.direwolf20.justdirethings.common.items.datacomponents.JustDireDataComponents;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

import javax.annotation.Nullable;

public class FluidCanister extends Item {
    public final static int maxMB = 8000;

    public FluidCanister() {
        super(new Properties()
                .stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        BlockHitResult blockhitresult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        if (blockhitresult.getType() == HitResult.Type.BLOCK) {
            if (player.isShiftKeyDown()) {
                if (placeFluid(level, player, itemStack, blockhitresult))
                    return InteractionResultHolder.fail(itemStack);
            } else {
                if (pickupFluid(level, player, itemStack, blockhitresult))
                    return InteractionResultHolder.fail(itemStack);
            }
        }
        return InteractionResultHolder.fail(itemStack);
    }


    public boolean placeFluid(Level level, Player player, ItemStack itemStack, BlockHitResult blockhitresult) {
        BlockPos blockpos = blockhitresult.getBlockPos();
        BlockState blockstate = level.getBlockState(blockpos);
        IFluidHandlerItem fluidHandler = itemStack.getCapability(Capabilities.FluidHandler.ITEM);
        if (fluidHandler == null) return false;
        FluidStack fluidStack = fluidHandler.getFluidInTank(0);
        if (fluidStack.isEmpty() || fluidStack.getAmount() < 1000) return false;
        Direction direction = blockhitresult.getDirection();
        BlockPos blockpos1 = blockpos.relative(direction);
        BlockPos blockpos2 = canBlockContainFluid(player, level, blockpos, blockstate, fluidStack.getFluid()) ? blockpos : blockpos1;
        if (this.emptyContents(player, level, blockpos2, itemStack)) {
            fluidHandler.drain(1000, IFluidHandler.FluidAction.EXECUTE);
            return true;
        }

        return false;
    }

    public boolean pickupFluid(Level level, Player player, ItemStack itemStack, BlockHitResult blockhitresult) {
        BlockPos blockpos = blockhitresult.getBlockPos();
        BlockState blockstate1 = level.getBlockState(blockpos);
        if (blockstate1.getBlock() instanceof LiquidBlock liquidBlock) {
            IFluidHandlerItem fluidHandler = itemStack.getCapability(Capabilities.FluidHandler.ITEM);
            if (fluidHandler == null) return false;
            Fluid fluid = liquidBlock.fluid;
            FluidStack fluidStack = fluidHandler.getFluidInTank(0);
            if (!liquidBlock.fluid.isSource(liquidBlock.fluid.getSource(false)) || (!fluidStack.isEmpty() && !fluidStack.is(fluid)))
                return false;

            int filledAmt = fluidHandler.fill(new FluidStack(fluid, 1000), IFluidHandler.FluidAction.SIMULATE);
            if (filledAmt == 1000) {
                ItemStack itemstack2 = liquidBlock.pickupBlock(player, level, blockpos, blockstate1);
                fluidHandler.fill(new FluidStack(fluid, 1000), IFluidHandler.FluidAction.EXECUTE);
                liquidBlock.getPickupSound(blockstate1).ifPresent(p_150709_ -> player.playSound(p_150709_, 1.0F, 1.0F));
                if (!level.isClientSide) {
                    CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer) player, itemstack2);
                }
            }
            return true;
        }
        return false;
    }

    public boolean emptyContents(@Nullable Player player, Level level, BlockPos pos, @Nullable ItemStack container) {
        SimpleFluidContent fluidData = getFluidData(container);
        if (fluidData == null || fluidData.isEmpty()) return false;

        Fluid fluid = fluidData.getFluid();
        if (!(fluid instanceof FlowingFluid flowingFluid)) return false;

        BlockState blockState = level.getBlockState(pos);
        Block block = blockState.getBlock();
        boolean canBeReplaced = blockState.canBeReplaced(fluid);

        if (!blockState.isAir() && !canBeReplaced) {
            if (block instanceof LiquidBlockContainer liquidBlockContainer && liquidBlockContainer.canPlaceLiquid(player, level, pos, blockState, fluid)) {
                liquidBlockContainer.placeLiquid(level, pos, blockState, flowingFluid.getSource(false));
                playEmptySound(player, level, pos, fluid);
                return true;
            }
            return false;
        }

        if (level.dimensionType().ultraWarm() && fluid.is(FluidTags.WATER)) {
            playExtinguishSound(level, pos);
            return true;
        }

        if (!level.isClientSide && canBeReplaced && !blockState.liquid()) {
            level.destroyBlock(pos, true);
        }

        if (level.setBlock(pos, fluid.defaultFluidState().createLegacyBlock(), 11) || blockState.getFluidState().isSource()) {
            playEmptySound(player, level, pos, fluid);
            return true;
        }
        return false;
    }

    protected void playEmptySound(@Nullable Player pPlayer, LevelAccessor pLevel, BlockPos pPos, Fluid fluid) {
        SoundEvent soundevent = fluid.getFluidType().getSound(pPlayer, pLevel, pPos, net.neoforged.neoforge.common.SoundActions.BUCKET_EMPTY);
        if (soundevent == null)
            soundevent = fluid.is(FluidTags.LAVA) ? SoundEvents.BUCKET_EMPTY_LAVA : SoundEvents.BUCKET_EMPTY;
        pLevel.playSound(pPlayer, pPos, soundevent, SoundSource.BLOCKS, 1.0F, 1.0F);
        pLevel.gameEvent(pPlayer, GameEvent.FLUID_PLACE, pPos);
    }

    protected void playExtinguishSound(Level level, BlockPos pos) {
        level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + (level.random.nextFloat() - level.random.nextFloat()) * 0.8F);

        for (int i = 0; i < 8; i++) {
            level.addParticle(ParticleTypes.LARGE_SMOKE, pos.getX() + Math.random(), pos.getY() + Math.random(), pos.getZ() + Math.random(), 0.0, 0.0, 0.0);
        }
    }

    protected boolean canBlockContainFluid(@Nullable Player player, Level worldIn, BlockPos posIn, BlockState blockstate, Fluid fluid) {
        return blockstate.getBlock() instanceof LiquidBlockContainer && ((LiquidBlockContainer) blockstate.getBlock()).canPlaceLiquid(player, worldIn, posIn, blockstate, fluid);
    }

    public static SimpleFluidContent getFluidData(ItemStack itemStack) {
        return itemStack.get(JustDireDataComponents.FLUID_CONTAINER);
    }

    public static Fluid getFluid(ItemStack itemStack) {
        SimpleFluidContent fluidData = getFluidData(itemStack);
        if (fluidData != null && !fluidData.isEmpty()) {
            return fluidData.getFluid();
        }
        return null;
    }

    public static int getFullness(ItemStack itemStack) {
        SimpleFluidContent fluidData = getFluidData(itemStack);
        if (fluidData != null && !fluidData.isEmpty()) {
            int mb = fluidData.getAmount();
            return (int) Math.ceil((double) mb / 1000);
        }
        return 0;
    }

    public static int getFluidColor(ItemStack itemStack) {
        SimpleFluidContent fluidData = getFluidData(itemStack);
        if (fluidData != null && !fluidData.isEmpty()) {
            if (fluidData.getFluid().isSame(Fluids.LAVA)) //Special case lava
                return 0xFFDB6B19;
            return IClientFluidTypeExtensions.of(fluidData.getFluidType()).getTintColor(fluidData.copy());
        }
        return 0xFFFFFFFF;
    }
}
