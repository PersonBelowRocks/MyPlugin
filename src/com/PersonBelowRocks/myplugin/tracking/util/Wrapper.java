package com.PersonBelowRocks.myplugin.tracking.util;

import org.bukkit.entity.Player;

public class Wrapper {
    private Player player;
    private int slot;
    private int dist;
    private String actionBar;
    public Wrapper(Player player, int slot) {
        this.player = player;
        this.slot = slot;
        this.dist = 0;
        this.actionBar = "";
    }

    public Player getPlayer() {
        return this.player;
    }

    public int getSlot() {
        return this.slot;
    }

    public int getDist() {
        return this.dist;
    }

    public void setActionBar(int dist, String actionBar) {
        this.dist = dist;
        this.actionBar = actionBar;
    }

    public String getActionBar() {
        return this.actionBar;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

}
