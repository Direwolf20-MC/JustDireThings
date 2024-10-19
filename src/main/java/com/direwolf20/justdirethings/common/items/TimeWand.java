package com.direwolf20.justdirethings.common.items;

import com.direwolf20.justdirethings.common.entities.TimeWandEntity;
import com.direwolf20.justdirethings.common.fluids.timefluid.TimeFluidBlock;
import com.direwolf20.justdirethings.common.items.interfaces.BasePoweredItem;
import com.direwolf20.justdirethings.common.items.interfaces.FluidContainingItem;
import com.direwolf20.justdirethings.common.items.interfaces.PoweredItem;
import com.direwolf20.justdirethings.setup.Config;
import com.direwolf20.justdirethings.setup.Registration;
import com.direwolf20.justdirethings.util.MagicHelpers;
import com.direwolf20.justdirethings.util.MiscTools;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

import java.util.List;
import java.util.Optional;

public class TimeWand extends BasePoweredItem implements PoweredItem, FluidContainingItem {
    public TimeWand() {
        super(new Properties()
                .stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (!Config.TIME_WAND_FAKE_PLAYER_ALLOWED.get() && player instanceof FakePlayer)
            return InteractionResultHolder.fail(itemStack);
        BlockHitResult blockhitresult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        if (blockhitresult.getType() == HitResult.Type.BLOCK) {
            if (pickupFluid(level, player, itemStack, blockhitresult))
                return InteractionResultHolder.fail(itemStack);
            if (spawnEntity(level, player, blockhitresult.getBlockPos(), itemStack))
                return InteractionResultHolder.success(itemStack);
        }
        return InteractionResultHolder.fail(itemStack);
    }

    public boolean spawnEntity(Level level, Player player, BlockPos blockPos, ItemStack itemStack) {
        if (level.isClientSide) return false;
        BlockState blockState = level.getBlockState(blockPos);
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (!MiscTools.isValidTickAccelBlock((ServerLevel) level, blockState, blockEntity))
            return false;

        int setRate = 1;
        Optional<TimeWandEntity> existingEntity = level.getEntitiesOfClass(TimeWandEntity.class, new AABB(blockPos)).stream().findFirst();

        if (existingEntity.isPresent()) {
            TimeWandEntity timeWandEntity = existingEntity.get();
            setRate = timeWandEntity.getTickSpeed() + 1;
            if (setRate > Config.logBase2(Config.TIME_WAND_MAX_MULTIPLIER.get()))
                return false;
            float accelRate = TimeWandEntity.calculateAccelRate(setRate);
            int cost = calculateFluidCost(player, (int) accelRate);
            int feCost = calculateFECost(player, (int) accelRate);
            if (hasResources(player, itemStack, feCost, cost)) {
                timeWandEntity.setTickSpeed(setRate);
                int timeExisted = timeWandEntity.getTotalTime() - timeWandEntity.getRemainingTime();
                int moreTime = timeExisted / 2;
                timeWandEntity.addTime(moreTime);
                FluidContainingItem.consumeFluid(itemStack, cost);
                PoweredItem.consumeEnergy(itemStack, feCost);
                playTimeWandSound(level, blockPos, setRate); // Play sound based on the click count
                return true;
            }
        } else {
            float accelRate = TimeWandEntity.calculateAccelRate(setRate);
            int cost = calculateFluidCost(player, (int) accelRate);
            int feCost = calculateFECost(player, (int) accelRate);
            if (hasResources(player, itemStack, feCost, cost)) {
                TimeWandEntity timeWandEntity = new TimeWandEntity(level, blockPos);
                level.addFreshEntity(timeWandEntity);
                FluidContainingItem.consumeFluid(itemStack, cost);
                PoweredItem.consumeEnergy(itemStack, feCost);
                playTimeWandSound(level, blockPos, setRate); // Play sound based on the click count
                return true;
            }
        }
        return false;
    }

    public boolean hasResources(Player player, ItemStack itemStack, int feCost, int fluidCost) {
        float failurePitch = 0.5F; // G# - lower pitch for failure sound
        if (!FluidContainingItem.hasEnoughFluid(itemStack, fluidCost)) {
            player.displayClientMessage(Component.translatable("justdirethings.lowportalfluid"), true);
            player.playNotifySound(SoundEvents.NOTE_BLOCK_IRON_XYLOPHONE.value(), SoundSource.PLAYERS, 1.0F, failurePitch);
            return false;
        }
        if (!PoweredItem.hasEnoughEnergy(itemStack, feCost)) {
            player.displayClientMessage(Component.translatable("justdirethings.lowenergy"), true);
            player.playNotifySound(SoundEvents.NOTE_BLOCK_IRON_XYLOPHONE.value(), SoundSource.PLAYERS, 1.0F, failurePitch);
            return false;
        }
        return true;
    }

    public static int calculateFECost(Player player, int setRate) {
        if (player.isCreative()) return 0;
        return setRate * getFEPerRate();
    }

    public static int calculateFluidCost(Player player, int setRate) {
        if (player.isCreative()) return 0;
        return (int) (setRate * getMBPerRate());
    }

    public static float getMBPerRate() {
        return Config.TIMEWAND_FLUID_COST.get().floatValue();
    }

    public static int getFEPerRate() {
        return Config.TIMEWAND_RF_COST.get();
    }

    public static boolean pickupFluid(Level level, Player player, ItemStack itemStack, BlockHitResult blockhitresult) {
        BlockPos blockpos = blockhitresult.getBlockPos();
        BlockState blockstate1 = level.getBlockState(blockpos);
        if (blockstate1.getBlock() instanceof TimeFluidBlock timeFluidBlock) {
            IFluidHandlerItem fluidHandler = itemStack.getCapability(Capabilities.FluidHandler.ITEM);
            if (fluidHandler == null) return true;
            int filledAmt = fluidHandler.fill(new FluidStack(Registration.TIME_FLUID_SOURCE.get(), 1000), IFluidHandler.FluidAction.SIMULATE);
            if (filledAmt == 1000) {
                ItemStack itemstack2 = timeFluidBlock.pickupBlock(player, level, blockpos, blockstate1);
                fluidHandler.fill(new FluidStack(Registration.TIME_FLUID_SOURCE.get(), 1000), IFluidHandler.FluidAction.EXECUTE);
                timeFluidBlock.getPickupSound(blockstate1).ifPresent(p_150709_ -> player.playSound(p_150709_, 1.0F, 1.0F));
                if (!level.isClientSide) {
                    CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer) player, itemstack2);
                }
            }
            return true;
        }
        return false;
    }

    private void playTimeWandSound(Level serverLevel, BlockPos pos, int setRate) {
        float pitch = switch (setRate) {
            case 1 -> 0.707107F; // C
            case 2 -> 0.793701F; // D
            case 3 -> 0.890899F; // E
            case 4 -> 0.943874F; // F
            case 5 -> 1.059463F; // G2
            case 6 -> 1.189207F; // A2
            case 7 -> 1.334840F; // B2
            case 8 -> 1.414214F; // C2
            case 9 -> 1.587401F; // D2
            case 10 -> 1.781797F; // E2
            case 11 -> 1.887749F; // F2
            default -> 1.0F; // Default sound
        };
        serverLevel.playSound(null, pos, SoundEvents.NOTE_BLOCK_IRON_XYLOPHONE.value(), SoundSource.PLAYERS, 1.0F, pitch);

    }

    @Override
    public int getMaxEnergy() {
        return Config.TIME_WAND_RF_CAPACITY.get();
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.NONE;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, context, tooltip, flagIn);
        Level level = context.level();
        if (level == null) {
            return;
        }
        IFluidHandlerItem fluidHandler = stack.getCapability(Capabilities.FluidHandler.ITEM);
        if (fluidHandler == null) {
            return;
        }
        tooltip.add(Component.translatable("justdirethings.timefluidamt", MagicHelpers.formatted(fluidHandler.getFluidInTank(0).getAmount()), MagicHelpers.formatted(fluidHandler.getTankCapacity(0))).withStyle(ChatFormatting.GREEN));
    }
}
