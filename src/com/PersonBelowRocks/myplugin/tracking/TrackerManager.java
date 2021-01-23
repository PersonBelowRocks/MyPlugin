package com.PersonBelowRocks.myplugin.tracking;

import com.PersonBelowRocks.myplugin.util.Wrapper;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;

import static com.PersonBelowRocks.myplugin.tracking.ActionBar.buildActionBar;

public class TrackerManager {

    private static final HashMap<Player, Wrapper> trackers = new HashMap<>();
    private static final LinkedList<Player> errorTrackers = new LinkedList<>();

    public static void track() {

        boolean hasCompass;
        int itemIndex;

        for (Player tracker : trackers.keySet()) {

            hasCompass = false;

            Player target = trackers.get(tracker).getPlayer();
            Inventory inv = tracker.getInventory();

            try {
                ItemStack item = inv.getItem(trackers.get(tracker).getSlot());
                if (item.getItemMeta().getLore().contains("§8item tracking_compass")) {
                    hasCompass = true;
                }
            } catch (NullPointerException ignored) {}

            if (!hasCompass) {
                for (itemIndex = 0; itemIndex <= inv.getSize(); itemIndex++) {
                    try {
                        ItemStack item = inv.getItem(itemIndex);
                        if (item.getItemMeta().getLore().contains("§8item tracking_compass")) {
                            hasCompass = true;
                            trackers.replace(tracker, new Wrapper(target, itemIndex));
                            break;
                        }
                    } catch (NullPointerException ignored) {}
                }
            }

            if (hasCompass) {
                if (!target.isOnline()) {
                    tracker.getInventory().setItemInMainHand(null);
                    tracker.sendMessage("§cTarget logged off!");
                    errorTrackers.add(tracker);
                    continue;
                }
                if (!tracker.isOnline()) {
                    errorTrackers.add(tracker);
                    continue;
                }
                if (!tracker.getWorld().getEnvironment().equals(target.getWorld().getEnvironment())) {
                    inv.setItem(trackers.get(tracker).getSlot(), null);
                    tracker.sendMessage("§cTarget is in another dimension! Rerun the command when you are in the same dimension.");
                    errorTrackers.add(tracker);
                    continue;
                }

                int dist = (int) Math.round(tracker.getLocation().distance(target.getLocation()));

                if (trackers.get(tracker).getDist() == dist) {
                    tracker.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                            TextComponent.fromLegacyText(trackers.get(tracker).getActionBar()));
                } else {
                    trackers.get(tracker).setActionBar(dist, buildActionBar(tracker, target));
                    tracker.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                            TextComponent.fromLegacyText(buildActionBar(tracker, target)));
                }

                ItemStack compass = inv.getItem(trackers.get(tracker).getSlot());
                CompassMeta compassMeta = (CompassMeta) compass.getItemMeta();

                if (compassMeta == null) {
                    tracker.getInventory().setItem(trackers.get(tracker).getSlot(), null);
                    errorTrackers.add(tracker);
                    tracker.sendMessage("§cYour tracking compass was removed!");
                    continue;
                }

                double targetX = Math.round(target.getLocation().getX());
                double targetY = Math.round(target.getLocation().getY());
                double targetZ = Math.round(target.getLocation().getZ());

                // lol
                try {
                    if (Math.abs(Objects.requireNonNull(compassMeta.getLodestone()).getX() - targetX) > 1 ||
                            Math.abs(compassMeta.getLodestone().getY() - targetY) > 1 ||
                            Math.abs(compassMeta.getLodestone().getZ() - targetZ) > 1) {

                        compassMeta.setLodestoneTracked(false);
                        compassMeta.setLodestone(target.getLocation());

                        compass.setItemMeta(compassMeta);
                        inv.setItem(trackers.get(tracker).getSlot(), compass);
                    }
                } catch (NullPointerException e) {
                    compassMeta.setLodestoneTracked(false);
                    compassMeta.setLodestone(target.getLocation());

                    compass.setItemMeta(compassMeta);
                    inv.setItem(trackers.get(tracker).getSlot(), compass);
                }
            }
        }

        if (!errorTrackers.isEmpty()) {
            for (Player errorTracker : errorTrackers) {
                trackers.remove(errorTracker);
            }
            errorTrackers.clear();
        }

    }

    public static void trackPlayer(Player carrier, Player target) {
        int dist = (int) Math.round(carrier.getLocation().distance(target.getLocation()));

        if (trackers.containsKey(carrier)) {
            Wrapper wrap = new Wrapper(target, 0);
            wrap.setActionBar(dist, buildActionBar(carrier, target));
            trackers.replace(carrier, wrap);

        } else {
            Wrapper wrap = new Wrapper(target, 0);
            wrap.setActionBar(dist, buildActionBar(carrier, target));
            trackers.put(carrier, wrap);

        }
    }
}
