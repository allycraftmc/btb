package de.tert0.btb;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
public class TimerRunnable implements Runnable {
    private final BTB plugin;
    private long secondsElapsed = 0;


    public TimerRunnable(BTB plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        secondsElapsed++;

        long seconds = secondsElapsed % 60;
        long minutes = (secondsElapsed / 60) % 60;
        long hours = (secondsElapsed / 60) / 60;
        Component component = Component.text(String.format("%02d:%02d:%02d", hours, minutes, seconds), NamedTextColor.GOLD);
        org.bukkit.scoreboard.Team tTeam = this.plugin.scoreboard.getTeam("SCOREBOARD_TIMER_VALUE");
        assert tTeam != null;
        tTeam.suffix(component);
    }
}
