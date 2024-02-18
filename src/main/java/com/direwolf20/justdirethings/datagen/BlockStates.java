package com.direwolf20.justdirethings.datagen;

import com.direwolf20.justdirethings.JustDireThings;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;


public class BlockStates extends BlockStateProvider {
    public BlockStates(PackOutput output, ExistingFileHelper helper) {
        super(output, JustDireThings.MODID, helper);
    }

    @Override
    protected void registerStatesAndModels() {
        //models().cubeAll(ForgeRegistries.BLOCKS.getKey(Registration.RenderBlock.get()).getPath(), blockTexture(Registration.RenderBlock.get())).renderType("cutout");
        //simpleBlock(Registration.RenderBlock.get(), models().cubeAll(Registration.RenderBlock.getId().getPath(), blockTexture(Registration.RenderBlock.get())).renderType("cutout"));
        //simpleBlock(Registration.LaserNode.get(), models().getExistingFile(modLoc("block/laser_node")));
    }
}
