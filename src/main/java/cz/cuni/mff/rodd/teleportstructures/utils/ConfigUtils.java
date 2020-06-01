package cz.cuni.mff.rodd.teleportstructures.utils;

import cz.cuni.mff.rodd.teleportstructures.TeleportStructures;
import cz.cuni.mff.rodd.teleportstructures.config.MainConfig;

public class ConfigUtils {

    public static void loadMainConfig(MainConfig config, TeleportStructures plugin) {
        //TODO: Check config version against MainConfig static field one, if
        //      versions doesn't check out create backup of config and create default

        //TODO: Solution above should be temporary as the right solution is to programatically
        //      set default configuration values and copy defaults if they don't exist in old config.
        //      Configuration values are to be deprecated at least over several config versions.

        //Load basic numerical values
        config.setMaxDistance(plugin.getConfig().getInt("maxDistance"));
        config.setCostPerBlockTraveled(plugin.getConfig().getInt("costPerBlockTraveled"));

        //Load Block Modifiers


    }

}
