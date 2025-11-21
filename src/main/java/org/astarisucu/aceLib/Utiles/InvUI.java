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
import java.util.function.Consumer;

public class InvUI implements Listener {

    private final String id;
    private final Plugin plugin;
    private final int size;
    private final String title;
    private final Map<Integer, Button> buttons = new HashMap<>();
    private Inventory inventory;

    public InvUI(String id, Plugin plugin, int size, String title) {
        this.id = id;
        this.plugin = plugin;
        this.size = size;
        this.title = title;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    // ボタン追加
    public void addButton(int slot, String name, String materialId, Consumer<Player> action) {
        Material mat = Material.matchMaterial(materialId);
        if (mat == null) {
            mat = Material.BARRIER; // 無効なIDならバリアで代替
        }

        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            item.setItemMeta(meta);
        }

        buttons.put(slot, new Button(item, action));
    }

    // ボタン削除
    public void deleteButton(int slot) {
        buttons.remove(slot);
    }

    // ボタンデータを取得
    public Map<Integer, Button> getButton() {
        return buttons;
    }

    // UIを完成させる
    public Inventory build() {
        inventory = Bukkit.createInventory(null, size, title);
        for (Map.Entry<Integer, Button> entry : buttons.entrySet()) {
            inventory.setItem(entry.getKey(), entry.getValue().item());
        }
        return inventory;
    }

    // プレイヤーにUIを開かせる
    public void open(Player player) {
        player.openInventory(build());
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (inventory == null) return;
        if (!event.getView().getTitle().equals(title)) return;
        if (event.getClickedInventory() == null) return;

        int slot = event.getSlot();
        if (buttons.containsKey(slot)) {
            event.setCancelled(true);
            if (event.getWhoClicked() instanceof Player player) {
                buttons.get(slot).action().accept(player);
            }
        }
    }

    public void updateUI(Player player) {
        if (this.inventory == null) {
            return;
        }
        this.inventory.clear();

        for (Map.Entry<Integer, Button> entry : buttons.entrySet()) {
            this.inventory.setItem(entry.getKey(), entry.getValue().item());
        }
        player.updateInventory();
    }

    // 内部的にボタンを保持するクラス
        public record Button(ItemStack item, Consumer<Player> action) {
    }
}
