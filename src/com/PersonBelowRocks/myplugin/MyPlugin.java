package com.PersonBelowRocks.myplugin;

import com.PersonBelowRocks.myplugin.commands.CommandTracker;
import com.PersonBelowRocks.myplugin.commands.CommandWand;
import com.PersonBelowRocks.myplugin.events.JoinGreeting;
import com.PersonBelowRocks.myplugin.events.WandFunctionality;
import com.PersonBelowRocks.myplugin.items.ItemManager;
import com.PersonBelowRocks.myplugin.tracking.TrackerManager;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;

public class MyPlugin extends JavaPlugin {

    private final PluginManager pm = getServer().getPluginManager();
    private final ConsoleCommandSender console = getServer().getConsoleSender();
    private FileConfiguration customConfig;
    private File customConfigFile;


    public void onEnable() {
        createCustomConfig();

        ItemManager.init();
        getCommand("getwand").setExecutor(new CommandWand());
        getCommand("track").setExecutor(new CommandTracker());

        pm.registerEvents(new JoinGreeting(), this);
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
    public FileConfiguration getCustomConfig() {
        return this.customConfig;
    }

    private void createCustomConfig() {
        customConfigFile = new File(getDataFolder(), "custom.yml");
        if (!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            saveResource("my_plugin.yml", false);
        }

        customConfig = new YamlConfiguration();
        try {
            customConfig.load(customConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

}
