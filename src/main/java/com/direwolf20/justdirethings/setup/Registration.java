package com.direwolf20.justdirethings.setup;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.common.blockentities.*;
import com.direwolf20.justdirethings.common.blockentities.basebe.BaseMachineBE;
import com.direwolf20.justdirethings.common.blockentities.basebe.PoweredMachineBE;
import com.direwolf20.justdirethings.common.blockentities.gooblocks.GooBlockBE_Tier1;
import com.direwolf20.justdirethings.common.blockentities.gooblocks.GooBlockBE_Tier2;
import com.direwolf20.justdirethings.common.blockentities.gooblocks.GooBlockBE_Tier3;
import com.direwolf20.justdirethings.common.blockentities.gooblocks.GooBlockBE_Tier4;
import com.direwolf20.justdirethings.common.blocks.*;
import com.direwolf20.justdirethings.common.blocks.gooblocks.*;
import com.direwolf20.justdirethings.common.blocks.resources.*;
import com.direwolf20.justdirethings.common.blocks.soil.GooSoilTier1;
import com.direwolf20.justdirethings.common.blocks.soil.GooSoilTier2;
import com.direwolf20.justdirethings.common.blocks.soil.GooSoilTier3;
import com.direwolf20.justdirethings.common.blocks.soil.GooSoilTier4;
import com.direwolf20.justdirethings.common.capabilities.EnergyStorageNoReceive;
import com.direwolf20.justdirethings.common.capabilities.MachineEnergyStorage;
import com.direwolf20.justdirethings.common.containers.*;
import com.direwolf20.justdirethings.common.containers.handlers.FilterBasicHandler;
import com.direwolf20.justdirethings.common.items.*;
import com.direwolf20.justdirethings.common.items.resources.*;
import com.direwolf20.justdirethings.common.items.tools.*;
import com.direwolf20.justdirethings.common.items.tools.utils.PoweredTool;
import com.direwolf20.justdirethings.datagen.recipes.GooSpreadRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

import static com.direwolf20.justdirethings.JustDireThings.MODID;
import static com.direwolf20.justdirethings.client.particles.ModParticles.PARTICLE_TYPES;

public class Registration {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister.Blocks SIDEDBLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister.Items TOOLS = DeferredRegister.createItems(MODID);
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MODID);
    private static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(Registries.MENU, MODID);
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, MODID);

    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, MODID);
    public static final Supplier<RecipeType<GooSpreadRecipe>> GOO_SPREAD_RECIPE_TYPE = RECIPE_TYPES.register("goospreadrecipe", () -> RecipeType.simple(new ResourceLocation(MODID, "goospreadrecipe")));

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, JustDireThings.MODID);
    public static final Supplier<GooSpreadRecipe.Serializer> GOO_SPREAD_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("goospread", GooSpreadRecipe.Serializer::new);

    public static void init(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        SIDEDBLOCKS.register(eventBus);
        ITEMS.register(eventBus);
        TOOLS.register(eventBus);
        BLOCK_ENTITIES.register(eventBus);
        CONTAINERS.register(eventBus);
        ATTACHMENT_TYPES.register(eventBus);
        RECIPE_SERIALIZERS.register(eventBus);
        RECIPE_TYPES.register(eventBus);
        PARTICLE_TYPES.register(eventBus);
    }

    //Gooblocks
    public static final DeferredHolder<Block, GooBlock_Tier1> GooBlock_Tier1 = BLOCKS.register("gooblock_tier1", GooBlock_Tier1::new);
    public static final DeferredHolder<Item, BlockItem> GooBlock_Tier1_ITEM = ITEMS.register("gooblock_tier1", () -> new BlockItem(GooBlock_Tier1.get(), new Item.Properties()));
    public static final DeferredHolder<Block, GooBlock_Tier2> GooBlock_Tier2 = BLOCKS.register("gooblock_tier2", GooBlock_Tier2::new);
    public static final DeferredHolder<Item, BlockItem> GooBlock_Tier2_ITEM = ITEMS.register("gooblock_tier2", () -> new BlockItem(GooBlock_Tier2.get(), new Item.Properties()));
    public static final DeferredHolder<Block, GooBlock_Tier3> GooBlock_Tier3 = BLOCKS.register("gooblock_tier3", GooBlock_Tier3::new);
    public static final DeferredHolder<Item, BlockItem> GooBlock_Tier3_ITEM = ITEMS.register("gooblock_tier3", () -> new BlockItem(GooBlock_Tier3.get(), new Item.Properties()));
    public static final DeferredHolder<Block, GooBlock_Tier4> GooBlock_Tier4 = BLOCKS.register("gooblock_tier4", GooBlock_Tier4::new);
    public static final DeferredHolder<Item, BlockItem> GooBlock_Tier4_ITEM = ITEMS.register("gooblock_tier4", () -> new BlockItem(GooBlock_Tier4.get(), new Item.Properties()));

    public static final DeferredHolder<Block, GooPatternBlock> GooPatternBlock = BLOCKS.register("goopatternblock", GooPatternBlock::new);


    //Blocks
    public static final DeferredHolder<Block, GooSoilTier1> GooSoil_Tier1 = BLOCKS.register("goosoil_tier1", GooSoilTier1::new);
    public static final DeferredHolder<Item, BlockItem> GooSoil_ITEM_Tier1 = ITEMS.register("goosoil_tier1", () -> new BlockItem(GooSoil_Tier1.get(), new Item.Properties()));
    public static final DeferredHolder<Block, GooSoilTier2> GooSoil_Tier2 = BLOCKS.register("goosoil_tier2", GooSoilTier2::new);
    public static final DeferredHolder<Item, BlockItem> GooSoil_ITEM_Tier2 = ITEMS.register("goosoil_tier2", () -> new BlockItem(GooSoil_Tier2.get(), new Item.Properties()));
    public static final DeferredHolder<Block, GooSoilTier3> GooSoil_Tier3 = BLOCKS.register("goosoil_tier3", GooSoilTier3::new);
    public static final DeferredHolder<Item, BlockItem> GooSoil_ITEM_Tier3 = ITEMS.register("goosoil_tier3", () -> new BlockItem(GooSoil_Tier3.get(), new Item.Properties()));
    public static final DeferredHolder<Block, GooSoilTier4> GooSoil_Tier4 = BLOCKS.register("goosoil_tier4", GooSoilTier4::new);
    public static final DeferredHolder<Item, BlockItem> GooSoil_ITEM_Tier4 = ITEMS.register("goosoil_tier4", () -> new BlockItem(GooSoil_Tier4.get(), new Item.Properties()));

    //Machines
    public static final DeferredHolder<Block, ItemCollector> ItemCollector = BLOCKS.register("itemcollector", ItemCollector::new);
    public static final DeferredHolder<Item, BlockItem> ItemCollector_ITEM = ITEMS.register("itemcollector", () -> new BlockItem(ItemCollector.get(), new Item.Properties()));
    public static final DeferredHolder<Block, BlockBreakerT1> BlockBreakerT1 = SIDEDBLOCKS.register("blockbreakert1", BlockBreakerT1::new);
    public static final DeferredHolder<Item, BlockItem> BlockBreakerT1_ITEM = ITEMS.register("blockbreakert1", () -> new BlockItem(BlockBreakerT1.get(), new Item.Properties()));
    public static final DeferredHolder<Block, BlockBreakerT2> BlockBreakerT2 = BLOCKS.register("blockbreakert2", BlockBreakerT2::new);
    public static final DeferredHolder<Item, BlockItem> BlockBreakerT2_ITEM = ITEMS.register("blockbreakert2", () -> new BlockItem(BlockBreakerT2.get(), new Item.Properties()));
    public static final DeferredHolder<Block, BlockPlacerT1> BlockPlacerT1 = SIDEDBLOCKS.register("blockplacert1", BlockPlacerT1::new);
    public static final DeferredHolder<Item, BlockItem> BlockPlacerT1_ITEM = ITEMS.register("blockplacert1", () -> new BlockItem(BlockPlacerT1.get(), new Item.Properties()));
    public static final DeferredHolder<Block, BlockPlacerT2> BlockPlacerT2 = BLOCKS.register("blockplacert2", BlockPlacerT2::new);
    public static final DeferredHolder<Item, BlockItem> BlockPlacerT2_ITEM = ITEMS.register("blockplacert2", () -> new BlockItem(BlockPlacerT2.get(), new Item.Properties()));
    public static final DeferredHolder<Block, ClickerT1> ClickerT1 = SIDEDBLOCKS.register("clickert1", ClickerT1::new);
    public static final DeferredHolder<Item, BlockItem> ClickerT1_ITEM = ITEMS.register("clickert1", () -> new BlockItem(ClickerT1.get(), new Item.Properties()));
    public static final DeferredHolder<Block, ClickerT2> ClickerT2 = BLOCKS.register("clickert2", ClickerT2::new);
    public static final DeferredHolder<Item, BlockItem> ClickerT2_ITEM = ITEMS.register("clickert2", () -> new BlockItem(ClickerT2.get(), new Item.Properties()));
    public static final DeferredHolder<Block, SensorT1> SensorT1 = SIDEDBLOCKS.register("sensort1", SensorT1::new);
    public static final DeferredHolder<Item, BlockItem> SensorT1_ITEM = ITEMS.register("sensort1", () -> new BlockItem(SensorT1.get(), new Item.Properties()));
    public static final DeferredHolder<Block, SensorT2> SensorT2 = BLOCKS.register("sensort2", SensorT2::new);
    public static final DeferredHolder<Item, BlockItem> SensorT2_ITEM = ITEMS.register("sensort2", () -> new BlockItem(SensorT2.get(), new Item.Properties()));
    public static final DeferredHolder<Block, DropperT1> DropperT1 = SIDEDBLOCKS.register("droppert1", DropperT1::new);
    public static final DeferredHolder<Item, BlockItem> DropperT1_ITEM = ITEMS.register("droppert1", () -> new BlockItem(DropperT1.get(), new Item.Properties()));
    public static final DeferredHolder<Block, DropperT2> DropperT2 = BLOCKS.register("droppert2", DropperT2::new);
    public static final DeferredHolder<Item, BlockItem> DropperT2_ITEM = ITEMS.register("droppert2", () -> new BlockItem(DropperT2.get(), new Item.Properties()));
    public static final DeferredHolder<Block, BlockSwapperT1> BlockSwapperT1 = SIDEDBLOCKS.register("blockswappert1", BlockSwapperT1::new);
    public static final DeferredHolder<Item, BlockItem> BlockSwapperT1_ITEM = ITEMS.register("blockswappert1", () -> new BlockItem(BlockSwapperT1.get(), new Item.Properties()));
    public static final DeferredHolder<Block, BlockSwapperT2> BlockSwapperT2 = BLOCKS.register("blockswappert2", BlockSwapperT2::new);
    public static final DeferredHolder<Item, BlockItem> BlockSwapperT2_ITEM = ITEMS.register("blockswappert2", () -> new BlockItem(BlockSwapperT2.get(), new Item.Properties()));

    //Power Machines
    public static final DeferredHolder<Block, GeneratorT1> GeneratorT1 = BLOCKS.register("generatort1", GeneratorT1::new);
    public static final DeferredHolder<Item, BlockItem> GeneratorT1_ITEM = ITEMS.register("generatort1", () -> new BlockItem(GeneratorT1.get(), new Item.Properties()));
    public static final DeferredHolder<Block, EnergyTransmitter> EnergyTransmitter = BLOCKS.register("energytransmitter", EnergyTransmitter::new);
    public static final DeferredHolder<Item, BlockItem> EnergyTransmitter_ITEM = ITEMS.register("energytransmitter", () -> new BlockItem(EnergyTransmitter.get(), new Item.Properties()));

    //Blocks - Raw Resources
    public static final DeferredHolder<Block, RawFerricoreOre> RawFerricoreOre = BLOCKS.register("raw_ferricore_ore", RawFerricoreOre::new);
    public static final DeferredHolder<Item, BlockItem> RawFerricoreOre_ITEM = ITEMS.register("raw_ferricore_ore", () -> new BlockItem(RawFerricoreOre.get(), new Item.Properties()));
    public static final DeferredHolder<Block, RawBlazegoldOre> RawBlazegoldOre = BLOCKS.register("raw_blazegold_ore", RawBlazegoldOre::new);
    public static final DeferredHolder<Item, BlockItem> RawBlazegoldOre_ITEM = ITEMS.register("raw_blazegold_ore", () -> new BlockItem(RawBlazegoldOre.get(), new Item.Properties()));
    public static final DeferredHolder<Block, RawCelestigemOre> RawCelestigemOre = BLOCKS.register("raw_celestigem_ore", RawCelestigemOre::new);
    public static final DeferredHolder<Item, BlockItem> RawCelestigemOre_ITEM = ITEMS.register("raw_celestigem_ore", () -> new BlockItem(RawCelestigemOre.get(), new Item.Properties()));
    public static final DeferredHolder<Block, RawEclipseAlloyOre> RawEclipseAlloyOre = BLOCKS.register("raw_eclipsealloy_ore", RawEclipseAlloyOre::new);
    public static final DeferredHolder<Item, BlockItem> RawEclipseAlloyOre_ITEM = ITEMS.register("raw_eclipsealloy_ore", () -> new BlockItem(RawEclipseAlloyOre.get(), new Item.Properties()));
    public static final DeferredHolder<Block, RawCoal_T1> RawCoal_T1 = BLOCKS.register("raw_coal_t1_ore", RawCoal_T1::new);
    public static final DeferredHolder<Item, BlockItem> RawCoal_T1_ITEM = ITEMS.register("raw_coal_t1_ore", () -> new BlockItem(RawCoal_T1.get(), new Item.Properties()));
    public static final DeferredHolder<Block, RawCoal_T2> RawCoal_T2 = BLOCKS.register("raw_coal_t2_ore", RawCoal_T2::new);
    public static final DeferredHolder<Item, BlockItem> RawCoal_T2_ITEM = ITEMS.register("raw_coal_t2_ore", () -> new BlockItem(RawCoal_T2.get(), new Item.Properties()));
    public static final DeferredHolder<Block, RawCoal_T3> RawCoal_T3 = BLOCKS.register("raw_coal_t3_ore", RawCoal_T3::new);
    public static final DeferredHolder<Item, BlockItem> RawCoal_T3_ITEM = ITEMS.register("raw_coal_t3_ore", () -> new BlockItem(RawCoal_T3.get(), new Item.Properties()));
    public static final DeferredHolder<Block, RawCoal_T4> RawCoal_T4 = BLOCKS.register("raw_coal_t4_ore", RawCoal_T4::new);
    public static final DeferredHolder<Item, BlockItem> RawCoal_T4_ITEM = ITEMS.register("raw_coal_t4_ore", () -> new BlockItem(RawCoal_T4.get(), new Item.Properties()));

    //Blocks Consolidated Resources
    public static final DeferredHolder<Block, FerricoreBlock> FerricoreBlock = BLOCKS.register("ferricore_block", FerricoreBlock::new);
    public static final DeferredHolder<Item, BlockItem> FerricoreBlock_ITEM = ITEMS.register("ferricore_block", () -> new BlockItem(FerricoreBlock.get(), new Item.Properties()));
    public static final DeferredHolder<Block, BlazeGoldBlock> BlazeGoldBlock = BLOCKS.register("blazegold_block", BlazeGoldBlock::new);
    public static final DeferredHolder<Item, BlockItem> BlazeGoldBlock_ITEM = ITEMS.register("blazegold_block", () -> new BlockItem(BlazeGoldBlock.get(), new Item.Properties()));
    public static final DeferredHolder<Block, CelestigemBlock> CelestigemBlock = BLOCKS.register("celestigem_block", CelestigemBlock::new);
    public static final DeferredHolder<Item, BlockItem> CelestigemBlock_ITEM = ITEMS.register("celestigem_block", () -> new BlockItem(CelestigemBlock.get(), new Item.Properties()));
    public static final DeferredHolder<Block, EclipseAlloyBlock> EclipseAlloyBlock = BLOCKS.register("eclipsealloy_block", EclipseAlloyBlock::new);
    public static final DeferredHolder<Item, BlockItem> EclipseAlloyBlock_ITEM = ITEMS.register("eclipsealloy_block", () -> new BlockItem(EclipseAlloyBlock.get(), new Item.Properties()));
    public static final DeferredHolder<Block, CoalBlock_T1> CoalBlock_T1 = BLOCKS.register("coalblock_t1", CoalBlock_T1::new);
    public static final DeferredHolder<Item, BlockItem> CoalBlock_T1_ITEM = ITEMS.register("coalblock_t1", () -> new BlockItem(CoalBlock_T1.get(), new Item.Properties()));
    public static final DeferredHolder<Block, CoalBlock_T2> CoalBlock_T2 = BLOCKS.register("coalblock_t2", CoalBlock_T2::new);
    public static final DeferredHolder<Item, BlockItem> CoalBlock_T2_ITEM = ITEMS.register("coalblock_t2", () -> new BlockItem(CoalBlock_T2.get(), new Item.Properties()));
    public static final DeferredHolder<Block, CoalBlock_T3> CoalBlock_T3 = BLOCKS.register("coalblock_t3", CoalBlock_T3::new);
    public static final DeferredHolder<Item, BlockItem> CoalBlock_T3_ITEM = ITEMS.register("coalblock_t3", () -> new BlockItem(CoalBlock_T3.get(), new Item.Properties()));
    public static final DeferredHolder<Block, CoalBlock_T4> CoalBlock_T4 = BLOCKS.register("coalblock_t4", CoalBlock_T4::new);
    public static final DeferredHolder<Item, BlockItem> CoalBlock_T4_ITEM = ITEMS.register("coalblock_t4", () -> new BlockItem(CoalBlock_T4.get(), new Item.Properties()));

    //BlockEntities (Not TileEntities - Honest)
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<GooBlockBE_Tier1>> GooBlockBE_Tier1 = BLOCK_ENTITIES.register("gooblock_tier1", () -> BlockEntityType.Builder.of(GooBlockBE_Tier1::new, GooBlock_Tier1.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<GooBlockBE_Tier2>> GooBlockBE_Tier2 = BLOCK_ENTITIES.register("gooblock_tier2", () -> BlockEntityType.Builder.of(GooBlockBE_Tier2::new, GooBlock_Tier2.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<GooBlockBE_Tier3>> GooBlockBE_Tier3 = BLOCK_ENTITIES.register("gooblock_tier3", () -> BlockEntityType.Builder.of(GooBlockBE_Tier3::new, GooBlock_Tier3.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<GooBlockBE_Tier4>> GooBlockBE_Tier4 = BLOCK_ENTITIES.register("gooblock_tier4", () -> BlockEntityType.Builder.of(GooBlockBE_Tier4::new, GooBlock_Tier4.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<GooSoilBE>> GooSoilBE = BLOCK_ENTITIES.register("goosoilbe", () -> BlockEntityType.Builder.of(GooSoilBE::new, GooSoil_Tier3.get(), GooSoil_Tier4.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ItemCollectorBE>> ItemCollectorBE = BLOCK_ENTITIES.register("itemcollectorbe", () -> BlockEntityType.Builder.of(ItemCollectorBE::new, ItemCollector.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockBreakerT1BE>> BlockBreakerT1BE = BLOCK_ENTITIES.register("blockbreakert1", () -> BlockEntityType.Builder.of(BlockBreakerT1BE::new, BlockBreakerT1.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockBreakerT2BE>> BlockBreakerT2BE = BLOCK_ENTITIES.register("blockbreakert2", () -> BlockEntityType.Builder.of(BlockBreakerT2BE::new, BlockBreakerT2.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockPlacerT1BE>> BlockPlacerT1BE = BLOCK_ENTITIES.register("blockplacert1", () -> BlockEntityType.Builder.of(BlockPlacerT1BE::new, BlockPlacerT1.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockPlacerT2BE>> BlockPlacerT2BE = BLOCK_ENTITIES.register("blockplacert2", () -> BlockEntityType.Builder.of(BlockPlacerT2BE::new, BlockPlacerT2.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ClickerT1BE>> ClickerT1BE = BLOCK_ENTITIES.register("clickert1", () -> BlockEntityType.Builder.of(ClickerT1BE::new, ClickerT1.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ClickerT2BE>> ClickerT2BE = BLOCK_ENTITIES.register("clickert2", () -> BlockEntityType.Builder.of(ClickerT2BE::new, ClickerT2.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SensorT1BE>> SensorT1BE = BLOCK_ENTITIES.register("sensort1be", () -> BlockEntityType.Builder.of(SensorT1BE::new, SensorT1.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SensorT2BE>> SensorT2BE = BLOCK_ENTITIES.register("sensort2be", () -> BlockEntityType.Builder.of(SensorT2BE::new, SensorT2.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<DropperT1BE>> DropperT1BE = BLOCK_ENTITIES.register("droppert1", () -> BlockEntityType.Builder.of(DropperT1BE::new, DropperT1.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<DropperT2BE>> DropperT2BE = BLOCK_ENTITIES.register("droppert2", () -> BlockEntityType.Builder.of(DropperT2BE::new, DropperT2.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<GeneratorT1BE>> GeneratorT1BE = BLOCK_ENTITIES.register("generatort1", () -> BlockEntityType.Builder.of(GeneratorT1BE::new, GeneratorT1.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<EnergyTransmitterBE>> EnergyTransmitterBE = BLOCK_ENTITIES.register("energytransmitter", () -> BlockEntityType.Builder.of(EnergyTransmitterBE::new, EnergyTransmitter.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockSwapperT1BE>> BlockSwapperT1BE = BLOCK_ENTITIES.register("blockswappert1", () -> BlockEntityType.Builder.of(BlockSwapperT1BE::new, BlockSwapperT1.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockSwapperT2BE>> BlockSwapperT2BE = BLOCK_ENTITIES.register("blockswappert2", () -> BlockEntityType.Builder.of(BlockSwapperT2BE::new, BlockSwapperT2.get()).build(null));

    //Items - Raw Resources
    public static final DeferredHolder<Item, RawFerricore> RawFerricore = ITEMS.register("raw_ferricore", RawFerricore::new);
    public static final DeferredHolder<Item, RawBlazegold> RawBlazegold = ITEMS.register("raw_blazegold", RawBlazegold::new);
    public static final DeferredHolder<Item, RawEclipseAlloy> RawEclipseAlloy = ITEMS.register("raw_eclipsealloy", RawEclipseAlloy::new);

    //Items - Resources
    public static final DeferredHolder<Item, FerricoreIngot> FerricoreIngot = ITEMS.register("ferricore_ingot", FerricoreIngot::new);
    public static final DeferredHolder<Item, BlazeGoldIngot> BlazegoldIngot = ITEMS.register("blazegold_ingot", BlazeGoldIngot::new);
    public static final DeferredHolder<Item, Celestigem> Celestigem = ITEMS.register("celestigem", Celestigem::new);
    public static final DeferredHolder<Item, EclipseAlloyIngot> EclipseAlloyIngot = ITEMS.register("eclipsealloy_ingot", EclipseAlloyIngot::new);
    public static final DeferredHolder<Item, Coal_T1> Coal_T1 = ITEMS.register("coal_t1", Coal_T1::new);
    public static final DeferredHolder<Item, Coal_T2> Coal_T2 = ITEMS.register("coal_t2", Coal_T2::new);
    public static final DeferredHolder<Item, Coal_T3> Coal_T3 = ITEMS.register("coal_t3", Coal_T3::new);
    public static final DeferredHolder<Item, Coal_T4> Coal_T4 = ITEMS.register("coal_t4", Coal_T4::new);

    //Items
    public static final DeferredHolder<Item, FuelCanister> Fuel_Canister = ITEMS.register("fuel_canister", FuelCanister::new);
    public static final DeferredHolder<Item, PocketGenerator> Pocket_Generator = ITEMS.register("pocket_generator", PocketGenerator::new);
    public static final DeferredHolder<Item, PocketGeneratorT2> Pocket_GeneratorT2 = ITEMS.register("pocket_generator_t2", PocketGeneratorT2::new);
    public static final DeferredHolder<Item, PocketGeneratorT3> Pocket_GeneratorT3 = ITEMS.register("pocket_generator_t3", PocketGeneratorT3::new);
    public static final DeferredHolder<Item, PocketGeneratorT4> Pocket_GeneratorT4 = ITEMS.register("pocket_generator_t4", PocketGeneratorT4::new);
    public static final DeferredHolder<Item, FerricoreWrench> FerricoreWrench = ITEMS.register("ferricore_wrench", FerricoreWrench::new);
    public static final DeferredHolder<Item, TotemOfDeathRecall> TotemOfDeathRecall = ITEMS.register("totem_of_death_recall", TotemOfDeathRecall::new);

    //Items - Tools
    public static final DeferredHolder<Item, FerricoreSword> FerricoreSword = TOOLS.register("ferricore_sword", FerricoreSword::new);
    public static final DeferredHolder<Item, FerricorePickaxe> FerricorePickaxe = TOOLS.register("ferricore_pickaxe", FerricorePickaxe::new);
    public static final DeferredHolder<Item, FerricoreShovel> FerricoreShovel = TOOLS.register("ferricore_shovel", FerricoreShovel::new);
    public static final DeferredHolder<Item, FerricoreAxe> FerricoreAxe = TOOLS.register("ferricore_axe", FerricoreAxe::new);
    public static final DeferredHolder<Item, FerricoreHoe> FerricoreHoe = TOOLS.register("ferricore_hoe", FerricoreHoe::new);
    public static final DeferredHolder<Item, BlazegoldSword> BlazegoldSword = TOOLS.register("blazegold_sword", BlazegoldSword::new);
    public static final DeferredHolder<Item, BlazegoldPickaxe> BlazegoldPickaxe = TOOLS.register("blazegold_pickaxe", BlazegoldPickaxe::new);
    public static final DeferredHolder<Item, BlazegoldShovel> BlazegoldShovel = TOOLS.register("blazegold_shovel", BlazegoldShovel::new);
    public static final DeferredHolder<Item, BlazegoldAxe> BlazegoldAxe = TOOLS.register("blazegold_axe", BlazegoldAxe::new);
    public static final DeferredHolder<Item, BlazegoldHoe> BlazegoldHoe = TOOLS.register("blazegold_hoe", BlazegoldHoe::new);
    public static final DeferredHolder<Item, CelestigemSword> CelestigemSword = TOOLS.register("celestigem_sword", CelestigemSword::new);
    public static final DeferredHolder<Item, CelestigemPickaxe> CelestigemPickaxe = TOOLS.register("celestigem_pickaxe", CelestigemPickaxe::new);
    public static final DeferredHolder<Item, CelestigemShovel> CelestigemShovel = TOOLS.register("celestigem_shovel", CelestigemShovel::new);
    public static final DeferredHolder<Item, CelestigemAxe> CelestigemAxe = TOOLS.register("celestigem_axe", CelestigemAxe::new);
    public static final DeferredHolder<Item, CelestigemHoe> CelestigemHoe = TOOLS.register("celestigem_hoe", CelestigemHoe::new);
    public static final DeferredHolder<Item, CelestigemPaxel> CelestigemPaxel = TOOLS.register("celestigem_paxel", CelestigemPaxel::new);
    public static final DeferredHolder<Item, EclipseAlloySword> EclipseAlloySword = TOOLS.register("eclipsealloy_sword", EclipseAlloySword::new);
    public static final DeferredHolder<Item, EclipseAlloyPickaxe> EclipseAlloyPickaxe = TOOLS.register("eclipsealloy_pickaxe", EclipseAlloyPickaxe::new);
    public static final DeferredHolder<Item, EclipseAlloyShovel> EclipseAlloyShovel = TOOLS.register("eclipsealloy_shovel", EclipseAlloyShovel::new);
    public static final DeferredHolder<Item, EclipseAlloyAxe> EclipseAlloyAxe = TOOLS.register("eclipsealloy_axe", EclipseAlloyAxe::new);
    public static final DeferredHolder<Item, EclipseAlloyHoe> EclipseAlloyHoe = TOOLS.register("eclipsealloy_hoe", EclipseAlloyHoe::new);
    public static final DeferredHolder<Item, EclipseAlloyPaxel> EclipseAlloyPaxel = TOOLS.register("eclipsealloy_paxel", EclipseAlloyPaxel::new);

    //Containers
    public static final DeferredHolder<MenuType<?>, MenuType<FuelCanisterContainer>> FuelCanister_Container = CONTAINERS.register("fuelcanister",
            () -> IMenuTypeExtension.create((windowId, inv, data) -> new FuelCanisterContainer(windowId, inv, inv.player, data)));
    public static final DeferredHolder<MenuType<?>, MenuType<PocketGeneratorContainer>> PocketGenerator_Container = CONTAINERS.register("pocketgenerator",
            () -> IMenuTypeExtension.create((windowId, inv, data) -> new PocketGeneratorContainer(windowId, inv, inv.player, data)));
    public static final DeferredHolder<MenuType<?>, MenuType<ToolSettingContainer>> Tool_Settings_Container = CONTAINERS.register("tool_settings",
            () -> IMenuTypeExtension.create((windowId, inv, data) -> new ToolSettingContainer(windowId, inv, inv.player, data)));
    public static final DeferredHolder<MenuType<?>, MenuType<ItemCollectorContainer>> Item_Collector_Container = CONTAINERS.register("item_collector_container",
            () -> IMenuTypeExtension.create(ItemCollectorContainer::new));
    public static final DeferredHolder<MenuType<?>, MenuType<BlockBreakerT1Container>> BlockBreakerT1_Container = CONTAINERS.register("blockbreakert1_container",
            () -> IMenuTypeExtension.create(BlockBreakerT1Container::new));
    public static final DeferredHolder<MenuType<?>, MenuType<BlockBreakerT2Container>> BlockBreakerT2_Container = CONTAINERS.register("blockbreakert2_container",
            () -> IMenuTypeExtension.create(BlockBreakerT2Container::new));
    public static final DeferredHolder<MenuType<?>, MenuType<BlockPlacerT1Container>> BlockPlacerT1_Container = CONTAINERS.register("blockplacert1_container",
            () -> IMenuTypeExtension.create(BlockPlacerT1Container::new));
    public static final DeferredHolder<MenuType<?>, MenuType<BlockPlacerT2Container>> BlockPlacerT2_Container = CONTAINERS.register("blockplacert2_container",
            () -> IMenuTypeExtension.create(BlockPlacerT2Container::new));
    public static final DeferredHolder<MenuType<?>, MenuType<ClickerT1Container>> ClickerT1_Container = CONTAINERS.register("clickert1_container",
            () -> IMenuTypeExtension.create(ClickerT1Container::new));
    public static final DeferredHolder<MenuType<?>, MenuType<ClickerT2Container>> ClickerT2_Container = CONTAINERS.register("clickert2_container",
            () -> IMenuTypeExtension.create(ClickerT2Container::new));
    public static final DeferredHolder<MenuType<?>, MenuType<SensorT1Container>> SensorT1_Container = CONTAINERS.register("sensort1_container",
            () -> IMenuTypeExtension.create(SensorT1Container::new));
    public static final DeferredHolder<MenuType<?>, MenuType<SensorT2Container>> SensorT2_Container = CONTAINERS.register("sensort2_container",
            () -> IMenuTypeExtension.create(SensorT2Container::new));
    public static final DeferredHolder<MenuType<?>, MenuType<DropperT1Container>> DropperT1_Container = CONTAINERS.register("droppert1_container",
            () -> IMenuTypeExtension.create(DropperT1Container::new));
    public static final DeferredHolder<MenuType<?>, MenuType<DropperT2Container>> DropperT2_Container = CONTAINERS.register("droppert2_container",
            () -> IMenuTypeExtension.create(DropperT2Container::new));
    public static final DeferredHolder<MenuType<?>, MenuType<GeneratorT1Container>> GeneratorT1_Container = CONTAINERS.register("generatort1_container",
            () -> IMenuTypeExtension.create(GeneratorT1Container::new));
    public static final DeferredHolder<MenuType<?>, MenuType<EnergyTransmitterContainer>> EnergyTransmitter_Container = CONTAINERS.register("energytransmitter_container",
            () -> IMenuTypeExtension.create(EnergyTransmitterContainer::new));
    public static final DeferredHolder<MenuType<?>, MenuType<BlockSwapperT1Container>> BlockSwapperT1_Container = CONTAINERS.register("blockswappert1_container",
            () -> IMenuTypeExtension.create(BlockSwapperT1Container::new));
    public static final DeferredHolder<MenuType<?>, MenuType<BlockSwapperT2Container>> BlockSwapperT2_Container = CONTAINERS.register("blockswappert2_container",
            () -> IMenuTypeExtension.create(BlockSwapperT2Container::new));

    //Data Attachments
    public static final Supplier<AttachmentType<ItemStackHandler>> HANDLER = ATTACHMENT_TYPES.register(
            "handler", () -> AttachmentType.serializable(() -> new ItemStackHandler(1)).build());
    public static final Supplier<AttachmentType<ItemStackHandler>> MACHINE_HANDLER = ATTACHMENT_TYPES.register(
            "machine_handler", () -> AttachmentType.serializable(holder -> {
                if (holder instanceof BaseMachineBE baseMachineBE)
                    return new ItemStackHandler(baseMachineBE.MACHINE_SLOTS);
                return new ItemStackHandler(1);
            }).build());
    public static final Supplier<AttachmentType<FilterBasicHandler>> HANDLER_BASIC_FILTER = ATTACHMENT_TYPES.register(
            "handler_item_collector", () -> AttachmentType.serializable(() -> new FilterBasicHandler(9)).build());
    public static final Supplier<AttachmentType<FilterBasicHandler>> HANDLER_BASIC_FILTER_ANYSIZE = ATTACHMENT_TYPES.register(
            "anysize_filter_handler", () -> AttachmentType.serializable(holder -> {
                if (holder instanceof BaseMachineBE baseMachineBE)
                    return new FilterBasicHandler(baseMachineBE.ANYSIZE_FILTER_SLOTS);
                return new FilterBasicHandler(0);
            }).build());

    public static final Supplier<AttachmentType<EnergyStorage>> ENERGYSTORAGE = ATTACHMENT_TYPES.register(
            "energystorage", () -> AttachmentType.serializable(holder -> {
                if (holder instanceof ItemStack itemStack) {
                    int capacity = 1000000; //Default
                    if (itemStack.getItem() instanceof PoweredTool poweredTool) {
                        capacity = poweredTool.getMaxEnergy();
                    }
                    return new EnergyStorage(capacity);
                } else {
                    throw new IllegalStateException("Cannot attach energy handler item to a non-item.");
                }
            }).build());
    public static final Supplier<AttachmentType<MachineEnergyStorage>> ENERGYSTORAGE_MACHINES = ATTACHMENT_TYPES.register(
            "energystorage_machines", () -> AttachmentType.serializable(holder -> {
                if (holder instanceof PoweredMachineBE feMachineBE) {
                    int capacity = feMachineBE.getMaxEnergy(); //Default
                    return new MachineEnergyStorage(capacity);
                } else {
                    throw new IllegalStateException("Cannot attach energy handler item to a non-PoweredMachine.");
                }
            }).build());
    public static final Supplier<AttachmentType<EnergyStorageNoReceive>> ENERGYSTORAGE_GENERATORS = ATTACHMENT_TYPES.register(
            "energystorage_generators", () -> AttachmentType.serializable(holder -> {
                if (holder instanceof PoweredMachineBE feMachineBE) {
                    int capacity = feMachineBE.getMaxEnergy(); //Default
                    return new EnergyStorageNoReceive(capacity);
                } else {
                    throw new IllegalStateException("Cannot attach energy handler item to a non-PoweredMachine.");
                }
            }).build());
    public static final Supplier<AttachmentType<EnergyStorageNoReceive>> ENERGYSTORAGENORECEIVE = ATTACHMENT_TYPES.register(
            "energystoragenoreceive", () -> AttachmentType.serializable(holder -> {
                if (holder instanceof ItemStack itemStack) {
                    int capacity = 1000000; //Default
                    if (itemStack.getItem() instanceof PoweredTool poweredTool) {
                        capacity = poweredTool.getMaxEnergy();
                    }
                    return new EnergyStorageNoReceive(capacity);
                } else {
                    throw new IllegalStateException("Cannot attach energy handler item to a non-item.");
                }
            }).build());
    public static final Supplier<AttachmentType<CompoundTag>> DEATH_DATA = ATTACHMENT_TYPES.register(
            "death_data",
            () -> AttachmentType.builder(CompoundTag::new).serialize(CompoundTag.CODEC).build()
    );
}
