package net.nic.npc.kingdom;

import net.minecraft.server.level.ServerLevel;
import net.nic.npc.data.KingdomSaveData;

import java.util.UUID;

public class KingdomManager {

    public static void registerKingdom(UUID playerUUID, KingdomInfo kingdom, ServerLevel level) {
        KingdomSaveData saveData = KingdomSaveData.get(level);
        saveData.registerKingdom(playerUUID, kingdom);
    }

    public static KingdomInfo getKingdom(UUID playerUUID, ServerLevel level) {
        KingdomSaveData saveData = KingdomSaveData.get(level);
        return saveData.getKingdoms().get(playerUUID);
    }

    public static boolean hasKingdom(UUID playerUUID, ServerLevel level) {
        KingdomSaveData saveData = KingdomSaveData.get(level);
        return saveData.getKingdoms().containsKey(playerUUID);
    }
}