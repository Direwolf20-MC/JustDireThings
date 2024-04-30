package com.direwolf20.justdirethings.common.items.interfaces;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.List;

public class ToolRecords {
    public record AbilityCooldown(String abilityName, int cooldownTicks, boolean isactive) {
        public static final Codec<AbilityCooldown> CODEC = RecordCodecBuilder.create(
                cooldownInstance -> cooldownInstance.group(
                                Codec.STRING.fieldOf("ability_name").forGetter(AbilityCooldown::abilityName),
                                Codec.INT.fieldOf("cooldown_ticks").forGetter(AbilityCooldown::cooldownTicks),
                                Codec.BOOL.fieldOf("is_active").forGetter(AbilityCooldown::isactive)
                        )
                        .apply(cooldownInstance, AbilityCooldown::new)
        );
        public static final Codec<List<AbilityCooldown>> LIST_CODEC = CODEC.listOf();
        public static final StreamCodec<ByteBuf, AbilityCooldown> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8,
                AbilityCooldown::abilityName,
                ByteBufCodecs.VAR_INT,
                AbilityCooldown::cooldownTicks,
                ByteBufCodecs.BOOL,
                AbilityCooldown::isactive,
                AbilityCooldown::new
        );
    }

    public record AbilityBinding(String abilityName, int key, boolean isMouse) {
        public static final Codec<AbilityBinding> CODEC = RecordCodecBuilder.create(
                cooldownInstance -> cooldownInstance.group(
                                Codec.STRING.fieldOf("ability_name").forGetter(AbilityBinding::abilityName),
                                Codec.INT.fieldOf("key").forGetter(AbilityBinding::key),
                                Codec.BOOL.fieldOf("is_mouse").forGetter(AbilityBinding::isMouse)
                        )
                        .apply(cooldownInstance, AbilityBinding::new)
        );
        public static final Codec<List<AbilityBinding>> LIST_CODEC = CODEC.listOf();
        public static final StreamCodec<ByteBuf, AbilityBinding> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8,
                AbilityBinding::abilityName,
                ByteBufCodecs.VAR_INT,
                AbilityBinding::key,
                ByteBufCodecs.BOOL,
                AbilityBinding::isMouse,
                AbilityBinding::new
        );
    }
}
