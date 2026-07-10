package me.oneharvest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.bukkit.Material;
import org.junit.jupiter.api.Test;

class HarvestRulesTest {
    @Test
    void classifiesHarvestMaterials() {
        assertTrue(HarvestRules.isOre(Material.DIAMOND_ORE));
        assertTrue(HarvestRules.isLog(Material.OAK_LOG));
        assertTrue(HarvestRules.supportsAreaMining(Material.STONE));
        assertFalse(HarvestRules.isOre(Material.STONE));
        assertFalse(HarvestRules.supportsAreaMining(Material.BEDROCK));
    }

    @Test
    void keepsRangesWithinSupportedBounds() {
        assertEquals(0, HarvestRules.clampRange(-1));
        assertEquals(2, HarvestRules.clampRange(2));
        assertEquals(3, HarvestRules.clampRange(99));
        assertEquals(7, HarvestRules.sideLength(3));
        assertFalse(HarvestRules.isValidRange(4));
    }
}
