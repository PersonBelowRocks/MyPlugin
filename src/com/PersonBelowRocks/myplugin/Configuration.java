package com.PersonBelowRocks.myplugin;

import java.util.HashMap;

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

    // we'll use this HashMap to store and get messages from because it's faster than the bukkit config
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

            // replace ampersands with section signs for color codes
            raw = raw.replaceAll("\\&", "\u00A7");
            this.messages.put(key, raw);
        }
    }
}
