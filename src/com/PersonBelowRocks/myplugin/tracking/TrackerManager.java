package com.PersonBelowRocks.myplugin.tracking;

import com.PersonBelowRocks.myplugin.items.ItemManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;

import java.util.HashMap;

public class TrackerManager {

    private static final HashMap<Player, Player> trackers = new HashMap<>();

    public static void track() {
        for (Player tracker : trackers.keySet()) {
            if (ItemManager.isEqual(tracker.getInventory().getItemInMainHand(), ItemManager.trackingCompass)) {
                Player target = trackers.get(tracker);

                if (!tracker.getWorld().getEnvironment().equals(target.getWorld().getEnvironment())) {
                    tracker.sendMessage("§eTarget is in another dimension!");
                    tracker.getInventory().setItemInMainHand(null);
                    trackers.remove(tracker);
                    return;
                }

                ItemStack compass = tracker.getInventory().getItemInMainHand();

                CompassMeta compassMeta = (CompassMeta) compass.getItemMeta();
                if (compassMeta == null) {
                    tracker.getInventory().setItemInMainHand(null);
                    tracker.sendMessage("§cYour tracking compass was removed!");
                    return;
                }
                compassMeta.setLodestoneTracked(false);
                compassMeta.setLodestone(target.getLocation());

                compassMeta.setDisplayName("§aDistance to Target: ["+ ( ((tracker.getLocation().distance(target.getLocation())) * 10) / 10.0f ) + "]");

                compass.setItemMeta(compassMeta);

                tracker.getInventory().setItemInMainHand(compass);
            }
        }
    }

    public static void trackPlayer(Player carrier, Player target) {
        trackers.put(carrier, target);
    }
}
