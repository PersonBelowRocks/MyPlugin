package com.PersonBelowRocks.myplugin.tracking;

import com.PersonBelowRocks.myplugin.tracking.util.Wrapper;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.LinkedList;

public class Master {
    private static final HashMap<Player, Wrapper> cacheTable = new HashMap<>();

    private static final HashMap<Player, LinkedList<Player>> targetsToTrackers = new HashMap<>();
    private static final HashMap<Player, Player> trackersToTargets = new HashMap<>();

    public static boolean isTarget(Player player) {
        return targetsToTrackers.containsKey(player);
    }

    public static boolean isTracker(Player player) {
        return trackersToTargets.containsKey(player);
    }

    public static void updatePair(Player tracker, Player target) {
        LinkedList<Player> targetsTrackers;

        if (targetsToTrackers.containsKey(target)) {
            targetsTrackers = targetsToTrackers.get(target);
            targetsTrackers.add(tracker);

            // update our HashMap
            targetsToTrackers.replace(target, targetsTrackers);

        } else {

            targetsTrackers = new LinkedList<>();
            targetsTrackers.add(tracker);

            // update our HashMap
            targetsToTrackers.put(target, targetsTrackers);
        }
        // set the tracker
        if (trackersToTargets.containsKey(tracker)) {
            trackersToTargets.replace(tracker, target);
        } else {
            trackersToTargets.put(tracker, target);
        }
    }

    public static LinkedList<Player> getTrackers(Player target) {
        if (!isTarget(target)) {
            throw new IllegalArgumentException();
        }

        return targetsToTrackers.get(target);
    }

    public static Player getTarget(Player tracker) {
        if (!isTracker(tracker)) {
            throw new IllegalArgumentException();
        }

        return trackersToTargets.get(tracker);
    }
}
