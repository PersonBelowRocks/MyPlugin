package com.PersonBelowRocks.myplugin.tracking;

import com.PersonBelowRocks.myplugin.Configuration;
import org.bukkit.entity.Player;

public class ActionBar {

    private final static int remain = 105;

    private static Configuration cfg;

    public static void init(Configuration c) {
        cfg = c;
    }

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

    public static String buildActionBar(Player tracker, Player target) {

        int dist = (int) Math.round(tracker.getLocation().distance(target.getLocation()));
        int deltaY = (int) Math.round(target.getLocation().getY() - tracker.getLocation().getY());
        String name = target.getName();

        String template;

        if (dist <= 105) {
            template = cfg.getString("high-detail");
        } else {
            template = cfg.getString("low-detail");
        }

        if (template.contains("£target£")) {
            template = template.replaceAll("£target£", name);
        }
        if (template.contains("£progressbar£")) {
            template = template.replaceAll("£progressbar£", progressBar(remain - Math.abs(dist - 5), remain));
        }
        if (template.contains("£deltay£")) {
            template = template.replaceAll("£deltay£", String.valueOf(deltaY));
        }
        if (template.contains("£distance£")) {
            template = template.replaceAll("£distance£", String.valueOf(dist));
        }

        return template;
    }
}
