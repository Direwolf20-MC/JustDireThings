package com.direwolf20.justdirethings.client.particles.glitterparticle;

import com.direwolf20.justdirethings.client.particles.ModParticles;
import com.mojang.datafixers.util.Function11;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import javax.annotation.Nonnull;
import java.util.function.Function;

public class GlitterParticleData implements ParticleOptions {
    public static final MapCodec<GlitterParticleData> MAP_CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.STRING.fieldOf("partType").forGetter(p -> p.partType),
                    Codec.DOUBLE.fieldOf("targetX").forGetter(p -> p.targetX),
                    Codec.DOUBLE.fieldOf("targetY").forGetter(p -> p.targetY),
                    Codec.DOUBLE.fieldOf("targetZ").forGetter(p -> p.targetZ),
                    Codec.FLOAT.fieldOf("size").forGetter(p -> p.size),
                    Codec.FLOAT.fieldOf("r").forGetter(p -> p.r),
                    Codec.FLOAT.fieldOf("g").forGetter(p -> p.g),
                    Codec.FLOAT.fieldOf("b").forGetter(p -> p.b),
                    Codec.FLOAT.fieldOf("a").forGetter(p -> p.a),
                    Codec.FLOAT.fieldOf("maxAgeMul").forGetter(p -> p.maxAgeMul),
                    Codec.BOOL.fieldOf("depthTest").forGetter(p -> p.depthTest)
            ).apply(instance, GlitterParticleData::new));
    public static final StreamCodec<FriendlyByteBuf, GlitterParticleData> STREAM_CODEC = composite(
            ByteBufCodecs.STRING_UTF8,
            GlitterParticleData::getPartType,
            ByteBufCodecs.DOUBLE,
            GlitterParticleData::getTargetX,
            ByteBufCodecs.DOUBLE,
            GlitterParticleData::getTargetY,
            ByteBufCodecs.DOUBLE,
            GlitterParticleData::getTargetZ,
            ByteBufCodecs.FLOAT,
            GlitterParticleData::getSize,
            ByteBufCodecs.FLOAT,
            GlitterParticleData::getR,
            ByteBufCodecs.FLOAT,
            GlitterParticleData::getG,
            ByteBufCodecs.FLOAT,
            GlitterParticleData::getB,
            ByteBufCodecs.FLOAT,
            GlitterParticleData::getA,
            ByteBufCodecs.FLOAT,
            GlitterParticleData::getMaxAgeMul,
            ByteBufCodecs.BOOL,
            GlitterParticleData::isDepthTest,
            GlitterParticleData::new
    );

    public static <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> StreamCodec<B, C> composite(
            final StreamCodec<? super B, T1> codec1,
            final Function<C, T1> getter1,
            final StreamCodec<? super B, T2> codec2,
            final Function<C, T2> getter2,
            final StreamCodec<? super B, T3> codec3,
            final Function<C, T3> getter3,
            final StreamCodec<? super B, T4> codec4,
            final Function<C, T4> getter4,
            final StreamCodec<? super B, T5> codec5,
            final Function<C, T5> getter5,
            final StreamCodec<? super B, T6> codec6,
            final Function<C, T6> getter6,
            final StreamCodec<? super B, T7> codec7,
            final Function<C, T7> getter7,
            final StreamCodec<? super B, T8> codec8,
            final Function<C, T8> getter8,
            final StreamCodec<? super B, T9> codec9,
            final Function<C, T9> getter9,
            final StreamCodec<? super B, T10> codec10,
            final Function<C, T10> getter10,
            final StreamCodec<? super B, T11> codec11,
            final Function<C, T11> getter11,
            final Function11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, C> p_331335_) {
        return new StreamCodec<B, C>() {
            @Override
            public C decode(B p_330310_) {
                T1 t1 = codec1.decode(p_330310_);
                T2 t2 = codec2.decode(p_330310_);
                T3 t3 = codec3.decode(p_330310_);
                T4 t4 = codec4.decode(p_330310_);
                T5 t5 = codec5.decode(p_330310_);
                T6 t6 = codec6.decode(p_330310_);
                T7 t7 = codec7.decode(p_330310_);
                T8 t8 = codec8.decode(p_330310_);
                T9 t9 = codec9.decode(p_330310_);
                T10 t10 = codec10.decode(p_330310_);
                T11 t11 = codec11.decode(p_330310_);
                return p_331335_.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11);
            }

            @Override
            public void encode(B p_332052_, C p_331912_) {
                codec1.encode(p_332052_, getter1.apply(p_331912_));
                codec2.encode(p_332052_, getter2.apply(p_331912_));
                codec3.encode(p_332052_, getter3.apply(p_331912_));
                codec4.encode(p_332052_, getter4.apply(p_331912_));
                codec5.encode(p_332052_, getter5.apply(p_331912_));
                codec6.encode(p_332052_, getter6.apply(p_331912_));
                codec7.encode(p_332052_, getter7.apply(p_331912_));
                codec8.encode(p_332052_, getter8.apply(p_331912_));
                codec9.encode(p_332052_, getter9.apply(p_331912_));
                codec10.encode(p_332052_, getter10.apply(p_331912_));
                codec11.encode(p_332052_, getter11.apply(p_331912_));
            }
        };
    }

    public final float size;
    public final float r, g, b, a;
    public final float maxAgeMul;
    public final boolean depthTest;
    public final double targetX;
    public final double targetY;
    public final double targetZ;
    public final String partType;

    public static GlitterParticleData playerparticle(String type, double targetX, double targetY, double targetZ, float size, float r, float g, float b) {
        return playerparticle(type, targetX, targetY, targetZ, size, r, g, b, 1);
    }

    public static GlitterParticleData playerparticle(String type, double targetX, double targetY, double targetZ, float size, float r, float g, float b, float maxAgeMul) {
        return playerparticle(type, targetX, targetY, targetZ, size, r, g, b, 1f, maxAgeMul, true);
    }

    public static GlitterParticleData playerparticle(String type, double targetX, double targetY, double targetZ, float size, float r, float g, float b, boolean depth) {
        return playerparticle(type, targetX, targetY, targetZ, size, r, g, b, 1f, 1, depth);
    }

    public static GlitterParticleData playerparticle(String type, double targetX, double targetY, double targetZ, float size, float r, float g, float b, float a, float maxAgeMul, boolean depthTest) {
        return new GlitterParticleData(type, targetX, targetY, targetZ, size, r, g, b, a, maxAgeMul, depthTest);
    }

    private GlitterParticleData(String type, double targetX, double targetY, double targetZ, float size, float r, float g, float b, float a, float maxAgeMul, boolean depthTest) {
        this.targetX = targetX;
        this.targetY = targetY;
        this.targetZ = targetZ;
        this.size = size;
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        this.maxAgeMul = maxAgeMul;
        this.depthTest = depthTest;
        this.partType = type;
    }


    @Nonnull
    @Override
    public ParticleType<GlitterParticleData> getType() {
        return ModParticles.GLITTERPARTICLE.get();
    }

    public float getSize() {
        return size;
    }

    public float getR() {
        return r;
    }

    public float getG() {
        return g;
    }

    public float getB() {
        return b;
    }

    public float getA() {
        return a;
    }

    public float getMaxAgeMul() {
        return maxAgeMul;
    }

    public boolean isDepthTest() {
        return depthTest;
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

    public String getPartType() {
        return partType;
    }
}
