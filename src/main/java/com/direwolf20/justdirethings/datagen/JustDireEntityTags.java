package com.direwolf20.justdirethings.datagen;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EntityTypeTags;
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
    public static final TagKey<EntityType<?>> PARADOX_ABSORB_DENY = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "paradox_absorb_deny"));
    public static final TagKey<EntityType<?>> POLYMORPHIC_PEACEFUL = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "polymorphic_peaceful"));
    public static final TagKey<EntityType<?>> POLYMORPHIC_HOSTILE = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "polymorphic_hostile"));
    public static final TagKey<EntityType<?>> POLYMORPHIC_TARGET_DENY = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "polymorphic_target_deny"));

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
                .add(Registration.TimeWandEntity.get())
                .add(Registration.ParadoxEntity.get());
        tag(PARADOX_DENY)
                .add(Registration.TimeWandEntity.get())
                .add(Registration.ParadoxEntity.get());
        tag(PARADOX_ABSORB_DENY)
                .add(Registration.TimeWandEntity.get())
                .add(Registration.ParadoxEntity.get());
        tag(EntityTypeTags.ARROWS)
                .add(Registration.JustDireArrow.get());
        tag(POLYMORPHIC_PEACEFUL).add(
                EntityType.SHEEP,
                EntityType.PIG,
                EntityType.COW,
                EntityType.MOOSHROOM,
                EntityType.CHICKEN,
                EntityType.BAT,
                EntityType.VILLAGER,
                EntityType.SQUID,
                EntityType.OCELOT,
                EntityType.WOLF,
                EntityType.HORSE,
                EntityType.RABBIT,
                EntityType.DONKEY,
                EntityType.MULE,
                EntityType.POLAR_BEAR,
                EntityType.LLAMA,
                EntityType.PARROT,
                EntityType.DOLPHIN,
                EntityType.COD,
                EntityType.SALMON,
                EntityType.PUFFERFISH,
                EntityType.TROPICAL_FISH,
                EntityType.TURTLE,
                EntityType.CAT,
                EntityType.FOX,
                EntityType.PANDA,
                EntityType.TRADER_LLAMA,
                EntityType.WANDERING_TRADER,
                EntityType.STRIDER,
                EntityType.GLOW_SQUID,
                EntityType.GOAT,
                EntityType.BEE,
                EntityType.FROG,
                EntityType.TADPOLE,
                EntityType.CAMEL,
                EntityType.AXOLOTL
        );
        tag(POLYMORPHIC_HOSTILE).add(
                EntityType.ZOMBIE,
                EntityType.SKELETON,
                EntityType.CREEPER,
                EntityType.SPIDER,
                EntityType.ENDERMAN,
                EntityType.SILVERFISH,
                EntityType.ZOMBIFIED_PIGLIN,
                EntityType.PIGLIN,
                EntityType.PIGLIN_BRUTE,
                EntityType.HOGLIN,
                EntityType.ZOGLIN,
                EntityType.GHAST,
                EntityType.BLAZE,
                EntityType.SLIME,
                EntityType.WITCH,
                EntityType.RABBIT,
                EntityType.ENDERMITE,
                EntityType.STRAY,
                EntityType.WITHER_SKELETON,
                EntityType.SKELETON_HORSE,
                EntityType.ZOMBIE_HORSE,
                EntityType.ZOMBIE_VILLAGER,
                EntityType.HUSK,
                EntityType.GUARDIAN,
                EntityType.EVOKER,
                EntityType.VEX,
                EntityType.VINDICATOR,
                EntityType.SHULKER,
                EntityType.DROWNED,
                EntityType.PHANTOM,
                EntityType.PILLAGER
        );
        tag(POLYMORPHIC_TARGET_DENY)
                .add(EntityType.ENDER_DRAGON)
                .add(EntityType.WITHER)
                .add(EntityType.WARDEN);

    }
}
