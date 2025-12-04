package org.astarisucu.aceLib.Utiles;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UIManager {

    private static final Map<UUID, InvUI> uiMap = new HashMap<>();

    public static InvUI getOrCreateUI(Player player, Plugin plugin, int size, String title) {
        return uiMap.computeIfAbsent(
                player.getUniqueId(),
                id -> new InvUI(id.toString(), plugin, size, title)
        );
    }

    public static InvUI getUI(Player player) {
        return uiMap.get(player.getUniqueId());
    }

    public static void remove(Player player) {
        uiMap.remove(player.getUniqueId());
    }
}
