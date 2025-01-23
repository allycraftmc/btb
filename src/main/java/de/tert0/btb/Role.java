package de.tert0.btb;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public enum Role {
    Knight("KNIGHT", Component.text(" [K]", NamedTextColor.DARK_RED), TrimMaterial.REDSTONE, TrimPattern.SENTRY),
    Healer("HEALER", Component.text(" [H]", NamedTextColor.DARK_GREEN), TrimMaterial.EMERALD, TrimPattern.TIDE),
    Tank("TANK", Component.text(" [T]", NamedTextColor.DARK_BLUE), TrimMaterial.LAPIS, TrimPattern.SILENCE);

    private static final Random random = new Random();
    public final String name;
    public final Component suffix;
    public final TrimMaterial trimMaterial;
    public final TrimPattern trimPattern;
    Role(String name, Component suffix, TrimMaterial trimMaterial, TrimPattern trimPattern) {
        this.name = name;
        this.suffix = suffix;
        this.trimMaterial = trimMaterial;
        this.trimPattern = trimPattern;
    }

    public static Role getRandomRole() {
        Role[] values = Role.values();
        return values[random.nextInt(values.length)];
    }

    public static @NotNull Role getByPlayer(Player player) {
        Role role = BTB.getPlugin().roles.get(player.getUniqueId());
        if(role == null) {
            role = getRandomRole();
            BTB.getPlugin().roles.put(player.getUniqueId(), role);
            BTB.getPlugin().updatePlayerName(player);
        }
        return role;
    }
}
