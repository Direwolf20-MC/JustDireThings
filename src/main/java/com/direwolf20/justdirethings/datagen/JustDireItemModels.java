package com.direwolf20.justdirethings.datagen;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.loaders.DynamicFluidContainerModelBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.internal.versions.neoforge.NeoForgeVersion;

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
        withExistingParent(Registration.BlockPlacerT1_ITEM.getId().getPath(), modLoc("block/blockplacert1"));
        withExistingParent(Registration.BlockBreakerT2_ITEM.getId().getPath(), modLoc("block/blockbreakert2"));
        withExistingParent(Registration.BlockPlacerT2_ITEM.getId().getPath(), modLoc("block/blockplacert2"));
        withExistingParent(Registration.ClickerT1_ITEM.getId().getPath(), modLoc("block/clickert1"));
        withExistingParent(Registration.ClickerT2_ITEM.getId().getPath(), modLoc("block/clickert2"));
        withExistingParent(Registration.SensorT1_ITEM.getId().getPath(), modLoc("block/sensort1"));
        withExistingParent(Registration.SensorT2_ITEM.getId().getPath(), modLoc("block/sensort2"));
        withExistingParent(Registration.DropperT1_ITEM.getId().getPath(), modLoc("block/droppert1"));
        withExistingParent(Registration.DropperT2_ITEM.getId().getPath(), modLoc("block/droppert2"));
        withExistingParent(Registration.GeneratorT1_ITEM.getId().getPath(), modLoc("block/generatort1"));
        withExistingParent(Registration.GeneratorFluidT1_ITEM.getId().getPath(), modLoc("block/generatorfluidt1"));
        withExistingParent(Registration.EnergyTransmitter_ITEM.getId().getPath(), modLoc("block/energytransmitter"));
        withExistingParent(Registration.RawCoal_T1_ITEM.getId().getPath(), modLoc("block/raw_coal_t1_ore"));
        withExistingParent(Registration.RawCoal_T2_ITEM.getId().getPath(), modLoc("block/raw_coal_t2_ore"));
        withExistingParent(Registration.RawCoal_T3_ITEM.getId().getPath(), modLoc("block/raw_coal_t3_ore"));
        withExistingParent(Registration.RawCoal_T4_ITEM.getId().getPath(), modLoc("block/raw_coal_t4_ore"));
        withExistingParent(Registration.CoalBlock_T1_ITEM.getId().getPath(), modLoc("block/coalblock_t1"));
        withExistingParent(Registration.CoalBlock_T2_ITEM.getId().getPath(), modLoc("block/coalblock_t2"));
        withExistingParent(Registration.CoalBlock_T3_ITEM.getId().getPath(), modLoc("block/coalblock_t3"));
        withExistingParent(Registration.CoalBlock_T4_ITEM.getId().getPath(), modLoc("block/coalblock_t4"));
        withExistingParent(Registration.BlockSwapperT1_ITEM.getId().getPath(), modLoc("block/blockswappert1"));
        withExistingParent(Registration.BlockSwapperT2_ITEM.getId().getPath(), modLoc("block/blockswappert2"));
        withExistingParent(Registration.PlayerAccessor.getId().getPath(), modLoc("block/playeraccessor"));
        withExistingParent(Registration.FluidPlacerT1_ITEM.getId().getPath(), modLoc("block/fluidplacert1"));
        withExistingParent(Registration.FluidPlacerT2_ITEM.getId().getPath(), modLoc("block/fluidplacert2"));
        withExistingParent(Registration.FluidCollectorT1_ITEM.getId().getPath(), modLoc("block/fluidcollectort1"));
        withExistingParent(Registration.FluidCollectorT2_ITEM.getId().getPath(), modLoc("block/fluidcollectort2"));

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
        singleTexture(Registration.Coal_T1.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/coal_t1"));
        singleTexture(Registration.Coal_T2.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/coal_t2"));
        singleTexture(Registration.Coal_T3.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/coal_t3"));
        singleTexture(Registration.Coal_T4.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/coal_t4"));
        singleTexture(Registration.PolymorphicCatalyst.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/polymorphic_catalyst"));
        singleTexture(Registration.PortalFluidCatalyst.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/portal_fluid_catalyst"));

        singleTexture(Registration.FerricoreWrench.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/ferricore_wrench"));
        singleTexture(Registration.TotemOfDeathRecall.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/totem_of_death_recall"));
        singleTexture(Registration.BlazejetWand.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/blazejet_wand"));
        singleTexture(Registration.VoidshiftWand.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/voidshift_wand"));
        singleTexture(Registration.EclipsegateWand.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/eclipsegate_wand"));
        //singleTexture(Registration.CreatureCatcher.getId().getPath() + "_base", mcLoc("item/generated"), "layer0", modLoc("item/creaturecatcher"));
        getBuilder(Registration.CreatureCatcher.getId().getPath())
                .parent(new ModelFile.UncheckedModelFile("builtin/entity"));
        singleTexture(Registration.MachineSettingsCopier.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/machine_settings_copier"));
        singleTexture(Registration.TEMPLATE_FERRICORE.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/template_ferricore"));
        singleTexture(Registration.TEMPLATE_BLAZEGOLD.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/template_blazegold"));
        singleTexture(Registration.TEMPLATE_CELESTIGEM.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/template_celestigem"));
        singleTexture(Registration.TEMPLATE_ECLIPSEALLOY.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/template_eclipsealloy"));

        registerBowModels();


        //Tool Items
        registerTools();
        registerArmors();
        buckets();
        registerUpgrades();

        //Generators
        registerEnabledTextureItem(Registration.Pocket_Generator.getId().getPath());

        //Buckets
        /*withExistingParent(Registration.PORTAL_FLUID_BUCKET.getId().getPath(), new ResourceLocation(NeoForgeVersion.MOD_ID, "item/bucket"))
                .customLoader(DynamicFluidContainerModelBuilder::begin)
                .fluid(Registration.PORTAL_FLUID_BUCKET.get().content);
        withExistingParent(Registration.UNSTABLE_PORTAL_FLUID_BUCKET.getId().getPath(), new ResourceLocation(NeoForgeVersion.MOD_ID, "item/bucket"))
                .customLoader(DynamicFluidContainerModelBuilder::begin)
                .fluid(Registration.UNSTABLE_PORTAL_FLUID_BUCKET.get().content);
        withExistingParent(Registration.POLYMORPHIC_FLUID_BUCKET.getId().getPath(), new ResourceLocation(NeoForgeVersion.MOD_ID, "item/bucket"))
                .customLoader(DynamicFluidContainerModelBuilder::begin)
                .fluid(Registration.POLYMORPHIC_FLUID_BUCKET.get().content);*/
    }

    public void buckets() {
        for (var bucket : Registration.BUCKET_ITEMS.getEntries()) {
            withExistingParent(bucket.getId().getPath(), ResourceLocation.fromNamespaceAndPath(NeoForgeVersion.MOD_ID, "item/bucket"))
                    .customLoader(DynamicFluidContainerModelBuilder::begin)
                    .fluid(((BucketItem) bucket.get()).content);

        }
    }

    public void registerTools() {
        for (var tool : Registration.TOOLS.getEntries()) {
            registerEnabledTextureItem(tool.getId().getPath());
        }
    }

    public void registerUpgrades() {
        for (var upgrade : Registration.UPGRADES.getEntries()) {
            String name = upgrade.getId().getPath().substring(upgrade.getId().getPath().indexOf("_") + 1);
            singleTexture(upgrade.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/abilityupgrades/" + name));
        }
    }

    public void registerArmors() {
        for (var armor : Registration.ARMORS.getEntries()) {
            //singleTexture(armor.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/" + armor.getId().getPath()));
            armorWithTrim((ArmorItem) armor.get().asItem(), modLoc("item/" + armor.getId().getPath()));
        }
    }

    protected ItemModelBuilder generated(ItemLike itemLike, ResourceLocation texture) {
        return withExistingParent(BuiltInRegistries.ITEM.getKey(itemLike.asItem()).getPath(), "item/generated").texture("layer0", texture);
    }

    protected ItemModelBuilder armorWithTrim(ArmorItem armorItem, ResourceLocation texture) {
        ItemModelBuilder builder = generated(armorItem, texture);
        for (ItemModelGenerators.TrimModelData trimModelData : ItemModelGenerators.GENERATED_TRIM_MODELS) {
            String trimId = trimModelData.name(armorItem.getMaterial());
            ItemModelBuilder override = withExistingParent(builder.getLocation().withSuffix("_" + trimId + "_trim").getPath(), "item/generated")
                    .texture("layer0", texture)
                    .texture("layer1", ResourceLocation.withDefaultNamespace("trims/items/" + armorItem.getType().getName() + "_trim_" + trimId));
            builder.override()
                    .predicate(ItemModelGenerators.TRIM_TYPE_PREDICATE_ID, trimModelData.itemModelIndex())
                    .model(override);
        }
        return builder;
    }

    public void registerEnabledTextureItem(String path) {
        ResourceLocation enabledModelPath = modLoc("item/" + path + "_active"); // Path to your enabled model
        ResourceLocation defaultModelPath = modLoc("item/" + path); // Path to your default model

        // Start building your item model
        getBuilder(path) // This should match your item's registry name
                .parent(getExistingFile(mcLoc("item/handheld")))
                .texture("layer0", defaultModelPath)
                .override()
                .predicate(ResourceLocation.fromNamespaceAndPath("justdirethings", "enabled"), 1.0F) // Using custom property
                .model(singleTexture(path + "_active", mcLoc("item/handheld"), "layer0", enabledModelPath))
                .end();
    }

    private ItemModelBuilder forBows(ItemModelBuilder builder) {
        return builder
                .transforms()
                .transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND)
                .rotation(-80, 260, -40)
                .translation(-1.0f, -2.0f, 2.5f)
                .scale(0.9f, 0.9f, 0.9f)
                .end()
                .transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND)
                .rotation(-80, -280, 40)
                .translation(-1.0f, -2.0f, 2.5f)
                .scale(0.9f, 0.9f, 0.9f)
                .end()
                .transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND)
                .rotation(0, -90, 25)
                .translation(1.13f, 3.2f, 1.13f)
                .scale(0.68f, 0.68f, 0.68f)
                .end()
                .transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND)
                .rotation(0, 90, -25)
                .translation(1.13f, 3.2f, 1.13f)
                .scale(0.68f, 0.68f, 0.68f)
                .end()
                .end();
    }

    private void registerBowModels() {
        for (var bow : Registration.BOWS.getEntries()) {
            String bowName = bow.getId().getPath();
            // Base bow model
            forBows(singleTexture(bowName, mcLoc("item/generated"), "layer0", modLoc("item/bows/" + bowName)));

            // Pulling models
            getBuilder(bowName + "_pulling_0")
                    .parent(getExistingFile(mcLoc("item/generated")))
                    .texture("layer0", modLoc("item/bows/" + bowName + "_pulling_0"));

            getBuilder(bowName + "_pulling_1")
                    .parent(getExistingFile(mcLoc("item/generated")))
                    .texture("layer0", modLoc("item/bows/" + bowName + "_pulling_1"));

            getBuilder(bowName + "_pulling_2")
                    .parent(getExistingFile(mcLoc("item/generated")))
                    .texture("layer0", modLoc("item/bows/" + bowName + "_pulling_2"));

            // Overrides for pulling states
            getBuilder(bowName)
                    .override().predicate(modLoc("pulling"), 1).model(getExistingFile(modLoc("item/" + bowName + "_pulling_0"))).end()
                    .override().predicate(modLoc("pulling"), 1).predicate(modLoc("pull"), 0.45f).model(getExistingFile(modLoc("item/" + bowName + "_pulling_1"))).end()
                    .override().predicate(modLoc("pulling"), 1).predicate(modLoc("pull"), 0.9f).model(getExistingFile(modLoc("item/" + bowName + "_pulling_2"))).end();
        }
    }
}
