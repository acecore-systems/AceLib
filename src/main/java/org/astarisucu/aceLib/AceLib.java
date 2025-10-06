package org.astarisucu.aceLib;

import org.astarisucu.aceLib.Utiles.Api;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class  AceLib extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        try {
            Api.loadOpenAPI();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
