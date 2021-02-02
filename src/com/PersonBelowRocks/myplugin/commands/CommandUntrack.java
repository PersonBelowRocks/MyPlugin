package com.PersonBelowRocks.myplugin.commands;

import com.PersonBelowRocks.myplugin.Configuration;
import com.PersonBelowRocks.myplugin.tracking.TrackerManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

// TODO: polish this a bit more, currently feels a little rough for the user
public class CommandUntrack implements CommandExecutor {
    private static Configuration cfg;

    public CommandUntrack(Configuration c) {
        cfg = c;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(cfg.getString("error-not-player"));
            return false;
        }

        Player tracker = (Player) sender;

        if (!tracker.hasPermission("playertracker.untrack")) {
            sender.sendMessage(cfg.getString("error-no-perms"));
            return false;
        }
        if (!TrackerManager.isTracker(tracker)) {
            sender.sendMessage(cfg.getString("error-isnt-tracker"));
            return false;
        }

        Player target = TrackerManager.getWrapper(tracker).getPlayer();

        String notif;
        if (cfg.getString("notif-tracking-stopped").contains("£target£")) {
            notif = cfg.getString("notif-tracking-stopped").replaceAll("£target£", target.getName());
        } else {
            notif = cfg.getString("notif-tracking-stopped");
        }

        TrackerManager.untrackPlayer(tracker, notif);
        return true;
    }
}
