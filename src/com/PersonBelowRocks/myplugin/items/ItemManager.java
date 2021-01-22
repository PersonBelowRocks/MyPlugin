package com.PersonBelowRocks.myplugin.items;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

import static com.PersonBelowRocks.myplugin.items.ItemTrackingCompass.createCompass;
import static com.PersonBelowRocks.myplugin.items.ItemWand.createWand;

public class ItemManager {

    private static final ArrayList<Boolean> tests = new ArrayList<>();

    public static ItemStack wand;
    public static ItemStack trackingCompass;

    public static void init() {
        wand = createWand();
        trackingCompass = createCompass();
    }

    public static boolean isEqual(ItemStack item1, ItemStack item2) {
        ItemMeta item1Meta = item1.getItemMeta();
        ItemMeta item2Meta = item2.getItemMeta();
        if (item1Meta == null || item2Meta == null) {
            return false;
        }

        tests.add(item1Meta.hasLore() == item2Meta.hasLore());
        tests.add(item1Meta.getLore().equals(item2Meta.getLore()));
        tests.add(item1Meta.getEnchants().equals(item2Meta.getEnchants()));

        boolean same = !tests.contains(false);

        tests.clear();

        return same;
    }
}
