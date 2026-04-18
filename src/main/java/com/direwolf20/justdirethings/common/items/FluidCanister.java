package com.direwolf20.justdirethings.common.items;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.common.items.datacomponents.JustDireDataComponents;
import com.direwolf20.justdirethings.common.items.interfaces.FluidContainingItem;
import com.direwolf20.justdirethings.util.MagicHelpers;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
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
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class FluidCanister extends Item implements FluidContainingItem {
    public enum FillMode {
        NONE("none"),
        JDTONLY("jdtonly"),
        ALL("all");

        private final String baseName;

        FillMode(String baseName) {
            this.baseName = baseName;
        }

        public Component getTooltip() {
            return Component.translatable(JustDireThings.MODID + ".fillmode." + baseName);
        }

        public FillMode next() {
            FillMode[] values = values();
            int nextOrdinal = (this.ordinal() + 1) % values.length;
            return values[nextOrdinal];
        }
    }

    public FluidCanister(Properties pProperties) {
        super(pProperties);
    }

    public int getMaxMB() {
        return 8000;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        BlockHitResult blockhitresult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        if (blockhitresult.getType() == HitResult.Type.BLOCK) {
            if (player.isShiftKeyDown()) {
                if (placeFluid(level, player, itemStack, blockhitresult))
                    return InteractionResult.SUCCESS.heldItemTransformedTo(itemStack);
            } else {
                if (pickupFluid(level, player, itemStack, blockhitresult))
                    return InteractionResult.SUCCESS.heldItemTransformedTo(itemStack);
                else {
                    if (placeFluid(level, player, itemStack, blockhitresult))
                        return InteractionResult.SUCCESS.heldItemTransformedTo(itemStack);
                }
            }
        } else {
            if (player.isShiftKeyDown()) {
                nextFillMode(itemStack);
                player.sendOverlayMessage(Component.translatable("justdirethings.fillmode.changed", getFillMode(itemStack).getTooltip()));
            }
        }
        return InteractionResult.FAIL;
    }

    @Override
    public void inventoryTick(@NotNull ItemStack itemStack, @NotNull ServerLevel world, @NotNull Entity entity, @Nullable EquipmentSlot slot) {
        FillMode fillMode = getFillMode(itemStack);
        if (fillMode == FillMode.NONE) return;
        if (entity instanceof Player player) {
            ResourceHandler<FluidResource> fluidHandler = itemStack.getCapability(Capabilities.Fluid.ITEM, null);
            if (fluidHandler == null) return;
            FluidResource stored = fluidHandler.getResource(0);
            if (stored.isEmpty()) return;
            int storedAmt = fluidHandler.getAmountAsInt(0);
            if (storedAmt <= 0) return;
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack slotStack = player.getInventory().getItem(i);
                if (slotStack.getItem() instanceof FluidCanister) continue;
                if (fillMode == FillMode.JDTONLY
                        && !slotStack.getItem().getCreatorModId(world.registryAccess(), slotStack).equals(JustDireThings.MODID))
                    continue;
                ResourceHandler<FluidResource> slotFluidHandler = slotStack.getCapability(Capabilities.Fluid.ITEM, null);
                if (slotFluidHandler == null) continue;
                int amtToFill = Math.min(storedAmt, 100);
                if (amtToFill <= 0) continue;
                int accepted;
                try (Transaction probe = Transaction.openRoot()) {
                    accepted = slotFluidHandler.insert(stored, amtToFill, probe);
                }
                if (accepted <= 0) continue;
                try (Transaction tx = Transaction.openRoot()) {
                    int extracted = fluidHandler.extract(0, stored, accepted, tx);
                    if (extracted <= 0) {
                        continue;
                    }
                    int inserted = slotFluidHandler.insert(stored, extracted, tx);
                    if (inserted != extracted) {
                        // Roll back if the receiver didn't take it all
                        continue;
                    }
                    tx.commit();
                    storedAmt -= inserted;
                    if (storedAmt <= 0) return;
                }
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, context, display, tooltip, flagIn);
        Level level = context.level();
        if (level == null) {
            return;
        }

        ResourceHandler<FluidResource> fluidHandler = stack.getCapability(Capabilities.Fluid.ITEM, null);
        if (fluidHandler == null) {
            return;
        }
        FluidResource fluidResource = fluidHandler.getResource(0);
        int amount = fluidHandler.getAmountAsInt(0);
        int fluidColor = getFluidColor(stack);

        // Creating a Style with the fluid color
        Style fluidStyle = Style.EMPTY.withColor(TextColor.fromRgb(fluidColor));

        FluidStack displayStack = fluidResource.toStack(Math.max(amount, 1));
        List<Component> buffer = new ArrayList<>();
        buffer.add(Component.translatable("justdirethings.fluidname").withStyle(ChatFormatting.GRAY).append(Component.translatable(displayStack.getHoverName().getString()).withStyle(fluidStyle)));
        buffer.add(Component.translatable("justdirethings.fluidamt").withStyle(ChatFormatting.GRAY).append(Component.literal(MagicHelpers.formatted(amount) + "/" + MagicHelpers.formatted(getMaxMB())).withStyle(ChatFormatting.GREEN)));
        buffer.add(Component.translatable("justdirethings.fillmode").withStyle(ChatFormatting.GRAY).append(Component.translatable(getFillMode(stack).getTooltip().getString()).withStyle(ChatFormatting.GREEN)));
        buffer.forEach(tooltip);
    }


    public boolean placeFluid(Level level, Player player, ItemStack itemStack, BlockHitResult blockhitresult) {
        BlockPos blockpos = blockhitresult.getBlockPos();
        BlockState blockstate = level.getBlockState(blockpos);
        if (blockstate.getBlock() instanceof LiquidBlock && !player.isShiftKeyDown()) return false;
        ResourceHandler<FluidResource> fluidHandler = itemStack.getCapability(Capabilities.Fluid.ITEM, null);
        if (fluidHandler == null) return false;
        FluidResource fluidResource = fluidHandler.getResource(0);
        int amount = fluidHandler.getAmountAsInt(0);
        if (fluidResource.isEmpty() || amount < 1000) return false;
        Direction direction = blockhitresult.getDirection();
        BlockPos blockpos1 = blockpos.relative(direction);
        BlockPos blockpos2 = canBlockContainFluid(player, level, blockpos, blockstate, fluidResource.getFluid()) ? blockpos : blockpos1;
        if (this.emptyContents(player, level, blockpos2, itemStack)) {
            try (Transaction tx = Transaction.openRoot()) {
                fluidHandler.extract(0, fluidResource, 1000, tx);
                tx.commit();
            }
            return true;
        }

        return false;
    }

    public boolean pickupFluid(Level level, Player player, ItemStack itemStack, BlockHitResult blockhitresult) {
        BlockPos blockpos = blockhitresult.getBlockPos();
        BlockState blockstate1 = level.getBlockState(blockpos);
        if (blockstate1.getBlock() instanceof LiquidBlock liquidBlock) {
            ResourceHandler<FluidResource> fluidHandler = itemStack.getCapability(Capabilities.Fluid.ITEM, null);
            if (fluidHandler == null) return false;
            Fluid fluid = liquidBlock.fluid;
            FluidResource stored = fluidHandler.getResource(0);
            if (!liquidBlock.fluid.isSource(liquidBlock.fluid.getSource(false)) || (!stored.isEmpty() && stored.getFluid() != fluid))
                return false;

            FluidResource resource = FluidResource.of(fluid);
            int filledAmt;
            try (Transaction probe = Transaction.openRoot()) {
                filledAmt = fluidHandler.insert(0, resource, 1000, probe);
            }
            if (filledAmt == 1000) {
                ItemStack itemstack2 = liquidBlock.pickupBlock(player, level, blockpos, blockstate1);
                try (Transaction tx = Transaction.openRoot()) {
                    fluidHandler.insert(0, resource, 1000, tx);
                    tx.commit();
                }
                liquidBlock.getPickupSound().ifPresent(p_150709_ -> player.playSound(p_150709_, 1.0F, 1.0F));
                if (!level.isClientSide()) {
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
        FluidStack fluidStack = new FluidStack(fluid, 1000);
        if (fluid.getFluidType().isVaporizedOnPlacement(level, pos, fluidStack)) {
            fluid.getFluidType().onVaporize(player, level, pos, fluidStack);
            return true;
        }

        if (!level.isClientSide() && canBeReplaced && !blockState.liquid()) {
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
        level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + (level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.8F);

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
                return 0xFFFF4500;
            net.neoforged.neoforge.fluids.FluidStack stack = new net.neoforged.neoforge.fluids.FluidStack(fluidData.getFluid(), fluidData.getAmount());
            net.minecraft.client.renderer.block.FluidModel model =
                    net.minecraft.client.Minecraft.getInstance().getModelManager().getFluidStateModelSet().get(stack.getFluid().defaultFluidState());
            net.neoforged.neoforge.client.fluid.FluidTintSource tintSource = model.fluidTintSource();
            int tint = tintSource != null ? tintSource.colorAsStack(stack) : 0xFFFFFFFF;
            return tint | 0xFF000000;
        }
        return 0xFFFFFFFF;
    }

    public static FillMode getFillMode(ItemStack itemStack) {
        return FillMode.values()[itemStack.getOrDefault(JustDireDataComponents.FLUID_CANISTER_MODE, 0)];
    }

    public static void nextFillMode(ItemStack itemStack) {
        itemStack.set(JustDireDataComponents.FLUID_CANISTER_MODE, getFillMode(itemStack).next().ordinal());
    }
}
