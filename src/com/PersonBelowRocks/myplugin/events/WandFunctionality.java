package com.PersonBelowRocks.myplugin.events;

import com.PersonBelowRocks.myplugin.items.ItemManager;
import com.PersonBelowRocks.myplugin.util.Utils;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Random;

import static org.bukkit.Bukkit.getServer;

public class WandFunctionality implements Listener {

    private static final double beamVerticalModifier = 1.2;
    private static final float scalarIncrement = 0.01f;
    private static final double damage = 5.0;
    private static final int maxIterations = 40;

    private static final double horizontalP = 0.0075;
    private static final double verticalP = 0.0175;

    private static final Random RNG = new Random();

    private static final double RADIANS_CONVERSION_FACTOR = Math.PI / 180;

    @EventHandler
    public static void onRightClick(PlayerInteractEvent event) {
        Player invoker = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getItem() != null) {
                if (event.getItem().getItemMeta().equals(ItemManager.wand.getItemMeta())) {

                    // todo: improve performance

                    Location playerLoc = invoker.getLocation();
                    World world = invoker.getWorld();

                    double pitch = (playerLoc.getPitch() + 90) * RADIANS_CONVERSION_FACTOR;
                    double yaw  = (playerLoc.getYaw() + 90)  * RADIANS_CONVERSION_FACTOR;

                    double x = Math.sin(pitch) * Math.cos(yaw);
                    double y = Math.sin(pitch) * Math.sin(yaw);
                    double z = Math.cos(pitch);

                    Vector3D invokerDirectionFacingVector = new Vector3D(x, z, y);
                    Vector3D invokerLocationVector = new Vector3D(playerLoc.getX(), playerLoc.getY(), playerLoc.getZ());

                    Vector knockbackVector = new Vector(
                            x,
                            z,
                            y
                    );

                    float scalar = 0.5f;

                    HashSet<LivingEntity> protectedEntities = new HashSet<>();

                    protectedEntities.add(invoker.getPlayer());

                    world.playSound(playerLoc, Sound.BLOCK_SHROOMLIGHT_PLACE, 2, 2);

                    for (int count = 0; count < maxIterations; count++) {
                        scalar += scalarIncrement;

                        invokerLocationVector = invokerLocationVector.add(invokerDirectionFacingVector.scalarMultiply(scalar));

                        Vector particleSpawnVector = new Vector(
                                invokerLocationVector.getX(),
                                invokerLocationVector.getY()+beamVerticalModifier,
                                invokerLocationVector.getZ()
                        );

                        Location particleSpawnLoc = particleSpawnVector.toLocation(world);

                        world.spawnParticle(Particle.FLAME, particleSpawnLoc, 1, 0, 0, 0, 0.02, null, true);

                        double vX = RNG.nextDouble() * horizontalP - horizontalP/2;
                        double vY = RNG.nextDouble() * verticalP;
                        double vZ = RNG.nextDouble() * horizontalP - horizontalP/2;

                        if (Utils.getRandomBoolean(0.2f)) {
                            world.spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, particleSpawnLoc, 0, vX, vY, vZ, 10, null, true);
                        }

                        for (Entity entity : world.getNearbyEntities(particleSpawnLoc, 1,1,1)) {
                            if (entity instanceof LivingEntity && !protectedEntities.contains(entity)) {
                                LivingEntity livingEntity = (LivingEntity) entity;
                                livingEntity.damage(damage);
                                livingEntity.setNoDamageTicks(0);

                                livingEntity.setVelocity(knockbackVector.multiply(1.5));

                                Location loc = livingEntity.getLocation();
                                world.playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1.2f, 2);

                                protectedEntities.add(livingEntity);
                            }
                        }



                    }
                }
            }
        }
    }
}
