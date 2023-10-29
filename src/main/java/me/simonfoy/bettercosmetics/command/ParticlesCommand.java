package me.simonfoy.bettercosmetics.command;

import me.simonfoy.bettercosmetics.BetterCosmetics;
import me.simonfoy.bettercosmetics.instance.particles.ParticlesUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ParticlesCommand implements CommandExecutor {

    private BetterCosmetics betterCosmetics;

    public ParticlesCommand(BetterCosmetics betterCosmetics) {
        this.betterCosmetics = betterCosmetics;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (sender instanceof Player) {
            new ParticlesUI(betterCosmetics, (Player) sender);

        }
        return false;
    }
}
