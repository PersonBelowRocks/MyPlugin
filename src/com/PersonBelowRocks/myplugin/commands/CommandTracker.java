package com.PersonBelowRocks.myplugin.commands;

import com.PersonBelowRocks.myplugin.items.ItemManager;
import com.PersonBelowRocks.myplugin.tracking.TrackerManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import static org.bukkit.Bukkit.getPlayer;

public class CommandTracker implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player target;
        Player tracker;
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Sorry! Only players are able to do that!");
            return true;
        }
        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /track <target>");
            return true;
        }
        target = getPlayer(args[0]);
        tracker = (Player) sender;
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Target does not exist!");
            return true;
        }
        if (target == tracker) {
            sender.sendMessage(ChatColor.RED + "You can't track yourself!");
            return true;
        }

        Inventory inv = tracker.getInventory();
        for (int itemIndex = 0; itemIndex <= inv.getSize(); itemIndex++) {
            try {
                ItemStack item = inv.getItem(itemIndex);
                if (item.getItemMeta().getLore().contains("§8item tracking_compass")) {
                    TrackerManager.trackPlayer(tracker, target);
                    tracker.sendMessage("§eNow tracking " + target.getName());
                    return true;
                }
            } catch (NullPointerException e) {
                continue;
            }
        }

        tracker.getInventory().addItem(ItemManager.trackingCompass);
        TrackerManager.trackPlayer(tracker, target);

        return true;
    }
}
