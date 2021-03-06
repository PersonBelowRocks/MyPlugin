package com.PersonBelowRocks.myplugin.tracking;

import org.bukkit.entity.Player;

import java.text.MessageFormat;

public class ActionBar {

    private final static int remain = 105;

    public static String progressBar(int pRemain, int total) {
        if (pRemain > total) {
            throw new IllegalArgumentException();
        }
        int maxBareSize = 10;
        int remainPercent = ((105 * pRemain) / total) / maxBareSize;
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

        return MessageFormat.format("§a< {0} §c{1} §6{2}dist: {3}m §a>",
                name,
                (dist <= 105 ? progressBar(remain - Math.abs(dist - 5), remain) : "§a|"),
                (dist <= 105 ? "Δy: " + deltaY + " | " : ""),
                dist);
    }
}
