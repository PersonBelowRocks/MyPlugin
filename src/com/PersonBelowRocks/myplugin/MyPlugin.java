package com.PersonBelowRocks.myplugin;

import com.PersonBelowRocks.myplugin.commands.CommandTrack;
import com.PersonBelowRocks.myplugin.commands.CommandUntrack;
import com.PersonBelowRocks.myplugin.commands.CommandWand;
import com.PersonBelowRocks.myplugin.events.JoinGreeting;
import com.PersonBelowRocks.myplugin.events.WandFunctionality;
import com.PersonBelowRocks.myplugin.items.ItemManager;
import com.PersonBelowRocks.myplugin.tracking.ActionBar;
import com.PersonBelowRocks.myplugin.tracking.TrackerManager;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

// todo: add /untrack command
public class MyPlugin extends JavaPlugin {

    private final PluginManager pm = getServer().getPluginManager();
    private final ConsoleCommandSender console = getServer().getConsoleSender();


    public void onEnable() {
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();

        TrackerManager.init(this);
        ActionBar.init(this);

        ItemManager.init();

        getCommand("getwand").setExecutor(new CommandWand());
        getCommand("track").setExecutor(new CommandTrack(this));
        getCommand("untrack").setExecutor(new CommandUntrack());

        pm.registerEvents(new JoinGreeting(this), this);
        pm.registerEvents(new WandFunctionality(), this);

        new BukkitRunnable() {
            @Override
            public void run() {
                TrackerManager.track();
            }
        }.runTaskTimer(this, 10, 10);

        console.sendMessage(ChatColor.GREEN + "[MyPlugin] Is enabled!");
    }

    @Override
    public void onDisable() {
        console.sendMessage(ChatColor.RED + "[MyPlugin] Is disabled!");
    }
    // TODO: config lmao
}
