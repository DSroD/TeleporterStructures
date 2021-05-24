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

    public String notEnoughFuel;
    public String teleporterAtDestinationBroken;
    public String fuel;
    public String fuelCost;
    public String currentFuel;
    public String maxFuel;
    public String costModifier;
    public String distanceModifier;
    public String maxDistance;
    public String fuelInvName;
    
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
        fuel = _strings.getString("fuel", "Fuel");
        fuelCost = _strings.getString("fuelCost", "Fuel cost");
        currentFuel = _strings.getString("currentFuel", "Current fuel");
        maxFuel = _strings.getString("maxFuel", "Maximum fuel");
        costModifier = _strings.getString("costModifier", "Cost modifier");
        distanceModifier = _strings.getString("distanceModifier", "Distance modifier");
        maxDistance = _strings.getString("maxDistance", "Maximal distance");
        fuelInvName = _strings.getString("fuelInvName", "Add Fuel");

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
