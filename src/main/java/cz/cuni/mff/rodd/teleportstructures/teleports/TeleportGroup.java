package cz.cuni.mff.rodd.teleportstructures.teleports;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class TeleportGroup {

    private String _groupName;
    private Set<Teleport> _teleports;
    private UUID _ownerUUID; //TODO: This could also be OfflinePlayer (I guess). I will keep it like this for now.

    public TeleportGroup(String name, UUID owner) {
        _groupName = name;
        _ownerUUID = owner;
        _teleports = new HashSet<>(); //TODO: Should this be HashSet? Teleporters should not be randomized in menu
        //                                    (but insert order iteration in LinkedHashSet is also not particulary
        //                                    great way to deal with this).
    }

    public void addTeleport(Teleport teleport) {
        //TODO: Can teleport be linked to more groups? If no, is it necessary to check for presence in other groups?
        _teleports.add(teleport);
    }

    public String getGroupName() {
        return _groupName;
    }

    public OfflinePlayer getOwner() {
        return Bukkit.getOfflinePlayer(_ownerUUID);
    }

    public UUID getOwnerUUID() {
        return _ownerUUID;
    }

    public Set<Teleport> getTeleports() {
        return _teleports;
    }

    public boolean HasTeleportOnLocation(Location location) {
        //TODO: Rather check nearby locations as we probably want player to be able to use teleporter at certain
        //      distance from teleporter centre (if this method is to be used as teleporter check)
        return _teleports.stream().anyMatch(x -> x.getLocation().equals(location));
    }

    public boolean isTeleportInGroup(String teleportername) {
        return _teleports.stream().anyMatch(x -> x.getName().equals(teleportername));
    }

}
