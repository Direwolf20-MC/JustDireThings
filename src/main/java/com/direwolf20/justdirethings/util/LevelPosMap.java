package com.direwolf20.justdirethings.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class LevelPosMap<T extends BlockEntity> {
    private final Class<T> type;
    public Map<String, Set<BlockPos>> posMap = new ConcurrentHashMap<>();

    public LevelPosMap(Class<T> type) {
        this.type = type;
    }

    public void addPosition(Level level, BlockPos pos) {
        String key = getKey(level);
        posMap.putIfAbsent(key, new HashSet<>());
        posMap.get(key).add(pos);
    }

    public void removePosition(Level level, BlockPos pos) {
        String key = getKey(level);
        if (posMap.containsKey(key)) {
            posMap.get(key).remove(pos);
        }
    }

    public Optional<T> find(Level level, Vec3 vec, BiPredicate<T, Vec3> filter) {
        String key = getKey(level);
        Set<BlockPos> positions = posMap.getOrDefault(key, new HashSet<>());

        for (BlockPos position : positions) {
            if (!level.isLoaded(position)) continue;

            BlockEntity be = level.getBlockEntity(position);
            if (!type.isInstance(be)) {
                positions.remove(position);
                continue;
            }

            T filtered = type.cast(be);

            if (filter.test(filtered, vec)) {
                return Optional.of(filtered);
            }
        }

        return Optional.empty();
    }

    private String getKey(Level level) {
        return level.dimension().location().toString();
    }
}
