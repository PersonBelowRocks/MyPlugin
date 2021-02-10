package com.PersonBelowRocks.myplugin.events;

import com.PersonBelowRocks.myplugin.items.ItemManager;
import com.PersonBelowRocks.myplugin.util.Utils;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.bukkit.*;
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

    // Material.GRASS, Material.TALL_GRASS, Material.WATER, Material.LAVA, Material.SUNFLOWER, Material.PEONY, Material.ROSE_BUSH
    private static final HashSet<Material> nonSolids = new HashSet<>();

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
        nonSolids.add(Material.GRASS);
        nonSolids.add(Material.TALL_GRASS);
        nonSolids.add(Material.WATER);
        nonSolids.add(Material.LAVA);
        nonSolids.add(Material.SUNFLOWER);
        nonSolids.add(Material.PEONY);
        nonSolids.add(Material.ROSE_BUSH);
        nonSolids.add(Material.LILAC);
        nonSolids.add(Material.LARGE_FERN);
        nonSolids.add(Material.AIR);

        Player invoker = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getItem() != null) {
                if (event.getItem().getItemMeta().equals(ItemManager.wand.getItemMeta())) {
                    long then = System.currentTimeMillis();

                    boolean broken = false;

                    double scalar = 0.5;
                    double radius = (scalar + (scalarIncrement*maxIterations))*maxIterations;
                    // todo: improve performance

                    Location invokerLocation = invoker.getLocation();
                    Location blockLookingAt = invoker.getTargetBlock(nonSolids, (int) Math.round(radius)).getLocation();
                    World world = invoker.getWorld();

                    double pitch = (invokerLocation.getPitch() + 90) * RADIANS_CONVERSION_FACTOR;
                    double yaw  = (invokerLocation.getYaw() + 90)  * RADIANS_CONVERSION_FACTOR;

                    double x = Math.sin(pitch) * Math.cos(yaw);
                    double y = Math.sin(pitch) * Math.sin(yaw);
                    double z = Math.cos(pitch);

                    final Vector3D invokerDirectionFacingVector = new Vector3D(x, z, y);
                    Vector3D invokerLocationVector = new Vector3D(invokerLocation.getX(), invokerLocation.getY(), invokerLocation.getZ());

                    final Vector knockbackVector = new Vector(
                            x,
                            z,
                            y
                    );

                    HashSet<LivingEntity> nearbyEntities = new HashSet<>();
                    for (Entity entity : world.getNearbyEntities(invokerLocation, radius, radius, radius)) {
                        if (entity instanceof LivingEntity && entity != invoker) {
                            nearbyEntities.add((LivingEntity) entity);
                        }
                    }

                    HashSet<LivingEntity> protectedEntities = new HashSet<>();
                    protectedEntities.add(invoker.getPlayer());

                    world.playSound(invokerLocation, Sound.BLOCK_SHROOMLIGHT_PLACE, 2, 2);
                    world.playSound(invokerLocation, Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, 0.5f, 2);

                    Location particleSpawnLoc = invokerLocation;

                    int count = 0;
                    for (; count < maxIterations; count++) {
                        scalar += scalarIncrement;

                        invokerLocationVector = invokerLocationVector.add(invokerDirectionFacingVector.scalarMultiply(scalar));

                        Vector particleSpawnVector = new Vector(
                                invokerLocationVector.getX(),
                                invokerLocationVector.getY()+beamVerticalModifier,
                                invokerLocationVector.getZ()
                        );

                        particleSpawnLoc = particleSpawnVector.toLocation(world);

                        if (particleSpawnLoc.distanceSquared(blockLookingAt) < 1.5) {
                            broken = true;
                            break;
                        }

                        world.spawnParticle(Particle.FLAME, particleSpawnLoc, 1, 0, 0, 0, 0.02, null, true);

                        double vX = RNG.nextDouble() * horizontalP - horizontalP/2;
                        double vY = RNG.nextDouble() * verticalP;
                        double vZ = RNG.nextDouble() * horizontalP - horizontalP/2;

                        if (Utils.getRandomBoolean(0.2f)) {
                            world.spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, particleSpawnLoc, 0, vX, vY, vZ, 10, null, true);
                        }

                        //getServer().getConsoleSender().sendMessage("ARE YOU HERE?");
                        //getServer().getConsoleSender().sendMessage(particleSpawnLoc.toString());
                        for (LivingEntity entity : nearbyEntities) {
                            if (particleSpawnLoc.distanceSquared(entity.getLocation()) < 2.0 && !protectedEntities.contains(entity)) {
                                entity.damage(damage);
                                entity.setNoDamageTicks(0);
                                //getServer().getConsoleSender().sendMessage("OR HERE?");
                                entity.setVelocity(knockbackVector.multiply(1.5));

                                Location loc = entity.getLocation();
                                world.playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1.2f, 2);

                                protectedEntities.add(entity);
                            }
                        }
                    }

                    if (broken) {
                        world.createExplosion(particleSpawnLoc, (maxIterations - count) * 0.10f);
                        world.spawnParticle(Particle.FIREWORKS_SPARK, particleSpawnLoc, 32, 0.2, 0.2, 0.2, 0.5);
                    }

                    long now = System.currentTimeMillis();
                    getServer().getConsoleSender().sendMessage(""+ (now-then));
                }
            }
        }
    }
}
