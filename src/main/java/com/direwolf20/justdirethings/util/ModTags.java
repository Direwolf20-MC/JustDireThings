package com.direwolf20.justdirethings.util;

import com.direwolf20.justdirethings.JustDireThings;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

import java.util.List;

public final class ModTags {
    private ModTags() {}

    private static Identifier mod(String name) {
        return Identifier.fromNamespaceAndPath(JustDireThings.MODID, name);
    }

    private static Identifier c(String name) {
        return Identifier.fromNamespaceAndPath("c", name);
    }

    public static final class Blocks {
        private Blocks() {}

        public static final TagKey<Block> LAWNMOWERABLE = block("lawnmowerable");
        public static final TagKey<Block> NO_AUTO_CLICK = block("noautoclick");
        public static final TagKey<Block> SWAPPERDENY = block("swapper_deny");
        public static final TagKey<Block> ECLISEGATEDENY = block("eclipsegate_deny");
        public static final TagKey<Block> PHASEDENY = block("phase_deny");
        public static final TagKey<Block> TICK_SPEED_DENY = block("tick_speed_deny");
        public static final TagKey<Block> PARADOX_ALLOW = block("paradox_allow");
        public static final TagKey<Block> PARADOX_ABSORB_DENY = block("paradox_absorb_deny");
        public static final TagKey<Block> CHARCOAL = blockC("storage_blocks/charcoal");

        private static TagKey<Block> block(String name) {
            return TagKey.create(Registries.BLOCK, mod(name));
        }

        private static TagKey<Block> blockC(String name) {
            return TagKey.create(Registries.BLOCK, c(name));
        }
    }

    public static final class Items {
        private Items() {}

        public static final TagKey<Item> WRENCHES = itemC("wrenches");
        public static final TagKey<Item> TOOLS_WRENCH = itemC("tools/wrench");
        public static final TagKey<Item> RAW_FERRICORE = itemC("raw_materials/ferricore");
        public static final TagKey<Item> RAW_BLAZEGOLD = itemC("raw_materials/blazegold");
        public static final TagKey<Item> RAW_ECLIPSEALLOY = itemC("raw_materials/eclipsealloy");
        public static final TagKey<Item> INGOT_FERRICORE = itemC("ingots/ferricore");
        public static final TagKey<Item> INGOT_BLAZEGOLD = itemC("ingots/blazegold");
        public static final TagKey<Item> INGOT_ECLIPSEALLOY = itemC("ingots/eclipsealloy");

        public static final TagKey<Item> BOWS = itemC("tools/bow");
        public static final TagKey<Item> RANGED_WEAPON = itemC("tools/ranged_weapon");
        public static final TagKey<Item> MELEE_WEAPON = itemC("tools/melee_weapon");
        public static final TagKey<Item> MINING_TOOL = itemC("tools/mining_tool");
        public static final TagKey<Item> PAXEL = itemC("tools/paxel");
        public static final TagKey<Item> STORAGEBLOCKS = itemC("storage_blocks");
        public static final TagKey<Item> CHARCOALBLOCKS = itemC("storage_blocks/charcoal");

        public static final TagKey<Item> FUEL_CANISTER_DENY = item("deny_fuel_canister");
        public static final TagKey<Item> AUTO_SMELT_DENY = item("auto_smelt_deny");
        public static final TagKey<Item> AUTO_SMOKE_DENY = item("auto_smoke_deny");
        public static final TagKey<Item> GOO_REVIVE_TIER_1 = item("goo_revive_tier_1");
        public static final TagKey<Item> GOO_REVIVE_TIER_2 = item("goo_revive_tier_2");
        public static final TagKey<Item> GOO_REVIVE_TIER_3 = item("goo_revive_tier_3");
        public static final TagKey<Item> GOO_REVIVE_TIER_4 = item("goo_revive_tier_4");
        public static final TagKey<Item> PARADOX_DENY = item("paradox_deny");
        public static final TagKey<Item> GOO_RECIPE_TIER_1 = item("goorecipe_tier/1");
        public static final TagKey<Item> GOO_RECIPE_TIER_2 = item("goorecipe_tier/2");
        public static final TagKey<Item> GOO_RECIPE_TIER_3 = item("goorecipe_tier/3");
        public static final TagKey<Item> GOO_RECIPE_TIER_4 = item("goorecipe_tier/4");

        public static final List<TagKey<Item>> GOO_RECIPE_TIERS = List.of(
                GOO_RECIPE_TIER_1, GOO_RECIPE_TIER_2, GOO_RECIPE_TIER_3, GOO_RECIPE_TIER_4);

        private static TagKey<Item> item(String name) {
            return TagKey.create(Registries.ITEM, mod(name));
        }

        private static TagKey<Item> itemC(String name) {
            return TagKey.create(Registries.ITEM, c(name));
        }
    }

    public static final class Fluids {
        private Fluids() {}

        public static final TagKey<Fluid> EXPERIENCE = TagKey.create(Registries.FLUID, c("experience"));
    }

    public static final class Entities {
        private Entities() {}

        public static final TagKey<EntityType<?>> CREATURE_CATCHER_DENY = entity("creature_catcher_deny");
        public static final TagKey<EntityType<?>> NO_AI_DENY = entity("no_ai_deny");
        public static final TagKey<EntityType<?>> NO_EARTHQUAKE = entity("no_earthquake");
        public static final TagKey<EntityType<?>> PARADOX_DENY = entity("paradox_deny");
        public static final TagKey<EntityType<?>> PARADOX_ABSORB_DENY = entity("paradox_absorb_deny");
        public static final TagKey<EntityType<?>> POLYMORPHIC_PEACEFUL = entity("polymorphic_peaceful");
        public static final TagKey<EntityType<?>> POLYMORPHIC_HOSTILE = entity("polymorphic_hostile");
        public static final TagKey<EntityType<?>> POLYMORPHIC_TARGET_DENY = entity("polymorphic_target_deny");

        private static TagKey<EntityType<?>> entity(String name) {
            return TagKey.create(Registries.ENTITY_TYPE, mod(name));
        }
    }

    public static final class Biomes {
        private Biomes() {}

        public static final TagKey<Biome> UNSTABLE_PORTAL_FLUID_VIABLE =
                TagKey.create(Registries.BIOME, mod("unstable_portal_fluid_viable"));
    }
}
