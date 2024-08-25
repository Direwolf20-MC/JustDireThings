package com.direwolf20.justdirethings.datagen;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class JustDireEntityTags extends EntityTypeTagsProvider {
    public JustDireEntityTags(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> providerCompletableFuture, @Nullable ExistingFileHelper existingFileHelper) {
        super(packOutput, providerCompletableFuture, JustDireThings.MODID, existingFileHelper);
    }

    public static final TagKey<EntityType<?>> CREATURE_CATCHER_DENY = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "creature_catcher_deny"));
    public static final TagKey<EntityType<?>> NO_AI_DENY = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "no_ai_deny"));
    public static final TagKey<EntityType<?>> NO_EARTHQUAKE = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "no_earthquake"));
    public static final TagKey<EntityType<?>> PARADOX_DENY = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "paradox_deny"));

    @Override
    public void addTags(HolderLookup.Provider lookupProvider) {
        tag(CREATURE_CATCHER_DENY).add(EntityType.ENDER_DRAGON);
        tag(NO_AI_DENY)
                .add(EntityType.ENDER_DRAGON)
                .add(EntityType.WITHER)
                .add(EntityType.WARDEN);
        tag(NO_EARTHQUAKE)
                .add(EntityType.ENDER_DRAGON)
                .add(EntityType.WITHER)
                .add(EntityType.WARDEN);
        tag(Tags.EntityTypes.TELEPORTING_NOT_SUPPORTED)
                .add(Registration.TimeWandEntity.get());
        tag(PARADOX_DENY)
                .add(Registration.TimeWandEntity.get());
    }
}
