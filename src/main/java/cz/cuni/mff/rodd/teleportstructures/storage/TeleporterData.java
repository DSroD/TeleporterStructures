package cz.cuni.mff.rodd.teleportstructures.storage;

import cz.cuni.mff.rodd.teleportstructures.TeleportStructures;
import cz.cuni.mff.rodd.teleportstructures.teleports.Teleport;
import cz.cuni.mff.rodd.teleportstructures.teleports.TeleportGroup;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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
        for(String groupSection : _data.getConfigurationSection("groups").getKeys(false)) { //Loop through groups
            TeleportGroup g = new TeleportGroup(groupSection, UUID.fromString(_data.getString("group."+groupSection+".owner")));
            _teleportGroups.put(groupSection, g);
            for(String teleport : _data.getConfigurationSection("groups."+groupSection+".teleports").getKeys(false)) { //Loop through teleports in group
                Teleport t = new Teleport(
                        new Location(
                            Bukkit.getWorld(UUID.fromString(_data.getString("groups."+groupSection+".teleports." + teleport + ".world"))),
                            _data.getInt("groups."+groupSection+".teleports." + teleport + ".x"),
                            _data.getInt("groups."+groupSection+".teleports." + teleport + ".y"),
                            _data.getInt("groups."+groupSection+".teleports." + teleport + ".z")),
                        _data.getString("groups."+groupSection+".teleports." + teleport),
                        _data.getString("groups."+groupSection),
                        _data.getInt("groups."+groupSection+".teleports." + teleport + ".fuel"));
                g.addTeleport(t);
            }
        }
    }

    public void saveTeleporterDataToFile() {
        try {
            _data.save(_dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setFuel(String groupName, String teleportName, int newFuel) //Call this on any fuel change event
    {
        Teleport t = _teleportGroups.get(groupName).getTeleport(teleportName);
        if(t == null) return;
        t.setFuel(newFuel);
        _data.set("groups." + t.getTeleportGroupName() + ".teleports." + t.getName() + ".fuel", newFuel);
    }

    public boolean createTeleport(Location l, String name, String groupName, UUID owner) {
        /**
         * This is called when player creates teleport ingame, structure passes all checks and newly created Teleport object
         * is passed here
         */
        Teleport t = new Teleport(l, name, groupName, 0);
        if(!_teleportGroups.containsKey(t.getTeleportGroupName())) {
            createGroup(t.getTeleportGroupName(), owner);
        }
        TeleportGroup g = _teleportGroups.get(t.getTeleportGroupName());
        if(g.getOwnerUUID() == owner) {
            g.addTeleport(t);
            _data.createSection("groups." + g.getGroupName() + ".teleports." + t.getName());
            _data.set("groups." + g.getGroupName() + ".teleports." + t.getName() + ".x", t.getLocation().getBlockX());
            _data.set("groups." + g.getGroupName() + ".teleports." + t.getName() + ".y", t.getLocation().getBlockY());
            _data.set("groups." + g.getGroupName() + ".teleports." + t.getName() + ".z", t.getLocation().getBlockZ());
            _data.set("groups." + g.getGroupName() + ".teleports." + t.getName() + ".world", t.getLocation().getWorld().getUID().toString());
            _data.set("groups." + g.getGroupName() + ".teleports." + t.getName() + ".fuel", 0);
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

    public boolean removeTeleport(String groupName, String teleportName) {
        _data.set("groups." + groupName + ".teleports." + teleportName, null);
        if(_teleportGroups.get(groupName).getTeleports().size() == 1) { //TODO: Add null check here haha
            _data.set("groups." + groupName, null);
            return _teleportGroups.remove(groupName) != null;
        }
        return _teleportGroups.get(groupName).removeTeleport(teleportName);
    }

    //I don't even know why would I have this method overload but whatever...
    public boolean removeTeleport(Teleport t) {
        _data.set("groups." + t.getTeleportGroupName() + ".teleports." + t.getName(), null);
        if(_teleportGroups.get(t.getTeleportGroupName()).getTeleports().size() == 1) { //TODO: Add null check here haha
            //This is last remaining teleporter
            _data.set("groups." + t.getTeleportGroupName(), null);
            return _teleportGroups.remove(t.getTeleportGroupName()) != null;
        }
        return _teleportGroups.get(t.getTeleportGroupName()).removeTeleport(t);

    }

}
