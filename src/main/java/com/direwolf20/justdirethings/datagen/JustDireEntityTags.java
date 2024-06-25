package com.direwolf20.justdirethings.datagen;

import com.direwolf20.justdirethings.JustDireThings;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class JustDireEntityTags extends EntityTypeTagsProvider {
    public JustDireEntityTags(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> providerCompletableFuture, @Nullable ExistingFileHelper existingFileHelper) {
        super(packOutput, providerCompletableFuture, JustDireThings.MODID, existingFileHelper);
    }

    public static final TagKey<EntityType<?>> CREATURE_CATCHER_DENY = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "creature_catcher_deny"));

    @Override
    public void addTags(HolderLookup.Provider lookupProvider) {
        tag(CREATURE_CATCHER_DENY).add(EntityType.ENDER_DRAGON);
    }
}
