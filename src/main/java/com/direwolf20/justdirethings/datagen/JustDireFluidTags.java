package com.direwolf20.justdirethings.datagen;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class JustDireFluidTags extends FluidTagsProvider {

    public static final TagKey<Fluid> EXPERIENCE = forgeTag("experience");

    public JustDireFluidTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, JustDireThings.MODID, existingFileHelper);
    }

    private static TagKey<Fluid> forgeTag(String name) {
        return FluidTags.create(ResourceLocation.fromNamespaceAndPath("c", name));
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(EXPERIENCE)
                .add(Registration.XP_FLUID_SOURCE.get())
                .add(Registration.XP_FLUID_FLOWING.get());
    }

    @Override
    public String getName() {
        return "JustDireThings FluidTags";
    }
}
