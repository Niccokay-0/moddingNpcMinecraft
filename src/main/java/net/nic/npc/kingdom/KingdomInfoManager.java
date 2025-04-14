package net.nic.npc.kingdom;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class KingdomInfoManager {

    // Nested map: World -> Player -> KingdomInfo instance
    private static final Map<ServerLevel, Map<UUID, KingdomInfo>> INFO_MAP = new HashMap<>();

    public static KingdomInfo get(ServerPlayer player) {
        ServerLevel world = player.serverLevel();
        UUID playerUUID = player.getUUID();

        INFO_MAP.putIfAbsent(world, new HashMap<>());
        Map<UUID, KingdomInfo> playerMap = INFO_MAP.get(world);

        // If the KingdomInfo instance doesn't exist yet, create and store one
        if (!playerMap.containsKey(playerUUID)) {
            playerMap.put(playerUUID, new KingdomInfo());
        }

        return playerMap.get(playerUUID);
    }
}
