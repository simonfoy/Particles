package me.simonfoy.bettercosmetics;

import me.simonfoy.bettercosmetics.command.ParticleStopCommand;
import me.simonfoy.bettercosmetics.command.ParticleVisibilityCommand;
import me.simonfoy.bettercosmetics.command.ParticlesCommand;
import me.simonfoy.bettercosmetics.instance.Cosmetic;
import me.simonfoy.bettercosmetics.instance.particles.ParticleType;
import me.simonfoy.bettercosmetics.listener.CosmeticListener;
import me.simonfoy.bettercosmetics.manager.DatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public final class BetterCosmetics extends JavaPlugin {

    private DatabaseManager databaseManager;

    private HashMap<UUID, List<Cosmetic>> activeCosmetics = new HashMap<>();
    private List<ParticleType> particleTypes;
    private FileConfiguration particlesConfig;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        databaseManager = new DatabaseManager(
                getConfig().getString("database.host"),
                getConfig().getInt("database.port"),
                getConfig().getString("database.name"),
                getConfig().getString("database.username"),
                getConfig().getString("database.password")
        );

        particlesConfig = getConfig();

        particleTypes = loadParticleTypes();

        if(!databaseManager.establishConnection()) {
            getLogger().severe("Could not establish a database connection! Disabling the plugin...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getCommand("particles").setExecutor(new ParticlesCommand(this));
        getCommand("particleshide").setExecutor(new ParticleVisibilityCommand(this));
        getCommand("particlestop").setExecutor(new ParticleStopCommand(this));

        Bukkit.getPluginManager().registerEvents(new CosmeticListener(this), this);
    }

    private List<ParticleType> loadParticleTypes() {
        List<ParticleType> types = new ArrayList<>();

        if (!particlesConfig.isConfigurationSection("particles")) {
            return types;
        }

        for (String key : particlesConfig.getConfigurationSection("particles").getKeys(false)) {
            String path = "particles." + key;
            String display = particlesConfig.getString(path + ".display");
            Material material = Material.valueOf(particlesConfig.getString(path + ".material"));
            int slot = particlesConfig.getInt(path + ".slot");
            List<String> description = particlesConfig.getStringList(path + ".description");
            String permission = particlesConfig.getString(path + ".permission");
            Particle particle = Particle.valueOf(particlesConfig.getString(path + ".particle"));

            ParticleType type = new ParticleType(key, display, description, material, slot, permission, particle);
            types.add(type);
        }

        return types;
    }

    public List<ParticleType> getAvailableParticleTypes() {
        return particleTypes;
    }


    @Override
    public void onDisable() {
        if (databaseManager != null) {
            databaseManager.closeConnection();
        }
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public HashMap<UUID, List<Cosmetic>> getActiveCosmetics() {
        return activeCosmetics;
    }

    public boolean isParticleVisibleForPlayer(UUID playerUUID) {
        return this.getDatabaseManager().isParticleVisible(playerUUID);
    }
}
