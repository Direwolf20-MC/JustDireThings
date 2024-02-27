package com.direwolf20.justdirethings.datagen;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.setup.ModSetup;
import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.data.PackOutput;


public class LanguageProvider extends net.neoforged.neoforge.common.data.LanguageProvider {
    public LanguageProvider(PackOutput output, String locale) {
        super(output, JustDireThings.MODID, locale);
    }

    @Override
    protected void addTranslations() {
        add("itemGroup." + ModSetup.TAB_JUSTDIRETHINGS, "Just Dire Things");

        //Blocks
        add(Registration.GooBlock_Tier1.get(), "GooBlock Tier 1");
        add(Registration.GooBlock_Tier2.get(), "GooBlock Tier 2");
        add(Registration.DireIronBlock.get(), "Dire Iron Block");

        //Items
        add(Registration.Fuel_Canister.get(), "Fuel Canister");
        add(Registration.Pocket_Generator.get(), "Pocket Generator");
        add(Registration.DireIronIngot.get(), "Dire Iron Ingot");

        //Misc
        add("justdirethings.shiftmoreinfo", "Hold Shift for details");
        add("justdirethings.fuelcanisteramt", "Cook time (ticks): %d");
        add("justdirethings.fuelcanisteramtstack", "Stack Cook time (ticks): %d");
        add("justdirethings.fuelcanisteritemsamt", "Fuel Amount: %f");
        add("justdirethings.fuelcanisteritemsamtstack", "Stack Fuel Amount: %f");
        add("justdirethings.pocketgeneratorburntime", "Burn Time: %f / %f");
        add("justdirethings.pocketgeneratorfuelstack", "Fuel: %f %s");
        add("justdirethings.pocketgeneratornofuel", "Fuel Empty");
        add("justdirethings.pocketgeneratorrf", "Forge Energy: %s / %s");

        //Keys
        //add("key.justdirethings.category", "Building Gadgets 2");

        //Tooltips
        //add("justdirethings.tooltips.holdshift", "Hold Shift for details");

        //GUI
        add("justdirethings.screen.energy", "Energy: %s/%s FE");
        add("justdirethings.screen.no_fuel", "Fuel source empty");
        add("justdirethings.screen.burn_time", "Burn time left: %ss");

        //Buttons
        //add("justdirethings.buttons.save", "Save");

        //Messages to Player
        //add("justdirethings.messages.invalidblock", "Invalid Block");

    }
}
