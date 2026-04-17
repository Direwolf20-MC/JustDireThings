package com.direwolf20.justdirethings.datagen;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.setup.Registration;
import com.direwolf20.justdirethings.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.Tags;

import java.util.concurrent.CompletableFuture;

public class JustDireEntityTags extends EntityTypeTagsProvider {
    public JustDireEntityTags(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider, JustDireThings.MODID);
    }

    @Override
    public void addTags(HolderLookup.Provider lookupProvider) {
        tag(ModTags.Entities.CREATURE_CATCHER_DENY).add(EntityType.ENDER_DRAGON);
        tag(ModTags.Entities.NO_AI_DENY)
                .add(EntityType.ENDER_DRAGON)
                .add(EntityType.WITHER)
                .add(EntityType.WARDEN);
        tag(ModTags.Entities.NO_EARTHQUAKE)
                .add(EntityType.ENDER_DRAGON)
                .add(EntityType.WITHER)
                .add(EntityType.WARDEN);
        tag(Tags.EntityTypes.TELEPORTING_NOT_SUPPORTED)
                .add(Registration.TimeWandEntity.get())
                .add(Registration.ParadoxEntity.get());
        tag(ModTags.Entities.PARADOX_DENY)
                .add(Registration.TimeWandEntity.get())
                .add(Registration.ParadoxEntity.get());
        tag(ModTags.Entities.PARADOX_ABSORB_DENY)
                .add(Registration.TimeWandEntity.get())
                .add(Registration.ParadoxEntity.get());
        tag(EntityTypeTags.ARROWS)
                .add(Registration.JustDireArrow.get());
        tag(ModTags.Entities.POLYMORPHIC_PEACEFUL).add(
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
        tag(ModTags.Entities.POLYMORPHIC_HOSTILE).add(
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
        tag(ModTags.Entities.POLYMORPHIC_TARGET_DENY)
                .add(EntityType.ENDER_DRAGON)
                .add(EntityType.WITHER)
                .add(EntityType.WARDEN);

    }
}
