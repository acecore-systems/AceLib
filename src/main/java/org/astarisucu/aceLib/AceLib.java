package org.astarisucu.aceLib;

import org.astarisucu.aceLib.Utiles.Api;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class  AceLib extends JavaPlugin {

    @Override
    public void onEnable() {
        File configFile = new File(this.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            this.saveResource("config.yml", false); // デフォルト config.yml をコピー
        }

        org.bukkit.configuration.file.FileConfiguration cfg = this.getConfig();
//        Api.loadBaseURL(cfg.getString("api.baseUrl", "http://localhost:8000"));
//
//        try {
//            Api.loadOpenAPI();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }



    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
