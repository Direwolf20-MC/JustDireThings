package com.direwolf20.justdirethings.datagen;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class JustDireBlockTags extends BlockTagsProvider {

    public JustDireBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, JustDireThings.MODID, existingFileHelper);
    }

    public static final TagKey<Block> LAWNMOWERABLE = BlockTags.create(new ResourceLocation(JustDireThings.MODID, "lawnmowerable"));

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BlockTags.MINEABLE_WITH_SHOVEL)
                .add(Registration.GooBlock_Tier1.get())
                .add(Registration.GooBlock_Tier2.get())
                .add(Registration.GooSoil_Tier1.get())
                .add(Registration.GooSoil_Tier2.get())
                .add(Registration.GooSoil_Tier3.get());
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(Registration.FerricoreBlock.get())
                .add(Registration.RawFerricoreOre.get())
                .add(Registration.BlazeGoldBlock.get())
                .add(Registration.RawBlazegoldOre.get())
                .add(Registration.CelestigemBlock.get())
                .add(Registration.RawCelestigemOre.get());
        tag(LAWNMOWERABLE)
                .addTag(BlockTags.FLOWERS)
                .add(Blocks.TALL_GRASS)
                .add(Blocks.SHORT_GRASS)
                .add(Blocks.DEAD_BUSH)
                .add(Blocks.SWEET_BERRY_BUSH);
        tag(Tags.Blocks.ORES)
                .add(Registration.RawFerricoreOre.get())
                .add(Registration.RawBlazegoldOre.get())
                .add(Registration.RawCelestigemOre.get());
    }

    @Override
    public String getName() {
        return "JustDireThings Tags";
    }
}
