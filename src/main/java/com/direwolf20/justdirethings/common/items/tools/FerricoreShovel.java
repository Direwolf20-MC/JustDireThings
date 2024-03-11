package com.direwolf20.justdirethings.common.items.tools;

import com.direwolf20.justdirethings.common.items.tools.basetools.BaseShovel;
import com.direwolf20.justdirethings.common.items.tools.utils.Ability;
import com.direwolf20.justdirethings.common.items.tools.utils.GooTier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class FerricoreShovel extends BaseShovel {
    public FerricoreShovel() {
        super(GooTier.FERRICORE, 1.5F, -3.0F, new Item.Properties());
        registerAbility(Ability.SKYSWEEPER);
        registerAbility(Ability.LAWNMOWER);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        lawnmower(level, player, hand);
        return super.use(level, player, hand);
    }
}
