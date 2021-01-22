package com.PersonBelowRocks.myplugin.commands;

import com.PersonBelowRocks.myplugin.items.ItemManager;
import com.PersonBelowRocks.myplugin.tracking.TrackerManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandCompass implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Sorry! Only players are able to do that!");
            return true;
        }
        Player tracker = (Player) sender;
            if (cmd.getName().equalsIgnoreCase("tracker")) {
                Player target;
                if (args.length == 1) {
                    try {
                        target = Bukkit.getServer().getPlayer(args[0]);
                    } catch (IllegalArgumentException e) {
                        tracker.sendMessage("§cIncorrect usage: /tracker <target player>");
                        return true;
                    }
                } else {
                    tracker.sendMessage("§cIncorrect usage: no player specified");
                    return true;
                }

                if (!tracker.getInventory().contains(ItemManager.trackingCompass)) {
                    tracker.getInventory().addItem(ItemManager.trackingCompass);
                    TrackerManager.trackPlayer(tracker, target);
                } else {
                    tracker.sendMessage("§cYou already have a tracking compass!");
                    return true;
                }
            }
            return true;
        }
}
