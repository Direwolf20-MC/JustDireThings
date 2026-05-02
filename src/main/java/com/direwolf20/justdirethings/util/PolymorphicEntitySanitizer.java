package com.direwolf20.justdirethings.util;

import net.minecraft.nbt.CompoundTag;

import java.util.Set;

public final class PolymorphicEntitySanitizer {
    // Cosmetic-only identity: variant + colour, no inventory/equipment/ownership/age/health/trades.
    // Covers both pre-1.21.5 "Variant"/"RabbitType" and the 1.21.5+ lowercase "variant" registry key
    // used by cow/pig/chicken/cat/frog/wolf.
    private static final Set<String> COSMETIC_FIELDS = Set.of(
            "Variant",        // axolotl, horse, llama, parrot, tropical fish
            "variant",        // cow, pig, chicken, cat, frog, wolf (VariantUtils)
            "RabbitType",     // rabbit
            "Color",          // sheep, shulker
            "Sheared",        // sheep
            "FromBucket",     // axolotl, fish — flagged so wild-spawned fish de-spawn timer works out
            "VillagerData",   // villager / zombie-villager profession & biome (not trade offers)
            "powered"         // charged creeper
    );

    private PolymorphicEntitySanitizer() {
    }

    public static CompoundTag cosmeticOnly(CompoundTag source) {
        CompoundTag out = new CompoundTag();
        for (String key : COSMETIC_FIELDS) {
            if (source.contains(key)) {
                out.put(key, source.get(key));
            }
        }
        return out;
    }
}
