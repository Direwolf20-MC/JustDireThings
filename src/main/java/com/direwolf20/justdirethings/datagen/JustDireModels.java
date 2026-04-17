package com.direwolf20.justdirethings.datagen;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.common.blocks.BlockBreakerT1;
import com.direwolf20.justdirethings.common.blocks.gooblocks.GooBlock_Base;
import com.direwolf20.justdirethings.common.blocks.gooblocks.GooPatternBlock;
import com.direwolf20.justdirethings.common.blocks.resources.TimeCrystalBuddingBlock;
import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.renderer.block.dispatch.VariantMutator;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.Optional;
import java.util.stream.Stream;

public class JustDireModels extends ModelProvider {
    public JustDireModels(PackOutput output) {
        super(output, JustDireThings.MODID);
    }

    private static Material blockTexture(String path) {
        return new Material(Identifier.fromNamespaceAndPath(JustDireThings.MODID, "block/" + path));
    }

    private static Identifier modLoc(String path) {
        return Identifier.fromNamespaceAndPath(JustDireThings.MODID, path);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        // Goo Blocks (ALIVE boolean)
        gooBlock(blockModels, Registration.GooBlock_Tier1.get());
        gooBlock(blockModels, Registration.GooBlock_Tier2.get());
        gooBlock(blockModels, Registration.GooBlock_Tier3.get());
        gooBlock(blockModels, Registration.GooBlock_Tier4.get());
        // Goo block items: point at the "_dead" variant's model
        blockModels.registerSimpleItemModel(Registration.GooBlock_Tier1_ITEM.get(),
                modLoc("block/" + Registration.GooBlock_Tier1.getId().getPath() + "_dead"));
        blockModels.registerSimpleItemModel(Registration.GooBlock_Tier2_ITEM.get(),
                modLoc("block/" + Registration.GooBlock_Tier2.getId().getPath() + "_dead"));
        blockModels.registerSimpleItemModel(Registration.GooBlock_Tier3_ITEM.get(),
                modLoc("block/" + Registration.GooBlock_Tier3.getId().getPath() + "_dead"));
        blockModels.registerSimpleItemModel(Registration.GooBlock_Tier4_ITEM.get(),
                modLoc("block/" + Registration.GooBlock_Tier4.getId().getPath() + "_dead"));

        // Simple storage/ore cube-all blocks
        simpleCubeAll(blockModels, Registration.FerricoreBlock.get());
        simpleCubeAll(blockModels, Registration.BlazeGoldBlock.get());
        simpleCubeAll(blockModels, Registration.CelestigemBlock.get());
        simpleCubeAll(blockModels, Registration.EclipseAlloyBlock.get());
        simpleCubeAll(blockModels, Registration.CoalBlock_T1.get());
        simpleCubeAll(blockModels, Registration.CharcoalBlock.get());
        simpleCubeAll(blockModels, Registration.CoalBlock_T2.get());
        simpleCubeAll(blockModels, Registration.CoalBlock_T3.get());
        simpleCubeAll(blockModels, Registration.CoalBlock_T4.get());
        simpleCubeAll(blockModels, Registration.PlayerAccessor.get());
        simpleCubeAll(blockModels, Registration.TimeCrystalBlock.get());
        simpleCubeAll(blockModels, Registration.InventoryHolder.get());
        // Eclipse gate uses cutout render-layer but model-wise is just cubeAll
        simpleCubeAll(blockModels, Registration.EclipseGateBlock.get());

        // Time crystal clusters: cross
        timeCrystalCluster(blockModels, Registration.TimeCrystalCluster_Small);
        timeCrystalCluster(blockModels, Registration.TimeCrystalCluster_Medium);
        timeCrystalCluster(blockModels, Registration.TimeCrystalCluster_Large);
        timeCrystalCluster(blockModels, Registration.TimeCrystalCluster);

        // Goo soil (MOISTURE 0-7 → dry/moist)
        soilBlock(blockModels, Registration.GooSoil_Tier1);
        soilBlock(blockModels, Registration.GooSoil_Tier2);
        soilBlock(blockModels, Registration.GooSoil_Tier3);
        soilBlock(blockModels, Registration.GooSoil_Tier4);

        // Raw ores: cube models with palette replacement
        oreBlock(blockModels, Registration.RawBlazegoldOre);
        oreBlock(blockModels, Registration.RawCelestigemOre);
        oreBlock(blockModels, Registration.RawEclipseAlloyOre);
        oreBlock(blockModels, Registration.RawFerricoreOre);
        oreBlock(blockModels, Registration.RawCoal_T1);
        oreBlock(blockModels, Registration.RawCoal_T2);
        oreBlock(blockModels, Registration.RawCoal_T3);
        oreBlock(blockModels, Registration.RawCoal_T4);

        // GooPatternBlock (GOOSTAGE 0-11)
        gooPatternBlock(blockModels);

        // Sided machines (FACING dispatch)
        sidedBlocks(blockModels);

        // Time crystal budding (STAGE 0-3)
        timeCrystalBudding(blockModels);

        // Fluid blocks (particle-only)
        for (var fluidBlock : Registration.FLUID_BLOCKS.getEntries()) {
            fluidBlockModel(blockModels, fluidBlock.get());
        }

        // -----------------------------------------------------------------
        // BLOCK ITEMS that reference their block model by parent
        // -----------------------------------------------------------------
        registerBlockItemParent(blockModels, Registration.GooSoil_ITEM_Tier1.get(), "block/goosoil_tier1");
        registerBlockItemParent(blockModels, Registration.GooSoil_ITEM_Tier2.get(), "block/goosoil_tier2");
        registerBlockItemParent(blockModels, Registration.GooSoil_ITEM_Tier3.get(), "block/goosoil_tier3");
        registerBlockItemParent(blockModels, Registration.GooSoil_ITEM_Tier4.get(), "block/goosoil_tier4");
        registerBlockItemParent(blockModels, Registration.FerricoreBlock_ITEM.get(), "block/ferricore_block");
        registerBlockItemParent(blockModels, Registration.RawFerricoreOre_ITEM.get(), "block/raw_ferricore_ore");
        registerBlockItemParent(blockModels, Registration.BlazeGoldBlock_ITEM.get(), "block/blazegold_block");
        registerBlockItemParent(blockModels, Registration.RawBlazegoldOre_ITEM.get(), "block/raw_blazegold_ore");
        registerBlockItemParent(blockModels, Registration.CelestigemBlock_ITEM.get(), "block/celestigem_block");
        registerBlockItemParent(blockModels, Registration.RawCelestigemOre_ITEM.get(), "block/raw_celestigem_ore");
        registerBlockItemParent(blockModels, Registration.EclipseAlloyBlock_ITEM.get(), "block/eclipsealloy_block");
        registerBlockItemParent(blockModels, Registration.RawEclipseAlloyOre_ITEM.get(), "block/raw_eclipsealloy_ore");
        registerBlockItemParent(blockModels, Registration.ItemCollector_ITEM.get(), "block/itemcollector");
        registerBlockItemParent(blockModels, Registration.BlockBreakerT1_ITEM.get(), "block/blockbreakert1");
        registerBlockItemParent(blockModels, Registration.BlockPlacerT1_ITEM.get(), "block/blockplacert1");
        registerBlockItemParent(blockModels, Registration.BlockBreakerT2_ITEM.get(), "block/blockbreakert2");
        registerBlockItemParent(blockModels, Registration.BlockPlacerT2_ITEM.get(), "block/blockplacert2");
        registerBlockItemParent(blockModels, Registration.ClickerT1_ITEM.get(), "block/clickert1");
        registerBlockItemParent(blockModels, Registration.ClickerT2_ITEM.get(), "block/clickert2");
        registerBlockItemParent(blockModels, Registration.SensorT1_ITEM.get(), "block/sensort1");
        registerBlockItemParent(blockModels, Registration.SensorT2_ITEM.get(), "block/sensort2");
        registerBlockItemParent(blockModels, Registration.DropperT1_ITEM.get(), "block/droppert1");
        registerBlockItemParent(blockModels, Registration.DropperT2_ITEM.get(), "block/droppert2");
        registerBlockItemParent(blockModels, Registration.GeneratorT1_ITEM.get(), "block/generatort1");
        registerBlockItemParent(blockModels, Registration.GeneratorFluidT1_ITEM.get(), "block/generatorfluidt1");
        registerBlockItemParent(blockModels, Registration.EnergyTransmitter_ITEM.get(), "block/energytransmitter");
        registerBlockItemParent(blockModels, Registration.CharcoalBlock_ITEM.get(), "block/charcoal");
        registerBlockItemParent(blockModels, Registration.RawCoal_T1_ITEM.get(), "block/raw_coal_t1_ore");
        registerBlockItemParent(blockModels, Registration.RawCoal_T2_ITEM.get(), "block/raw_coal_t2_ore");
        registerBlockItemParent(blockModels, Registration.RawCoal_T3_ITEM.get(), "block/raw_coal_t3_ore");
        registerBlockItemParent(blockModels, Registration.RawCoal_T4_ITEM.get(), "block/raw_coal_t4_ore");
        registerBlockItemParent(blockModels, Registration.CoalBlock_T1_ITEM.get(), "block/coalblock_t1");
        registerBlockItemParent(blockModels, Registration.CoalBlock_T2_ITEM.get(), "block/coalblock_t2");
        registerBlockItemParent(blockModels, Registration.CoalBlock_T3_ITEM.get(), "block/coalblock_t3");
        registerBlockItemParent(blockModels, Registration.CoalBlock_T4_ITEM.get(), "block/coalblock_t4");
        registerBlockItemParent(blockModels, Registration.BlockSwapperT1_ITEM.get(), "block/blockswappert1");
        registerBlockItemParent(blockModels, Registration.BlockSwapperT2_ITEM.get(), "block/blockswappert2");
        registerBlockItemParent(blockModels, Registration.PlayerAccessor_ITEM.get(), "block/playeraccessor");
        registerBlockItemParent(blockModels, Registration.FluidPlacerT1_ITEM.get(), "block/fluidplacert1");
        registerBlockItemParent(blockModels, Registration.FluidPlacerT2_ITEM.get(), "block/fluidplacert2");
        registerBlockItemParent(blockModels, Registration.FluidCollectorT1_ITEM.get(), "block/fluidcollectort1");
        registerBlockItemParent(blockModels, Registration.FluidCollectorT2_ITEM.get(), "block/fluidcollectort2");
        registerBlockItemParent(blockModels, Registration.TimeCrystalBlock_ITEM.get(), "block/time_crystal_block");
        registerBlockItemParent(blockModels, Registration.TimeCrystalBuddingBlock_ITEM.get(), "block/time_crystal_budding_block_state_0");
        registerBlockItemParent(blockModels, Registration.TimeCrystalCluster_ITEM.get(), "block/time_crystal_cluster");
        registerBlockItemParent(blockModels, Registration.TimeCrystalCluster_Small_ITEM.get(), "block/time_crystal_cluster_small");
        registerBlockItemParent(blockModels, Registration.TimeCrystalCluster_Medium_ITEM.get(), "block/time_crystal_cluster_medium");
        registerBlockItemParent(blockModels, Registration.TimeCrystalCluster_Large_ITEM.get(), "block/time_crystal_cluster_large");
        registerBlockItemParent(blockModels, Registration.ParadoxMachine_ITEM.get(), "block/paradoxmachine");
        registerBlockItemParent(blockModels, Registration.InventoryHolder_ITEM.get(), "block/inventory_holder");
        registerBlockItemParent(blockModels, Registration.ExperienceHolder_ITEM.get(), "block/experienceholder");
        // GooPatternBlock has no BlockItem (creative-only placement), so nothing to register.

        // -----------------------------------------------------------------
        // PLAIN ITEM MODELS (flat_item / handheld)
        // -----------------------------------------------------------------
        itemModels.generateFlatItem(Registration.Fuel_Canister.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(Registration.RawFerricore.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(Registration.FerricoreIngot.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(Registration.RawBlazegold.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(Registration.BlazegoldIngot.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(Registration.Celestigem.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(Registration.RawEclipseAlloy.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(Registration.EclipseAlloyIngot.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(Registration.Coal_T1.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(Registration.Coal_T2.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(Registration.Coal_T3.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(Registration.Coal_T4.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(Registration.PolymorphicCatalyst.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(Registration.PortalFluidCatalyst.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(Registration.TimeCrystal.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(Registration.TotemOfDeathRecall.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(Registration.MachineSettingsCopier.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(Registration.TEMPLATE_FERRICORE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(Registration.TEMPLATE_BLAZEGOLD.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(Registration.TEMPLATE_CELESTIGEM.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(Registration.TEMPLATE_ECLIPSEALLOY.get(), ModelTemplates.FLAT_ITEM);

        // Handheld wands / tools (flat_handheld)
        itemModels.generateFlatItem(Registration.FerricoreWrench.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(Registration.BlazejetWand.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(Registration.VoidshiftWand.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(Registration.EclipsegateWand.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(Registration.TimeWand.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(Registration.PolymorphicWand.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(Registration.PolymorphicWandV2.get(), ModelTemplates.FLAT_HANDHELD_ITEM);

        // CreatureCatcher uses built-in entity renderer
        itemModels.itemModelOutput.accept(Registration.CreatureCatcher.get(),
                ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(Registration.CreatureCatcher.get())));
        itemModels.modelOutput.accept(
                ModelLocationUtils.getModelLocation(Registration.CreatureCatcher.get()),
                () -> {
                    com.google.gson.JsonObject obj = new com.google.gson.JsonObject();
                    obj.addProperty("parent", "builtin/entity");
                    return obj;
                });

        // Upgrades (flat_item, all textures under item/abilityupgrades/<suffix>)
        for (var upgrade : Registration.UPGRADES.getEntries()) {
            Item u = upgrade.get();
            String path = upgrade.getId().getPath();
            String suffix = path.substring(path.indexOf("_") + 1);
            Identifier model = ModelLocationUtils.getModelLocation(u);
            ModelTemplates.FLAT_ITEM.create(model,
                    new TextureMapping().put(TextureSlot.LAYER0,
                            new Material(modLoc("item/abilityupgrades/" + suffix))),
                    itemModels.modelOutput);
            itemModels.itemModelOutput.accept(u, ItemModelUtils.plainModel(model));
        }

        // Tools: base flat_handheld model (enabled-swap deferred — see TODO in ClientSetup)
        for (var tool : Registration.TOOLS.getEntries()) {
            itemModels.generateFlatItem(tool.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        }
        // Pocket generators (and similar enabled-swap tools grouped under TOOLS above)
        itemModels.generateFlatItem(Registration.Pocket_Generator.get(), ModelTemplates.FLAT_HANDHELD_ITEM);

        // Bows: full pulling-stage dispatch via vanilla generateBow
        for (var bow : Registration.BOWS.getEntries()) {
            // Note: vanilla generateBow looks at item/<id> for base and item/<id>_pulling_{0,1,2}.
            // Our bow textures live under item/bows/<id>; emit a model at item/<id> that points at the bows/ texture,
            // then delegate the stage models + dispatch to vanilla.
            Item bowItem = bow.get();
            String bowName = bow.getId().getPath();
            // Override base bow texture path: emit a bow model whose layer0 lives under item/bows/
            ModelTemplates.BOW.create(ModelLocationUtils.getModelLocation(bowItem),
                    new TextureMapping().put(TextureSlot.LAYER0, new Material(modLoc("item/bows/" + bowName))),
                    itemModels.modelOutput);
            // Pulling stage models each point at item/bows/<id>_pulling_<stage>
            for (int i = 0; i < 3; i++) {
                ModelTemplates.BOW.create(ModelLocationUtils.getModelLocation(bowItem, "_pulling_" + i),
                        new TextureMapping().put(TextureSlot.LAYER0,
                                new Material(modLoc("item/bows/" + bowName + "_pulling_" + i))),
                        itemModels.modelOutput);
            }
            // Build the conditional/rangeselect dispatch
            ItemModel.Unbaked base = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(bowItem));
            ItemModel.Unbaked pulling0 = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(bowItem, "_pulling_0"));
            ItemModel.Unbaked pulling1 = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(bowItem, "_pulling_1"));
            ItemModel.Unbaked pulling2 = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(bowItem, "_pulling_2"));
            itemModels.itemModelOutput.accept(bowItem,
                    ItemModelUtils.conditional(
                            ItemModelUtils.isUsingItem(),
                            ItemModelUtils.rangeSelect(
                                    new net.minecraft.client.renderer.item.properties.numeric.UseDuration(false),
                                    0.05F, pulling0,
                                    ItemModelUtils.override(pulling1, 0.65F),
                                    ItemModelUtils.override(pulling2, 0.9F)),
                            base));
        }

        // Armors: dynamic-trim wrapper (Neo extension). 26.1 deleted ArmorItem —
        // armor is plain Item with components — so we route the trim prefix by the
        // registry-name suffix (_helmet/_chestplate/_leggings/_boots).
        for (var armor : Registration.ARMORS.getEntries()) {
            Item armorItem = armor.get();
            String armorPath = armor.getId().getPath();
            Identifier prefix;
            if (armorPath.endsWith("_helmet")) {
                prefix = ItemModelGenerators.TRIM_PREFIX_HELMET;
            } else if (armorPath.endsWith("_chestplate")) {
                prefix = ItemModelGenerators.TRIM_PREFIX_CHESTPLATE;
            } else if (armorPath.endsWith("_leggings")) {
                prefix = ItemModelGenerators.TRIM_PREFIX_LEGGINGS;
            } else if (armorPath.endsWith("_boots")) {
                prefix = ItemModelGenerators.TRIM_PREFIX_BOOTS;
            } else {
                prefix = ItemModelGenerators.TRIM_PREFIX_HELMET;
            }
            // Emit the base (non-trim) flat item texture first so the wrapper can reference it.
            ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(armorItem),
                    TextureMapping.layer0(armorItem),
                    itemModels.modelOutput);
            itemModels.generateDynamicTrimmableItem(armorItem, prefix);
        }

        // Buckets: plain flat item (fluid tint is handled by FluidTintSource in client setup — Stage 16 TODO)
        for (var bucket : Registration.BUCKET_ITEMS.getEntries()) {
            itemModels.generateFlatItem(bucket.get(), ModelTemplates.FLAT_ITEM);
        }
    }

    // -----------------------------------------------------------------
    // Block-generation helpers
    // -----------------------------------------------------------------

    private static void simpleCubeAll(BlockModelGenerators blockModels, Block block) {
        blockModels.createTrivialCube(block);
    }

    private static void timeCrystalCluster(BlockModelGenerators blockModels,
                                           DeferredHolder<Block, ? extends Block> holder) {
        Block block = holder.get();
        net.minecraft.client.data.models.MultiVariant model = BlockModelGenerators.plainVariant(
                ModelTemplates.CROSS.create(block, TextureMapping.cross(block), blockModels.modelOutput));
        blockModels.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(block, model).with(BlockModelGenerators.ROTATIONS_COLUMN_WITH_FACING));
    }

    private static void gooBlock(BlockModelGenerators blockModels, Block block) {
        // Two alive/dead texture variants → two cubeAll models dispatched on ALIVE.
        String basePath = BuiltInRegistries.BLOCK.getKey(block).getPath();
        net.minecraft.client.data.models.MultiVariant alive = BlockModelGenerators.plainVariant(
                ModelTemplates.CUBE_ALL.create(block,
                        TextureMapping.cube(blockTexture(basePath)),
                        blockModels.modelOutput));
        Identifier deadId = ModelLocationUtils.getModelLocation(block, "_dead");
        net.minecraft.client.data.models.MultiVariant dead = BlockModelGenerators.plainVariant(
                ModelTemplates.CUBE_ALL.create(deadId,
                        TextureMapping.cube(blockTexture(basePath + "_dead")),
                        blockModels.modelOutput));
        blockModels.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(block).with(
                        BlockModelGenerators.createBooleanModelDispatch(GooBlock_Base.ALIVE, alive, dead)));
    }

    private static void soilBlock(BlockModelGenerators blockModels,
                                  DeferredHolder<Block, ? extends Block> holder) {
        Block block = holder.get();
        String basePath = holder.getId().getPath();
        String tierSuffix = basePath.replace("goosoil_tier", "");
        // Dry & moist textures reuse vanilla template_farmland with custom dirt + top textures.
        TextureMapping dry = new TextureMapping()
                .put(TextureSlot.DIRT, blockTexture("goosoilside_tier" + tierSuffix))
                .put(TextureSlot.TOP, blockTexture("goofarmland_tier" + tierSuffix));
        TextureMapping moist = new TextureMapping()
                .put(TextureSlot.DIRT, blockTexture("goosoilside_tier" + tierSuffix))
                .put(TextureSlot.TOP, blockTexture("goofarmland_tier" + tierSuffix + "_moist"));
        net.minecraft.client.data.models.MultiVariant dryModel = BlockModelGenerators.plainVariant(
                ModelTemplates.FARMLAND.create(block, dry, blockModels.modelOutput));
        net.minecraft.client.data.models.MultiVariant moistModel = BlockModelGenerators.plainVariant(
                ModelTemplates.FARMLAND.create(
                        ModelLocationUtils.getModelLocation(block, "_moist"),
                        moist, blockModels.modelOutput));
        blockModels.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(block).with(
                        BlockModelGenerators.createEmptyOrFullDispatch(
                                BlockStateProperties.MOISTURE, 7, moistModel, dryModel)));
    }

    private static void oreBlock(BlockModelGenerators blockModels,
                                 DeferredHolder<Block, ? extends Block> holder) {
        Block block = holder.get();
        String basePath = holder.getId().getPath();
        // Ore model uses a custom `raw_ore_template` parent with a `palette` slot.
        ModelTemplate ORE_TEMPLATE = new ModelTemplate(
                Optional.of(modLoc("block/raw_ore_template")),
                Optional.empty(),
                PALETTE_SLOT);
        Identifier modelId = ModelLocationUtils.getModelLocation(block);
        TextureMapping mapping = new TextureMapping().put(PALETTE_SLOT, blockTexture(basePath));
        ORE_TEMPLATE.create(modelId, mapping, blockModels.modelOutput);
        net.minecraft.client.data.models.MultiVariant variant = BlockModelGenerators.plainVariant(modelId);
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block, variant));
    }

    // Custom texture slot named "palette" matching the hand-written raw_ore_template.json.
    private static final TextureSlot PALETTE_SLOT = TextureSlot.create("palette");

    private static void gooPatternBlock(BlockModelGenerators blockModels) {
        Block block = Registration.GooPatternBlock.get();
        String basePath = BuiltInRegistries.BLOCK.getKey(block).getPath();
        PropertyDispatch<net.minecraft.client.data.models.MultiVariant> dispatch =
                PropertyDispatch.initial(GooPatternBlock.GOOSTAGE).generate(stage -> {
                    TextureMapping mapping;
                    String modelSuffix = String.valueOf(stage);
                    if (stage == 11) {
                        Material full = blockTexture("goopatterns/goorender_full");
                        mapping = new TextureMapping()
                                .put(TextureSlot.TOP, full)
                                .put(TextureSlot.BOTTOM, full)
                                .put(TextureSlot.SIDE, full)
                                .put(TextureSlot.FRONT, full);
                    } else if (stage == 10) {
                        Material full = blockTexture("goopatterns/goorender_full");
                        mapping = new TextureMapping()
                                .put(TextureSlot.TOP, full)
                                .put(TextureSlot.BOTTOM, full)
                                .put(TextureSlot.SIDE, full)
                                .put(TextureSlot.FRONT, full);
                    } else if (stage == 9) {
                        Material full = blockTexture("goopatterns/goorender_full");
                        mapping = new TextureMapping()
                                .put(TextureSlot.TOP, blockTexture("goopatterns/goopatterblock_top"))
                                .put(TextureSlot.BOTTOM, full)
                                .put(TextureSlot.SIDE, full)
                                .put(TextureSlot.FRONT, full);
                    } else {
                        Material side = blockTexture("goopatterns/goorender_side" + stage);
                        Material full = blockTexture("goopatterns/goorender_full");
                        Material blank = blockTexture("goopatterns/goorender_blank");
                        mapping = new TextureMapping()
                                .put(TextureSlot.TOP, blank)
                                .put(TextureSlot.BOTTOM, full)
                                .put(TextureSlot.SIDE, side)
                                .put(TextureSlot.FRONT, side);
                    }
                    Identifier modelId = ModelLocationUtils.getModelLocation(block, modelSuffix);
                    return BlockModelGenerators.plainVariant(
                            ModelTemplates.CUBE_ORIENTABLE_TOP_BOTTOM.create(modelId, mapping, blockModels.modelOutput));
                });
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block).with(dispatch));
    }

    private static void timeCrystalBudding(BlockModelGenerators blockModels) {
        Block block = Registration.TimeCrystalBuddingBlock.get();
        PropertyDispatch<net.minecraft.client.data.models.MultiVariant> dispatch =
                PropertyDispatch.initial(TimeCrystalBuddingBlock.STAGE).generate(stage -> {
                    String suffix = (stage < 3) ? "_state_" + stage : "";
                    String texPath = (stage < 3)
                            ? "time_crystal_budding_block_state_" + stage
                            : "time_crystal_budding_block";
                    Identifier modelId = ModelLocationUtils.getModelLocation(block, suffix.isEmpty() ? "" : suffix);
                    ModelTemplates.CUBE_ALL.create(modelId,
                            TextureMapping.cube(blockTexture(texPath)),
                            blockModels.modelOutput);
                    return BlockModelGenerators.plainVariant(modelId);
                });
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block).with(dispatch));
    }

    private static void fluidBlockModel(BlockModelGenerators blockModels, Block fluidBlock) {
        // Particle-only: a model whose only texture is the `particle` slot, no geometry elements.
        ModelTemplate PARTICLE_ONLY = new ModelTemplate(Optional.empty(), Optional.empty(), TextureSlot.PARTICLE);
        Identifier modelId = ModelLocationUtils.getModelLocation(fluidBlock);
        TextureMapping mapping = new TextureMapping().put(TextureSlot.PARTICLE, blockTexture("fluid_source"));
        PARTICLE_ONLY.create(modelId, mapping, blockModels.modelOutput);
        blockModels.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(fluidBlock, BlockModelGenerators.plainVariant(modelId)));
    }

    private static void sidedBlocks(BlockModelGenerators blockModels) {
        for (var sidedBlock : Registration.SIDEDBLOCKS.getEntries()) {
            Block block = sidedBlock.get();
            String path = sidedBlock.getId().getPath();
            if (sidedBlock.equals(Registration.BlockBreakerT1)) {
                sidedBlockActiveDispatch(blockModels, block, path);
            } else {
                sidedBlockPlain(blockModels, block, path);
            }
        }
    }

    private static void sidedBlockPlain(BlockModelGenerators blockModels, Block block, String path) {
        TextureMapping mapping = new TextureMapping()
                .put(TextureSlot.TOP, blockTexture(path + "_top"))
                .put(TextureSlot.BOTTOM, blockTexture(path + "_bottom"))
                .put(TextureSlot.SIDE, blockTexture(path + "_side"))
                .put(TextureSlot.FRONT, blockTexture(path + "_side"));
        Identifier modelId = ModelTemplates.CUBE_ORIENTABLE_TOP_BOTTOM.create(block, mapping, blockModels.modelOutput);
        net.minecraft.client.data.models.MultiVariant model = BlockModelGenerators.plainVariant(modelId);
        blockModels.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(block, model).with(BlockModelGenerators.ROTATION_FACING));
    }

    private static void sidedBlockActiveDispatch(BlockModelGenerators blockModels, Block block, String path) {
        // BlockBreakerT1 has ACTIVE + FACING. Active variant uses a _top_active texture.
        TextureMapping inactive = new TextureMapping()
                .put(TextureSlot.TOP, blockTexture(path + "_top"))
                .put(TextureSlot.BOTTOM, blockTexture(path + "_bottom"))
                .put(TextureSlot.SIDE, blockTexture(path + "_side"))
                .put(TextureSlot.FRONT, blockTexture(path + "_side"));
        TextureMapping active = new TextureMapping()
                .put(TextureSlot.TOP, blockTexture(path + "_top_active"))
                .put(TextureSlot.BOTTOM, blockTexture(path + "_bottom"))
                .put(TextureSlot.SIDE, blockTexture(path + "_side"))
                .put(TextureSlot.FRONT, blockTexture(path + "_side"));
        Identifier inactiveId = ModelTemplates.CUBE_ORIENTABLE_TOP_BOTTOM.create(block, inactive, blockModels.modelOutput);
        Identifier activeId = ModelTemplates.CUBE_ORIENTABLE_TOP_BOTTOM.createWithSuffix(block, "_active", active, blockModels.modelOutput);
        net.minecraft.client.data.models.MultiVariant inactiveModel = BlockModelGenerators.plainVariant(inactiveId);
        net.minecraft.client.data.models.MultiVariant activeModel = BlockModelGenerators.plainVariant(activeId);
        blockModels.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(block)
                        .with(BlockModelGenerators.createBooleanModelDispatch(BlockBreakerT1.ACTIVE, activeModel, inactiveModel))
                        .with(BlockModelGenerators.ROTATION_FACING));
    }

    // -----------------------------------------------------------------
    // Item helpers
    // -----------------------------------------------------------------

    private static void registerBlockItemParent(BlockModelGenerators blockModels, Item blockItem, String parentPath) {
        blockModels.registerSimpleItemModel(blockItem, modLoc(parentPath));
    }

    // -----------------------------------------------------------------
    // Scope override: we want BOTH block state gen AND item gen for all mod
    // registry entries. The default getKnownBlocks/Items filter matches namespace,
    // which is what we want — no override needed.
    // -----------------------------------------------------------------
}
