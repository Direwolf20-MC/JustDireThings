package com.direwolf20.justdirethings.client.particles.gooexplodeparticle;

import com.direwolf20.justdirethings.client.particles.ModParticles;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.arguments.item.ItemInput;
import net.minecraft.commands.arguments.item.ItemParser;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.Locale;

public class GooExplodeParticleData implements ParticleOptions {
    private final ItemStack itemStack;

    public GooExplodeParticleData(ItemStack itemStack) {
        this.itemStack = itemStack.copy(); //Forge: Fix stack updating after the fact causing particle changes.
    }

    @Nonnull
    @Override
    public ParticleType<GooExplodeParticleData> getType() {
        return ModParticles.GOOEXPLODEPARTICLE.get();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {
        buffer.writeItem(this.itemStack);
    }

    @Nonnull
    @Override
    public String writeToString() {
        return String.format(Locale.ROOT, "%s",
                this.getType());
    }

    @OnlyIn(Dist.CLIENT)
    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public static final Deserializer<GooExplodeParticleData> DESERIALIZER = new Deserializer<GooExplodeParticleData>() {
        @Nonnull
        @Override
        public GooExplodeParticleData fromCommand(ParticleType<GooExplodeParticleData> particleTypeIn, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            ItemParser.ItemResult itemparser$itemresult = ItemParser.parseForItem(BuiltInRegistries.ITEM.asLookup(), reader);
            ItemStack itemstack = (new ItemInput(itemparser$itemresult.item(), itemparser$itemresult.nbt())).createItemStack(1, false);


            return new GooExplodeParticleData(itemstack);
        }

        @Override
        public GooExplodeParticleData fromNetwork(ParticleType<GooExplodeParticleData> particleTypeIn, FriendlyByteBuf buffer) {
            return new GooExplodeParticleData(buffer.readItem());
        }
    };
}

