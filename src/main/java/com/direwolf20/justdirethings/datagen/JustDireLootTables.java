package com.direwolf20.justdirethings.datagen;

import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.data.loot.packs.VanillaBlockLoot;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.ArrayList;
import java.util.List;

public class JustDireLootTables extends VanillaBlockLoot {

    @Override
    protected void generate() {
        dropSelf(Registration.GooBlock_Tier1.get());
        dropSelf(Registration.GooBlock_Tier2.get());
        dropSelf(Registration.GooBlock_Tier3.get());
        dropSelf(Registration.GooBlock_Tier4.get());
        dropSelf(Registration.FerricoreBlock.get());
        dropSelf(Registration.BlazeGoldBlock.get());
        dropSelf(Registration.CelestigemBlock.get());
        dropSelf(Registration.EclipseAlloyBlock.get());
        dropSelf(Registration.CoalBlock_T1.get());
        dropSelf(Registration.CoalBlock_T2.get());
        dropSelf(Registration.CoalBlock_T3.get());
        dropSelf(Registration.CoalBlock_T4.get());
        dropSelf(Registration.GooPatternBlock.get());
        dropSelf(Registration.ItemCollector.get());
        dropSelf(Registration.BlockBreakerT1.get());
        dropSelf(Registration.BlockBreakerT2.get());
        dropSelf(Registration.BlockPlacerT1.get());
        dropSelf(Registration.BlockPlacerT2.get());
        dropSelf(Registration.ClickerT1.get());
        dropSelf(Registration.ClickerT2.get());
        dropSelf(Registration.SensorT1.get());
        dropSelf(Registration.SensorT2.get());
        dropSelf(Registration.DropperT1.get());
        dropSelf(Registration.DropperT2.get());
        dropSelf(Registration.GeneratorT1.get());
        dropSelf(Registration.EnergyTransmitter.get());
        dropSelf(Registration.PlayerAccessor.get());
        dropOther(Registration.GooSoil_Tier1.get(), Items.DIRT);
        dropOther(Registration.GooSoil_Tier2.get(), Items.DIRT);
        dropOther(Registration.GooSoil_Tier3.get(), Items.DIRT);
        dropOther(Registration.GooSoil_Tier4.get(), Items.DIRT);
        dropSelf(Registration.BlockSwapperT1.get());
        dropSelf(Registration.BlockSwapperT2.get());

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
        add(Registration.RawEclipseAlloyOre.get(), createSilkTouchDispatchTable(
                Registration.RawEclipseAlloyOre.get(),
                this.applyExplosionDecay(
                        Registration.RawEclipseAlloyOre.get(),
                        LootItem.lootTableItem(Registration.RawEclipseAlloy.get())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 4.0F)))
                )
        ));
        add(Registration.RawCoal_T1.get(), createSilkTouchDispatchTable(
                Registration.RawCoal_T1.get(),
                this.applyExplosionDecay(
                        Registration.RawCoal_T1.get(),
                        LootItem.lootTableItem(Registration.Coal_T1.get())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 4.0F)))
                )
        ));
        add(Registration.RawCoal_T2.get(), createSilkTouchDispatchTable(
                Registration.RawCoal_T2.get(),
                this.applyExplosionDecay(
                        Registration.RawCoal_T2.get(),
                        LootItem.lootTableItem(Registration.Coal_T2.get())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 4.0F)))
                )
        ));
        add(Registration.RawCoal_T3.get(), createSilkTouchDispatchTable(
                Registration.RawCoal_T3.get(),
                this.applyExplosionDecay(
                        Registration.RawCoal_T3.get(),
                        LootItem.lootTableItem(Registration.Coal_T3.get())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 4.0F)))
                )
        ));
        add(Registration.RawCoal_T4.get(), createSilkTouchDispatchTable(
                Registration.RawCoal_T4.get(),
                this.applyExplosionDecay(
                        Registration.RawCoal_T4.get(),
                        LootItem.lootTableItem(Registration.Coal_T4.get())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 4.0F)))
                )
        ));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        List<Block> knownBlocks = new ArrayList<>();
        knownBlocks.addAll(Registration.BLOCKS.getEntries().stream().map(DeferredHolder::get).toList());
        knownBlocks.addAll(Registration.SIDEDBLOCKS.getEntries().stream().map(DeferredHolder::get).toList());
        return knownBlocks;
    }
}
