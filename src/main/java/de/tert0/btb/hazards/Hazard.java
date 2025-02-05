package de.tert0.btb.hazards;

public interface Hazard {
    void start();

    boolean isRunning();

    void stop();
}
