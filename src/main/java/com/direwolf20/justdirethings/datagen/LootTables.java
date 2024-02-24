package com.direwolf20.justdirethings.datagen;

import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.data.loot.packs.VanillaBlockLoot;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.stream.Collectors;

public class LootTables extends VanillaBlockLoot {

    @Override
    protected void generate() {
        dropWhenSilkTouch(Registration.GooBlock_Tier1.get());
        dropSelf(Registration.DireIronBlock.get());
        dropSelf(Registration.TestBlock.get());
        //add(Registration.RenderBlock.get(), noDrop());
        //dropSelf(Registration.TemplateManager.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return Registration.BLOCKS.getEntries().stream().map(DeferredHolder::get).collect(Collectors.toList());
    }
}
