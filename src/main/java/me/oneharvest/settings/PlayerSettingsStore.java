package me.oneharvest.settings;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import me.oneharvest.HarvestRules;
import me.oneharvest.OneHarvest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

/** Stores player-owned switches and area ranges independently of the global config. */
public final class PlayerSettingsStore {
    private final OneHarvest plugin;
    private final File file;
    private final Map<UUID, PlayerHarvestSettings> settings = new HashMap<>();
    private FileConfiguration configuration;

    public PlayerSettingsStore(final OneHarvest plugin) {
        this.plugin = plugin;
        file = new File(plugin.getDataFolder(), "players.yml");
    }

    public void load() {
        configuration = YamlConfiguration.loadConfiguration(file);
        settings.clear();
        for (final String key : configuration.getKeys(false)) {
            try {
                final UUID playerId = UUID.fromString(key);
                final boolean enabled = configuration.getBoolean(key + ".enabled", true);
                final int range = HarvestRules.clampRange(configuration.getInt(key + ".range", plugin.defaultRange()));
                settings.put(playerId, new PlayerHarvestSettings(enabled, range));
            } catch (final IllegalArgumentException ignored) {
                plugin.getLogger().warning("Ignoring invalid player settings key: " + key);
            }
        }
    }

    public PlayerHarvestSettings get(final Player player) {
        return settings.computeIfAbsent(
                player.getUniqueId(),
                ignored -> new PlayerHarvestSettings(true, plugin.defaultRange())
        );
    }

    public PlayerHarvestSettings toggle(final Player player) {
        final PlayerHarvestSettings current = get(player);
        final PlayerHarvestSettings updated = new PlayerHarvestSettings(!current.enabled(), current.range());
        settings.put(player.getUniqueId(), updated);
        save();
        return updated;
    }

    public PlayerHarvestSettings setRange(final Player player, final int range) {
        final PlayerHarvestSettings current = get(player);
        final PlayerHarvestSettings updated = new PlayerHarvestSettings(current.enabled(), HarvestRules.clampRange(range));
        settings.put(player.getUniqueId(), updated);
        save();
        return updated;
    }

    public void save() {
        if (configuration == null) {
            configuration = new YamlConfiguration();
        }
        for (final Map.Entry<UUID, PlayerHarvestSettings> entry : settings.entrySet()) {
            final String path = entry.getKey().toString();
            configuration.set(path + ".enabled", entry.getValue().enabled());
            configuration.set(path + ".range", entry.getValue().range());
        }
        try {
            configuration.save(file);
        } catch (final IOException exception) {
            plugin.getLogger().severe("Could not save players.yml: " + exception.getMessage());
        }
    }
}
