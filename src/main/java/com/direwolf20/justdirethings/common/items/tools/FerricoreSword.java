package com.direwolf20.justdirethings.common.items.tools;

import com.direwolf20.justdirethings.client.renderactions.ThingFinder;
import com.direwolf20.justdirethings.common.items.tools.basetools.BaseSword;
import com.direwolf20.justdirethings.common.items.tools.utils.GooTier;
import com.direwolf20.justdirethings.common.items.tools.utils.TieredGooItem;
import com.direwolf20.justdirethings.common.items.tools.utils.ToolAbility;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class FerricoreSword extends BaseSword {
    public FerricoreSword() {
        super(GooTier.FERRICORE, 3, -2.0F, new Item.Properties());
        abilities.add(ToolAbility.MOBSCANNER);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide && !player.isShiftKeyDown()) {
            ItemStack itemStack = player.getItemInHand(hand);
            if (canUseAbility(itemStack, ToolAbility.MOBSCANNER)) {
                if (itemStack.getItem() instanceof TieredGooItem tieredGooItem)
                    ThingFinder.discoverMobs(player, itemStack, tieredGooItem.getGooTier(), true);
            }
        }
        return super.use(level, player, hand);
    }


}
