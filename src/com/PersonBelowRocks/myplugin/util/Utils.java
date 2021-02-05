package com.PersonBelowRocks.myplugin.util;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class Utils {
    private static final Random random = new Random();

    public static int getIndexFromLore(Inventory inv, String lore) {
        for (int itemIndex = 0; itemIndex <= inv.getSize(); itemIndex++) {
            try {
                if (inv.getItem(itemIndex).getItemMeta().getLore().contains(lore)) {
                    return itemIndex;
                }
            } catch (NullPointerException ignored){}
        }
        throw new NullPointerException();
    }

    public static boolean itemLoreContains(ItemStack item, String lore) {
        try {
            return item.getItemMeta().getLore().contains(lore);
        } catch (NullPointerException e) {
            return false;
        }
    }

    public static boolean hasItemWithLore(Inventory inv, String lore) {
        for (ItemStack item : inv) {
            try {
                if (item.getItemMeta().getLore().contains(lore)) {
                    return true;
                }
            } catch (NullPointerException ignored) {}
        }
        return false;
    }

    public static boolean getRandomBoolean(float p) {
        return random.nextFloat() < p;
    }
}
