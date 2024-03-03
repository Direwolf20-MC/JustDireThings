package com.direwolf20.justdirethings.common.items.tools;

import com.direwolf20.justdirethings.client.renderactions.ThingFinder;
import com.direwolf20.justdirethings.common.items.tools.utils.GooTier;
import com.direwolf20.justdirethings.common.items.tools.utils.TieredGooItem;
import com.direwolf20.justdirethings.common.items.tools.utils.ToggleableTool;
import com.direwolf20.justdirethings.common.items.tools.utils.ToolAbility;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.Level;

import java.util.EnumSet;

public class FerricoreSword extends SwordItem implements TieredGooItem, ToggleableTool {
    private final EnumSet<ToolAbility> abilities = EnumSet.noneOf(ToolAbility.class);

    public FerricoreSword() {
        super(GooTier.FERRICORE, 3, -2.0F, new Item.Properties());
        abilities.add(ToolAbility.MOBSCANNER);
    }

    @Override
    public EnumSet<ToolAbility> getAbilities() {
        return abilities;
    }

    @Override
    public GooTier gooTier() {
        return (GooTier) this.getTier();
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (level.isClientSide && !player.isShiftKeyDown()) {
            if (canUseAbility(itemStack, ToolAbility.MOBSCANNER)) {
                if (itemStack.getItem() instanceof TieredGooItem tieredGooItem)
                    ThingFinder.discoverMobs(player, itemStack, tieredGooItem.getGooTier(), true);
            }
        } else { //ServerSide
            if (player.isShiftKeyDown())
                openSettings(player);
        }
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }


}
