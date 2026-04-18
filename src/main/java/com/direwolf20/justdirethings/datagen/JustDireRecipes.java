package com.direwolf20.justdirethings.datagen;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.common.blocks.baseblocks.BaseMachineBlock;
import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.common.items.interfaces.ToggleableTool;
import com.direwolf20.justdirethings.datagen.recipes.*;
import com.direwolf20.justdirethings.setup.JDTRegistration;
import com.direwolf20.justdirethings.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.crafting.BlockTagIngredient;

import java.util.EnumSet;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class JustDireRecipes extends RecipeProvider {

    public JustDireRecipes(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    private static String idPath(Item item) {
        return net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(item).getPath();
    }

    @Override
    protected void buildRecipes() {
        //Goo Blocks
        this.shaped(RecipeCategory.MISC, JDTRegistration.GooBlock_Tier1.get())
                .pattern("csc")
                .pattern("fdf")
                .pattern("csc")
                .define('d', Items.DIRT)
                .define('c', Items.CLAY_BALL)
                .define('s', Items.SUGAR)
                .define('f', Items.ROTTEN_FLESH)
                .group("justdirethings")
                .unlockedBy("has_coal", this.has(Items.COAL))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.GooBlock_Tier2.get())
                .pattern("brb")
                .pattern("wgw")
                .pattern("brb")
                .define('g', JDTRegistration.GooBlock_Tier1_ITEM.get())
                .define('b', Items.BLAZE_POWDER)
                .define('r', Tags.Items.DUSTS_REDSTONE)
                .define('w', Items.NETHER_WART)
                .group("justdirethings")
                .unlockedBy("has_goo_block1", this.has(JDTRegistration.GooBlock_Tier1_ITEM.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.GooBlock_Tier3.get())
                .pattern("brb")
                .pattern("wgw")
                .pattern("brb")
                .define('g', JDTRegistration.GooBlock_Tier2_ITEM.get())
                .define('b', Items.ENDER_PEARL)
                .define('r', Items.END_STONE)
                .define('w', Items.DRAGON_BREATH)
                .group("justdirethings")
                .unlockedBy("has_goo_block2", this.has(JDTRegistration.GooBlock_Tier2_ITEM.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.GooBlock_Tier4.get())
                .pattern("brb")
                .pattern("wgw")
                .pattern("brb")
                .define('g', JDTRegistration.GooBlock_Tier3_ITEM.get())
                .define('b', Items.SCULK)
                .define('r', Items.SCULK_SHRIEKER)
                .define('w', Items.ECHO_SHARD)
                .group("justdirethings")
                .unlockedBy("has_goo_block3", this.has(JDTRegistration.GooBlock_Tier3_ITEM.get()))
                .save(this.output);

        //Machines
        this.shaped(RecipeCategory.MISC, JDTRegistration.ItemCollector.get())
                .pattern(" d ")
                .pattern("heh")
                .pattern("fff")
                .define('e', Items.ENDER_PEARL)
                .define('f', JDTRegistration.FerricoreIngot.get())
                .define('d', Items.DIAMOND)
                .define('h', Items.HOPPER)
                .group("justdirethings")
                .unlockedBy("has_ferricore_ingot", this.has(JDTRegistration.FerricoreIngot.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.EnergyTransmitter.get())
                .pattern(" d ")
                .pattern("heh")
                .pattern("fff")
                .define('e', Items.ENDER_PEARL)
                .define('f', JDTRegistration.BlazegoldIngot.get())
                .define('d', JDTRegistration.Celestigem.get())
                .define('h', Items.REDSTONE)
                .group("justdirethings")
                .unlockedBy("has_blazegold_ingot", this.has(JDTRegistration.BlazegoldIngot.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.ExperienceHolder.get())
                .pattern(" d ")
                .pattern("heh")
                .pattern("fff")
                .define('e', Items.ENDER_PEARL)
                .define('f', JDTRegistration.BlazegoldIngot.get())
                .define('d', Items.EMERALD)
                .define('h', Items.BOOK)
                .group("justdirethings")
                .unlockedBy("has_blazegold_ingot", this.has(JDTRegistration.BlazegoldIngot.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.BlockBreakerT1.get())
                .pattern("fdf")
                .pattern("lol")
                .pattern("frf")
                .define('o', Items.OBSERVER)
                .define('f', JDTRegistration.FerricoreIngot.get())
                .define('d', Items.DIAMOND)
                .define('l', Items.LAPIS_LAZULI)
                .define('r', Items.REDSTONE)
                .group("justdirethings")
                .unlockedBy("has_ferricore_ingot", this.has(JDTRegistration.FerricoreIngot.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.BlockBreakerT2.get())
                .pattern("fdf")
                .pattern("dod")
                .pattern("frf")
                .define('o', JDTRegistration.BlockBreakerT1_ITEM.get())
                .define('f', JDTRegistration.Celestigem.get())
                .define('d', Items.ENDER_EYE)
                .define('r', Items.REDSTONE)
                .group("justdirethings")
                .unlockedBy("has_celestigem", this.has(JDTRegistration.Celestigem.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.BlockPlacerT1.get())
                .pattern("fdf")
                .pattern("lol")
                .pattern("frf")
                .define('o', Items.DISPENSER)
                .define('f', JDTRegistration.FerricoreIngot.get())
                .define('d', Items.DIAMOND)
                .define('l', Items.LAPIS_LAZULI)
                .define('r', Items.REDSTONE)
                .group("justdirethings")
                .unlockedBy("has_ferricore_ingot", this.has(JDTRegistration.FerricoreIngot.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.BlockPlacerT2.get())
                .pattern("fdf")
                .pattern("dod")
                .pattern("frf")
                .define('o', JDTRegistration.BlockPlacerT1_ITEM.get())
                .define('f', JDTRegistration.Celestigem.get())
                .define('d', Items.ENDER_EYE)
                .define('r', Items.REDSTONE)
                .group("justdirethings")
                .unlockedBy("has_celestigem", this.has(JDTRegistration.Celestigem.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.ClickerT1.get())
                .pattern("fdf")
                .pattern("lol")
                .pattern("frf")
                .define('o', Items.DISPENSER)
                .define('f', JDTRegistration.FerricoreIngot.get())
                .define('d', Items.ENDER_EYE)
                .define('l', Items.LAPIS_LAZULI)
                .define('r', Items.REDSTONE)
                .group("justdirethings")
                .unlockedBy("has_ferricore_ingot", this.has(JDTRegistration.FerricoreIngot.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.ClickerT2.get())
                .pattern("fdf")
                .pattern("dod")
                .pattern("frf")
                .define('o', JDTRegistration.ClickerT1_ITEM.get())
                .define('f', JDTRegistration.Celestigem.get())
                .define('d', Items.ENDER_EYE)
                .define('r', Items.REDSTONE)
                .group("justdirethings")
                .unlockedBy("has_celestigem", this.has(JDTRegistration.Celestigem.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.SensorT1.get())
                .pattern("fdf")
                .pattern("lol")
                .pattern("frf")
                .define('o', Items.OBSERVER)
                .define('f', JDTRegistration.FerricoreIngot.get())
                .define('d', Items.ENDER_EYE)
                .define('l', Items.LAPIS_LAZULI)
                .define('r', Items.REDSTONE)
                .group("justdirethings")
                .unlockedBy("has_ferricore_ingot", this.has(JDTRegistration.FerricoreIngot.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.SensorT2.get())
                .pattern("fdf")
                .pattern("dod")
                .pattern("frf")
                .define('o', JDTRegistration.SensorT1_ITEM.get())
                .define('f', JDTRegistration.Celestigem.get())
                .define('d', Items.ENDER_EYE)
                .define('r', Items.REDSTONE)
                .group("justdirethings")
                .unlockedBy("has_celestigem", this.has(JDTRegistration.Celestigem.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.DropperT1.get())
                .pattern("fdf")
                .pattern("lol")
                .pattern("frf")
                .define('o', Items.DROPPER)
                .define('f', JDTRegistration.FerricoreIngot.get())
                .define('d', Items.REDSTONE)
                .define('l', Items.LAPIS_LAZULI)
                .define('r', Items.REDSTONE)
                .group("justdirethings")
                .unlockedBy("has_ferricore_ingot", this.has(JDTRegistration.FerricoreIngot.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.DropperT2.get())
                .pattern("fdf")
                .pattern("dod")
                .pattern("frf")
                .define('o', JDTRegistration.DropperT1_ITEM.get())
                .define('f', JDTRegistration.Celestigem.get())
                .define('d', Items.ENDER_EYE)
                .define('r', Items.REDSTONE)
                .group("justdirethings")
                .unlockedBy("has_celestigem", this.has(JDTRegistration.Celestigem.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.GeneratorT1.get())
                .pattern("iri")
                .pattern("cfc")
                .pattern("iri")
                .define('i', JDTRegistration.FerricoreIngot.get())
                .define('c', Items.COAL)
                .define('r', Items.REDSTONE)
                .define('f', Items.BLAST_FURNACE)
                .group("justdirethings")
                .unlockedBy("has_ferricore_ingot", this.has(JDTRegistration.FerricoreIngot.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.GeneratorFluidT1.get())
                .pattern("grg")
                .pattern("bfb")
                .pattern("grg")
                .define('g', JDTRegistration.BlazegoldIngot.get())
                .define('b', Items.BUCKET)
                .define('r', Items.REDSTONE)
                .define('f', Items.BLAST_FURNACE)
                .group("justdirethings")
                .unlockedBy("has_blazegold_ingot", this.has(JDTRegistration.BlazegoldIngot.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.BlockSwapperT1.get())
                .pattern("fdf")
                .pattern("lol")
                .pattern("frf")
                .define('o', Items.OBSERVER)
                .define('f', JDTRegistration.BlazegoldIngot.get())
                .define('d', Items.ENDER_EYE)
                .define('l', Items.LAPIS_LAZULI)
                .define('r', Items.REDSTONE)
                .group("justdirethings")
                .unlockedBy("has_blazegold_ingot", this.has(JDTRegistration.BlazegoldIngot.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.BlockSwapperT2.get())
                .pattern("fdf")
                .pattern("dod")
                .pattern("frf")
                .define('o', JDTRegistration.BlockSwapperT1_ITEM.get())
                .define('f', JDTRegistration.EclipseAlloyIngot.get())
                .define('d', Items.ENDER_EYE)
                .define('r', Items.REDSTONE)
                .group("justdirethings")
                .unlockedBy("has_eclipsealloy_ingot", this.has(JDTRegistration.EclipseAlloyIngot.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.PlayerAccessor.get())
                .pattern("fdf")
                .pattern("lol")
                .pattern("frf")
                .define('o', Items.DISPENSER)
                .define('f', JDTRegistration.BlazegoldIngot.get())
                .define('d', Items.ENDER_EYE)
                .define('l', Items.ENDER_PEARL)
                .define('r', Items.LAPIS_LAZULI)
                .group("justdirethings")
                .unlockedBy("has_blazegold_ingot", this.has(JDTRegistration.BlazegoldIngot.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.InventoryHolder.get())
                .pattern(" f ")
                .pattern("fof")
                .pattern(" f ")
                .define('o', Items.ARMOR_STAND)
                .define('f', JDTRegistration.BlazegoldIngot.get())
                .group("justdirethings")
                .unlockedBy("has_blazegold_ingot", this.has(JDTRegistration.BlazegoldIngot.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.FluidPlacerT1.get())
                .pattern("fdf")
                .pattern("lol")
                .pattern("frf")
                .define('o', Items.DROPPER)
                .define('f', JDTRegistration.FerricoreIngot.get())
                .define('d', Items.BUCKET)
                .define('l', Items.LAPIS_LAZULI)
                .define('r', Items.REDSTONE)
                .group("justdirethings")
                .unlockedBy("has_ferricore_ingot", this.has(JDTRegistration.FerricoreIngot.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.FluidPlacerT2.get())
                .pattern("fdf")
                .pattern("dod")
                .pattern("frf")
                .define('o', JDTRegistration.FluidPlacerT1_ITEM.get())
                .define('f', JDTRegistration.Celestigem.get())
                .define('d', Items.ENDER_EYE)
                .define('r', Items.REDSTONE)
                .group("justdirethings")
                .unlockedBy("has_celestigem", this.has(JDTRegistration.Celestigem.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.FluidCollectorT1.get())
                .pattern("fdf")
                .pattern("lol")
                .pattern("frf")
                .define('o', Items.DISPENSER)
                .define('f', JDTRegistration.FerricoreIngot.get())
                .define('d', Items.BUCKET)
                .define('l', Items.LAPIS_LAZULI)
                .define('r', Items.REDSTONE)
                .group("justdirethings")
                .unlockedBy("has_ferricore_ingot", this.has(JDTRegistration.FerricoreIngot.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.FluidCollectorT2.get())
                .pattern("fdf")
                .pattern("dod")
                .pattern("frf")
                .define('o', JDTRegistration.FluidCollectorT1_ITEM.get())
                .define('f', JDTRegistration.Celestigem.get())
                .define('d', Items.ENDER_EYE)
                .define('r', Items.REDSTONE)
                .group("justdirethings")
                .unlockedBy("has_celestigem", this.has(JDTRegistration.Celestigem.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.ParadoxMachine.get())
                .pattern("fdf")
                .pattern("dod")
                .pattern("frf")
                .define('o', JDTRegistration.TimeCrystalBlock.get())
                .define('f', JDTRegistration.EclipseAlloyIngot.get())
                .define('d', JDTRegistration.TimeCrystal.get())
                .define('r', JDTRegistration.TIME_FLUID_BUCKET.get())
                .group("justdirethings")
                .unlockedBy("has_eclipsealloy_ingot", this.has(JDTRegistration.EclipseAlloyIngot.get()))
                .save(this.output);

        //Resource
        this.shapeless(RecipeCategory.MISC, JDTRegistration.PolymorphicCatalyst.get(), 4)
                .requires(JDTRegistration.BlazegoldIngot.get())
                .requires(Items.NETHER_WART)
                .group("justdirethings")
                .unlockedBy("has_blazegold_ingot", this.has(JDTRegistration.BlazegoldIngot.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.PortalFluidCatalyst.get())
                .pattern(" c ")
                .pattern("clc")
                .pattern(" c ")
                .define('c', Items.CHORUS_FRUIT)
                .define('l', Items.LAPIS_LAZULI)
                .group("justdirethings")
                .unlockedBy("has_chorus_fruit", this.has(Items.CHORUS_FRUIT))
                .save(this.output);

        //Items
        this.shaped(RecipeCategory.MISC, JDTRegistration.FerricoreWrench.get())
                .pattern(" i ")
                .pattern(" ii")
                .pattern("i  ")
                .define('i', JDTRegistration.FerricoreIngot.get())
                .group("justdirethings")
                .unlockedBy("has_ferricore_ingot", this.has(JDTRegistration.FerricoreIngot.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.TotemOfDeathRecall.get())
                .pattern("bdb")
                .pattern("lel")
                .pattern("bdb")
                .define('b', JDTRegistration.BlazegoldIngot.get())
                .define('e', Items.ENDER_PEARL)
                .define('d', Items.DIAMOND)
                .define('l', Items.LAPIS_LAZULI)
                .group("justdirethings")
                .unlockedBy("has_blazegold_ingot", this.has(JDTRegistration.BlazegoldIngot.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.Fuel_Canister.get())
                .pattern(" i ")
                .pattern("ici")
                .pattern(" i ")
                .define('i', JDTRegistration.FerricoreIngot.get())
                .define('c', Items.COAL)
                .group("justdirethings")
                .unlockedBy("has_ferricore_ingot", this.has(JDTRegistration.FerricoreIngot.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.FluidCanister.get())
                .pattern(" i ")
                .pattern("igi")
                .pattern(" i ")
                .define('i', JDTRegistration.FerricoreIngot.get())
                .define('g', Items.GLASS)
                .group("justdirethings")
                .unlockedBy("has_ferricore_ingot", this.has(JDTRegistration.FerricoreIngot.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.PotionCanister.get())
                .pattern(" i ")
                .pattern("igi")
                .pattern(" i ")
                .define('i', JDTRegistration.FerricoreIngot.get())
                .define('g', Items.GLASS_BOTTLE)
                .group("justdirethings")
                .unlockedBy("has_ferricore_ingot", this.has(JDTRegistration.FerricoreIngot.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.Pocket_Generator.get())
                .pattern("iri")
                .pattern("cfc")
                .pattern("iri")
                .define('i', JDTRegistration.FerricoreIngot.get())
                .define('c', Items.COAL)
                .define('r', Items.REDSTONE_BLOCK)
                .define('f', Items.FURNACE)
                .group("justdirethings")
                .unlockedBy("has_ferricore_ingot", this.has(JDTRegistration.FerricoreIngot.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.BlazejetWand.get())
                .pattern("  b")
                .pattern(" i ")
                .pattern("i  ")
                .define('i', JDTRegistration.FerricoreIngot.get())
                .define('b', JDTRegistration.BlazegoldIngot.get())
                .group("justdirethings")
                .unlockedBy("has_blazegold_ingot", this.has(JDTRegistration.BlazegoldIngot.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.VoidshiftWand.get())
                .pattern("  c")
                .pattern(" w ")
                .pattern("b  ")
                .define('b', JDTRegistration.BlazegoldIngot.get())
                .define('w', JDTRegistration.BlazejetWand.get())
                .define('c', JDTRegistration.Celestigem.get())
                .group("justdirethings")
                .unlockedBy("has_celestigem", this.has(JDTRegistration.Celestigem.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.EclipsegateWand.get())
                .pattern("  e")
                .pattern(" w ")
                .pattern("c  ")
                .define('e', JDTRegistration.EclipseAlloyIngot.get())
                .define('w', JDTRegistration.VoidshiftWand.get())
                .define('c', JDTRegistration.Celestigem.get())
                .group("justdirethings")
                .unlockedBy("has_eclipsealloy_ingot", this.has(JDTRegistration.EclipseAlloyIngot.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.PolymorphicWand.get())
                .pattern("  b")
                .pattern(" r ")
                .pattern("i  ")
                .define('i', JDTRegistration.BlazegoldIngot.get())
                .define('r', JDTRegistration.PolymorphicCatalyst.get())
                .define('b', JDTRegistration.FluidCanister.get())
                .group("justdirethings")
                .unlockedBy("has_blazegold_ingot", this.has(JDTRegistration.BlazegoldIngot.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.PolymorphicWandV2.get())
                .pattern("  b")
                .pattern(" r ")
                .pattern("i  ")
                .define('i', JDTRegistration.Celestigem.get())
                .define('r', JDTRegistration.PolymorphicWand.get())
                .define('b', JDTRegistration.EclipseAlloyIngot.get())
                .group("justdirethings")
                .unlockedBy("has_eclipsealloy_ingot", this.has(JDTRegistration.EclipseAlloyIngot.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.TimeWand.get())
                .pattern(" bt")
                .pattern(" ib")
                .pattern("i  ")
                .define('i', JDTRegistration.FerricoreIngot.get())
                .define('b', JDTRegistration.BlazegoldIngot.get())
                .define('t', JDTRegistration.TimeCrystal.get())
                .group("justdirethings")
                .unlockedBy("has_time_crystal", this.has(JDTRegistration.TimeCrystal.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.CreatureCatcher.get())
                .pattern(" b ")
                .pattern("beb")
                .pattern(" b ")
                .define('b', JDTRegistration.BlazegoldIngot.get())
                .define('e', Items.ENDER_PEARL)
                .group("justdirethings")
                .unlockedBy("has_eclipsealloy_ingot", this.has(JDTRegistration.EclipseAlloyIngot.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.MachineSettingsCopier.get())
                .pattern("  c")
                .pattern(" p ")
                .pattern("f  ")
                .define('f', JDTRegistration.FerricoreIngot.get())
                .define('p', Items.PAPER)
                .define('c', JDTRegistration.Coal_T1.get())
                .group("justdirethings")
                .unlockedBy("has_ferricore_ingot", this.has(JDTRegistration.FerricoreIngot.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.PortalGun.get())
                .pattern(" bb")
                .pattern("beb")
                .pattern("b  ")
                .define('e', Items.ENDER_EYE)
                .define('b', JDTRegistration.BlazegoldIngot.get())
                .group("justdirethings")
                .unlockedBy("has_blazegold_ingot", this.has(JDTRegistration.BlazegoldIngot.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.PortalGunV2.get())
                .pattern(" bc")
                .pattern("beb")
                .pattern("bf ")
                .define('e', JDTRegistration.PortalFluidCatalyst.get())
                .define('b', JDTRegistration.BlazegoldIngot.get())
                .define('c', JDTRegistration.Celestigem.get())
                .define('f', JDTRegistration.FluidCanister.get())
                .group("justdirethings")
                .unlockedBy("has_celestigem", this.has(JDTRegistration.Celestigem.get()))
                .save(this.output);

        //Upgrades
        this.shaped(RecipeCategory.MISC, JDTRegistration.UPGRADE_BASE.get())
                .pattern(" f ")
                .pattern("f f")
                .pattern(" f ")
                .define('f', JDTRegistration.FerricoreIngot.get())
                .group("justdirethings")
                .unlockedBy("has_ferricore_ingot", this.has(JDTRegistration.FerricoreIngot.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.TEMPLATE_FERRICORE.get())
                .pattern("f f")
                .pattern("fbf")
                .pattern(" f ")
                .define('b', JDTRegistration.UPGRADE_BASE.get())
                .define('f', JDTRegistration.FerricoreIngot.get())
                .unlockedBy("has_upgrade_base", this.has(JDTRegistration.UPGRADE_BASE.get()))
                .save(this.output);
        this.shapeless(RecipeCategory.MISC, JDTRegistration.TEMPLATE_FERRICORE.get(), 2)
                .requires(JDTRegistration.TEMPLATE_FERRICORE.get())
                .requires(JDTRegistration.FerricoreIngot.get(), 2)
                .unlockedBy("has_upgrade_base", this.has(JDTRegistration.UPGRADE_BASE.get()))
                .save(this.output, idPath(JDTRegistration.TEMPLATE_FERRICORE.get()) + "-duplicate");
        this.shaped(RecipeCategory.MISC, JDTRegistration.TEMPLATE_BLAZEGOLD.get())
                .pattern("f f")
                .pattern("fbf")
                .pattern(" f ")
                .define('b', JDTRegistration.UPGRADE_BASE.get())
                .define('f', JDTRegistration.BlazegoldIngot.get())
                .unlockedBy("has_upgrade_base", this.has(JDTRegistration.UPGRADE_BASE.get()))
                .save(this.output);
        this.shapeless(RecipeCategory.MISC, JDTRegistration.TEMPLATE_BLAZEGOLD.get(), 2)
                .requires(JDTRegistration.TEMPLATE_BLAZEGOLD.get())
                .requires(JDTRegistration.BlazegoldIngot.get(), 2)
                .unlockedBy("has_upgrade_base", this.has(JDTRegistration.UPGRADE_BASE.get()))
                .save(this.output, idPath(JDTRegistration.TEMPLATE_BLAZEGOLD.get()) + "-duplicate");
        this.shaped(RecipeCategory.MISC, JDTRegistration.TEMPLATE_CELESTIGEM.get())
                .pattern("f f")
                .pattern("fbf")
                .pattern(" f ")
                .define('b', JDTRegistration.UPGRADE_BASE.get())
                .define('f', JDTRegistration.Celestigem.get())
                .unlockedBy("has_upgrade_base", this.has(JDTRegistration.UPGRADE_BASE.get()))
                .save(this.output);
        this.shapeless(RecipeCategory.MISC, JDTRegistration.TEMPLATE_CELESTIGEM.get(), 2)
                .requires(JDTRegistration.TEMPLATE_CELESTIGEM.get())
                .requires(JDTRegistration.Celestigem.get(), 2)
                .unlockedBy("has_upgrade_base", this.has(JDTRegistration.UPGRADE_BASE.get()))
                .save(this.output, idPath(JDTRegistration.TEMPLATE_CELESTIGEM.get()) + "-duplicate");
        this.shaped(RecipeCategory.MISC, JDTRegistration.TEMPLATE_ECLIPSEALLOY.get())
                .pattern("f f")
                .pattern("fbf")
                .pattern(" f ")
                .define('b', JDTRegistration.UPGRADE_BASE.get())
                .define('f', JDTRegistration.EclipseAlloyIngot.get())
                .unlockedBy("has_upgrade_base", this.has(JDTRegistration.UPGRADE_BASE.get()))
                .save(this.output);
        this.shapeless(RecipeCategory.MISC, JDTRegistration.TEMPLATE_ECLIPSEALLOY.get(), 2)
                .requires(JDTRegistration.TEMPLATE_ECLIPSEALLOY.get())
                .requires(JDTRegistration.EclipseAlloyIngot.get(), 2)
                .unlockedBy("has_upgrade_base", this.has(JDTRegistration.UPGRADE_BASE.get()))
                .save(this.output, idPath(JDTRegistration.TEMPLATE_ECLIPSEALLOY.get()) + "-duplicate");
        this.shapeless(RecipeCategory.MISC, JDTRegistration.UPGRADE_ELYTRA.get())
                .requires(JDTRegistration.UPGRADE_BASE.get())
                .requires(Items.ELYTRA)
                .group("justdirethings")
                .unlockedBy("has_upgrade_base", this.has(JDTRegistration.UPGRADE_BASE.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.UPGRADE_DEATHPROTECTION.get())
                .pattern("tet")
                .pattern("fbf")
                .pattern("tet")
                .define('b', JDTRegistration.UPGRADE_BASE.get())
                .define('f', Items.TOTEM_OF_UNDYING)
                .define('e', Items.EMERALD)
                .define('t', JDTRegistration.TimeCrystal.get())
                .unlockedBy("has_upgrade_base", this.has(JDTRegistration.UPGRADE_BASE.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.UPGRADE_DEBUFFREMOVER.get())
                .pattern("mem")
                .pattern("fbf")
                .pattern("mem")
                .define('b', JDTRegistration.UPGRADE_BASE.get())
                .define('f', Items.GOLDEN_APPLE)
                .define('m', Items.MILK_BUCKET)
                .define('e', Items.HONEY_BOTTLE)
                .unlockedBy("has_upgrade_base", this.has(JDTRegistration.UPGRADE_BASE.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.UPGRADE_MOBSCANNER.get())
                .pattern(" e ")
                .pattern("fbf")
                .pattern(" e ")
                .define('b', JDTRegistration.UPGRADE_BASE.get())
                .define('f', Items.AMETHYST_SHARD)
                .define('e', Items.SPIDER_EYE)
                .unlockedBy("has_upgrade_base", this.has(JDTRegistration.UPGRADE_BASE.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.UPGRADE_OREMINER.get())
                .pattern("   ")
                .pattern("fbf")
                .pattern("   ")
                .define('b', JDTRegistration.UPGRADE_BASE.get())
                .define('f', Items.IRON_PICKAXE)
                .unlockedBy("has_upgrade_base", this.has(JDTRegistration.UPGRADE_BASE.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.UPGRADE_ORESCANNER.get())
                .pattern(" e ")
                .pattern("fbf")
                .pattern(" e ")
                .define('b', JDTRegistration.UPGRADE_BASE.get())
                .define('f', Items.AMETHYST_SHARD)
                .define('e', Items.REDSTONE)
                .unlockedBy("has_upgrade_base", this.has(JDTRegistration.UPGRADE_BASE.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.UPGRADE_LAWNMOWER.get())
                .pattern("   ")
                .pattern("fbf")
                .pattern("   ")
                .define('b', JDTRegistration.UPGRADE_BASE.get())
                .define('f', Items.SHORT_GRASS)
                .unlockedBy("has_upgrade_base", this.has(JDTRegistration.UPGRADE_BASE.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.UPGRADE_SKYSWEEPER.get())
                .pattern(" e ")
                .pattern("fbf")
                .pattern(" e ")
                .define('b', JDTRegistration.UPGRADE_BASE.get())
                .define('f', Items.GRAVEL)
                .define('e', Items.SAND)
                .unlockedBy("has_upgrade_base", this.has(JDTRegistration.UPGRADE_BASE.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.UPGRADE_LEAFBREAKER.get())
                .pattern("   ")
                .pattern("fbf")
                .pattern("   ")
                .define('b', JDTRegistration.UPGRADE_BASE.get())
                .define('f', this.tag(ItemTags.LEAVES))
                .unlockedBy("has_upgrade_base", this.has(JDTRegistration.UPGRADE_BASE.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.UPGRADE_RUNSPEED.get())
                .pattern(" e ")
                .pattern("fbf")
                .pattern(" e ")
                .define('b', JDTRegistration.UPGRADE_BASE.get())
                .define('f', Items.SUGAR)
                .define('e', Items.POTATO)
                .unlockedBy("has_upgrade_base", this.has(JDTRegistration.UPGRADE_BASE.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.UPGRADE_WALKSPEED.get())
                .pattern(" e ")
                .pattern("fbf")
                .pattern(" e ")
                .define('b', JDTRegistration.UPGRADE_BASE.get())
                .define('f', Items.SUGAR)
                .define('e', Items.CARROT)
                .unlockedBy("has_upgrade_base", this.has(JDTRegistration.UPGRADE_BASE.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.UPGRADE_STEPHEIGHT.get())
                .pattern(" f ")
                .pattern("fbf")
                .pattern(" f ")
                .define('b', JDTRegistration.UPGRADE_BASE.get())
                .define('f', Items.LADDER)
                .unlockedBy("has_upgrade_base", this.has(JDTRegistration.UPGRADE_BASE.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.UPGRADE_JUMPBOOST.get())
                .pattern("   ")
                .pattern("fbf")
                .pattern("   ")
                .define('b', JDTRegistration.UPGRADE_BASE.get())
                .define('f', Items.PISTON)
                .unlockedBy("has_upgrade_base", this.has(JDTRegistration.UPGRADE_BASE.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.UPGRADE_MINDFOG.get())
                .pattern(" e ")
                .pattern("fbf")
                .pattern(" e ")
                .define('b', JDTRegistration.UPGRADE_BASE.get())
                .define('f', Items.FERMENTED_SPIDER_EYE)
                .define('e', Items.LAPIS_LAZULI)
                .unlockedBy("has_upgrade_base", this.has(JDTRegistration.UPGRADE_BASE.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.UPGRADE_INVULNERABILITY.get())
                .pattern(" e ")
                .pattern("fbf")
                .pattern(" e ")
                .define('b', JDTRegistration.UPGRADE_BASE.get())
                .define('f', Items.DIAMOND_CHESTPLATE)
                .define('e', Items.SHIELD)
                .unlockedBy("has_upgrade_base", this.has(JDTRegistration.UPGRADE_BASE.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.UPGRADE_TREEFELLER.get())
                .pattern("   ")
                .pattern("fbf")
                .pattern("   ")
                .define('b', JDTRegistration.UPGRADE_BASE.get())
                .define('f', Items.DIAMOND_AXE)
                .unlockedBy("has_upgrade_base", this.has(JDTRegistration.UPGRADE_BASE.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.UPGRADE_SMELTER.get())
                .pattern(" e ")
                .pattern("fbf")
                .pattern(" e ")
                .define('b', JDTRegistration.UPGRADE_BASE.get())
                .define('f', Items.BLAST_FURNACE)
                .define('e', Items.BLAZE_ROD)
                .unlockedBy("has_upgrade_base", this.has(JDTRegistration.UPGRADE_BASE.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.UPGRADE_SMOKER.get())
                .pattern(" e ")
                .pattern("fbf")
                .pattern(" e ")
                .define('b', JDTRegistration.UPGRADE_BASE.get())
                .define('f', Items.SMOKER)
                .define('e', Items.BLAZE_ROD)
                .unlockedBy("has_upgrade_base", this.has(JDTRegistration.UPGRADE_BASE.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.UPGRADE_HAMMER.get())
                .pattern("   ")
                .pattern("fbf")
                .pattern("   ")
                .define('b', JDTRegistration.UPGRADE_BASE.get())
                .define('f', Items.DIAMOND_PICKAXE)
                .unlockedBy("has_upgrade_base", this.has(JDTRegistration.UPGRADE_BASE.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.UPGRADE_CAUTERIZEWOUNDS.get())
                .pattern(" e ")
                .pattern("fbf")
                .pattern(" e ")
                .define('b', JDTRegistration.UPGRADE_BASE.get())
                .define('f', Items.MAGMA_CREAM)
                .define('e', Items.GOLDEN_APPLE)
                .unlockedBy("has_upgrade_base", this.has(JDTRegistration.UPGRADE_BASE.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.UPGRADE_SWIMSPEED.get())
                .pattern(" e ")
                .pattern("fbf")
                .pattern(" e ")
                .define('b', JDTRegistration.UPGRADE_BASE.get())
                .define('f', Items.SUGAR)
                .define('e', Items.KELP)
                .unlockedBy("has_upgrade_base", this.has(JDTRegistration.UPGRADE_BASE.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.UPGRADE_WATERBREATHING.get())
                .pattern("fep")
                .pattern("gbg")
                .pattern("pef")
                .define('b', JDTRegistration.UPGRADE_BASE.get())
                .define('g', JDTRegistration.BlazegoldIngot.get())
                .define('f', Items.PRISMARINE_SHARD)
                .define('p', Items.PUFFERFISH)
                .define('e', Items.KELP)
                .unlockedBy("has_upgrade_base", this.has(JDTRegistration.UPGRADE_BASE.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.UPGRADE_GROUNDSTOMP.get())
                .pattern(" e ")
                .pattern("fbf")
                .pattern(" e ")
                .define('b', JDTRegistration.UPGRADE_BASE.get())
                .define('f', Items.SLIME_BLOCK)
                .define('e', Items.PISTON)
                .unlockedBy("has_upgrade_base", this.has(JDTRegistration.UPGRADE_BASE.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.UPGRADE_EXTINGUISH.get())
                .pattern("aea")
                .pattern("fbf")
                .pattern("aea")
                .define('b', JDTRegistration.UPGRADE_BASE.get())
                .define('f', Items.WATER_BUCKET)
                .define('e', Items.BLAZE_ROD)
                .define('a', Items.MAGMA_CREAM)
                .unlockedBy("has_upgrade_base", this.has(JDTRegistration.UPGRADE_BASE.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.UPGRADE_STUPEFY.get())
                .pattern("aea")
                .pattern("fbf")
                .pattern("aea")
                .define('b', JDTRegistration.UPGRADE_BASE.get())
                .define('f', Items.FERMENTED_SPIDER_EYE)
                .define('e', Items.POISONOUS_POTATO)
                .define('a', Items.ENDER_EYE)
                .unlockedBy("has_upgrade_base", this.has(JDTRegistration.UPGRADE_BASE.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.UPGRADE_DROPTELEPORT.get())
                .pattern("aea")
                .pattern("fbf")
                .pattern("aea")
                .define('b', JDTRegistration.UPGRADE_BASE.get())
                .define('f', Items.AMETHYST_SHARD)
                .define('e', Items.ENDER_CHEST)
                .define('a', Items.ENDER_PEARL)
                .unlockedBy("has_upgrade_base", this.has(JDTRegistration.UPGRADE_BASE.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.UPGRADE_NEGATEFALLDAMAGE.get())
                .pattern(" e ")
                .pattern("fbf")
                .pattern(" e ")
                .define('b', JDTRegistration.UPGRADE_BASE.get())
                .define('f', this.tag(ItemTags.WOOL))
                .define('e', Items.LEATHER)
                .unlockedBy("has_upgrade_base", this.has(JDTRegistration.UPGRADE_BASE.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.UPGRADE_NIGHTVISION.get())
                .pattern(" e ")
                .pattern("fbf")
                .pattern(" e ")
                .define('b', JDTRegistration.UPGRADE_BASE.get())
                .define('f', Items.GOLDEN_CARROT)
                .define('e', Items.ENDER_EYE)
                .unlockedBy("has_upgrade_base", this.has(JDTRegistration.UPGRADE_BASE.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.UPGRADE_DECOY.get())
                .pattern("aea")
                .pattern("fbf")
                .pattern("aea")
                .define('b', JDTRegistration.UPGRADE_BASE.get())
                .define('f', Items.ARMOR_STAND)
                .define('e', Items.WITHER_SKELETON_SKULL)
                .define('a', Items.PHANTOM_MEMBRANE)
                .unlockedBy("has_upgrade_base", this.has(JDTRegistration.UPGRADE_BASE.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.UPGRADE_OREXRAY.get())
                .pattern("aea")
                .pattern("fbf")
                .pattern("aea")
                .define('b', JDTRegistration.UPGRADE_BASE.get())
                .define('f', Items.SCULK_SHRIEKER)
                .define('e', Items.CALIBRATED_SCULK_SENSOR)
                .define('a', Items.ENDER_EYE)
                .unlockedBy("has_upgrade_base", this.has(JDTRegistration.UPGRADE_BASE.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.UPGRADE_GLOWING.get())
                .pattern("aea")
                .pattern("fbf")
                .pattern("aea")
                .define('b', JDTRegistration.UPGRADE_BASE.get())
                .define('f', Items.SCULK_SHRIEKER)
                .define('e', Items.CALIBRATED_SCULK_SENSOR)
                .define('a', Items.END_ROD)
                .unlockedBy("has_upgrade_base", this.has(JDTRegistration.UPGRADE_BASE.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.UPGRADE_INSTABREAK.get())
                .pattern("aea")
                .pattern("fbf")
                .pattern("aea")
                .define('b', JDTRegistration.UPGRADE_BASE.get())
                .define('f', Items.NETHERITE_PICKAXE)
                .define('e', Items.NETHER_STAR)
                .define('a', JDTRegistration.TimeCrystal.get())
                .unlockedBy("has_upgrade_base", this.has(JDTRegistration.UPGRADE_BASE.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.UPGRADE_EARTHQUAKE.get())
                .pattern("aea")
                .pattern("fbf")
                .pattern("aea")
                .define('b', JDTRegistration.UPGRADE_BASE.get())
                .define('f', Items.STICKY_PISTON)
                .define('e', Items.FERMENTED_SPIDER_EYE)
                .define('a', Items.SOUL_SAND)
                .unlockedBy("has_upgrade_base", this.has(JDTRegistration.UPGRADE_BASE.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.UPGRADE_NOAI.get())
                .pattern("aea")
                .pattern("fbf")
                .pattern("aea")
                .define('b', JDTRegistration.UPGRADE_BASE.get())
                .define('f', JDTRegistration.TimeCrystal.get())
                .define('e', Items.END_ROD)
                .define('a', Items.CALIBRATED_SCULK_SENSOR)
                .unlockedBy("has_upgrade_base", this.has(JDTRegistration.UPGRADE_BASE.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.UPGRADE_FLIGHT.get())
                .pattern("aea")
                .pattern("fbf")
                .pattern("aea")
                .define('b', JDTRegistration.UPGRADE_BASE.get())
                .define('f', Items.FEATHER)
                .define('e', Items.END_CRYSTAL)
                .define('a', Items.PHANTOM_MEMBRANE)
                .unlockedBy("has_upgrade_base", this.has(JDTRegistration.UPGRADE_BASE.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.UPGRADE_LAVAIMMUNITY.get())
                .pattern("aea")
                .pattern("fbf")
                .pattern("aea")
                .define('b', JDTRegistration.UPGRADE_BASE.get())
                .define('f', Items.MAGMA_CREAM)
                .define('e', Items.LAVA_BUCKET)
                .define('a', Items.NETHERITE_SCRAP)
                .unlockedBy("has_upgrade_base", this.has(JDTRegistration.UPGRADE_BASE.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.UPGRADE_PHASE.get())
                .pattern("aea")
                .pattern("fbf")
                .pattern("aea")
                .define('b', JDTRegistration.UPGRADE_BASE.get())
                .define('f', Items.DRAGON_BREATH)
                .define('e', Items.PHANTOM_MEMBRANE)
                .define('a', Items.SHULKER_SHELL)
                .unlockedBy("has_upgrade_base", this.has(JDTRegistration.UPGRADE_BASE.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.UPGRADE_TIMEPROTECTION.get())
                .pattern(" s ")
                .pattern("fbf")
                .pattern(" s ")
                .define('b', JDTRegistration.UPGRADE_BASE.get())
                .define('f', JDTRegistration.TimeCrystal.get())
                .define('s', Items.SHIELD)
                .unlockedBy("has_upgrade_base", this.has(JDTRegistration.UPGRADE_BASE.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.UPGRADE_SPLASH.get())
                .pattern("p p")
                .pattern("gbg")
                .pattern("p p")
                .define('b', JDTRegistration.UPGRADE_BASE.get())
                .define('g', Items.GLASS_BOTTLE)
                .define('p', Items.GUNPOWDER)
                .unlockedBy("has_upgrade_base", this.has(JDTRegistration.UPGRADE_BASE.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.UPGRADE_LINGERING.get())
                .pattern("p p")
                .pattern("gbg")
                .pattern("p p")
                .define('b', JDTRegistration.UPGRADE_BASE.get())
                .define('g', Items.GLASS_BOTTLE)
                .define('p', Items.DRAGON_BREATH)
                .unlockedBy("has_upgrade_base", this.has(JDTRegistration.UPGRADE_BASE.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.UPGRADE_POTIONARROW.get())
                .pattern("p p")
                .pattern("gbg")
                .pattern("p p")
                .define('b', JDTRegistration.UPGRADE_BASE.get())
                .define('g', Items.GLASS_BOTTLE)
                .define('p', Items.NETHER_WART)
                .unlockedBy("has_upgrade_base", this.has(JDTRegistration.UPGRADE_BASE.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.UPGRADE_HOMING.get())
                .pattern("p p")
                .pattern("gbg")
                .pattern("p p")
                .define('b', JDTRegistration.UPGRADE_BASE.get())
                .define('g', Items.TARGET)
                .define('p', Items.ENDER_EYE)
                .unlockedBy("has_upgrade_base", this.has(JDTRegistration.UPGRADE_BASE.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.UPGRADE_EPICARROW.get())
                .pattern("psp")
                .pattern("gbg")
                .pattern("psp")
                .define('b', JDTRegistration.UPGRADE_BASE.get())
                .define('g', Items.PHANTOM_MEMBRANE)
                .define('p', Items.ENDER_EYE)
                .define('s', Items.NETHER_STAR)
                .unlockedBy("has_upgrade_base", this.has(JDTRegistration.UPGRADE_BASE.get()))
                .save(this.output);

        //GooSpread Recipes
        GooSpreadRecipeBuilder.shapeless(Identifier.fromNamespaceAndPath(JustDireThings.MODID, "dire_iron_block"), Blocks.IRON_BLOCK.defaultBlockState(), JDTRegistration.RawFerricoreOre.get().defaultBlockState(), 1, 2400)
                .group("justdirethings")
                .unlockedBy("has_goo_block_t1", this.has(JDTRegistration.GooBlock_Tier1_ITEM.get()))
                .save(this.output);
        GooSpreadRecipeBuilder.shapeless(Identifier.fromNamespaceAndPath(JustDireThings.MODID, "dire_gold_block"), Blocks.GOLD_BLOCK.defaultBlockState(), JDTRegistration.RawBlazegoldOre.get().defaultBlockState(), 2, 2400)
                .group("justdirethings")
                .unlockedBy("has_goo_block_t2", this.has(JDTRegistration.GooBlock_Tier2_ITEM.get()))
                .save(this.output);
        GooSpreadRecipeBuilder.shapeless(Identifier.fromNamespaceAndPath(JustDireThings.MODID, "dire_diamond_block"), Blocks.DIAMOND_BLOCK.defaultBlockState(), JDTRegistration.RawCelestigemOre.get().defaultBlockState(), 3, 4800)
                .group("justdirethings")
                .unlockedBy("has_goo_block_t3", this.has(JDTRegistration.GooBlock_Tier3_ITEM.get()))
                .save(this.output);
        GooSpreadRecipeBuilder.shapeless(Identifier.fromNamespaceAndPath(JustDireThings.MODID, "dire_netherite_block"), Blocks.NETHERITE_BLOCK.defaultBlockState(), JDTRegistration.RawEclipseAlloyOre.get().defaultBlockState(), 4, 4800)
                .group("justdirethings")
                .unlockedBy("has_goo_block_t4", this.has(JDTRegistration.GooBlock_Tier4_ITEM.get()))
                .save(this.output);

        //GooSpread Coal
        GooSpreadRecipeBuilder.shapeless(Identifier.fromNamespaceAndPath(JustDireThings.MODID, "coal_block_t1"), Blocks.COAL_BLOCK.defaultBlockState(), JDTRegistration.RawCoal_T1.get().defaultBlockState(), 1, 2400)
                .group("justdirethings")
                .unlockedBy("has_goo_block_t1", this.has(JDTRegistration.GooBlock_Tier1_ITEM.get()))
                .save(this.output);
        GooSpreadRecipeTagBuilder.shapeless(Identifier.fromNamespaceAndPath(JustDireThings.MODID, "coal_block_t1"), new BlockTagIngredient(ModTags.Blocks.CHARCOAL), JDTRegistration.RawCoal_T1.get().defaultBlockState(), 1, 2400)
                .group("justdirethings")
                .unlockedBy("has_goo_block_t1", this.has(JDTRegistration.GooBlock_Tier1_ITEM.get()))
                .save(this.output);

        GooSpreadRecipeBuilder.shapeless(Identifier.fromNamespaceAndPath(JustDireThings.MODID, "coal_block_t2"), JDTRegistration.CoalBlock_T1.get().defaultBlockState(), JDTRegistration.RawCoal_T2.get().defaultBlockState(), 2, 2400)
                .group("justdirethings")
                .unlockedBy("has_goo_block_t2", this.has(JDTRegistration.GooBlock_Tier2_ITEM.get()))
                .save(this.output);
        GooSpreadRecipeBuilder.shapeless(Identifier.fromNamespaceAndPath(JustDireThings.MODID, "coal_block_t3"), JDTRegistration.CoalBlock_T2.get().defaultBlockState(), JDTRegistration.RawCoal_T3.get().defaultBlockState(), 3, 4800)
                .group("justdirethings")
                .unlockedBy("has_goo_block_t3", this.has(JDTRegistration.GooBlock_Tier3_ITEM.get()))
                .save(this.output);
        GooSpreadRecipeBuilder.shapeless(Identifier.fromNamespaceAndPath(JustDireThings.MODID, "coal_block_t4"), JDTRegistration.CoalBlock_T3.get().defaultBlockState(), JDTRegistration.RawCoal_T4.get().defaultBlockState(), 4, 4800)
                .group("justdirethings")
                .unlockedBy("has_goo_block_t4", this.has(JDTRegistration.GooBlock_Tier4_ITEM.get()))
                .save(this.output);

        //GooSpread Misc
        GooSpreadRecipeBuilder.shapeless(Identifier.fromNamespaceAndPath(JustDireThings.MODID, "portal_fluid"), JDTRegistration.UNSTABLE_PORTAL_FLUID_BLOCK.get().defaultBlockState(), JDTRegistration.PORTAL_FLUID_BLOCK.get().defaultBlockState(), 3, 2400)
                .group("justdirethings")
                .unlockedBy("has_goo_block_t3", this.has(JDTRegistration.GooBlock_Tier3_ITEM.get()))
                .save(this.output);
        GooSpreadRecipeBuilder.shapeless(Identifier.fromNamespaceAndPath(JustDireThings.MODID, "refined_t2_fluid"), JDTRegistration.UNREFINED_T2_FLUID_BLOCK.get().defaultBlockState(), JDTRegistration.REFINED_T2_FLUID_BLOCK.get().defaultBlockState(), 2, 2400)
                .group("justdirethings")
                .unlockedBy("has_goo_block_t2", this.has(JDTRegistration.GooBlock_Tier2_ITEM.get()))
                .save(this.output);
        GooSpreadRecipeBuilder.shapeless(Identifier.fromNamespaceAndPath(JustDireThings.MODID, "refined_t3_fluid"), JDTRegistration.UNREFINED_T3_FLUID_BLOCK.get().defaultBlockState(), JDTRegistration.REFINED_T3_FLUID_BLOCK.get().defaultBlockState(), 3, 2400)
                .group("justdirethings")
                .unlockedBy("has_goo_block_t3", this.has(JDTRegistration.GooBlock_Tier3_ITEM.get()))
                .save(this.output);
        GooSpreadRecipeBuilder.shapeless(Identifier.fromNamespaceAndPath(JustDireThings.MODID, "refined_t4_fluid"), JDTRegistration.UNREFINED_T4_FLUID_BLOCK.get().defaultBlockState(), JDTRegistration.REFINED_T4_FLUID_BLOCK.get().defaultBlockState(), 4, 2400)
                .group("justdirethings")
                .unlockedBy("has_goo_block_t4", this.has(JDTRegistration.GooBlock_Tier4_ITEM.get()))
                .save(this.output);
        GooSpreadRecipeBuilder.shapeless(Identifier.fromNamespaceAndPath(JustDireThings.MODID, "budding_time_amethyst"), Blocks.BUDDING_AMETHYST.defaultBlockState(), JDTRegistration.TimeCrystalBuddingBlock.get().defaultBlockState(), 4, 4800)
                .group("justdirethings")
                .unlockedBy("has_goo_block_t4", this.has(JDTRegistration.GooBlock_Tier4_ITEM.get()))
                .save(this.output, "budding_time_amethyst");
        GooSpreadRecipeBuilder.shapeless(Identifier.fromNamespaceAndPath(JustDireThings.MODID, "budding_time_timeblock"), JDTRegistration.TimeCrystalBlock.get().defaultBlockState(), JDTRegistration.TimeCrystalBuddingBlock.get().defaultBlockState(), 4, 4800)
                .group("justdirethings")
                .unlockedBy("has_goo_block_t4", this.has(JDTRegistration.GooBlock_Tier4_ITEM.get()))
                .save(this.output, "budding_time_timeblock");

        //FluidDrop Recipes
        FluidDropRecipeBuilder.shapeless(Identifier.fromNamespaceAndPath(JustDireThings.MODID, "polymorphic_fluid"), Blocks.WATER.defaultBlockState(), JDTRegistration.POLYMORPHIC_FLUID_BLOCK.get().defaultBlockState(), JDTRegistration.PolymorphicCatalyst.get())
                .group("justdirethings")
                .unlockedBy("has_polymorphic_catalyst", this.has(JDTRegistration.PolymorphicCatalyst.get()))
                .save(this.output);
        FluidDropRecipeBuilder.shapeless(Identifier.fromNamespaceAndPath(JustDireThings.MODID, "unstable_portal_fluid"), JDTRegistration.POLYMORPHIC_FLUID_BLOCK.get().defaultBlockState(), JDTRegistration.UNSTABLE_PORTAL_FLUID_BLOCK.get().defaultBlockState(), JDTRegistration.PortalFluidCatalyst.get())
                .group("justdirethings")
                .unlockedBy("has_portal_catalyst", this.has(JDTRegistration.PortalFluidCatalyst.get()))
                .save(this.output);
        FluidDropRecipeBuilder.shapeless(Identifier.fromNamespaceAndPath(JustDireThings.MODID, "unrefined_t2_fluid"), JDTRegistration.POLYMORPHIC_FLUID_BLOCK.get().defaultBlockState(), JDTRegistration.UNREFINED_T2_FLUID_BLOCK.get().defaultBlockState(), JDTRegistration.Coal_T2.get())
                .group("justdirethings")
                .unlockedBy("has_coal_t2", this.has(JDTRegistration.Coal_T2.get()))
                .save(this.output);
        FluidDropRecipeBuilder.shapeless(Identifier.fromNamespaceAndPath(JustDireThings.MODID, "unrefined_t3_fluid"), JDTRegistration.REFINED_T2_FLUID_BLOCK.get().defaultBlockState(), JDTRegistration.UNREFINED_T3_FLUID_BLOCK.get().defaultBlockState(), JDTRegistration.Coal_T3.get())
                .group("justdirethings")
                .unlockedBy("has_coal_t3", this.has(JDTRegistration.Coal_T3.get()))
                .save(this.output);
        FluidDropRecipeBuilder.shapeless(Identifier.fromNamespaceAndPath(JustDireThings.MODID, "unrefined_t4_fluid"), JDTRegistration.REFINED_T3_FLUID_BLOCK.get().defaultBlockState(), JDTRegistration.UNREFINED_T4_FLUID_BLOCK.get().defaultBlockState(), JDTRegistration.Coal_T4.get())
                .group("justdirethings")
                .unlockedBy("has_coal_t4", this.has(JDTRegistration.Coal_T4.get()))
                .save(this.output);
        FluidDropRecipeBuilder.shapeless(Identifier.fromNamespaceAndPath(JustDireThings.MODID, "time_fluid"), JDTRegistration.POLYMORPHIC_FLUID_BLOCK.get().defaultBlockState(), JDTRegistration.TIME_FLUID_BLOCK.get().defaultBlockState(), JDTRegistration.TimeCrystal.get())
                .group("justdirethings")
                .unlockedBy("has_time_crystal", this.has(JDTRegistration.TimeCrystal.get()))
                .save(this.output);

        //Smelting
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(JDTRegistration.RawFerricore.get()), RecipeCategory.MISC, CookingBookCategory.MISC,
                        JDTRegistration.FerricoreIngot.get(), 1.0f, 200)
                .unlockedBy("has_ferricore_raw", this.has(JDTRegistration.RawFerricore.get()))
                .save(this.output, "ferricore_ingot_smelted");
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(JDTRegistration.RawBlazegold.get()), RecipeCategory.MISC, CookingBookCategory.MISC,
                        JDTRegistration.BlazegoldIngot.get(), 1.0f, 200)
                .unlockedBy("has_blazegold_raw", this.has(JDTRegistration.RawBlazegold.get()))
                .save(this.output, "blazegold_ingot_smelted");
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(JDTRegistration.RawEclipseAlloy.get()), RecipeCategory.MISC, CookingBookCategory.MISC,
                        JDTRegistration.EclipseAlloyIngot.get(), 1.0f, 200)
                .unlockedBy("has_eclipsealloy_raw", this.has(JDTRegistration.RawEclipseAlloy.get()))
                .save(this.output, "eclipsealloy_ingot_smelted");
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(JDTRegistration.RawFerricore.get()), RecipeCategory.MISC, CookingBookCategory.MISC,
                        JDTRegistration.FerricoreIngot.get(), 1.0f, 100)
                .unlockedBy("has_ferricore_raw", this.has(JDTRegistration.RawFerricore.get()))
                .save(this.output, "ferricore_ingot_blasted");
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(JDTRegistration.RawBlazegold.get()), RecipeCategory.MISC, CookingBookCategory.MISC,
                        JDTRegistration.BlazegoldIngot.get(), 1.0f, 100)
                .unlockedBy("has_blazegold_raw", this.has(JDTRegistration.RawBlazegold.get()))
                .save(this.output, "blazegold_ingot_blasted");
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(JDTRegistration.RawEclipseAlloy.get()), RecipeCategory.MISC, CookingBookCategory.MISC,
                        JDTRegistration.EclipseAlloyIngot.get(), 1.0f, 100)
                .unlockedBy("has_eclipsealloy_raw", this.has(JDTRegistration.RawEclipseAlloy.get()))
                .save(this.output, "eclipsealloy_ingot_blasted");

        //Tools
        this.shaped(RecipeCategory.MISC, JDTRegistration.FerricoreSword.get())
                .pattern(" f ")
                .pattern(" f ")
                .pattern(" s ")
                .define('f', JDTRegistration.FerricoreIngot.get())
                .define('s', Items.STICK)
                .group("justdirethings")
                .unlockedBy("has_ferricore_ingot", this.has(JDTRegistration.FerricoreIngot.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.FerricorePickaxe.get())
                .pattern("fff")
                .pattern(" s ")
                .pattern(" s ")
                .define('f', JDTRegistration.FerricoreIngot.get())
                .define('s', Items.STICK)
                .group("justdirethings")
                .unlockedBy("has_ferricore_ingot", this.has(JDTRegistration.FerricoreIngot.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.FerricoreShovel.get())
                .pattern(" f ")
                .pattern(" s ")
                .pattern(" s ")
                .define('f', JDTRegistration.FerricoreIngot.get())
                .define('s', Items.STICK)
                .group("justdirethings")
                .unlockedBy("has_ferricore_ingot", this.has(JDTRegistration.FerricoreIngot.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.FerricoreAxe.get())
                .pattern("ff ")
                .pattern("fs ")
                .pattern(" s ")
                .define('f', JDTRegistration.FerricoreIngot.get())
                .define('s', Items.STICK)
                .group("justdirethings")
                .unlockedBy("has_ferricore_ingot", this.has(JDTRegistration.FerricoreIngot.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.FerricoreHoe.get())
                .pattern("ff ")
                .pattern(" s ")
                .pattern(" s ")
                .define('f', JDTRegistration.FerricoreIngot.get())
                .define('s', Items.STICK)
                .group("justdirethings")
                .unlockedBy("has_ferricore_ingot", this.has(JDTRegistration.FerricoreIngot.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.FerricoreBow.get())
                .pattern("sf ")
                .pattern("s f")
                .pattern("sf ")
                .define('f', JDTRegistration.FerricoreIngot.get())
                .define('s', Items.STRING)
                .group("justdirethings")
                .unlockedBy("has_ferricore_ingot", this.has(JDTRegistration.FerricoreIngot.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.BlazegoldBow.get())
                .pattern("sf ")
                .pattern("s f")
                .pattern("sf ")
                .define('f', JDTRegistration.BlazegoldIngot.get())
                .define('s', Items.STRING)
                .group("justdirethings")
                .unlockedBy("has_blazegold_ingot", this.has(JDTRegistration.BlazegoldIngot.get()))
                .save(this.output);
        SmithingTransformRecipeBuilder.smithing(Ingredient.of(JDTRegistration.TEMPLATE_BLAZEGOLD.get()), Ingredient.of(JDTRegistration.FerricoreBow.get()),
                        Ingredient.of(JDTRegistration.BlazegoldIngot.get()), RecipeCategory.MISC, JDTRegistration.BlazegoldBow.get())
                .unlocks("has_template_blazegold", this.has(JDTRegistration.TEMPLATE_BLAZEGOLD.get()))
                .save(this.output, Identifier.fromNamespaceAndPath(JustDireThings.MODID, idPath(JDTRegistration.BlazegoldBow.get().asItem()) + "-templateupgrade").toString());
        this.shaped(RecipeCategory.MISC, JDTRegistration.CelestigemBow.get())
                .pattern("sf ")
                .pattern("s f")
                .pattern("sf ")
                .define('f', JDTRegistration.Celestigem.get())
                .define('s', Items.STRING)
                .group("justdirethings")
                .unlockedBy("has_celestigem", this.has(JDTRegistration.Celestigem.get()))
                .save(this.output);
        SmithingTransformRecipeBuilder.smithing(Ingredient.of(JDTRegistration.TEMPLATE_CELESTIGEM.get()), Ingredient.of(JDTRegistration.BlazegoldBow.get()),
                        Ingredient.of(JDTRegistration.Celestigem.get()), RecipeCategory.MISC, JDTRegistration.CelestigemBow.get())
                .unlocks("has_template_celestigem", this.has(JDTRegistration.TEMPLATE_CELESTIGEM.get()))
                .save(this.output, Identifier.fromNamespaceAndPath(JustDireThings.MODID, idPath(JDTRegistration.CelestigemBow.get().asItem()) + "-templateupgrade").toString());
        this.shaped(RecipeCategory.MISC, JDTRegistration.EclipseAlloyBow.get())
                .pattern("sf ")
                .pattern("s f")
                .pattern("sf ")
                .define('f', JDTRegistration.EclipseAlloyIngot.get())
                .define('s', Items.STRING)
                .group("justdirethings")
                .unlockedBy("has_eclipsealloy", this.has(JDTRegistration.EclipseAlloyIngot.get()))
                .save(this.output);
        SmithingTransformRecipeBuilder.smithing(Ingredient.of(JDTRegistration.TEMPLATE_ECLIPSEALLOY.get()), Ingredient.of(JDTRegistration.CelestigemBow.get()),
                        Ingredient.of(JDTRegistration.EclipseAlloyIngot.get()), RecipeCategory.MISC, JDTRegistration.EclipseAlloyBow.get())
                .unlocks("has_template_eclipsealloy", this.has(JDTRegistration.TEMPLATE_ECLIPSEALLOY.get()))
                .save(this.output, Identifier.fromNamespaceAndPath(JustDireThings.MODID, idPath(JDTRegistration.EclipseAlloyBow.get().asItem()) + "-templateupgrade").toString());

        this.shaped(RecipeCategory.MISC, JDTRegistration.BlazegoldSword.get())
                .pattern(" f ")
                .pattern(" f ")
                .pattern(" s ")
                .define('f', JDTRegistration.BlazegoldIngot.get())
                .define('s', Items.STICK)
                .group("justdirethings")
                .unlockedBy("has_blazegold_ingot", this.has(JDTRegistration.BlazegoldIngot.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.BlazegoldPickaxe.get())
                .pattern("fff")
                .pattern(" s ")
                .pattern(" s ")
                .define('f', JDTRegistration.BlazegoldIngot.get())
                .define('s', Items.STICK)
                .group("justdirethings")
                .unlockedBy("has_blazegold_ingot", this.has(JDTRegistration.BlazegoldIngot.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.BlazegoldShovel.get())
                .pattern(" f ")
                .pattern(" s ")
                .pattern(" s ")
                .define('f', JDTRegistration.BlazegoldIngot.get())
                .define('s', Items.STICK)
                .group("justdirethings")
                .unlockedBy("has_blazegold_ingot", this.has(JDTRegistration.BlazegoldIngot.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.BlazegoldAxe.get())
                .pattern("ff ")
                .pattern("fs ")
                .pattern(" s ")
                .define('f', JDTRegistration.BlazegoldIngot.get())
                .define('s', Items.STICK)
                .group("justdirethings")
                .unlockedBy("has_blazegold_ingot", this.has(JDTRegistration.BlazegoldIngot.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.BlazegoldHoe.get())
                .pattern("ff ")
                .pattern(" s ")
                .pattern(" s ")
                .define('f', JDTRegistration.BlazegoldIngot.get())
                .define('s', Items.STICK)
                .group("justdirethings")
                .unlockedBy("has_blazegold_ingot", this.has(JDTRegistration.BlazegoldIngot.get()))
                .save(this.output);
        SmithingTransformRecipeBuilder.smithing(Ingredient.of(JDTRegistration.TEMPLATE_BLAZEGOLD.get()), Ingredient.of(JDTRegistration.FerricoreSword.get()),
                        Ingredient.of(JDTRegistration.BlazegoldIngot.get()), RecipeCategory.MISC, JDTRegistration.BlazegoldSword.get())
                .unlocks("has_template_blazegold", this.has(JDTRegistration.TEMPLATE_BLAZEGOLD.get()))
                .save(this.output, Identifier.fromNamespaceAndPath(JustDireThings.MODID, idPath(JDTRegistration.BlazegoldSword.get().asItem()) + "-templateupgrade").toString());
        SmithingTransformRecipeBuilder.smithing(Ingredient.of(JDTRegistration.TEMPLATE_BLAZEGOLD.get()), Ingredient.of(JDTRegistration.FerricorePickaxe.get()),
                        Ingredient.of(JDTRegistration.BlazegoldIngot.get()), RecipeCategory.MISC, JDTRegistration.BlazegoldPickaxe.get())
                .unlocks("has_template_blazegold", this.has(JDTRegistration.TEMPLATE_BLAZEGOLD.get()))
                .save(this.output, Identifier.fromNamespaceAndPath(JustDireThings.MODID, idPath(JDTRegistration.BlazegoldPickaxe.get().asItem()) + "-templateupgrade").toString());
        SmithingTransformRecipeBuilder.smithing(Ingredient.of(JDTRegistration.TEMPLATE_BLAZEGOLD.get()), Ingredient.of(JDTRegistration.FerricoreShovel.get()),
                        Ingredient.of(JDTRegistration.BlazegoldIngot.get()), RecipeCategory.MISC, JDTRegistration.BlazegoldShovel.get())
                .unlocks("has_template_blazegold", this.has(JDTRegistration.TEMPLATE_BLAZEGOLD.get()))
                .save(this.output, Identifier.fromNamespaceAndPath(JustDireThings.MODID, idPath(JDTRegistration.BlazegoldShovel.get().asItem()) + "-templateupgrade").toString());
        SmithingTransformRecipeBuilder.smithing(Ingredient.of(JDTRegistration.TEMPLATE_BLAZEGOLD.get()), Ingredient.of(JDTRegistration.FerricoreAxe.get()),
                        Ingredient.of(JDTRegistration.BlazegoldIngot.get()), RecipeCategory.MISC, JDTRegistration.BlazegoldAxe.get())
                .unlocks("has_template_blazegold", this.has(JDTRegistration.TEMPLATE_BLAZEGOLD.get()))
                .save(this.output, Identifier.fromNamespaceAndPath(JustDireThings.MODID, idPath(JDTRegistration.BlazegoldAxe.get().asItem()) + "-templateupgrade").toString());
        SmithingTransformRecipeBuilder.smithing(Ingredient.of(JDTRegistration.TEMPLATE_BLAZEGOLD.get()), Ingredient.of(JDTRegistration.FerricoreHoe.get()),
                        Ingredient.of(JDTRegistration.BlazegoldIngot.get()), RecipeCategory.MISC, JDTRegistration.BlazegoldHoe.get())
                .unlocks("has_template_blazegold", this.has(JDTRegistration.TEMPLATE_BLAZEGOLD.get()))
                .save(this.output, Identifier.fromNamespaceAndPath(JustDireThings.MODID, idPath(JDTRegistration.BlazegoldHoe.get().asItem()) + "-templateupgrade").toString());

        this.shaped(RecipeCategory.MISC, JDTRegistration.CelestigemSword.get())
                .pattern(" f ")
                .pattern(" f ")
                .pattern(" s ")
                .define('f', JDTRegistration.Celestigem.get())
                .define('s', Items.STICK)
                .group("justdirethings")
                .unlockedBy("has_celestigem", this.has(JDTRegistration.Celestigem.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.CelestigemPickaxe.get())
                .pattern("fff")
                .pattern(" s ")
                .pattern(" s ")
                .define('f', JDTRegistration.Celestigem.get())
                .define('s', Items.STICK)
                .group("justdirethings")
                .unlockedBy("has_celestigem", this.has(JDTRegistration.Celestigem.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.CelestigemShovel.get())
                .pattern(" f ")
                .pattern(" s ")
                .pattern(" s ")
                .define('f', JDTRegistration.Celestigem.get())
                .define('s', Items.STICK)
                .group("justdirethings")
                .unlockedBy("has_celestigem", this.has(JDTRegistration.Celestigem.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.CelestigemAxe.get())
                .pattern("ff ")
                .pattern("fs ")
                .pattern(" s ")
                .define('f', JDTRegistration.Celestigem.get())
                .define('s', Items.STICK)
                .group("justdirethings")
                .unlockedBy("has_celestigem", this.has(JDTRegistration.Celestigem.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.CelestigemHoe.get())
                .pattern("ff ")
                .pattern(" s ")
                .pattern(" s ")
                .define('f', JDTRegistration.Celestigem.get())
                .define('s', Items.STICK)
                .group("justdirethings")
                .unlockedBy("has_celestigem", this.has(JDTRegistration.Celestigem.get()))
                .save(this.output);
        PaxelRecipeBuilder.shapeless(Ingredient.of(JDTRegistration.CelestigemPickaxe.get()), Ingredient.of(JDTRegistration.CelestigemAxe.get()),
                        Ingredient.of(JDTRegistration.CelestigemShovel.get()), JDTRegistration.CelestigemPaxel.get())
                .unlockedBy("has_celestigem", this.has(JDTRegistration.Celestigem.get()))
                .save(this.output, Identifier.fromNamespaceAndPath(JustDireThings.MODID, idPath(JDTRegistration.CelestigemPaxel.get().asItem())).toString());

        SmithingTransformRecipeBuilder.smithing(Ingredient.of(JDTRegistration.TEMPLATE_CELESTIGEM.get()), Ingredient.of(JDTRegistration.BlazegoldSword.get()),
                        Ingredient.of(JDTRegistration.Celestigem.get()), RecipeCategory.MISC, JDTRegistration.CelestigemSword.get())
                .unlocks("has_template_celestigem", this.has(JDTRegistration.TEMPLATE_CELESTIGEM.get()))
                .save(this.output, Identifier.fromNamespaceAndPath(JustDireThings.MODID, idPath(JDTRegistration.CelestigemSword.get().asItem()) + "-templateupgrade").toString());
        SmithingTransformRecipeBuilder.smithing(Ingredient.of(JDTRegistration.TEMPLATE_CELESTIGEM.get()), Ingredient.of(JDTRegistration.BlazegoldPickaxe.get()),
                        Ingredient.of(JDTRegistration.Celestigem.get()), RecipeCategory.MISC, JDTRegistration.CelestigemPickaxe.get())
                .unlocks("has_template_celestigem", this.has(JDTRegistration.TEMPLATE_CELESTIGEM.get()))
                .save(this.output, Identifier.fromNamespaceAndPath(JustDireThings.MODID, idPath(JDTRegistration.CelestigemPickaxe.get().asItem()) + "-templateupgrade").toString());
        SmithingTransformRecipeBuilder.smithing(Ingredient.of(JDTRegistration.TEMPLATE_CELESTIGEM.get()), Ingredient.of(JDTRegistration.BlazegoldShovel.get()),
                        Ingredient.of(JDTRegistration.Celestigem.get()), RecipeCategory.MISC, JDTRegistration.CelestigemShovel.get())
                .unlocks("has_template_celestigem", this.has(JDTRegistration.TEMPLATE_CELESTIGEM.get()))
                .save(this.output, Identifier.fromNamespaceAndPath(JustDireThings.MODID, idPath(JDTRegistration.CelestigemShovel.get().asItem()) + "-templateupgrade").toString());
        SmithingTransformRecipeBuilder.smithing(Ingredient.of(JDTRegistration.TEMPLATE_CELESTIGEM.get()), Ingredient.of(JDTRegistration.BlazegoldAxe.get()),
                        Ingredient.of(JDTRegistration.Celestigem.get()), RecipeCategory.MISC, JDTRegistration.CelestigemAxe.get())
                .unlocks("has_template_celestigem", this.has(JDTRegistration.TEMPLATE_CELESTIGEM.get()))
                .save(this.output, Identifier.fromNamespaceAndPath(JustDireThings.MODID, idPath(JDTRegistration.CelestigemAxe.get().asItem()) + "-templateupgrade").toString());
        SmithingTransformRecipeBuilder.smithing(Ingredient.of(JDTRegistration.TEMPLATE_CELESTIGEM.get()), Ingredient.of(JDTRegistration.BlazegoldHoe.get()),
                        Ingredient.of(JDTRegistration.Celestigem.get()), RecipeCategory.MISC, JDTRegistration.CelestigemHoe.get())
                .unlocks("has_template_celestigem", this.has(JDTRegistration.TEMPLATE_CELESTIGEM.get()))
                .save(this.output, Identifier.fromNamespaceAndPath(JustDireThings.MODID, idPath(JDTRegistration.CelestigemHoe.get().asItem()) + "-templateupgrade").toString());

        this.shaped(RecipeCategory.MISC, JDTRegistration.EclipseAlloySword.get())
                .pattern(" f ")
                .pattern(" f ")
                .pattern(" s ")
                .define('f', JDTRegistration.EclipseAlloyIngot.get())
                .define('s', Items.STICK)
                .group("justdirethings")
                .unlockedBy("has_eclipsealloy_ingot", this.has(JDTRegistration.EclipseAlloyIngot.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.EclipseAlloyPickaxe.get())
                .pattern("fff")
                .pattern(" s ")
                .pattern(" s ")
                .define('f', JDTRegistration.EclipseAlloyIngot.get())
                .define('s', Items.STICK)
                .group("justdirethings")
                .unlockedBy("has_eclipsealloy_ingot", this.has(JDTRegistration.EclipseAlloyIngot.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.EclipseAlloyShovel.get())
                .pattern(" f ")
                .pattern(" s ")
                .pattern(" s ")
                .define('f', JDTRegistration.EclipseAlloyIngot.get())
                .define('s', Items.STICK)
                .group("justdirethings")
                .unlockedBy("has_eclipsealloy_ingot", this.has(JDTRegistration.EclipseAlloyIngot.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.EclipseAlloyAxe.get())
                .pattern("ff ")
                .pattern("fs ")
                .pattern(" s ")
                .define('f', JDTRegistration.EclipseAlloyIngot.get())
                .define('s', Items.STICK)
                .group("justdirethings")
                .unlockedBy("has_eclipsealloy_ingot", this.has(JDTRegistration.EclipseAlloyIngot.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.EclipseAlloyHoe.get())
                .pattern("ff ")
                .pattern(" s ")
                .pattern(" s ")
                .define('f', JDTRegistration.EclipseAlloyIngot.get())
                .define('s', Items.STICK)
                .group("justdirethings")
                .unlockedBy("has_eclipsealloy_ingot", this.has(JDTRegistration.EclipseAlloyIngot.get()))
                .save(this.output);
        PaxelRecipeBuilder.shapeless(Ingredient.of(JDTRegistration.EclipseAlloyPickaxe.get()), Ingredient.of(JDTRegistration.EclipseAlloyAxe.get()),
                        Ingredient.of(JDTRegistration.EclipseAlloyShovel.get()), JDTRegistration.EclipseAlloyPaxel.get())
                .unlockedBy("has_eclipsealloy_ingot", this.has(JDTRegistration.EclipseAlloyIngot.get()))
                .save(this.output, Identifier.fromNamespaceAndPath(JustDireThings.MODID, idPath(JDTRegistration.EclipseAlloyPaxel.get().asItem())).toString());
        SmithingTransformRecipeBuilder.smithing(Ingredient.of(JDTRegistration.TEMPLATE_ECLIPSEALLOY.get()), Ingredient.of(JDTRegistration.CelestigemSword.get()),
                        Ingredient.of(JDTRegistration.EclipseAlloyIngot.get()), RecipeCategory.MISC, JDTRegistration.EclipseAlloySword.get())
                .unlocks("has_template_eclipsealloy", this.has(JDTRegistration.TEMPLATE_ECLIPSEALLOY.get()))
                .save(this.output, Identifier.fromNamespaceAndPath(JustDireThings.MODID, idPath(JDTRegistration.EclipseAlloySword.get().asItem()) + "-templateupgrade").toString());
        SmithingTransformRecipeBuilder.smithing(Ingredient.of(JDTRegistration.TEMPLATE_ECLIPSEALLOY.get()), Ingredient.of(JDTRegistration.CelestigemPickaxe.get()),
                        Ingredient.of(JDTRegistration.EclipseAlloyIngot.get()), RecipeCategory.MISC, JDTRegistration.EclipseAlloyPickaxe.get())
                .unlocks("has_template_eclipsealloy", this.has(JDTRegistration.TEMPLATE_ECLIPSEALLOY.get()))
                .save(this.output, Identifier.fromNamespaceAndPath(JustDireThings.MODID, idPath(JDTRegistration.EclipseAlloyPickaxe.get().asItem()) + "-templateupgrade").toString());
        SmithingTransformRecipeBuilder.smithing(Ingredient.of(JDTRegistration.TEMPLATE_ECLIPSEALLOY.get()), Ingredient.of(JDTRegistration.CelestigemShovel.get()),
                        Ingredient.of(JDTRegistration.EclipseAlloyIngot.get()), RecipeCategory.MISC, JDTRegistration.EclipseAlloyShovel.get())
                .unlocks("has_template_eclipsealloy", this.has(JDTRegistration.TEMPLATE_ECLIPSEALLOY.get()))
                .save(this.output, Identifier.fromNamespaceAndPath(JustDireThings.MODID, idPath(JDTRegistration.EclipseAlloyShovel.get().asItem()) + "-templateupgrade").toString());
        SmithingTransformRecipeBuilder.smithing(Ingredient.of(JDTRegistration.TEMPLATE_ECLIPSEALLOY.get()), Ingredient.of(JDTRegistration.CelestigemAxe.get()),
                        Ingredient.of(JDTRegistration.EclipseAlloyIngot.get()), RecipeCategory.MISC, JDTRegistration.EclipseAlloyAxe.get())
                .unlocks("has_template_eclipsealloy", this.has(JDTRegistration.TEMPLATE_ECLIPSEALLOY.get()))
                .save(this.output, Identifier.fromNamespaceAndPath(JustDireThings.MODID, idPath(JDTRegistration.EclipseAlloyAxe.get().asItem()) + "-templateupgrade").toString());
        SmithingTransformRecipeBuilder.smithing(Ingredient.of(JDTRegistration.TEMPLATE_ECLIPSEALLOY.get()), Ingredient.of(JDTRegistration.CelestigemHoe.get()),
                        Ingredient.of(JDTRegistration.EclipseAlloyIngot.get()), RecipeCategory.MISC, JDTRegistration.EclipseAlloyHoe.get())
                .unlocks("has_template_eclipsealloy", this.has(JDTRegistration.TEMPLATE_ECLIPSEALLOY.get()))
                .save(this.output, Identifier.fromNamespaceAndPath(JustDireThings.MODID, idPath(JDTRegistration.EclipseAlloyHoe.get().asItem()) + "-templateupgrade").toString());
        SmithingTransformRecipeBuilder.smithing(Ingredient.of(JDTRegistration.TEMPLATE_ECLIPSEALLOY.get()), Ingredient.of(JDTRegistration.CelestigemPaxel.get()),
                        Ingredient.of(JDTRegistration.EclipseAlloyIngot.get()), RecipeCategory.MISC, JDTRegistration.EclipseAlloyPaxel.get())
                .unlocks("has_template_eclipsealloy", this.has(JDTRegistration.TEMPLATE_ECLIPSEALLOY.get()))
                .save(this.output, Identifier.fromNamespaceAndPath(JustDireThings.MODID, idPath(JDTRegistration.EclipseAlloyPaxel.get().asItem()) + "-templateupgrade").toString());

        this.shaped(RecipeCategory.MISC, JDTRegistration.FerricoreBoots.get())
                .pattern("f f")
                .pattern("f f")
                .pattern("   ")
                .define('f', JDTRegistration.FerricoreIngot.get())
                .group("justdirethings")
                .unlockedBy("has_ferricore_ingot", this.has(JDTRegistration.FerricoreIngot.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.FerricoreLeggings.get())
                .pattern("fff")
                .pattern("f f")
                .pattern("f f")
                .define('f', JDTRegistration.FerricoreIngot.get())
                .group("justdirethings")
                .unlockedBy("has_ferricore_ingot", this.has(JDTRegistration.FerricoreIngot.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.FerricoreChestplate.get())
                .pattern("f f")
                .pattern("fff")
                .pattern("fff")
                .define('f', JDTRegistration.FerricoreIngot.get())
                .group("justdirethings")
                .unlockedBy("has_ferricore_ingot", this.has(JDTRegistration.FerricoreIngot.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.FerricoreHelmet.get())
                .pattern("fff")
                .pattern("f f")
                .pattern("   ")
                .define('f', JDTRegistration.FerricoreIngot.get())
                .group("justdirethings")
                .unlockedBy("has_ferricore_ingot", this.has(JDTRegistration.FerricoreIngot.get()))
                .save(this.output);
        this.shaped(RecipeCategory.MISC, JDTRegistration.BlazegoldBoots.get())
                .pattern("f f")
                .pattern("f f")
                .pattern("   ")
                .define('f', JDTRegistration.BlazegoldIngot.get())
                .group("justdirethings")
                .unlockedBy("has_blazegold_ingot", this.has(JDTRegistration.BlazegoldIngot.get()))
                .save(this.output);
        SmithingTransformRecipeBuilder.smithing(Ingredient.of(JDTRegistration.TEMPLATE_BLAZEGOLD.get()), Ingredient.of(JDTRegistration.FerricoreBoots.get()),
                        Ingredient.of(JDTRegistration.BlazegoldIngot.get()), RecipeCategory.MISC, JDTRegistration.BlazegoldBoots.get())
                .unlocks("has_template_blazegold", this.has(JDTRegistration.TEMPLATE_BLAZEGOLD.get()))
                .save(this.output, Identifier.fromNamespaceAndPath(JustDireThings.MODID, idPath(JDTRegistration.BlazegoldBoots.get().asItem()) + "-templateupgrade").toString());
        this.shaped(RecipeCategory.MISC, JDTRegistration.BlazegoldLeggings.get())
                .pattern("fff")
                .pattern("f f")
                .pattern("f f")
                .define('f', JDTRegistration.BlazegoldIngot.get())
                .group("justdirethings")
                .unlockedBy("has_blazegold_ingot", this.has(JDTRegistration.BlazegoldIngot.get()))
                .save(this.output);
        SmithingTransformRecipeBuilder.smithing(Ingredient.of(JDTRegistration.TEMPLATE_BLAZEGOLD.get()), Ingredient.of(JDTRegistration.FerricoreLeggings.get()),
                        Ingredient.of(JDTRegistration.BlazegoldIngot.get()), RecipeCategory.MISC, JDTRegistration.BlazegoldLeggings.get())
                .unlocks("has_template_blazegold", this.has(JDTRegistration.TEMPLATE_BLAZEGOLD.get()))
                .save(this.output, Identifier.fromNamespaceAndPath(JustDireThings.MODID, idPath(JDTRegistration.BlazegoldLeggings.get().asItem()) + "-templateupgrade").toString());
        this.shaped(RecipeCategory.MISC, JDTRegistration.BlazegoldChestplate.get())
                .pattern("f f")
                .pattern("fff")
                .pattern("fff")
                .define('f', JDTRegistration.BlazegoldIngot.get())
                .group("justdirethings")
                .unlockedBy("has_blazegold_ingot", this.has(JDTRegistration.BlazegoldIngot.get()))
                .save(this.output);
        SmithingTransformRecipeBuilder.smithing(Ingredient.of(JDTRegistration.TEMPLATE_BLAZEGOLD.get()), Ingredient.of(JDTRegistration.FerricoreChestplate.get()),
                        Ingredient.of(JDTRegistration.BlazegoldIngot.get()), RecipeCategory.MISC, JDTRegistration.BlazegoldChestplate.get())
                .unlocks("has_template_blazegold", this.has(JDTRegistration.TEMPLATE_BLAZEGOLD.get()))
                .save(this.output, Identifier.fromNamespaceAndPath(JustDireThings.MODID, idPath(JDTRegistration.BlazegoldChestplate.get().asItem()) + "-templateupgrade").toString());
        this.shaped(RecipeCategory.MISC, JDTRegistration.BlazegoldHelmet.get())
                .pattern("fff")
                .pattern("f f")
                .pattern("   ")
                .define('f', JDTRegistration.BlazegoldIngot.get())
                .group("justdirethings")
                .unlockedBy("has_blazegold_ingot", this.has(JDTRegistration.BlazegoldIngot.get()))
                .save(this.output);
        SmithingTransformRecipeBuilder.smithing(Ingredient.of(JDTRegistration.TEMPLATE_BLAZEGOLD.get()), Ingredient.of(JDTRegistration.FerricoreHelmet.get()),
                        Ingredient.of(JDTRegistration.BlazegoldIngot.get()), RecipeCategory.MISC, JDTRegistration.BlazegoldHelmet.get())
                .unlocks("has_template_blazegold", this.has(JDTRegistration.TEMPLATE_BLAZEGOLD.get()))
                .save(this.output, Identifier.fromNamespaceAndPath(JustDireThings.MODID, idPath(JDTRegistration.BlazegoldHelmet.get().asItem()) + "-templateupgrade").toString());
        this.shaped(RecipeCategory.MISC, JDTRegistration.CelestigemBoots.get())
                .pattern("f f")
                .pattern("f f")
                .pattern("   ")
                .define('f', JDTRegistration.Celestigem.get())
                .group("justdirethings")
                .unlockedBy("has_celestigem", this.has(JDTRegistration.Celestigem.get()))
                .save(this.output);
        SmithingTransformRecipeBuilder.smithing(Ingredient.of(JDTRegistration.TEMPLATE_CELESTIGEM.get()), Ingredient.of(JDTRegistration.BlazegoldBoots.get()),
                        Ingredient.of(JDTRegistration.Celestigem.get()), RecipeCategory.MISC, JDTRegistration.CelestigemBoots.get())
                .unlocks("has_template_celestigem", this.has(JDTRegistration.TEMPLATE_CELESTIGEM.get()))
                .save(this.output, Identifier.fromNamespaceAndPath(JustDireThings.MODID, idPath(JDTRegistration.CelestigemBoots.get().asItem()) + "-templateupgrade").toString());
        this.shaped(RecipeCategory.MISC, JDTRegistration.CelestigemLeggings.get())
                .pattern("fff")
                .pattern("f f")
                .pattern("f f")
                .define('f', JDTRegistration.Celestigem.get())
                .group("justdirethings")
                .unlockedBy("has_celestigem", this.has(JDTRegistration.Celestigem.get()))
                .save(this.output);
        SmithingTransformRecipeBuilder.smithing(Ingredient.of(JDTRegistration.TEMPLATE_CELESTIGEM.get()), Ingredient.of(JDTRegistration.BlazegoldLeggings.get()),
                        Ingredient.of(JDTRegistration.Celestigem.get()), RecipeCategory.MISC, JDTRegistration.CelestigemLeggings.get())
                .unlocks("has_template_celestigem", this.has(JDTRegistration.TEMPLATE_CELESTIGEM.get()))
                .save(this.output, Identifier.fromNamespaceAndPath(JustDireThings.MODID, idPath(JDTRegistration.CelestigemLeggings.get().asItem()) + "-templateupgrade").toString());
        this.shaped(RecipeCategory.MISC, JDTRegistration.CelestigemChestplate.get())
                .pattern("f f")
                .pattern("fff")
                .pattern("fff")
                .define('f', JDTRegistration.Celestigem.get())
                .group("justdirethings")
                .unlockedBy("has_celestigem", this.has(JDTRegistration.Celestigem.get()))
                .save(this.output);
        SmithingTransformRecipeBuilder.smithing(Ingredient.of(JDTRegistration.TEMPLATE_CELESTIGEM.get()), Ingredient.of(JDTRegistration.BlazegoldChestplate.get()),
                        Ingredient.of(JDTRegistration.Celestigem.get()), RecipeCategory.MISC, JDTRegistration.CelestigemChestplate.get())
                .unlocks("has_template_celestigem", this.has(JDTRegistration.TEMPLATE_CELESTIGEM.get()))
                .save(this.output, Identifier.fromNamespaceAndPath(JustDireThings.MODID, idPath(JDTRegistration.CelestigemChestplate.get().asItem()) + "-templateupgrade").toString());
        this.shaped(RecipeCategory.MISC, JDTRegistration.CelestigemHelmet.get())
                .pattern("fff")
                .pattern("f f")
                .pattern("   ")
                .define('f', JDTRegistration.Celestigem.get())
                .group("justdirethings")
                .unlockedBy("has_celestigem", this.has(JDTRegistration.Celestigem.get()))
                .save(this.output);
        SmithingTransformRecipeBuilder.smithing(Ingredient.of(JDTRegistration.TEMPLATE_CELESTIGEM.get()), Ingredient.of(JDTRegistration.BlazegoldHelmet.get()),
                        Ingredient.of(JDTRegistration.Celestigem.get()), RecipeCategory.MISC, JDTRegistration.CelestigemHelmet.get())
                .unlocks("has_template_celestigem", this.has(JDTRegistration.TEMPLATE_CELESTIGEM.get()))
                .save(this.output, Identifier.fromNamespaceAndPath(JustDireThings.MODID, idPath(JDTRegistration.CelestigemHelmet.get().asItem()) + "-templateupgrade").toString());
        this.shaped(RecipeCategory.MISC, JDTRegistration.EclipseAlloyBoots.get())
                .pattern("f f")
                .pattern("f f")
                .pattern("   ")
                .define('f', JDTRegistration.EclipseAlloyIngot.get())
                .group("justdirethings")
                .unlockedBy("has_eclipsealloy_ingot", this.has(JDTRegistration.EclipseAlloyIngot.get()))
                .save(this.output);
        SmithingTransformRecipeBuilder.smithing(Ingredient.of(JDTRegistration.TEMPLATE_ECLIPSEALLOY.get()), Ingredient.of(JDTRegistration.CelestigemBoots.get()),
                        Ingredient.of(JDTRegistration.EclipseAlloyIngot.get()), RecipeCategory.MISC, JDTRegistration.EclipseAlloyBoots.get())
                .unlocks("has_template_eclipsealloy", this.has(JDTRegistration.TEMPLATE_ECLIPSEALLOY.get()))
                .save(this.output, Identifier.fromNamespaceAndPath(JustDireThings.MODID, idPath(JDTRegistration.EclipseAlloyBoots.get().asItem()) + "-templateupgrade").toString());
        this.shaped(RecipeCategory.MISC, JDTRegistration.EclipseAlloyLeggings.get())
                .pattern("fff")
                .pattern("f f")
                .pattern("f f")
                .define('f', JDTRegistration.EclipseAlloyIngot.get())
                .group("justdirethings")
                .unlockedBy("has_eclipsealloy_ingot", this.has(JDTRegistration.EclipseAlloyIngot.get()))
                .save(this.output);
        SmithingTransformRecipeBuilder.smithing(Ingredient.of(JDTRegistration.TEMPLATE_ECLIPSEALLOY.get()), Ingredient.of(JDTRegistration.CelestigemLeggings.get()),
                        Ingredient.of(JDTRegistration.EclipseAlloyIngot.get()), RecipeCategory.MISC, JDTRegistration.EclipseAlloyLeggings.get())
                .unlocks("has_template_eclipsealloy", this.has(JDTRegistration.TEMPLATE_ECLIPSEALLOY.get()))
                .save(this.output, Identifier.fromNamespaceAndPath(JustDireThings.MODID, idPath(JDTRegistration.EclipseAlloyLeggings.get().asItem()) + "-templateupgrade").toString());
        this.shaped(RecipeCategory.MISC, JDTRegistration.EclipseAlloyChestplate.get())
                .pattern("f f")
                .pattern("fff")
                .pattern("fff")
                .define('f', JDTRegistration.EclipseAlloyIngot.get())
                .group("justdirethings")
                .unlockedBy("has_eclipsealloy_ingot", this.has(JDTRegistration.EclipseAlloyIngot.get()))
                .save(this.output);
        SmithingTransformRecipeBuilder.smithing(Ingredient.of(JDTRegistration.TEMPLATE_ECLIPSEALLOY.get()), Ingredient.of(JDTRegistration.CelestigemChestplate.get()),
                        Ingredient.of(JDTRegistration.EclipseAlloyIngot.get()), RecipeCategory.MISC, JDTRegistration.EclipseAlloyChestplate.get())
                .unlocks("has_template_eclipsealloy", this.has(JDTRegistration.TEMPLATE_ECLIPSEALLOY.get()))
                .save(this.output, Identifier.fromNamespaceAndPath(JustDireThings.MODID, idPath(JDTRegistration.EclipseAlloyChestplate.get().asItem()) + "-templateupgrade").toString());
        this.shaped(RecipeCategory.MISC, JDTRegistration.EclipseAlloyHelmet.get())
                .pattern("fff")
                .pattern("f f")
                .pattern("   ")
                .define('f', JDTRegistration.EclipseAlloyIngot.get())
                .group("justdirethings")
                .unlockedBy("has_eclipsealloy_ingot", this.has(JDTRegistration.EclipseAlloyIngot.get()))
                .save(this.output);
        SmithingTransformRecipeBuilder.smithing(Ingredient.of(JDTRegistration.TEMPLATE_ECLIPSEALLOY.get()), Ingredient.of(JDTRegistration.CelestigemHelmet.get()),
                        Ingredient.of(JDTRegistration.EclipseAlloyIngot.get()), RecipeCategory.MISC, JDTRegistration.EclipseAlloyHelmet.get())
                .unlocks("has_template_eclipsealloy", this.has(JDTRegistration.TEMPLATE_ECLIPSEALLOY.get()))
                .save(this.output, Identifier.fromNamespaceAndPath(JustDireThings.MODID, idPath(JDTRegistration.EclipseAlloyHelmet.get().asItem()) + "-templateupgrade").toString());


        //Resource Conversions
        this.nineBlockStorageRecipes(RecipeCategory.MISC, JDTRegistration.FerricoreIngot.get(), RecipeCategory.BUILDING_BLOCKS, JDTRegistration.FerricoreBlock.get(),
                JDTRegistration.FerricoreIngot.getId().toString() + "_9x9", "justdirethings",
                JDTRegistration.FerricoreBlock.getId().toString() + "_9x9", "justdirethings");
        this.nineBlockStorageRecipes(RecipeCategory.MISC, JDTRegistration.BlazegoldIngot.get(), RecipeCategory.BUILDING_BLOCKS, JDTRegistration.BlazeGoldBlock.get(),
                JDTRegistration.BlazegoldIngot.getId().toString() + "_9x9", "justdirethings",
                JDTRegistration.BlazeGoldBlock.getId().toString() + "_9x9", "justdirethings");
        this.nineBlockStorageRecipes(RecipeCategory.MISC, JDTRegistration.Celestigem.get(), RecipeCategory.BUILDING_BLOCKS, JDTRegistration.CelestigemBlock.get(),
                JDTRegistration.Celestigem.getId().toString() + "_9x9", "justdirethings",
                JDTRegistration.CelestigemBlock.getId().toString() + "_9x9", "justdirethings");
        this.nineBlockStorageRecipes(RecipeCategory.MISC, JDTRegistration.EclipseAlloyIngot.get(), RecipeCategory.BUILDING_BLOCKS, JDTRegistration.EclipseAlloyBlock.get(),
                JDTRegistration.EclipseAlloyIngot.getId().toString() + "_9x9", "justdirethings",
                JDTRegistration.EclipseAlloyBlock.getId().toString() + "_9x9", "justdirethings");
        this.nineBlockStorageRecipes(RecipeCategory.MISC, JDTRegistration.Coal_T1.get(), RecipeCategory.BUILDING_BLOCKS, JDTRegistration.CoalBlock_T1.get(),
                JDTRegistration.Coal_T1.getId().toString() + "_9x9", "justdirethings",
                JDTRegistration.CoalBlock_T1.getId().toString() + "_9x9", "justdirethings");
        this.nineBlockStorageRecipes(RecipeCategory.MISC, JDTRegistration.Coal_T2.get(), RecipeCategory.BUILDING_BLOCKS, JDTRegistration.CoalBlock_T2.get(),
                JDTRegistration.Coal_T2.getId().toString() + "_9x9", "justdirethings",
                JDTRegistration.CoalBlock_T2.getId().toString() + "_9x9", "justdirethings");
        this.nineBlockStorageRecipes(RecipeCategory.MISC, JDTRegistration.Coal_T3.get(), RecipeCategory.BUILDING_BLOCKS, JDTRegistration.CoalBlock_T3.get(),
                JDTRegistration.Coal_T3.getId().toString() + "_9x9", "justdirethings",
                JDTRegistration.CoalBlock_T3.getId().toString() + "_9x9", "justdirethings");
        this.nineBlockStorageRecipes(RecipeCategory.MISC, JDTRegistration.Coal_T4.get(), RecipeCategory.BUILDING_BLOCKS, JDTRegistration.CoalBlock_T4.get(),
                JDTRegistration.Coal_T4.getId().toString() + "_9x9", "justdirethings",
                JDTRegistration.CoalBlock_T4.getId().toString() + "_9x9", "justdirethings");
        this.nineBlockStorageRecipes(RecipeCategory.MISC, JDTRegistration.TimeCrystal.get(), RecipeCategory.BUILDING_BLOCKS, JDTRegistration.TimeCrystalBlock.get(),
                JDTRegistration.TimeCrystal.getId().toString() + "_9x9", "justdirethings",
                JDTRegistration.TimeCrystalBlock.getId().toString() + "_9x9", "justdirethings");
        this.nineBlockStorageRecipes(RecipeCategory.MISC, Items.CHARCOAL, RecipeCategory.BUILDING_BLOCKS, JDTRegistration.CharcoalBlock.get(),
                JDTRegistration.CharcoalBlock_ITEM.getId().toString() + "_9x9", "justdirethings",
                JDTRegistration.CharcoalBlock.getId().toString() + "_block_9x9", "justdirethings");

        //NBT Clear
        for (var sidedBlock : JDTRegistration.SIDEDBLOCKS.getEntries()) {
            if (sidedBlock.get() instanceof BaseMachineBlock baseMachineBlock) {
                if (sidedBlock.equals(JDTRegistration.ParadoxMachine))
                    continue; //Skip paradox machine, no reseting paradox energy
                this.shapeless(RecipeCategory.MISC, sidedBlock.get())
                        .requires(sidedBlock.get())
                        .group("justdirethings")
                        .unlockedBy("has_" + sidedBlock.getId().getPath(), this.has(sidedBlock.get()))
                        .save(this.output, sidedBlock.getId().toString() + "_nbtclear");
            }
        }
        for (var sidedBlock : JDTRegistration.BLOCKS.getEntries()) {
            if (sidedBlock.get() instanceof BaseMachineBlock baseMachineBlock) {
                this.shapeless(RecipeCategory.MISC, sidedBlock.get())
                        .requires(sidedBlock.get())
                        .group("justdirethings")
                        .unlockedBy("has_" + sidedBlock.getId().getPath(), this.has(sidedBlock.get()))
                        .save(this.output, sidedBlock.getId().toString() + "_nbtclear");
            }
        }

        registerUpgrades();
    }

    public void registerUpgrades() {
        for (var upgrade : JDTRegistration.UPGRADES.getEntries()) {
            Ability ability = Ability.getAbilityFromUpgradeItem(upgrade.get());
            if (ability != null) {
                for (var armor : JDTRegistration.ARMORS.getEntries()) {
                    Item armorItem = armor.get();
                    if (armorItem instanceof ToggleableTool toggleableTool) {
                        EnumSet<Ability> abilities = toggleableTool.getAllAbilities();
                        if (abilities.contains(ability)) {
                            AbilityRecipeBuilder.shapeless(Optional.empty(), Ingredient.of(armor.get()), Ingredient.of(upgrade.get()))
                                    .group("justdirethings")
                                    .unlockedBy("has_upgrade_base", this.has(JDTRegistration.UPGRADE_BASE.get()))
                                    .save(this.output);
                        }
                    }
                }
                for (var tool : JDTRegistration.TOOLS.getEntries()) {
                    Item toolItem = tool.get();
                    if (toolItem instanceof ToggleableTool toggleableTool) {
                        EnumSet<Ability> abilities = toggleableTool.getAllAbilities();
                        if (abilities.contains(ability)) {
                            AbilityRecipeBuilder.shapeless(Optional.empty(), Ingredient.of(tool.get()), Ingredient.of(upgrade.get()))
                                    .group("justdirethings")
                                    .unlockedBy("has_upgrade_base", this.has(JDTRegistration.UPGRADE_BASE.get()))
                                    .save(this.output);
                        }
                    }
                }
                for (var bow : JDTRegistration.BOWS.getEntries()) {
                    Item bowItem = bow.get();
                    if (bowItem instanceof ToggleableTool toggleableTool) {
                        EnumSet<Ability> abilities = toggleableTool.getAllAbilities();
                        if (abilities.contains(ability)) {
                            AbilityRecipeBuilder.shapeless(Optional.empty(), Ingredient.of(bow.get()), Ingredient.of(upgrade.get()))
                                    .group("justdirethings")
                                    .unlockedBy("has_upgrade_base", this.has(JDTRegistration.UPGRADE_BASE.get()))
                                    .save(this.output);
                        }
                    }
                }
            }
        }
    }

    public static class Runner extends RecipeProvider.Runner {
        public Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
            super(packOutput, registries);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
            return new JustDireRecipes(registries, output);
        }

        @Override
        public String getName() {
            return "JustDireThings Recipes";
        }
    }
}
