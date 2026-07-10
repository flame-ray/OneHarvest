package me.oneharvest;

import java.util.Set;
import org.bukkit.Material;

/** Pure material and range rules that are shared by commands and event handling. */
public final class HarvestRules {
    private static final int MAX_RANGE = 3;
    private static final Set<Material> AREA_MATERIALS = Set.of(
            Material.STONE,
            Material.COBBLESTONE,
            Material.DEEPSLATE,
            Material.COBBLED_DEEPSLATE,
            Material.DIRT,
            Material.GRASS_BLOCK,
            Material.COARSE_DIRT,
            Material.ROOTED_DIRT,
            Material.SAND,
            Material.RED_SAND,
            Material.GRAVEL,
            Material.TUFF,
            Material.ANDESITE,
            Material.DIORITE,
            Material.GRANITE,
            Material.NETHERRACK
    );

    private HarvestRules() {
    }

    public static boolean isOre(final Material material) {
        return material.name().endsWith("_ORE");
    }

    public static boolean isLog(final Material material) {
        final String name = material.name();
        return name.endsWith("_LOG")
                || name.endsWith("_STEM")
                || name.endsWith("_HYPHAE")
                || material == Material.BAMBOO_BLOCK;
    }

    public static boolean supportsAreaMining(final Material material) {
        return AREA_MATERIALS.contains(material);
    }

    public static int clampRange(final int range) {
        return Math.clamp(range, 0, MAX_RANGE);
    }

    public static boolean isValidRange(final int range) {
        return range >= 0 && range <= MAX_RANGE;
    }

    public static int sideLength(final int range) {
        return (clampRange(range) * 2) + 1;
    }
}
