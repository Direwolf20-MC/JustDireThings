package com.direwolf20.justdirethings.common.items;

import com.direwolf20.justdirethings.common.entities.PortalProjectile;
import com.direwolf20.justdirethings.common.fluids.portalfluid.PortalFluidBlock;
import com.direwolf20.justdirethings.common.items.datacomponents.JustDireDataComponents;
import com.direwolf20.justdirethings.common.items.interfaces.BasePoweredItem;
import com.direwolf20.justdirethings.common.items.interfaces.FluidContainingItem;
import com.direwolf20.justdirethings.common.items.interfaces.PoweredItem;
import com.direwolf20.justdirethings.setup.Config;
import com.direwolf20.justdirethings.setup.JDTRegistration;
import com.direwolf20.justdirethings.util.MagicHelpers;
import com.direwolf20.justdirethings.util.MiscHelpers;
import com.direwolf20.justdirethings.util.NBTHelpers;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class PortalGunV2 extends BasePoweredItem implements PoweredItem, FluidContainingItem {
    public static final int MAX_FAVORITES = 12;
    public static final int maxMB = 8000;

    public PortalGunV2() {
        super(new Properties()
                .stacksTo(1));
    }

    @Override
    public int getMaxEnergy() {
        return Config.PORTAL_GUN_V2_RF_CAPACITY.get();
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        BlockHitResult blockhitresult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        if (blockhitresult.getType() == HitResult.Type.BLOCK) {
            if (pickupFluid(level, player, itemStack, blockhitresult))
                return InteractionResult.FAIL;
        }
        if (!level.isClientSide()) {
            spawnProjectile(level, player, itemStack, true);
        }
        return InteractionResult.FAIL;
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
        List<Component> buffer = new ArrayList<>();
        buffer.add(Component.translatable("justdirethings.portalfluidamt", MagicHelpers.formatted(fluidHandler.getAmountAsInt(0)), MagicHelpers.formatted(maxMB)).withStyle(ChatFormatting.GREEN));
        buffer.forEach(tooltip);
    }

    public static boolean pickupFluid(Level level, Player player, ItemStack itemStack, BlockHitResult blockhitresult) {
        BlockPos blockpos = blockhitresult.getBlockPos();
        BlockState blockstate1 = level.getBlockState(blockpos);
        if (blockstate1.getBlock() instanceof PortalFluidBlock portalFluidBlock) {
            ResourceHandler<FluidResource> fluidHandler = itemStack.getCapability(Capabilities.Fluid.ITEM, null);
            if (fluidHandler == null) return true;
            FluidResource resource = FluidResource.of(JDTRegistration.PORTAL_FLUID_SOURCE.get());
            int filledAmt;
            try (Transaction probe = Transaction.openRoot()) {
                filledAmt = fluidHandler.insert(0, resource, 1000, probe);
            }
            if (filledAmt == 1000) {
                ItemStack itemstack2 = portalFluidBlock.pickupBlock(player, level, blockpos, blockstate1);
                try (Transaction tx = Transaction.openRoot()) {
                    fluidHandler.insert(0, resource, 1000, tx);
                    tx.commit();
                }
                portalFluidBlock.getPickupSound().ifPresent(p_150709_ -> player.playSound(p_150709_, 1.0F, 1.0F));
                if (!level.isClientSide()) {
                    CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer) player, itemstack2);
                }
            }
            return true;
        }
        return false;
    }

    public static void spawnProjectile(Level level, Player player, ItemStack itemStack, boolean isPrimaryType) {
        NBTHelpers.PortalDestination portalDestination = player.isShiftKeyDown() ? getPrevious(itemStack) : getSelectedFavorite(itemStack);
        if (portalDestination == null || portalDestination.equals(NBTHelpers.PortalDestination.EMPTY)) return;
        int cost = calculateFluidCost((ServerLevel) level, player, portalDestination);
        if (!hasEnoughFluid(itemStack, cost)) {
            player.sendOverlayMessage(Component.translatable("justdirethings.lowportalfluid"));
            player.playSound(SoundEvents.VAULT_INSERT_ITEM_FAIL, 1.0F, 1.0F);
            return;
        }
        if (!PoweredItem.consumeEnergy(itemStack, Config.PORTAL_GUN_V2_RF_COST.get())) {
            player.sendOverlayMessage(Component.translatable("justdirethings.lowenergy"));
            player.playSound(SoundEvents.VAULT_INSERT_ITEM_FAIL, 1.0F, 1.0F);
            return;
        }
        PortalProjectile projectile = new PortalProjectile(level, player, getUUID(itemStack), isPrimaryType, true, portalDestination);
        projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1F, 1.0F);
        level.addFreshEntity(projectile);
        consumeFluid(itemStack, cost);
        setPrevious(player, itemStack);
    }

    public static int calculateFluidCost(ServerLevel sourceLevel, Player player, NBTHelpers.PortalDestination portalDestination) {
        if (player.isCreative()) return 0;
        ServerLevel targetLevel = sourceLevel.getServer().getLevel(portalDestination.globalVec3().dimension());
        if (!targetLevel.equals(sourceLevel)) {
            return 100;
        }
        HitResult result = player.pick(5, 0f, false); //This will get the location the projectile will hit, or close to it probably
        Vec3 targetPosition = portalDestination.globalVec3().position();
        double distance = targetPosition.distanceTo(result.getLocation());
        return Math.min((int) Math.ceil(distance * 0.25), 100);
    }

    public static boolean hasEnoughFluid(ItemStack itemStack, int amt) {
        ResourceHandler<FluidResource> fluidHandler = itemStack.getCapability(Capabilities.Fluid.ITEM, null);
        if (fluidHandler == null) {
            return false;
        }
        return fluidHandler.getAmountAsInt(0) >= amt;
    }

    public static void consumeFluid(ItemStack itemStack, int amt) {
        ResourceHandler<FluidResource> fluidHandler = itemStack.getCapability(Capabilities.Fluid.ITEM, null);
        if (fluidHandler == null) {
            return;
        }
        try (Transaction tx = Transaction.openRoot()) {
            fluidHandler.extract(0, fluidHandler.getResource(0), amt, tx);
            tx.commit();
        }
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return ItemUseAnimation.NONE;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    public static UUID setUUID(ItemStack itemStack) {
        UUID uuid = UUID.randomUUID();
        itemStack.set(JustDireDataComponents.PORTALGUN_UUID, uuid);
        return uuid;
    }

    public static UUID getUUID(ItemStack itemStack) {
        if (!itemStack.has(JustDireDataComponents.PORTALGUN_UUID))
            return setUUID(itemStack);
        return itemStack.get(JustDireDataComponents.PORTALGUN_UUID);
    }

    public static NBTHelpers.PortalDestination getSelectedFavorite(ItemStack itemStack) {
        List<NBTHelpers.PortalDestination> favoritesList = new ArrayList<>(getFavorites(itemStack));
        if (favoritesList.isEmpty()) return null;
        return favoritesList.get(getFavoritePosition(itemStack));
    }

    public static NBTHelpers.PortalDestination getFavorite(ItemStack itemStack, int slot) {
        List<NBTHelpers.PortalDestination> favoritesList = new ArrayList<>(getFavorites(itemStack));
        if (favoritesList.isEmpty()) return null;
        return favoritesList.get(slot);
    }

    public static int getFavoritePosition(ItemStack itemStack) {
        return itemStack.getOrDefault(JustDireDataComponents.PORTALGUN_FAVORITE, 0);
    }

    public static void setFavoritePosition(ItemStack itemStack, int favorite) {
        itemStack.set(JustDireDataComponents.PORTALGUN_FAVORITE, favorite);
    }

    public static List<NBTHelpers.PortalDestination> getFavorites(ItemStack itemStack) {
        return itemStack.getOrDefault(JustDireDataComponents.PORTAL_GUN_FAVORITES, new ArrayList<>());
    }

    public static void setFavorites(ItemStack itemStack, List<NBTHelpers.PortalDestination> favorites) {
        itemStack.set(JustDireDataComponents.PORTAL_GUN_FAVORITES, favorites);
    }

    public static void setPrevious(Player player, ItemStack itemStack) {
        Vec3 position = player.position();
        Direction facing = MiscHelpers.getFacingDirection(player);
        if (facing == Direction.DOWN) facing = Direction.NORTH; //Down is bad
        ResourceKey<Level> dimension = player.level().dimension();
        NBTHelpers.PortalDestination newDestination = new NBTHelpers.PortalDestination(new NBTHelpers.GlobalVec3(dimension, position), facing, "previous");
        itemStack.set(JustDireDataComponents.PORTAL_GUN_PREVIOUS, newDestination);
    }

    public static NBTHelpers.PortalDestination getPrevious(ItemStack itemStack) {
        return itemStack.getOrDefault(JustDireDataComponents.PORTAL_GUN_PREVIOUS, NBTHelpers.PortalDestination.EMPTY);
    }

    public static void addFavorite(ItemStack itemStack, int position, NBTHelpers.PortalDestination portalDestination) {
        if (!itemStack.has(JustDireDataComponents.PORTAL_GUN_FAVORITES)) {
            List<NBTHelpers.PortalDestination> list = new ArrayList<>(MAX_FAVORITES);
            for (int i = 0; i < MAX_FAVORITES; i++) {
                list.add(NBTHelpers.PortalDestination.EMPTY); //Prefill List
            }
            setFavorites(itemStack, list);
        }
        List<NBTHelpers.PortalDestination> favoritesList = new ArrayList<>(getFavorites(itemStack));
        favoritesList.set(position, portalDestination);
        setFavorites(itemStack, favoritesList);
    }

    public static void removeFavorite(ItemStack itemStack, int position) {
        List<NBTHelpers.PortalDestination> favoritesList = new ArrayList<>(getFavorites(itemStack));
        if (favoritesList.isEmpty()) return;
        favoritesList.set(position, NBTHelpers.PortalDestination.EMPTY);
        setFavorites(itemStack, favoritesList);
    }

    public static boolean getStayOpen(ItemStack itemStack) {
        return itemStack.getOrDefault(JustDireDataComponents.PORTAL_GUN_STAY_OPEN, false);
    }

    public static void setStayOpen(ItemStack itemStack, boolean stayOpen) {
        itemStack.set(JustDireDataComponents.PORTAL_GUN_STAY_OPEN, stayOpen);
    }

    public static ItemStack getPortalGunv2(Player player) {
        ItemStack mainHand = player.getMainHandItem();
        if (mainHand.getItem() instanceof PortalGunV2)
            return mainHand;
        ItemStack offHand = player.getOffhandItem();
        if (offHand.getItem() instanceof PortalGunV2)
            return offHand;
        return ItemStack.EMPTY;
    }

    public static int getFullness(ItemStack itemStack) {
        ResourceHandler<FluidResource> fluidHandler = itemStack.getCapability(Capabilities.Fluid.ITEM, null);
        if (fluidHandler != null && !fluidHandler.getResource(0).isEmpty()) {
            float percentFull = ((float) fluidHandler.getAmountAsInt(0) / maxMB) * 100;
            if (percentFull > 0 && percentFull <= 33) {
                return 1;
            } else if (percentFull > 33 && percentFull <= 66) {
                return 2;
            } else if (percentFull > 66) {
                return 3;
            }
        }
        return 0;
    }
}
