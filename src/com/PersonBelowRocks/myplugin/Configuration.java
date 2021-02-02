package com.PersonBelowRocks.myplugin;

import java.util.HashMap;

import static org.bukkit.Bukkit.getServer;

public class Configuration {
    private static final String[] configMessageKeys = {
            "error-compass-meta",
            "error-target-offline",
            "error-different-dimension",
            "error-no-compass",
            "error-self-track",
            "error-not-player",
            "error-no-perms",
            "error-wrong-usage",
            "error-wrong-target",
            "error-already-tracking",

            "notif-now-tracking",

            "low-detail",
            "high-detail"
    };

    private final MyPlugin plugin;
    private final HashMap<String, String> messages;

    public Configuration(MyPlugin plugin) {
        this.plugin = plugin;
        this.messages = new HashMap<>();
    }

    public String getString(String key) {
        return this.messages.get(key);
    }

    public void loadConfig() {
        this.messages.clear();
        this.plugin.reloadConfig();

        for (String key : configMessageKeys) {
            String raw = this.plugin.getConfig().getString(key);
            raw = raw.replaceAll("\\&", "\u00A7");

            getServer().getConsoleSender().sendMessage(raw);

            this.messages.put(key, raw);
        }
    }
}
