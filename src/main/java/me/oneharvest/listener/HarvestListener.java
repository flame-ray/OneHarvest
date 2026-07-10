package me.oneharvest.listener;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import me.oneharvest.HarvestRules;
import me.oneharvest.OneHarvest;
import me.oneharvest.settings.PlayerHarvestSettings;
import me.oneharvest.settings.PlayerSettingsStore;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.util.Vector;

/** Applies automatic harvesting only after the player's original block-break event succeeds. */
public final class HarvestListener implements Listener {
    private static final int[][] ORTHOGONAL_NEIGHBOURS = {
            {1, 0, 0}, {-1, 0, 0}, {0, 1, 0}, {0, -1, 0}, {0, 0, 1}, {0, 0, -1}
    };
    private static final int[][] ALL_NEIGHBOURS = createAllNeighbours();

    private final OneHarvest plugin;
    private final PlayerSettingsStore settings;
    private final Set<UUID> automaticBreakPlayers = new HashSet<>();

    public HarvestListener(final OneHarvest plugin, final PlayerSettingsStore settings) {
        this.plugin = plugin;
        this.settings = settings;
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(final BlockBreakEvent event) {
        final Player player = event.getPlayer();
        if (automaticBreakPlayers.contains(player.getUniqueId())
                || !plugin.isFeatureEnabled("enabled")
                || !player.hasPermission("oneharvest.use")) {
            return;
        }

        final PlayerHarvestSettings playerSettings = settings.get(player);
        if (!playerSettings.enabled()) {
            return;
        }

        final Block origin = event.getBlock();
        final int maximumBreaks = plugin.maximumAutomaticBreaks();
        final List<Block> targets;

        if (plugin.isFeatureEnabled("vein-mining") && HarvestRules.isOre(origin.getType())) {
            targets = connectedBlocks(origin, maximumBreaks, ORTHOGONAL_NEIGHBOURS);
        } else if (plugin.isFeatureEnabled("tree-felling") && HarvestRules.isLog(origin.getType())) {
            targets = connectedBlocks(origin, maximumBreaks, ALL_NEIGHBOURS);
        } else if (plugin.isFeatureEnabled("area-mining") && HarvestRules.supportsAreaMining(origin.getType())) {
            targets = areaBlocks(player, origin, playerSettings.range(), maximumBreaks);
        } else {
            return;
        }

        scheduleAutomaticBreaks(player, targets);
    }

    private List<Block> connectedBlocks(
            final Block origin,
            final int maximumBreaks,
            final int[][] neighbours
    ) {
        final List<Block> targets = new ArrayList<>();
        final Set<BlockPosition> visited = new HashSet<>();
        final Deque<Block> pending = new ArrayDeque<>();
        final BlockPosition originPosition = BlockPosition.from(origin);
        final var material = origin.getType();

        pending.add(origin);
        visited.add(originPosition);
        while (!pending.isEmpty() && targets.size() < maximumBreaks) {
            final Block current = pending.removeFirst();
            if (!BlockPosition.from(current).equals(originPosition)) {
                targets.add(current);
            }
            for (final int[] offset : neighbours) {
                final Block next = current.getRelative(offset[0], offset[1], offset[2]);
                final BlockPosition position = BlockPosition.from(next);
                if (next.getType() == material && visited.add(position)) {
                    pending.addLast(next);
                }
            }
        }
        return targets;
    }

    private List<Block> areaBlocks(
            final Player player,
            final Block origin,
            final int range,
            final int maximumBreaks
    ) {
        final int radius = HarvestRules.clampRange(range);
        if (radius == 0) {
            return List.of();
        }

        final List<Block> targets = new ArrayList<>();
        final Vector direction = player.getLocation().getDirection();
        final double horizontalX = Math.abs(direction.getX());
        final double horizontalZ = Math.abs(direction.getZ());
        final double verticalY = Math.abs(direction.getY());
        final var material = origin.getType();

        for (int first = -radius; first <= radius && targets.size() < maximumBreaks; first++) {
            for (int second = -radius; second <= radius && targets.size() < maximumBreaks; second++) {
                if (first == 0 && second == 0) {
                    continue;
                }
                final Block candidate;
                if (verticalY >= horizontalX && verticalY >= horizontalZ) {
                    candidate = origin.getRelative(first, 0, second);
                } else if (horizontalX >= horizontalZ) {
                    candidate = origin.getRelative(0, first, second);
                } else {
                    candidate = origin.getRelative(first, second, 0);
                }
                if (candidate.getType() == material) {
                    targets.add(candidate);
                }
            }
        }
        return targets;
    }

    private void scheduleAutomaticBreaks(final Player player, final List<Block> targets) {
        if (targets.isEmpty()) {
            return;
        }
        plugin.getServer().getScheduler().runTask(plugin, () -> {
            if (!player.isOnline()) {
                return;
            }
            automaticBreakPlayers.add(player.getUniqueId());
            try {
                for (final Block target : targets) {
                    if (!target.isEmpty()) {
                        // Player#breakBlock dispatches a normal BlockBreakEvent, allowing protection plugins to cancel it.
                        player.breakBlock(target);
                    }
                }
            } finally {
                automaticBreakPlayers.remove(player.getUniqueId());
            }
        });
    }

    private static int[][] createAllNeighbours() {
        final List<int[]> offsets = new ArrayList<>();
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (x != 0 || y != 0 || z != 0) {
                        offsets.add(new int[]{x, y, z});
                    }
                }
            }
        }
        return offsets.toArray(int[][]::new);
    }

    private record BlockPosition(int x, int y, int z) {
        private static BlockPosition from(final Block block) {
            return new BlockPosition(block.getX(), block.getY(), block.getZ());
        }
    }
}
