package com.direwolf20.justdirethings.common.items.armors.utils;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

public class ArmorTiers {
    public static final Holder<ArmorMaterial> FERRICORE = register(
            "ferricore",
            Util.make(new EnumMap<>(ArmorItem.Type.class), p_323384_ -> {
                p_323384_.put(ArmorItem.Type.BOOTS, 2);
                p_323384_.put(ArmorItem.Type.LEGGINGS, 5);
                p_323384_.put(ArmorItem.Type.CHESTPLATE, 6);
                p_323384_.put(ArmorItem.Type.HELMET, 2);
                p_323384_.put(ArmorItem.Type.BODY, 6);
            }),
            9,
            SoundEvents.ARMOR_EQUIP_IRON,
            0.0F,
            0.0F,
            () -> Ingredient.of(Registration.FerricoreIngot.get()),
            List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "ferricore"), "", true))
    );
    public static final Holder<ArmorMaterial> BLAZEGOLD = register(
            "blazegold",
            Util.make(new EnumMap<>(ArmorItem.Type.class), p_323384_ -> {
                p_323384_.put(ArmorItem.Type.BOOTS, 2);
                p_323384_.put(ArmorItem.Type.LEGGINGS, 5);
                p_323384_.put(ArmorItem.Type.CHESTPLATE, 6);
                p_323384_.put(ArmorItem.Type.HELMET, 2);
                p_323384_.put(ArmorItem.Type.BODY, 6);
            }),
            25,
            SoundEvents.ARMOR_EQUIP_GOLD,
            0.0F,
            0.0F,
            () -> Ingredient.of(Registration.BlazegoldIngot.get()),
            List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "blazegold"), "", true))
    );
    public static final Holder<ArmorMaterial> CELESTIGEM = register(
            "celestigem",
            Util.make(new EnumMap<>(ArmorItem.Type.class), p_323384_ -> {
                p_323384_.put(ArmorItem.Type.BOOTS, 3);
                p_323384_.put(ArmorItem.Type.LEGGINGS, 6);
                p_323384_.put(ArmorItem.Type.CHESTPLATE, 8);
                p_323384_.put(ArmorItem.Type.HELMET, 3);
                p_323384_.put(ArmorItem.Type.BODY, 11);
            }),
            10,
            SoundEvents.ARMOR_EQUIP_DIAMOND,
            2.0F,
            0.0F,
            () -> Ingredient.of(Registration.Celestigem.get()),
            List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "celestigem"), "", true))
    );
    public static final Holder<ArmorMaterial> ECLIPSEALLOY = register(
            "eclipsealloy",
            Util.make(new EnumMap<>(ArmorItem.Type.class), p_323384_ -> {
                p_323384_.put(ArmorItem.Type.BOOTS, 3);
                p_323384_.put(ArmorItem.Type.LEGGINGS, 6);
                p_323384_.put(ArmorItem.Type.CHESTPLATE, 8);
                p_323384_.put(ArmorItem.Type.HELMET, 3);
                p_323384_.put(ArmorItem.Type.BODY, 11);
            }),
            15,
            SoundEvents.ARMOR_EQUIP_NETHERITE,
            3.0F,
            0.1F,
            () -> Ingredient.of(Registration.EclipseAlloyIngot.get()),
            List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "eclipsealloy"), "", true))
    );

    private static Holder<ArmorMaterial> register(
            String name,
            EnumMap<ArmorItem.Type, Integer> armors,
            int enchantability,
            Holder<SoundEvent> equipsound,
            float toughness,
            float knockbackResistance,
            Supplier<Ingredient> repairMaterial,
            List<ArmorMaterial.Layer> armorLayers
    ) {
        EnumMap<ArmorItem.Type, Integer> enummap = new EnumMap<>(ArmorItem.Type.class);

        for (ArmorItem.Type armoritem$type : ArmorItem.Type.values()) {
            enummap.put(armoritem$type, armors.get(armoritem$type));
        }

        return Registry.registerForHolder(
                BuiltInRegistries.ARMOR_MATERIAL,
                ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, name),
                new ArmorMaterial(enummap, enchantability, equipsound, repairMaterial, armorLayers, toughness, knockbackResistance)
        );
    }
}

