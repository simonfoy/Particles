package me.simonfoy.bettercosmetics.instance.particles;

import me.simonfoy.bettercosmetics.BetterCosmetics;
import me.simonfoy.bettercosmetics.instance.Cosmetic;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ParticlesUI {

    public ParticlesUI(BetterCosmetics betterCosmetics, Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, ChatColor.RED.toString() + ChatColor.BOLD + "Particles");

        List<ParticleType> active = new ArrayList<>();
        if (betterCosmetics.getActiveCosmetics().containsKey(player.getUniqueId())) {
            for (Cosmetic cosmetic : betterCosmetics.getActiveCosmetics().get(player.getUniqueId())) {
                if (cosmetic instanceof Particle) {
                    active.add(((Particle) cosmetic).getType());
                    break;
                }
            }
        }
        for (ParticleType type : betterCosmetics.getAvailableParticleTypes()) {
            ItemStack itemStack = new ItemStack(type.getMaterial());
            ItemMeta itemMeta = itemStack.getItemMeta();

            itemMeta.setDisplayName(type.getDisplay() + " " + (active.contains(type) ? ChatColor.RED + "[CLICK TO DISABLE]" : ChatColor.GREEN + "[CLICK TO ENABLE]"));
            itemMeta.setLore(type.getDescription());
            itemMeta.setLocalizedName(type.getName());
            itemStack.setItemMeta(itemMeta);

            gui.setItem(type.getSlot(), itemStack);
        }
        player.openInventory(gui);
    }
}
