package com.PersonBelowRocks.myplugin.tracking;

import com.PersonBelowRocks.myplugin.MyPlugin;
import org.bukkit.entity.Player;

import java.text.MessageFormat;
import java.util.Locale;

public class ActionBar {

    private final static int remain = 105;

    private static MyPlugin plugin;

    private static String lowDetail;
    private static String highDetail;

    public static void init(MyPlugin p) {
        plugin = p;

        /*
        LOW DETAIL:
        §a< £target£ | §6dist: £distance£m §a>

        HIGH DETAIL:
        §a< £target£ §c£progressbar£ §6£Δy: £deltaY£ | dist: £distance£m §a>

        target = {0}
        progress bar = {1}
        deltaY = {2}
        distance = {3}
         */
        lowDetail = plugin.getConfig().getString("low-detail");
        highDetail = plugin.getConfig().getString("high-detail");
        /*
        lowDetail = lowDetail.replaceAll("£target£", "{0}");
        lowDetail = lowDetail.replaceAll("£progressbar£", "{1}");
        lowDetail = lowDetail.replaceAll("£deltay£", "{2}");
        lowDetail = lowDetail.replaceAll("£distance£", "{3}");

        highDetail = highDetail.replaceAll("£target£", "{0}");
        highDetail = highDetail.replaceAll("£progressbar£", "{1}");
        highDetail = highDetail.replaceAll("£deltay£", "{2}");
        highDetail = highDetail.replaceAll("£distance£", "{3}");*/
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

        /*
        HIGH DETAIL:
        §a< £target£ §c£progressbar£ §6£Δy: £deltaY£ | dist: £distance£m §a>

        LOW DETAIL:
        §a< £target£ | §6dist: £distance£m §a>

        target = {0}
        progress bar = {1}
        deltaY = {2}
        distance = {3}
         */
        String template;
        if (dist <= 105) {
            template = highDetail;
        } else {
            template = lowDetail;
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
        /*
        if (dist <= 105) {

        } else {
            return MessageFormat.format(lowDetail,
                    name,
                    progressBar(remain - Math.abs(dist - 5), remain),
                    deltaY,
                    dist);
        }

        return MessageFormat.format("§a< {0} §c{1} §6{2}dist: {3}m §a>",
                name,
                (dist <= 105 ? progressBar(remain - Math.abs(dist - 5), remain) : "§a|"),
                (dist <= 105 ? "Δy: " + deltaY + " | " : ""),
                dist);*/
    }
}
