package org.astarisucu.aceLib.Utiles;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class InvUI implements Listener {

    private final String id;
    private final Plugin plugin;
    private final int size;
    private final String title;

    // プレイヤーごとの Inventory
    private final Map<UUID, Inventory> inventories = new HashMap<>();

    private final Map<Integer, Button> buttons = new HashMap<>();

    public InvUI(String id, Plugin plugin, int size, String title) {
        this.id = id;
        this.plugin = plugin;
        this.size = size;
        this.title = title;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void addButton(int slot, String name, String materialId, Consumer<Player> action) {
        Material mat = Material.matchMaterial(materialId);
        if (mat == null) mat = Material.BARRIER;

        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            item.setItemMeta(meta);
        }

        buttons.put(slot, new Button(item, action));
    }

    public void deleteButton(int slot) {
        buttons.remove(slot);
    }

    // 特定プレイヤー用インベントリ作成
    private Inventory build(Player player) {
        Inventory inv = Bukkit.createInventory(player, size, title);
        for (Map.Entry<Integer, Button> e : buttons.entrySet()) {
            inv.setItem(e.getKey(), e.getValue().item());
        }
        inventories.put(player.getUniqueId(), inv);
        return inv;
    }

    public void open(Player player) {
        Inventory inv = build(player);
        player.openInventory(inv);
    }

    // 更新（プレイヤー専用）
    public void updateUI(Player player) {
        Inventory inv = inventories.get(player.getUniqueId());
        if (inv == null) return;

        inv.clear();
        for (Map.Entry<Integer, Button> e : buttons.entrySet()) {
            inv.setItem(e.getKey(), e.getValue().item());
        }
        player.updateInventory();
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        Inventory inv = inventories.get(player.getUniqueId());
        if (inv == null) return;
        if (!event.getView().getTitle().equals(title)) return;
        if (event.getClickedInventory() != inv) return;

        int slot = event.getSlot();
        if (buttons.containsKey(slot)) {
            event.setCancelled(true);
            buttons.get(slot).action().accept(player);
        }
    }

    public record Button(ItemStack item, Consumer<Player> action) {}
}
