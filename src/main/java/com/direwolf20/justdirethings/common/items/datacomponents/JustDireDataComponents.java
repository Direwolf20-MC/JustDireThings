package com.direwolf20.justdirethings.common.items.datacomponents;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.common.items.interfaces.ToolRecords;
import com.direwolf20.justdirethings.util.NBTHelpers;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> COPY_AREA_SETTINGS = COMPONENTS.register("copy_area_settings", () -> DataComponentType.<Boolean>builder().persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL).build());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> COPY_OFFSET_SETTINGS = COMPONENTS.register("copy_offset_settings", () -> DataComponentType.<Boolean>builder().persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL).build());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> COPY_FILTER_SETTINGS = COMPONENTS.register("copy_filter_settings", () -> DataComponentType.<Boolean>builder().persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL).build());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> COPY_REDSTONE_SETTINGS = COMPONENTS.register("copy_redstone_settings", () -> DataComponentType.<Boolean>builder().persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL).build());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CustomData>> COPIED_MACHINE_DATA = COMPONENTS.register("copied_machine_data", () -> DataComponentType.<CustomData>builder().persistent(CustomData.CODEC).networkSynchronized(CustomData.STREAM_CODEC).build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<UUID>> PORTALGUN_UUID = COMPONENTS.register("portalgun_uuid", () -> DataComponentType.<UUID>builder().persistent(UUIDUtil.CODEC).networkSynchronized(UUIDUtil.STREAM_CODEC).build());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> PORTALGUN_FAVORITE = COMPONENTS.register("portalgun_favorite", () -> DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT).build());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<NBTHelpers.PortalDestination>>> PORTAL_GUN_FAVORITES = COMPONENTS.register("portal_gun_favorites", () -> DataComponentType.<List<NBTHelpers.PortalDestination>>builder().persistent(NBTHelpers.PortalDestination.CODEC.listOf()).networkSynchronized(NBTHelpers.PortalDestination.STREAM_CODEC.apply(ByteBufCodecs.list())).build());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<NBTHelpers.PortalDestination>> PORTAL_GUN_PREVIOUS = COMPONENTS.register("portal_gun_previous", () -> DataComponentType.<NBTHelpers.PortalDestination>builder().persistent(NBTHelpers.PortalDestination.CODEC).networkSynchronized(NBTHelpers.PortalDestination.STREAM_CODEC).build());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> PORTAL_GUN_STAY_OPEN = COMPONENTS.register("portal_gun_stay_open", () -> DataComponentType.<Boolean>builder().persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL).build());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<SimpleFluidContent>> FLUID_CONTAINER = COMPONENTS.register("fluid_container", () -> DataComponentType.<SimpleFluidContent>builder().persistent(SimpleFluidContent.CODEC).networkSynchronized(SimpleFluidContent.STREAM_CODEC).build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> FORGE_ENERGY = COMPONENTS.register("forge_energy", () -> DataComponentType.<Integer>builder().persistent(Codec.INT.orElse(0)).networkSynchronized(ByteBufCodecs.VAR_INT).build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> FLUID_CANISTER_MODE = COMPONENTS.register("fluid_canister_mode", () -> DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT).build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<String>>> STUPEFY_TARGETS = COMPONENTS.register("stupefy_targets", () -> DataComponentType.<List<String>>builder().persistent(Codec.STRING.listOf()).networkSynchronized(ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list())).build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemContainerContents>> ITEMSTACK_HANDLER = COMPONENTS.register("itemstack_handler", () -> DataComponentType.<ItemContainerContents>builder().persistent(ItemContainerContents.CODEC).networkSynchronized(ItemContainerContents.STREAM_CODEC).cacheEncoding().build());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemContainerContents>> TOOL_CONTENTS = COMPONENTS.register("tool_contents", () -> DataComponentType.<ItemContainerContents>builder().persistent(ItemContainerContents.CODEC).networkSynchronized(ItemContainerContents.STREAM_CODEC).cacheEncoding().build());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<PotionContents>> POTION_CONTENTS = COMPONENTS.register("potion_contents", () -> DataComponentType.<PotionContents>builder().persistent(PotionContents.CODEC).networkSynchronized(PotionContents.STREAM_CODEC).cacheEncoding().build());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> POTION_AMOUNT = COMPONENTS.register("potion_amount", () -> DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT).build());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> EPIC_ARROW = COMPONENTS.register("epic_arrow", () -> DataComponentType.<Boolean>builder().persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL).build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CustomData>> CUSTOM_DATA_1 = COMPONENTS.register("custom_data_1", () -> DataComponentType.<CustomData>builder().persistent(CustomData.CODEC).networkSynchronized(CustomData.STREAM_CODEC).build());

    public static final Map<Ability, DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>>> ABILITY_TOGGLES = new HashMap<>();
    public static final Map<Ability, DeferredHolder<DataComponentType<?>, DataComponentType<Integer>>> ABILITY_CUSTOM_SETTINGS = new HashMap<>();
    public static final Map<Ability, DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>>> ABILITY_UPGRADE_INSTALLS = new HashMap<>();
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
            if (ability.hasCustomSetting()) {
                DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> ABILITY_CUSTOM_SETTING = COMPONENTS.register(ability.getName() + "_custom_setting", () -> DataComponentType.<Integer>builder().persistent(Codec.INT.orElse(0)).networkSynchronized(ByteBufCodecs.VAR_INT).build());
                ABILITY_CUSTOM_SETTINGS.put(ability, ABILITY_CUSTOM_SETTING);
            }
            if (ability.requiresUpgrade()) {
                DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> ABILITY_UPGRADE_INSTALLED = COMPONENTS.register(ability.getName() + "_upgrade_installed", () -> DataComponentType.<Boolean>builder().persistent(Codec.BOOL.orElse(true)).networkSynchronized(ByteBufCodecs.BOOL).build());
                ABILITY_UPGRADE_INSTALLS.put(ability, ABILITY_UPGRADE_INSTALLED);
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
