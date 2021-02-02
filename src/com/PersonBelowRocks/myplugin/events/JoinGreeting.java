package com.PersonBelowRocks.myplugin.events;

import com.PersonBelowRocks.myplugin.MyPlugin;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinGreeting implements Listener {

    private MyPlugin plugin;

    public JoinGreeting(MyPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (this.plugin.getConfig().getBoolean("join-message")) {
            Player player = event.getPlayer();

            String greeting = String.format("Hello %s!", player.getName());

            player.sendMessage(ChatColor.AQUA + greeting);
        }
    }
}
