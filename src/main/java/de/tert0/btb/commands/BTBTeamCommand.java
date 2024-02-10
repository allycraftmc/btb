package de.tert0.btb.commands;

import de.tert0.btb.BTB;
import de.tert0.btb.GameState;
import de.tert0.btb.Team;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BTBTeamCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(!(commandSender instanceof Player player)) {
            commandSender.sendMessage(Component.text("command must be run by an player!", NamedTextColor.RED));
            return false;
        }
        if(BTB.getPlugin().gameState != GameState.Lobby) {
            commandSender.sendMessage(Component.text("It is currently not possible to chose a team!", NamedTextColor.RED));
            return false;
        }

        if(args.length != 1) {
            commandSender.sendMessage(Component.text("usage: /" + s + " <team>"));
            return false;
        }

        Team team;
        try {
            team = Team.valueOf(args[0]);
        } catch (IllegalArgumentException e) {
            commandSender.sendMessage(Component.text("invalid team name", NamedTextColor.RED));
            return false;
        }

        if(BTB.getPlugin().teams.get(team).size() == 2) {
            commandSender.sendMessage(Component.text("Team is full!", NamedTextColor.RED));
            return false;
        }

        Team currentTeam = Team.getByPlayer(player);
        if(currentTeam != null) {
            if(currentTeam == team) {
                commandSender.sendMessage(Component.text("You are already in that team!", NamedTextColor.GREEN));
                return true;
            } else {
                BTB.getPlugin().teams.get(currentTeam).remove(player);
            }
        }

        BTB.getPlugin().teams.get(team).add(player);
        commandSender.sendMessage(Component.text("You joined Team ").append(Component.text(team.name, team.color)));
        Component displayName = Component.text(player.getName(), team.color);
        player.displayName(displayName);
        player.playerListName(displayName);

        org.bukkit.scoreboard.Team nTeam = BTB.getPlugin().scoreboard.getTeam(team.name);
        assert nTeam != null;
        nTeam.addPlayer(player);

        BTB.getPlugin().game.try_start();
        return true;
    }
}
