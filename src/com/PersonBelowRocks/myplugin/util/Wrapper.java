package com.PersonBelowRocks.myplugin.util;

import org.bukkit.entity.Player;

public class Wrapper {
    Player player;
    int slot;
    public Wrapper(Player player, int slot) {
        this.player = player;
        this.slot = slot;
    }

    public Player getPlayer() {
        return this.player;
    }

    public int getSlot() {
        return this.slot;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }
}
