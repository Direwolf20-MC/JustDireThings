package com.direwolf20.justdirethings.datagen;

import com.direwolf20.justdirethings.JustDireThings;
import net.minecraft.client.data.models.EquipmentAssetProvider;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;

import java.util.function.BiConsumer;

public class JustDireEquipmentAssets extends EquipmentAssetProvider {
    public JustDireEquipmentAssets(PackOutput output) {
        super(output);
    }

    @Override
    protected void registerModels(BiConsumer<ResourceKey<EquipmentAsset>, EquipmentClientInfo> output) {
        output.accept(assetKey("ferricore"), humanoidLayer("ferricore"));
        output.accept(assetKey("blazegold"), humanoidLayer("blazegold"));
        output.accept(assetKey("celestigem"), humanoidLayer("celestigem"));
        output.accept(assetKey("eclipsealloy"), humanoidLayer("eclipsealloy"));
    }

    private static ResourceKey<EquipmentAsset> assetKey(String name) {
        return ResourceKey.create(EquipmentAssets.ROOT_ID, Identifier.fromNamespaceAndPath(JustDireThings.MODID, name));
    }

    private static EquipmentClientInfo humanoidLayer(String name) {
        return EquipmentClientInfo.builder()
                .addHumanoidLayers(Identifier.fromNamespaceAndPath(JustDireThings.MODID, name))
                .build();
    }
}
