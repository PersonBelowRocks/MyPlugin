package com.PersonBelowRocks.myplugin.items;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemWand {
    public static ItemStack createWand() {
        ItemStack item = new ItemStack(Material.STICK);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§eStick of Zeus");

        List<String> lore = new ArrayList<>();
        lore.add("§8Strike me down Zeus!");
        meta.setLore(lore);

        meta.addEnchant(Enchantment.ARROW_INFINITE, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        item.setItemMeta(meta);
        return item;
    }
}
