package com.direwolf20.justdirethings.setup;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.common.blockentities.*;
import com.direwolf20.justdirethings.common.blockentities.basebe.BaseMachineBE;
import com.direwolf20.justdirethings.common.blockentities.basebe.FluidMachineBE;
import com.direwolf20.justdirethings.common.blockentities.basebe.PoweredMachineBE;
import com.direwolf20.justdirethings.common.blockentities.gooblocks.GooBlockBE_Tier1;
import com.direwolf20.justdirethings.common.blockentities.gooblocks.GooBlockBE_Tier2;
import com.direwolf20.justdirethings.common.blockentities.gooblocks.GooBlockBE_Tier3;
import com.direwolf20.justdirethings.common.blockentities.gooblocks.GooBlockBE_Tier4;
import com.direwolf20.justdirethings.common.blocks.*;
import com.direwolf20.justdirethings.common.blocks.baseblocks.BaseMachineBlock;
import com.direwolf20.justdirethings.common.blocks.gooblocks.*;
import com.direwolf20.justdirethings.common.blocks.resources.*;
import com.direwolf20.justdirethings.common.blocks.soil.GooSoilTier1;
import com.direwolf20.justdirethings.common.blocks.soil.GooSoilTier2;
import com.direwolf20.justdirethings.common.blocks.soil.GooSoilTier3;
import com.direwolf20.justdirethings.common.blocks.soil.GooSoilTier4;
import com.direwolf20.justdirethings.common.capabilities.*;
import com.direwolf20.justdirethings.common.containers.*;
import com.direwolf20.justdirethings.common.containers.handlers.FilterBasicHandler;
import com.direwolf20.justdirethings.common.entities.*;
import com.direwolf20.justdirethings.common.fluids.basefluids.RefinedFuel;
import com.direwolf20.justdirethings.common.fluids.polymorphicfluid.PolymorphicFluid;
import com.direwolf20.justdirethings.common.fluids.polymorphicfluid.PolymorphicFluidBlock;
import com.direwolf20.justdirethings.common.fluids.polymorphicfluid.PolymorphicFluidType;
import com.direwolf20.justdirethings.common.fluids.portalfluid.PortalFluid;
import com.direwolf20.justdirethings.common.fluids.portalfluid.PortalFluidBlock;
import com.direwolf20.justdirethings.common.fluids.portalfluid.PortalFluidType;
import com.direwolf20.justdirethings.common.fluids.refinedt2fuel.RefinedT2Fuel;
import com.direwolf20.justdirethings.common.fluids.refinedt2fuel.RefinedT2FuelBlock;
import com.direwolf20.justdirethings.common.fluids.refinedt2fuel.RefinedT2FuelType;
import com.direwolf20.justdirethings.common.fluids.refinedt3fuel.RefinedT3Fuel;
import com.direwolf20.justdirethings.common.fluids.refinedt3fuel.RefinedT3FuelBlock;
import com.direwolf20.justdirethings.common.fluids.refinedt3fuel.RefinedT3FuelType;
import com.direwolf20.justdirethings.common.fluids.refinedt4fuel.RefinedT4Fuel;
import com.direwolf20.justdirethings.common.fluids.refinedt4fuel.RefinedT4FuelBlock;
import com.direwolf20.justdirethings.common.fluids.refinedt4fuel.RefinedT4FuelType;
import com.direwolf20.justdirethings.common.fluids.timefluid.TimeFluid;
import com.direwolf20.justdirethings.common.fluids.timefluid.TimeFluidBlock;
import com.direwolf20.justdirethings.common.fluids.timefluid.TimeFluidType;
import com.direwolf20.justdirethings.common.fluids.unrefinedt2fuel.UnrefinedT2Fuel;
import com.direwolf20.justdirethings.common.fluids.unrefinedt2fuel.UnrefinedT2FuelBlock;
import com.direwolf20.justdirethings.common.fluids.unrefinedt2fuel.UnrefinedT2FuelType;
import com.direwolf20.justdirethings.common.fluids.unrefinedt3fuel.UnrefinedT3Fuel;
import com.direwolf20.justdirethings.common.fluids.unrefinedt3fuel.UnrefinedT3FuelBlock;
import com.direwolf20.justdirethings.common.fluids.unrefinedt3fuel.UnrefinedT3FuelType;
import com.direwolf20.justdirethings.common.fluids.unrefinedt4fuel.UnrefinedT4Fuel;
import com.direwolf20.justdirethings.common.fluids.unrefinedt4fuel.UnrefinedT4FuelBlock;
import com.direwolf20.justdirethings.common.fluids.unrefinedt4fuel.UnrefinedT4FuelType;
import com.direwolf20.justdirethings.common.fluids.unstableportalfluid.UnstablePortalFluid;
import com.direwolf20.justdirethings.common.fluids.unstableportalfluid.UnstablePortalFluidBlock;
import com.direwolf20.justdirethings.common.fluids.unstableportalfluid.UnstablePortalFluidType;
import com.direwolf20.justdirethings.common.fluids.xpfluid.XPFluid;
import com.direwolf20.justdirethings.common.fluids.xpfluid.XPFluidBlock;
import com.direwolf20.justdirethings.common.fluids.xpfluid.XPFluidType;
import com.direwolf20.justdirethings.common.items.*;
import com.direwolf20.justdirethings.common.items.abilityupgrades.Upgrade;
import com.direwolf20.justdirethings.common.items.abilityupgrades.UpgradeBlank;
import com.direwolf20.justdirethings.common.items.abilityupgrades.UpgradeTemplate;
import com.direwolf20.justdirethings.common.items.armors.*;
import com.direwolf20.justdirethings.common.items.armors.utils.ArmorTiers;
import com.direwolf20.justdirethings.common.items.datacomponents.JustDireDataComponents;
import com.direwolf20.justdirethings.common.items.resources.*;
import com.direwolf20.justdirethings.common.items.tools.*;
import com.direwolf20.justdirethings.common.items.tools.utils.GooTier;
import com.direwolf20.justdirethings.datagen.recipes.AbilityRecipe;
import com.direwolf20.justdirethings.datagen.recipes.FluidDropRecipe;
import com.direwolf20.justdirethings.datagen.recipes.GooSpreadRecipe;
import com.direwolf20.justdirethings.datagen.recipes.PaxelRecipe;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.common.world.chunk.TicketController;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;

import java.util.function.Supplier;

import static com.direwolf20.justdirethings.JustDireThings.MODID;
import static com.direwolf20.justdirethings.client.particles.ModParticles.PARTICLE_TYPES;

public class JDTRegistration {
    public static final TicketController TICKET_CONTROLLER = new TicketController(Identifier.fromNamespaceAndPath(MODID, "chunk_loader"), null);

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister.Blocks FLUID_BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.FLUID_TYPES, MODID);
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(Registries.FLUID, MODID);
    public static final DeferredRegister.Blocks SIDEDBLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister.Items BUCKET_ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister.Items TOOLS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister.Items BOWS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister.Items ARMORS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister.Items UPGRADES = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, MODID);
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MODID);
    private static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(Registries.MENU, MODID);
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, MODID);
    private static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(Registries.ATTRIBUTE, MODID);


    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, MODID);
    public static final Supplier<RecipeType<GooSpreadRecipe>> GOO_SPREAD_RECIPE_TYPE = RECIPE_TYPES.register("goospreadrecipe", () -> RecipeType.simple(Identifier.fromNamespaceAndPath(MODID, "goospreadrecipe")));
    public static final Supplier<RecipeType<FluidDropRecipe>> FLUID_DROP_RECIPE_TYPE = RECIPE_TYPES.register("fluiddroprecipe", () -> RecipeType.simple(Identifier.fromNamespaceAndPath(MODID, "fluiddroprecipe")));
    public static final Supplier<RecipeType<AbilityRecipe>> ABILITY_RECIPE_TYPE = RECIPE_TYPES.register("abilityrecipe", () -> RecipeType.simple(Identifier.fromNamespaceAndPath(MODID, "abilityrecipe")));
    public static final Supplier<RecipeType<PaxelRecipe>> PAXEL_RECIPE_TYPE = RECIPE_TYPES.register("paxelrecipe", () -> RecipeType.simple(Identifier.fromNamespaceAndPath(MODID, "paxelrecipe")));

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, JustDireThings.MODID);
    public static final Supplier<RecipeSerializer<GooSpreadRecipe>> GOO_SPREAD_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("goospread", () -> new RecipeSerializer<>(GooSpreadRecipe.CODEC, GooSpreadRecipe.STREAM_CODEC));
    public static final Supplier<RecipeSerializer<FluidDropRecipe>> FLUID_DROP_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("fluiddrop", () -> new RecipeSerializer<>(FluidDropRecipe.CODEC, FluidDropRecipe.STREAM_CODEC));
    public static final Supplier<RecipeSerializer<AbilityRecipe>> ABILITY_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("ability", () -> new RecipeSerializer<>(AbilityRecipe.CODEC, AbilityRecipe.STREAM_CODEC));
    public static final Supplier<RecipeSerializer<PaxelRecipe>> PAXEL_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("paxel", () -> new RecipeSerializer<>(PaxelRecipe.CODEC, PaxelRecipe.STREAM_CODEC));

    private static final DeferredRegister<SoundEvent> SOUND_REGISTRY = DeferredRegister.create(Registries.SOUND_EVENT, JustDireThings.MODID);
    public static final Supplier<SoundEvent> BEEP = SOUND_REGISTRY.register("beep", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(JustDireThings.MODID, "beep")));
    public static final Supplier<SoundEvent> PORTAL_GUN_CLOSE = SOUND_REGISTRY.register("portal_gun_close", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(JustDireThings.MODID, "portal_gun_close")));
    public static final Supplier<SoundEvent> PORTAL_GUN_OPEN = SOUND_REGISTRY.register("portal_gun_open", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(JustDireThings.MODID, "portal_gun_open")));
    public static final Supplier<SoundEvent> PARADOX_AMBIENT = SOUND_REGISTRY.register("paradox_ambient", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(JustDireThings.MODID, "paradox_ambient")));


    public static void init(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        FLUID_BLOCKS.register(eventBus);
        FLUID_TYPES.register(eventBus);
        FLUIDS.register(eventBus);
        SIDEDBLOCKS.register(eventBus);
        ITEMS.register(eventBus);
        BUCKET_ITEMS.register(eventBus);
        TOOLS.register(eventBus);
        BOWS.register(eventBus);
        BLOCK_ENTITIES.register(eventBus);
        CONTAINERS.register(eventBus);
        ATTACHMENT_TYPES.register(eventBus);
        RECIPE_SERIALIZERS.register(eventBus);
        RECIPE_TYPES.register(eventBus);
        PARTICLE_TYPES.register(eventBus);
        ENTITY_TYPES.register(eventBus);
        ARMORS.register(eventBus);
        SOUND_REGISTRY.register(eventBus);
        ATTRIBUTES.register(eventBus);
        UPGRADES.register(eventBus);

        JustDireDataComponents.genAbilityData();
        JustDireDataComponents.COMPONENTS.register(eventBus);

    }

    //Gooblocks
    private static Supplier<BlockBehaviour.Properties> gooBlockProps() {
        return () -> BlockBehaviour.Properties.of().sound(SoundType.FUNGUS).strength(2.0f).noOcclusion();
    }
    private static Supplier<BlockBehaviour.Properties> gooSoilProps() {
        return () -> BlockBehaviour.Properties.of().sound(SoundType.GRAVEL).strength(2.0f).randomTicks();
    }
    private static Supplier<BlockBehaviour.Properties> machineProps() {
        return () -> BlockBehaviour.Properties.of().sound(SoundType.METAL).strength(2.0f).isRedstoneConductor(BaseMachineBlock::never);
    }

    public static final DeferredHolder<Block, GooBlock_Tier1> GooBlock_Tier1 = BLOCKS.registerBlock("gooblock_tier1", GooBlock_Tier1::new, gooBlockProps());
    public static final DeferredHolder<Item, GooBlock_Item> GooBlock_Tier1_ITEM = ITEMS.registerItem("gooblock_tier1",
            props -> new GooBlock_Item(GooBlock_Tier1.get(), props), () -> new Item.Properties().useBlockDescriptionPrefix());
    public static final DeferredHolder<Block, GooBlock_Tier2> GooBlock_Tier2 = BLOCKS.registerBlock("gooblock_tier2", GooBlock_Tier2::new, gooBlockProps());
    public static final DeferredHolder<Item, GooBlock_Item> GooBlock_Tier2_ITEM = ITEMS.registerItem("gooblock_tier2",
            props -> new GooBlock_Item(GooBlock_Tier2.get(), props), () -> new Item.Properties().useBlockDescriptionPrefix());
    public static final DeferredHolder<Block, GooBlock_Tier3> GooBlock_Tier3 = BLOCKS.registerBlock("gooblock_tier3", GooBlock_Tier3::new, gooBlockProps());
    public static final DeferredHolder<Item, GooBlock_Item> GooBlock_Tier3_ITEM = ITEMS.registerItem("gooblock_tier3",
            props -> new GooBlock_Item(GooBlock_Tier3.get(), props), () -> new Item.Properties().useBlockDescriptionPrefix());
    public static final DeferredHolder<Block, GooBlock_Tier4> GooBlock_Tier4 = BLOCKS.registerBlock("gooblock_tier4", GooBlock_Tier4::new, gooBlockProps());
    public static final DeferredHolder<Item, GooBlock_Item> GooBlock_Tier4_ITEM = ITEMS.registerItem("gooblock_tier4",
            props -> new GooBlock_Item(GooBlock_Tier4.get(), props), () -> new Item.Properties().useBlockDescriptionPrefix());

    public static final DeferredHolder<Block, GooPatternBlock> GooPatternBlock = BLOCKS.registerBlock("goopatternblock", GooPatternBlock::new, gooBlockProps());


    //Blocks
    public static final DeferredHolder<Block, GooSoilTier1> GooSoil_Tier1 = BLOCKS.registerBlock("goosoil_tier1", GooSoilTier1::new, gooSoilProps());
    public static final DeferredHolder<Item, BlockItem> GooSoil_ITEM_Tier1 = ITEMS.registerSimpleBlockItem("goosoil_tier1", GooSoil_Tier1);
    public static final DeferredHolder<Block, GooSoilTier2> GooSoil_Tier2 = BLOCKS.registerBlock("goosoil_tier2", GooSoilTier2::new, gooSoilProps());
    public static final DeferredHolder<Item, BlockItem> GooSoil_ITEM_Tier2 = ITEMS.registerSimpleBlockItem("goosoil_tier2", GooSoil_Tier2);
    public static final DeferredHolder<Block, GooSoilTier3> GooSoil_Tier3 = BLOCKS.registerBlock("goosoil_tier3", GooSoilTier3::new, gooSoilProps());
    public static final DeferredHolder<Item, BlockItem> GooSoil_ITEM_Tier3 = ITEMS.registerSimpleBlockItem("goosoil_tier3", GooSoil_Tier3);
    public static final DeferredHolder<Block, GooSoilTier4> GooSoil_Tier4 = BLOCKS.registerBlock("goosoil_tier4", GooSoilTier4::new, gooSoilProps());
    public static final DeferredHolder<Item, BlockItem> GooSoil_ITEM_Tier4 = ITEMS.registerSimpleBlockItem("goosoil_tier4", GooSoil_Tier4);
    public static final DeferredHolder<Block, EclipseGateBlock> EclipseGateBlock = BLOCKS.registerBlock("eclipsegateblock", EclipseGateBlock::new,
            () -> BlockBehaviour.Properties.of().strength(20f, 3600000.0F).noOcclusion().noCollision().forceSolidOn().pushReaction(PushReaction.BLOCK));

    //Fluids
    //Polymorphic Fluid
    public static final DeferredHolder<FluidType, FluidType> POLYMORPHIC_FLUID_TYPE = FLUID_TYPES.register("polymorphic_fluid_type",
            PolymorphicFluidType::new);
    public static final DeferredHolder<Fluid, PolymorphicFluid> POLYMORPHIC_FLUID_FLOWING = FLUIDS.register("polymorphic_fluid_flowing",
            PolymorphicFluid.Flowing::new);
    public static final DeferredHolder<Fluid, PolymorphicFluid> POLYMORPHIC_FLUID_SOURCE = FLUIDS.register("polymorphic_fluid_source",
            PolymorphicFluid.Source::new);
    public static final DeferredHolder<Block, LiquidBlock> POLYMORPHIC_FLUID_BLOCK = FLUID_BLOCKS.registerBlock("polymorphic_fluid_block",
            PolymorphicFluidBlock::new,
            () -> BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).replaceable().noCollision().strength(100.0F)
                    .pushReaction(PushReaction.DESTROY).noLootTable().liquid().sound(SoundType.EMPTY));
    public static final DeferredHolder<Item, BucketItem> POLYMORPHIC_FLUID_BUCKET = BUCKET_ITEMS.registerItem("polymorphic_fluid_bucket",
            props -> new BucketItem(JDTRegistration.POLYMORPHIC_FLUID_SOURCE.get(), props),
            () -> new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1));

    //Portal Fluid
    public static final DeferredHolder<FluidType, FluidType> PORTAL_FLUID_TYPE = FLUID_TYPES.register("portal_fluid_type",
            PortalFluidType::new);
    public static final DeferredHolder<Fluid, PortalFluid> PORTAL_FLUID_FLOWING = FLUIDS.register("portal_fluid_flowing",
            PortalFluid.Flowing::new);
    public static final DeferredHolder<Fluid, PortalFluid> PORTAL_FLUID_SOURCE = FLUIDS.register("portal_fluid_source",
            PortalFluid.Source::new);
    public static final DeferredHolder<Block, LiquidBlock> PORTAL_FLUID_BLOCK = FLUID_BLOCKS.registerBlock("portal_fluid_block",
            PortalFluidBlock::new,
            () -> BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GREEN).replaceable().noCollision().strength(100.0F)
                    .pushReaction(PushReaction.DESTROY).noLootTable().liquid().sound(SoundType.EMPTY));
    public static final DeferredHolder<Item, BucketItem> PORTAL_FLUID_BUCKET = BUCKET_ITEMS.registerItem("portal_fluid_bucket",
            props -> new BucketItem(JDTRegistration.PORTAL_FLUID_SOURCE.get(), props),
            () -> new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1));

    //Time Fluid
    public static final DeferredHolder<FluidType, FluidType> TIME_FLUID_TYPE = FLUID_TYPES.register("time_fluid_type",
            TimeFluidType::new);
    public static final DeferredHolder<Fluid, TimeFluid> TIME_FLUID_FLOWING = FLUIDS.register("time_fluid_flowing",
            TimeFluid.Flowing::new);
    public static final DeferredHolder<Fluid, TimeFluid> TIME_FLUID_SOURCE = FLUIDS.register("time_fluid_source",
            TimeFluid.Source::new);
    public static final DeferredHolder<Block, LiquidBlock> TIME_FLUID_BLOCK = FLUID_BLOCKS.registerBlock("time_fluid_block",
            TimeFluidBlock::new,
            () -> BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GREEN).replaceable().noCollision().strength(100.0F)
                    .pushReaction(PushReaction.DESTROY).noLootTable().liquid().sound(SoundType.EMPTY));
    public static final DeferredHolder<Item, BucketItem> TIME_FLUID_BUCKET = BUCKET_ITEMS.registerItem("time_fluid_bucket",
            props -> new BucketItem(JDTRegistration.TIME_FLUID_SOURCE.get(), props),
            () -> new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1));

    //Unstable Portal Fluid
    public static final DeferredHolder<FluidType, FluidType> UNSTABLE_PORTAL_FLUID_TYPE = FLUID_TYPES.register("unstable_portal_fluid_type",
            UnstablePortalFluidType::new);
    public static final DeferredHolder<Fluid, UnstablePortalFluid> UNSTABLE_PORTAL_FLUID_FLOWING = FLUIDS.register("unstable_portal_fluid_flowing",
            UnstablePortalFluid.Flowing::new);
    public static final DeferredHolder<Fluid, UnstablePortalFluid> UNSTABLE_PORTAL_FLUID_SOURCE = FLUIDS.register("unstable_portal_fluid_source",
            UnstablePortalFluid.Source::new);
    public static final DeferredHolder<Block, LiquidBlock> UNSTABLE_PORTAL_FLUID_BLOCK = FLUID_BLOCKS.registerBlock("unstable_portal_fluid_block",
            UnstablePortalFluidBlock::new,
            () -> BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PURPLE).replaceable().noCollision().strength(100.0F)
                    .pushReaction(PushReaction.DESTROY).noLootTable().liquid().sound(SoundType.EMPTY));
    public static final DeferredHolder<Item, BucketItem> UNSTABLE_PORTAL_FLUID_BUCKET = BUCKET_ITEMS.registerItem("unstable_portal_fluid_bucket",
            props -> new BucketItem(JDTRegistration.UNSTABLE_PORTAL_FLUID_SOURCE.get(), props),
            () -> new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1));

    //Unrefined T2 Fuel
    public static final DeferredHolder<FluidType, FluidType> UNREFINED_T2_FLUID_TYPE = FLUID_TYPES.register("unrefined_t2_fluid_type",
            UnrefinedT2FuelType::new);
    public static final DeferredHolder<Fluid, UnrefinedT2Fuel> UNREFINED_T2_FLUID_FLOWING = FLUIDS.register("unrefined_t2_fluid_flowing",
            UnrefinedT2Fuel.Flowing::new);
    public static final DeferredHolder<Fluid, UnrefinedT2Fuel> UNREFINED_T2_FLUID_SOURCE = FLUIDS.register("unrefined_t2_fluid_source",
            UnrefinedT2Fuel.Source::new);
    public static final DeferredHolder<Block, LiquidBlock> UNREFINED_T2_FLUID_BLOCK = FLUID_BLOCKS.registerBlock("unrefined_t2_fluid_block",
            UnrefinedT2FuelBlock::new,
            () -> BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_RED).replaceable().noCollision().strength(100.0F)
                    .pushReaction(PushReaction.DESTROY).noLootTable().liquid().sound(SoundType.EMPTY));
    public static final DeferredHolder<Item, BucketItem> UNREFINED_T2_FLUID_BUCKET = BUCKET_ITEMS.registerItem("unrefined_t2_fluid_bucket",
            props -> new BucketItem(JDTRegistration.UNREFINED_T2_FLUID_SOURCE.get(), props),
            () -> new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1));

    //Refined T2 Fuel
    public static final DeferredHolder<FluidType, FluidType> REFINED_T2_FLUID_TYPE = FLUID_TYPES.register("refined_t2_fluid_type",
            RefinedT2FuelType::new);
    public static final DeferredHolder<Fluid, RefinedT2Fuel> REFINED_T2_FLUID_FLOWING = FLUIDS.register("refined_t2_fluid_flowing",
            RefinedT2Fuel.Flowing::new);
    public static final DeferredHolder<Fluid, RefinedT2Fuel> REFINED_T2_FLUID_SOURCE = FLUIDS.register("refined_t2_fluid_source",
            RefinedT2Fuel.Source::new);
    public static final DeferredHolder<Block, LiquidBlock> REFINED_T2_FLUID_BLOCK = FLUID_BLOCKS.registerBlock("refined_t2_fluid_block",
            RefinedT2FuelBlock::new,
            () -> BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_RED).replaceable().noCollision().strength(100.0F)
                    .pushReaction(PushReaction.DESTROY).noLootTable().liquid().sound(SoundType.EMPTY));
    public static final DeferredHolder<Item, BucketItem> REFINED_T2_FLUID_BUCKET = BUCKET_ITEMS.registerItem("refined_t2_fluid_bucket",
            props -> new BucketItem(JDTRegistration.REFINED_T2_FLUID_SOURCE.get(), props),
            () -> new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1));

    //Unrefined T3 Fuel
    public static final DeferredHolder<FluidType, FluidType> UNREFINED_T3_FLUID_TYPE = FLUID_TYPES.register("unrefined_t3_fluid_type",
            UnrefinedT3FuelType::new);
    public static final DeferredHolder<Fluid, UnrefinedT3Fuel> UNREFINED_T3_FLUID_FLOWING = FLUIDS.register("unrefined_t3_fluid_flowing",
            UnrefinedT3Fuel.Flowing::new);
    public static final DeferredHolder<Fluid, UnrefinedT3Fuel> UNREFINED_T3_FLUID_SOURCE = FLUIDS.register("unrefined_t3_fluid_source",
            UnrefinedT3Fuel.Source::new);
    public static final DeferredHolder<Block, LiquidBlock> UNREFINED_T3_FLUID_BLOCK = FLUID_BLOCKS.registerBlock("unrefined_t3_fluid_block",
            UnrefinedT3FuelBlock::new,
            () -> BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_RED).replaceable().noCollision().strength(100.0F)
                    .pushReaction(PushReaction.DESTROY).noLootTable().liquid().sound(SoundType.EMPTY));
    public static final DeferredHolder<Item, BucketItem> UNREFINED_T3_FLUID_BUCKET = BUCKET_ITEMS.registerItem("unrefined_t3_fluid_bucket",
            props -> new BucketItem(JDTRegistration.UNREFINED_T3_FLUID_SOURCE.get(), props),
            () -> new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1));

    //Refined T3 Fuel
    public static final DeferredHolder<FluidType, FluidType> REFINED_T3_FLUID_TYPE = FLUID_TYPES.register("refined_t3_fluid_type",
            RefinedT3FuelType::new);
    public static final DeferredHolder<Fluid, RefinedT3Fuel> REFINED_T3_FLUID_FLOWING = FLUIDS.register("refined_t3_fluid_flowing",
            RefinedT3Fuel.Flowing::new);
    public static final DeferredHolder<Fluid, RefinedT3Fuel> REFINED_T3_FLUID_SOURCE = FLUIDS.register("refined_t3_fluid_source",
            RefinedT3Fuel.Source::new);
    public static final DeferredHolder<Block, LiquidBlock> REFINED_T3_FLUID_BLOCK = FLUID_BLOCKS.registerBlock("refined_t3_fluid_block",
            RefinedT3FuelBlock::new,
            () -> BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_RED).replaceable().noCollision().strength(100.0F)
                    .pushReaction(PushReaction.DESTROY).noLootTable().liquid().sound(SoundType.EMPTY));
    public static final DeferredHolder<Item, BucketItem> REFINED_T3_FLUID_BUCKET = BUCKET_ITEMS.registerItem("refined_t3_fluid_bucket",
            props -> new BucketItem(JDTRegistration.REFINED_T3_FLUID_SOURCE.get(), props),
            () -> new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1));

    //Unrefined T4 Fuel
    public static final DeferredHolder<FluidType, FluidType> UNREFINED_T4_FLUID_TYPE = FLUID_TYPES.register("unrefined_t4_fluid_type",
            UnrefinedT4FuelType::new);
    public static final DeferredHolder<Fluid, UnrefinedT4Fuel> UNREFINED_T4_FLUID_FLOWING = FLUIDS.register("unrefined_t4_fluid_flowing",
            UnrefinedT4Fuel.Flowing::new);
    public static final DeferredHolder<Fluid, UnrefinedT4Fuel> UNREFINED_T4_FLUID_SOURCE = FLUIDS.register("unrefined_t4_fluid_source",
            UnrefinedT4Fuel.Source::new);
    public static final DeferredHolder<Block, LiquidBlock> UNREFINED_T4_FLUID_BLOCK = FLUID_BLOCKS.registerBlock("unrefined_t4_fluid_block",
            UnrefinedT4FuelBlock::new,
            () -> BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_RED).replaceable().noCollision().strength(100.0F)
                    .pushReaction(PushReaction.DESTROY).noLootTable().liquid().sound(SoundType.EMPTY));
    public static final DeferredHolder<Item, BucketItem> UNREFINED_T4_FLUID_BUCKET = BUCKET_ITEMS.registerItem("unrefined_t4_fluid_bucket",
            props -> new BucketItem(JDTRegistration.UNREFINED_T4_FLUID_SOURCE.get(), props),
            () -> new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1));

    //Refined T4 Fuel
    public static final DeferredHolder<FluidType, FluidType> REFINED_T4_FLUID_TYPE = FLUID_TYPES.register("refined_t4_fluid_type",
            RefinedT4FuelType::new);
    public static final DeferredHolder<Fluid, RefinedT4Fuel> REFINED_T4_FLUID_FLOWING = FLUIDS.register("refined_t4_fluid_flowing",
            RefinedT4Fuel.Flowing::new);
    public static final DeferredHolder<Fluid, RefinedT4Fuel> REFINED_T4_FLUID_SOURCE = FLUIDS.register("refined_t4_fluid_source",
            RefinedT4Fuel.Source::new);
    public static final DeferredHolder<Block, LiquidBlock> REFINED_T4_FLUID_BLOCK = FLUID_BLOCKS.registerBlock("refined_t4_fluid_block",
            RefinedT4FuelBlock::new,
            () -> BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_RED).replaceable().noCollision().strength(100.0F)
                    .pushReaction(PushReaction.DESTROY).noLootTable().liquid().sound(SoundType.EMPTY));
    public static final DeferredHolder<Item, BucketItem> REFINED_T4_FLUID_BUCKET = BUCKET_ITEMS.registerItem("refined_t4_fluid_bucket",
            props -> new BucketItem(JDTRegistration.REFINED_T4_FLUID_SOURCE.get(), props),
            () -> new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1));

    //XP Fluid
    public static final DeferredHolder<FluidType, FluidType> XP_FLUID_TYPE = FLUID_TYPES.register("xp_fluid_type",
            XPFluidType::new);
    public static final DeferredHolder<Fluid, XPFluid> XP_FLUID_FLOWING = FLUIDS.register("xp_fluid_flowing",
            XPFluid.Flowing::new);
    public static final DeferredHolder<Fluid, XPFluid> XP_FLUID_SOURCE = FLUIDS.register("xp_fluid_source",
            XPFluid.Source::new);
    public static final DeferredHolder<Block, LiquidBlock> XP_FLUID_BLOCK = FLUID_BLOCKS.registerBlock("xp_fluid_block",
            XPFluidBlock::new,
            () -> BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GREEN).replaceable().noCollision().strength(100.0F)
                    .pushReaction(PushReaction.DESTROY).noLootTable().liquid().sound(SoundType.EMPTY));
    public static final DeferredHolder<Item, BucketItem> XP_FLUID_BUCKET = BUCKET_ITEMS.registerItem("xp_fluid_bucket",
            props -> new BucketItem(JDTRegistration.XP_FLUID_SOURCE.get(), props),
            () -> new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1));


    //Machines
    public static final DeferredHolder<Block, ItemCollector> ItemCollector = BLOCKS.registerBlock("itemcollector", ItemCollector::new,
            () -> BlockBehaviour.Properties.of().sound(SoundType.METAL).strength(2.0f).noOcclusion().isRedstoneConductor(BaseMachineBlock::never));
    public static final DeferredHolder<Item, BlockItem> ItemCollector_ITEM = ITEMS.registerSimpleBlockItem("itemcollector", ItemCollector);
    public static final DeferredHolder<Block, BlockBreakerT1> BlockBreakerT1 = SIDEDBLOCKS.registerBlock("blockbreakert1", BlockBreakerT1::new, machineProps());
    public static final DeferredHolder<Item, BlockItem> BlockBreakerT1_ITEM = ITEMS.registerSimpleBlockItem("blockbreakert1", BlockBreakerT1);
    public static final DeferredHolder<Block, BlockBreakerT2> BlockBreakerT2 = SIDEDBLOCKS.registerBlock("blockbreakert2", BlockBreakerT2::new, machineProps());
    public static final DeferredHolder<Item, BlockItem> BlockBreakerT2_ITEM = ITEMS.registerSimpleBlockItem("blockbreakert2", BlockBreakerT2);
    public static final DeferredHolder<Block, BlockPlacerT1> BlockPlacerT1 = SIDEDBLOCKS.registerBlock("blockplacert1", BlockPlacerT1::new, machineProps());
    public static final DeferredHolder<Item, BlockItem> BlockPlacerT1_ITEM = ITEMS.registerSimpleBlockItem("blockplacert1", BlockPlacerT1);
    public static final DeferredHolder<Block, BlockPlacerT2> BlockPlacerT2 = SIDEDBLOCKS.registerBlock("blockplacert2", BlockPlacerT2::new, machineProps());
    public static final DeferredHolder<Item, BlockItem> BlockPlacerT2_ITEM = ITEMS.registerSimpleBlockItem("blockplacert2", BlockPlacerT2);
    public static final DeferredHolder<Block, ClickerT1> ClickerT1 = SIDEDBLOCKS.registerBlock("clickert1", ClickerT1::new, machineProps());
    public static final DeferredHolder<Item, BlockItem> ClickerT1_ITEM = ITEMS.registerSimpleBlockItem("clickert1", ClickerT1);
    public static final DeferredHolder<Block, ClickerT2> ClickerT2 = SIDEDBLOCKS.registerBlock("clickert2", ClickerT2::new, machineProps());
    public static final DeferredHolder<Item, BlockItem> ClickerT2_ITEM = ITEMS.registerSimpleBlockItem("clickert2", ClickerT2);
    public static final DeferredHolder<Block, SensorT1> SensorT1 = SIDEDBLOCKS.registerBlock("sensort1", SensorT1::new, machineProps());
    public static final DeferredHolder<Item, BlockItem> SensorT1_ITEM = ITEMS.registerSimpleBlockItem("sensort1", SensorT1);
    public static final DeferredHolder<Block, SensorT2> SensorT2 = SIDEDBLOCKS.registerBlock("sensort2", SensorT2::new, machineProps());
    public static final DeferredHolder<Item, BlockItem> SensorT2_ITEM = ITEMS.registerSimpleBlockItem("sensort2", SensorT2);
    public static final DeferredHolder<Block, DropperT1> DropperT1 = SIDEDBLOCKS.registerBlock("droppert1", DropperT1::new, machineProps());
    public static final DeferredHolder<Item, BlockItem> DropperT1_ITEM = ITEMS.registerSimpleBlockItem("droppert1", DropperT1);
    public static final DeferredHolder<Block, DropperT2> DropperT2 = SIDEDBLOCKS.registerBlock("droppert2", DropperT2::new, machineProps());
    public static final DeferredHolder<Item, BlockItem> DropperT2_ITEM = ITEMS.registerSimpleBlockItem("droppert2", DropperT2);
    public static final DeferredHolder<Block, BlockSwapperT1> BlockSwapperT1 = SIDEDBLOCKS.registerBlock("blockswappert1", BlockSwapperT1::new, machineProps());
    public static final DeferredHolder<Item, BlockItem> BlockSwapperT1_ITEM = ITEMS.registerSimpleBlockItem("blockswappert1", BlockSwapperT1);
    public static final DeferredHolder<Block, BlockSwapperT2> BlockSwapperT2 = SIDEDBLOCKS.registerBlock("blockswappert2", BlockSwapperT2::new, machineProps());
    public static final DeferredHolder<Item, BlockItem> BlockSwapperT2_ITEM = ITEMS.registerSimpleBlockItem("blockswappert2", BlockSwapperT2);
    public static final DeferredHolder<Block, PlayerAccessor> PlayerAccessor = BLOCKS.registerBlock("playeraccessor", PlayerAccessor::new, machineProps());
    public static final DeferredHolder<Item, BlockItem> PlayerAccessor_ITEM = ITEMS.registerSimpleBlockItem("playeraccessor", PlayerAccessor);
    public static final DeferredHolder<Block, FluidPlacerT1> FluidPlacerT1 = SIDEDBLOCKS.registerBlock("fluidplacert1", FluidPlacerT1::new, machineProps());
    public static final DeferredHolder<Item, BlockItem> FluidPlacerT1_ITEM = ITEMS.registerSimpleBlockItem("fluidplacert1", FluidPlacerT1);
    public static final DeferredHolder<Block, FluidPlacerT2> FluidPlacerT2 = SIDEDBLOCKS.registerBlock("fluidplacert2", FluidPlacerT2::new, machineProps());
    public static final DeferredHolder<Item, BlockItem> FluidPlacerT2_ITEM = ITEMS.registerSimpleBlockItem("fluidplacert2", FluidPlacerT2);
    public static final DeferredHolder<Block, FluidCollectorT1> FluidCollectorT1 = SIDEDBLOCKS.registerBlock("fluidcollectort1", FluidCollectorT1::new, machineProps());
    public static final DeferredHolder<Item, BlockItem> FluidCollectorT1_ITEM = ITEMS.registerSimpleBlockItem("fluidcollectort1", FluidCollectorT1);
    public static final DeferredHolder<Block, FluidCollectorT2> FluidCollectorT2 = SIDEDBLOCKS.registerBlock("fluidcollectort2", FluidCollectorT2::new, machineProps());
    public static final DeferredHolder<Item, BlockItem> FluidCollectorT2_ITEM = ITEMS.registerSimpleBlockItem("fluidcollectort2", FluidCollectorT2);
    public static final DeferredHolder<Block, ParadoxMachine> ParadoxMachine = SIDEDBLOCKS.registerBlock("paradoxmachine", ParadoxMachine::new, machineProps());
    public static final DeferredHolder<Item, BlockItem> ParadoxMachine_ITEM = ITEMS.registerSimpleBlockItem("paradoxmachine", ParadoxMachine);
    public static final DeferredHolder<Block, InventoryHolder> InventoryHolder = BLOCKS.registerBlock("inventory_holder", InventoryHolder::new, machineProps());
    public static final DeferredHolder<Item, BlockItem> InventoryHolder_ITEM = ITEMS.registerSimpleBlockItem("inventory_holder", InventoryHolder);
    public static final DeferredHolder<Block, ExperienceHolder> ExperienceHolder = BLOCKS.registerBlock("experienceholder", ExperienceHolder::new,
            () -> BlockBehaviour.Properties.of().sound(SoundType.METAL).strength(2.0f).noOcclusion().forceSolidOn().isRedstoneConductor(BaseMachineBlock::never));
    public static final DeferredHolder<Item, BlockItem> ExperienceHolder_ITEM = ITEMS.registerSimpleBlockItem("experienceholder", ExperienceHolder);


    //Power Machines
    public static final DeferredHolder<Block, GeneratorT1> GeneratorT1 = BLOCKS.registerBlock("generatort1", GeneratorT1::new, machineProps());
    public static final DeferredHolder<Block, GeneratorFluidT1> GeneratorFluidT1 = BLOCKS.registerBlock("generatorfluidt1", GeneratorFluidT1::new, machineProps());
    public static final DeferredHolder<Item, BlockItem> GeneratorT1_ITEM = ITEMS.registerSimpleBlockItem("generatort1", GeneratorT1);
    public static final DeferredHolder<Item, BlockItem> GeneratorFluidT1_ITEM = ITEMS.registerSimpleBlockItem("generatorfluidt1", GeneratorFluidT1);
    public static final DeferredHolder<Block, EnergyTransmitter> EnergyTransmitter = BLOCKS.registerBlock("energytransmitter", EnergyTransmitter::new,
            () -> BlockBehaviour.Properties.of().sound(SoundType.METAL).strength(2.0f).noOcclusion().forceSolidOn().isRedstoneConductor(BaseMachineBlock::never));
    public static final DeferredHolder<Item, BlockItem> EnergyTransmitter_ITEM = ITEMS.registerSimpleBlockItem("energytransmitter", EnergyTransmitter);

    //Blocks - Raw Resources
    private static Supplier<BlockBehaviour.Properties> rawOreAmethystProps() {
        return () -> BlockBehaviour.Properties.of().sound(SoundType.AMETHYST).requiresCorrectToolForDrops().noOcclusion().strength(5.0F, 6.0F);
    }
    private static Supplier<BlockBehaviour.Properties> rawOreCoalProps() {
        return () -> BlockBehaviour.Properties.of().requiresCorrectToolForDrops().noOcclusion().strength(5.0F, 6.0F);
    }

    public static final DeferredHolder<Block, RawFerricoreOre> RawFerricoreOre = BLOCKS.registerBlock("raw_ferricore_ore", RawFerricoreOre::new, rawOreAmethystProps());
    public static final DeferredHolder<Item, BlockItem> RawFerricoreOre_ITEM = ITEMS.registerSimpleBlockItem("raw_ferricore_ore", RawFerricoreOre);
    public static final DeferredHolder<Block, RawBlazegoldOre> RawBlazegoldOre = BLOCKS.registerBlock("raw_blazegold_ore", RawBlazegoldOre::new, rawOreAmethystProps());
    public static final DeferredHolder<Item, BlockItem> RawBlazegoldOre_ITEM = ITEMS.registerSimpleBlockItem("raw_blazegold_ore", RawBlazegoldOre);
    public static final DeferredHolder<Block, RawCelestigemOre> RawCelestigemOre = BLOCKS.registerBlock("raw_celestigem_ore", RawCelestigemOre::new, rawOreAmethystProps());
    public static final DeferredHolder<Item, BlockItem> RawCelestigemOre_ITEM = ITEMS.registerSimpleBlockItem("raw_celestigem_ore", RawCelestigemOre);
    public static final DeferredHolder<Block, RawEclipseAlloyOre> RawEclipseAlloyOre = BLOCKS.registerBlock("raw_eclipsealloy_ore", RawEclipseAlloyOre::new, rawOreAmethystProps());
    public static final DeferredHolder<Item, BlockItem> RawEclipseAlloyOre_ITEM = ITEMS.registerSimpleBlockItem("raw_eclipsealloy_ore", RawEclipseAlloyOre);
    public static final DeferredHolder<Block, RawCoal_T1> RawCoal_T1 = BLOCKS.registerBlock("raw_coal_t1_ore", RawCoal_T1::new, rawOreCoalProps());
    public static final DeferredHolder<Item, BlockItem> RawCoal_T1_ITEM = ITEMS.registerSimpleBlockItem("raw_coal_t1_ore", RawCoal_T1);
    public static final DeferredHolder<Block, RawCoal_T2> RawCoal_T2 = BLOCKS.registerBlock("raw_coal_t2_ore", RawCoal_T2::new, rawOreCoalProps());
    public static final DeferredHolder<Item, BlockItem> RawCoal_T2_ITEM = ITEMS.registerSimpleBlockItem("raw_coal_t2_ore", RawCoal_T2);
    public static final DeferredHolder<Block, RawCoal_T3> RawCoal_T3 = BLOCKS.registerBlock("raw_coal_t3_ore", RawCoal_T3::new, rawOreCoalProps());
    public static final DeferredHolder<Item, BlockItem> RawCoal_T3_ITEM = ITEMS.registerSimpleBlockItem("raw_coal_t3_ore", RawCoal_T3);
    public static final DeferredHolder<Block, RawCoal_T4> RawCoal_T4 = BLOCKS.registerBlock("raw_coal_t4_ore", RawCoal_T4::new, rawOreCoalProps());
    public static final DeferredHolder<Item, BlockItem> RawCoal_T4_ITEM = ITEMS.registerSimpleBlockItem("raw_coal_t4_ore", RawCoal_T4);
    public static final DeferredHolder<Block, TimeCrystalBlock> TimeCrystalBlock = BLOCKS.registerBlock("time_crystal_block", TimeCrystalBlock::new,
            () -> BlockBehaviour.Properties.of().sound(SoundType.AMETHYST).requiresCorrectToolForDrops().strength(1.5F));
    public static final DeferredHolder<Item, BlockItem> TimeCrystalBlock_ITEM = ITEMS.registerSimpleBlockItem("time_crystal_block", TimeCrystalBlock);
    public static final DeferredHolder<Block, TimeCrystalBuddingBlock> TimeCrystalBuddingBlock = BLOCKS.registerBlock("time_crystal_budding_block", TimeCrystalBuddingBlock::new,
            () -> BlockBehaviour.Properties.of().sound(SoundType.AMETHYST).randomTicks().strength(1.5F));
    public static final DeferredHolder<Item, BlockItem> TimeCrystalBuddingBlock_ITEM = ITEMS.registerSimpleBlockItem("time_crystal_budding_block", TimeCrystalBuddingBlock);
    public static final DeferredHolder<Block, TimeCrystalCluster> TimeCrystalCluster = BLOCKS.registerBlock("time_crystal_cluster",
            props -> new TimeCrystalCluster(7.0F, 3.0F, props),
            () -> BlockBehaviour.Properties.of()
                    .forceSolidOn()
                    .noOcclusion()
                    .sound(SoundType.AMETHYST_CLUSTER)
                    .strength(1.5F)
                    .lightLevel(p_152632_ -> 5)
                    .pushReaction(PushReaction.DESTROY));
    public static final DeferredHolder<Item, BlockItem> TimeCrystalCluster_ITEM = ITEMS.registerSimpleBlockItem("time_crystal_cluster", TimeCrystalCluster);
    public static final DeferredHolder<Block, TimeCrystalCluster> TimeCrystalCluster_Small = BLOCKS.registerBlock("time_crystal_cluster_small",
            props -> new TimeCrystalCluster(3.0F, 4.0F, props),
            () -> BlockBehaviour.Properties.ofLegacyCopy(TimeCrystalCluster.get()).sound(SoundType.SMALL_AMETHYST_BUD).lightLevel(p_187409_ -> 1));
    public static final DeferredHolder<Item, BlockItem> TimeCrystalCluster_Small_ITEM = ITEMS.registerSimpleBlockItem("time_crystal_cluster_small", TimeCrystalCluster_Small);
    public static final DeferredHolder<Block, TimeCrystalCluster> TimeCrystalCluster_Medium = BLOCKS.registerBlock("time_crystal_cluster_medium",
            props -> new TimeCrystalCluster(4.0F, 3.0F, props),
            () -> BlockBehaviour.Properties.ofLegacyCopy(TimeCrystalCluster.get()).sound(SoundType.MEDIUM_AMETHYST_BUD).lightLevel(p_152617_ -> 2));
    public static final DeferredHolder<Item, BlockItem> TimeCrystalCluster_Medium_ITEM = ITEMS.registerSimpleBlockItem("time_crystal_cluster_medium", TimeCrystalCluster_Medium);
    public static final DeferredHolder<Block, TimeCrystalCluster> TimeCrystalCluster_Large = BLOCKS.registerBlock("time_crystal_cluster_large",
            props -> new TimeCrystalCluster(5.0F, 3.0F, props),
            () -> BlockBehaviour.Properties.ofLegacyCopy(TimeCrystalCluster.get()).sound(SoundType.LARGE_AMETHYST_BUD).lightLevel(p_152629_ -> 4));
    public static final DeferredHolder<Item, BlockItem> TimeCrystalCluster_Large_ITEM = ITEMS.registerSimpleBlockItem("time_crystal_cluster_large", TimeCrystalCluster_Large);



    //Blocks Consolidated Resources
    private static Supplier<BlockBehaviour.Properties> metalResourceProps() {
        return () -> BlockBehaviour.Properties.of().sound(SoundType.METAL).requiresCorrectToolForDrops().strength(5.0F, 6.0F);
    }
    private static Supplier<BlockBehaviour.Properties> amethystResourceProps() {
        return () -> BlockBehaviour.Properties.of().sound(SoundType.AMETHYST).requiresCorrectToolForDrops().strength(5.0F, 6.0F);
    }
    private static Supplier<BlockBehaviour.Properties> coalResourceProps() {
        return () -> BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(5.0F, 6.0F);
    }

    public static final DeferredHolder<Block, FerricoreBlock> FerricoreBlock = BLOCKS.registerBlock("ferricore_block", FerricoreBlock::new, metalResourceProps());
    public static final DeferredHolder<Item, BlockItem> FerricoreBlock_ITEM = ITEMS.registerSimpleBlockItem("ferricore_block", FerricoreBlock);
    public static final DeferredHolder<Block, BlazeGoldBlock> BlazeGoldBlock = BLOCKS.registerBlock("blazegold_block", BlazeGoldBlock::new, metalResourceProps());
    public static final DeferredHolder<Item, BlockItem> BlazeGoldBlock_ITEM = ITEMS.registerSimpleBlockItem("blazegold_block", BlazeGoldBlock);
    public static final DeferredHolder<Block, CelestigemBlock> CelestigemBlock = BLOCKS.registerBlock("celestigem_block", CelestigemBlock::new, amethystResourceProps());
    public static final DeferredHolder<Item, BlockItem> CelestigemBlock_ITEM = ITEMS.registerSimpleBlockItem("celestigem_block", CelestigemBlock);
    public static final DeferredHolder<Block, EclipseAlloyBlock> EclipseAlloyBlock = BLOCKS.registerBlock("eclipsealloy_block", EclipseAlloyBlock::new, metalResourceProps());
    public static final DeferredHolder<Item, BlockItem> EclipseAlloyBlock_ITEM = ITEMS.registerSimpleBlockItem("eclipsealloy_block", EclipseAlloyBlock);
    public static final DeferredHolder<Block, CoalBlock_T1> CoalBlock_T1 = BLOCKS.registerBlock("coalblock_t1", CoalBlock_T1::new, coalResourceProps());
    public static final DeferredHolder<Item, BlockItem> CoalBlock_T1_ITEM = ITEMS.registerSimpleBlockItem("coalblock_t1", CoalBlock_T1);
    public static final DeferredHolder<Block, CoalBlock_T2> CoalBlock_T2 = BLOCKS.registerBlock("coalblock_t2", CoalBlock_T2::new, coalResourceProps());
    public static final DeferredHolder<Item, BlockItem> CoalBlock_T2_ITEM = ITEMS.registerSimpleBlockItem("coalblock_t2", CoalBlock_T2);
    public static final DeferredHolder<Block, CoalBlock_T3> CoalBlock_T3 = BLOCKS.registerBlock("coalblock_t3", CoalBlock_T3::new, coalResourceProps());
    public static final DeferredHolder<Item, BlockItem> CoalBlock_T3_ITEM = ITEMS.registerSimpleBlockItem("coalblock_t3", CoalBlock_T3);
    public static final DeferredHolder<Block, CoalBlock_T4> CoalBlock_T4 = BLOCKS.registerBlock("coalblock_t4", CoalBlock_T4::new, coalResourceProps());
    public static final DeferredHolder<Item, BlockItem> CoalBlock_T4_ITEM = ITEMS.registerSimpleBlockItem("coalblock_t4", CoalBlock_T4);
    public static final DeferredHolder<Block, CharcoalBlock> CharcoalBlock = BLOCKS.registerBlock("charcoal", CharcoalBlock::new, coalResourceProps());
    public static final DeferredHolder<Item, BlockItem> CharcoalBlock_ITEM = ITEMS.registerSimpleBlockItem("charcoal", CharcoalBlock);

    //BlockEntities (Not TileEntities - Honest)
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<GooBlockBE_Tier1>> GooBlockBE_Tier1 = BLOCK_ENTITIES.register("gooblock_tier1", () -> new BlockEntityType<>(GooBlockBE_Tier1::new, GooBlock_Tier1.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<GooBlockBE_Tier2>> GooBlockBE_Tier2 = BLOCK_ENTITIES.register("gooblock_tier2", () -> new BlockEntityType<>(GooBlockBE_Tier2::new, GooBlock_Tier2.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<GooBlockBE_Tier3>> GooBlockBE_Tier3 = BLOCK_ENTITIES.register("gooblock_tier3", () -> new BlockEntityType<>(GooBlockBE_Tier3::new, GooBlock_Tier3.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<GooBlockBE_Tier4>> GooBlockBE_Tier4 = BLOCK_ENTITIES.register("gooblock_tier4", () -> new BlockEntityType<>(GooBlockBE_Tier4::new, GooBlock_Tier4.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<GooSoilBE>> GooSoilBE = BLOCK_ENTITIES.register("goosoilbe", () -> new BlockEntityType<>(GooSoilBE::new, GooSoil_Tier3.get(), GooSoil_Tier4.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ItemCollectorBE>> ItemCollectorBE = BLOCK_ENTITIES.register("itemcollectorbe", () -> new BlockEntityType<>(ItemCollectorBE::new, ItemCollector.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockBreakerT1BE>> BlockBreakerT1BE = BLOCK_ENTITIES.register("blockbreakert1", () -> new BlockEntityType<>(BlockBreakerT1BE::new, BlockBreakerT1.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockBreakerT2BE>> BlockBreakerT2BE = BLOCK_ENTITIES.register("blockbreakert2", () -> new BlockEntityType<>(BlockBreakerT2BE::new, BlockBreakerT2.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockPlacerT1BE>> BlockPlacerT1BE = BLOCK_ENTITIES.register("blockplacert1", () -> new BlockEntityType<>(BlockPlacerT1BE::new, BlockPlacerT1.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockPlacerT2BE>> BlockPlacerT2BE = BLOCK_ENTITIES.register("blockplacert2", () -> new BlockEntityType<>(BlockPlacerT2BE::new, BlockPlacerT2.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ClickerT1BE>> ClickerT1BE = BLOCK_ENTITIES.register("clickert1", () -> new BlockEntityType<>(ClickerT1BE::new, ClickerT1.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ClickerT2BE>> ClickerT2BE = BLOCK_ENTITIES.register("clickert2", () -> new BlockEntityType<>(ClickerT2BE::new, ClickerT2.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SensorT1BE>> SensorT1BE = BLOCK_ENTITIES.register("sensort1be", () -> new BlockEntityType<>(SensorT1BE::new, SensorT1.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SensorT2BE>> SensorT2BE = BLOCK_ENTITIES.register("sensort2be", () -> new BlockEntityType<>(SensorT2BE::new, SensorT2.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<DropperT1BE>> DropperT1BE = BLOCK_ENTITIES.register("droppert1", () -> new BlockEntityType<>(DropperT1BE::new, DropperT1.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<DropperT2BE>> DropperT2BE = BLOCK_ENTITIES.register("droppert2", () -> new BlockEntityType<>(DropperT2BE::new, DropperT2.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<GeneratorT1BE>> GeneratorT1BE = BLOCK_ENTITIES.register("generatort1", () -> new BlockEntityType<>(GeneratorT1BE::new, GeneratorT1.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<GeneratorFluidT1BE>> GeneratorFluidT1BE = BLOCK_ENTITIES.register("generatorfluidt1", () -> new BlockEntityType<>(GeneratorFluidT1BE::new, GeneratorFluidT1.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<EnergyTransmitterBE>> EnergyTransmitterBE = BLOCK_ENTITIES.register("energytransmitter", () -> new BlockEntityType<>(EnergyTransmitterBE::new, EnergyTransmitter.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockSwapperT1BE>> BlockSwapperT1BE = BLOCK_ENTITIES.register("blockswappert1", () -> new BlockEntityType<>(BlockSwapperT1BE::new, BlockSwapperT1.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockSwapperT2BE>> BlockSwapperT2BE = BLOCK_ENTITIES.register("blockswappert2", () -> new BlockEntityType<>(BlockSwapperT2BE::new, BlockSwapperT2.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PlayerAccessorBE>> PlayerAccessorBE = BLOCK_ENTITIES.register("playeraccessorbe", () -> new BlockEntityType<>(PlayerAccessorBE::new, PlayerAccessor.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<EclipseGateBE>> EclipseGateBE = BLOCK_ENTITIES.register("eclipsegatebe", () -> new BlockEntityType<>(EclipseGateBE::new, EclipseGateBlock.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FluidPlacerT1BE>> FluidPlacerT1BE = BLOCK_ENTITIES.register("fluidplacert1", () -> new BlockEntityType<>(FluidPlacerT1BE::new, FluidPlacerT1.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FluidPlacerT2BE>> FluidPlacerT2BE = BLOCK_ENTITIES.register("fluidplacert2", () -> new BlockEntityType<>(FluidPlacerT2BE::new, FluidPlacerT2.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FluidCollectorT1BE>> FluidCollectorT1BE = BLOCK_ENTITIES.register("fluidcollectort1", () -> new BlockEntityType<>(FluidCollectorT1BE::new, FluidCollectorT1.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FluidCollectorT2BE>> FluidCollectorT2BE = BLOCK_ENTITIES.register("fluidcollectort2", () -> new BlockEntityType<>(FluidCollectorT2BE::new, FluidCollectorT2.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ParadoxMachineBE>> ParadoxMachineBE = BLOCK_ENTITIES.register("paradoxmachine", () -> new BlockEntityType<>(ParadoxMachineBE::new, ParadoxMachine.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<InventoryHolderBE>> InventoryHolderBE = BLOCK_ENTITIES.register("inventory_holder", () -> new BlockEntityType<>(InventoryHolderBE::new, InventoryHolder.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ExperienceHolderBE>> ExperienceHolderBE = BLOCK_ENTITIES.register("experienceholder", () -> new BlockEntityType<>(ExperienceHolderBE::new, ExperienceHolder.get()));

    //Items - Raw Resources
    public static final DeferredHolder<Item, RawFerricore> RawFerricore = ITEMS.registerItem("raw_ferricore", RawFerricore::new, Item.Properties::new);
    public static final DeferredHolder<Item, RawBlazegold> RawBlazegold = ITEMS.registerItem("raw_blazegold", RawBlazegold::new, Item.Properties::new);
    public static final DeferredHolder<Item, RawEclipseAlloy> RawEclipseAlloy = ITEMS.registerItem("raw_eclipsealloy", RawEclipseAlloy::new, Item.Properties::new);

    //Items - Resources
    public static final DeferredHolder<Item, FerricoreIngot> FerricoreIngot = ITEMS.registerItem("ferricore_ingot", FerricoreIngot::new, Item.Properties::new);
    public static final DeferredHolder<Item, BlazeGoldIngot> BlazegoldIngot = ITEMS.registerItem("blazegold_ingot", BlazeGoldIngot::new, Item.Properties::new);
    public static final DeferredHolder<Item, Celestigem> Celestigem = ITEMS.registerItem("celestigem", Celestigem::new, Item.Properties::new);
    public static final DeferredHolder<Item, EclipseAlloyIngot> EclipseAlloyIngot = ITEMS.registerItem("eclipsealloy_ingot", EclipseAlloyIngot::new, Item.Properties::new);
    public static final DeferredHolder<Item, Coal_T1> Coal_T1 = ITEMS.registerItem("coal_t1", Coal_T1::new, Item.Properties::new);
    public static final DeferredHolder<Item, Coal_T2> Coal_T2 = ITEMS.registerItem("coal_t2", Coal_T2::new, Item.Properties::new);
    public static final DeferredHolder<Item, Coal_T3> Coal_T3 = ITEMS.registerItem("coal_t3", Coal_T3::new, Item.Properties::new);
    public static final DeferredHolder<Item, Coal_T4> Coal_T4 = ITEMS.registerItem("coal_t4", Coal_T4::new, Item.Properties::new);
    public static final DeferredHolder<Item, PolymorphicCatalyst> PolymorphicCatalyst = ITEMS.registerItem("polymorphic_catalyst", PolymorphicCatalyst::new, Item.Properties::new);
    public static final DeferredHolder<Item, PortalFluidCatalyst> PortalFluidCatalyst = ITEMS.registerItem("portal_fluid_catalyst", PortalFluidCatalyst::new, Item.Properties::new);
    public static final DeferredHolder<Item, TimeCrystal> TimeCrystal = ITEMS.registerItem("time_crystal", TimeCrystal::new, Item.Properties::new);

    //Items
    public static final DeferredHolder<Item, FuelCanister> Fuel_Canister = ITEMS.registerItem("fuel_canister", FuelCanister::new, () -> new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, PocketGenerator> Pocket_Generator = ITEMS.registerItem("pocket_generator", PocketGenerator::new, () -> new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, FerricoreWrench> FerricoreWrench = ITEMS.registerItem("ferricore_wrench", FerricoreWrench::new, () -> new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, TotemOfDeathRecall> TotemOfDeathRecall = ITEMS.registerItem("totem_of_death_recall", TotemOfDeathRecall::new, () -> new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, BlazejetWand> BlazejetWand = ITEMS.registerItem("blazejet_wand", BlazejetWand::new, () -> new Item.Properties().fireResistant().durability(200));
    public static final DeferredHolder<Item, VoidshiftWand> VoidshiftWand = ITEMS.registerItem("voidshift_wand", VoidshiftWand::new, () -> new Item.Properties().durability(200).fireResistant());
    public static final DeferredHolder<Item, EclipsegateWand> EclipsegateWand = ITEMS.registerItem("eclipsegate_wand", EclipsegateWand::new, () -> new Item.Properties().durability(200).fireResistant());
    public static final DeferredHolder<Item, TimeWand> TimeWand = ITEMS.registerItem("time_wand", TimeWand::new, () -> new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, CreatureCatcher> CreatureCatcher = ITEMS.registerItem("creaturecatcher", CreatureCatcher::new, Item.Properties::new);
    public static final DeferredHolder<Item, MachineSettingsCopier> MachineSettingsCopier = ITEMS.registerItem("machinesettingscopier", MachineSettingsCopier::new, () -> new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, PortalGun> PortalGun = ITEMS.registerItem("portalgun", PortalGun::new, () -> new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, PortalGunV2> PortalGunV2 = ITEMS.registerItem("portalgun_v2", PortalGunV2::new, () -> new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, FluidCanister> FluidCanister = ITEMS.registerItem("fluid_canister", FluidCanister::new, () -> new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, PotionCanister> PotionCanister = ITEMS.registerItem("potion_canister", PotionCanister::new, () -> new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, FerricoreBow> FerricoreBow = BOWS.registerItem("bow_ferricore", FerricoreBow::new, () -> new Item.Properties().durability(250));
    public static final DeferredHolder<Item, BlazegoldBow> BlazegoldBow = BOWS.registerItem("bow_blazegold", BlazegoldBow::new, () -> new Item.Properties().durability(450).fireResistant());
    public static final DeferredHolder<Item, CelestigemBow> CelestigemBow = BOWS.registerItem("bow_celestigem", CelestigemBow::new, () -> new Item.Properties().durability(450).fireResistant());
    public static final DeferredHolder<Item, EclipseAlloyBow> EclipseAlloyBow = BOWS.registerItem("bow_eclipsealloy", EclipseAlloyBow::new, () -> new Item.Properties().durability(450).fireResistant());
    public static final DeferredHolder<Item, PolymorphicWand> PolymorphicWand = ITEMS.registerItem("polymorphic_wand", PolymorphicWand::new, () -> new Item.Properties().fireResistant().durability(200));
    public static final DeferredHolder<Item, PolymorphicWandV2> PolymorphicWandV2 = ITEMS.registerItem("polymorphic_wand_v2", PolymorphicWandV2::new, () -> new Item.Properties().fireResistant());

    //Items - Tools
    public static final DeferredHolder<Item, FerricoreSword> FerricoreSword = TOOLS.registerItem("ferricore_sword", FerricoreSword::new, () -> new Item.Properties().sword(GooTier.FERRICORE.material(), 3, -2.0F));
    public static final DeferredHolder<Item, FerricorePickaxe> FerricorePickaxe = TOOLS.registerItem("ferricore_pickaxe", FerricorePickaxe::new, () -> new Item.Properties().pickaxe(GooTier.FERRICORE.material(), 1.0F, -2.8F));
    public static final DeferredHolder<Item, FerricoreShovel> FerricoreShovel = TOOLS.registerItem("ferricore_shovel", FerricoreShovel::new, Item.Properties::new);
    public static final DeferredHolder<Item, FerricoreAxe> FerricoreAxe = TOOLS.registerItem("ferricore_axe", FerricoreAxe::new, Item.Properties::new);
    public static final DeferredHolder<Item, FerricoreHoe> FerricoreHoe = TOOLS.registerItem("ferricore_hoe", FerricoreHoe::new, Item.Properties::new);
    public static final DeferredHolder<Item, BlazegoldSword> BlazegoldSword = TOOLS.registerItem("blazegold_sword", BlazegoldSword::new, () -> new Item.Properties().sword(GooTier.BLAZEGOLD.material(), 3, -2.0F).fireResistant());
    public static final DeferredHolder<Item, BlazegoldPickaxe> BlazegoldPickaxe = TOOLS.registerItem("blazegold_pickaxe", BlazegoldPickaxe::new, () -> new Item.Properties().pickaxe(GooTier.BLAZEGOLD.material(), 1.0F, -2.8F).fireResistant());
    public static final DeferredHolder<Item, BlazegoldShovel> BlazegoldShovel = TOOLS.registerItem("blazegold_shovel", BlazegoldShovel::new, () -> new Item.Properties().fireResistant());
    public static final DeferredHolder<Item, BlazegoldAxe> BlazegoldAxe = TOOLS.registerItem("blazegold_axe", BlazegoldAxe::new, () -> new Item.Properties().fireResistant());
    public static final DeferredHolder<Item, BlazegoldHoe> BlazegoldHoe = TOOLS.registerItem("blazegold_hoe", BlazegoldHoe::new, () -> new Item.Properties().fireResistant());
    public static final DeferredHolder<Item, CelestigemSword> CelestigemSword = TOOLS.registerItem("celestigem_sword", CelestigemSword::new, () -> new Item.Properties().sword(GooTier.CELESTIGEM.material(), 3, -2.0F).fireResistant());
    public static final DeferredHolder<Item, CelestigemPickaxe> CelestigemPickaxe = TOOLS.registerItem("celestigem_pickaxe", CelestigemPickaxe::new, () -> new Item.Properties().pickaxe(GooTier.CELESTIGEM.material(), 1.0F, -2.8F).fireResistant());
    public static final DeferredHolder<Item, CelestigemShovel> CelestigemShovel = TOOLS.registerItem("celestigem_shovel", CelestigemShovel::new, () -> new Item.Properties().fireResistant());
    public static final DeferredHolder<Item, CelestigemAxe> CelestigemAxe = TOOLS.registerItem("celestigem_axe", CelestigemAxe::new, () -> new Item.Properties().fireResistant());
    public static final DeferredHolder<Item, CelestigemHoe> CelestigemHoe = TOOLS.registerItem("celestigem_hoe", CelestigemHoe::new, () -> new Item.Properties().fireResistant());
    public static final DeferredHolder<Item, CelestigemPaxel> CelestigemPaxel = TOOLS.registerItem("celestigem_paxel", CelestigemPaxel::new, () -> new Item.Properties().pickaxe(GooTier.CELESTIGEM.material(), 1.0F, -2.8F).fireResistant());
    public static final DeferredHolder<Item, EclipseAlloySword> EclipseAlloySword = TOOLS.registerItem("eclipsealloy_sword", EclipseAlloySword::new, () -> new Item.Properties().sword(GooTier.ECLIPSEALLOY.material(), 3, -2.0F).fireResistant());
    public static final DeferredHolder<Item, EclipseAlloyPickaxe> EclipseAlloyPickaxe = TOOLS.registerItem("eclipsealloy_pickaxe", EclipseAlloyPickaxe::new, () -> new Item.Properties().pickaxe(GooTier.ECLIPSEALLOY.material(), 1.0F, -2.8F).fireResistant());
    public static final DeferredHolder<Item, EclipseAlloyShovel> EclipseAlloyShovel = TOOLS.registerItem("eclipsealloy_shovel", EclipseAlloyShovel::new, () -> new Item.Properties().fireResistant());
    public static final DeferredHolder<Item, EclipseAlloyAxe> EclipseAlloyAxe = TOOLS.registerItem("eclipsealloy_axe", EclipseAlloyAxe::new, () -> new Item.Properties().fireResistant());
    public static final DeferredHolder<Item, EclipseAlloyHoe> EclipseAlloyHoe = TOOLS.registerItem("eclipsealloy_hoe", EclipseAlloyHoe::new, () -> new Item.Properties().fireResistant());
    public static final DeferredHolder<Item, EclipseAlloyPaxel> EclipseAlloyPaxel = TOOLS.registerItem("eclipsealloy_paxel", EclipseAlloyPaxel::new, () -> new Item.Properties().pickaxe(GooTier.ECLIPSEALLOY.material(), 1.0F, -2.8F).fireResistant());

    //Items - Armor
    public static final DeferredHolder<Item, FerricoreHelmet> FerricoreHelmet = ARMORS.registerItem("ferricore_helmet", FerricoreHelmet::new, () -> new Item.Properties().humanoidArmor(ArmorTiers.FERRICORE, ArmorType.HELMET));
    public static final DeferredHolder<Item, FerricoreChestplate> FerricoreChestplate = ARMORS.registerItem("ferricore_chestplate", FerricoreChestplate::new, () -> new Item.Properties().humanoidArmor(ArmorTiers.FERRICORE, ArmorType.CHESTPLATE));
    public static final DeferredHolder<Item, FerricoreLeggings> FerricoreLeggings = ARMORS.registerItem("ferricore_leggings", FerricoreLeggings::new, () -> new Item.Properties().humanoidArmor(ArmorTiers.FERRICORE, ArmorType.LEGGINGS));
    public static final DeferredHolder<Item, FerricoreBoots> FerricoreBoots = ARMORS.registerItem("ferricore_boots", FerricoreBoots::new, () -> new Item.Properties().humanoidArmor(ArmorTiers.FERRICORE, ArmorType.BOOTS));

    public static final DeferredHolder<Item, BlazegoldHelmet> BlazegoldHelmet = ARMORS.registerItem("blazegold_helmet", BlazegoldHelmet::new, () -> new Item.Properties().humanoidArmor(ArmorTiers.BLAZEGOLD, ArmorType.HELMET).fireResistant().durability(ArmorType.HELMET.getDurability(25)));
    public static final DeferredHolder<Item, BlazegoldChestplate> BlazegoldChestplate = ARMORS.registerItem("blazegold_chestplate", BlazegoldChestplate::new, () -> new Item.Properties().humanoidArmor(ArmorTiers.BLAZEGOLD, ArmorType.CHESTPLATE).fireResistant().durability(ArmorType.CHESTPLATE.getDurability(25)));
    public static final DeferredHolder<Item, BlazegoldLeggings> BlazegoldLeggings = ARMORS.registerItem("blazegold_leggings", BlazegoldLeggings::new, () -> new Item.Properties().humanoidArmor(ArmorTiers.BLAZEGOLD, ArmorType.LEGGINGS).fireResistant().durability(ArmorType.LEGGINGS.getDurability(25)));
    public static final DeferredHolder<Item, BlazegoldBoots> BlazegoldBoots = ARMORS.registerItem("blazegold_boots", BlazegoldBoots::new, () -> new Item.Properties().humanoidArmor(ArmorTiers.BLAZEGOLD, ArmorType.BOOTS).fireResistant().durability(ArmorType.BOOTS.getDurability(25)));

    public static final DeferredHolder<Item, CelestigemHelmet> CelestigemHelmet = ARMORS.registerItem("celestigem_helmet", CelestigemHelmet::new, () -> new Item.Properties().humanoidArmor(ArmorTiers.CELESTIGEM, ArmorType.HELMET).fireResistant().durability(ArmorType.HELMET.getDurability(25)));
    public static final DeferredHolder<Item, CelestigemChestplate> CelestigemChestplate = ARMORS.registerItem("celestigem_chestplate", CelestigemChestplate::new, () -> new Item.Properties().humanoidArmor(ArmorTiers.CELESTIGEM, ArmorType.CHESTPLATE).fireResistant().durability(ArmorType.CHESTPLATE.getDurability(25)));
    public static final DeferredHolder<Item, CelestigemLeggings> CelestigemLeggings = ARMORS.registerItem("celestigem_leggings", CelestigemLeggings::new, () -> new Item.Properties().humanoidArmor(ArmorTiers.CELESTIGEM, ArmorType.LEGGINGS).fireResistant().durability(ArmorType.LEGGINGS.getDurability(25)));
    public static final DeferredHolder<Item, CelestigemBoots> CelestigemBoots = ARMORS.registerItem("celestigem_boots", CelestigemBoots::new, () -> new Item.Properties().humanoidArmor(ArmorTiers.CELESTIGEM, ArmorType.BOOTS).fireResistant().durability(ArmorType.BOOTS.getDurability(25)));

    public static final DeferredHolder<Item, EclipseAlloyHelmet> EclipseAlloyHelmet = ARMORS.registerItem("eclipsealloy_helmet", EclipseAlloyHelmet::new, () -> new Item.Properties().humanoidArmor(ArmorTiers.ECLIPSEALLOY, ArmorType.HELMET).fireResistant().durability(ArmorType.HELMET.getDurability(25)));
    public static final DeferredHolder<Item, EclipseAlloyChestplate> EclipseAlloyChestplate = ARMORS.registerItem("eclipsealloy_chestplate", EclipseAlloyChestplate::new, () -> new Item.Properties().humanoidArmor(ArmorTiers.ECLIPSEALLOY, ArmorType.CHESTPLATE).fireResistant().durability(ArmorType.CHESTPLATE.getDurability(25)));
    public static final DeferredHolder<Item, EclipseAlloyLeggings> EclipseAlloyLeggings = ARMORS.registerItem("eclipsealloy_leggings", EclipseAlloyLeggings::new, () -> new Item.Properties().humanoidArmor(ArmorTiers.ECLIPSEALLOY, ArmorType.LEGGINGS).fireResistant().durability(ArmorType.LEGGINGS.getDurability(25)));
    public static final DeferredHolder<Item, EclipseAlloyBoots> EclipseAlloyBoots = ARMORS.registerItem("eclipsealloy_boots", EclipseAlloyBoots::new, () -> new Item.Properties().humanoidArmor(ArmorTiers.ECLIPSEALLOY, ArmorType.BOOTS).fireResistant().durability(ArmorType.BOOTS.getDurability(25)));

    //Items - Ability Upgrades
    public static final DeferredHolder<Item, UpgradeTemplate> TEMPLATE_FERRICORE = ITEMS.registerItem("template_ferricore", UpgradeTemplate::new, Item.Properties::new);
    public static final DeferredHolder<Item, UpgradeTemplate> TEMPLATE_BLAZEGOLD = ITEMS.registerItem("template_blazegold", UpgradeTemplate::new, Item.Properties::new);
    public static final DeferredHolder<Item, UpgradeTemplate> TEMPLATE_CELESTIGEM = ITEMS.registerItem("template_celestigem", UpgradeTemplate::new, Item.Properties::new);
    public static final DeferredHolder<Item, UpgradeTemplate> TEMPLATE_ECLIPSEALLOY = ITEMS.registerItem("template_eclipsealloy", UpgradeTemplate::new, Item.Properties::new);

    public static final DeferredHolder<Item, UpgradeBlank> UPGRADE_BASE = UPGRADES.registerItem("upgrade_blank", UpgradeBlank::new, Item.Properties::new);


    //Tier 1 Abilities
    public static final DeferredHolder<Item, Upgrade> UPGRADE_MOBSCANNER = UPGRADES.registerItem("upgrade_mobscanner", Upgrade::new, () -> new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, Upgrade> UPGRADE_OREMINER = UPGRADES.registerItem("upgrade_oreminer", Upgrade::new, () -> new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, Upgrade> UPGRADE_ORESCANNER = UPGRADES.registerItem("upgrade_orescanner", Upgrade::new, () -> new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, Upgrade> UPGRADE_LAWNMOWER = UPGRADES.registerItem("upgrade_lawnmower", Upgrade::new, () -> new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, Upgrade> UPGRADE_SKYSWEEPER = UPGRADES.registerItem("upgrade_skysweeper", Upgrade::new, () -> new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, Upgrade> UPGRADE_TREEFELLER = UPGRADES.registerItem("upgrade_treefeller", Upgrade::new, () -> new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, Upgrade> UPGRADE_LEAFBREAKER = UPGRADES.registerItem("upgrade_leafbreaker", Upgrade::new, () -> new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, Upgrade> UPGRADE_RUNSPEED = UPGRADES.registerItem("upgrade_runspeed", Upgrade::new, () -> new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, Upgrade> UPGRADE_WALKSPEED = UPGRADES.registerItem("upgrade_walkspeed", Upgrade::new, () -> new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, Upgrade> UPGRADE_STEPHEIGHT = UPGRADES.registerItem("upgrade_stepheight", Upgrade::new, () -> new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, Upgrade> UPGRADE_JUMPBOOST = UPGRADES.registerItem("upgrade_jumpboost", Upgrade::new, () -> new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, Upgrade> UPGRADE_MINDFOG = UPGRADES.registerItem("upgrade_mindfog", Upgrade::new, () -> new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, Upgrade> UPGRADE_INVULNERABILITY = UPGRADES.registerItem("upgrade_invulnerability", Upgrade::new, () -> new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, Upgrade> UPGRADE_POTIONARROW = UPGRADES.registerItem("upgrade_potionarrow", Upgrade::new, () -> new Item.Properties().stacksTo(1));

    //Tier 2 Abilities
    public static final DeferredHolder<Item, Upgrade> UPGRADE_SMELTER = UPGRADES.registerItem("upgrade_smelter", Upgrade::new, () -> new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, Upgrade> UPGRADE_SMOKER = UPGRADES.registerItem("upgrade_smoker", Upgrade::new, () -> new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, Upgrade> UPGRADE_HAMMER = UPGRADES.registerItem("upgrade_hammer", Upgrade::new, () -> new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, Upgrade> UPGRADE_CAUTERIZEWOUNDS = UPGRADES.registerItem("upgrade_cauterizewounds", Upgrade::new, () -> new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, Upgrade> UPGRADE_SWIMSPEED = UPGRADES.registerItem("upgrade_swimspeed", Upgrade::new, () -> new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, Upgrade> UPGRADE_GROUNDSTOMP = UPGRADES.registerItem("upgrade_groundstomp", Upgrade::new, () -> new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, Upgrade> UPGRADE_EXTINGUISH = UPGRADES.registerItem("upgrade_extinguish", Upgrade::new, () -> new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, Upgrade> UPGRADE_STUPEFY = UPGRADES.registerItem("upgrade_stupefy", Upgrade::new, () -> new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, Upgrade> UPGRADE_SPLASH = UPGRADES.registerItem("upgrade_splash", Upgrade::new, () -> new Item.Properties().stacksTo(1));

    //Tier 3 Abilities
    public static final DeferredHolder<Item, Upgrade> UPGRADE_ELYTRA = UPGRADES.registerItem("upgrade_elytra", Upgrade::new, () -> new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, Upgrade> UPGRADE_DROPTELEPORT = UPGRADES.registerItem("upgrade_dropteleport", Upgrade::new, () -> new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, Upgrade> UPGRADE_NEGATEFALLDAMAGE = UPGRADES.registerItem("upgrade_negatefalldamage", Upgrade::new, () -> new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, Upgrade> UPGRADE_NIGHTVISION = UPGRADES.registerItem("upgrade_nightvision", Upgrade::new, () -> new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, Upgrade> UPGRADE_DECOY = UPGRADES.registerItem("upgrade_decoy", Upgrade::new, () -> new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, Upgrade> UPGRADE_LINGERING = UPGRADES.registerItem("upgrade_lingering", Upgrade::new, () -> new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, Upgrade> UPGRADE_HOMING = UPGRADES.registerItem("upgrade_homing", Upgrade::new, () -> new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, Upgrade> UPGRADE_WATERBREATHING = UPGRADES.registerItem("upgrade_waterbreathing", Upgrade::new, () -> new Item.Properties().stacksTo(1));

    //Tier 4 Abilities
    public static final DeferredHolder<Item, Upgrade> UPGRADE_OREXRAY = UPGRADES.registerItem("upgrade_orexray", Upgrade::new, () -> new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, Upgrade> UPGRADE_GLOWING = UPGRADES.registerItem("upgrade_glowing", Upgrade::new, () -> new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, Upgrade> UPGRADE_INSTABREAK = UPGRADES.registerItem("upgrade_instabreak", Upgrade::new, () -> new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, Upgrade> UPGRADE_EARTHQUAKE = UPGRADES.registerItem("upgrade_earthquake", Upgrade::new, () -> new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, Upgrade> UPGRADE_NOAI = UPGRADES.registerItem("upgrade_noai", Upgrade::new, () -> new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, Upgrade> UPGRADE_FLIGHT = UPGRADES.registerItem("upgrade_flight", Upgrade::new, () -> new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, Upgrade> UPGRADE_LAVAIMMUNITY = UPGRADES.registerItem("upgrade_lavaimmunity", Upgrade::new, () -> new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, Upgrade> UPGRADE_PHASE = UPGRADES.registerItem("upgrade_phase", Upgrade::new, () -> new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, Upgrade> UPGRADE_DEATHPROTECTION = UPGRADES.registerItem("upgrade_deathprotection", Upgrade::new, () -> new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, Upgrade> UPGRADE_DEBUFFREMOVER = UPGRADES.registerItem("upgrade_debuffremover", Upgrade::new, () -> new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, Upgrade> UPGRADE_EPICARROW = UPGRADES.registerItem("upgrade_epicarrow", Upgrade::new, () -> new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, Upgrade> UPGRADE_TIMEPROTECTION = UPGRADES.registerItem("upgrade_time_protection", Upgrade::new, () -> new Item.Properties().stacksTo(1));

    //Entities
    public static final DeferredHolder<EntityType<?>, EntityType<CreatureCatcherEntity>> CreatureCatcherEntity = ENTITY_TYPES.register("creature_catcher",
            () -> EntityType.Builder.<CreatureCatcherEntity>of(CreatureCatcherEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(MODID, "creature_catcher"))));
    public static final DeferredHolder<EntityType<?>, EntityType<JustDireArrow>> JustDireArrow = ENTITY_TYPES.register("justdirearrow",
            () -> EntityType.Builder.<JustDireArrow>of(JustDireArrow::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .eyeHeight(0.13F)
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(MODID, "justdirearrow"))));

    public static final DeferredHolder<EntityType<?>, EntityType<PortalProjectile>> PortalProjectile = ENTITY_TYPES.register("portal_projectile",
            () -> EntityType.Builder.<PortalProjectile>of(PortalProjectile::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(MODID, "portal_projectile"))));

    public static final DeferredHolder<EntityType<?>, EntityType<PortalEntity>> PortalEntity = ENTITY_TYPES.register("portal_entity",
            () -> EntityType.Builder.<PortalEntity>of(PortalEntity::new, MobCategory.MISC)
                    .sized(1F, 2F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(MODID, "portal_entity"))));
    public static final DeferredHolder<EntityType<?>, EntityType<DecoyEntity>> DecoyEntity = ENTITY_TYPES.register("decoy_entity",
            () -> EntityType.Builder.<DecoyEntity>of(DecoyEntity::new, MobCategory.MISC)
                    .sized(1F, 2F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(MODID, "decoy_entity"))));
    public static final DeferredHolder<EntityType<?>, EntityType<JustDireAreaEffectCloud>> JustDireAreaEffectCloud = ENTITY_TYPES.register("justdireareaeffectcloud",
            () -> EntityType.Builder.<JustDireAreaEffectCloud>of(JustDireAreaEffectCloud::new, MobCategory.MISC)
                    .fireImmune()
                    .sized(6.0F, 0.5F)
                    .clientTrackingRange(10)
                    .updateInterval(Integer.MAX_VALUE)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(MODID, "justdireareaeffectcloud"))));
    public static final DeferredHolder<EntityType<?>, EntityType<TimeWandEntity>> TimeWandEntity = ENTITY_TYPES.register("time_wand_entity",
            () -> EntityType.Builder.<TimeWandEntity>of(TimeWandEntity::new, MobCategory.MISC)
                    .sized(1F, 1F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(MODID, "time_wand_entity"))));
    public static final DeferredHolder<EntityType<?>, EntityType<ParadoxEntity>> ParadoxEntity = ENTITY_TYPES.register("paradox_entity",
            () -> EntityType.Builder.<ParadoxEntity>of(ParadoxEntity::new, MobCategory.MISC)
                    .sized(1F, 1F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(MODID, "paradox_entity"))));

    //Attributes
    public static final Holder<Attribute> PHASE = ATTRIBUTES.register("phase", () -> new RangedAttribute("justdirethings.phase", 0D, 0D, Double.MAX_VALUE).setSyncable(true));


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
    public static final DeferredHolder<MenuType<?>, MenuType<GeneratorFluidT1Container>> GeneratorFluidT1_Container = CONTAINERS.register("generatorfluidt1_container",
            () -> IMenuTypeExtension.create(GeneratorFluidT1Container::new));
    public static final DeferredHolder<MenuType<?>, MenuType<GeneratorT1Container>> GeneratorT1_Container = CONTAINERS.register("generatort1_container",
            () -> IMenuTypeExtension.create(GeneratorT1Container::new));
    public static final DeferredHolder<MenuType<?>, MenuType<EnergyTransmitterContainer>> EnergyTransmitter_Container = CONTAINERS.register("energytransmitter_container",
            () -> IMenuTypeExtension.create(EnergyTransmitterContainer::new));
    public static final DeferredHolder<MenuType<?>, MenuType<BlockSwapperT1Container>> BlockSwapperT1_Container = CONTAINERS.register("blockswappert1_container",
            () -> IMenuTypeExtension.create(BlockSwapperT1Container::new));
    public static final DeferredHolder<MenuType<?>, MenuType<BlockSwapperT2Container>> BlockSwapperT2_Container = CONTAINERS.register("blockswappert2_container",
            () -> IMenuTypeExtension.create(BlockSwapperT2Container::new));
    public static final DeferredHolder<MenuType<?>, MenuType<PlayerAccessorContainer>> PlayerAccessor_Container = CONTAINERS.register("playeraccessor_container",
            () -> IMenuTypeExtension.create(PlayerAccessorContainer::new));
    public static final DeferredHolder<MenuType<?>, MenuType<FluidPlacerT1Container>> FluidPlacerT1_Container = CONTAINERS.register("fluidplacert1_container",
            () -> IMenuTypeExtension.create(FluidPlacerT1Container::new));
    public static final DeferredHolder<MenuType<?>, MenuType<FluidPlacerT2Container>> FluidPlacerT2_Container = CONTAINERS.register("fluidplacert2_container",
            () -> IMenuTypeExtension.create(FluidPlacerT2Container::new));
    public static final DeferredHolder<MenuType<?>, MenuType<FluidCollectorT1Container>> FluidCollectorT1_Container = CONTAINERS.register("fluidcollectort1_container",
            () -> IMenuTypeExtension.create(FluidCollectorT1Container::new));
    public static final DeferredHolder<MenuType<?>, MenuType<FluidCollectorT2Container>> FluidCollectorT2_Container = CONTAINERS.register("fluidcollectort2_container",
            () -> IMenuTypeExtension.create(FluidCollectorT2Container::new));
    public static final DeferredHolder<MenuType<?>, MenuType<PotionCanisterContainer>> PotionCanister_Container = CONTAINERS.register("potioncanister_container",
            () -> IMenuTypeExtension.create((windowId, inv, data) -> new PotionCanisterContainer(windowId, inv, inv.player, data)));
    public static final DeferredHolder<MenuType<?>, MenuType<ParadoxMachineContainer>> ParadoxMachine_Container = CONTAINERS.register("paradoxmachine_container",
            () -> IMenuTypeExtension.create(ParadoxMachineContainer::new));
    public static final DeferredHolder<MenuType<?>, MenuType<InventoryHolderContainer>> InventoryHolder_Container = CONTAINERS.register("inventoryholder_container",
            () -> IMenuTypeExtension.create(InventoryHolderContainer::new));
    public static final DeferredHolder<MenuType<?>, MenuType<ExperienceHolderContainer>> Experience_Holder_Container = CONTAINERS.register("experienceholder_container",
            () -> IMenuTypeExtension.create(ExperienceHolderContainer::new));

    //Data Attachments
    public static final Supplier<AttachmentType<ItemStacksResourceHandler>> HANDLER = ATTACHMENT_TYPES.register(
            "handler", () -> AttachmentType.serializable(() -> new ItemStacksResourceHandler(1)).build());
    public static final Supplier<AttachmentType<ItemStacksResourceHandler>> MACHINE_HANDLER = ATTACHMENT_TYPES.register(
            "machine_handler", () -> AttachmentType.serializable(holder -> {
                if (holder instanceof BaseMachineBE baseMachineBE)
                    return new ItemStacksResourceHandler(baseMachineBE.MACHINE_SLOTS);
                return new ItemStacksResourceHandler(1);
            }).build());
    public static final Supplier<AttachmentType<GeneratorItemHandler>> GENERATOR_ITEM_HANDLER = ATTACHMENT_TYPES.register(
            "generator_item_handler", () -> AttachmentType.serializable(holder -> {
                GeneratorItemHandler handler = holder instanceof BaseMachineBE baseMachineBE
                        ? new GeneratorItemHandler(baseMachineBE.MACHINE_SLOTS)
                        : new GeneratorItemHandler(1);
                if (holder instanceof net.minecraft.world.level.block.entity.BlockEntity be)
                    handler.setHolder(be);
                return handler;
            }).build());
    /*public static final Supplier<AttachmentType<InventoryHolderItemHandler>> INVENTORY_HOLDER_ITEM_HANDLER = ATTACHMENT_TYPES.register(
            "inventory_holder_item_handler", () -> AttachmentType.serializable(holder -> {
                if (holder instanceof InventoryHolderBE inventoryHolderBE)
                    return new InventoryHolderItemHandler(inventoryHolderBE.MACHINE_SLOTS, inventoryHolderBE);
                return new InventoryHolderItemHandler(1);
            }).build());*/
    public static final Supplier<AttachmentType<GeneratorFluidItemHandler>> GENERATOR_FLUID_ITEM_HANDLER = ATTACHMENT_TYPES.register(
            "generator_fluid_item_handler", () -> AttachmentType.serializable(holder -> {
                if (holder instanceof BaseMachineBE baseMachineBE)
                    return new GeneratorFluidItemHandler(baseMachineBE.MACHINE_SLOTS);
                return new GeneratorFluidItemHandler(1);
            }).build());
    public static final Supplier<AttachmentType<FilterBasicHandler>> HANDLER_BASIC_FILTER = ATTACHMENT_TYPES.register(
            "handler_item_collector", () -> AttachmentType.serializable(() -> new FilterBasicHandler(9)).build());
    public static final Supplier<AttachmentType<FilterBasicHandler>> HANDLER_BASIC_FILTER_ANYSIZE = ATTACHMENT_TYPES.register(
            "anysize_filter_handler", () -> AttachmentType.serializable(holder -> {
                if (holder instanceof BaseMachineBE baseMachineBE)
                    return new FilterBasicHandler(baseMachineBE.ANYSIZE_FILTER_SLOTS);
                return new FilterBasicHandler(0);
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
    public static final Supplier<AttachmentType<TransmitterEnergyStorage>> ENERGYSTORAGE_TRANSMITTERS = ATTACHMENT_TYPES.register(
            "energystorage_transmitters", () -> AttachmentType.serializable(holder -> {
                if (holder instanceof EnergyTransmitterBE energyTransmitterBE) {
                    int capacity = energyTransmitterBE.getMaxEnergy(); //Default
                    return new TransmitterEnergyStorage(capacity, energyTransmitterBE);
                } else {
                    throw new IllegalStateException("Cannot attach energy handler item to a non-EnergyTransmitter.");
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
    public static final Supplier<AttachmentType<CompoundTag>> DEATH_DATA = ATTACHMENT_TYPES.register(
            "death_data",
            () -> AttachmentType.builder(() -> new CompoundTag())
                    .serialize(new net.neoforged.neoforge.attachment.IAttachmentSerializer<CompoundTag>() {
                        @Override
                        public CompoundTag read(net.neoforged.neoforge.attachment.IAttachmentHolder holder, net.minecraft.world.level.storage.ValueInput input) {
                            return input.read("data", CompoundTag.CODEC).orElseGet(CompoundTag::new);
                        }

                        @Override
                        public boolean write(CompoundTag attachment, net.minecraft.world.level.storage.ValueOutput output) {
                            if (attachment.isEmpty()) return false;
                            output.store("data", CompoundTag.CODEC, attachment);
                            return true;
                        }
                    }).build()
    );

    //Fluids
    public static final Supplier<AttachmentType<JustDireFluidTank>> MACHINE_FLUID_HANDLER = ATTACHMENT_TYPES.register(
            "machine_fluid_handler", () -> AttachmentType.serializable(holder -> {
                if (holder instanceof FluidMachineBE fluidMachineBE)
                    return new JustDireFluidTank(fluidMachineBE.getMaxMB());
                return new JustDireFluidTank(0);
            }).build());
    public static final Supplier<AttachmentType<JustDireFluidTank>> GENERATOR_FLUID_HANDLER = ATTACHMENT_TYPES.register(
            "generator_fluid_handler", () -> AttachmentType.serializable(holder -> {
                if (holder instanceof FluidMachineBE fluidMachineBE)
                    return new JustDireFluidTank(fluidMachineBE.getMaxMB(), fluidstack -> fluidstack.getFluid() instanceof RefinedFuel);
                return new JustDireFluidTank(0);
            }).build());
    public static final Supplier<AttachmentType<JustDireFluidTank>> PARADOX_FLUID_HANDLER = ATTACHMENT_TYPES.register(
            "paradox_fluid_handler", () -> AttachmentType.serializable(holder -> {
                if (holder instanceof FluidMachineBE fluidMachineBE)
                    return new JustDireFluidTank(fluidMachineBE.getMaxMB(), fluidstack -> fluidstack.getFluid() instanceof TimeFluid);
                return new JustDireFluidTank(0);
            }).build());
}
