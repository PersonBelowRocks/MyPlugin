package com.PersonBelowRocks.myplugin.tracking;

import com.PersonBelowRocks.myplugin.Configuration;
import com.PersonBelowRocks.myplugin.util.Utils;
import com.PersonBelowRocks.myplugin.util.Wrapper;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;

import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Objects;

import static com.PersonBelowRocks.myplugin.tracking.ActionBar.buildActionBar;

public class TrackerManager {

    private static final HashMap<Player, Wrapper> trackers = new HashMap<>();
    private static final HashMap<Player, String> errorTrackers = new HashMap<>();

    private static final String requiredLore = "§8item tracking_compass";

    private static Configuration cfg;

    public static void init(Configuration c) {
        cfg = c;
    }

    public static void track() {
        for (Player tracker : trackers.keySet()) { // CYCLE THROUGH ALL REGISTERED TRACKERS

            // init stuff
            Wrapper wrapper = trackers.get(tracker);
            Player target = wrapper.getPlayer();
            Inventory inv = tracker.getInventory();
            int itemIndex = wrapper.getSlot();

            // determine position and existence of compass in player inventory
            if (!Utils.itemLoreContains(
                    inv.getItem(itemIndex),
                    requiredLore)) {
                try {
                    itemIndex = Utils.getIndexFromLore(inv, requiredLore);
                    wrapper.setSlot(itemIndex);
                    wrapper.setCompassState(true);
                } catch (NullPointerException e) {
                    wrapper.setCompassState(false);
                }
            } else {
                wrapper.setCompassState(true);
            }

            // todo: check if compass is null instead of compassMeta
            // does our player have a compass?
            if (wrapper.getCompassState()) {
                // get compass & compass meta
                ItemStack compass = inv.getItem(itemIndex);
                CompassMeta compassMeta = (CompassMeta) compass.getItemMeta();

                // remove if compass metadata somehow vanished
                if (compassMeta == null) {
                    errorTrackers.put(tracker, cfg.getString("error-compass-meta"));
                    continue;
                }

                // handle if target logged off
                if (!target.isOnline()) {
                    errorTrackers.put(tracker, cfg.getString("error-target-offline"));
                    continue;
                }

                // handle if compass carrier / tracker logged off
                if (!tracker.isOnline()) {
                    errorTrackers.put(tracker, "");
                    continue;
                }

                // handle if carrier and target are in different dimensions
                if (!tracker.getWorld().getEnvironment().equals(target.getWorld().getEnvironment())) {
                    errorTrackers.put(tracker, cfg.getString("error-different-dimension"));
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

                try {
                    float targetDistanceMoved = (float) target.getLocation().distance(Objects.requireNonNull(compassMeta.getLodestone()));
                    if (targetDistanceMoved > 1.2f) {

                        compassMeta.setLodestoneTracked(false);
                        compassMeta.setLodestone(target.getLocation());

                        compass.setItemMeta(compassMeta);
                    }
                } catch (NullPointerException e) {

                    compassMeta.setLodestoneTracked(false);
                    compassMeta.setLodestone(target.getLocation());

                    compass.setItemMeta(compassMeta);
                }

            } else {
                wrapper.setCompassState(false);
                if (wrapper.getTimeWithoutCompass() > (10*1000)) {
                    errorTrackers.put(tracker, cfg.getString("error-no-compass"));
                }
            }
        }

        // remove trackers with problems (target or carrier offline, target and carrier in different dimensions)
        if (!errorTrackers.isEmpty()) {
            for (Player errorTracker : errorTrackers.keySet()) {
                String notif = errorTrackers.get(errorTracker);

                trackers.remove(errorTracker);

                try {
                    Inventory inv = errorTracker.getInventory();
                    int itemIndex = Utils.getIndexFromLore(inv, requiredLore);
                    inv.setItem(itemIndex, null);
                } catch (NullPointerException ignored) {}

                // notify tracker why their tracking stopped if a reason is provided
                // (there will always be a reason provided, unless the tracker logged off in which case there is no point in sending them a message)
                if (!notif.equals("")) {
                    errorTracker.sendMessage(notif);
                    // clear their action bar
                    errorTracker.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                            TextComponent.fromLegacyText(""));
                }
            }
            errorTrackers.clear();
        }
    }

    public static void trackPlayer(Player tracker, Player target) {
        int dist = (int) Math.round(tracker.getLocation().distance(target.getLocation()));

        if (trackers.containsKey(tracker)) {
            Wrapper wrapper = new Wrapper(target, 0);
            wrapper.setActionBar(dist, buildActionBar(tracker, target));
            trackers.replace(tracker, wrapper);

        } else {
            Wrapper wrapper = new Wrapper(target, 0);
            wrapper.setActionBar(dist, buildActionBar(tracker, target));
            trackers.put(tracker, wrapper);

        }
    }

    public static void untrackPlayer(Player tracker, String notif) {
        if (!trackers.containsKey(tracker)) {
            throw new NullPointerException("not a tracker");
        }
        errorTrackers.put(tracker, notif);
    }

    public static boolean isTracker(Player tracker) {
        return trackers.containsKey(tracker);
    }

    public static Wrapper getWrapper(Player tracker) {
        if (!trackers.containsKey(tracker)) {
            throw new NoSuchElementException();
        }
        return trackers.get(tracker);
    }
}
