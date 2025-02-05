package de.tert0.btb.hazards;

import java.util.*;

public class HazardManager {
    private final Map<String, Hazard> hazards = new HashMap<>();

    public void registerHazard(String name, Hazard hazard) {
        hazards.put(name, hazard);
    }

    private Hazard getHazard(String name) {
        return hazards.get(name);
    }

    public boolean hasHazard(String name) {
        return hazards.containsKey(name);
    }

    public boolean isRunning(String name) {
        return getHazard(name).isRunning();
    }

    public boolean startHazard(String name) {
        Hazard hazard = this.getHazard(name);

        if(hazard.isRunning()) {
            return false;
        }

        hazard.start();
        return true;
    }

    public void stopAll() {
        hazards.values().forEach(Hazard::stop); // TODO on game end: call this
    }
}
