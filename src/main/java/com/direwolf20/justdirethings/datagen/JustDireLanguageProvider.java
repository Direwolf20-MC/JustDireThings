package com.direwolf20.justdirethings.datagen;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.setup.ModSetup;
import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.data.PackOutput;


public class JustDireLanguageProvider extends net.neoforged.neoforge.common.data.LanguageProvider {
    public JustDireLanguageProvider(PackOutput output, String locale) {
        super(output, JustDireThings.MODID, locale);
    }

    @Override
    protected void addTranslations() {
        add("itemGroup." + ModSetup.TAB_JUSTDIRETHINGS, "Just Dire Things");

        //Blocks
        add(Registration.GooSoil_Tier1.get(), "Primogel Soil");
        add(Registration.GooSoil_Tier2.get(), "Blazebloom Soil");
        add(Registration.GooSoil_Tier3.get(), "VoidShimmer Soil");
        add(Registration.GooSoil_Tier4.get(), "Shadowpulse Soil");
        add(Registration.GooBlock_Tier1.get(), "Primogel Goo");
        add(Registration.GooBlock_Tier2.get(), "Blazebloom Goo");
        add(Registration.GooBlock_Tier3.get(), "VoidShimmer Goo");
        add(Registration.GooBlock_Tier4.get(), "Shadowpulse Goo");
        add(Registration.ItemCollector.get(), "Item Collector");
        add(Registration.BlockBreakerT1.get(), "Simple Block Breaker");
        add(Registration.BlockBreakerT2.get(), "Advanced Block Breaker");
        add(Registration.BlockPlacerT1.get(), "Simple Block Placer");
        add(Registration.BlockPlacerT2.get(), "Advanced Block Placer");
        add(Registration.ClickerT1.get(), "Simple Clicker");
        add(Registration.ClickerT2.get(), "Advanced Clicker");
        add(Registration.SensorT1.get(), "Simple Sensor");
        add(Registration.SensorT2.get(), "Advanced Sensor");
        add(Registration.DropperT1.get(), "Simple Dropper");
        add(Registration.DropperT2.get(), "Advanced Dropper");
        add(Registration.GeneratorT1.get(), "Simple Coal Generator");
        add(Registration.GeneratorFluidT1.get(), "Simple Fuel Generator");
        add(Registration.EnergyTransmitter.get(), "Energy Transmitter");
        add(Registration.BlockSwapperT1.get(), "Simple Swapper");
        add(Registration.BlockSwapperT2.get(), "Advanced Swapper");
        add(Registration.PlayerAccessor.get(), "Player Accessor");
        add(Registration.EclipseGateBlock.get(), "Eclipse Gate");
        add(Registration.FluidPlacerT1.get(), "Simple Fluid Placer");
        add(Registration.FluidPlacerT2.get(), "Advanced Fluid Placer");
        add(Registration.FluidCollectorT1.get(), "Simple Fluid Collector");
        add(Registration.FluidCollectorT2.get(), "Advanced Fluid Collector");

        //Fluids
        add(Registration.PORTAL_FLUID_BLOCK.get(), "Portal Fluid");
        add(Registration.PORTAL_FLUID_BUCKET.get(), "Portal Fluid Bucket");
        add("fluid_type.justdirethings.portal_fluid_type", "Portal Fluid");
        add(Registration.UNSTABLE_PORTAL_FLUID_BLOCK.get(), "Unstable Portal Fluid");
        add(Registration.UNSTABLE_PORTAL_FLUID_BUCKET.get(), "Unstable Portal Fluid Bucket");
        add("fluid_type.justdirethings.unstable_portal_fluid_type", "Unstable Portal Fluid");
        add(Registration.POLYMORPHIC_FLUID_BLOCK.get(), "Polymorphic Fluid");
        add(Registration.POLYMORPHIC_FLUID_BUCKET.get(), "Polymorphic Fluid Bucket");
        add("fluid_type.justdirethings.polymorphic_fluid_type", "Polymorphic Fluid");
        add(Registration.UNREFINED_T2_FLUID_BLOCK.get(), "Unrefined Blaze Ember Fuel");
        add(Registration.UNREFINED_T2_FLUID_BUCKET.get(), "Unrefined Blaze Ember Fuel Bucket");
        add("fluid_type.justdirethings.unrefined_t2_fluid_type", "Unrefined Blaze Ember Fuel");
        add(Registration.REFINED_T2_FLUID_BLOCK.get(), "Blaze Ember Fuel");
        add(Registration.REFINED_T2_FLUID_BUCKET.get(), "Blaze Ember Fuel Bucket");
        add("fluid_type.justdirethings.refined_t2_fluid_type", "Blaze Ember Fuel");
        add(Registration.UNREFINED_T3_FLUID_BLOCK.get(), "Unrefined Voidflame Fuel");
        add(Registration.UNREFINED_T3_FLUID_BUCKET.get(), "Unrefined Voidflame Fuel Bucket");
        add("fluid_type.justdirethings.unrefined_t3_fluid_type", "Unrefined Voidflame Fuel");
        add(Registration.REFINED_T3_FLUID_BLOCK.get(), "Voidflame Fuel");
        add(Registration.REFINED_T3_FLUID_BUCKET.get(), "Voidflame Fuel Bucket");
        add("fluid_type.justdirethings.refined_t3_fluid_type", "Voidflame Fuel");
        add(Registration.UNREFINED_T4_FLUID_BLOCK.get(), "Unrefined Eclipse Ember Fuel");
        add(Registration.UNREFINED_T4_FLUID_BUCKET.get(), "Unrefined Eclipse Ember Fuel Bucket");
        add("fluid_type.justdirethings.unrefined_t4_fluid_type", "Unrefined Eclipse Ember Fuel");
        add(Registration.REFINED_T4_FLUID_BLOCK.get(), "Eclipse Ember Fuel");
        add(Registration.REFINED_T4_FLUID_BUCKET.get(), "Eclipse Ember Fuel Bucket");
        add("fluid_type.justdirethings.refined_t4_fluid_type", "Eclipse Ember Fuel");

        //Resources
        add(Registration.FerricoreBlock.get(), "Ferricore Block");
        add(Registration.RawFerricoreOre.get(), "Raw Ferricore Ore");
        add(Registration.BlazeGoldBlock.get(), "Blazegold Block");
        add(Registration.RawBlazegoldOre.get(), "Raw Blazegold Ore");
        add(Registration.RawCelestigemOre.get(), "Raw Celestigem Ore");
        add(Registration.CelestigemBlock.get(), "Celestigem Block");
        add(Registration.RawEclipseAlloyOre.get(), "Raw Eclipse Alloy Ore");
        add(Registration.EclipseAlloyBlock.get(), "Eclipse Alloy Block");
        add(Registration.RawCoal_T1.get(), "Raw Primal Coal");
        add(Registration.CoalBlock_T1.get(), "Primal Coal Block");
        add(Registration.RawCoal_T2.get(), "Raw Blaze Ember");
        add(Registration.CoalBlock_T2.get(), "Blaze Ember Block");
        add(Registration.RawCoal_T3.get(), "Raw Voidflame Coal");
        add(Registration.CoalBlock_T3.get(), "Voidflame Coal Block");
        add(Registration.RawCoal_T4.get(), "Raw Eclipse Ember");
        add(Registration.CoalBlock_T4.get(), "Eclipse Ember Block");
        add(Registration.PolymorphicCatalyst.get(), "Polymorphic Catalyst");
        add(Registration.PortalFluidCatalyst.get(), "Portal Fluid Catalyst");

        //Items
        add(Registration.Fuel_Canister.get(), "Fuel Canister");
        add(Registration.Pocket_Generator.get(), "Pocket Generator");
        add(Registration.TotemOfDeathRecall.get(), "Totem of Death Recall");
        add(Registration.BlazejetWand.get(), "Blazejet Wand");
        add(Registration.VoidshiftWand.get(), "Voidshift Wand");
        add(Registration.EclipsegateWand.get(), "Eclipsegate Wand");
        add(Registration.CreatureCatcher.get(), "Creature Catcher");
        add(Registration.MachineSettingsCopier.get(), "Machine Settings Copier");
        add(Registration.PortalGun.get(), "Portal Gun");
        add(Registration.PortalGunV2.get(), "Advanced Portal Gun");
        add(Registration.FluidCanister.get(), "Fluid Canister");

        //Tools
        add(Registration.FerricoreSword.get(), "Ferricore Sword");
        add(Registration.FerricorePickaxe.get(), "Ferricore Pickaxe");
        add(Registration.FerricoreShovel.get(), "Ferricore Shovel");
        add(Registration.FerricoreAxe.get(), "Ferricore Axe");
        add(Registration.FerricoreHoe.get(), "Ferricore Hoe");
        add(Registration.BlazegoldSword.get(), "Blazegold Sword");
        add(Registration.BlazegoldPickaxe.get(), "Blazegold Pickaxe");
        add(Registration.BlazegoldShovel.get(), "Blazegold Shovel");
        add(Registration.BlazegoldAxe.get(), "Blazegold Axe");
        add(Registration.BlazegoldHoe.get(), "Blazegold Hoe");
        add(Registration.CelestigemSword.get(), "Celestigem Sword");
        add(Registration.CelestigemPickaxe.get(), "Celestigem Pickaxe");
        add(Registration.CelestigemShovel.get(), "Celestigem Shovel");
        add(Registration.CelestigemAxe.get(), "Celestigem Axe");
        add(Registration.CelestigemHoe.get(), "Celestigem Hoe");
        add(Registration.EclipseAlloySword.get(), "Eclipse Alloy Sword");
        add(Registration.EclipseAlloyPickaxe.get(), "Eclipse Alloy Pickaxe");
        add(Registration.EclipseAlloyShovel.get(), "Eclipse Alloy Shovel");
        add(Registration.EclipseAlloyAxe.get(), "Eclipse Alloy Axe");
        add(Registration.EclipseAlloyHoe.get(), "Eclipse Alloy Hoe");
        add(Registration.CelestigemPaxel.get(), "Celestigem Paxel");
        add(Registration.EclipseAlloyPaxel.get(), "Eclipse Alloy Paxel");
        add(Registration.FerricoreWrench.get(), "Ferricore Wrench");

        //Armors
        add(Registration.FerricoreBoots.get(), "Ferricore Boots");
        add(Registration.FerricoreChestplate.get(), "Ferricore Chestplate");
        add(Registration.FerricoreLeggings.get(), "Ferricore Leggings");
        add(Registration.FerricoreHelmet.get(), "Ferricore Helmet");
        add(Registration.BlazegoldBoots.get(), "Blazegold Boots");
        add(Registration.BlazegoldChestplate.get(), "Blazegold Chestplate");
        add(Registration.BlazegoldLeggings.get(), "Blazegold Leggings");
        add(Registration.BlazegoldHelmet.get(), "Blazegold Helmet");
        add(Registration.CelestigemBoots.get(), "Celestigem Boots");
        add(Registration.CelestigemChestplate.get(), "Celestigem Chestplate");
        add(Registration.CelestigemLeggings.get(), "Celestigem Leggings");
        add(Registration.CelestigemHelmet.get(), "Celestigem Helmet");
        add(Registration.EclipseAlloyBoots.get(), "Eclipse Alloy Boots");
        add(Registration.EclipseAlloyChestplate.get(), "Eclipse Alloy Chestplate");
        add(Registration.EclipseAlloyLeggings.get(), "Eclipse Alloy Leggings");
        add(Registration.EclipseAlloyHelmet.get(), "Eclipse Alloy Helmet");

        //Resources
        add(Registration.FerricoreIngot.get(), "Ferricore Ingot");
        add(Registration.RawFerricore.get(), "Raw Ferricore");
        add(Registration.BlazegoldIngot.get(), "Blazegold Ingot");
        add(Registration.RawBlazegold.get(), "Raw Blazegold");
        add(Registration.Celestigem.get(), "Celestigem");
        add(Registration.EclipseAlloyIngot.get(), "Eclipse Alloy Ingot");
        add(Registration.RawEclipseAlloy.get(), "Raw Eclipse Alloy");
        add(Registration.Coal_T1.get(), "Primal Coal");
        add(Registration.Coal_T2.get(), "Blaze Ember");
        add(Registration.Coal_T3.get(), "Voidflame Coal");
        add(Registration.Coal_T4.get(), "Eclipse Ember");

        //Upgrades
        add(Registration.UPGRADE_BASE.get(), "Upgrade: Blank");
        add(Registration.UPGRADE_ELYTRA.get(), "Upgrade: Elytra");


        //Misc
        add("justdirethings.shiftmoreinfo", "Hold Shift for details");
        add("justdirethings.presshotkey", "<Press %s>");
        add("justdirethings.enabled", "Enabled");
        add("justdirethings.disabled", "Disabled");
        add("justdirethings.lowenergy", "Insufficient Energy");
        add("justdirethings.fuelcanisteramt", "Cook time (ticks): %d");
        add("justdirethings.fuelcanisteramtstack", "Stack Cook time (ticks): %d");
        add("justdirethings.fuelcanisteritemsamt", "Fuel Amount: %f");
        add("justdirethings.fuelcanisteritemsamtstack", "Stack Fuel Amount: %f");
        add("justdirethings.pocketgeneratorburntime", "Burn Time: %f / %f");
        add("justdirethings.pocketgeneratorfuelstack", "Fuel: %f %s");
        add("justdirethings.pocketgeneratornofuel", "Fuel Empty");
        add("justdirethings.festored", "Forge Energy: %s / %s");
        add("justdirethings.portalfluidamt", "Portal Fluid: %s / %s");
        add("justdirethings.lowportalfluid", "Insufficient Portal Fluid");
        add("justdirethings.boundto", "Bound to: %s:%s");
        add("justdirethings.boundto-missing", "Bound to (MISSING BLOCK): %s:%s");
        add("justdirethings.unbound", " -Not Bound");
        add("justdirethings.bindfailed", "Binding Failed");
        add("justdirethings.bindremoved", "Binding Removed");
        add("justdirethings.unbound-screen", "Not Bound");
        add("justdirethings.bound-key", "Bound to: %s");
        add("justdirethings.bound-mouse", "Mouse Button: %s");
        add("justdirethings.boundside", " -Bound Side: ");
        add("justdirethings.creature", "Creature: ");
        add("justdirethings.ability", "Ability: %s - %s");
        add("justdirethings.toolenabled", "Tool: %s - %s");
        add("justdirethings.settingscopied", "Settings Copied");
        add("justdirethings.settingspasted", "Settings Pasted");
        add("justdirethings.fillmode.changed", "Fill Mode Set to: %s");
        add("justdirethings.fluidamt", "Amount: ");
        add("justdirethings.fluidname", "Fluid: ");
        add("justdirethings.fillmode", "Fill Mode: ");
        add("justdirethings.fillmode.none", "None");
        add("justdirethings.fillmode.jdtonly", "JustDireThings");
        add("justdirethings.fillmode.all", "All");
        add("justdirethings.hint.dropinwater", "Drop in Water");
        add("justdirethings.decoy", "Decoy");
        add("justdirethings.missingupgrade", "(Missing Upgrade)");

        //Keys
        add("justdirethings.key.category", "Just Dire Things");
        add("justdirethings.key.toggle_tool", "Toggle Tool Abilities");

        //Abilities
        add(Ability.MOBSCANNER.getLocalization(), "Mob Scanner");
        add(Ability.ORESCANNER.getLocalization(), "Ore Scanner");
        add(Ability.OREMINER.getLocalization(), "Ore Miner");
        add(Ability.LAWNMOWER.getLocalization(), "Lawnmower");
        add(Ability.SKYSWEEPER.getLocalization(), "Sky Sweeper");
        add(Ability.TREEFELLER.getLocalization(), "Tree Feller");
        add(Ability.LEAFBREAKER.getLocalization(), "Leaf Breaker");
        add(Ability.SMELTER.getLocalization(), "Auto Smelter");
        add(Ability.SMOKER.getLocalization(), "Auto Smoker");
        add(Ability.LAVAREPAIR.getLocalization(), "Lava Repair");
        add(Ability.CAUTERIZEWOUNDS.getLocalization(), "Cauterize Wounds");
        add(Ability.HAMMER.getLocalization(), "Hammer");
        add(Ability.HAMMER.getLocalization() + "_off", "Hammer: Disabled");
        add(Ability.HAMMER.getLocalization() + "_3", "Hammer: 3x3");
        add(Ability.HAMMER.getLocalization() + "_5", "Hammer: 5x5");
        add(Ability.HAMMER.getLocalization() + "_7", "Hammer: 7x7");
        add(Ability.OREXRAY.getLocalization(), "X-Ray");
        add(Ability.DROPTELEPORT.getLocalization(), "Drops Teleporter");
        add(Ability.GLOWING.getLocalization(), "Mob X-Ray");
        add(Ability.INSTABREAK.getLocalization(), "Instant Break");
        add(Ability.AIRBURST.getLocalization(), "Air Burst");
        add(Ability.VOIDSHIFT.getLocalization(), "Void Shift");
        add(Ability.ECLIPSEGATE.getLocalization(), "Eclipse Gate");
        add(Ability.RUNSPEED.getLocalization(), "Run Speed");
        add(Ability.WALKSPEED.getLocalization(), "Walk Speed");
        add(Ability.SWIMSPEED.getLocalization(), "Swim Speed");
        add(Ability.GROUNDSTOMP.getLocalization(), "Ground Stomp");
        add(Ability.EXTINGUISH.getLocalization(), "Extinguish");
        add(Ability.STUPEFY.getLocalization(), "Stupefy");
        add(Ability.STEPHEIGHT.getLocalization(), "Step Assist");
        add(Ability.JUMPBOOST.getLocalization(), "Jump Boost");
        add(Ability.MINDFOG.getLocalization(), "Mind Fog");
        add(Ability.INVULNERABILITY.getLocalization(), "Invulnerability");
        add(Ability.ELYTRA.getLocalization(), "Elytra");
        add(Ability.NEGATEFALLDAMAGE.getLocalization(), "Negate Fall Damage");
        add(Ability.DECOY.getLocalization(), "Decoy");
        add(Ability.NIGHTVISION.getLocalization(), "Night Vision");
        add(Ability.DEATHPROTECTION.getLocalization(), "Death Protection");
        add(Ability.DEBUFFREMOVER.getLocalization(), "Debuff Remover");
        add(Ability.NOAI.getLocalization(), "Mental Obliteration");
        add(Ability.FLIGHT.getLocalization(), "Flight");
        add(Ability.LAVAIMMUNITY.getLocalization(), "Lava Immunity");
        add(Ability.PHASE.getLocalization(), "Phase");
        add(Ability.EARTHQUAKE.getLocalization(), "Earthquake");

        //GUI
        add("justdirethings.screen.energy", "Energy: %s/%s FE");
        add("justdirethings.screen.fluid", "%s: %s/%s MB");
        add("justdirethings.screen.energycost", "Energy Cost: %s");
        add("justdirethings.screen.fepertick", "FE/T: %s");
        add("justdirethings.screen.no_fuel", "Fuel source empty");
        add("justdirethings.screen.burn_time", "Burn time left: %ss");
        add("justdirethings.screen.ignored", "Ignored");
        add("justdirethings.screen.low", "Low");
        add("justdirethings.screen.high", "High");
        add("justdirethings.screen.pulse", "Pulse");
        add("justdirethings.screen.allowlist", "Allow List");
        add("justdirethings.screen.denylist", "Deny List");
        add("justdirethings.screen.renderarea", "Render Area");
        add("justdirethings.screen.comparenbt", "Compare NBT");
        add("justdirethings.screen.direction-down", "Down");
        add("justdirethings.screen.direction-up", "Up");
        add("justdirethings.screen.direction-north", "North");
        add("justdirethings.screen.direction-south", "South");
        add("justdirethings.screen.direction-west", "West");
        add("justdirethings.screen.direction-east", "East");
        add("justdirethings.screen.direction-none", "None");
        add("justdirethings.screen.filter-block", "Filter: Block");
        add("justdirethings.screen.filter-item", "Filter: Item");
        add("justdirethings.screen.tickspeed", "Speed (Ticks)");
        add("justdirethings.screen.click-right", "Right Click");
        add("justdirethings.screen.click-left", "Left Click");
        add("justdirethings.screen.click-custom", "Custom Binding");
        add("justdirethings.screen.setbinding", "Set Binding");
        add("justdirethings.screen.target-block", "Target Blocks");
        add("justdirethings.screen.target-noblock", "Ignore Blocks");
        add("justdirethings.screen.target-air", "Target Air");
        add("justdirethings.screen.target-hostile", "Target Hostile");
        add("justdirethings.screen.target-passive", "Target Passive");
        add("justdirethings.screen.target-adult", "Target Adult");
        add("justdirethings.screen.target-child", "Target Child");
        add("justdirethings.screen.target-player", "Target Player");
        add("justdirethings.screen.target-living", "Target All Living");
        add("justdirethings.screen.target-item", "Target Items");
        add("justdirethings.screen.entity-none", "No Entities");
        add("justdirethings.screen.entity-all", "All Entities");
        add("justdirethings.screen.sneak-click", "Sneak Click");
        add("justdirethings.screen.showfakeplayer", "Show Fake Player");
        add("justdirethings.screen.redstone-weak", "Weak Signal");
        add("justdirethings.screen.redstone-strong", "Strong Signal");
        add("justdirethings.screen.senseamount", "Sense Amount");
        add("justdirethings.screen.greaterthan", "Greater Than");
        add("justdirethings.screen.lessthan", "Less Than");
        add("justdirethings.screen.equals", "Equals");
        add("justdirethings.screen.dropcount", "Drop Amount");
        add("justdirethings.screen.showparticles", "Show Particles");
        add("justdirethings.screen.showrender", "Show Render");
        add("justdirethings.screen.burnspeedmultiplier", "Burn Speed Multiplier: %s");
        add("justdirethings.screen.click-hold", "Hold Click");
        add("justdirethings.screen.click-hold-for", "Hold Click For (ticks)");
        add("justdirethings.screen.inv-normal", "Inventory Slots");
        add("justdirethings.screen.inv-armor", "Armor Slots");
        add("justdirethings.screen.inv-offhand", "Offhand Slots");
        add("justdirethings.screen.rightclicksettings", "Right Click for Settings");
        add("justdirethings.screen.copy_area", "Copy Area");
        add("justdirethings.screen.copy_offset", "Copy Offset");
        add("justdirethings.screen.copy_filter", "Copy Filters");
        add("justdirethings.screen.copy_redstone", "Copy Redstone");
        add("justdirethings.screen.add_favorite", "Add Favorite");
        add("justdirethings.screen.remove_favorite", "Remove Favorite");
        add("justdirethings.screen.edit_favorite", "Edit Favorite");
        add("justdirethings.screen.stay_open", "Stay Open");
        add("justdirethings.screen.save_close", "Save and Close");
        add("justdirethings.screen.cancel", "Cancel");

        //Buttons
        //add("justdirethings.buttons.save", "Save");

        //Entities
        add("entity.justdirethings.decoy_entity", "Decoy Entity");

        //Recipes
        add("justdirethings.goospreadrecipe.title", "Goo Spreading Recipes");
        add("justdirethings.fluiddroprecipe.title", "Fluid Drop Recipes");

    }
}
