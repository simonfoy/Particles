package me.simonfoy.bettercosmetics.instance;

import me.simonfoy.bettercosmetics.BetterCosmetics;
import org.bukkit.entity.Player;

public abstract class Cosmetic {

    protected BetterCosmetics betterCosmetics;
    protected Player player;

    public Cosmetic(BetterCosmetics betterCosmetics, Player player) {
        this.betterCosmetics = betterCosmetics;
        this.player = player;
    }

    public abstract void enable();

    public abstract void disable();


}
