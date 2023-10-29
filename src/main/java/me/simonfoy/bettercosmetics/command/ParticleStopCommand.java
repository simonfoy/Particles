package me.simonfoy.bettercosmetics.command;

import me.simonfoy.bettercosmetics.BetterCosmetics;
import me.simonfoy.bettercosmetics.instance.Cosmetic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.List;

public class ParticleStopCommand implements CommandExecutor {

    private BetterCosmetics betterCosmetics;

    public ParticleStopCommand(BetterCosmetics betterCosmetics) {
        this.betterCosmetics = betterCosmetics;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (betterCosmetics.getActiveCosmetics().containsKey(player.getUniqueId())) {
                List<Cosmetic> active = betterCosmetics.getActiveCosmetics().get(player.getUniqueId());
                Iterator<Cosmetic> itr = active.listIterator();
                while (itr.hasNext()) {
                    Cosmetic cosmetic = itr.next();
                    if (cosmetic instanceof me.simonfoy.bettercosmetics.instance.particles.Particle) {
                        cosmetic.disable();
                        itr.remove();
                    }
                }
                betterCosmetics.getDatabaseManager().setPlayerParticle(player.getUniqueId(), null);
                player.sendMessage("Your particle effect has been stopped!");
            } else {
                player.sendMessage("You don't have any active particle effect!");
            }
        }
        return true;
    }
}