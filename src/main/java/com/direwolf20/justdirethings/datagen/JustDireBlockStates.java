package com.direwolf20.justdirethings.datagen;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.common.blocks.gooblocks.GooPatternBlock;
import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.Objects;


public class JustDireBlockStates extends BlockStateProvider {
    public JustDireBlockStates(PackOutput output, ExistingFileHelper helper) {
        super(output, JustDireThings.MODID, helper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(Registration.GooBlock_Tier1.get(), models().cubeAll(Registration.GooBlock_Tier1_ITEM.getId().getPath(), blockTexture(Registration.GooBlock_Tier1.get())).renderType("cutout"));
        simpleBlock(Registration.GooBlock_Tier2.get(), models().cubeAll(Registration.GooBlock_Tier2_ITEM.getId().getPath(), blockTexture(Registration.GooBlock_Tier2.get())).renderType("cutout"));
        simpleBlock(Registration.FerricoreBlock.get(), models().cubeAll(Registration.FerricoreBlock_ITEM.getId().getPath(), blockTexture(Registration.FerricoreBlock.get())));
        simpleBlock(Registration.RawFerricoreOre.get(), models().cubeAll(Registration.RawFerricoreOre_ITEM.getId().getPath(), blockTexture(Registration.RawFerricoreOre.get())).renderType("translucent"));
        simpleBlock(Registration.BlazeGoldBlock.get(), models().cubeAll(Registration.BlazeGoldBlock_ITEM.getId().getPath(), blockTexture(Registration.BlazeGoldBlock.get())));
        simpleBlock(Registration.RawBlazegoldOre.get(), models().cubeAll(Registration.RawBlazegoldOre_ITEM.getId().getPath(), blockTexture(Registration.RawBlazegoldOre.get())).renderType("translucent"));
        patternBlock();
    }

    private void patternBlock() {
        getVariantBuilder(Registration.GooPatternBlock.get()).forAllStates(s -> {
            int stage = s.getValue(GooPatternBlock.GOOSTAGE);
            ModelFile model;
            if (stage == 11) {
                model = models().orientableWithBottom(
                        Objects.requireNonNull(Registration.GooPatternBlock.getId()).getPath() + "11",
                        modLoc("block/goopatterns/goorender_full"),
                        modLoc("block/goopatterns/goorender_full"),
                        modLoc("block/goopatterns/goorender_full"),
                        modLoc("block/goopatterns/goorender_full")
                ).renderType("cutout");
            } else if (stage == 10) {
                model = models().orientableWithBottom(
                        Objects.requireNonNull(Registration.GooPatternBlock.getId()).getPath() + "10",
                        modLoc("block/goopatterns/goorender_full"),
                        modLoc("block/goopatterns/goorender_full"),
                        modLoc("block/goopatterns/goorender_full"),
                        modLoc("block/goopatterns/goorender_full")
                ).renderType("cutout");
            } else if (stage == 9) {
                model = models().orientableWithBottom(
                        Objects.requireNonNull(Registration.GooPatternBlock.getId()).getPath() + "9",
                        modLoc("block/goopatterns/goorender_full"),
                        modLoc("block/goopatterns/goorender_full"),
                        modLoc("block/goopatterns/goorender_full"),
                        modLoc("block/goopatterns/goopatterblock_top")
                ).renderType("cutout");
            } else {
                model = models().orientableWithBottom(
                        Objects.requireNonNull(Registration.GooPatternBlock.getId()).getPath() + stage,
                        modLoc("block/goopatterns/goorender_side" + stage),
                        modLoc("block/goopatterns/goorender_side" + stage),
                        modLoc("block/goopatterns/goorender_full"),
                        modLoc("block/goopatterns/goorender_blank")
                ).renderType("cutout");
            }
            return ConfiguredModel.builder()
                    .modelFile(model).build();
        });
    }
}
