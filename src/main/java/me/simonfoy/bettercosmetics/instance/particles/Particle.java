package me.simonfoy.bettercosmetics.instance.particles;

import me.simonfoy.bettercosmetics.BetterCosmetics;
import me.simonfoy.bettercosmetics.instance.Cosmetic;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class Particle extends Cosmetic {

    private ParticleType type;
    private BukkitTask task;

    public Particle(BetterCosmetics betterCosmetics, Player player, ParticleType type) {
        super(betterCosmetics, player);
        this.type = type;
    }

    @Override
    public void enable() {
        task = Bukkit.getScheduler().runTaskTimer(betterCosmetics, new Runnable() {
            Location location = player.getLocation();

            @Override
            public void run() {
                Location loc = player.getLocation();
                if (loc.getX() != location.getX() || loc.getZ() != location.getZ()) {
                    if (betterCosmetics.isParticleVisibleForPlayer(player.getUniqueId())) {
                        player.spawnParticle(type.getParticle(), player.getLocation(), 5);
                    }

                    for (Player nearbyPlayer : getNearbyPlayers(player, 30)) {
                        if (betterCosmetics.isParticleVisibleForPlayer(nearbyPlayer.getUniqueId())) {
                            nearbyPlayer.spawnParticle(type.getParticle(), player.getLocation(), 5);
                        }
                    }
                }
            }
        }, 0, 1);
    }

    @Override
    public void disable() {
        task.cancel();
    }

    private List<Player> getNearbyPlayers(Player player, double radius) {
        List<Player> nearbyPlayers = new ArrayList<>();
        for (Entity entity : player.getNearbyEntities(radius, radius, radius)) {
            if (entity instanceof Player && !entity.equals(player)) {
                nearbyPlayers.add((Player) entity);
            }
        }
        return nearbyPlayers;
    }

    public ParticleType getType() { return type; }
}
