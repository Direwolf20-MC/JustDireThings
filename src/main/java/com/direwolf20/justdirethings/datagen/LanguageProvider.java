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
        //add(Registration.TemplateManager.get(), "Template Manager");

        //Items
        add(Registration.Fuel_Canister.get(), "Fuel Canister");

        //Misc
        add("justdirethings.fuelcanisteramt", "Cook time (ticks): %d");
        add("justdirethings.fuelcanisteritemsamt", "Fuel Amount: %f");

        //Keys
        //add("key.justdirethings.category", "Building Gadgets 2");

        //Tooltips
        //add("justdirethings.tooltips.holdshift", "Hold Shift for details");

        //GUI
        //add("justdirethings.screen.confirm", "Confirm");

        //Buttons
        //add("justdirethings.buttons.save", "Save");

        //Messages to Player
        //add("justdirethings.messages.invalidblock", "Invalid Block");

    }
}
