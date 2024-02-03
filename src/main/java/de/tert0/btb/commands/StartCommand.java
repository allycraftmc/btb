package de.tert0.btb.commands;

import de.tert0.btb.BTB;
import de.tert0.btb.GameState;
import de.tert0.btb.Team;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class StartCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!commandSender.hasPermission("btb.start")) {
            commandSender.sendMessage(Component.text("Permission denied.", NamedTextColor.RED));
            return false;
        }
        switch (BTB.getPlugin().gameState) {
            case Initialization -> {
                commandSender.sendMessage(Component.text("Not ready! Cannot start game!", NamedTextColor.RED));
                return false;
            }
            case Playing -> {
                commandSender.sendMessage(Component.text("Game is already running!", NamedTextColor.GREEN));
                return false;
            }
            case End -> {
                commandSender.sendMessage(Component.text("Server must be restarted to start a new game!", NamedTextColor.RED));
                return false;
            }
        }

        long usedTeams = BTB.getPlugin().teams.entrySet().stream().filter(entry -> !entry.getValue().isEmpty()).count();
        if(usedTeams < 2) {
            commandSender.sendMessage(Component.text("At least 2 teams are required to start a game", NamedTextColor.RED));
            return false;
        }
        
        if(!BTB.getPlugin().game.start()) {
            commandSender.sendMessage(Component.text("Game is already starting!", NamedTextColor.DARK_RED));
            return true;
        }

        commandSender.sendMessage(Component.text("Successfully started game", NamedTextColor.GREEN));
        return true;
    }
}
