package com.direwolf20.justdirethings.common.items.armors.utils;

import com.direwolf20.justdirethings.JustDireThings;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;

import java.util.Map;

public class ArmorTiers {
    // TODO(port, stage-17): populate these repair tags in the item tag provider so repair actually works.
    private static final TagKey<Item> REPAIRS_FERRICORE = repairTag("ferricore");
    private static final TagKey<Item> REPAIRS_BLAZEGOLD = repairTag("blazegold");
    private static final TagKey<Item> REPAIRS_CELESTIGEM = repairTag("celestigem");
    private static final TagKey<Item> REPAIRS_ECLIPSEALLOY = repairTag("eclipsealloy");

    public static final ArmorMaterial FERRICORE = new ArmorMaterial(
            15,
            defense(2, 5, 6, 2, 6),
            9,
            SoundEvents.ARMOR_EQUIP_IRON,
            0.0F,
            0.0F,
            REPAIRS_FERRICORE,
            asset("ferricore")
    );
    public static final ArmorMaterial BLAZEGOLD = new ArmorMaterial(
            15,
            defense(2, 5, 6, 2, 6),
            25,
            SoundEvents.ARMOR_EQUIP_GOLD,
            0.0F,
            0.0F,
            REPAIRS_BLAZEGOLD,
            asset("blazegold")
    );
    public static final ArmorMaterial CELESTIGEM = new ArmorMaterial(
            33,
            defense(3, 6, 8, 3, 11),
            10,
            SoundEvents.ARMOR_EQUIP_DIAMOND,
            2.0F,
            0.0F,
            REPAIRS_CELESTIGEM,
            asset("celestigem")
    );
    public static final ArmorMaterial ECLIPSEALLOY = new ArmorMaterial(
            37,
            defense(3, 6, 8, 3, 11),
            15,
            SoundEvents.ARMOR_EQUIP_NETHERITE,
            3.0F,
            0.1F,
            REPAIRS_ECLIPSEALLOY,
            asset("eclipsealloy")
    );

    private static Map<ArmorType, Integer> defense(int boots, int legs, int chest, int helm, int body) {
        return Map.of(
                ArmorType.BOOTS, boots,
                ArmorType.LEGGINGS, legs,
                ArmorType.CHESTPLATE, chest,
                ArmorType.HELMET, helm,
                ArmorType.BODY, body
        );
    }

    private static TagKey<Item> repairTag(String name) {
        return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(JustDireThings.MODID, "repairs_" + name + "_armor"));
    }

    private static ResourceKey<EquipmentAsset> asset(String name) {
        return ResourceKey.create(EquipmentAssets.ROOT_ID, Identifier.fromNamespaceAndPath(JustDireThings.MODID, name));
    }
}
