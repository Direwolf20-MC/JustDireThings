package com.direwolf20.justdirethings.datagen;

import com.direwolf20.justdirethings.setup.JDTRegistration;
import net.minecraft.core.HolderLookup;
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

    public JustDireLootTables(HolderLookup.Provider p_344962_) {
        super(p_344962_);
    }

    @Override
    protected void generate() {
        dropSelf(JDTRegistration.GooBlock_Tier1.get());
        dropSelf(JDTRegistration.GooBlock_Tier2.get());
        dropSelf(JDTRegistration.GooBlock_Tier3.get());
        dropSelf(JDTRegistration.GooBlock_Tier4.get());
        dropSelf(JDTRegistration.FerricoreBlock.get());
        dropSelf(JDTRegistration.BlazeGoldBlock.get());
        dropSelf(JDTRegistration.CelestigemBlock.get());
        dropSelf(JDTRegistration.EclipseAlloyBlock.get());
        dropSelf(JDTRegistration.CharcoalBlock.get());
        dropSelf(JDTRegistration.CoalBlock_T1.get());
        dropSelf(JDTRegistration.CoalBlock_T2.get());
        dropSelf(JDTRegistration.CoalBlock_T3.get());
        dropSelf(JDTRegistration.CoalBlock_T4.get());
        dropSelf(JDTRegistration.GooPatternBlock.get());
        dropSelf(JDTRegistration.ItemCollector.get());
        dropSelf(JDTRegistration.BlockBreakerT1.get());
        dropSelf(JDTRegistration.BlockBreakerT2.get());
        dropSelf(JDTRegistration.BlockPlacerT1.get());
        dropSelf(JDTRegistration.BlockPlacerT2.get());
        dropSelf(JDTRegistration.ClickerT1.get());
        dropSelf(JDTRegistration.ClickerT2.get());
        dropSelf(JDTRegistration.SensorT1.get());
        dropSelf(JDTRegistration.SensorT2.get());
        dropSelf(JDTRegistration.DropperT1.get());
        dropSelf(JDTRegistration.DropperT2.get());
        dropSelf(JDTRegistration.GeneratorT1.get());
        dropSelf(JDTRegistration.GeneratorFluidT1.get());
        dropSelf(JDTRegistration.EnergyTransmitter.get());
        dropSelf(JDTRegistration.PlayerAccessor.get());
        dropOther(JDTRegistration.GooSoil_Tier1.get(), Items.DIRT);
        dropOther(JDTRegistration.GooSoil_Tier2.get(), Items.DIRT);
        dropOther(JDTRegistration.GooSoil_Tier3.get(), Items.DIRT);
        dropOther(JDTRegistration.GooSoil_Tier4.get(), Items.DIRT);
        dropSelf(JDTRegistration.BlockSwapperT1.get());
        dropSelf(JDTRegistration.BlockSwapperT2.get());
        add(JDTRegistration.EclipseGateBlock.get(), noDrop());
        dropSelf(JDTRegistration.FluidPlacerT1.get());
        dropSelf(JDTRegistration.FluidPlacerT2.get());
        dropSelf(JDTRegistration.FluidCollectorT1.get());
        dropSelf(JDTRegistration.FluidCollectorT2.get());
        dropSelf(JDTRegistration.TimeCrystalBlock.get());
        //dropOther(Registration.TimeCrystalCluster.get(), Registration.TimeCrystal.get());
        //dropOther(Registration.TimeCrystalBuddingBlock.get(), Registration.TimeCrystalBlock.get());
        add(JDTRegistration.TimeCrystalCluster.get(), createSilkTouchDispatchTable(
                JDTRegistration.TimeCrystalCluster.get(),
                this.applyExplosionDecay(
                        JDTRegistration.TimeCrystalCluster.get(),
                        LootItem.lootTableItem(JDTRegistration.TimeCrystal.get())
                )
        ));
        add(JDTRegistration.TimeCrystalCluster_Small.get(), createSilkTouchOnlyTable(JDTRegistration.TimeCrystalCluster_Small_ITEM.get()));
        add(JDTRegistration.TimeCrystalCluster_Medium.get(), createSilkTouchOnlyTable(JDTRegistration.TimeCrystalCluster_Medium_ITEM.get()));
        add(JDTRegistration.TimeCrystalCluster_Large.get(), createSilkTouchOnlyTable(JDTRegistration.TimeCrystalCluster_Large_ITEM.get()));
        add(JDTRegistration.TimeCrystalBuddingBlock.get(), noDrop());
        dropSelf(JDTRegistration.ParadoxMachine.get());
        dropSelf(JDTRegistration.InventoryHolder.get());
        dropSelf(JDTRegistration.ExperienceHolder.get());

        //Raw Ores
        add(JDTRegistration.RawFerricoreOre.get(), createSilkTouchDispatchTable(
                JDTRegistration.RawFerricoreOre.get(),
                this.applyExplosionDecay(
                        JDTRegistration.RawFerricoreOre.get(),
                        LootItem.lootTableItem(JDTRegistration.RawFerricore.get())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 4.0F)))
                )
        ));
        add(JDTRegistration.RawBlazegoldOre.get(), createSilkTouchDispatchTable(
                JDTRegistration.RawBlazegoldOre.get(),
                this.applyExplosionDecay(
                        JDTRegistration.RawBlazegoldOre.get(),
                        LootItem.lootTableItem(JDTRegistration.RawBlazegold.get())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 4.0F)))
                )
        ));
        add(JDTRegistration.RawCelestigemOre.get(), createSilkTouchDispatchTable(
                JDTRegistration.RawCelestigemOre.get(),
                this.applyExplosionDecay(
                        JDTRegistration.RawCelestigemOre.get(),
                        LootItem.lootTableItem(JDTRegistration.Celestigem.get())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 4.0F)))
                )
        ));
        add(JDTRegistration.RawEclipseAlloyOre.get(), createSilkTouchDispatchTable(
                JDTRegistration.RawEclipseAlloyOre.get(),
                this.applyExplosionDecay(
                        JDTRegistration.RawEclipseAlloyOre.get(),
                        LootItem.lootTableItem(JDTRegistration.RawEclipseAlloy.get())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 4.0F)))
                )
        ));
        add(JDTRegistration.RawCoal_T1.get(), createSilkTouchDispatchTable(
                JDTRegistration.RawCoal_T1.get(),
                this.applyExplosionDecay(
                        JDTRegistration.RawCoal_T1.get(),
                        LootItem.lootTableItem(JDTRegistration.Coal_T1.get())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 4.0F)))
                )
        ));
        add(JDTRegistration.RawCoal_T2.get(), createSilkTouchDispatchTable(
                JDTRegistration.RawCoal_T2.get(),
                this.applyExplosionDecay(
                        JDTRegistration.RawCoal_T2.get(),
                        LootItem.lootTableItem(JDTRegistration.Coal_T2.get())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 4.0F)))
                )
        ));
        add(JDTRegistration.RawCoal_T3.get(), createSilkTouchDispatchTable(
                JDTRegistration.RawCoal_T3.get(),
                this.applyExplosionDecay(
                        JDTRegistration.RawCoal_T3.get(),
                        LootItem.lootTableItem(JDTRegistration.Coal_T3.get())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 4.0F)))
                )
        ));
        add(JDTRegistration.RawCoal_T4.get(), createSilkTouchDispatchTable(
                JDTRegistration.RawCoal_T4.get(),
                this.applyExplosionDecay(
                        JDTRegistration.RawCoal_T4.get(),
                        LootItem.lootTableItem(JDTRegistration.Coal_T4.get())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 4.0F)))
                )
        ));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        List<Block> knownBlocks = new ArrayList<>();
        knownBlocks.addAll(JDTRegistration.BLOCKS.getEntries().stream().map(DeferredHolder::get).toList());
        knownBlocks.addAll(JDTRegistration.SIDEDBLOCKS.getEntries().stream().map(DeferredHolder::get).toList());
        return knownBlocks;
    }
}
