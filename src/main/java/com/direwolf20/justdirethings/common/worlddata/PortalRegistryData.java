package com.direwolf20.justdirethings.common.worlddata;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.common.entities.PortalEntity;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import org.jspecify.annotations.Nullable;

import java.util.*;

public class PortalRegistryData extends SavedData {

    public record Entry(UUID portalUuid,
                        ResourceKey<Level> dimension,
                        ChunkPos chunkPos,
                        @Nullable UUID partnerUuid,
                        @Nullable UUID gunUuid,
                        @Nullable UUID ownerUuid,
                        boolean isPrimary) {
        public static final Codec<Entry> CODEC = RecordCodecBuilder.create(i -> i.group(
                UUIDUtil.CODEC.fieldOf("portal").forGetter(Entry::portalUuid),
                Level.RESOURCE_KEY_CODEC.fieldOf("dim").forGetter(Entry::dimension),
                ChunkPos.CODEC.fieldOf("chunk").forGetter(Entry::chunkPos),
                UUIDUtil.CODEC.optionalFieldOf("partner").forGetter(e -> Optional.ofNullable(e.partnerUuid())),
                UUIDUtil.CODEC.optionalFieldOf("gun").forGetter(e -> Optional.ofNullable(e.gunUuid())),
                UUIDUtil.CODEC.optionalFieldOf("owner").forGetter(e -> Optional.ofNullable(e.ownerUuid())),
                Codec.BOOL.fieldOf("primary").forGetter(Entry::isPrimary)
        ).apply(i, (p, d, c, pt, g, o, pr) ->
                new Entry(p, d, c, pt.orElse(null), g.orElse(null), o.orElse(null), pr)));

        public Entry withPartner(@Nullable UUID newPartner) {
            return new Entry(portalUuid, dimension, chunkPos, newPartner, gunUuid, ownerUuid, isPrimary);
        }
    }

    private static final Codec<PortalRegistryData> CODEC = Entry.CODEC.listOf()
            .fieldOf("entries")
            .xmap(PortalRegistryData::unpack, PortalRegistryData::pack)
            .codec();

    public static final SavedDataType<PortalRegistryData> TYPE = new SavedDataType<>(
            Identifier.fromNamespaceAndPath(JustDireThings.MODID, "portal_registry"),
            PortalRegistryData::new,
            CODEC);

    private final Map<UUID, Entry> entries = new HashMap<>();

    private PortalRegistryData() {
    }

    private static PortalRegistryData unpack(List<Entry> list) {
        PortalRegistryData data = new PortalRegistryData();
        for (Entry e : list) data.entries.put(e.portalUuid(), e);
        return data;
    }

    private List<Entry> pack() {
        return List.copyOf(entries.values());
    }

    public static PortalRegistryData get(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(TYPE);
    }

    public void addOrUpdate(UUID portal, ResourceKey<Level> dim, ChunkPos chunk, @Nullable UUID partner,
                            @Nullable UUID gun, @Nullable UUID owner, boolean isPrimary) {
        entries.put(portal, new Entry(portal, dim, chunk, partner, gun, owner, isPrimary));
        setDirty();
    }

    public void linkPair(UUID a, ResourceKey<Level> aDim, ChunkPos aChunk, @Nullable UUID gunA, @Nullable UUID ownerA, boolean primA,
                         UUID b, ResourceKey<Level> bDim, ChunkPos bChunk, @Nullable UUID gunB, @Nullable UUID ownerB, boolean primB) {
        // If a or b were previously linked to someone else, clear those stale back-pointers
        // so the old partner doesn't later cascade-kill the new link.
        Entry prevA = entries.get(a);
        if (prevA != null && prevA.partnerUuid() != null && !prevA.partnerUuid().equals(b)) {
            Entry stale = entries.get(prevA.partnerUuid());
            if (stale != null) entries.put(stale.portalUuid(), stale.withPartner(null));
        }
        Entry prevB = entries.get(b);
        if (prevB != null && prevB.partnerUuid() != null && !prevB.partnerUuid().equals(a)) {
            Entry stale = entries.get(prevB.partnerUuid());
            if (stale != null) entries.put(stale.portalUuid(), stale.withPartner(null));
        }
        entries.put(a, new Entry(a, aDim, aChunk, b, gunA, ownerA, primA));
        entries.put(b, new Entry(b, bDim, bChunk, a, gunB, ownerB, primB));
        setDirty();
    }

    public void removePortal(UUID uuid) {
        Entry removed = entries.remove(uuid);
        if (removed != null && removed.partnerUuid() != null) {
            Entry partner = entries.get(removed.partnerUuid());
            if (partner != null) {
                entries.put(partner.portalUuid(), partner.withPartner(null));
            }
        }
        setDirty();
    }

    public @Nullable Entry getEntry(UUID portalUuid) {
        return entries.get(portalUuid);
    }

    public List<Entry> findByGun(UUID gunUuid) {
        List<Entry> out = new ArrayList<>();
        for (Entry e : entries.values()) {
            if (gunUuid.equals(e.gunUuid())) out.add(e);
        }
        return out;
    }

    public List<Entry> findByGunAndPrimary(UUID gunUuid, boolean isPrimary) {
        List<Entry> out = new ArrayList<>();
        for (Entry e : entries.values()) {
            if (gunUuid.equals(e.gunUuid()) && e.isPrimary() == isPrimary) out.add(e);
        }
        return out;
    }

    public List<Entry> findByOwnerOrGun(@Nullable UUID ownerUuid, @Nullable UUID gunUuid) {
        List<Entry> out = new ArrayList<>();
        for (Entry e : entries.values()) {
            if ((ownerUuid != null && ownerUuid.equals(e.ownerUuid()))
                    || (gunUuid != null && gunUuid.equals(e.gunUuid()))) {
                out.add(e);
            }
        }
        return out;
    }

    public static @Nullable PortalEntity resolveEntity(MinecraftServer server, Entry entry) {
        ServerLevel lvl = server.getLevel(entry.dimension());
        if (lvl == null) return null;
        Entity e = lvl.getEntity(entry.portalUuid());
        return e instanceof PortalEntity pe ? pe : null;
    }

    /**
     * Resolve the partner entity, force-loading its chunk if it isn't already loaded.
     * Use this on paths that must reach the partner right now (teleport, cascade-close);
     * normal lookups should prefer {@link #resolveEntity} to avoid waking dormant chunks.
     */
    public static @Nullable PortalEntity resolveEntityForceLoad(MinecraftServer server, Entry entry) {
        ServerLevel lvl = server.getLevel(entry.dimension());
        if (lvl == null) return null;
        Entity e = lvl.getEntity(entry.portalUuid());
        if (e instanceof PortalEntity pe) return pe;
        // Not loaded — bring the chunk in. getChunk(x,z) is a blocking FULL load.
        lvl.getChunk(entry.chunkPos().x(), entry.chunkPos().z());
        e = lvl.getEntity(entry.portalUuid());
        return e instanceof PortalEntity pe ? pe : null;
    }
}
