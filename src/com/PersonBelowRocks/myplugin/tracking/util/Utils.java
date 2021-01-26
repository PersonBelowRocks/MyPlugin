package com.PersonBelowRocks.myplugin.tracking.util;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Utils {

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
}
