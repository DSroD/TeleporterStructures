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
import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nullable;

public class TeleporterData {
    //TODO: This class will be used to save and load teleporter groups.
    //TODO: Should datafile contain version check for deprecated formats?

    TeleportStructures _plugin;

    private Map<String, TeleportGroup> _teleportGroups;
    private Map<String, Teleport> _teleports;

    private FileConfiguration _data = null;
    private File _dataFile = null;

    public TeleporterData(TeleportStructures plugin) {
        _plugin = plugin;

        //Initialize teleport HashMap
        _teleportGroups = new HashMap<>();
        _teleports = new HashMap<>();

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
                Teleport t = new Teleport(_plugin,
                        new Location(
                            Bukkit.getWorld(UUID.fromString(_data.getString("groups."+groupSection+".teleports." + teleport + ".world"))),
                            _data.getInt("groups."+groupSection+".teleports." + teleport + ".x"),
                            _data.getInt("groups."+groupSection+".teleports." + teleport + ".y"),
                            _data.getInt("groups."+groupSection+".teleports." + teleport + ".z")),
                        _data.getString("groups."+groupSection+".teleports." + teleport),
                        g);
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
        _data.set("groups." + t.getTeleportGroup().getGroupName() + ".teleports." + t.getName() + ".fuel", newFuel);
    }

    public boolean createTeleport(Location l, String name, String groupName, UUID owner) {
        /**
         * This is called when player creates teleport ingame, structure passes all checks and newly created Teleport object
         * is passed here
         */

        createGroup(groupName, owner);
        
        TeleportGroup g = _teleportGroups.get(groupName);
        Teleport t = new Teleport(_plugin, l, name, g);
        
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

    public boolean isTeleport(String name) {
        return _teleports.containsKey(name);
    }

    public TeleportGroup getOrCreateTeleportGroup(String name, UUID owner) {
        if(_teleportGroups.containsKey(name)) return _teleportGroups.get(name);

        _teleportGroups.put(name, new TeleportGroup(name, owner));
        _data.createSection("groups." + name);
        _data.set("groups." + name + ".owner", owner.toString());
        _data.createSection("groups." + name + ".teleports");
        return _teleportGroups.get(name);
    }

    public boolean removeTeleport(String groupName, String teleportName) {
        _data.set("groups." + groupName + ".teleports." + teleportName, null);
        if(!_teleportGroups.containsKey(groupName)) {return false;}
        Teleport t = _teleportGroups.get(groupName).getTeleport(teleportName);
        if(t == null) {return false;}
        _plugin.underigsterEvents(t.getTeleporterMenu().getFuelMenu());
        _plugin.underigsterEvents(t.getTeleporterMenu());
        if(_teleportGroups.get(groupName).getTeleports().size() <= 1) {
            _data.set("groups." + groupName, null);
            return _teleportGroups.remove(groupName) != null;
        }

        return _teleportGroups.get(groupName).removeTeleport(teleportName);
    }

    @Nullable
    public Teleport getTeleportOnLocation(Location loc) {
        final Optional<Teleport> n = _teleports.values().stream().filter((el) -> {
            return  (Math.abs(loc.getBlockX() - el.getLocation().getBlockX()) < 4) &&
                    ((loc.getBlockY() - el.getLocation().getBlockY()) < 4) &&
                    (Math.abs(loc.getBlockZ() - el.getLocation().getBlockZ()) < 4);
        }).findFirst();
        return (n.isPresent()) ? n.get() : null;
    }

    
    public boolean removeTeleport(Teleport t) {
        _data.set("groups." + t.getTeleportGroup().getGroupName() + ".teleports." + t.getName(), null);
        if(t.getTeleportGroup().getTeleports().size() == 1) {
            //This is last remaining teleporter
            _data.set("groups." + t.getTeleportGroup().getGroupName(), null);
            return _teleportGroups.remove(t.getTeleportGroup().getGroupName()) != null;
        }
        return t.getTeleportGroup().removeTeleport(t);

    }

}
