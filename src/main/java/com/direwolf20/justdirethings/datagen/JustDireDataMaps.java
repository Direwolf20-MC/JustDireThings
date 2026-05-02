package com.direwolf20.justdirethings.datagen;

import com.direwolf20.justdirethings.setup.JDTRegistration;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

import java.util.concurrent.CompletableFuture;

public class JustDireDataMaps extends DataMapProvider {
    public JustDireDataMaps(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.Provider provider) {
        builder(NeoForgeDataMaps.FURNACE_FUELS)
                .add(JDTRegistration.Coal_T1.getId(), new FurnaceFuel(4800), false)
                .add(JDTRegistration.CoalBlock_T1.getId(), new FurnaceFuel(48000), false)
                .add(JDTRegistration.Coal_T2.getId(), new FurnaceFuel(14400), false)
                .add(JDTRegistration.CoalBlock_T2.getId(), new FurnaceFuel(144000), false)
                .add(JDTRegistration.Coal_T3.getId(), new FurnaceFuel(43200), false)
                .add(JDTRegistration.CoalBlock_T3.getId(), new FurnaceFuel(432000), false)
                .add(JDTRegistration.Coal_T4.getId(), new FurnaceFuel(129600), false)
                .add(JDTRegistration.CoalBlock_T4.getId(), new FurnaceFuel(1296000), false)
                .add(JDTRegistration.CharcoalBlock.getId(), new FurnaceFuel(16000), false);
        ;
    }
}
