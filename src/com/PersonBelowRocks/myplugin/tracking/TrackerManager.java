package com.PersonBelowRocks.myplugin.tracking;

import com.PersonBelowRocks.myplugin.util.Wrapper;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;

import java.util.HashMap;

import static org.bukkit.Bukkit.getServer;

public class TrackerManager {

    private static final HashMap<Player, Wrapper> trackers = new HashMap<>();

    private static String progressBar(int remain, int total) {
        if (remain > total) {
            throw new IllegalArgumentException();
        }
        int maxBareSize = 10;
        int remainPercent = ((105 * remain) / total) / maxBareSize;
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
                    trackers.remove(tracker);
                    tracker.sendMessage("§cTarget logged off!");
                }

                ItemStack compass = inv.getItem(trackers.get(tracker).getSlot());
                CompassMeta compassMeta = (CompassMeta) compass.getItemMeta();

                if (!tracker.getWorld().getEnvironment().equals(target.getWorld().getEnvironment())) {
                    trackers.remove(tracker);
                    inv.setItem(trackers.get(tracker).getSlot(), null);
                    tracker.sendMessage("§cTarget is in another dimension! Rerun the command when you are in the same dimension.");
                    return;
                }

                if (compassMeta == null) {
                    tracker.getInventory().setItem(trackers.get(tracker).getSlot(), null);
                    tracker.sendMessage("§cYour tracking compass was removed!");
                    return;
                }

                compassMeta.setLodestoneTracked(false);
                compassMeta.setLodestone(target.getLocation());

                compass.setItemMeta(compassMeta);
                inv.setItem(trackers.get(tracker).getSlot(), compass);

                int dist = (int) Math.round(tracker.getLocation().distance(target.getLocation()));
                int deltaY = (int) Math.round(target.getLocation().getY() - tracker.getLocation().getY());

                tracker.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                        TextComponent.fromLegacyText("§a< "
                                + target.getName()
                                + " §c" + (dist <= 105 ? progressBar(100 - Math.abs(dist - 5), 100) : "§7[#]")
                                + " §6Δy: " + deltaY
                                + " | dist: " + dist + "m §a>"));
            } else {
                return;
            }
        }
    }

    public static void trackPlayer(Player carrier, Player target) {
        if (trackers.containsKey(carrier)) {
            trackers.replace(carrier, new Wrapper(target, 0));
        } else {
            trackers.put(carrier, new Wrapper(target, 0));
        }
    }
}
