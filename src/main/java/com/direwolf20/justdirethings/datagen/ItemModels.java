package com.direwolf20.justdirethings.datagen;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ItemModels extends ItemModelProvider {
    public ItemModels(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, JustDireThings.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        //Block Items
        //withExistingParent(Registration.RenderBlock_ITEM.getId().getPath(), modLoc("block/render_block"));
        //withExistingParent(Registration.LaserNode_ITEM.getId().getPath(), modLoc("block/laser_node"));

        //Item items
        singleTexture(Registration.Fuel_Canister.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/fuel_canister"));
        singleTexture(Registration.Pocket_Generator.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/pocketgenerator"));
    }
}
