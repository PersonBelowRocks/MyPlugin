package com.PersonBelowRocks.myplugin;

import com.PersonBelowRocks.myplugin.events.JoinGreeting;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class MyPlugin extends JavaPlugin {

    private final PluginManager pm = getServer().getPluginManager();
    private final ConsoleCommandSender console = getServer().getConsoleSender();

    @Override
    public void onEnable() {
        console.sendMessage(ChatColor.GREEN + "[MyPlugin] Is enabled!");
        pm.registerEvents(new JoinGreeting(), this);
    }

    @Override
    public void onDisable() {
        console.sendMessage(ChatColor.RED + "[MyPlugin] Is disabled!");
    }
}
