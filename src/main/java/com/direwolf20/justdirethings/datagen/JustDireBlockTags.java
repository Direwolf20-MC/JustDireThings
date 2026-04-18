package com.direwolf20.justdirethings.datagen;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.setup.JDTRegistration;
import com.direwolf20.justdirethings.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;

import java.util.concurrent.CompletableFuture;

public class JustDireBlockTags extends BlockTagsProvider {

    public JustDireBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, JustDireThings.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BlockTags.MINEABLE_WITH_SHOVEL)
                .add(JDTRegistration.GooBlock_Tier1.get())
                .add(JDTRegistration.GooBlock_Tier2.get())
                .add(JDTRegistration.GooBlock_Tier3.get())
                .add(JDTRegistration.GooBlock_Tier4.get())
                .add(JDTRegistration.GooSoil_Tier1.get())
                .add(JDTRegistration.GooSoil_Tier2.get())
                .add(JDTRegistration.GooSoil_Tier3.get())
                .add(JDTRegistration.GooSoil_Tier4.get());
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(JDTRegistration.FerricoreBlock.get())
                .add(JDTRegistration.RawFerricoreOre.get())
                .add(JDTRegistration.BlazeGoldBlock.get())
                .add(JDTRegistration.RawBlazegoldOre.get())
                .add(JDTRegistration.CelestigemBlock.get())
                .add(JDTRegistration.RawCelestigemOre.get())
                .add(JDTRegistration.EclipseAlloyBlock.get())
                .add(JDTRegistration.RawEclipseAlloyOre.get())
                .add(JDTRegistration.ItemCollector.get())
                .add(JDTRegistration.BlockBreakerT1.get())
                .add(JDTRegistration.BlockBreakerT2.get())
                .add(JDTRegistration.BlockPlacerT1.get())
                .add(JDTRegistration.BlockPlacerT2.get())
                .add(JDTRegistration.ClickerT1.get())
                .add(JDTRegistration.ClickerT2.get())
                .add(JDTRegistration.SensorT1.get())
                .add(JDTRegistration.SensorT2.get())
                .add(JDTRegistration.DropperT1.get())
                .add(JDTRegistration.DropperT2.get())
                .add(JDTRegistration.GeneratorT1.get())
                .add(JDTRegistration.GeneratorFluidT1.get())
                .add(JDTRegistration.EnergyTransmitter.get())
                .add(JDTRegistration.RawCoal_T1.get())
                .add(JDTRegistration.RawCoal_T2.get())
                .add(JDTRegistration.RawCoal_T3.get())
                .add(JDTRegistration.RawCoal_T4.get())
                .add(JDTRegistration.CoalBlock_T1.get())
                .add(JDTRegistration.CoalBlock_T2.get())
                .add(JDTRegistration.CoalBlock_T3.get())
                .add(JDTRegistration.CoalBlock_T4.get())
                .add(JDTRegistration.BlockSwapperT1.get())
                .add(JDTRegistration.BlockSwapperT2.get())
                .add(JDTRegistration.PlayerAccessor.get())
                .add(JDTRegistration.FluidPlacerT1.get())
                .add(JDTRegistration.FluidPlacerT2.get())
                .add(JDTRegistration.FluidCollectorT1.get())
                .add(JDTRegistration.FluidCollectorT2.get())
                .add(JDTRegistration.TimeCrystalCluster.get())
                .add(JDTRegistration.TimeCrystalBlock.get())
                .add(JDTRegistration.ParadoxMachine.get())
                .add(JDTRegistration.InventoryHolder.get())
                .add(JDTRegistration.ExperienceHolder.get())
                .add(JDTRegistration.CharcoalBlock.get());
        tag(ModTags.Blocks.LAWNMOWERABLE)
                .addTag(BlockTags.FLOWERS)
                .add(Blocks.TALL_GRASS)
                .add(Blocks.SHORT_GRASS)
                .add(Blocks.DEAD_BUSH)
                .add(Blocks.SWEET_BERRY_BUSH)
                .add(Blocks.FERN)
                .add(Blocks.LARGE_FERN);
        tag(Tags.Blocks.ORES)
                .add(JDTRegistration.RawFerricoreOre.get())
                .add(JDTRegistration.RawBlazegoldOre.get())
                .add(JDTRegistration.RawCelestigemOre.get())
                .add(JDTRegistration.RawEclipseAlloyOre.get())
                .add(JDTRegistration.RawCoal_T1.get())
                .add(JDTRegistration.RawCoal_T2.get())
                .add(JDTRegistration.RawCoal_T3.get())
                .add(JDTRegistration.RawCoal_T4.get());
        tag(Tags.Blocks.STORAGE_BLOCKS)
                .add(JDTRegistration.FerricoreBlock.get())
                .add(JDTRegistration.BlazeGoldBlock.get())
                .add(JDTRegistration.CelestigemBlock.get())
                .add(JDTRegistration.EclipseAlloyBlock.get())
                .add(JDTRegistration.CoalBlock_T1.get())
                .add(JDTRegistration.CoalBlock_T2.get())
                .add(JDTRegistration.CoalBlock_T3.get())
                .add(JDTRegistration.CoalBlock_T4.get());
        tag(BlockTags.SUPPORTS_BAMBOO)
                .add(JDTRegistration.GooSoil_Tier1.get())
                .add(JDTRegistration.GooSoil_Tier2.get())
                .add(JDTRegistration.GooSoil_Tier3.get())
                .add(JDTRegistration.GooSoil_Tier4.get());
        tag(ModTags.Blocks.NO_AUTO_CLICK);
        tag(ModTags.Blocks.SWAPPERDENY)
                .add(Blocks.PISTON_HEAD)
                .add(Blocks.MOVING_PISTON)
                .add(Blocks.BEDROCK)
                .add(Blocks.END_PORTAL_FRAME)
                .add(Blocks.CANDLE_CAKE)
                .addTag(BlockTags.BEDS)
                .addTag(BlockTags.PORTALS)
                .addTag(BlockTags.DOORS);
        tag(ModTags.Blocks.ECLISEGATEDENY)
                .addTag(BlockTags.PORTALS)
                .addTag(BlockTags.DOORS)
                .addOptionalTag(TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("powah", "player_transmitters")));
        tag(ModTags.Blocks.PHASEDENY)
                .addTags(BlockTags.PORTALS)
                .add(Blocks.BARRIER,
                        Blocks.BEDROCK,
                        Blocks.END_PORTAL,
                        Blocks.END_PORTAL_FRAME,
                        Blocks.END_GATEWAY,
                        Blocks.STRUCTURE_BLOCK,
                        Blocks.JIGSAW);
        tag(Tags.Blocks.BUDDING_BLOCKS)
                .add(JDTRegistration.TimeCrystalBuddingBlock.get());
        tag(Tags.Blocks.BUDS)
                .add(JDTRegistration.TimeCrystalCluster_Small.get())
                .add(JDTRegistration.TimeCrystalCluster_Medium.get())
                .add(JDTRegistration.TimeCrystalCluster_Large.get());
        tag(Tags.Blocks.CLUSTERS)
                .add(JDTRegistration.TimeCrystalCluster.get());
        tag(ModTags.Blocks.TICK_SPEED_DENY);
        tag(ModTags.Blocks.PARADOX_ALLOW)
                .addTag(Tags.Blocks.ORES);
        tag(ModTags.Blocks.PARADOX_ABSORB_DENY)
                .add(Blocks.BEDROCK);
        tag(ModTags.Blocks.CHARCOAL)
                .add(JDTRegistration.CharcoalBlock.get());
    }
}
