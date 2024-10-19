package com.direwolf20.justdirethings.common.blocks.gooblocks;

import com.direwolf20.justdirethings.setup.Config;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class GooBlock_Item extends BlockItem {
    public GooBlock_Item(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, context, tooltip, flagIn);
        Level level = context.level();
        if (level == null) {
            return;
        }

        tooltip.add(Component.translatable("justdirethings.requiresfeeding")
                .withStyle(ChatFormatting.DARK_RED));
    }

    @Override
    protected BlockState getPlacementState(BlockPlaceContext context) {
        BlockState blockstate = Config.GOO_CAN_DIE.get() ? this.getBlock().getStateForPlacement(context) : this.getBlock().getStateForPlacement(context).setValue(GooBlock_Base.ALIVE, true);
        if (blockstate == null)
            return null;
        return this.canPlace(context, blockstate) ? blockstate : null;
    }
}
