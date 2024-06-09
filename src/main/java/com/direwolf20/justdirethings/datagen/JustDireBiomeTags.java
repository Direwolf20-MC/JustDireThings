package com.direwolf20.justdirethings.datagen;

import com.direwolf20.justdirethings.JustDireThings;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class JustDireBiomeTags extends BiomeTagsProvider {
    public JustDireBiomeTags(PackOutput p_255800_, CompletableFuture<HolderLookup.Provider> p_256205_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_255800_, p_256205_, JustDireThings.MODID, existingFileHelper);
    }

    public static final TagKey<Biome> UNSTABLE_PORTAL_FLUID_VIABLE = TagKey.create(Registries.BIOME, new ResourceLocation(JustDireThings.MODID, "unstable_portal_fluid_viable"));

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(UNSTABLE_PORTAL_FLUID_VIABLE)
                .add(Biomes.END_BARRENS, Biomes.END_HIGHLANDS, Biomes.END_MIDLANDS, Biomes.SMALL_END_ISLANDS, Biomes.THE_END);
    }
}
