package com.PersonBelowRocks.myplugin;

import com.PersonBelowRocks.myplugin.commands.CommandCompass;
import com.PersonBelowRocks.myplugin.commands.CommandWand;
import com.PersonBelowRocks.myplugin.events.JoinGreeting;
import com.PersonBelowRocks.myplugin.events.WandFunctionality;
import com.PersonBelowRocks.myplugin.items.ItemManager;
import com.PersonBelowRocks.myplugin.tracking.TrackerManager;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class MyPlugin extends JavaPlugin {

    private final PluginManager pm = getServer().getPluginManager();
    private final ConsoleCommandSender console = getServer().getConsoleSender();

    public void onEnable() {
        ItemManager.init();
        getCommand("getwand").setExecutor(new CommandWand());
        getCommand("tracker").setExecutor(new CommandCompass());

        pm.registerEvents(new JoinGreeting(), this);
        pm.registerEvents(new WandFunctionality(), this);

        new BukkitRunnable() {
            @Override
            public void run() {
                TrackerManager.track();
            }
        }.runTaskTimer(this, 30, 30);

        console.sendMessage(ChatColor.GREEN + "[MyPlugin] Is enabled!");
    }

    @Override
    public void onDisable() {
        console.sendMessage(ChatColor.RED + "[MyPlugin] Is disabled!");
    }
}
