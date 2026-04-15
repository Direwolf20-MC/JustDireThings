package com.direwolf20.justdirethings.common.items.tools.utils;

import com.direwolf20.justdirethings.JustDireThings;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.level.block.Block;

import static net.minecraft.tags.BlockTags.*;

public enum GooTier {
    FERRICORE(new ToolMaterial(INCORRECT_FOR_IRON_TOOL, 500, 7.0F, 2.5F, 15, repairTag("ferricore"))),
    BLAZEGOLD(new ToolMaterial(INCORRECT_FOR_DIAMOND_TOOL, 1440, 12.0F, 3.0F, 22, repairTag("blazegold"))),
    CELESTIGEM(new ToolMaterial(INCORRECT_FOR_DIAMOND_TOOL, 1561, 10.0F, 4.0F, 18, repairTag("celestigem"))),
    ECLIPSEALLOY(new ToolMaterial(INCORRECT_FOR_NETHERITE_TOOL, 2561, 16.0F, 5.0F, 25, repairTag("eclipsealloy")));

    private final ToolMaterial material;

    GooTier(ToolMaterial material) {
        this.material = material;
    }

    public ToolMaterial material() {
        return material;
    }

    public int getUses() {
        return material.durability();
    }

    public float getSpeed() {
        return material.speed();
    }

    public float getAttackDamageBonus() {
        return material.attackDamageBonus();
    }

    public TagKey<Block> getIncorrectBlocksForDrops() {
        return material.incorrectBlocksForDrops();
    }

    public int getEnchantmentValue() {
        return material.enchantmentValue();
    }

    // TODO(port, stage-17): populate these repair tags in the item tag provider so repair actually works.
    private static TagKey<Item> repairTag(String name) {
        return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(JustDireThings.MODID, "repairs_" + name + "_tool"));
    }
}
