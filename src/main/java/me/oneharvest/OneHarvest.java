package me.oneharvest;

import java.util.Objects;
import me.oneharvest.command.OneHarvestCommand;
import me.oneharvest.listener.HarvestListener;
import me.oneharvest.settings.PlayerSettingsStore;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class OneHarvest extends JavaPlugin {
    private PlayerSettingsStore playerSettings;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        playerSettings = new PlayerSettingsStore(this);
        playerSettings.load();

        getServer().getPluginManager().registerEvents(new HarvestListener(this, playerSettings), this);

        final PluginCommand command = Objects.requireNonNull(
                getCommand("oh"),
                "The /oh command must be declared in plugin.yml"
        );
        final OneHarvestCommand handler = new OneHarvestCommand(this, playerSettings);
        command.setExecutor(handler);
        command.setTabCompleter(handler);

        getLogger().info("OneHarvest " + getDescription().getVersion() + " enabled.");
    }

    @Override
    public void onDisable() {
        if (playerSettings != null) {
            playerSettings.save();
        }
        getLogger().info("OneHarvest disabled.");
    }

    public int maximumAutomaticBreaks() {
        return Math.clamp(getConfig().getInt("max-chain-break", 10_000), 1, 100_000);
    }

    public int defaultRange() {
        return HarvestRules.clampRange(getConfig().getInt("default-range", 1));
    }

    public boolean isFeatureEnabled(final String key) {
        return getConfig().getBoolean(key, true);
    }

    public void reloadOneHarvest() {
        reloadConfig();
        playerSettings.load();
    }
}
