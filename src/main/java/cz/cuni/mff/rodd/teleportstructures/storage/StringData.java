package cz.cuni.mff.rodd.teleportstructures.storage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import cz.cuni.mff.rodd.teleportstructures.TeleportStructures;

public class StringData {

    private final TeleportStructures _plugin;

    private String notEnoughFuel;
    public String getNotEnoughFuel() {
        return notEnoughFuel;
    }

    private String teleporterAtDestinationBroken;
    public String getTeleporterAtDestinationBroken() {
        return teleporterAtDestinationBroken;
    }

    private String teleporterHereBroken;
    public String getTeleporterHereBroken() {
        return teleporterHereBroken;
    }
    private String fuel;
    public String getFuel() {
        return fuel;
    }

    private String fuelCost;
    public String getFuelCost() {
        return fuelCost;
    }

    private String currentFuel;
    public String getCurrentFuel() {
        return currentFuel;
    }

    private String maxFuel;
    public String getMaxFuel() {
        return maxFuel;
    }
    
    private String costModifier;
    public String getCostModifier() {
        return costModifier;
    }

    private String distanceModifier;
    public String getDistanceModifier() {
        return distanceModifier;
    }

    private String fuelInvName;
    public String getFuelInvName() {
        return fuelInvName;
    }

    private String maxDistance;
    public String getMaxDistance() {
        return maxDistance;
    }

    private String notOnTeleporter;
    public String getNotOnTeleporter() {
        return notOnTeleporter;
    }

    private String notAllowed;
    
    public String getNotAllowed() {
        return notAllowed;
    }

    public final List<String> signWrongName;
    public final List<String> signNameInUse;
    public final List<String> signInvalidStructure;
    public final List<String> signGroupOwnerMismatch;

    private FileConfiguration _strings = null;
    private File _stringsFile = null;

    public StringData(TeleportStructures plugin) {
        _plugin = plugin;
        signWrongName = new ArrayList<String>();
        signNameInUse= new ArrayList<String>();
        signInvalidStructure = new ArrayList<String>();
        signGroupOwnerMismatch = new ArrayList<String>();

        if(_stringsFile == null) {
            _stringsFile = new File(_plugin.getDataFolder(), "strings.yml");
        }
        if(!_stringsFile.exists()) {
            _plugin.saveResource("strings.yml", false);
        }

        _strings = new YamlConfiguration();
        try {
            _strings.load(_stringsFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void loadStrings() {
        notEnoughFuel = _strings.getString("notEnoughFuel", "Not enough fuel!");
        teleporterAtDestinationBroken = _strings.getString("teleporterAtDestinationBroken", "Teleport at chosen destination is broken!");
        teleporterHereBroken = _strings.getString("teleporterHereBroken", "Teleport is broken!");
        fuel = _strings.getString("fuel", "Fuel");
        fuelCost = _strings.getString("fuelCost", "Fuel cost");
        currentFuel = _strings.getString("currentFuel", "Current fuel");
        maxFuel = _strings.getString("maxFuel", "Maximum fuel");
        costModifier = _strings.getString("costModifier", "Cost modifier");
        distanceModifier = _strings.getString("distanceModifier", "Distance modifier");
        maxDistance = _strings.getString("maxDistance", "Maximal distance");
        fuelInvName = _strings.getString("fuelInvName", "Add Fuel");
        notAllowed = _strings.getString("notAllowed", "You are not allowed to do this.");
        notOnTeleporter = _strings.getString("notOnTeleporter", "You are not standing on any teleporter");
        


        signWrongName.clear();
        signWrongName.addAll(_strings.getStringList("signWrongName"));

        signNameInUse.clear();
        signNameInUse.addAll(_strings.getStringList("signNameInUse"));

        signInvalidStructure.clear();
        signInvalidStructure.addAll(_strings.getStringList("signInvalidStructure"));

        signGroupOwnerMismatch.clear();
        signGroupOwnerMismatch.addAll(_strings.getStringList("signGroupOwnerMismatch"));
    }
    
}
