package com.PersonBelowRocks.myplugin.commands;

import com.PersonBelowRocks.myplugin.tracking.TrackerManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

// TODO: polish this a bit more, currently feels a little rough for the user
public class CommandUntrack implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Sorry! Only players are able to do that!");
            return true;
        }

        Player tracker = (Player) sender;

        if (!tracker.hasPermission("playertracker.untrack")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
            return true;
        }
        if (!TrackerManager.isTracker(tracker)) {
            sender.sendMessage(ChatColor.RED + "You are not tracking anyone!");
            return true;
        }

        Player target = TrackerManager.getWrapper(tracker).getPlayer();

        TrackerManager.untrackPlayer(tracker, "§eNo longer tracking " + target.getName());
        return true;
    }

}
