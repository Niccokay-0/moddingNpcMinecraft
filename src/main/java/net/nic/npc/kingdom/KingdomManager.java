package net.nic.npc.kingdom;

import net.minecraft.server.level.ServerLevel;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class KingdomManager {

    private static final Map<UUID, KingdomInfo> KINGDOMS = new HashMap<>();

    public static void registerKingdom(UUID playerUUID, KingdomInfo kingdom, ServerLevel level) {
        KINGDOMS.put(playerUUID, kingdom);
    }

    public static KingdomInfo getKingdom(UUID playerUUID) {
        return KINGDOMS.get(playerUUID);
    }

    public static boolean hasKingdom(UUID playerUUID) {
        return KINGDOMS.containsKey(playerUUID);
    }
}
