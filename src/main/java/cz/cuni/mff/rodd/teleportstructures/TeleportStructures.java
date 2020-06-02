package cz.cuni.mff.rodd.teleportstructures;

import cz.cuni.mff.rodd.teleportstructures.config.MainConfig;
import cz.cuni.mff.rodd.teleportstructures.teleports.TeleportGroup;
import cz.cuni.mff.rodd.teleportstructures.utils.ConfigUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class TeleportStructures extends JavaPlugin {

    private MainConfig _pluginConfig;

    private List<TeleportGroup> _teleportGroups;

    @Override
    public void onEnable() {
        //Save default configuration file (won't overwrite if file already exists)
        this.saveDefaultConfig();
        //Read configuration
        _pluginConfig = new MainConfig(this);
        ConfigUtils.loadMainConfig(this);

        //Initialize array of teleport groups
        _teleportGroups = new ArrayList<>();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        //TODO: All data should be saved here (autosave is also necessary as we can't count on server being
        //      gracefuly shut down every time, add repeating task?)
    }

    public MainConfig getMainConfig() {
        return _pluginConfig;
    }
}
