package net.nic.npc.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;
import net.nic.npc.kingdom.KingdomInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class KingdomSaveData extends SavedData {

    public static ServerLevel levelForLoading;
    private final Map<UUID, KingdomInfo> kingdoms = new HashMap<>();

    public static final SavedData.Factory<KingdomSaveData> FACTORY = new SavedData.Factory<>(
            KingdomSaveData::new,
            (tag, lookup) -> load(tag, levelForLoading),
            DataFixTypes.SAVED_DATA_SCOREBOARD // <-- This is the missing third parameter
    );

    public static KingdomSaveData get(ServerLevel level) {
        levelForLoading = level;
        KingdomSaveData data = level.getDataStorage().computeIfAbsent(FACTORY, "kingdom_data");
        levelForLoading = null;
        return data;
    }


    public static KingdomSaveData loadWrapped(CompoundTag tag) {
        return load(tag, levelForLoading);
    }


        public void registerKingdom(UUID playerUUID, KingdomInfo kingdom) {
        kingdoms.put(playerUUID, kingdom);
        setDirty();
    }

    public Map<UUID, KingdomInfo> getKingdoms() {
        return kingdoms;
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag, HolderLookup.Provider provider) {
        ListTag listTag = new ListTag();
        for (Map.Entry<UUID, KingdomInfo> entry : kingdoms.entrySet()) {
            CompoundTag entryTag = new CompoundTag();
            entryTag.putUUID("UUID", entry.getKey());
            entryTag.put("Kingdom", entry.getValue().save(new CompoundTag()));
            listTag.add(entryTag);
        }
        compoundTag.put("Kingdoms", listTag);
        return compoundTag;
    }

    public static KingdomSaveData load(CompoundTag tag, ServerLevel level) {
        KingdomSaveData data = new KingdomSaveData();
        ListTag listTag = tag.getList("Kingdoms", Tag.TAG_COMPOUND);

        for (Tag base : listTag) {
            CompoundTag entryTag = (CompoundTag) base;
            UUID uuid = entryTag.getUUID("UUID");
            KingdomInfo info = KingdomInfo.load(entryTag.getCompound("Kingdom"), level);
            data.kingdoms.put(uuid, info);
        }

        return data;
    }
}