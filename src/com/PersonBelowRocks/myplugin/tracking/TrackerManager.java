package com.PersonBelowRocks.myplugin.tracking;

import com.PersonBelowRocks.myplugin.tracking.util.Utils;
import com.PersonBelowRocks.myplugin.tracking.util.Wrapper;
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

    private static final String requiredLore = "§8item tracking_compass";

    public static void track() {

        boolean hasCompass;
        int itemIndex;

        for (Player tracker : trackers.keySet()) { // CYCLE THROUGH ALL REGISTERED TRACKERS

            // init stuff
            Wrapper wrapper = trackers.get(tracker);

            Player target = wrapper.getPlayer();
            Inventory inv = tracker.getInventory();
            itemIndex = wrapper.getSlot();

            // determine position and existence of compass in player inventory
            if (!Utils.itemLoreContains(
                    inv.getItem(itemIndex),
                    requiredLore)) {
                try {
                    itemIndex = Utils.getIndexFromLore(inv, requiredLore);
                    wrapper.setSlot(itemIndex);
                    hasCompass = true;
                } catch (NullPointerException e) {
                    hasCompass = false;
                }
            } else {
                hasCompass = true;
            }

            // does our player have a compass?
            if (hasCompass) {
                // handle if target logged off
                if (!target.isOnline()) {
                    inv.setItem(itemIndex, null);
                    tracker.sendMessage("§cTarget logged off!");
                    tracker.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                            TextComponent.fromLegacyText(""));
                    errorTrackers.add(tracker);
                    continue;
                }

                // handle if compass carrier / tracker logged off
                if (!tracker.isOnline()) {
                    inv.setItem(itemIndex, null);
                    errorTrackers.add(tracker);
                    continue;
                }

                wrapper.setCompassState(true);

                // handle if carrier and target are in different dimensions
                if (!tracker.getWorld().getEnvironment().equals(target.getWorld().getEnvironment())) {
                    inv.setItem(itemIndex, null);
                    tracker.sendMessage("§cTarget is in another dimension! Rerun the command when you are in the same dimension.");
                    tracker.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                            TextComponent.fromLegacyText(""));
                    errorTrackers.add(tracker);
                    continue;
                }

                // calculate distance
                int dist = (int) Math.round(tracker.getLocation().distance(target.getLocation()));

                // build new action bar if distance changed
                if (wrapper.getDist() == dist) {
                    tracker.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                            TextComponent.fromLegacyText(wrapper.getActionBar()));
                } else {
                    wrapper.setActionBar(dist, buildActionBar(tracker, target));
                    tracker.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                            TextComponent.fromLegacyText(buildActionBar(tracker, target)));
                }

                // get compass & compass meta
                ItemStack compass = inv.getItem(itemIndex);
                CompassMeta compassMeta = (CompassMeta) compass.getItemMeta();

                /*
                * remove compass if compassMeta is null
                * this can happen if:
                * A. minecraft does something.. well.. minecrafty and messes up the metadata
                * B. the player in question used a custom item editor and removed the metadata (i think)
                *
                * either way, better safe than constant NPEs in the console!
                 */
                if (compassMeta == null) {
                    inv.setItem(itemIndex, null);
                    tracker.sendMessage("§cYour tracking compass was removed!");
                    tracker.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                            TextComponent.fromLegacyText(""));
                    errorTrackers.add(tracker);
                    continue;
                }

                // lol
                try {
                    float targetDistanceMoved = (float) target.getLocation().distance(Objects.requireNonNull(compassMeta.getLodestone()));
                    if (targetDistanceMoved > 1.2f) {

                        compassMeta.setLodestoneTracked(false);
                        compassMeta.setLodestone(target.getLocation());

                        compass.setItemMeta(compassMeta);
                        inv.setItem(itemIndex, compass);
                    }
                } catch (NullPointerException e) {
                    compassMeta.setLodestoneTracked(false);
                    compassMeta.setLodestone(target.getLocation());

                    compass.setItemMeta(compassMeta);
                    inv.setItem(itemIndex, compass);
                }

            } else {
                wrapper.setCompassState(false);
                if (wrapper.getTimeWithoutCompass() > (10*1000)) {
                    tracker.sendMessage("§cYour tracking was stopped because a tracking compass could not be found in your inventory!");
                    errorTrackers.add(tracker);
                }
            }
        }

        // remove trackers with problems (target or carrier offline, target and carrier in different dimensions)
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
