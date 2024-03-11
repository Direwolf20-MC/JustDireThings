package com.direwolf20.justdirethings.common.items.tools;

import com.direwolf20.justdirethings.common.items.tools.basetools.BaseAxe;
import com.direwolf20.justdirethings.common.items.tools.utils.Ability;
import com.direwolf20.justdirethings.common.items.tools.utils.GooTier;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;

public class FerricoreAxe extends BaseAxe {
    public FerricoreAxe() {
        super(GooTier.FERRICORE, 7.0F, -2.5F, new Item.Properties());
        registerAbility(Ability.TREEFELLER);
        registerAbility(Ability.LEAFBREAKER);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        leafbreaker(pContext);
        return super.useOn(pContext);
    }
}
