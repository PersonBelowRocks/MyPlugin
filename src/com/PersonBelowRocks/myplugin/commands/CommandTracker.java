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

import java.util.Objects;

public class CommandTracker implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player target;
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Sorry! Only players are able to do that!");
            return true;
        }
        Player tracker = (Player) sender;
            if (cmd.getName().equalsIgnoreCase("track")) {
                try {
                    target = Bukkit.getServer().getPlayer(args[0]);
                    if (!target.isOnline()) {
                        throw new Exception("a");
                    }
                } catch (Exception e) {
                    tracker.sendMessage("§eSomething went wrong :O");
                    return true;
                }
                /*
                if (args.length == 1) {
                    try {
                        target = Bukkit.getServer().getPlayer(args[0]);
                        if (!target.isOnline()) {

                        }
                    } catch (IllegalArgumentException e) {
                        tracker.sendMessage("§cIncorrect usage: /track <target player>");
                        return true;
                    }
                } else {
                    tracker.sendMessage("§cIncorrect usage: no player specified");
                    return true;
                } */

                /*
                holy shit what the hell is this

                i cannot think of a better way to check if the player already has a compass than to compare the item's LORE.
                also the IDE keeps complaining about potential nUlLpOiNtErExCePtIoNs even though it is quite literally not possible
                for them to happen if the previous condition is true.

                i really fucking wish i do NOT have to manually compare /every last bit/ of data associated with an ItemStack
                to ensure that said ItemStack is actually what I want it to be.
                I could have compared so many more things here to be even surer that the player already has a compass,
                but for the sake of both mine and your sanity I decided to call it a day after writing this
                multi-layered, confusing as shit, inefficient, and downright ridiculous if-statement hell.

                if there is a better way to do this (which there's gotta be, I mean come on, there's NO way this is
                how you're supposed to compare ItemStacks), then please for the love of god fix it because I don't want to
                peek inside this class file ever again.

                (ok admittedly it looks kinda neat from a distance, but that doesn't change the fact that it still sucks
                on every other level)
                 */

                Inventory inv = tracker.getInventory();
                for (int itemIndex = 0; itemIndex <= inv.getSize(); itemIndex++) {
                    if (inv.getItem(itemIndex) != null) {
                        if (Objects.requireNonNull(inv.getItem(itemIndex)).hasItemMeta()) {
                            if (Objects.requireNonNull(Objects.requireNonNull(inv.getItem(itemIndex)).getItemMeta()).hasLore()) {
                                if (Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(inv.getItem(itemIndex)).getItemMeta()).getLore()).get(0).equals("§8Tracks players")) {
                                    tracker.sendMessage("§cYou already have a tracking compass!");
                                    return true;
                                }
                            }
                        }
                    }
                }

                tracker.getInventory().addItem(ItemManager.trackingCompass);
                TrackerManager.trackPlayer(tracker, target);
            }
            return true;
        }
}
