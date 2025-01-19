package de.tert0.btb;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public enum Role {
    Knight("KNIGHT"),
    Healer("HEALER"),
    Tank("TANK");

    private static final Random random = new Random();
    public final String name;
    Role(String name) {
        this.name = name;
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
        }
        return role;
    }
}
