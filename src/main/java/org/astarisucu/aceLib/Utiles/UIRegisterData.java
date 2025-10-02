package org.astarisucu.aceLib.Utiles;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class UIRegisterData {
    private final Plugin plugin;
    private final String gameName;
    private final ItemStack icon;
    private final InvUI ui;

    public UIRegisterData(Plugin plugin, String gameName, ItemStack icon, InvUI ui) {
        this.plugin = plugin;
        this.gameName = gameName;
        this.icon = icon;
        this.ui = ui;
    }

    public Plugin getPlugin() { return plugin; }
    public String getGameName() { return gameName; }
    public ItemStack getIcon() { return icon; }
    public InvUI getUI() { return ui; }
}
