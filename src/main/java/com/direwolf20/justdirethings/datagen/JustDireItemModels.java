package com.direwolf20.justdirethings.datagen;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class JustDireItemModels extends ItemModelProvider {
    public JustDireItemModels(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, JustDireThings.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        //Block Items
        withExistingParent(Registration.GooSoil_Tier1.getId().getPath(), modLoc("block/goosoil_tier1"));
        withExistingParent(Registration.GooSoil_Tier2.getId().getPath(), modLoc("block/goosoil_tier2"));
        withExistingParent(Registration.GooSoil_Tier3.getId().getPath(), modLoc("block/goosoil_tier3"));
        withExistingParent(Registration.GooSoil_Tier4.getId().getPath(), modLoc("block/goosoil_tier4"));
        withExistingParent(Registration.GooBlock_Tier1_ITEM.getId().getPath(), modLoc("block/gooblock_tier1"));
        withExistingParent(Registration.GooBlock_Tier2_ITEM.getId().getPath(), modLoc("block/gooblock_tier2"));
        withExistingParent(Registration.GooBlock_Tier3_ITEM.getId().getPath(), modLoc("block/gooblock_tier3"));
        withExistingParent(Registration.GooBlock_Tier4_ITEM.getId().getPath(), modLoc("block/gooblock_tier4"));
        withExistingParent(Registration.FerricoreBlock_ITEM.getId().getPath(), modLoc("block/ferricore_block"));
        withExistingParent(Registration.RawFerricoreOre_ITEM.getId().getPath(), modLoc("block/raw_ferricore_ore"));
        withExistingParent(Registration.BlazeGoldBlock_ITEM.getId().getPath(), modLoc("block/blazegold_block"));
        withExistingParent(Registration.RawBlazegoldOre_ITEM.getId().getPath(), modLoc("block/raw_blazegold_ore"));
        withExistingParent(Registration.CelestigemBlock_ITEM.getId().getPath(), modLoc("block/celestigem_block"));
        withExistingParent(Registration.RawCelestigemOre_ITEM.getId().getPath(), modLoc("block/raw_celestigem_ore"));
        withExistingParent(Registration.EclipseAlloyBlock_ITEM.getId().getPath(), modLoc("block/eclipsealloy_block"));
        withExistingParent(Registration.RawEclipseAlloyOre_ITEM.getId().getPath(), modLoc("block/raw_eclipsealloy_ore"));
        withExistingParent(Registration.ItemCollector_ITEM.getId().getPath(), modLoc("block/itemcollector"));
        withExistingParent(Registration.BlockBreakerT1_ITEM.getId().getPath(), modLoc("block/blockbreakert1"));
        withExistingParent(Registration.BlockBreakerT2_ITEM.getId().getPath(), modLoc("block/blockbreakert2"));

        //Item items
        singleTexture(Registration.Fuel_Canister.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/fuel_canister"));
        //singleTexture(Registration.Pocket_Generator.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/pocketgenerator"));
        //singleTexture(Registration.Pocket_GeneratorT2.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/pocketgenerator_t2"));
        //singleTexture(Registration.Pocket_GeneratorT3.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/pocketgenerator_t3"));
        //singleTexture(Registration.Pocket_GeneratorT4.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/pocketgenerator_t4"));
        singleTexture(Registration.RawFerricore.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/raw_ferricore"));
        singleTexture(Registration.FerricoreIngot.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/ferricore_ingot"));
        singleTexture(Registration.RawBlazegold.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/raw_blazegold"));
        singleTexture(Registration.BlazegoldIngot.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/blazegold_ingot"));
        singleTexture(Registration.Celestigem.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/celestigem"));
        singleTexture(Registration.RawBlazegold.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/raw_blazegold"));
        singleTexture(Registration.BlazegoldIngot.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/blazegold_ingot"));
        singleTexture(Registration.RawEclipseAlloy.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/raw_eclipsealloy"));
        singleTexture(Registration.EclipseAlloyIngot.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/eclipsealloy_ingot"));

        //Tool Items
        registerTools();

        //Generators
        registerEnabledTextureItem(Registration.Pocket_Generator.getId().getPath());
        registerEnabledTextureItem(Registration.Pocket_GeneratorT2.getId().getPath());
        registerEnabledTextureItem(Registration.Pocket_GeneratorT3.getId().getPath());
        registerEnabledTextureItem(Registration.Pocket_GeneratorT4.getId().getPath());

    }

    public void registerTools() {
        for (var tool : Registration.TOOLS.getEntries()) {
            registerEnabledTextureItem(tool.getId().getPath());
        }
    }

    public void registerEnabledTextureItem(String path) {
        ResourceLocation enabledModelPath = modLoc("item/" + path + "_active"); // Path to your enabled model
        ResourceLocation defaultModelPath = modLoc("item/" + path); // Path to your default model

        // Start building your item model
        getBuilder(path) // This should match your item's registry name
                .parent(getExistingFile(mcLoc("item/handheld")))
                .texture("layer0", defaultModelPath)
                .override()
                .predicate(new ResourceLocation("justdirethings", "enabled"), 1.0F) // Using custom property
                .model(singleTexture(path + "_active", mcLoc("item/handheld"), "layer0", enabledModelPath))
                .end();
    }
}
