package com.direwolf20.justdirethings.datagen;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.common.blocks.gooblocks.GooPatternBlock;
import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
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
        simpleBlock(Registration.GooBlock_Tier3.get(), models().cubeAll(Registration.GooBlock_Tier3_ITEM.getId().getPath(), blockTexture(Registration.GooBlock_Tier3.get())).renderType("cutout"));
        simpleBlock(Registration.GooBlock_Tier4.get(), models().cubeAll(Registration.GooBlock_Tier4_ITEM.getId().getPath(), blockTexture(Registration.GooBlock_Tier4.get())).renderType("cutout"));
        simpleBlock(Registration.FerricoreBlock.get(), models().cubeAll(Registration.FerricoreBlock_ITEM.getId().getPath(), blockTexture(Registration.FerricoreBlock.get())));
        simpleBlock(Registration.RawFerricoreOre.get(), models().cubeAll(Registration.RawFerricoreOre_ITEM.getId().getPath(), blockTexture(Registration.RawFerricoreOre.get())).renderType("translucent"));
        simpleBlock(Registration.BlazeGoldBlock.get(), models().cubeAll(Registration.BlazeGoldBlock_ITEM.getId().getPath(), blockTexture(Registration.BlazeGoldBlock.get())));
        simpleBlock(Registration.RawBlazegoldOre.get(), models().cubeAll(Registration.RawBlazegoldOre_ITEM.getId().getPath(), blockTexture(Registration.RawBlazegoldOre.get())).renderType("translucent"));
        simpleBlock(Registration.CelestigemBlock.get(), models().cubeAll(Registration.CelestigemBlock_ITEM.getId().getPath(), blockTexture(Registration.CelestigemBlock.get())));
        simpleBlock(Registration.RawCelestigemOre.get(), models().cubeAll(Registration.RawCelestigemOre_ITEM.getId().getPath(), blockTexture(Registration.RawCelestigemOre.get())).renderType("translucent"));
        simpleBlock(Registration.EclipseAlloyBlock.get(), models().cubeAll(Registration.EclipseAlloyBlock_ITEM.getId().getPath(), blockTexture(Registration.EclipseAlloyBlock.get())));
        simpleBlock(Registration.RawEclipseAlloyOre.get(), models().cubeAll(Registration.RawEclipseAlloyOre_ITEM.getId().getPath(), blockTexture(Registration.RawEclipseAlloyOre.get())).renderType("translucent"));

        patternBlock();
        soilBlocks();
    }

    private void soilBlocks() {
        getVariantBuilder(Registration.GooSoil_Tier1.get()).forAllStates(s -> {
            ModelFile model;
            int Moisture = s.getValue(BlockStateProperties.MOISTURE);
            if (Moisture == 7) { //Moist
                model = models().withExistingParent(Registration.GooSoil_Tier1.getId().getPath() + "_moist", new ResourceLocation("minecraft:block/template_farmland"))
                        .texture("dirt", modLoc("block/goosoilside_tier1"))
                        .texture("top", modLoc("block/goofarmland_tier1_moist"));
            } else {
                model = models().withExistingParent(Registration.GooSoil_Tier1.getId().getPath(), new ResourceLocation("minecraft:block/template_farmland"))
                        .texture("dirt", modLoc("block/goosoilside_tier1"))
                        .texture("top", modLoc("block/goofarmland_tier1"));
            }
            return ConfiguredModel.builder()
                    .modelFile(model).build();
        });

        getVariantBuilder(Registration.GooSoil_Tier2.get()).forAllStates(s -> {
            ModelFile model;
            int Moisture = s.getValue(BlockStateProperties.MOISTURE);
            if (Moisture == 7) { //Moist
                model = models().withExistingParent(Registration.GooSoil_Tier2.getId().getPath() + "_moist", new ResourceLocation("minecraft:block/template_farmland"))
                        .texture("dirt", modLoc("block/goosoilside_tier2"))
                        .texture("top", modLoc("block/goofarmland_tier2_moist"));
            } else {
                model = models().withExistingParent(Registration.GooSoil_Tier2.getId().getPath(), new ResourceLocation("minecraft:block/template_farmland"))
                        .texture("dirt", modLoc("block/goosoilside_tier2"))
                        .texture("top", modLoc("block/goofarmland_tier2"));
            }
            return ConfiguredModel.builder()
                    .modelFile(model).build();
        });

        getVariantBuilder(Registration.GooSoil_Tier3.get()).forAllStates(s -> {
            ModelFile model;
            int Moisture = s.getValue(BlockStateProperties.MOISTURE);
            if (Moisture == 7) { //Moist
                model = models().withExistingParent(Registration.GooSoil_Tier3.getId().getPath() + "_moist", new ResourceLocation("minecraft:block/template_farmland"))
                        .texture("dirt", modLoc("block/goosoilside_tier3"))
                        .texture("top", modLoc("block/goofarmland_tier3_moist"));
            } else {
                model = models().withExistingParent(Registration.GooSoil_Tier3.getId().getPath(), new ResourceLocation("minecraft:block/template_farmland"))
                        .texture("dirt", modLoc("block/goosoilside_tier3"))
                        .texture("top", modLoc("block/goofarmland_tier3"));
            }
            return ConfiguredModel.builder()
                    .modelFile(model).build();
        });
        getVariantBuilder(Registration.GooSoil_Tier4.get()).forAllStates(s -> {
            ModelFile model;
            int Moisture = s.getValue(BlockStateProperties.MOISTURE);
            if (Moisture == 7) { //Moist
                model = models().withExistingParent(Registration.GooSoil_Tier4.getId().getPath() + "_moist", new ResourceLocation("minecraft:block/template_farmland"))
                        .texture("dirt", modLoc("block/goosoilside_tier4"))
                        .texture("top", modLoc("block/goofarmland_tier4_moist"));
            } else {
                model = models().withExistingParent(Registration.GooSoil_Tier4.getId().getPath(), new ResourceLocation("minecraft:block/template_farmland"))
                        .texture("dirt", modLoc("block/goosoilside_tier4"))
                        .texture("top", modLoc("block/goofarmland_tier4"));
            }
            return ConfiguredModel.builder()
                    .modelFile(model).build();
        });
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
