package com.direwolf20.justdirethings.datagen;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.common.items.tools.utils.Ability;
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
        add(Registration.GooSoil_Tier1.get(), "Goo Soil Tier1");
        add(Registration.GooSoil_Tier2.get(), "Goo Soil Tier2");
        add(Registration.GooBlock_Tier1.get(), "Primogel Goo");
        add(Registration.GooBlock_Tier2.get(), "Blazebloom Goo");
        add(Registration.GooBlock_Tier3.get(), "VoidShimmer Goo");

        //Resources
        add(Registration.FerricoreBlock.get(), "Ferricore Block");
        add(Registration.RawFerricoreOre.get(), "Raw Ferricore Ore");
        add(Registration.BlazeGoldBlock.get(), "Blazegold Block");
        add(Registration.RawBlazegoldOre.get(), "Raw Blazegold Ore");
        add(Registration.RawCelestigemOre.get(), "Raw Celestigem Ore");

        //Items
        add(Registration.Fuel_Canister.get(), "Fuel Canister");
        add(Registration.Pocket_Generator.get(), "Pocket Generator");
        add(Registration.Pocket_GeneratorT2.get(), "Improved Pocket Generator");

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


        //Resources
        add(Registration.FerricoreIngot.get(), "Ferricore Ingot");
        add(Registration.RawFerricore.get(), "Raw Ferricore");
        add(Registration.BlazegoldIngot.get(), "Blazegold Ingot");
        add(Registration.RawBlazegold.get(), "Raw Blazegold");
        add(Registration.Celestigem.get(), "Celestigem");

        //Misc
        add("justdirethings.shiftmoreinfo", "Hold Shift for details");
        add("justdirethings.presshotkey", "<Press %s>");
        add("justdirethings.enabled", "Enabled");
        add("justdirethings.disabled", "Disabled");
        add("justdirethings.fuelcanisteramt", "Cook time (ticks): %d");
        add("justdirethings.fuelcanisteramtstack", "Stack Cook time (ticks): %d");
        add("justdirethings.fuelcanisteritemsamt", "Fuel Amount: %f");
        add("justdirethings.fuelcanisteritemsamtstack", "Stack Fuel Amount: %f");
        add("justdirethings.pocketgeneratorburntime", "Burn Time: %f / %f");
        add("justdirethings.pocketgeneratorfuelstack", "Fuel: %f %s");
        add("justdirethings.pocketgeneratornofuel", "Fuel Empty");
        add("justdirethings.festored", "Forge Energy: %s / %s");
        add("justdirethings.boundto", "Bound to: %s:%s");
        add("justdirethings.unbound", " -Not Bound");

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
        add(Ability.HAMMER.getLocalization(), "Hammer");
        add(Ability.HAMMER.getLocalization() + "value", "Hammer: %s");
        add(Ability.OREXRAY.getLocalization(), "X-Ray");
        add(Ability.DROPTELEPORT.getLocalization(), "Drops Teleporter");

        //GUI
        add("justdirethings.screen.energy", "Energy: %s/%s FE");
        add("justdirethings.screen.no_fuel", "Fuel source empty");
        add("justdirethings.screen.burn_time", "Burn time left: %ss");

        //Buttons
        //add("justdirethings.buttons.save", "Save");

        //Messages to Player
        //add("justdirethings.messages.invalidblock", "Invalid Block");

        //Recipes
        add("justdirethings.goospreadrecipe.title", "Goo Spreading Recipes");

    }
}
