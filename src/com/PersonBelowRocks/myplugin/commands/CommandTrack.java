package com.PersonBelowRocks.myplugin.commands;

import com.PersonBelowRocks.myplugin.Configuration;
import com.PersonBelowRocks.myplugin.items.ItemManager;
import com.PersonBelowRocks.myplugin.tracking.TrackerManager;
import com.PersonBelowRocks.myplugin.tracking.util.Utils;
import com.PersonBelowRocks.myplugin.tracking.util.Wrapper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import static org.bukkit.Bukkit.getPlayer;

// TODO: make players have to have a compass item in their inventory to use /track (their compass will be replaced with a tracking compass!)
public class CommandTrack implements CommandExecutor {
    private static Configuration cfg;

    public CommandTrack(Configuration c) {
        cfg = c;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(cfg.getString("error-not-player"));
            return false;
        }

        Player tracker = (Player) sender;

        if (!tracker.hasPermission("playertracker.track")) {
            sender.sendMessage(cfg.getString("error-no-perms"));
            return false;
        }

        if (args.length != 1) {
            sender.sendMessage(cfg.getString("error-wrong-usage"));
            return false;
        }

        Player target = getPlayer(args[0]);

        if (target == null) {
            sender.sendMessage(cfg.getString("error-wrong-target"));
            return false;
        }

        if (target == tracker) {
            sender.sendMessage(cfg.getString("error-self-track"));
            return false;
        }

        if (TrackerManager.isTracker(tracker)) {

            Wrapper wrapper = TrackerManager.getWrapper(tracker);

            if (wrapper.getPlayer().equals(target)) {

                if (cfg.getString("error-already-tracking").contains("£target£")) {
                    sender.sendMessage(cfg.getString("error-already-tracking").replaceAll("£target£", target.getName()));
                } else {
                    sender.sendMessage(cfg.getString("error-already-tracking"));
                }

                return false;

            } else {

                if (cfg.getString("notif-now-tracking").contains("£target£")) {
                    sender.sendMessage(cfg.getString("notif-now-tracking").replaceAll("£target£", target.getName()));
                } else {
                    sender.sendMessage(cfg.getString("notif-now-tracking"));
                }

                TrackerManager.trackPlayer(tracker, target);

            }
            return true;
        }

        Inventory inv = tracker.getInventory();

        if (cfg.getString("notif-now-tracking").contains("£target£")) {
            tracker.sendMessage(cfg.getString("notif-now-tracking").replaceAll("£target£", target.getName()));
        } else {
            tracker.sendMessage(cfg.getString("notif-now-tracking"));
        }

        if (!Utils.hasItemWithLore(inv, "§8item tracking_compass") && !TrackerManager.isTracker(tracker)) {
            tracker.getInventory().addItem(ItemManager.trackingCompass);
        }

        TrackerManager.trackPlayer(tracker, target);

        return true;
    }
}
