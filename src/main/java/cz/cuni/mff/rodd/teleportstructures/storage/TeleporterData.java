package cz.cuni.mff.rodd.teleportstructures.storage;

import cz.cuni.mff.rodd.teleportstructures.TeleportStructures;
import cz.cuni.mff.rodd.teleportstructures.teleports.Teleport;
import cz.cuni.mff.rodd.teleportstructures.teleports.TeleportGroup;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TeleporterData {
    //TODO: This class will be used to save and load teleporter groups.
    //TODO: Should datafile contain version check for deprecated formats?

    TeleportStructures _plugin;

    private Map<String, TeleportGroup> _teleportGroups;

    private FileConfiguration _data = null;
    private File _dataFile = null;

    public TeleporterData(TeleportStructures plugin) {
        _plugin = plugin;

        //Initialize teleport HashMap
        _teleportGroups = new HashMap();

        //Initialize (and create) data file
        if(_dataFile == null) {
            _dataFile = new File(_plugin.getDataFolder(), "data.yml");
        }
        if(!_dataFile.exists()) {
            try { //Why so safe?
                _dataFile.createNewFile();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void loadTeleporterDataFromFile() {
        _data = YamlConfiguration.loadConfiguration(_dataFile);
        //TODO: Load teleport groups and teleports to hash map for faster access

    }

    public void saveTeleporterDataToFile() {
        //TODO: Add checks for empty groups and delete them
        try {
            _data.save(_dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public boolean createTeleport(Teleport t, UUID owner) {
        /**
         * This is called when player creates teleport ingame, structure passes all checks and newly created Teleport object
         * is passed here
         */
        if(!_teleportGroups.containsKey(t.getTeleportGroupName())) {
            createGroup(t.getTeleportGroupName(), owner);
        }
        TeleportGroup g = _teleportGroups.get(t.getTeleportGroupName());
        if(g.getOwnerUUID() == owner) {
            g.addTeleport(t);
            _data.createSection("groups." + g.getGroupName() + ".teleports." + t.getName());
            _data.set("groups." + g.getGroupName() + ".teleports + " + t.getName() + ".x", t.getLocation().getBlockX());
            _data.set("groups." + g.getGroupName() + ".teleports + " + t.getName() + ".y", t.getLocation().getBlockY());
            _data.set("groups." + g.getGroupName() + ".teleports + " + t.getName() + ".z", t.getLocation().getBlockZ());
            return true;
        }
        return false;
    }

    public boolean createGroup(String name, UUID owner) {
        if(_teleportGroups.containsKey(name)) return false;
        _teleportGroups.put(name, new TeleportGroup(name, owner));
        _data.createSection("groups." + name);
        _data.set("groups." + name + ".owner", owner.toString());
        _data.createSection("groups." + name + ".teleports");
        return true;
    }

    public boolean removeTeleport(Teleport t) {
        //TODO: Remove teleport from both HashMap and datafile (empty group check could also be here - deletion is
        //      probably not as frequent as saving.
        throw new NotImplementedException();

    }

}
