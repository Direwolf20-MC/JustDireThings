package com.direwolf20.justdirethings.datagen;

import com.direwolf20.justdirethings.common.blocks.BlockBreakerT1;
import com.direwolf20.justdirethings.common.blocks.gooblocks.GooBlock_Base;
import com.direwolf20.justdirethings.common.blocks.gooblocks.GooPatternBlock;
import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.ModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.Objects;

import static com.direwolf20.justdirethings.JustDireThings.MODID;


public class JustDireBlockStates extends BlockStateProvider {
    public JustDireBlockStates(PackOutput output, ExistingFileHelper helper) {
        super(output, MODID, helper);
    }

    @Override
    protected void registerStatesAndModels() {
        gooBlocks(Registration.GooBlock_Tier1);
        gooBlocks(Registration.GooBlock_Tier2);
        gooBlocks(Registration.GooBlock_Tier3);
        gooBlocks(Registration.GooBlock_Tier4);
        simpleBlock(Registration.FerricoreBlock.get(), models().cubeAll(Registration.FerricoreBlock_ITEM.getId().getPath(), blockTexture(Registration.FerricoreBlock.get())));
        simpleBlock(Registration.BlazeGoldBlock.get(), models().cubeAll(Registration.BlazeGoldBlock_ITEM.getId().getPath(), blockTexture(Registration.BlazeGoldBlock.get())));
        simpleBlock(Registration.CelestigemBlock.get(), models().cubeAll(Registration.CelestigemBlock_ITEM.getId().getPath(), blockTexture(Registration.CelestigemBlock.get())));
        simpleBlock(Registration.EclipseAlloyBlock.get(), models().cubeAll(Registration.EclipseAlloyBlock_ITEM.getId().getPath(), blockTexture(Registration.EclipseAlloyBlock.get())));
        simpleBlock(Registration.CoalBlock_T1.get(), models().cubeAll(Registration.CoalBlock_T1.getId().getPath(), blockTexture(Registration.CoalBlock_T1.get())));
        simpleBlock(Registration.CoalBlock_T2.get(), models().cubeAll(Registration.CoalBlock_T2.getId().getPath(), blockTexture(Registration.CoalBlock_T2.get())));
        simpleBlock(Registration.CoalBlock_T3.get(), models().cubeAll(Registration.CoalBlock_T3.getId().getPath(), blockTexture(Registration.CoalBlock_T3.get())));
        simpleBlock(Registration.CoalBlock_T4.get(), models().cubeAll(Registration.CoalBlock_T4.getId().getPath(), blockTexture(Registration.CoalBlock_T4.get())));
        simpleBlock(Registration.PlayerAccessor.get(), models().cubeAll(Registration.PlayerAccessor.getId().getPath(), blockTexture(Registration.PlayerAccessor.get())));
        simpleBlock(Registration.EclipseGateBlock.get(), models().cubeAll(Registration.EclipseGateBlock.getId().getPath(), blockTexture(Registration.EclipseGateBlock.get())).renderType("cutout"));

        patternBlock();
        soilBlocks();
        sidedBlocks();
        //sidedNonRotating();
        fluidBlocks();
    }

    private void gooBlocks(DeferredHolder<Block, ? extends GooBlock_Base> block) {
        getVariantBuilder(block.get()).forAllStates(state -> {
            boolean alive = state.getValue(GooBlock_Base.ALIVE);
            String texturePath = alive ? block.getId().getPath() : block.getId().getPath() + "_dead";
            return ConfiguredModel.builder()
                    .modelFile(models().cubeAll(texturePath, modLoc("block/" + texturePath)))
                    .build();
        });
    }

    private void fluidBlocks() {
        for (var fluidBlock : Registration.FLUID_BLOCKS.getEntries()) {
            simpleBlock(fluidBlock.get(), models().getBuilder(fluidBlock.getId().getPath()).texture("particle", ResourceLocation.fromNamespaceAndPath(MODID, ModelProvider.BLOCK_FOLDER + "/" + "fluid_source")));
        }
    }

    private void sidedNonRotating() {
        simpleBlock(Registration.BlockBreakerT2.get(), models().cubeBottomTop(
                Registration.BlockBreakerT2.getId().getPath(),
                modLoc("block/" + Registration.BlockBreakerT2.getId().getPath() + "_side"),
                modLoc("block/" + Registration.BlockBreakerT2.getId().getPath() + "_bottom"),
                modLoc("block/" + Registration.BlockBreakerT2.getId().getPath() + "_top")));
        simpleBlock(Registration.BlockPlacerT2.get(), models().cubeBottomTop(
                Registration.BlockPlacerT2.getId().getPath(),
                modLoc("block/" + Registration.BlockPlacerT2.getId().getPath() + "_side"),
                modLoc("block/" + Registration.BlockPlacerT2.getId().getPath() + "_bottom"),
                modLoc("block/" + Registration.BlockPlacerT2.getId().getPath() + "_top")));
        simpleBlock(Registration.ClickerT2.get(), models().cubeBottomTop(
                Registration.ClickerT2.getId().getPath(),
                modLoc("block/" + Registration.ClickerT2.getId().getPath() + "_side"),
                modLoc("block/" + Registration.ClickerT2.getId().getPath() + "_bottom"),
                modLoc("block/" + Registration.ClickerT2.getId().getPath() + "_top")));
        simpleBlock(Registration.SensorT2.get(), models().cubeBottomTop(
                Registration.SensorT2.getId().getPath(),
                modLoc("block/" + Registration.SensorT2.getId().getPath() + "_side"),
                modLoc("block/" + Registration.SensorT2.getId().getPath() + "_bottom"),
                modLoc("block/" + Registration.SensorT2.getId().getPath() + "_top")));
        simpleBlock(Registration.DropperT2.get(), models().cubeBottomTop(
                Registration.DropperT2.getId().getPath(),
                modLoc("block/" + Registration.DropperT2.getId().getPath() + "_side"),
                modLoc("block/" + Registration.DropperT2.getId().getPath() + "_bottom"),
                modLoc("block/" + Registration.DropperT2.getId().getPath() + "_top")));
        simpleBlock(Registration.BlockSwapperT2.get(), models().cubeBottomTop(
                Registration.BlockSwapperT2.getId().getPath(),
                modLoc("block/" + Registration.BlockSwapperT2.getId().getPath() + "_side"),
                modLoc("block/" + Registration.BlockSwapperT2.getId().getPath() + "_bottom"),
                modLoc("block/" + Registration.BlockSwapperT2.getId().getPath() + "_top")));
        simpleBlock(Registration.FluidPlacerT2.get(), models().cubeBottomTop(
                Registration.FluidPlacerT2.getId().getPath(),
                modLoc("block/" + Registration.FluidPlacerT2.getId().getPath() + "_side"),
                modLoc("block/" + Registration.FluidPlacerT2.getId().getPath() + "_bottom"),
                modLoc("block/" + Registration.FluidPlacerT2.getId().getPath() + "_top")));
        simpleBlock(Registration.FluidCollectorT2.get(), models().cubeBottomTop(
                Registration.FluidCollectorT2.getId().getPath(),
                modLoc("block/" + Registration.FluidCollectorT2.getId().getPath() + "_side"),
                modLoc("block/" + Registration.FluidCollectorT2.getId().getPath() + "_bottom"),
                modLoc("block/" + Registration.FluidCollectorT2.getId().getPath() + "_top")));
    }

    private void sidedBlocks() {
        for (var sidedBlock : Registration.SIDEDBLOCKS.getEntries()) {
            if (sidedBlock.equals(Registration.BlockBreakerT1)) {
                getVariantBuilder(Registration.BlockBreakerT1.get()).forAllStates(s -> {
                    ModelFile model;
                    boolean active = s.getValue(BlockBreakerT1.ACTIVE);
                    Direction dir = s.getValue(BlockStateProperties.FACING);
                    if (active) { //Active
                        model = models().orientableWithBottom(
                                Objects.requireNonNull(sidedBlock.getId()).getPath() + "_active",
                                modLoc("block/" + sidedBlock.getId().getPath() + "_side"),
                                modLoc("block/" + sidedBlock.getId().getPath() + "_side"),
                                modLoc("block/" + sidedBlock.getId().getPath() + "_bottom"),
                                modLoc("block/" + sidedBlock.getId().getPath() + "_top_active")
                        ).renderType("solid");
                    } else {
                        model = models().orientableWithBottom(
                                Objects.requireNonNull(sidedBlock.getId()).getPath(),
                                modLoc("block/" + sidedBlock.getId().getPath() + "_side"),
                                modLoc("block/" + sidedBlock.getId().getPath() + "_side"),
                                modLoc("block/" + sidedBlock.getId().getPath() + "_bottom"),
                                modLoc("block/" + sidedBlock.getId().getPath() + "_top")
                        ).renderType("solid");
                    }
                    return ConfiguredModel.builder()
                            .modelFile(model)
                            .rotationX(dir == Direction.DOWN ? 180 : dir.getAxis().isHorizontal() ? 90 : 0)
                            .rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + 180) % 360)
                            .build();
                });
            } else {
                ModelFile model = models().orientableWithBottom(
                        Objects.requireNonNull(sidedBlock.getId()).getPath(),
                        modLoc("block/" + sidedBlock.getId().getPath() + "_side"),
                        modLoc("block/" + sidedBlock.getId().getPath() + "_side"),
                        modLoc("block/" + sidedBlock.getId().getPath() + "_bottom"),
                        modLoc("block/" + sidedBlock.getId().getPath() + "_top")
                ).renderType("solid");

                directionalBlock(sidedBlock.get(), model);
            }
        }
    }

    private void soilBlocks() {
        getVariantBuilder(Registration.GooSoil_Tier1.get()).forAllStates(s -> {
            ModelFile model;
            int Moisture = s.getValue(BlockStateProperties.MOISTURE);
            if (Moisture == 7) { //Moist
                model = models().withExistingParent(Registration.GooSoil_Tier1.getId().getPath() + "_moist", ResourceLocation.parse("minecraft:block/template_farmland"))
                        .texture("dirt", modLoc("block/goosoilside_tier1"))
                        .texture("top", modLoc("block/goofarmland_tier1_moist"));
            } else {
                model = models().withExistingParent(Registration.GooSoil_Tier1.getId().getPath(), ResourceLocation.parse("minecraft:block/template_farmland"))
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
                model = models().withExistingParent(Registration.GooSoil_Tier2.getId().getPath() + "_moist", ResourceLocation.parse("minecraft:block/template_farmland"))
                        .texture("dirt", modLoc("block/goosoilside_tier2"))
                        .texture("top", modLoc("block/goofarmland_tier2_moist"));
            } else {
                model = models().withExistingParent(Registration.GooSoil_Tier2.getId().getPath(), ResourceLocation.parse("minecraft:block/template_farmland"))
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
                model = models().withExistingParent(Registration.GooSoil_Tier3.getId().getPath() + "_moist", ResourceLocation.parse("minecraft:block/template_farmland"))
                        .texture("dirt", modLoc("block/goosoilside_tier3"))
                        .texture("top", modLoc("block/goofarmland_tier3_moist"));
            } else {
                model = models().withExistingParent(Registration.GooSoil_Tier3.getId().getPath(), ResourceLocation.parse("minecraft:block/template_farmland"))
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
                model = models().withExistingParent(Registration.GooSoil_Tier4.getId().getPath() + "_moist", ResourceLocation.parse("minecraft:block/template_farmland"))
                        .texture("dirt", modLoc("block/goosoilside_tier4"))
                        .texture("top", modLoc("block/goofarmland_tier4_moist"));
            } else {
                model = models().withExistingParent(Registration.GooSoil_Tier4.getId().getPath(), ResourceLocation.parse("minecraft:block/template_farmland"))
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
