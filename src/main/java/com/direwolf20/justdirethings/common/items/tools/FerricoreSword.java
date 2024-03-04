package com.direwolf20.justdirethings.common.items.tools;

import com.direwolf20.justdirethings.client.renderactions.ThingFinder;
import com.direwolf20.justdirethings.common.items.tools.basetools.BaseSword;
import com.direwolf20.justdirethings.common.items.tools.utils.GooTier;
import com.direwolf20.justdirethings.common.items.tools.utils.TieredGooItem;
import com.direwolf20.justdirethings.common.items.tools.utils.ToolAbility;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class FerricoreSword extends BaseSword {
    public FerricoreSword() {
        super(GooTier.FERRICORE, 3, -2.0F, new Item.Properties());
        registerAbility(ToolAbility.MOBSCANNER);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!player.isShiftKeyDown()) {
            ItemStack itemStack = player.getItemInHand(hand);
            if (level.isClientSide) {
                if (canUseAbility(itemStack, ToolAbility.MOBSCANNER)) {
                    if (itemStack.getItem() instanceof TieredGooItem tieredGooItem) {
                        ThingFinder.discoverMobs(player, itemStack, tieredGooItem.getGooTier(), true);
                    }
                }
            } else { //ServerSide
                if (canUseAbility(itemStack, ToolAbility.MOBSCANNER)) {
                    itemStack.hurtAndBreak(ToolAbility.MOBSCANNER.getDurabilityCost(), player, p_40992_ -> p_40992_.broadcastBreakEvent(EquipmentSlot.MAINHAND));
                }
            }
        }
        return super.use(level, player, hand);
    }


}
