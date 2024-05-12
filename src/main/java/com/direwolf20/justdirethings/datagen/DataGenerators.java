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

@EventBusSubscriber(modid = JustDireThings.MODID, bus = EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(event.includeServer(), new JustDireRecipes(packOutput, event.getLookupProvider()));
        generator.addProvider(event.includeServer(), new LootTableProvider(packOutput, Collections.emptySet(),
                List.of(new LootTableProvider.SubProviderEntry(JustDireLootTables::new, LootContextParamSets.BLOCK)), event.getLookupProvider()));
        JustDireBlockTags blockTags = new JustDireBlockTags(packOutput, lookupProvider, event.getExistingFileHelper());
        generator.addProvider(event.includeServer(), blockTags);
        JustDireItemTags itemTags = new JustDireItemTags(packOutput, lookupProvider, blockTags, event.getExistingFileHelper());
        generator.addProvider(event.includeServer(), itemTags);
        JustDireDataMaps dataMaps = new JustDireDataMaps(packOutput, lookupProvider);
        generator.addProvider(event.includeServer(), dataMaps);
        JustDireEntityTags entityTags = new JustDireEntityTags(packOutput, lookupProvider, event.getExistingFileHelper());
        generator.addProvider(event.includeServer(), entityTags);

        generator.addProvider(event.includeClient(), new JustDireBlockStates(packOutput, event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new JustDireItemModels(packOutput, event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new JustDireLanguageProvider(packOutput, "en_us"));

    }
}
