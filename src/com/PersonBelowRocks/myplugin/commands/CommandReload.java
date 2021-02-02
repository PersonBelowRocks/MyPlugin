package com.PersonBelowRocks.myplugin.commands;

import com.PersonBelowRocks.myplugin.Configuration;
import com.PersonBelowRocks.myplugin.MyPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandReload implements CommandExecutor {
    private static Configuration cfg;

    public CommandReload(Configuration c, MyPlugin p) {
        cfg = c;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!sender.hasPermission("playertracker.track")) {
            sender.sendMessage(cfg.getString("error-no-perms"));
            return false;
        }

        sender.sendMessage("§eReloading...");
        cfg.loadConfig();
        return true;
    }
}
