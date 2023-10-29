package me.simonfoy.bettercosmetics.instance.particles;

import org.bukkit.Material;
import org.bukkit.Particle;

import java.util.Collections;
import java.util.List;

public class ParticleType {

    private String name;
    private String display;
    private List<String> description;
    private Material material;
    private int slot;
    private String permission;
    private Particle particle;

    public ParticleType(String name, String display, List<String> description, Material material, int slot, String permission, Particle particle) {
        this.name = name;
        this.display = display;
        this.description = description;
        this.material = material;
        this.slot = slot;
        this.permission = permission;
        this.particle = particle;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getDisplay() {
        return display;
    }

    public List<String> getDescription() {
        return description;
    }

    public Material getMaterial() {
        return material;
    }

    public int getSlot() {
        return slot;
    }

    public String getPermission() {
        return permission;
    }

    public Particle getParticle() {
        return particle;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public void setDescription(List<String> description) {
        this.description = description;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public void setParticle(Particle particle) {
        this.particle = particle;
    }
}
