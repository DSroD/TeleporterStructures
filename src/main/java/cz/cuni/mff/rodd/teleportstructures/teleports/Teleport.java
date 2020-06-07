package cz.cuni.mff.rodd.teleportstructures.teleports;

import org.bukkit.Location;
import org.bukkit.Material;

public class Teleport {

    private Location _location;
    private String _teleportName;
    private String _teleportGroupName;
    private int _fuel;
    private int _maxFuel;
    private int _maxDistance;
    private Material _menuItem; //TODO: Implement selection of menu item (can be managed using inventory GUI)

    public Teleport(Location loc, String name, String groupName, int fuel) {
        _location = loc;
        _teleportName = name;
        _teleportGroupName = groupName;
        _fuel= fuel;
    }

    public Location getLocation() {
        return _location;
    }

    public String getName() {
        return _teleportName;
    }

    public String getTeleportGroupName() {
        return _teleportGroupName;
    }

    public void setFuel(int fuel) {
        _fuel = fuel;
    }

}
