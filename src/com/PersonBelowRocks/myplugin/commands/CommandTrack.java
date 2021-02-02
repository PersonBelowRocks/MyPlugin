package com.PersonBelowRocks.myplugin.commands;

import com.PersonBelowRocks.myplugin.MyPlugin;
import com.PersonBelowRocks.myplugin.items.ItemManager;
import com.PersonBelowRocks.myplugin.tracking.TrackerManager;
import com.PersonBelowRocks.myplugin.tracking.util.Utils;
import com.PersonBelowRocks.myplugin.tracking.util.Wrapper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;

import static org.bukkit.Bukkit.getPlayer;

// TODO: make players have to have a compass item in their inventory to use /track (their compass will be replaced with a tracking compass!)
public class CommandTrack implements CommandExecutor {
    private static MyPlugin plugin;

    private static final HashMap<String, String> messages = new HashMap<>();
    private static final String[] configKeys = {
            "error-self-track",
            "error-not-player",
            "error-no-perms",
            "error-wrong-usage",
            "error-wrong-target",
            "error-already-tracking",
            "notif-now-tracking"
    };

    public CommandTrack(MyPlugin p) {
        plugin = p;

        for (String key : configKeys) {
            messages.put(key, plugin.getConfig().getString(key));
        }

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(messages.get("error-not-player"));
            return true;
        }

        Player tracker = (Player) sender;

        if (!tracker.hasPermission("playertracker.track")) {
            sender.sendMessage(messages.get("error-no-perms"));
            return true;
        }
        if (args.length != 1) {
            sender.sendMessage(messages.get("error-wrong-usage"));
            return true;
        }

        Player target = getPlayer(args[0]);

        if (target == null) {
            sender.sendMessage(messages.get("error-wrong-target"));
            return true;
        }

        if (target == tracker) {
            sender.sendMessage(messages.get("error-self-track"));
            return true;
        }

        if (TrackerManager.isTracker(tracker)) {
            Wrapper wrapper = TrackerManager.getWrapper(tracker);
            if (wrapper.getPlayer().equals(target)) {
                if (messages.get("error-already-tracking").contains("£target£")) {
                    sender.sendMessage(messages.get("error-already-tracking").replaceAll("£target£", target.getName()));
                } else {
                    sender.sendMessage(messages.get("error-already-tracking"));
                }

            } else {
                if (messages.get("notif-now-tracking").contains("£target£")) {
                    sender.sendMessage(messages.get("notif-now-tracking").replaceAll("£target£", target.getName()));
                } else {
                    sender.sendMessage(messages.get("notif-now-tracking"));
                }

                TrackerManager.trackPlayer(tracker, target);
            }
            return true;
        }

        Inventory inv = tracker.getInventory();

        tracker.sendMessage(messages.get("notif-now-tracking").replaceAll("£target£", target.getName()));
        if (!Utils.hasItemWithLore(inv, "§8item tracking_compass") && !TrackerManager.isTracker(tracker)) {
            tracker.getInventory().addItem(ItemManager.trackingCompass);
        }
        TrackerManager.trackPlayer(tracker, target);

        return true;
    }
}
