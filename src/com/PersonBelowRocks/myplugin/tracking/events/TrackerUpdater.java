package com.PersonBelowRocks.myplugin.tracking.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.PersonBelowRocks.myplugin.tracking.Master;

import java.util.LinkedList;

public class TrackerUpdater implements Listener {

    /*
    when tracker moves, update DISTANCE
    when target moves, update DISTANCE and COMPASS
     */

    @EventHandler
    public static void onPlayerMove(PlayerMoveEvent event) {
        // get event's player for readability
        boolean updatedEverything = false;
        Player player = event.getPlayer();

        // event player is a target
        if (Master.isTarget(player)) {

            // get list of trackers
            LinkedList<Player> trackers = Master.getTrackers(player);

            // update compass and action bar message for everyone
            for (Player tracker : trackers) {

            }
        }
    }
}
