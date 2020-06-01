package cz.cuni.mff.rodd.teleportstructures.teleports;

import org.bukkit.Location;
import org.bukkit.Material;

public class Teleport {

    private Location _location;
    private String _teleportName;
    private TeleportGroup _teleportGroup;
    private int _fuel;
    private int _maxFuel;
    private int _maxDistance;
    private Material _menuItem; //TODO: Implement selection of menu item (can be managed using inventory GUI)

    public Teleport(Location loc, String name, TeleportGroup group, int maxFuel, int maxDistance) {
        _location = loc;
        _teleportName = name;
        _teleportGroup = group;
        _maxFuel = maxFuel; //TODO: Is this necessary? Max fuel should be based on teleporting modifiers and can be
        //                          recounted each time fuel is spent / added to inventory.
        //                          It can also be made as fixed parameter in config
        _fuel= 0;
        _maxDistance = maxDistance; //TODO: Is it necessary to put this as constructor argument? Max distance can be
        //                                  re-evaluated at each teleport use (based on modifier blocks)
    }

    public Location getLocation() {
        return _location;
    }

    public String getName() {
        return _teleportName;
    }

    public TeleportGroup getTeleportGroup() {
        return _teleportGroup;
    }

}
