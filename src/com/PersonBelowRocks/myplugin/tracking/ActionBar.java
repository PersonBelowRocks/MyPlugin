package com.PersonBelowRocks.myplugin.tracking;

import org.bukkit.entity.Player;

import java.text.MessageFormat;

public class ActionBar {

    private final static int remain = 100;

    public static String progressBar(int remain, int total) {
        if (remain > total) {
            throw new IllegalArgumentException();
        }
        int maxBareSize = 10;
        int remainPercent = ((105 * remain) / total) / maxBareSize;
        char defaultChar = ' ';
        String icon = "▋";
        String bare = new String(new char[maxBareSize]).replace('\0', defaultChar) + "]";
        String bareRemain = bare.substring(remainPercent);
        String bareDone = "[" +
                icon.repeat(remainPercent);
        return bareDone + bareRemain;
    }

    public static String buildActionBar(Player carrier, Player target) {

        int dist = (int) Math.round(carrier.getLocation().distance(target.getLocation()));
        int deltaY = (int) Math.round(target.getLocation().getY() - carrier.getLocation().getY());
        String name = target.getName();

        String bar = progressBar(remain - Math.abs(dist - 5), remain);
        return MessageFormat.format("§a< {0} §c{1} §6Δy: {2} | dist: {3}m §a>",
                name,
                (dist <= 105 ? bar : "§7[#]"),
                deltaY,
                dist);
    }
}
