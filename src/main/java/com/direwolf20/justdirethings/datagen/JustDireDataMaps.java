package com.direwolf20.justdirethings.datagen;

import com.direwolf20.justdirethings.setup.Registration;
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
    protected void gather() {
        builder(NeoForgeDataMaps.FURNACE_FUELS)
                .add(Registration.Coal_T1.getId(), new FurnaceFuel(6400), false)
                .add(Registration.CoalBlock_T1.getId(), new FurnaceFuel(64000), false)
                .add(Registration.Coal_T2.getId(), new FurnaceFuel(25600), false)
                .add(Registration.CoalBlock_T2.getId(), new FurnaceFuel(256000), false)
                .add(Registration.Coal_T3.getId(), new FurnaceFuel(102400), false)
                .add(Registration.CoalBlock_T3.getId(), new FurnaceFuel(1024000), false)
                .add(Registration.Coal_T4.getId(), new FurnaceFuel(409600), false)
                .add(Registration.CoalBlock_T4.getId(), new FurnaceFuel(4096000), false);
        ;
    }


}
