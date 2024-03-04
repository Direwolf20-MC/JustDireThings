package com.direwolf20.justdirethings.client.particles.itemparticle;

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

public class ItemFlowParticleData implements ParticleOptions {
    private final ItemStack itemStack;
    public final boolean doGravity;
    public final boolean shrinking;

    public ItemFlowParticleData(ItemStack itemStack, boolean doGravity, boolean shrinking) {
        this.itemStack = itemStack.copy(); //Forge: Fix stack updating after the fact causing particle changes.
        this.doGravity = doGravity;
        this.shrinking = shrinking;
    }

    @Nonnull
    @Override
    public ParticleType<ItemFlowParticleData> getType() {
        return ModParticles.ITEMFLOWPARTICLE.get();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {
        buffer.writeItem(this.itemStack);
    }

    @Nonnull
    @Override
    public String writeToString() {
        return String.format(Locale.ROOT, "%s %b %b",
                this.getType(), this.doGravity, this.shrinking);
    }

    @OnlyIn(Dist.CLIENT)
    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public static final Deserializer<ItemFlowParticleData> DESERIALIZER = new Deserializer<ItemFlowParticleData>() {
        @Nonnull
        @Override
        public ItemFlowParticleData fromCommand(ParticleType<ItemFlowParticleData> particleTypeIn, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            ItemParser.ItemResult itemparser$itemresult = ItemParser.parseForItem(BuiltInRegistries.ITEM.asLookup(), reader);
            ItemStack itemstack = (new ItemInput(itemparser$itemresult.item(), itemparser$itemresult.nbt())).createItemStack(1, false);

            reader.expect(' ');
            boolean doGravity = reader.readBoolean();
            reader.expect(' ');
            boolean building = reader.readBoolean();

            return new ItemFlowParticleData(itemstack, doGravity, building);
        }

        @Override
        public ItemFlowParticleData fromNetwork(ParticleType<ItemFlowParticleData> particleTypeIn, FriendlyByteBuf buffer) {
            return new ItemFlowParticleData(buffer.readItem(), buffer.readBoolean(), buffer.readBoolean());
        }
    };
}

