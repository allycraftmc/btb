package de.tert0.btb;

import de.tert0.btb.commands.BTBRoleCommand;
import de.tert0.btb.commands.BTBTeamCommand;
import de.tert0.btb.commands.StartCommand;
import de.tert0.btb.listeners.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.title.Title;
import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.*;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

public final class BTB extends JavaPlugin {
    private static BTB plugin;
    public Game game;
    public GameState gameState = GameState.Initialization;

    public World lobbyWorld;
    public World btbWorld;
    Logger logger;

    public Map<Team, List<Player>> teams;
    public Map<UUID, Role> roles;
    public List<Player> deadPlayers;
    public Map<Team, Boolean> teamStates;
    public Map<Team, Inventory> teamChests;
    public CooldownManager cooldownManager;

    public Scoreboard scoreboard;
    public Objective objective;

    public TimerRunnable timerRunnable;
    public BukkitTask timerTask;
    public BukkitTask cooldownShowTask;

    @Override
    public void onEnable() {
        plugin = this;
        logger = this.getLogger();

        btbWorld = Bukkit.getWorld("btb");

        if(btbWorld != null) {
            Bukkit.unloadWorld(btbWorld, false);
            logger.info("Unloaded World");
        } else {
            logger.info("World does not exist");
        }

        File worldFolder = new File("btb");
        if(worldFolder.exists()) {
            try {
                FileUtils.deleteDirectory(worldFolder);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            logger.info("Deleted world folder");
        } else {
            logger.info("World folder does not exist");
        }

        try {
            FileUtils.copyDirectory(new File("template"), new File("btb"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        logger.info("Copied template into world");

        btbWorld = new WorldCreator("btb").createWorld();
        assert btbWorld != null;
        btbWorld.save();
        logger.info("Saved new world");

        lobbyWorld = Bukkit.getWorld("world");
        assert lobbyWorld != null;

        lobbyWorld.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        lobbyWorld.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        lobbyWorld.setGameRule(GameRule.SPAWN_RADIUS, 0);
        lobbyWorld.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        lobbyWorld.setTime(1000);
        lobbyWorld.setStorm(false);
        lobbyWorld.setClearWeatherDuration(0);
        lobbyWorld.setPVP(false);

        btbWorld.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        btbWorld.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        btbWorld.setGameRule(GameRule.SPAWN_RADIUS, 0);
        btbWorld.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        btbWorld.setGameRule(GameRule.KEEP_INVENTORY, false);
        btbWorld.setTime(1000);
        btbWorld.setStorm(false);
        btbWorld.setClearWeatherDuration(0);
        btbWorld.setPVP(true);

        teams = new HashMap<>();
        for(Team team : Team.values()) {
            teams.put(team, new ArrayList<>());
        }
        roles = new HashMap<>();
        deadPlayers = new ArrayList<>();
        teamStates = new HashMap<>();
        for(Team team : Team.values()) {
            teams.put(team, new ArrayList<>());
        }

        teamChests = new HashMap<>();
        for(Team team : Team.values()) {
            teamChests.put(team, Bukkit.createInventory(null, 9*4, Component.text("TeamChest", team.color)));
        }
        cooldownManager = new CooldownManager();


        getServer().getPluginManager().registerEvents(new HandlePlayerLogin(), this);
        getServer().getPluginManager().registerEvents(new HandlePlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new HandlePlayerLeave(), this);
        getServer().getPluginManager().registerEvents(new HandlePlayerDamage(), this);
        getServer().getPluginManager().registerEvents(new HandleBlockBreak(), this);
        getServer().getPluginManager().registerEvents(new HandleBlockPlace(), this);
        getServer().getPluginManager().registerEvents(new HandlePlayerInteractBlock(), this);
        getServer().getPluginManager().registerEvents(new HandlePlayerDeath(), this);
        getServer().getPluginManager().registerEvents(new HandlePlayerRespawn(), this);
        getServer().getPluginManager().registerEvents(new HandleSmithingInventory(), this);
        getServer().getPluginManager().registerEvents(new HandleEnderChest(), this);
        getServer().getPluginManager().registerEvents(new HandlePlayerInteract(), this);
        Objects.requireNonNull(getCommand("btbteam")).setExecutor(new BTBTeamCommand());
        Objects.requireNonNull(getCommand("btbrole")).setExecutor(new BTBRoleCommand());
        Objects.requireNonNull(getCommand("start")).setExecutor(new StartCommand());

        getServer().getRecipesFor(new ItemStack(Material.SMITHING_TABLE)).forEach(recipe -> {
            if(recipe instanceof CraftingRecipe r) {
                getServer().removeRecipe(r.getKey());
            }
        });

        initializeScoreboard();

        game = new Game(this);

        logger.info("Server is ready for connections");
        gameState = GameState.Lobby;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static BTB getPlugin() {
        return plugin;
    }

    void initializeScoreboard() {
        ScoreboardManager scoreboardManager = this.getServer().getScoreboardManager();
        scoreboard = scoreboardManager.getNewScoreboard();
        objective = scoreboard.registerNewObjective("teams", Criteria.DUMMY, Component.text("AllyCraft - BTB", NamedTextColor.YELLOW));

        org.bukkit.scoreboard.Team emptyAboveTeam = scoreboard.registerNewTeam("SCOREBOARD_EMPTY_ABOVE");
        emptyAboveTeam.addEntry(" ");
        objective.getScore(" ").setScore(Team.values().length + 4);

        org.bukkit.scoreboard.Team timerNameTeam = scoreboard.registerNewTeam("SCOREBOARD_TIMER_NAME");
        timerNameTeam.addEntry("Zeit:");
        objective.getScore("Zeit:").setScore(Team.values().length + 3);

        org.bukkit.scoreboard.Team timerValueTeam = scoreboard.registerNewTeam("SCOREBOARD_TIMER_VALUE");
        timerValueTeam.addEntry(LegacyComponentSerializer.legacySection().serialize(Component.text("", NamedTextColor.WHITE).append(Component.text("", NamedTextColor.RED))));
        timerValueTeam.suffix(Component.text("??:??:??"));
        objective.getScore(LegacyComponentSerializer.legacySection().serialize(Component.text("", NamedTextColor.WHITE).append(Component.text("", NamedTextColor.RED)))).setScore(Team.values().length + 2);

        org.bukkit.scoreboard.Team emptyBelowTeam = scoreboard.registerNewTeam("SCOREBOARD_EMPTY_BELOW");
        emptyBelowTeam.addEntry("  ");
        objective.getScore("  ").setScore(Team.values().length + 1);

        int i = Team.values().length;
        for(Team team : Team.values()) {
            i--;
            Component name = Component.text(team.name, team.color)
                    .append(Component.text(": "));
            org.bukkit.scoreboard.Team sTeam = scoreboard.registerNewTeam(team.name + "_SIDEBAR");
            sTeam.addEntry(LegacyComponentSerializer.legacySection().serialize(name));
            sTeam.suffix(Component.text("?"));
            objective.getScore(LegacyComponentSerializer.legacySection().serialize(name)).setScore(i);

            org.bukkit.scoreboard.Team nTeam = scoreboard.registerNewTeam(team.name);
            nTeam.color(team.color);
            nTeam.setAllowFriendlyFire(false);
            nTeam.setCanSeeFriendlyInvisibles(true);
        }
    }

    public void updateScoreboard() {
        for(Team team : Team.values()) {
            org.bukkit.scoreboard.Team sTeam = scoreboard.getTeam(team.name + "_SIDEBAR");
            assert sTeam != null;
            sTeam.suffix(
                    Component.text(
                            (teamStates.get(team) ? "✔" : "❌"),
                            teamStates.get(team) ? NamedTextColor.GREEN : NamedTextColor.RED)
            );
        }
    }

    public void updateTeamStates() {
        for(Team team : this.teams.keySet()) {
            long playerAlive = this.teams.get(team).stream()
                    .filter(p -> !this.deadPlayers.contains(p))
                    .count();
            boolean state = playerAlive > 0;
            if(!state && this.teamStates.containsKey(team) && this.teamStates.get(team)) {
                Bukkit.broadcast(
                        Component.text("Team ")
                                .append(Component.text(team.name, team.color))
                                .append(Component.text(" ist tot!"))
                );
            }
            this.teamStates.put(team, state);
        }
        updateScoreboard();

        List<Team> teamsAlive = this.teamStates.entrySet().stream()
                .filter(Map.Entry::getValue).map(Map.Entry::getKey).toList();
        switch (teamsAlive.size()) {
            case 0 -> {
                getServer().broadcast(Component.text("Das Spiel ist fertig, da kein Team mehr lebt!"));
                btbWorld.getPlayers().forEach(p -> p.setGameMode(GameMode.SPECTATOR));
                gameState = GameState.End;
            }
            case 1 -> {
                Component message = Component.text("Team ")
                        .append(Component.text(teamsAlive.getFirst().name, teamsAlive.getFirst().color))
                        .append(Component.text(" hat gewonnen!"));
                getServer().broadcast(message);
                getServer().showTitle(Title.title(message, Component.empty()));
                btbWorld.getPlayers().forEach(p -> p.setGameMode(GameMode.SPECTATOR));
                gameState = GameState.End;
            }
        }

        if(gameState == GameState.End) {
            timerTask.cancel();
            cooldownShowTask.cancel();
        }
    }

    public void updatePlayerName(Player player) {
        Team team = Team.getByPlayer(player);
        Component name = Component.text(player.getName(),team  == null ? NamedTextColor.WHITE : team.color)
                .append(Role.getByPlayer(player).suffix);
        player.displayName(name);
        player.playerListName(name);
    }
}
