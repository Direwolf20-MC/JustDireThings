package com.direwolf20.justdirethings.datagen;

import com.direwolf20.justdirethings.JustDireThings;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = JustDireThings.MODID)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherServerData(GatherDataEvent.Server event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(true, new JustDireRecipes.Runner(packOutput, lookupProvider));
        generator.addProvider(true, new LootTableProvider(packOutput, Collections.emptySet(),
                List.of(new LootTableProvider.SubProviderEntry(JustDireLootTables::new, LootContextParamSets.BLOCK)), lookupProvider));
        generator.addProvider(true, new JustDireBlockTags(packOutput, lookupProvider));
        generator.addProvider(true, new JustDireFluidTags(packOutput, lookupProvider));
        generator.addProvider(true, new JustDireItemTags(packOutput, lookupProvider));
        generator.addProvider(true, new JustDireEntityTags(packOutput, lookupProvider));
        generator.addProvider(true, new JustDireBiomeTags(packOutput, lookupProvider));
        generator.addProvider(true, new JustDireDataMaps(packOutput, lookupProvider));
    }

    @SubscribeEvent
    public static void gatherClientData(GatherDataEvent.Client event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();

        generator.addProvider(true, new JustDireModels(packOutput));
        generator.addProvider(true, new JustDireEquipmentAssets(packOutput));
        generator.addProvider(true, new JustDireLanguageProvider(packOutput, "en_us"));
        generator.addProvider(true, new JustDireSounds(packOutput));
    }
}
