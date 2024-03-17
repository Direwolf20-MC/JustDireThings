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
    public final double targetX;
    public final double targetY;
    public final double targetZ;
    public final int ticksPerBlock;

    public ItemFlowParticleData(ItemStack itemStack, double tx, double ty, double tz, int ticks) {
        this.itemStack = itemStack.copy(); //Forge: Fix stack updating after the fact causing particle changes.
        targetX = tx;
        targetY = ty;
        targetZ = tz;
        ticksPerBlock = ticks;
    }

    @Nonnull
    @Override
    public ParticleType<ItemFlowParticleData> getType() {
        return ModParticles.ITEMFLOWPARTICLE.get();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {
        buffer.writeItem(this.itemStack);
        buffer.writeDouble(this.targetX);
        buffer.writeDouble(this.targetY);
        buffer.writeDouble(this.targetZ);
        buffer.writeInt(this.ticksPerBlock);
    }

    @Nonnull
    @Override
    public String writeToString() {
        return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f %.2f %s",
                this.getType(), this.targetX, this.targetY, this.targetZ, this.ticksPerBlock);
    }

    /*public String getParameters() {
        return Registry.PARTICLE_TYPE.getKey(this.getType()) + " " + (new ItemInput(this.itemStack.getItem().builtInRegistryHolder(), this.itemStack.getTag())).serialize();
    }*/

    @OnlyIn(Dist.CLIENT)
    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public static final Deserializer<ItemFlowParticleData> DESERIALIZER = new Deserializer<ItemFlowParticleData>() {
        @Nonnull
        @Override
        public ItemFlowParticleData fromCommand(ParticleType<ItemFlowParticleData> particleTypeIn, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            //ItemParser itemparser = (new ItemParser(reader, false)).parse();
            //ItemStack itemstack = (new ItemInput(itemparser.getItem(), itemparser.getNbt())).createItemStack(1, false);
            ItemParser.ItemResult itemparser$itemresult = ItemParser.parseForItem(BuiltInRegistries.ITEM.asLookup(), reader);
            ItemStack itemstack = (new ItemInput(itemparser$itemresult.item(), itemparser$itemresult.nbt())).createItemStack(1, false);

            reader.expect(' ');
            double tx = reader.readDouble();
            reader.expect(' ');
            double ty = reader.readDouble();
            reader.expect(' ');
            double tz = reader.readDouble();
            reader.expect(' ');
            int ticks = reader.readInt();
            return new ItemFlowParticleData(itemstack, tx, ty, tz, ticks);
        }

        @Override
        public ItemFlowParticleData fromNetwork(ParticleType<ItemFlowParticleData> particleTypeIn, FriendlyByteBuf buffer) {
            return new ItemFlowParticleData(buffer.readItem(), buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readInt());
        }
    };
}

