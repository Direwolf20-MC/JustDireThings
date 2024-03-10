package com.direwolf20.justdirethings.datagen;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.common.items.tools.utils.AutoSmeltLootModifier;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;

/**
 * An alternate way to smelt drops - I wrote all this, but went back to my old approach. Will re-implement if told its necessary
 */
public class JustDireLootProviders extends GlobalLootModifierProvider {

    public JustDireLootProviders(PackOutput output) {
        super(output, JustDireThings.MODID);
    }

    @Override
    protected void start() {
        this.add("auto_smelt", new AutoSmeltLootModifier(new LootItemCondition[]{}));
    }

}
