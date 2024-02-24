package com.direwolf20.justdirethings.datagen;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;


public class BlockStates extends BlockStateProvider {
    public BlockStates(PackOutput output, ExistingFileHelper helper) {
        super(output, JustDireThings.MODID, helper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(Registration.GooBlock_Tier1.get(), models().cubeAll(Registration.GooBlock_Tier1_ITEM.getId().getPath(), blockTexture(Registration.GooBlock_Tier1.get())));
        simpleBlock(Registration.DireIronBlock.get(), models().cubeAll(Registration.DireIronBlock_ITEM.getId().getPath(), blockTexture(Registration.DireIronBlock.get())));
        //models().cubeAll(ForgeRegistries.BLOCKS.getKey(Registration.RenderBlock.get()).getPath(), blockTexture(Registration.RenderBlock.get())).renderType("cutout");
        //simpleBlock(Registration.RenderBlock.get(), models().cubeAll(Registration.RenderBlock.getId().getPath(), blockTexture(Registration.RenderBlock.get())).renderType("cutout"));
        //simpleBlock(Registration.LaserNode.get(), models().getExistingFile(modLoc("block/laser_node")));
    }
}
