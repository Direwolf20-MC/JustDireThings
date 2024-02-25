package com.direwolf20.justdirethings.datagen;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.common.blocks.gooblocks.GooPatternBlock;
import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.Objects;


public class BlockStates extends BlockStateProvider {
    public BlockStates(PackOutput output, ExistingFileHelper helper) {
        super(output, JustDireThings.MODID, helper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(Registration.GooBlock_Tier1.get(), models().cubeAll(Registration.GooBlock_Tier1_ITEM.getId().getPath(), blockTexture(Registration.GooBlock_Tier1.get())));
        simpleBlock(Registration.DireIronBlock.get(), models().cubeAll(Registration.DireIronBlock_ITEM.getId().getPath(), blockTexture(Registration.DireIronBlock.get())));


        getVariantBuilder(Registration.GooPatternBlock.get()) //TODO Loop
                .partialState().with(GooPatternBlock.GOOSTAGE, 0).setModels(new ConfiguredModel(models().orientableWithBottom(
                        Objects.requireNonNull(Registration.GooPatternBlock.getId()).getPath() + "0",
                        modLoc("block/goopatterns/goorender_side0"),
                        modLoc("block/goopatterns/goorender_side0"),
                        modLoc("block/goopatterns/goorender_full"),
                        modLoc("block/goopatterns/goorender_blank")
                ).renderType("cutout")))
                .partialState().with(GooPatternBlock.GOOSTAGE, 1).setModels(new ConfiguredModel(models().orientableWithBottom(
                        Objects.requireNonNull(Registration.GooPatternBlock.getId()).getPath() + "1",
                        modLoc("block/goopatterns/goorender_side1"),
                        modLoc("block/goopatterns/goorender_side1"),
                        modLoc("block/goopatterns/goorender_full"),
                        modLoc("block/goopatterns/goorender_blank")
                ).renderType("cutout")))
                .partialState().with(GooPatternBlock.GOOSTAGE, 2).setModels(new ConfiguredModel(models().orientableWithBottom(
                        Objects.requireNonNull(Registration.GooPatternBlock.getId()).getPath() + "2",
                        modLoc("block/goopatterns/goorender_side2"),
                        modLoc("block/goopatterns/goorender_side2"),
                        modLoc("block/goopatterns/goorender_full"),
                        modLoc("block/goopatterns/goorender_blank")
                ).renderType("cutout")))
                .partialState().with(GooPatternBlock.GOOSTAGE, 3).setModels(new ConfiguredModel(models().orientableWithBottom(
                        Objects.requireNonNull(Registration.GooPatternBlock.getId()).getPath() + "3",
                        modLoc("block/goopatterns/goorender_side3"),
                        modLoc("block/goopatterns/goorender_side3"),
                        modLoc("block/goopatterns/goorender_full"),
                        modLoc("block/goopatterns/goorender_blank")
                ).renderType("cutout")))
                .partialState().with(GooPatternBlock.GOOSTAGE, 4).setModels(new ConfiguredModel(models().orientableWithBottom(
                        Objects.requireNonNull(Registration.GooPatternBlock.getId()).getPath() + "4",
                        modLoc("block/goopatterns/goorender_side4"),
                        modLoc("block/goopatterns/goorender_side4"),
                        modLoc("block/goopatterns/goorender_full"),
                        modLoc("block/goopatterns/goorender_blank")
                ).renderType("cutout")))
                .partialState().with(GooPatternBlock.GOOSTAGE, 5).setModels(new ConfiguredModel(models().orientableWithBottom(
                        Objects.requireNonNull(Registration.GooPatternBlock.getId()).getPath() + "5",
                        modLoc("block/goopatterns/goorender_side5"),
                        modLoc("block/goopatterns/goorender_side5"),
                        modLoc("block/goopatterns/goorender_full"),
                        modLoc("block/goopatterns/goorender_blank")
                ).renderType("cutout")))
                .partialState().with(GooPatternBlock.GOOSTAGE, 6).setModels(new ConfiguredModel(models().orientableWithBottom(
                        Objects.requireNonNull(Registration.GooPatternBlock.getId()).getPath() + "6",
                        modLoc("block/goopatterns/goorender_side6"),
                        modLoc("block/goopatterns/goorender_side6"),
                        modLoc("block/goopatterns/goorender_full"),
                        modLoc("block/goopatterns/goorender_blank")
                ).renderType("cutout")))
                .partialState().with(GooPatternBlock.GOOSTAGE, 7).setModels(new ConfiguredModel(models().orientableWithBottom(
                        Objects.requireNonNull(Registration.GooPatternBlock.getId()).getPath() + "7",
                        modLoc("block/goopatterns/goorender_side7"),
                        modLoc("block/goopatterns/goorender_side7"),
                        modLoc("block/goopatterns/goorender_full"),
                        modLoc("block/goopatterns/goorender_blank")
                ).renderType("cutout")))
                .partialState().with(GooPatternBlock.GOOSTAGE, 8).setModels(new ConfiguredModel(models().orientableWithBottom(
                        Objects.requireNonNull(Registration.GooPatternBlock.getId()).getPath() + "8",
                        modLoc("block/goopatterns/goorender_side8"),
                        modLoc("block/goopatterns/goorender_side8"),
                        modLoc("block/goopatterns/goorender_full"),
                        modLoc("block/goopatterns/goorender_blank")
                ).renderType("cutout")))
                .partialState().with(GooPatternBlock.GOOSTAGE, 9).setModels(new ConfiguredModel(models().orientableWithBottom(
                        Objects.requireNonNull(Registration.GooPatternBlock.getId()).getPath() + "9",
                        modLoc("block/goopatterns/goorender_full"),
                        modLoc("block/goopatterns/goorender_full"),
                        modLoc("block/goopatterns/goorender_full"),
                        modLoc("block/goopatterns/goopatterblock_top")
                ).renderType("cutout")))
                .partialState().with(GooPatternBlock.GOOSTAGE, 10).setModels(new ConfiguredModel(models().orientableWithBottom(
                        Objects.requireNonNull(Registration.GooPatternBlock.getId()).getPath() + "10",
                        modLoc("block/goopatterns/goorender_full"),
                        modLoc("block/goopatterns/goorender_full"),
                        modLoc("block/goopatterns/goorender_full"),
                        modLoc("block/goopatterns/goorender_full")
                ).renderType("cutout")))
                .partialState().with(GooPatternBlock.GOOSTAGE, 11).setModels(new ConfiguredModel(models().orientableWithBottom(
                        Objects.requireNonNull(Registration.GooPatternBlock.getId()).getPath() + "11",
                        modLoc("block/goopatterns/goorender_full"),
                        modLoc("block/goopatterns/goorender_full"),
                        modLoc("block/goopatterns/goorender_full"),
                        modLoc("block/goopatterns/goorender_full")
                ).renderType("cutout")))
        ;
    /*directionalBlock(Registration.GooPatternBlock.get(), models().orientableWithBottom(
                Objects.requireNonNull(Registration.GooPatternBlock.getId()).getPath(),
                modLoc("block/goopatterblock_side"),
                modLoc("block/goopatterblock_side"),
                modLoc("block/goopatterblock_bottom"),
                modLoc("block/goopatterblock_top")
        ).renderType("cutout")));*/
    }

}
