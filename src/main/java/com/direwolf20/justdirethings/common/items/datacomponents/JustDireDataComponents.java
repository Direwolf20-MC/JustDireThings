package com.direwolf20.justdirethings.common.items.datacomponents;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.common.containers.handlers.JustDireItemContainerContents;
import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.common.items.interfaces.ToolRecords;
import com.direwolf20.justdirethings.util.NBTHelpers;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JustDireDataComponents {
    public static final DeferredRegister<DataComponentType<?>> COMPONENTS = DeferredRegister.createDataComponents(JustDireThings.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> ENTITIYTYPE = COMPONENTS.register("entitytype", () -> DataComponentType.<String>builder().persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8).build());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> FLOATINGTICKS = COMPONENTS.register("floatingticks", () -> DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT).build());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<BlockPos>> LAVAREPAIR_LAVAPOS = COMPONENTS.register("lavapos", () -> DataComponentType.<BlockPos>builder().persistent(BlockPos.CODEC).networkSynchronized(BlockPos.STREAM_CODEC).build());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<ToolRecords.AbilityCooldown>>> ABILITY_COOLDOWNS = COMPONENTS.register("ability_cooldowns", () -> DataComponentType.<List<ToolRecords.AbilityCooldown>>builder().persistent(ToolRecords.AbilityCooldown.LIST_CODEC).networkSynchronized(ToolRecords.AbilityCooldown.STREAM_CODEC.apply(ByteBufCodecs.list())).build());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<NBTHelpers.BoundInventory>> BOUND_INVENTORY = COMPONENTS.register("bound_inventory", () -> DataComponentType.<NBTHelpers.BoundInventory>builder().persistent(NBTHelpers.BoundInventory.CODEC).networkSynchronized(NBTHelpers.BoundInventory.STREAM_CODEC).build());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> TOOL_ENABLED = COMPONENTS.register("tool_enabled", () -> DataComponentType.<Boolean>builder().persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL).build());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<NBTHelpers.GlobalVec3>> BOUND_GLOBAL_VEC3 = COMPONENTS.register("bound_global_vec3", () -> DataComponentType.<NBTHelpers.GlobalVec3>builder().persistent(NBTHelpers.GlobalVec3.CODEC).networkSynchronized(NBTHelpers.GlobalVec3.STREAM_CODEC).build());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<GlobalPos>> BOUND_GLOBAL_POS = COMPONENTS.register("bound_global_pos", () -> DataComponentType.<GlobalPos>builder().persistent(GlobalPos.CODEC).networkSynchronized(GlobalPos.STREAM_CODEC).build());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<String>>> LEFT_CLICK_ABILITIES = COMPONENTS.register("left_click_abilities", () -> DataComponentType.<List<String>>builder().persistent(Codec.STRING.listOf()).networkSynchronized(ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list())).build());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<ToolRecords.AbilityBinding>>> ABILITY_BINDINGS = COMPONENTS.register("ability_bindings", () -> DataComponentType.<List<ToolRecords.AbilityBinding>>builder().persistent(ToolRecords.AbilityBinding.LIST_CODEC).networkSynchronized(ToolRecords.AbilityBinding.STREAM_CODEC.apply(ByteBufCodecs.list())).build());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> POCKETGEN_COUNTER = COMPONENTS.register("pocketgen_counter", () -> DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT).build());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> POCKETGEN_FUELMULT = COMPONENTS.register("pocketgen_fuelmult", () -> DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT).build());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> POCKETGEN_MAXBURN = COMPONENTS.register("pocketgen_maxburn", () -> DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT).build());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> FUELCANISTER_FUELLEVEL = COMPONENTS.register("fuelcanister_fuellevel", () -> DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT).build());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Double>> FUELCANISTER_BURNSPEED = COMPONENTS.register("fuelcanister_burnspeed", () -> DataComponentType.<Double>builder().persistent(Codec.DOUBLE).networkSynchronized(ByteBufCodecs.DOUBLE).build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> FORGE_ENERGY = COMPONENTS.register("forge_energy", () -> DataComponentType.<Integer>builder().persistent(Codec.INT.orElse(0)).networkSynchronized(ByteBufCodecs.VAR_INT).build());


    public static final DeferredHolder<DataComponentType<?>, DataComponentType<JustDireItemContainerContents>> ITEMSTACK_HANDLER = COMPONENTS.register("itemstack_handler", () -> DataComponentType.<JustDireItemContainerContents>builder().persistent(JustDireItemContainerContents.CODEC).networkSynchronized(JustDireItemContainerContents.STREAM_CODEC).cacheEncoding().build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CustomData>> CUSTOM_DATA_1 = COMPONENTS.register("custom_data_1", () -> DataComponentType.<CustomData>builder().persistent(CustomData.CODEC).networkSynchronized(CustomData.STREAM_CODEC).build());

    public static final Map<Ability, DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>>> ABILITY_TOGGLES = new HashMap<>();
    public static final Map<Ability, DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>>> ABILITY_RENDER_TOGGLES = new HashMap<>();
    public static final Map<Ability, DeferredHolder<DataComponentType<?>, DataComponentType<Integer>>> ABILITY_VALUES = new HashMap<>();
    public static final Map<Ability, DeferredHolder<DataComponentType<?>, DataComponentType<Integer>>> ABILITY_BINDING_MODES = new HashMap<>();

    public static void genAbilityData() {
        for (Ability ability : Ability.values()) {
            DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> ABILITY_TOGGLE = COMPONENTS.register(ability.getName() + "_toggle", () -> DataComponentType.<Boolean>builder().persistent(Codec.BOOL.orElse(true)).networkSynchronized(ByteBufCodecs.BOOL).build());
            DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> ABILITY_VALUE = COMPONENTS.register(ability.getName() + "_value", () -> DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT).build());
            DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> ABILITY_BINDING_MODE = COMPONENTS.register(ability.getName() + "_bindingmode", () -> DataComponentType.<Integer>builder().persistent(Codec.INT.orElse(0)).networkSynchronized(ByteBufCodecs.VAR_INT).build());
            ABILITY_TOGGLES.put(ability, ABILITY_TOGGLE);
            ABILITY_VALUES.put(ability, ABILITY_VALUE);
            ABILITY_BINDING_MODES.put(ability, ABILITY_BINDING_MODE);
            if (ability.hasRenderButton()) {
                DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> ABILITY_RENDER = COMPONENTS.register(ability.getName() + "_render", () -> DataComponentType.<Boolean>builder().persistent(Codec.BOOL.orElse(true)).networkSynchronized(ByteBufCodecs.BOOL).build());
                ABILITY_RENDER_TOGGLES.put(ability, ABILITY_RENDER);
            }
        }
    }

    private static @NotNull <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name, final Codec<T> codec) {
        return register(name, codec, null);
    }

    private static @NotNull <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name, final Codec<T> codec, @Nullable final StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
        if (streamCodec == null) {
            return COMPONENTS.register(name, () -> DataComponentType.<T>builder().persistent(codec).build());
        } else {
            return COMPONENTS.register(name, () -> DataComponentType.<T>builder().persistent(codec).networkSynchronized(streamCodec).build());
        }
    }
}
