package de.tert0.btb;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public enum Team {
    Red("RED", NamedTextColor.RED),
    Blue("BLUE", NamedTextColor.BLUE),
    Green("GREEN", NamedTextColor.GREEN),
    Yellow("YELLOW", NamedTextColor.YELLOW);

    public final String name;
    public final NamedTextColor color;
    Team(String name, NamedTextColor color) {
        this.name = name;
        this.color = color;
    }

    public static Team getByPlayer(Player player) {
        for(Map.Entry<Team, List<Player>> entry : BTB.getPlugin().teams.entrySet()) {
            if(entry.getValue().contains(player)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public Location getSpawnPoint(World world) {
        return switch (this) {
            case Red -> new Location(world, 5, 1, 5);
            case Blue -> new Location(world, 5, 1, -5);
            case Green -> new Location(world, -5, 1, -5);
            case Yellow -> new Location(world, -5, 1, 5);
        };
    }
}
