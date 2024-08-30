package com.direwolf20.justdirethings.client.particles.paradoxparticle;

import com.direwolf20.justdirethings.client.particles.ModParticles;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.UUID;

public class ParadoxParticleData implements ParticleOptions {
    public static final MapCodec<ParadoxParticleData> MAP_CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    ItemStack.CODEC.fieldOf("itemStack").forGetter(p -> p.itemStack),
                    Codec.DOUBLE.fieldOf("targetX").forGetter(p -> p.targetX),
                    Codec.DOUBLE.fieldOf("targetY").forGetter(p -> p.targetY),
                    Codec.DOUBLE.fieldOf("targetZ").forGetter(p -> p.targetZ),
                    Codec.INT.fieldOf("ticksPerBlock").forGetter(p -> p.ticksPerBlock),
                    UUIDUtil.CODEC.fieldOf("paradox_uuid").forGetter(p -> p.paradox_uuid)
            ).apply(instance, ParadoxParticleData::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, ParadoxParticleData> STREAM_CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC,
            ParadoxParticleData::getItemStack,
            ByteBufCodecs.DOUBLE,
            ParadoxParticleData::getTargetX,
            ByteBufCodecs.DOUBLE,
            ParadoxParticleData::getTargetY,
            ByteBufCodecs.DOUBLE,
            ParadoxParticleData::getTargetZ,
            ByteBufCodecs.INT,
            ParadoxParticleData::getTicksPerBlock,
            UUIDUtil.STREAM_CODEC,
            ParadoxParticleData::getParadox_uuid,
            ParadoxParticleData::new
    );

    private final ItemStack itemStack;
    public final double targetX;
    public final double targetY;
    public final double targetZ;
    public final int ticksPerBlock;
    public final UUID paradox_uuid;

    public ParadoxParticleData(ItemStack itemStack, double tx, double ty, double tz, int ticks, UUID paradox_uuid) {
        this.itemStack = itemStack.copy(); //Forge: Fix stack updating after the fact causing particle changes.
        targetX = tx;
        targetY = ty;
        targetZ = tz;
        ticksPerBlock = ticks;
        this.paradox_uuid = paradox_uuid;
    }

    @Nonnull
    @Override
    public ParticleType<ParadoxParticleData> getType() {
        return ModParticles.PARADOXPARTICLE.get();
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

    public UUID getParadox_uuid() {
        return paradox_uuid;
    }
}

