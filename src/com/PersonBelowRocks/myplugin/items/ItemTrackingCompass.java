package com.PersonBelowRocks.myplugin.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemTrackingCompass {
    public static ItemStack createCompass() {
        ItemStack item = new ItemStack(Material.COMPASS);

        CompassMeta meta = (CompassMeta) item.getItemMeta();
        meta.setDisplayName("§ePlayer Tracking Compass");

        List<String> lore = new ArrayList<>();
        lore.add("§8Tracks players");
        meta.setLore(lore);

        meta.setLodestoneTracked(false);

        item.setItemMeta(meta);
        return item;
    }
}
