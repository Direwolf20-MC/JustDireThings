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
import com.direwolf20.justdirethings.common.blocks.gooblocks.*;
import com.direwolf20.justdirethings.common.blocks.resources.*;
import com.direwolf20.justdirethings.common.blocks.soil.GooSoilTier1;
import com.direwolf20.justdirethings.common.blocks.soil.GooSoilTier2;
import com.direwolf20.justdirethings.common.blocks.soil.GooSoilTier3;
import com.direwolf20.justdirethings.common.blocks.soil.GooSoilTier4;
import com.direwolf20.justdirethings.common.capabilities.*;
import com.direwolf20.justdirethings.common.containers.*;
import com.direwolf20.justdirethings.common.containers.handlers.FilterBasicHandler;
import com.direwolf20.justdirethings.common.entities.CreatureCatcherEntity;
import com.direwolf20.justdirethings.common.entities.DecoyEntity;
import com.direwolf20.justdirethings.common.entities.PortalEntity;
import com.direwolf20.justdirethings.common.entities.PortalProjectile;
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
import com.direwolf20.justdirethings.common.items.*;
import com.direwolf20.justdirethings.common.items.abilityupgrades.*;
import com.direwolf20.justdirethings.common.items.armors.*;
import com.direwolf20.justdirethings.common.items.datacomponents.JustDireDataComponents;
import com.direwolf20.justdirethings.common.items.resources.*;
import com.direwolf20.justdirethings.common.items.tools.*;
import com.direwolf20.justdirethings.datagen.recipes.AbilityRecipe;
import com.direwolf20.justdirethings.datagen.recipes.FluidDropRecipe;
import com.direwolf20.justdirethings.datagen.recipes.GooSpreadRecipe;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.common.world.chunk.TicketController;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

import static com.direwolf20.justdirethings.JustDireThings.MODID;
import static com.direwolf20.justdirethings.client.particles.ModParticles.PARTICLE_TYPES;

public class Registration {
    public static final TicketController TICKET_CONTROLLER = new TicketController(ResourceLocation.fromNamespaceAndPath(MODID, "chunk_loader"), null);

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister.Blocks FLUID_BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.FLUID_TYPES, MODID);
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(Registries.FLUID, MODID);
    public static final DeferredRegister.Blocks SIDEDBLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister.Items BUCKET_ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister.Items TOOLS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister.Items ARMORS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister.Items UPGRADES = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, MODID);
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MODID);
    private static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(Registries.MENU, MODID);
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, MODID);
    private static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(Registries.ATTRIBUTE, MODID);


    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, MODID);
    public static final Supplier<RecipeType<GooSpreadRecipe>> GOO_SPREAD_RECIPE_TYPE = RECIPE_TYPES.register("goospreadrecipe", () -> RecipeType.simple(ResourceLocation.fromNamespaceAndPath(MODID, "goospreadrecipe")));
    public static final Supplier<RecipeType<FluidDropRecipe>> FLUID_DROP_RECIPE_TYPE = RECIPE_TYPES.register("fluiddroprecipe", () -> RecipeType.simple(ResourceLocation.fromNamespaceAndPath(MODID, "fluiddroprecipe")));
    public static final Supplier<RecipeType<AbilityRecipe>> ABILITY_RECIPE_TYPE = RECIPE_TYPES.register("abilityrecipe", () -> RecipeType.simple(ResourceLocation.fromNamespaceAndPath(MODID, "abilityrecipe")));

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, JustDireThings.MODID);
    public static final Supplier<GooSpreadRecipe.Serializer> GOO_SPREAD_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("goospread", GooSpreadRecipe.Serializer::new);
    public static final Supplier<FluidDropRecipe.Serializer> FLUID_DROP_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("fluiddrop", FluidDropRecipe.Serializer::new);
    public static final Supplier<AbilityRecipe.Serializer> ABILITY_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("ability", AbilityRecipe.Serializer::new);

    private static final DeferredRegister<SoundEvent> SOUND_REGISTRY = DeferredRegister.create(Registries.SOUND_EVENT, JustDireThings.MODID);
    public static final Supplier<SoundEvent> BEEP = SOUND_REGISTRY.register("beep", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "beep")));
    public static final Supplier<SoundEvent> PORTAL_GUN_CLOSE = SOUND_REGISTRY.register("portal_gun_close", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "portal_gun_close")));
    public static final Supplier<SoundEvent> PORTAL_GUN_OPEN = SOUND_REGISTRY.register("portal_gun_open", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "portal_gun_open")));


    public static void init(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        FLUID_BLOCKS.register(eventBus);
        FLUID_TYPES.register(eventBus);
        FLUIDS.register(eventBus);
        SIDEDBLOCKS.register(eventBus);
        ITEMS.register(eventBus);
        BUCKET_ITEMS.register(eventBus);
        TOOLS.register(eventBus);
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
    public static final DeferredHolder<Block, EclipseGateBlock> EclipseGateBlock = BLOCKS.register("eclipsegateblock", EclipseGateBlock::new);

    //Fluids
    //Polymorphic Fluid
    public static final DeferredHolder<FluidType, FluidType> POLYMORPHIC_FLUID_TYPE = FLUID_TYPES.register("polymorphic_fluid_type",
            PolymorphicFluidType::new);
    public static final DeferredHolder<Fluid, PolymorphicFluid> POLYMORPHIC_FLUID_FLOWING = FLUIDS.register("polymorphic_fluid_flowing",
            PolymorphicFluid.Flowing::new);
    public static final DeferredHolder<Fluid, PolymorphicFluid> POLYMORPHIC_FLUID_SOURCE = FLUIDS.register("polymorphic_fluid_source",
            PolymorphicFluid.Source::new);
    public static final DeferredHolder<Block, LiquidBlock> POLYMORPHIC_FLUID_BLOCK = FLUID_BLOCKS.register("polymorphic_fluid_block",
            PolymorphicFluidBlock::new);
    public static final DeferredHolder<Item, BucketItem> POLYMORPHIC_FLUID_BUCKET = BUCKET_ITEMS.register("polymorphic_fluid_bucket",
            () -> new BucketItem(Registration.POLYMORPHIC_FLUID_SOURCE.get(), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    //Portal Fluid
    public static final DeferredHolder<FluidType, FluidType> PORTAL_FLUID_TYPE = FLUID_TYPES.register("portal_fluid_type",
            PortalFluidType::new);
    public static final DeferredHolder<Fluid, PortalFluid> PORTAL_FLUID_FLOWING = FLUIDS.register("portal_fluid_flowing",
            PortalFluid.Flowing::new);
    public static final DeferredHolder<Fluid, PortalFluid> PORTAL_FLUID_SOURCE = FLUIDS.register("portal_fluid_source",
            PortalFluid.Source::new);
    public static final DeferredHolder<Block, LiquidBlock> PORTAL_FLUID_BLOCK = FLUID_BLOCKS.register("portal_fluid_block",
            PortalFluidBlock::new);
    public static final DeferredHolder<Item, BucketItem> PORTAL_FLUID_BUCKET = BUCKET_ITEMS.register("portal_fluid_bucket",
            () -> new BucketItem(Registration.PORTAL_FLUID_SOURCE.get(), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    //Unstable Portal Fluid
    public static final DeferredHolder<FluidType, FluidType> UNSTABLE_PORTAL_FLUID_TYPE = FLUID_TYPES.register("unstable_portal_fluid_type",
            UnstablePortalFluidType::new);
    public static final DeferredHolder<Fluid, UnstablePortalFluid> UNSTABLE_PORTAL_FLUID_FLOWING = FLUIDS.register("unstable_portal_fluid_flowing",
            UnstablePortalFluid.Flowing::new);
    public static final DeferredHolder<Fluid, UnstablePortalFluid> UNSTABLE_PORTAL_FLUID_SOURCE = FLUIDS.register("unstable_portal_fluid_source",
            UnstablePortalFluid.Source::new);
    public static final DeferredHolder<Block, LiquidBlock> UNSTABLE_PORTAL_FLUID_BLOCK = FLUID_BLOCKS.register("unstable_portal_fluid_block",
            UnstablePortalFluidBlock::new);
    public static final DeferredHolder<Item, BucketItem> UNSTABLE_PORTAL_FLUID_BUCKET = BUCKET_ITEMS.register("unstable_portal_fluid_bucket",
            () -> new BucketItem(Registration.UNSTABLE_PORTAL_FLUID_SOURCE.get(), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    //Unrefined T2 Fuel
    public static final DeferredHolder<FluidType, FluidType> UNREFINED_T2_FLUID_TYPE = FLUID_TYPES.register("unrefined_t2_fluid_type",
            UnrefinedT2FuelType::new);
    public static final DeferredHolder<Fluid, UnrefinedT2Fuel> UNREFINED_T2_FLUID_FLOWING = FLUIDS.register("unrefined_t2_fluid_flowing",
            UnrefinedT2Fuel.Flowing::new);
    public static final DeferredHolder<Fluid, UnrefinedT2Fuel> UNREFINED_T2_FLUID_SOURCE = FLUIDS.register("unrefined_t2_fluid_source",
            UnrefinedT2Fuel.Source::new);
    public static final DeferredHolder<Block, LiquidBlock> UNREFINED_T2_FLUID_BLOCK = FLUID_BLOCKS.register("unrefined_t2_fluid_block",
            UnrefinedT2FuelBlock::new);
    public static final DeferredHolder<Item, BucketItem> UNREFINED_T2_FLUID_BUCKET = BUCKET_ITEMS.register("unrefined_t2_fluid_bucket",
            () -> new BucketItem(Registration.UNREFINED_T2_FLUID_SOURCE.get(), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    //Refined T2 Fuel
    public static final DeferredHolder<FluidType, FluidType> REFINED_T2_FLUID_TYPE = FLUID_TYPES.register("refined_t2_fluid_type",
            RefinedT2FuelType::new);
    public static final DeferredHolder<Fluid, RefinedT2Fuel> REFINED_T2_FLUID_FLOWING = FLUIDS.register("refined_t2_fluid_flowing",
            RefinedT2Fuel.Flowing::new);
    public static final DeferredHolder<Fluid, RefinedT2Fuel> REFINED_T2_FLUID_SOURCE = FLUIDS.register("refined_t2_fluid_source",
            RefinedT2Fuel.Source::new);
    public static final DeferredHolder<Block, LiquidBlock> REFINED_T2_FLUID_BLOCK = FLUID_BLOCKS.register("refined_t2_fluid_block",
            RefinedT2FuelBlock::new);
    public static final DeferredHolder<Item, BucketItem> REFINED_T2_FLUID_BUCKET = BUCKET_ITEMS.register("refined_t2_fluid_bucket",
            () -> new BucketItem(Registration.REFINED_T2_FLUID_SOURCE.get(), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    //Unrefined T3 Fuel
    public static final DeferredHolder<FluidType, FluidType> UNREFINED_T3_FLUID_TYPE = FLUID_TYPES.register("unrefined_t3_fluid_type",
            UnrefinedT3FuelType::new);
    public static final DeferredHolder<Fluid, UnrefinedT3Fuel> UNREFINED_T3_FLUID_FLOWING = FLUIDS.register("unrefined_t3_fluid_flowing",
            UnrefinedT3Fuel.Flowing::new);
    public static final DeferredHolder<Fluid, UnrefinedT3Fuel> UNREFINED_T3_FLUID_SOURCE = FLUIDS.register("unrefined_t3_fluid_source",
            UnrefinedT3Fuel.Source::new);
    public static final DeferredHolder<Block, LiquidBlock> UNREFINED_T3_FLUID_BLOCK = FLUID_BLOCKS.register("unrefined_t3_fluid_block",
            UnrefinedT3FuelBlock::new);
    public static final DeferredHolder<Item, BucketItem> UNREFINED_T3_FLUID_BUCKET = BUCKET_ITEMS.register("unrefined_t3_fluid_bucket",
            () -> new BucketItem(Registration.UNREFINED_T3_FLUID_SOURCE.get(), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    //Refined T3 Fuel
    public static final DeferredHolder<FluidType, FluidType> REFINED_T3_FLUID_TYPE = FLUID_TYPES.register("refined_t3_fluid_type",
            RefinedT3FuelType::new);
    public static final DeferredHolder<Fluid, RefinedT3Fuel> REFINED_T3_FLUID_FLOWING = FLUIDS.register("refined_t3_fluid_flowing",
            RefinedT3Fuel.Flowing::new);
    public static final DeferredHolder<Fluid, RefinedT3Fuel> REFINED_T3_FLUID_SOURCE = FLUIDS.register("refined_t3_fluid_source",
            RefinedT3Fuel.Source::new);
    public static final DeferredHolder<Block, LiquidBlock> REFINED_T3_FLUID_BLOCK = FLUID_BLOCKS.register("refined_t3_fluid_block",
            RefinedT3FuelBlock::new);
    public static final DeferredHolder<Item, BucketItem> REFINED_T3_FLUID_BUCKET = BUCKET_ITEMS.register("refined_t3_fluid_bucket",
            () -> new BucketItem(Registration.REFINED_T3_FLUID_SOURCE.get(), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    //Unrefined T4 Fuel
    public static final DeferredHolder<FluidType, FluidType> UNREFINED_T4_FLUID_TYPE = FLUID_TYPES.register("unrefined_t4_fluid_type",
            UnrefinedT4FuelType::new);
    public static final DeferredHolder<Fluid, UnrefinedT4Fuel> UNREFINED_T4_FLUID_FLOWING = FLUIDS.register("unrefined_t4_fluid_flowing",
            UnrefinedT4Fuel.Flowing::new);
    public static final DeferredHolder<Fluid, UnrefinedT4Fuel> UNREFINED_T4_FLUID_SOURCE = FLUIDS.register("unrefined_t4_fluid_source",
            UnrefinedT4Fuel.Source::new);
    public static final DeferredHolder<Block, LiquidBlock> UNREFINED_T4_FLUID_BLOCK = FLUID_BLOCKS.register("unrefined_t4_fluid_block",
            UnrefinedT4FuelBlock::new);
    public static final DeferredHolder<Item, BucketItem> UNREFINED_T4_FLUID_BUCKET = BUCKET_ITEMS.register("unrefined_t4_fluid_bucket",
            () -> new BucketItem(Registration.UNREFINED_T4_FLUID_SOURCE.get(), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    //Refined T4 Fuel
    public static final DeferredHolder<FluidType, FluidType> REFINED_T4_FLUID_TYPE = FLUID_TYPES.register("refined_t4_fluid_type",
            RefinedT4FuelType::new);
    public static final DeferredHolder<Fluid, RefinedT4Fuel> REFINED_T4_FLUID_FLOWING = FLUIDS.register("refined_t4_fluid_flowing",
            RefinedT4Fuel.Flowing::new);
    public static final DeferredHolder<Fluid, RefinedT4Fuel> REFINED_T4_FLUID_SOURCE = FLUIDS.register("refined_t4_fluid_source",
            RefinedT4Fuel.Source::new);
    public static final DeferredHolder<Block, LiquidBlock> REFINED_T4_FLUID_BLOCK = FLUID_BLOCKS.register("refined_t4_fluid_block",
            RefinedT4FuelBlock::new);
    public static final DeferredHolder<Item, BucketItem> REFINED_T4_FLUID_BUCKET = BUCKET_ITEMS.register("refined_t4_fluid_bucket",
            () -> new BucketItem(Registration.REFINED_T4_FLUID_SOURCE.get(), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));


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
    public static final DeferredHolder<Block, PlayerAccessor> PlayerAccessor = BLOCKS.register("playeraccessor", PlayerAccessor::new);
    public static final DeferredHolder<Item, BlockItem> PlayerAccessor_ITEM = ITEMS.register("playeraccessor", () -> new BlockItem(PlayerAccessor.get(), new Item.Properties()));
    public static final DeferredHolder<Block, FluidPlacerT1> FluidPlacerT1 = SIDEDBLOCKS.register("fluidplacert1", FluidPlacerT1::new);
    public static final DeferredHolder<Item, BlockItem> FluidPlacerT1_ITEM = ITEMS.register("fluidplacert1", () -> new BlockItem(FluidPlacerT1.get(), new Item.Properties()));
    public static final DeferredHolder<Block, FluidPlacerT2> FluidPlacerT2 = BLOCKS.register("fluidplacert2", FluidPlacerT2::new);
    public static final DeferredHolder<Item, BlockItem> FluidPlacerT2_ITEM = ITEMS.register("fluidplacert2", () -> new BlockItem(FluidPlacerT2.get(), new Item.Properties()));
    public static final DeferredHolder<Block, FluidCollectorT1> FluidCollectorT1 = SIDEDBLOCKS.register("fluidcollectort1", FluidCollectorT1::new);
    public static final DeferredHolder<Item, BlockItem> FluidCollectorT1_ITEM = ITEMS.register("fluidcollectort1", () -> new BlockItem(FluidCollectorT1.get(), new Item.Properties()));
    public static final DeferredHolder<Block, FluidCollectorT2> FluidCollectorT2 = BLOCKS.register("fluidcollectort2", FluidCollectorT2::new);
    public static final DeferredHolder<Item, BlockItem> FluidCollectorT2_ITEM = ITEMS.register("fluidcollectort2", () -> new BlockItem(FluidCollectorT2.get(), new Item.Properties()));

    //Power Machines
    public static final DeferredHolder<Block, GeneratorT1> GeneratorT1 = BLOCKS.register("generatort1", GeneratorT1::new);
    public static final DeferredHolder<Block, GeneratorFluidT1> GeneratorFluidT1 = BLOCKS.register("generatorfluidt1", GeneratorFluidT1::new);
    public static final DeferredHolder<Item, BlockItem> GeneratorT1_ITEM = ITEMS.register("generatort1", () -> new BlockItem(GeneratorT1.get(), new Item.Properties()));
    public static final DeferredHolder<Item, BlockItem> GeneratorFluidT1_ITEM = ITEMS.register("generatorfluidt1", () -> new BlockItem(GeneratorFluidT1.get(), new Item.Properties()));
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
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<GeneratorFluidT1BE>> GeneratorFluidT1BE = BLOCK_ENTITIES.register("generatorfluidt1", () -> BlockEntityType.Builder.of(GeneratorFluidT1BE::new, GeneratorFluidT1.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<EnergyTransmitterBE>> EnergyTransmitterBE = BLOCK_ENTITIES.register("energytransmitter", () -> BlockEntityType.Builder.of(EnergyTransmitterBE::new, EnergyTransmitter.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockSwapperT1BE>> BlockSwapperT1BE = BLOCK_ENTITIES.register("blockswappert1", () -> BlockEntityType.Builder.of(BlockSwapperT1BE::new, BlockSwapperT1.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockSwapperT2BE>> BlockSwapperT2BE = BLOCK_ENTITIES.register("blockswappert2", () -> BlockEntityType.Builder.of(BlockSwapperT2BE::new, BlockSwapperT2.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PlayerAccessorBE>> PlayerAccessorBE = BLOCK_ENTITIES.register("playeraccessorbe", () -> BlockEntityType.Builder.of(PlayerAccessorBE::new, PlayerAccessor.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<EclipseGateBE>> EclipseGateBE = BLOCK_ENTITIES.register("eclipsegatebe", () -> BlockEntityType.Builder.of(EclipseGateBE::new, EclipseGateBlock.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FluidPlacerT1BE>> FluidPlacerT1BE = BLOCK_ENTITIES.register("fluidplacert1", () -> BlockEntityType.Builder.of(FluidPlacerT1BE::new, FluidPlacerT1.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FluidPlacerT2BE>> FluidPlacerT2BE = BLOCK_ENTITIES.register("fluidplacert2", () -> BlockEntityType.Builder.of(FluidPlacerT2BE::new, FluidPlacerT2.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FluidCollectorT1BE>> FluidCollectorT1BE = BLOCK_ENTITIES.register("fluidcollectort1", () -> BlockEntityType.Builder.of(FluidCollectorT1BE::new, FluidCollectorT1.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FluidCollectorT2BE>> FluidCollectorT2BE = BLOCK_ENTITIES.register("fluidcollectort2", () -> BlockEntityType.Builder.of(FluidCollectorT2BE::new, FluidCollectorT2.get()).build(null));

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
    public static final DeferredHolder<Item, PolymorphicCatalyst> PolymorphicCatalyst = ITEMS.register("polymorphic_catalyst", PolymorphicCatalyst::new);
    public static final DeferredHolder<Item, PortalFluidCatalyst> PortalFluidCatalyst = ITEMS.register("portal_fluid_catalyst", PortalFluidCatalyst::new);

    //Items
    public static final DeferredHolder<Item, FuelCanister> Fuel_Canister = ITEMS.register("fuel_canister", FuelCanister::new);
    public static final DeferredHolder<Item, PocketGenerator> Pocket_Generator = ITEMS.register("pocket_generator", PocketGenerator::new);
    public static final DeferredHolder<Item, FerricoreWrench> FerricoreWrench = ITEMS.register("ferricore_wrench", FerricoreWrench::new);
    public static final DeferredHolder<Item, TotemOfDeathRecall> TotemOfDeathRecall = ITEMS.register("totem_of_death_recall", TotemOfDeathRecall::new);
    public static final DeferredHolder<Item, BlazejetWand> BlazejetWand = ITEMS.register("blazejet_wand", BlazejetWand::new);
    public static final DeferredHolder<Item, VoidshiftWand> VoidshiftWand = ITEMS.register("voidshift_wand", VoidshiftWand::new);
    public static final DeferredHolder<Item, EclipsegateWand> EclipsegateWand = ITEMS.register("eclipsegate_wand", EclipsegateWand::new);
    public static final DeferredHolder<Item, CreatureCatcher> CreatureCatcher = ITEMS.register("creaturecatcher", CreatureCatcher::new);
    public static final DeferredHolder<Item, MachineSettingsCopier> MachineSettingsCopier = ITEMS.register("machinesettingscopier", MachineSettingsCopier::new);
    public static final DeferredHolder<Item, PortalGun> PortalGun = ITEMS.register("portalgun", PortalGun::new);
    public static final DeferredHolder<Item, PortalGunV2> PortalGunV2 = ITEMS.register("portalgun_v2", PortalGunV2::new);
    public static final DeferredHolder<Item, FluidCanister> FluidCanister = ITEMS.register("fluid_canister", FluidCanister::new);

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

    //Items - Armor
    public static final DeferredHolder<Item, FerricoreHelmet> FerricoreHelmet = ARMORS.register("ferricore_helmet", FerricoreHelmet::new);
    public static final DeferredHolder<Item, FerricoreChestplate> FerricoreChestplate = ARMORS.register("ferricore_chestplate", FerricoreChestplate::new);
    public static final DeferredHolder<Item, FerricoreLeggings> FerricoreLeggings = ARMORS.register("ferricore_leggings", FerricoreLeggings::new);
    public static final DeferredHolder<Item, FerricoreBoots> FerricoreBoots = ARMORS.register("ferricore_boots", FerricoreBoots::new);

    public static final DeferredHolder<Item, BlazegoldHelmet> BlazegoldHelmet = ARMORS.register("blazegold_helmet", BlazegoldHelmet::new);
    public static final DeferredHolder<Item, BlazegoldChestplate> BlazegoldChestplate = ARMORS.register("blazegold_chestplate", BlazegoldChestplate::new);
    public static final DeferredHolder<Item, BlazegoldLeggings> BlazegoldLeggings = ARMORS.register("blazegold_leggings", BlazegoldLeggings::new);
    public static final DeferredHolder<Item, BlazegoldBoots> BlazegoldBoots = ARMORS.register("blazegold_boots", BlazegoldBoots::new);

    public static final DeferredHolder<Item, CelestigemHelmet> CelestigemHelmet = ARMORS.register("celestigem_helmet", CelestigemHelmet::new);
    public static final DeferredHolder<Item, CelestigemChestplate> CelestigemChestplate = ARMORS.register("celestigem_chestplate", CelestigemChestplate::new);
    public static final DeferredHolder<Item, CelestigemLeggings> CelestigemLeggings = ARMORS.register("celestigem_leggings", CelestigemLeggings::new);
    public static final DeferredHolder<Item, CelestigemBoots> CelestigemBoots = ARMORS.register("celestigem_boots", CelestigemBoots::new);

    public static final DeferredHolder<Item, EclipseAlloyHelmet> EclipseAlloyHelmet = ARMORS.register("eclipsealloy_helmet", EclipseAlloyHelmet::new);
    public static final DeferredHolder<Item, EclipseAlloyChestplate> EclipseAlloyChestplate = ARMORS.register("eclipsealloy_chestplate", EclipseAlloyChestplate::new);
    public static final DeferredHolder<Item, EclipseAlloyLeggings> EclipseAlloyLeggings = ARMORS.register("eclipsealloy_leggings", EclipseAlloyLeggings::new);
    public static final DeferredHolder<Item, EclipseAlloyBoots> EclipseAlloyBoots = ARMORS.register("eclipsealloy_boots", EclipseAlloyBoots::new);

    //Items - Ability Upgrades
    public static final DeferredHolder<Item, TemplateFerricore> TEMPLATE_FERRICORE = ITEMS.register("template_ferricore", TemplateFerricore::new);
    public static final DeferredHolder<Item, TemplateBlazeGold> TEMPLATE_BLAZEGOLD = ITEMS.register("template_blazegold", TemplateBlazeGold::new);
    public static final DeferredHolder<Item, TemplateCelestigem> TEMPLATE_CELESTIGEM = ITEMS.register("template_celestigem", TemplateCelestigem::new);
    public static final DeferredHolder<Item, TemplateEclipseAlloy> TEMPLATE_ECLIPSEALLOY = ITEMS.register("template_eclipsealloy", TemplateEclipseAlloy::new);

    public static final DeferredHolder<Item, UpgradeBlank> UPGRADE_BASE = UPGRADES.register("upgrade_blank", UpgradeBlank::new);
    public static final DeferredHolder<Item, UpgradeElytra> UPGRADE_ELYTRA = UPGRADES.register("upgrade_elytra", UpgradeElytra::new);
    public static final DeferredHolder<Item, UpgradeDeathProtection> UPGRADE_DEATHPROTECTION = UPGRADES.register("upgrade_deathprotection", UpgradeDeathProtection::new);


    //Entities
    public static final DeferredHolder<EntityType<?>, EntityType<CreatureCatcherEntity>> CreatureCatcherEntity = ENTITY_TYPES.register("creature_catcher",
            () -> EntityType.Builder.<CreatureCatcherEntity>of(CreatureCatcherEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build("creature_catcher"));

    public static final DeferredHolder<EntityType<?>, EntityType<PortalProjectile>> PortalProjectile = ENTITY_TYPES.register("portal_projectile",
            () -> EntityType.Builder.<PortalProjectile>of(PortalProjectile::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build("portal_projectile"));

    public static final DeferredHolder<EntityType<?>, EntityType<PortalEntity>> PortalEntity = ENTITY_TYPES.register("portal_entity",
            () -> EntityType.Builder.<PortalEntity>of(PortalEntity::new, MobCategory.MISC)
                    .sized(1F, 2F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build("portal_entity"));
    public static final DeferredHolder<EntityType<?>, EntityType<DecoyEntity>> DecoyEntity = ENTITY_TYPES.register("decoy_entity",
            () -> EntityType.Builder.<DecoyEntity>of(DecoyEntity::new, MobCategory.MISC)
                    .sized(1F, 2F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build("decoy_entity"));

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

    //Data Attachments
    public static final Supplier<AttachmentType<ItemStackHandler>> HANDLER = ATTACHMENT_TYPES.register(
            "handler", () -> AttachmentType.serializable(() -> new ItemStackHandler(1)).build());
    public static final Supplier<AttachmentType<ItemStackHandler>> MACHINE_HANDLER = ATTACHMENT_TYPES.register(
            "machine_handler", () -> AttachmentType.serializable(holder -> {
                if (holder instanceof BaseMachineBE baseMachineBE)
                    return new ItemStackHandler(baseMachineBE.MACHINE_SLOTS);
                return new ItemStackHandler(1);
            }).build());
    public static final Supplier<AttachmentType<GeneratorItemHandler>> GENERATOR_ITEM_HANDLER = ATTACHMENT_TYPES.register(
            "generator_item_handler", () -> AttachmentType.serializable(holder -> {
                if (holder instanceof BaseMachineBE baseMachineBE)
                    return new GeneratorItemHandler(baseMachineBE.MACHINE_SLOTS);
                return new GeneratorItemHandler(1);
            }).build());
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
            () -> AttachmentType.builder(CompoundTag::new).serialize(CompoundTag.CODEC).build()
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
}
