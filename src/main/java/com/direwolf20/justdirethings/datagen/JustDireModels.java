package com.direwolf20.justdirethings.datagen;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.client.itemcustomrenders.*;
import com.direwolf20.justdirethings.common.blocks.BlockBreakerT1;
import com.direwolf20.justdirethings.common.blocks.gooblocks.GooBlock_Base;
import com.direwolf20.justdirethings.common.blocks.gooblocks.GooPatternBlock;
import com.direwolf20.justdirethings.common.blocks.resources.TimeCrystalBuddingBlock;
import com.direwolf20.justdirethings.setup.JDTRegistration;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.RangeSelectItemModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JustDireModels extends ModelProvider {
    // Blocks whose blockstate + model JSONs are hand-authored under src/main/resources
    // (BlockBench-exported custom geometry) and therefore excluded from datagen validation.
    private static final java.util.Set<Block> HAND_AUTHORED_BLOCKS = java.util.Set.of(
            JDTRegistration.ItemCollector.get(),
            JDTRegistration.ExperienceHolder.get(),
            JDTRegistration.GeneratorT1.get(),
            JDTRegistration.GeneratorFluidT1.get(),
            JDTRegistration.EnergyTransmitter.get()
    );

    public JustDireModels(PackOutput output) {
        super(output, JustDireThings.MODID);
    }

    @Override
    protected java.util.stream.Stream<? extends net.minecraft.core.Holder<Block>> getKnownBlocks() {
        return super.getKnownBlocks().filter(h -> !HAND_AUTHORED_BLOCKS.contains(h.value()));
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
        gooBlock(blockModels, JDTRegistration.GooBlock_Tier1.get());
        gooBlock(blockModels, JDTRegistration.GooBlock_Tier2.get());
        gooBlock(blockModels, JDTRegistration.GooBlock_Tier3.get());
        gooBlock(blockModels, JDTRegistration.GooBlock_Tier4.get());
        // Goo block items: point at the "_dead" variant's model
        blockModels.registerSimpleItemModel(JDTRegistration.GooBlock_Tier1_ITEM.get(),
                modLoc("block/" + JDTRegistration.GooBlock_Tier1.getId().getPath() + "_dead"));
        blockModels.registerSimpleItemModel(JDTRegistration.GooBlock_Tier2_ITEM.get(),
                modLoc("block/" + JDTRegistration.GooBlock_Tier2.getId().getPath() + "_dead"));
        blockModels.registerSimpleItemModel(JDTRegistration.GooBlock_Tier3_ITEM.get(),
                modLoc("block/" + JDTRegistration.GooBlock_Tier3.getId().getPath() + "_dead"));
        blockModels.registerSimpleItemModel(JDTRegistration.GooBlock_Tier4_ITEM.get(),
                modLoc("block/" + JDTRegistration.GooBlock_Tier4.getId().getPath() + "_dead"));

        // Simple storage/ore cube-all blocks
        simpleCubeAll(blockModels, JDTRegistration.FerricoreBlock.get());
        simpleCubeAll(blockModels, JDTRegistration.BlazeGoldBlock.get());
        simpleCubeAll(blockModels, JDTRegistration.CelestigemBlock.get());
        simpleCubeAll(blockModels, JDTRegistration.EclipseAlloyBlock.get());
        simpleCubeAll(blockModels, JDTRegistration.CoalBlock_T1.get());
        simpleCubeAll(blockModels, JDTRegistration.CharcoalBlock.get());
        simpleCubeAll(blockModels, JDTRegistration.CoalBlock_T2.get());
        simpleCubeAll(blockModels, JDTRegistration.CoalBlock_T3.get());
        simpleCubeAll(blockModels, JDTRegistration.CoalBlock_T4.get());
        simpleCubeAll(blockModels, JDTRegistration.PlayerAccessor.get());
        simpleCubeAll(blockModels, JDTRegistration.TimeCrystalBlock.get());
        simpleCubeAll(blockModels, JDTRegistration.InventoryHolder.get());
        // Eclipse gate uses cutout render-layer but model-wise is just cubeAll
        simpleCubeAll(blockModels, JDTRegistration.EclipseGateBlock.get());

        // Time crystal clusters: cross
        timeCrystalCluster(blockModels, JDTRegistration.TimeCrystalCluster_Small);
        timeCrystalCluster(blockModels, JDTRegistration.TimeCrystalCluster_Medium);
        timeCrystalCluster(blockModels, JDTRegistration.TimeCrystalCluster_Large);
        timeCrystalCluster(blockModels, JDTRegistration.TimeCrystalCluster);

        // Goo soil (MOISTURE 0-7 → dry/moist)
        soilBlock(blockModels, JDTRegistration.GooSoil_Tier1);
        soilBlock(blockModels, JDTRegistration.GooSoil_Tier2);
        soilBlock(blockModels, JDTRegistration.GooSoil_Tier3);
        soilBlock(blockModels, JDTRegistration.GooSoil_Tier4);

        // Raw ores: cube models with palette replacement
        oreBlock(blockModels, JDTRegistration.RawBlazegoldOre);
        oreBlock(blockModels, JDTRegistration.RawCelestigemOre);
        oreBlock(blockModels, JDTRegistration.RawEclipseAlloyOre);
        oreBlock(blockModels, JDTRegistration.RawFerricoreOre);
        oreBlock(blockModels, JDTRegistration.RawCoal_T1);
        oreBlock(blockModels, JDTRegistration.RawCoal_T2);
        oreBlock(blockModels, JDTRegistration.RawCoal_T3);
        oreBlock(blockModels, JDTRegistration.RawCoal_T4);

        // GooPatternBlock (GOOSTAGE 0-11)
        gooPatternBlock(blockModels);

        // Sided machines (FACING dispatch)
        sidedBlocks(blockModels);

        // Time crystal budding (STAGE 0-3)
        timeCrystalBudding(blockModels);

        // Fluid blocks (particle-only)
        for (var fluidBlock : JDTRegistration.FLUID_BLOCKS.getEntries()) {
            fluidBlockModel(blockModels, fluidBlock.get());
        }

        // -----------------------------------------------------------------
        // BLOCK ITEMS that reference their block model by parent
        // -----------------------------------------------------------------
        registerBlockItemParent(blockModels, JDTRegistration.GooSoil_ITEM_Tier1.get(), "block/goosoil_tier1");
        registerBlockItemParent(blockModels, JDTRegistration.GooSoil_ITEM_Tier2.get(), "block/goosoil_tier2");
        registerBlockItemParent(blockModels, JDTRegistration.GooSoil_ITEM_Tier3.get(), "block/goosoil_tier3");
        registerBlockItemParent(blockModels, JDTRegistration.GooSoil_ITEM_Tier4.get(), "block/goosoil_tier4");
        registerBlockItemParent(blockModels, JDTRegistration.FerricoreBlock_ITEM.get(), "block/ferricore_block");
        registerBlockItemParent(blockModels, JDTRegistration.RawFerricoreOre_ITEM.get(), "block/raw_ferricore_ore");
        registerBlockItemParent(blockModels, JDTRegistration.BlazeGoldBlock_ITEM.get(), "block/blazegold_block");
        registerBlockItemParent(blockModels, JDTRegistration.RawBlazegoldOre_ITEM.get(), "block/raw_blazegold_ore");
        registerBlockItemParent(blockModels, JDTRegistration.CelestigemBlock_ITEM.get(), "block/celestigem_block");
        registerBlockItemParent(blockModels, JDTRegistration.RawCelestigemOre_ITEM.get(), "block/raw_celestigem_ore");
        registerBlockItemParent(blockModels, JDTRegistration.EclipseAlloyBlock_ITEM.get(), "block/eclipsealloy_block");
        registerBlockItemParent(blockModels, JDTRegistration.RawEclipseAlloyOre_ITEM.get(), "block/raw_eclipsealloy_ore");
        registerBlockItemParent(blockModels, JDTRegistration.ItemCollector_ITEM.get(), "block/itemcollector");
        registerBlockItemParent(blockModels, JDTRegistration.BlockBreakerT1_ITEM.get(), "block/blockbreakert1");
        registerBlockItemParent(blockModels, JDTRegistration.BlockPlacerT1_ITEM.get(), "block/blockplacert1");
        registerBlockItemParent(blockModels, JDTRegistration.BlockBreakerT2_ITEM.get(), "block/blockbreakert2");
        registerBlockItemParent(blockModels, JDTRegistration.BlockPlacerT2_ITEM.get(), "block/blockplacert2");
        registerBlockItemParent(blockModels, JDTRegistration.ClickerT1_ITEM.get(), "block/clickert1");
        registerBlockItemParent(blockModels, JDTRegistration.ClickerT2_ITEM.get(), "block/clickert2");
        registerBlockItemParent(blockModels, JDTRegistration.SensorT1_ITEM.get(), "block/sensort1");
        registerBlockItemParent(blockModels, JDTRegistration.SensorT2_ITEM.get(), "block/sensort2");
        registerBlockItemParent(blockModels, JDTRegistration.DropperT1_ITEM.get(), "block/droppert1");
        registerBlockItemParent(blockModels, JDTRegistration.DropperT2_ITEM.get(), "block/droppert2");
        registerBlockItemParent(blockModels, JDTRegistration.GeneratorT1_ITEM.get(), "block/generatort1");
        registerBlockItemParent(blockModels, JDTRegistration.GeneratorFluidT1_ITEM.get(), "block/generatorfluidt1");
        registerBlockItemParent(blockModels, JDTRegistration.EnergyTransmitter_ITEM.get(), "block/energytransmitter");
        registerBlockItemParent(blockModels, JDTRegistration.CharcoalBlock_ITEM.get(), "block/charcoal");
        registerBlockItemParent(blockModels, JDTRegistration.RawCoal_T1_ITEM.get(), "block/raw_coal_t1_ore");
        registerBlockItemParent(blockModels, JDTRegistration.RawCoal_T2_ITEM.get(), "block/raw_coal_t2_ore");
        registerBlockItemParent(blockModels, JDTRegistration.RawCoal_T3_ITEM.get(), "block/raw_coal_t3_ore");
        registerBlockItemParent(blockModels, JDTRegistration.RawCoal_T4_ITEM.get(), "block/raw_coal_t4_ore");
        registerBlockItemParent(blockModels, JDTRegistration.CoalBlock_T1_ITEM.get(), "block/coalblock_t1");
        registerBlockItemParent(blockModels, JDTRegistration.CoalBlock_T2_ITEM.get(), "block/coalblock_t2");
        registerBlockItemParent(blockModels, JDTRegistration.CoalBlock_T3_ITEM.get(), "block/coalblock_t3");
        registerBlockItemParent(blockModels, JDTRegistration.CoalBlock_T4_ITEM.get(), "block/coalblock_t4");
        registerBlockItemParent(blockModels, JDTRegistration.BlockSwapperT1_ITEM.get(), "block/blockswappert1");
        registerBlockItemParent(blockModels, JDTRegistration.BlockSwapperT2_ITEM.get(), "block/blockswappert2");
        registerBlockItemParent(blockModels, JDTRegistration.PlayerAccessor_ITEM.get(), "block/playeraccessor");
        registerBlockItemParent(blockModels, JDTRegistration.FluidPlacerT1_ITEM.get(), "block/fluidplacert1");
        registerBlockItemParent(blockModels, JDTRegistration.FluidPlacerT2_ITEM.get(), "block/fluidplacert2");
        registerBlockItemParent(blockModels, JDTRegistration.FluidCollectorT1_ITEM.get(), "block/fluidcollectort1");
        registerBlockItemParent(blockModels, JDTRegistration.FluidCollectorT2_ITEM.get(), "block/fluidcollectort2");
        registerBlockItemParent(blockModels, JDTRegistration.TimeCrystalBlock_ITEM.get(), "block/time_crystal_block");
        registerBlockItemParent(blockModels, JDTRegistration.TimeCrystalBuddingBlock_ITEM.get(), "block/time_crystal_budding_block_state_0");
        registerBlockItemParent(blockModels, JDTRegistration.TimeCrystalCluster_ITEM.get(), "block/time_crystal_cluster");
        registerBlockItemParent(blockModels, JDTRegistration.TimeCrystalCluster_Small_ITEM.get(), "block/time_crystal_cluster_small");
        registerBlockItemParent(blockModels, JDTRegistration.TimeCrystalCluster_Medium_ITEM.get(), "block/time_crystal_cluster_medium");
        registerBlockItemParent(blockModels, JDTRegistration.TimeCrystalCluster_Large_ITEM.get(), "block/time_crystal_cluster_large");
        registerBlockItemParent(blockModels, JDTRegistration.ParadoxMachine_ITEM.get(), "block/paradoxmachine");
        registerBlockItemParent(blockModels, JDTRegistration.InventoryHolder_ITEM.get(), "block/inventory_holder");
        registerBlockItemParent(blockModels, JDTRegistration.ExperienceHolder_ITEM.get(), "block/experienceholder");
        // GooPatternBlock has no BlockItem (creative-only placement), so nothing to register.

        // -----------------------------------------------------------------
        // PLAIN ITEM MODELS (flat_item / handheld)
        // -----------------------------------------------------------------
        itemModels.generateFlatItem(JDTRegistration.Fuel_Canister.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JDTRegistration.RawFerricore.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JDTRegistration.FerricoreIngot.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JDTRegistration.RawBlazegold.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JDTRegistration.BlazegoldIngot.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JDTRegistration.Celestigem.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JDTRegistration.RawEclipseAlloy.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JDTRegistration.EclipseAlloyIngot.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JDTRegistration.Coal_T1.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JDTRegistration.Coal_T2.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JDTRegistration.Coal_T3.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JDTRegistration.Coal_T4.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JDTRegistration.PolymorphicCatalyst.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JDTRegistration.PortalFluidCatalyst.get(), ModelTemplates.FLAT_ITEM);
        // Time Crystal uses the time_crystal_shard.png texture (registered id is "time_crystal").
        ModelTemplates.FLAT_ITEM.create(
                ModelLocationUtils.getModelLocation(JDTRegistration.TimeCrystal.get()),
                new TextureMapping().put(TextureSlot.LAYER0, new Material(modLoc("item/time_crystal_shard"))),
                itemModels.modelOutput);
        itemModels.itemModelOutput.accept(JDTRegistration.TimeCrystal.get(),
                ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(JDTRegistration.TimeCrystal.get())));
        itemModels.generateFlatItem(JDTRegistration.TotemOfDeathRecall.get(), ModelTemplates.FLAT_ITEM);
        // Machine Settings Copier: registered id is "machinesettingscopier" but the texture is "machine_settings_copier.png".
        ModelTemplates.FLAT_ITEM.create(
                ModelLocationUtils.getModelLocation(JDTRegistration.MachineSettingsCopier.get()),
                new TextureMapping().put(TextureSlot.LAYER0, new Material(modLoc("item/machine_settings_copier"))),
                itemModels.modelOutput);
        itemModels.itemModelOutput.accept(JDTRegistration.MachineSettingsCopier.get(),
                ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(JDTRegistration.MachineSettingsCopier.get())));
        itemModels.generateFlatItem(JDTRegistration.TEMPLATE_FERRICORE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JDTRegistration.TEMPLATE_BLAZEGOLD.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JDTRegistration.TEMPLATE_CELESTIGEM.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JDTRegistration.TEMPLATE_ECLIPSEALLOY.get(), ModelTemplates.FLAT_ITEM);

        // Handheld wands / tools (flat_handheld)
        itemModels.generateFlatItem(JDTRegistration.FerricoreWrench.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(JDTRegistration.BlazejetWand.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(JDTRegistration.VoidshiftWand.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(JDTRegistration.EclipsegateWand.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(JDTRegistration.TimeWand.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(JDTRegistration.PolymorphicWand.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(JDTRegistration.PolymorphicWandV2.get(), ModelTemplates.FLAT_HANDHELD_ITEM);

        // CreatureCatcher uses a hand-written Blockbench model at models/item/creaturecatcher_base.json.
        // The client item JSON composites the base model with a special renderer that draws the
        // captured mob on top of the catcher when one is stored in the ItemStack.
        itemModels.itemModelOutput.accept(JDTRegistration.CreatureCatcher.get(),
                ItemModelUtils.composite(
                        ItemModelUtils.plainModel(modLoc("item/creaturecatcher_base")),
                        ItemModelUtils.specialModel(modLoc("item/creaturecatcher_base"),
                                new CreatureCatcherSpecialRenderer.Unbaked())));

        // Upgrades (flat_item, all textures under item/abilityupgrades/<suffix>)
        for (var upgrade : JDTRegistration.UPGRADES.getEntries()) {
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

        // Tools: enabled-state texture swap via ConditionalItemModelProperty dispatch.
        // Emit two handheld models per tool: <tool> (disabled/default) and <tool>_active (enabled).
        for (var tool : JDTRegistration.TOOLS.getEntries()) {
            emitEnabledSwapItem(itemModels, tool.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        }
        // Pocket generator — enabled-state swap, flat handheld.
        emitEnabledSwapItem(itemModels, JDTRegistration.Pocket_Generator.get(), ModelTemplates.FLAT_HANDHELD_ITEM);

        // Fluid canister (8 fullness levels) — RangeSelectItemModelProperty dispatch with tint source on the fluid layer.
        emitFluidCanisterModels(itemModels);

        // Potion canister (4 fullness levels) — RangeSelectItemModelProperty dispatch with potion tint source.
        emitPotionCanisterModels(itemModels);

        // Portal Gun V1 — uses a hand-written Blockbench model at models/item/portalgun.json.
        itemModels.itemModelOutput.accept(JDTRegistration.PortalGun.get(),
                ItemModelUtils.plainModel(modLoc("item/portalgun")));

        // Portal Gun V2 (4 fullness levels: 0/33/66/100) — reuses existing hand-written models.
        emitPortalGunV2Model(itemModels);

        // Bows: full pulling-stage dispatch via vanilla generateBow
        for (var bow : JDTRegistration.BOWS.getEntries()) {
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
        for (var armor : JDTRegistration.ARMORS.getEntries()) {
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

        // Buckets: neoforge:fluid_container dynamic model.
        // Shell from vanilla item/bucket, fluid mask from neoforge item/mask/bucket_fluid_drip.
        // The registered FluidModel's fluidTintSource supplies the tint automatically.
        for (var bucket : JDTRegistration.BUCKET_ITEMS.getEntries()) {
            Item bucketItem = bucket.get();
            net.minecraft.world.level.material.Fluid fluid = ((net.minecraft.world.item.BucketItem) bucketItem).content;
            Material bucketShell = new Material(Identifier.withDefaultNamespace("item/bucket"));
            Material fluidMask = new Material(Identifier.fromNamespaceAndPath("neoforge", "item/mask/bucket_fluid_drip"));
            net.neoforged.neoforge.client.model.item.DynamicFluidContainerModel.Textures textures =
                    new net.neoforged.neoforge.client.model.item.DynamicFluidContainerModel.Textures(
                            Optional.empty(),
                            Optional.of(bucketShell),
                            Optional.of(fluidMask),
                            Optional.empty());
            itemModels.itemModelOutput.accept(bucketItem,
                    new net.neoforged.neoforge.client.model.item.DynamicFluidContainerModel.Unbaked(
                            textures, fluid, false, false, true));
        }
    }

    // -----------------------------------------------------------------
    // Client-items helpers (enabled swap, fluid/potion fullness, portal gun)
    // -----------------------------------------------------------------

    private static void emitEnabledSwapItem(ItemModelGenerators itemModels, Item item, ModelTemplate template) {
        // Base (disabled) model: item/<id>.png
        Identifier baseModelId = ModelLocationUtils.getModelLocation(item);
        template.create(baseModelId, TextureMapping.layer0(item), itemModels.modelOutput);
        // Active (enabled) model: item/<id>_active.png
        Identifier activeModelId = ModelLocationUtils.getModelLocation(item, "_active");
        String itemPath = BuiltInRegistries.ITEM.getKey(item).getPath();
        template.create(activeModelId,
                new TextureMapping().put(TextureSlot.LAYER0, new Material(modLoc("item/" + itemPath + "_active"))),
                itemModels.modelOutput);
        itemModels.itemModelOutput.accept(item,
                ItemModelUtils.conditional(new ToolEnabledProperty(),
                        ItemModelUtils.plainModel(activeModelId),
                        ItemModelUtils.plainModel(baseModelId)));
    }

    private static void emitFluidCanisterModels(ItemModelGenerators itemModels) {
        // neoforge:fluid_container is an *item-model type* (goes into items/*.json), not a block-model loader.
        // Emit one DynamicFluidContainerModel.Unbaked per fullness level, each with a different mask texture.
        Item canister = JDTRegistration.FluidCanister.get();
        Material baseTex = new Material(modLoc("item/fluidcanister/fluid_canister"));

        // Empty (fullness 0): base shell only. Use layer_0 mask, which is the empty canister sprite.
        ItemModel.Unbaked fallback = fluidCanisterLevelModel(baseTex, 0);

        // Fullness 1-8: each level uses its matching fluid_canister_fluid_layer_N mask.
        List<RangeSelectItemModel.Entry> entries = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            entries.add(ItemModelUtils.override(fluidCanisterLevelModel(baseTex, i), i));
        }

        itemModels.itemModelOutput.accept(canister,
                ItemModelUtils.rangeSelect(new FluidCanisterFullnessProperty(), fallback, entries));
    }

    private static ItemModel.Unbaked fluidCanisterLevelModel(Material base, int level) {
        Material mask = new Material(modLoc("item/fluidcanister/fluid_canister_fluid_layer_" + level));
        net.neoforged.neoforge.client.model.item.DynamicFluidContainerModel.Textures textures =
                new net.neoforged.neoforge.client.model.item.DynamicFluidContainerModel.Textures(
                        Optional.empty(),
                        Optional.of(base),
                        Optional.of(mask),
                        Optional.empty());
        // Default fluid = minecraft:empty so the empty canister renders only the base shell.
        // When filled, the model reads the actual fluid from the item's fluid capability.
        return new net.neoforged.neoforge.client.model.item.DynamicFluidContainerModel.Unbaked(
                textures, net.minecraft.world.level.material.Fluids.EMPTY, true, false, true);
    }

    private static void emitPotionCanisterModels(ItemModelGenerators itemModels) {
        // Textures live directly at item/potion_canister.png and item/potion_canister_layer_{0..4}.png
        // (no subdirectory — matches the 1.21.1 layout).
        Item canister = JDTRegistration.PotionCanister.get();
        ItemTintSource tint = new PotionCanisterTintSource();
        Material shell = new Material(modLoc("item/potion_canister"));

        // Empty (fullness 0): shell + layer_0 (plain fluid sprite, no tint needed).
        Identifier emptyModelId = ModelLocationUtils.getModelLocation(canister);
        TextureMapping emptyMapping = new TextureMapping()
                .put(TextureSlot.LAYER0, shell)
                .put(TextureSlot.LAYER1, new Material(modLoc("item/potion_canister_layer_0")));
        ModelTemplates.TWO_LAYERED_ITEM.create(emptyModelId, emptyMapping, itemModels.modelOutput);

        // Fullness 1-4: shell + tinted layer_N.
        List<RangeSelectItemModel.Entry> entries = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            Identifier levelModelId = ModelLocationUtils.getModelLocation(canister, "_" + i);
            TextureMapping mapping = new TextureMapping()
                    .put(TextureSlot.LAYER0, shell)
                    .put(TextureSlot.LAYER1, new Material(modLoc("item/potion_canister_layer_" + i)));
            ModelTemplates.TWO_LAYERED_ITEM.create(levelModelId, mapping, itemModels.modelOutput);
            ItemModel.Unbaked submodel = ItemModelUtils.tintedModel(levelModelId, ItemModelUtils.constantTint(-1), tint);
            entries.add(ItemModelUtils.override(submodel, i));
        }

        ItemModel.Unbaked fallback = ItemModelUtils.plainModel(emptyModelId);
        itemModels.itemModelOutput.accept(canister,
                ItemModelUtils.rangeSelect(new PotionCanisterFullnessProperty(), fallback, entries));
    }

    private static void emitPortalGunV2Model(ItemModelGenerators itemModels) {
        // PortalGunV2 uses hand-written Blockbench models (portalgun_v2_0/33/66/100.json) at assets/justdirethings/models/item/.
        // The fullness property returns 0/1/2/3; emit a range-select dispatch pointing at those models.
        Item gun = JDTRegistration.PortalGunV2.get();
        ItemModel.Unbaked m0 = ItemModelUtils.plainModel(modLoc("item/portalgun_v2_0"));
        ItemModel.Unbaked m1 = ItemModelUtils.plainModel(modLoc("item/portalgun_v2_33"));
        ItemModel.Unbaked m2 = ItemModelUtils.plainModel(modLoc("item/portalgun_v2_66"));
        ItemModel.Unbaked m3 = ItemModelUtils.plainModel(modLoc("item/portalgun_v2_100"));
        itemModels.itemModelOutput.accept(gun,
                ItemModelUtils.rangeSelect(new PortalGunFullnessProperty(), m0,
                        ItemModelUtils.override(m1, 1),
                        ItemModelUtils.override(m2, 2),
                        ItemModelUtils.override(m3, 3)));
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
        blockModels.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(block, variant).with(BlockModelGenerators.ROTATIONS_COLUMN_WITH_FACING));
    }

    // Custom texture slot named "palette" matching the hand-written raw_ore_template.json.
    private static final TextureSlot PALETTE_SLOT = TextureSlot.create("palette");

    private static void gooPatternBlock(BlockModelGenerators blockModels) {
        Block block = JDTRegistration.GooPatternBlock.get();
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
        Block block = JDTRegistration.TimeCrystalBuddingBlock.get();
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
        for (var sidedBlock : JDTRegistration.SIDEDBLOCKS.getEntries()) {
            Block block = sidedBlock.get();
            String path = sidedBlock.getId().getPath();
            if (sidedBlock.equals(JDTRegistration.BlockBreakerT1)) {
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
                MultiVariantGenerator.dispatch(block, model).with(BlockModelGenerators.ROTATIONS_COLUMN_WITH_FACING));
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
                        .with(BlockModelGenerators.ROTATIONS_COLUMN_WITH_FACING));
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
