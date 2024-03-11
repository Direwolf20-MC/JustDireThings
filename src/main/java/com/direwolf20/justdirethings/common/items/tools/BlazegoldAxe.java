package com.direwolf20.justdirethings.common.items.tools;

import com.direwolf20.justdirethings.common.items.tools.basetools.BaseAxe;
import com.direwolf20.justdirethings.common.items.tools.utils.Ability;
import com.direwolf20.justdirethings.common.items.tools.utils.GooTier;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;

public class BlazegoldAxe extends BaseAxe {
    public BlazegoldAxe() {
        super(GooTier.BLAZEGOLD, 7.0F, -2.5F, new Properties());
        registerAbility(Ability.TREEFELLER);
        registerAbility(Ability.LEAFBREAKER);
        registerAbility(Ability.SMELTER);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        leafbreaker(pContext);
        return super.useOn(pContext);
    }
}
