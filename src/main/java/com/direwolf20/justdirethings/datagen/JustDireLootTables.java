package com.direwolf20.justdirethings.datagen;

import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.data.loot.packs.VanillaBlockLoot;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.stream.Collectors;

public class JustDireLootTables extends VanillaBlockLoot {

    @Override
    protected void generate() {
        dropWhenSilkTouch(Registration.GooBlock_Tier1.get());
        dropWhenSilkTouch(Registration.GooBlock_Tier2.get());
        dropWhenSilkTouch(Registration.GooBlock_Tier3.get());
        dropSelf(Registration.FerricoreBlock.get());
        dropSelf(Registration.BlazeGoldBlock.get());
        dropSelf(Registration.CelestigemBlock.get());
        dropSelf(Registration.GooPatternBlock.get());
        dropOther(Registration.GooSoil_Tier1.get(), Items.DIRT);
        dropOther(Registration.GooSoil_Tier2.get(), Items.DIRT);

        //Raw Ores
        add(Registration.RawFerricoreOre.get(), createSilkTouchDispatchTable(
                Registration.RawFerricoreOre.get(),
                this.applyExplosionDecay(
                        Registration.RawFerricoreOre.get(),
                        LootItem.lootTableItem(Registration.RawFerricore.get())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 4.0F)))
                )
        ));
        add(Registration.RawBlazegoldOre.get(), createSilkTouchDispatchTable(
                Registration.RawBlazegoldOre.get(),
                this.applyExplosionDecay(
                        Registration.RawBlazegoldOre.get(),
                        LootItem.lootTableItem(Registration.RawBlazegold.get())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 4.0F)))
                )
        ));
        add(Registration.RawCelestigemOre.get(), createSilkTouchDispatchTable(
                Registration.RawCelestigemOre.get(),
                this.applyExplosionDecay(
                        Registration.RawCelestigemOre.get(),
                        LootItem.lootTableItem(Registration.Celestigem.get())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 4.0F)))
                )
        ));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return Registration.BLOCKS.getEntries().stream().map(DeferredHolder::get).collect(Collectors.toList());
    }
}
