package me.oneharvest;

import org.bukkit.plugin.java.JavaPlugin;

public class OneHarvest extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().info("OneHarvest enabled!");
        // TODO: Register commands and event listeners here
    }

    @Override
    public void onDisable() {
        getLogger().info("OneHarvest disabled!");
    }
}
