package com.PersonBelowRocks.myplugin.events;

import com.PersonBelowRocks.myplugin.items.ItemManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class WandFunctionality implements Listener {

    @EventHandler
    public static void onRightClick(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getItem() != null) {
                if (event.getItem().getItemMeta().equals(ItemManager.wand.getItemMeta())) {
                    Player caller = event.getPlayer();

                    caller.getWorld().strikeLightning(caller.getTargetBlock(null, 128).getLocation());
                    caller.getWorld().createExplosion(caller.getTargetBlock(null, 128).getLocation(),
                            10.0f);
                }
            }
        }
    }
}
