package com.direwolf20.justdirethings.datagen.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public final class RecipeCodecs {
    private RecipeCodecs() {
    }

    /**
     * BlockState as an inlined map of {@code id} + optional {@code properties}, e.g.
     * <pre>{ "id": "minecraft:furnace", "properties": { "facing": "north" } }</pre>
     * Distinct from {@link BlockState#CODEC}, which uses NBT-style {@code Name}/{@code Properties}.
     * <p>
     * As a {@link MapCodec}, it composes directly into the surrounding object via {@code .fieldOf(...)}
     * or by flattening into another MapCodec.
     */
    public static final MapCodec<BlockState> BLOCK_STATE_AS_MAP = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BuiltInRegistries.BLOCK.byNameCodec().fieldOf("id").forGetter(BlockState::getBlock),
            Codec.unboundedMap(Codec.STRING, Codec.STRING).optionalFieldOf("properties", Map.of())
                    .forGetter(state -> {
                        Map<String, String> result = new LinkedHashMap<>();
                        for (Property<?> property : state.getProperties()) {
                            result.put(property.getName(), getName(state, property));
                        }
                        return result;
                    })
    ).apply(instance, RecipeCodecs::applyProperties));

    public static final Codec<BlockState> BLOCK_STATE = BLOCK_STATE_AS_MAP.codec();

    private static <T extends Comparable<T>> String getName(BlockState state, Property<T> property) {
        return property.getName(state.getValue(property));
    }

    private static BlockState applyProperties(Block block, Map<String, String> properties) {
        BlockState state = block.defaultBlockState();
        StateDefinition<Block, BlockState> def = block.getStateDefinition();
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            Property<?> property = def.getProperty(entry.getKey());
            if (property != null) {
                state = setValue(state, property, entry.getValue());
            }
        }
        return state;
    }

    private static <T extends Comparable<T>> BlockState setValue(BlockState state, Property<T> property, String value) {
        Optional<T> parsed = property.getValue(value);
        return parsed.map(t -> state.setValue(property, t)).orElse(state);
    }
}
