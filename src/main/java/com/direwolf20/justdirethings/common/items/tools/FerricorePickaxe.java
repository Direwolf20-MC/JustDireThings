package com.direwolf20.justdirethings.common.items.tools;

import com.direwolf20.justdirethings.common.items.tools.basetools.BasePickaxe;
import com.direwolf20.justdirethings.common.items.tools.utils.Ability;
import com.direwolf20.justdirethings.common.items.tools.utils.GooTier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class FerricorePickaxe extends BasePickaxe {
    public FerricorePickaxe() {
        super(GooTier.FERRICORE, 1, -2.8F, new Item.Properties());
        registerAbility(Ability.ORESCANNER);
        registerAbility(Ability.OREMINER);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        scanFor(level, player, hand, Ability.ORESCANNER);
        return super.use(level, player, hand);
    }
}
