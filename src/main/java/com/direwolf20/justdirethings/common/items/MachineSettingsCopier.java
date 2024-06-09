package com.direwolf20.justdirethings.common.items;

import com.direwolf20.justdirethings.client.screens.ModScreens;
import com.direwolf20.justdirethings.common.blockentities.basebe.AreaAffectingBE;
import com.direwolf20.justdirethings.common.blockentities.basebe.BaseMachineBE;
import com.direwolf20.justdirethings.common.blockentities.basebe.FilterableBE;
import com.direwolf20.justdirethings.common.blockentities.basebe.RedstoneControlledBE;
import com.direwolf20.justdirethings.common.containers.handlers.FilterBasicHandler;
import com.direwolf20.justdirethings.common.items.datacomponents.JustDireDataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class MachineSettingsCopier extends Item {
    public MachineSettingsCopier() {
        super(new Properties()
                .stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (!level.isClientSide() || !player.isShiftKeyDown())
            return new InteractionResultHolder<>(InteractionResult.PASS, itemstack);

        if (level.isClientSide)
            ModScreens.openMachineSettingsCopierScreen(itemstack);

        return new InteractionResultHolder<>(InteractionResult.PASS, itemstack);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Level level = pContext.getLevel();
        if (level.isClientSide) return InteractionResult.SUCCESS;
        BlockEntity blockEntity = level.getBlockEntity(pContext.getClickedPos());

        if (!(blockEntity instanceof BaseMachineBE)) return InteractionResult.PASS;
        ItemStack itemStack = pContext.getItemInHand();

        Player player = pContext.getPlayer();

        if (player.isShiftKeyDown()) { //Copy
            saveSettings(level, blockEntity, itemStack);
            player.displayClientMessage(Component.translatable("justdirethings.settingscopied"), true);
            player.playNotifySound(SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, SoundSource.PLAYERS, 1.0F, 1.0F);
        } else { //Paste
            loadSettings(level, blockEntity, itemStack);
            player.displayClientMessage(Component.translatable("justdirethings.settingspasted"), true);
            player.playNotifySound(SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
        }

        return InteractionResult.SUCCESS;
    }

    public void loadSettings(Level level, BlockEntity blockEntity, ItemStack itemStack) {
        if (!itemStack.has(JustDireDataComponents.COPIED_MACHINE_DATA)) return;
        CompoundTag compoundTag = itemStack.get(JustDireDataComponents.COPIED_MACHINE_DATA).copyTag();
        if (compoundTag.isEmpty()) return;

        if (blockEntity instanceof AreaAffectingBE areaAffectingBE) {
            if (getCopyArea(itemStack))
                areaAffectingBE.loadAreaOnly(compoundTag);
            if (getCopyOffset(itemStack))
                areaAffectingBE.loadOffsetOnly(compoundTag);
        }

        if (getCopyFilter(itemStack) && blockEntity instanceof FilterableBE filterableBE) {
            filterableBE.loadFilterSettings(compoundTag);
            if (compoundTag.contains("filteredItems")) {
                CompoundTag filteredItems = compoundTag.getCompound("filteredItems");
                FilterBasicHandler filterBasicHandler = filterableBE.getFilterHandler();
                filterBasicHandler.deserializeNBT(level.registryAccess(), filteredItems);
            }
        }

        if (getCopyRedstone(itemStack) && blockEntity instanceof RedstoneControlledBE redstoneControlledBE)
            redstoneControlledBE.loadRedstoneSettings(compoundTag);

        ((BaseMachineBE) blockEntity).markDirtyClient();
    }

    public void saveSettings(Level level, BlockEntity blockEntity, ItemStack itemStack) {
        CompoundTag compoundTag = new CompoundTag();
        if (blockEntity instanceof AreaAffectingBE areaAffectingBE) {
            if (getCopyArea(itemStack))
                areaAffectingBE.saveAreaOnly(compoundTag);
            if (getCopyOffset(itemStack))
                areaAffectingBE.saveOffsetOnly(compoundTag);
        }

        if (getCopyFilter(itemStack) && blockEntity instanceof FilterableBE filterableBE) {
            filterableBE.saveFilterSettings(compoundTag);
            FilterBasicHandler filterBasicHandler = filterableBE.getFilterHandler();
            compoundTag.put("filteredItems", filterBasicHandler.serializeNBT(level.registryAccess()));
        }

        if (getCopyRedstone(itemStack) && blockEntity instanceof RedstoneControlledBE redstoneControlledBE)
            redstoneControlledBE.saveRedstoneSettings(compoundTag);

        if (!compoundTag.isEmpty())
            itemStack.set(JustDireDataComponents.COPIED_MACHINE_DATA, CustomData.of(compoundTag));
    }

    public static void setSettings(ItemStack itemStack, boolean area, boolean offset, boolean filter, boolean redstone) {
        itemStack.set(JustDireDataComponents.COPY_AREA_SETTINGS, area);
        itemStack.set(JustDireDataComponents.COPY_OFFSET_SETTINGS, offset);
        itemStack.set(JustDireDataComponents.COPY_FILTER_SETTINGS, filter);
        itemStack.set(JustDireDataComponents.COPY_REDSTONE_SETTINGS, redstone);
    }

    public static boolean getCopyArea(ItemStack itemStack) {
        return itemStack.getOrDefault(JustDireDataComponents.COPY_AREA_SETTINGS, true);
    }

    public static boolean getCopyOffset(ItemStack itemStack) {
        return itemStack.getOrDefault(JustDireDataComponents.COPY_OFFSET_SETTINGS, true);
    }

    public static boolean getCopyFilter(ItemStack itemStack) {
        return itemStack.getOrDefault(JustDireDataComponents.COPY_FILTER_SETTINGS, true);
    }

    public static boolean getCopyRedstone(ItemStack itemStack) {
        return itemStack.getOrDefault(JustDireDataComponents.COPY_REDSTONE_SETTINGS, true);
    }
}
