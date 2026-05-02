package com.direwolf20.justdirethings.datagen.recipes;

import com.mojang.serialization.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.stream.Stream;

/**
 * Recipe input that accepts either a concrete BlockState (with optional properties) or a block tag.
 * <p>
 * JSON serialization (via {@link #CODEC}):
 * <ul>
 *   <li>{@code { "id": "minecraft:coal_block", "properties": { ... } }} — single block, optionally with state properties</li>
 *   <li>{@code { "tag": "c:storage_blocks/charcoal" }} — block tag</li>
 * </ul>
 * Both forms appear under the same {@code "input"} key in recipe JSON; the user no longer
 * needs a separate recipe type for tag inputs.
 */
public sealed interface BlockOrTagInput {
    boolean matches(BlockState state);

    record OfBlock(BlockState state) implements BlockOrTagInput {
        @Override
        public boolean matches(BlockState other) {
            return other.equals(state);
        }
    }

    record OfTag(TagKey<Block> tag) implements BlockOrTagInput {
        @Override
        public boolean matches(BlockState other) {
            return other.is(tag);
        }
    }

    MapCodec<TagKey<Block>> TAG_CODEC = TagKey.codec(Registries.BLOCK).fieldOf("tag");

    /**
     * Dispatches at the same JSON level: if the map has a {@code tag} field, parse as tag; otherwise parse as block state.
     * Encoding flattens both forms directly into the surrounding object.
     */
    MapCodec<BlockOrTagInput> CODEC = new MapCodec<>() {
        @Override
        public <T> DataResult<BlockOrTagInput> decode(DynamicOps<T> ops, MapLike<T> input) {
            if (input.get("tag") != null) {
                return TAG_CODEC.decode(ops, input).map(OfTag::new);
            }
            return RecipeCodecs.BLOCK_STATE_AS_MAP.decode(ops, input).map(OfBlock::new);
        }

        @Override
        public <T> RecordBuilder<T> encode(BlockOrTagInput value, DynamicOps<T> ops, RecordBuilder<T> prefix) {
            if (value instanceof OfTag(TagKey<Block> tag)) {
                return TAG_CODEC.encode(tag, ops, prefix);
            }
            return RecipeCodecs.BLOCK_STATE_AS_MAP.encode(((OfBlock) value).state(), ops, prefix);
        }

        @Override
        public <T> Stream<T> keys(DynamicOps<T> ops) {
            return Stream.of(ops.createString("id"), ops.createString("properties"), ops.createString("tag"));
        }
    };

    StreamCodec<RegistryFriendlyByteBuf, BlockOrTagInput> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public BlockOrTagInput decode(RegistryFriendlyByteBuf buf) {
            boolean isTag = buf.readBoolean();
            if (isTag) {
                Identifier id = buf.readIdentifier();
                return new OfTag(TagKey.create(Registries.BLOCK, id));
            }
            return new OfBlock(Block.stateById(buf.readInt()));
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, BlockOrTagInput value) {
            if (value instanceof OfTag(TagKey<Block> tag)) {
                buf.writeBoolean(true);
                buf.writeIdentifier(tag.location());
            } else {
                buf.writeBoolean(false);
                buf.writeInt(Block.getId(((OfBlock) value).state()));
            }
        }
    };

    static BlockOrTagInput of(BlockState state) {
        return new OfBlock(state);
    }

    static BlockOrTagInput of(TagKey<Block> tag) {
        return new OfTag(tag);
    }

    /**
     * @return either the concrete block state, or null if this input is a tag.
     * Useful for callers that only care about block-form inputs.
     */
    default BlockState asState() {
        return this instanceof OfBlock(BlockState s) ? s : null;
    }

    default TagKey<Block> asTag() {
        return this instanceof OfTag(TagKey<Block> t) ? t : null;
    }
}
