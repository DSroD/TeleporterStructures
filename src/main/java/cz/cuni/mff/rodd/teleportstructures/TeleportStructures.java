package cz.cuni.mff.rodd.teleportstructures;

import cz.cuni.mff.rodd.teleportstructures.commands.GenerateCommand;
import cz.cuni.mff.rodd.teleportstructures.config.MainConfig;
import cz.cuni.mff.rodd.teleportstructures.handlers.PlayerSneakHandler;
import cz.cuni.mff.rodd.teleportstructures.handlers.SignHandler;
import cz.cuni.mff.rodd.teleportstructures.storage.TeleporterData;
import cz.cuni.mff.rodd.teleportstructures.utils.ConfigUtils;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.logging.Level;

public final class TeleportStructures extends JavaPlugin {

    private MainConfig _pluginConfig;

    private TeleporterData _teleportData;

    private BukkitTask _autosaveTask;

    @Override
    public void onEnable() {
        //Save default configuration file (won't overwrite if file already exists)
        this.saveDefaultConfig();
        //Read configuration
        _pluginConfig = new MainConfig();
        ConfigUtils.loadMainConfig(this);

        //Initialize teleporter data
        _teleportData = new TeleporterData(this);
        _teleportData.loadTeleporterDataFromFile();

        //Register events
        Bukkit.getPluginManager().registerEvents(new PlayerSneakHandler(this), this);
        Bukkit.getPluginManager().registerEvents(new SignHandler(this), this);

        _autosaveTask = Bukkit.getScheduler().runTaskTimerAsynchronously(this, new Runnable(){
            @Override
            public void run() {
                _teleportData.saveTeleporterDataToFile();
                Bukkit.getLogger().log(Level.INFO, "TeleportData autosave!");
            }
        }, 1000L, 2000L);

        this.getCommand("telestruct").setExecutor(new GenerateCommand());
        
    }

    

    

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        _teleportData.saveTeleporterDataToFile();
        _autosaveTask.cancel();

    }

    public MainConfig getMainConfig() {
        return _pluginConfig;
    }

    public TeleporterData getTeleporterData() {
        return _teleportData;
    }

    public void unregisterEvents(Listener listener) {
         HandlerList.unregisterAll(listener);
    }
}
