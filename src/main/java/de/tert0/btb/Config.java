package de.tert0.btb;

import java.util.logging.Logger;

public record Config(
        int maxTeamSize,
        boolean autoStart
) {
    static final Config DEFAULT = new Config(2, true);

    static Config fromEnv(Logger logger) throws RuntimeException {
        int maxTeamSize = DEFAULT.maxTeamSize;
        String maxTeamSizeRaw = System.getenv("BTB_MAX_TEAM_SIZE");
        if (maxTeamSizeRaw != null) {
            try {
                maxTeamSize = Integer.parseInt(maxTeamSizeRaw);
            } catch (NumberFormatException e) {
                logger.severe("Invalid value for BTB_MAX_TEAM_SIZE: '" + maxTeamSizeRaw + "'. Falling back to default value.");
            }
        }

        boolean autoStart = DEFAULT.autoStart;
        String autoStartRaw = System.getenv("BTB_AUTO_START");
        if(autoStartRaw != null) {
            if(autoStartRaw.equalsIgnoreCase("true")){
                autoStart = true;
            } else if(autoStartRaw.equalsIgnoreCase("false")){
                autoStart = false;
            } else {
                logger.severe("Invalid BTB_AUTO_START: '" + autoStartRaw + "'. Falling back to default value.");
            }
        }

        return new Config(maxTeamSize, autoStart);
    }
    boolean validate(Logger logger) throws RuntimeException {
        if(maxTeamSize <= 0) {
            logger.severe("Config option maxTeamSize must at least be 1. Falling back to default config.");
            return false;
        }
        return true;
    }
}
