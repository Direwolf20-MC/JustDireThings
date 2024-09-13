package com.direwolf20.justdirethings.common.network.data;

import com.direwolf20.justdirethings.JustDireThings;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ExperienceHolderSettingsPayload(
        int targetExp,
        boolean ownerOnly,
        boolean collectExp
) implements CustomPacketPayload {
    public static final Type<ExperienceHolderSettingsPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "experience_holder_settings"));

    @Override
    public Type<ExperienceHolderSettingsPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<FriendlyByteBuf, ExperienceHolderSettingsPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, ExperienceHolderSettingsPayload::targetExp,
            ByteBufCodecs.BOOL, ExperienceHolderSettingsPayload::ownerOnly,
            ByteBufCodecs.BOOL, ExperienceHolderSettingsPayload::collectExp,
            ExperienceHolderSettingsPayload::new
    );
}
