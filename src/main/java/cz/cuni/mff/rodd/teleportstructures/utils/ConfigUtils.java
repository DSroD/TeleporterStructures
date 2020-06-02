package cz.cuni.mff.rodd.teleportstructures.utils;

import cz.cuni.mff.rodd.teleportstructures.TeleportStructures;
import cz.cuni.mff.rodd.teleportstructures.config.MainConfig;
import org.bukkit.Material;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.logging.Level;

public class ConfigUtils {

    public static void loadMainConfig(TeleportStructures plugin) {
        MainConfig config = plugin.getMainConfig();
        //TODO: Check config version against MainConfig static field one, if
        //      versions doesn't check out create backup of config and create default

        if (plugin.getConfig().getInt("configVersion") != MainConfig.configVersion)
            plugin.getLogger().log(Level.WARNING, "Wrong configuration version detected!");

        //TODO: Solution above should be temporary as the right solution is to programatically
        //      set default configuration values and copy defaults if they don't exist in old config.
        //      Configuration values are to be deprecated at least over several config versions.

        //Load basic numerical values
        config.setMaxDistance(plugin.getConfig().getInt("maxDistance"));
        config.setCostPerBlockTraveled(plugin.getConfig().getInt("costPerBlockTraveled"));

        //Load Block Modifiers
        for (String strmat : plugin.getConfig().getConfigurationSection("blockModifiers").getKeys(false)) {
            if (strmat == null) break; //Is this appropriate NPE check in this case?

            Material mat = Material.getMaterial(strmat);
            if (mat == null) {
                plugin.getLogger().log(Level.WARNING, "Wrong material " + strmat + " at blockModifiers" +
                        " section in TeleportStructures config.yml!");
                continue; //BREAK THE WHEEL but don't obviously. Continue the wheel wouldn't sound so good.
            }
            if (plugin.getConfig().isDouble("blockModifiers." + strmat + ".cost")) //Add Cost Modifier
                config.addCostModifier(mat, plugin.getConfig().getDouble("blockModifiers." + strmat + ".cost"));

            if (plugin.getConfig().isDouble("blockModifiers." + strmat + ".distance")) //Add Distance Modifier
                config.addDistanceModifiers(mat, plugin.getConfig().getDouble("blockModifiers." + strmat
                        + ".distance"));

        }

        //Load Fuels
        //This part of code is repeated code. I even repeated comments.
        //I'm so glad this isn't python and I don't have my IDE screaming at me.
        //Now I wish for plugins in python. Fuck. Does that mean I enjoy being screamed at?
        for (String strmat : plugin.getConfig().getConfigurationSection("fuel").getKeys(false)) {
            if (strmat == null) break; //Is this appropriate NPE check in this case?

            Material mat = Material.getMaterial(strmat);
            if (mat == null) {
                plugin.getLogger().log(Level.WARNING, "Wrong material " + strmat + " at fuel" +
                        " section in TeleportStructures config.yml!");
                continue; //BREAK THE WHEEL but don't obviously. Continue the wheel wouldn't sound so good.
            }
            if (plugin.getConfig().isInt("fuel." + strmat + ".fuelValue"))
                config.addFuel(mat, plugin.getConfig().getInt("fuel." + strmat + ".fuelValue"));
        }

    }

    public static void saveConfiguration(MainConfig config, TeleportStructures plugin) {
        //HAHA I should definetly call this asap!
        //      Really this method is not needed yet as there is no means of changing config values programatically,
        //      I will keep this here just for the sake of completeness and add functionality when needed
        throw new NotImplementedException();
    }

}
