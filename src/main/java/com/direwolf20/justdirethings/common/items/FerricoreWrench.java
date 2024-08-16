package com.direwolf20.justdirethings.common.items;

import com.direwolf20.justdirethings.common.blockentities.BlockSwapperT1BE;
import com.direwolf20.justdirethings.common.items.datacomponents.JustDireDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.List;

public class FerricoreWrench extends Item {
    public FerricoreWrench() {
        super(new Properties()
                .stacksTo(1));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        if (player == null) return InteractionResult.FAIL;
        ItemStack itemstack = player.getItemInHand(context.getHand());

        BlockState state = level.getBlockState(pos);
        if (!player.isShiftKeyDown() && specialBlockHandling(level, player, pos, state, itemstack))
            return InteractionResult.SUCCESS;
        if (!level.isClientSide) {
            for (Property<?> prop : state.getProperties()) {
                if (prop instanceof DirectionProperty && prop.getName().equals("facing")) {
                    // Rotate the block using the improved logic.
                    BlockState rotatedState = rotateBlock(state, (DirectionProperty) prop, state.getValue(prop));
                    level.setBlock(pos, rotatedState, 3);
                    level.playSound(null, pos, SoundEvents.ITEM_FRAME_ROTATE_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }

    private boolean specialBlockHandling(Level level, Player player, BlockPos blockPos, BlockState blockState, ItemStack itemStack) {
        if (level.getBlockEntity(blockPos) instanceof BlockSwapperT1BE blockSwapperT1BE) {
            if (level.isClientSide) return false;
            GlobalPos boundPos = getBoundTo(itemStack);
            if (boundPos == null) {
                GlobalPos newBind = GlobalPos.of(level.dimension(), blockPos);
                setBoundTo(itemStack, newBind);
                player.displayClientMessage(Component.translatable("justdirethings.boundto", Component.translatable(newBind.dimension().location().getPath()), "[" + newBind.pos().toShortString() + "]"), true);
                player.playNotifySound(SoundEvents.END_PORTAL_FRAME_FILL, SoundSource.PLAYERS, 1.0F, 1.0F);
            } else {
                boolean bound = blockSwapperT1BE.handleConnection(boundPos);
                if (bound) {
                    player.displayClientMessage(Component.translatable("justdirethings.boundto", Component.translatable(boundPos.dimension().location().getPath()), "[" + boundPos.pos().toShortString() + "]"), true);
                    player.playNotifySound(SoundEvents.END_PORTAL_FRAME_FILL, SoundSource.PLAYERS, 1.0F, 1.0F);
                } else {
                    player.displayClientMessage(Component.translatable("justdirethings.bindremoved"), true);
                    player.playNotifySound(SoundEvents.ENDER_EYE_DEATH, SoundSource.PLAYERS, 1.0F, 1.0F);
                }
                removeBoundTo(itemStack);
            }
            return true;
        }
        return false;
    }

    public boolean isFoil(ItemStack pStack) {
        return getBoundTo(pStack) != null;
    }

    private BlockState rotateBlock(BlockState state, DirectionProperty prop, Comparable<?> currentValue) {
        List<Direction> directions = prop.getPossibleValues().stream().toList();
        int currentDirectionIndex = directions.indexOf(currentValue);
        int nextDirectionIndex = (currentDirectionIndex + 1) % directions.size();
        Direction nextDirection = directions.get(nextDirectionIndex);
        return state.setValue(prop, nextDirection);
    }

    public static GlobalPos getBoundTo(ItemStack stack) {
        return stack.getOrDefault(JustDireDataComponents.BOUND_GLOBAL_POS, null);
    }

    public static void setBoundTo(ItemStack stack, GlobalPos globalPos) {
        stack.set(JustDireDataComponents.BOUND_GLOBAL_POS, globalPos);
    }

    public static void removeBoundTo(ItemStack stack) {
        stack.remove(JustDireDataComponents.BOUND_GLOBAL_POS);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, context, tooltip, flagIn);
        Level level = context.level();
        if (level == null) {
            return;
        }
        GlobalPos boundPos = getBoundTo(stack);
        ChatFormatting chatFormatting = ChatFormatting.DARK_PURPLE;
        if (boundPos != null) {
            tooltip.add(Component.translatable("justdirethings.boundto", I18n.get(boundPos.dimension().location().getPath()), boundPos.pos().toShortString()).withStyle(chatFormatting));
        }
    }
}
