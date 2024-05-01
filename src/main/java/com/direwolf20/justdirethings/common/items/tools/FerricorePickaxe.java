package com.direwolf20.justdirethings.common.items.tools;

import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.common.items.tools.basetools.BasePickaxe;
import com.direwolf20.justdirethings.common.items.tools.utils.GooTier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.PickaxeItem;

public class FerricorePickaxe extends BasePickaxe {
    public FerricorePickaxe() {
        super(GooTier.FERRICORE, new Item.Properties()
                .attributes(PickaxeItem.createAttributes(GooTier.FERRICORE, 1.0F, -2.8F)));
        registerAbility(Ability.ORESCANNER);
        registerAbility(Ability.OREMINER);
    }
}
