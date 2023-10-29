package me.simonfoy.bettercosmetics.listener;

import me.simonfoy.bettercosmetics.BetterCosmetics;
import me.simonfoy.bettercosmetics.instance.Cosmetic;
import me.simonfoy.bettercosmetics.instance.particles.Particle;
import me.simonfoy.bettercosmetics.instance.particles.ParticleType;
import me.simonfoy.bettercosmetics.instance.particles.ParticlesUI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CosmeticListener implements Listener {

    private BetterCosmetics betterCosmetics;

    public CosmeticListener(BetterCosmetics betterCosmetics) {
        this.betterCosmetics = betterCosmetics;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked();

        if (!event.getView().getTitle().endsWith("Particles")) return;

        if (event.getCurrentItem() == null) return;

        ParticleType selectedType = findParticleTypeByName(event.getCurrentItem().getItemMeta().getLocalizedName());

        if (selectedType == null || !player.hasPermission(selectedType.getPermission())) {
            player.sendMessage(ChatColor.RED + "You need the " + selectedType.getPermission() + " permission to use this particle!");
            event.setCancelled(true);
            return;
        }

        List<Cosmetic> activeCosmetics = betterCosmetics.getActiveCosmetics().getOrDefault(player.getUniqueId(), new ArrayList<>());

        boolean isParticleAlreadyEnabled = false;

        Iterator<Cosmetic> iterator = activeCosmetics.iterator();
        while (iterator.hasNext()) {
            Cosmetic cosmetic = iterator.next();

            if (cosmetic instanceof Particle) {
                Particle particle = (Particle) cosmetic;

                if (particle.getType() == selectedType) {
                    particle.disable();
                    iterator.remove();

                    player.sendMessage(ChatColor.RED + "You disabled " + selectedType.getDisplay() + "!");
                    player.closeInventory();
                    isParticleAlreadyEnabled = true;
                    betterCosmetics.getDatabaseManager().setPlayerParticle(player.getUniqueId(), null);
                    break;
                } else {
                    particle.disable();
                    iterator.remove();
                }
            }
        }

        if (!isParticleAlreadyEnabled) {
            Particle newParticle = new Particle(betterCosmetics, player, selectedType);
            newParticle.enable();
            activeCosmetics.add(newParticle);
            betterCosmetics.getActiveCosmetics().put(player.getUniqueId(), activeCosmetics);

            player.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + "You enabled " + selectedType.getDisplay() + "!");
            betterCosmetics.getDatabaseManager().setPlayerParticle(player.getUniqueId(), selectedType);
        }

        player.closeInventory();
    }

    private ParticleType findParticleTypeByName(String name) {
        for (ParticleType type : betterCosmetics.getAvailableParticleTypes()) {
            if (type.getName().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        activateParticleForPlayer(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {

        Player player = event.getPlayer();

        if (betterCosmetics.getActiveCosmetics().containsKey(player.getUniqueId())) {
            for (Cosmetic cosmetic : betterCosmetics.getActiveCosmetics().get(player.getUniqueId())) {
                cosmetic.disable();
            }
            betterCosmetics.getActiveCosmetics().remove(player.getUniqueId());
        }
    }

    private void activateParticleForPlayer(Player player) {
        String particleName = betterCosmetics.getDatabaseManager().getPlayerParticle(player.getUniqueId());
        if (particleName != null) {
            ParticleType type = findParticleTypeByName(particleName);
            if (type != null) {
                Particle particle = new Particle(betterCosmetics, player, type);
                particle.enable();

                List<Cosmetic> activeCosmetics = betterCosmetics.getActiveCosmetics().getOrDefault(player.getUniqueId(), new ArrayList<>());
                activeCosmetics.add(particle);
                betterCosmetics.getActiveCosmetics().put(player.getUniqueId(), activeCosmetics);

                player.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + "Your " + type.getDisplay() + " particle has been enabled!");
            }
        }
    }
}
