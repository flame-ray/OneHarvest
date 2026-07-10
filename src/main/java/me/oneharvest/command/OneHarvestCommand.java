package me.oneharvest.command;

import java.util.List;
import java.util.Locale;
import me.oneharvest.HarvestRules;
import me.oneharvest.OneHarvest;
import me.oneharvest.settings.PlayerHarvestSettings;
import me.oneharvest.settings.PlayerSettingsStore;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public final class OneHarvestCommand implements CommandExecutor, TabCompleter {
    private static final String PREFIX = "§6[OneHarvest] §f";

    private final OneHarvest plugin;
    private final PlayerSettingsStore settings;

    public OneHarvestCommand(final OneHarvest plugin, final PlayerSettingsStore settings) {
        this.plugin = plugin;
        this.settings = settings;
    }

    @Override
    public boolean onCommand(
            final CommandSender sender,
            final Command command,
            final String label,
            final String[] arguments
    ) {
        if (arguments.length == 0 || arguments[0].equalsIgnoreCase("help")) {
            sendHelp(sender);
            return true;
        }

        return switch (arguments[0].toLowerCase(Locale.ROOT)) {
            case "toggle" -> toggle(sender);
            case "range" -> setRange(sender, arguments);
            case "max" -> setMaximum(sender, arguments);
            case "reload" -> reload(sender);
            default -> {
                sender.sendMessage(PREFIX + "未知子命令。使用 §e/oh help §f查看帮助。");
                yield true;
            }
        };
    }

    @Override
    public List<String> onTabComplete(
            final CommandSender sender,
            final Command command,
            final String alias,
            final String[] arguments
    ) {
        if (arguments.length == 1) {
            return filter(arguments[0], List.of("help", "toggle", "range", "max", "reload"));
        }
        if (arguments.length == 2 && arguments[0].equalsIgnoreCase("range")) {
            return filter(arguments[1], List.of("0", "1", "2", "3"));
        }
        return List.of();
    }

    private boolean toggle(final CommandSender sender) {
        final Player player = requirePlayer(sender);
        if (player == null) {
            return true;
        }
        final PlayerHarvestSettings updated = settings.toggle(player);
        sender.sendMessage(PREFIX + "自动采集已" + (updated.enabled() ? "§a开启" : "§c关闭") + "§f。");
        return true;
    }

    private boolean setRange(final CommandSender sender, final String[] arguments) {
        final Player player = requirePlayer(sender);
        if (player == null) {
            return true;
        }
        if (arguments.length != 2) {
            sender.sendMessage(PREFIX + "用法：§e/oh range <0-3>§f（0=1×1，1=3×3，2=5×5，3=7×7）。");
            return true;
        }
        try {
            final int range = Integer.parseInt(arguments[1]);
            if (!HarvestRules.isValidRange(range)) {
                throw new NumberFormatException();
            }
            final PlayerHarvestSettings updated = settings.setRange(player, range);
            sender.sendMessage(PREFIX + "范围挖掘已设为 §e" + HarvestRules.sideLength(updated.range())
                    + "×" + HarvestRules.sideLength(updated.range()) + "§f。");
        } catch (final NumberFormatException exception) {
            sender.sendMessage(PREFIX + "范围必须是 0 到 3 的整数。");
        }
        return true;
    }

    private boolean setMaximum(final CommandSender sender, final String[] arguments) {
        if (!sender.hasPermission("oneharvest.admin")) {
            sender.sendMessage(PREFIX + "§c你没有修改全局上限的权限。");
            return true;
        }
        if (arguments.length != 2) {
            sender.sendMessage(PREFIX + "用法：§e/oh max <1-100000>§f。");
            return true;
        }
        try {
            final int maximum = Integer.parseInt(arguments[1]);
            if (maximum < 1 || maximum > 100_000) {
                throw new NumberFormatException();
            }
            plugin.getConfig().set("max-chain-break", maximum);
            plugin.saveConfig();
            sender.sendMessage(PREFIX + "全局最大自动破坏数已设为 §e" + maximum + "§f。");
        } catch (final NumberFormatException exception) {
            sender.sendMessage(PREFIX + "上限必须是 1 到 100000 的整数。");
        }
        return true;
    }

    private boolean reload(final CommandSender sender) {
        if (!sender.hasPermission("oneharvest.reload")) {
            sender.sendMessage(PREFIX + "§c你没有重载配置的权限。");
            return true;
        }
        plugin.reloadOneHarvest();
        sender.sendMessage(PREFIX + "配置与玩家设置已重载。");
        return true;
    }

    private void sendHelp(final CommandSender sender) {
        sender.sendMessage("§6§lOneHarvest 命令");
        sender.sendMessage("§e/oh toggle §f- 开启或关闭自己的自动采集。");
        sender.sendMessage("§e/oh range <0-3> §f- 设置范围：1×1、3×3、5×5、7×7。");
        sender.sendMessage("§e/oh max <1-100000> §f- 修改全局自动破坏上限（管理员）。");
        sender.sendMessage("§e/oh reload §f- 重载配置（管理员）。");
    }

    private Player requirePlayer(final CommandSender sender) {
        if (sender instanceof Player player) {
            return player;
        }
        sender.sendMessage(PREFIX + "此命令只能由玩家执行。");
        return null;
    }

    private List<String> filter(final String prefix, final List<String> candidates) {
        return candidates.stream()
                .filter(candidate -> candidate.startsWith(prefix.toLowerCase(Locale.ROOT)))
                .toList();
    }
}
