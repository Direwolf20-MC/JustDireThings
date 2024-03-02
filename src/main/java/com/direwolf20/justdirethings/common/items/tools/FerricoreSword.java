package com.direwolf20.justdirethings.common.items.tools;

import com.direwolf20.justdirethings.client.renderactions.ThingFinder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.Level;

public class FerricoreSword extends SwordItem implements TieredGooItem {
    public FerricoreSword() {
        super(GooTier.FERRICORE, 3, -2.0F, new Item.Properties());
    }

    @Override
    public GooTier gooTier() {
        return (GooTier) this.getTier();
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide) {
            ItemStack itemStack = player.getItemInHand(hand);
            if (itemStack.getItem() instanceof TieredGooItem tieredGooItem)
                ThingFinder.discoverMobs(player, itemStack, tieredGooItem.getGooTier(), true);
        }
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }
}
