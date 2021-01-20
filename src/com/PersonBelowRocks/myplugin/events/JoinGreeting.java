package com.PersonBelowRocks.myplugin.events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinGreeting implements Listener {

    @EventHandler
    public static void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        String greeting = String.format("Hello %s!", player.getName());

        player.sendMessage(ChatColor.AQUA + greeting);

    }

}
