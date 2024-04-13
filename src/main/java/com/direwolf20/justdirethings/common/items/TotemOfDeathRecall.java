package com.direwolf20.justdirethings.common.items;

import com.direwolf20.justdirethings.util.NBTHelpers;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

import java.util.HashSet;
import java.util.List;

public class TotemOfDeathRecall extends Item {
    public TotemOfDeathRecall() {
        super(new Properties()
                .stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (getBoundTo(itemStack) == null) return new InteractionResultHolder<>(InteractionResult.PASS, itemStack);
        player.startUsingItem(hand);
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, player.getItemInHand(hand));
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BOW;
    }

    @Override
    public void releaseUsing(ItemStack stack, Level world, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof Player player) {
            int usedDuration = this.getUseDuration(stack) - timeLeft;
            if (usedDuration >= 20) {  // 60 ticks = 3 seconds
                // Retrieve death location from NBT and teleport
                if (!world.isClientSide) {
                    CompoundTag tag = stack.getTag();
                    if (tag != null) {
                        GlobalPos globalPos = getBoundTo(stack);
                        BlockPos blockPos = globalPos.pos();
                        ServerLevel targetLevel = world.getServer().getLevel(globalPos.dimension());
                        if (targetLevel != null) {
                            player.teleportTo(targetLevel, blockPos.getX(), blockPos.getY(), blockPos.getZ(), new HashSet<>(), player.getYRot(), player.getXRot());
                            stack.shrink(1);
                            world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_BREAK, SoundSource.PLAYERS, 1.0F, 1.0F);
                        }
                    }
                }
            }
        }
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;  // Max duration (arbitrary large number)
    }

    public boolean isFoil(ItemStack pStack) {
        return getBoundTo(pStack) != null;
    }

    public static GlobalPos getBoundTo(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null && tag.contains("boundTo") ? NBTHelpers.nbtToGlobalPos(tag.getCompound("boundTo")) : null;
    }

    public static void setBoundTo(ItemStack stack, GlobalPos globalPos) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.put("boundTo", NBTHelpers.globalPosToNBT(globalPos));
    }

    @Override
    public void appendHoverText(ItemStack stack, @javax.annotation.Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, level, tooltip, flagIn);
        Minecraft mc = Minecraft.getInstance();
        if (level == null || mc.player == null) {
            return;
        }
        GlobalPos boundPos = getBoundTo(stack);
        ChatFormatting chatFormatting = ChatFormatting.DARK_PURPLE;
        if (boundPos != null) {
            tooltip.add(Component.translatable("justdirethings.boundto", I18n.get(boundPos.dimension().location().getPath()), boundPos.pos().toShortString()).withStyle(chatFormatting));
        }
    }
}
