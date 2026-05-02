package com.direwolf20.justdirethings.datagen;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.world.level.biome.Biomes;

import java.util.concurrent.CompletableFuture;

public class JustDireBiomeTags extends BiomeTagsProvider {
    public JustDireBiomeTags(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider, JustDireThings.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ModTags.Biomes.UNSTABLE_PORTAL_FLUID_VIABLE)
                .add(Biomes.END_BARRENS, Biomes.END_HIGHLANDS, Biomes.END_MIDLANDS, Biomes.SMALL_END_ISLANDS, Biomes.THE_END);
    }
}
