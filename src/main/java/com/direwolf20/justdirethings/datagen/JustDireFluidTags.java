package com.direwolf20.justdirethings.datagen;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.setup.JDTRegistration;
import com.direwolf20.justdirethings.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;

import java.util.concurrent.CompletableFuture;

public class JustDireFluidTags extends FluidTagsProvider {

    public JustDireFluidTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, JustDireThings.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(ModTags.Fluids.EXPERIENCE)
                .add(JDTRegistration.XP_FLUID_SOURCE.get())
                .add(JDTRegistration.XP_FLUID_FLOWING.get());
    }
}
