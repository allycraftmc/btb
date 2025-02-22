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

import java.util.Arrays;
import java.util.stream.Collectors;

public class BTBTeamCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage(Component.text("command must be run by an player!", NamedTextColor.RED));
            return false;
        }


        if(args.length != 1 && (args.length != 2)) {
            commandSender.sendMessage(Component.text("usage: /" + label + " <team> [player]"));
            return false;
        }

        Team team;
        try {
            team = Team.valueOf(args[0]);
        } catch (IllegalArgumentException e) {
            commandSender.sendMessage(
                    Component.text("invalid team name (", NamedTextColor.RED)
                            .append(
                                    Component.text(
                                            Arrays.stream(Team.values()).map(Enum::toString).collect(Collectors.joining(", ")),
                                            NamedTextColor.GOLD
                                    )
                            )
                            .append(Component.text(")", NamedTextColor.RED))
            );
            return false;
        }

        Player target;
        if(args.length == 2) {
            target = Bukkit.getPlayer(args[1]);
            if(target == null) {
                commandSender.sendMessage(
                        Component.text("Could not find player: ", NamedTextColor.RED)
                                .append(Component.text(args[1], NamedTextColor.GOLD))
                );
                return false;
            }
            if(!commandSender.equals(target) && !commandSender.hasPermission("btb.team.admin")) {
                commandSender.sendMessage(Component.text("Permission denied", NamedTextColor.RED));
                return false;
            }
        } else {
            target = (Player) commandSender;
        }

        Team currentTeam = Team.getByPlayer(target);
        if(currentTeam != null) {
            if (currentTeam == team) {
                commandSender.sendMessage(Component.text("You are already in that team!", NamedTextColor.GREEN));
                return true;
            }
        }

        if(BTB.getPlugin().gameState != GameState.Lobby) {
            // Spectators can join a team during the game
            if(!BTB.getPlugin().game.isSpectator(target) || BTB.getPlugin().gameState != GameState.Playing) {
                commandSender.sendMessage(Component.text("It is currently not possible to chose a team!", NamedTextColor.RED));
                return false;
            }
        }

        if(BTB.getPlugin().teams.get(team).size() >= BTB.getPlugin().config.maxTeamSize()) {
            commandSender.sendMessage(Component.text("Team is full!", NamedTextColor.RED));
            return false;
        }

        if(BTB.getPlugin().game.isSpectator(target)) {
            if(!BTB.getPlugin().teamStates.get(team)) {
                commandSender.sendMessage(Component.text("The team is not in the game!", NamedTextColor.RED));
                return false;
            }
        }

        if(currentTeam != null) BTB.getPlugin().teams.get(currentTeam).remove(target);
        BTB.getPlugin().teams.get(team).add(target);
        BTB.getPlugin().updatePlayerName(target);

        // Note: Player does not need to be removed from the previous team, as Minecraft takes care of that.
        // (Each Player/Entity can be at most in one team)
        org.bukkit.scoreboard.Team nTeam = BTB.getPlugin().scoreboard.getTeam(team.name);
        assert nTeam != null;
        nTeam.addPlayer(target);

        if(BTB.getPlugin().game.isSpectator(target)) {
            BTB.getPlugin().game.removeSpectator(target);
            BTB.getPlugin().game.resetPlayer(target);
            BTB.getPlugin().game.setupPlayer(target);
            target.teleport(team.getSpawnPoint(BTB.getPlugin().btbWorld));
        }

        commandSender.sendMessage(Component.text("You joined Team ").append(Component.text(team.name, team.color)));
        BTB.getPlugin().game.try_start();
        return true;
    }
}
