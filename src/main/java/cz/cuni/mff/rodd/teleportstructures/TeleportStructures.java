package cz.cuni.mff.rodd.teleportstructures;

import cz.cuni.mff.rodd.teleportstructures.config.MainConfig;
import cz.cuni.mff.rodd.teleportstructures.handlers.FuelMenu;
import cz.cuni.mff.rodd.teleportstructures.handlers.PlayerSneakHandler;
import cz.cuni.mff.rodd.teleportstructures.handlers.SignHandler;
import cz.cuni.mff.rodd.teleportstructures.storage.TeleporterData;
import cz.cuni.mff.rodd.teleportstructures.teleports.TeleportGroup;
import cz.cuni.mff.rodd.teleportstructures.utils.ConfigUtils;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class TeleportStructures extends JavaPlugin {

    private MainConfig _pluginConfig;

    private TeleporterData _teleportData;

    @Override
    public void onEnable() {
        //Save default configuration file (won't overwrite if file already exists)
        this.saveDefaultConfig();
        //Read configuration
        _pluginConfig = new MainConfig(this);
        ConfigUtils.loadMainConfig(this);

        //Initialize teleporter data
        _teleportData = new TeleporterData(this);
        _teleportData.loadTeleporterDataFromFile();

        //Register events
        Bukkit.getPluginManager().registerEvents(new PlayerSneakHandler(this), this);
        Bukkit.getPluginManager().registerEvents(new SignHandler(this), this);
        
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

    public TeleporterData getTeleporterData() {
        return _teleportData;
    }

    public void underigsterEvents(Listener listener) {
         HandlerList.unregisterAll(listener);
    }
}
