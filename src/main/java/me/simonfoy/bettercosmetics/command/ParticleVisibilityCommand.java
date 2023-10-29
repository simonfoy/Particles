package me.simonfoy.bettercosmetics.command;

import me.simonfoy.bettercosmetics.BetterCosmetics;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ParticleVisibilityCommand implements CommandExecutor {

    private BetterCosmetics betterCosmetics;

    public ParticleVisibilityCommand(BetterCosmetics betterCosmetics) {
        this.betterCosmetics = betterCosmetics;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (command.getName().equalsIgnoreCase("particleshide")) {
                boolean currentVisibility = betterCosmetics.getDatabaseManager().isParticleVisible(player.getUniqueId());
                betterCosmetics.getDatabaseManager().setParticleVisibility(player.getUniqueId(), !currentVisibility);
                player.sendMessage("Your particle visibility has been toggled " + (!currentVisibility ? "on" : "off") + "!");
            }
        }
        return true;
    }
}
