package cz.cuni.mff.rodd.teleportstructures.teleports;

import org.bukkit.Location;

public class Teleport {

    private Location _location;
    private String _teleportName;
    private TeleportGroup _teleportGroup;
    private int _charge;
    private int _maxCharge;

    public Teleport(Location loc, String name, TeleportGroup group, int maxCharge) {
        _location = loc;
        _teleportName = name;
        _teleportGroup = group;
        _maxCharge = maxCharge;
        _charge = 0;
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
