package com.direwolf20.justdirethings.datagen;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class BlockTags extends BlockTagsProvider {

    public BlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, JustDireThings.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(net.minecraft.tags.BlockTags.MINEABLE_WITH_SHOVEL)
                .add(Registration.GooBlock_Tier1.get());
        tag(net.minecraft.tags.BlockTags.MINEABLE_WITH_SHOVEL)
                .add(Registration.GooBlock_Tier2.get());
        tag(net.minecraft.tags.BlockTags.MINEABLE_WITH_PICKAXE)
                .add(Registration.FerricoreBlock.get());
        tag(net.minecraft.tags.BlockTags.MINEABLE_WITH_PICKAXE)
                .add(Registration.BlazeGoldBlock.get());
    }

    @Override
    public String getName() {
        return "JustDireThings Tags";
    }
}
