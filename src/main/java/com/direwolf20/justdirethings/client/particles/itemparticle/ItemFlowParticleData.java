package com.direwolf20.justdirethings.client.particles.itemparticle;

import com.direwolf20.justdirethings.client.particles.ModParticles;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class ItemFlowParticleData implements ParticleOptions {
    public static final MapCodec<ItemFlowParticleData> MAP_CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    ItemStack.CODEC.fieldOf("itemStack").forGetter(p -> p.itemStack),
                    Codec.DOUBLE.fieldOf("targetX").forGetter(p -> p.targetX),
                    Codec.DOUBLE.fieldOf("targetY").forGetter(p -> p.targetY),
                    Codec.DOUBLE.fieldOf("targetZ").forGetter(p -> p.targetZ),
                    Codec.INT.fieldOf("ticksPerBlock").forGetter(p -> p.ticksPerBlock)
            ).apply(instance, ItemFlowParticleData::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, ItemFlowParticleData> STREAM_CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC,
            ItemFlowParticleData::getItemStack,
            ByteBufCodecs.DOUBLE,
            ItemFlowParticleData::getTargetX,
            ByteBufCodecs.DOUBLE,
            ItemFlowParticleData::getTargetY,
            ByteBufCodecs.DOUBLE,
            ItemFlowParticleData::getTargetZ,
            ByteBufCodecs.INT,
            ItemFlowParticleData::getTicksPerBlock,
            ItemFlowParticleData::new
    );

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

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public double getTargetX() {
        return targetX;
    }

    public double getTargetY() {
        return targetY;
    }

    public double getTargetZ() {
        return targetZ;
    }

    public int getTicksPerBlock() {
        return ticksPerBlock;
    }
}

