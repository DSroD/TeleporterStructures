package cz.cuni.mff.rodd.teleportstructures;

import cz.cuni.mff.rodd.teleportstructures.config.MainConfig;
import org.bukkit.plugin.java.JavaPlugin;

public final class TeleportStructures extends JavaPlugin {

    private MainConfig _pluginConfig;

    @Override
    public void onEnable() {
        //Save default configuration file (won't overwrite if file already exists)
        this.saveDefaultConfig();
        //Read configuration
        _pluginConfig = new MainConfig(this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
