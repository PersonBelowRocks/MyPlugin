package com.PersonBelowRocks.myplugin.tracking;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;

import java.util.HashMap;

import static org.bukkit.Bukkit.getServer;

public class TrackerManager {

    private static final HashMap<Player, Player> trackers = new HashMap<>();

    private static String progressBar(int remain, int total) {
        if (remain > total) {
            throw new IllegalArgumentException();
        }
        int maxBareSize = 10;
        int remainPercent = ((105 * remain) / total) / maxBareSize;
        getServer().getConsoleSender().sendMessage(String.valueOf(remainPercent));
        char defaultChar = ' ';
        String icon = "■";
        String bare = new String(new char[maxBareSize]).replace('\0', defaultChar) + "]";
        String bareRemain = bare.substring(remainPercent);
        String bareDone = "[" +
                icon.repeat(remainPercent);
        return bareDone + bareRemain;
    }

    public static void track() {

        boolean hasCompass;
        int itemIndex;

        for (Player tracker : trackers.keySet()) {

            hasCompass = false;

            Player target = trackers.get(tracker);
            Inventory inv = tracker.getInventory();

            for (itemIndex = 0; itemIndex <= inv.getSize(); itemIndex++) {
                try {
                    ItemStack item = inv.getItem(itemIndex);
                    if (item.getItemMeta().getLore().contains("§8item tracking_compass")) {
                        hasCompass = true;
                        break;
                    }
                } catch (NullPointerException e) {
                    continue;
                }
            }

            if (hasCompass) {
                if (!target.isOnline()) {
                    tracker.getInventory().setItemInMainHand(null);
                    trackers.remove(tracker);
                    tracker.sendMessage("§cTarget logged off!");
                }

                ItemStack compass = inv.getItem(itemIndex);
                CompassMeta compassMeta = (CompassMeta) compass.getItemMeta();

                if (!tracker.getWorld().getEnvironment().equals(target.getWorld().getEnvironment())) {
                    trackers.remove(tracker);
                    inv.setItem(itemIndex, null);
                    tracker.sendMessage("§cTarget is in another dimension! Rerun the command when you are in the same dimension.");
                    return;
                }

                if (compassMeta == null) {
                    tracker.getInventory().setItem(itemIndex, null);
                    tracker.sendMessage("§cYour tracking compass was removed!");
                    return;
                }

                compassMeta.setLodestoneTracked(false);
                compassMeta.setLodestone(target.getLocation());

                compass.setItemMeta(compassMeta);
                inv.setItem(itemIndex, compass);

                int dist = (int) Math.round(tracker.getLocation().distance(target.getLocation()));
                int deltaY = (int) Math.round(target.getLocation().getY() - tracker.getLocation().getY());

                tracker.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                        TextComponent.fromLegacyText("§a< "
                                + target.getName()
                                + " §7" + (dist <= 105 ? progressBar(100 - Math.abs(dist - 5), 100) : "§7[#]")
                                + " §6Δy: " + deltaY
                                + " | dist: " + dist + "m §a>"));
            } else {
                return;
            }
        }
    }

    public static void trackPlayer(Player carrier, Player target) {
        if (trackers.containsKey(carrier)) {
            trackers.replace(carrier, target);
        } else {
            trackers.put(carrier, target);
        }
    }
}
