package com.PersonBelowRocks.myplugin.tracking.util;

import org.bukkit.entity.Player;

public class Wrapper {

    private Player player;
    private int slot;
    private int dist;
    private String actionBar;
    private long noCompassTimestamp;
    private boolean compassState;

    public Wrapper(Player player, int slot) {
        this.player = player;
        this.slot = slot;
        this.dist = 0;
        this.actionBar = "";
    }

    // getters
    public Player getPlayer() {
        return this.player;
    }

    public int getSlot() {
        return this.slot;
    }

    public int getDist() {
        return this.dist;
    }

    public String getActionBar() {
        return this.actionBar;
    }

    public int getTimeWithoutCompass() {
        if (!this.compassState) return (int) (System.currentTimeMillis() - this.noCompassTimestamp);
        else return 0;
    }

    // setters
    public void setSlot(int slot) {
        this.slot = slot;
    }

    public void setCompassState(boolean val) {
        if (val) this.compassState = true;
        else {
            if (this.compassState) {
                this.noCompassTimestamp = System.currentTimeMillis();
            }
            this.compassState = false;
        }
    }

    public void setActionBar(int dist, String actionBar) {
        this.dist = dist;
        this.actionBar = actionBar;
    }

}
