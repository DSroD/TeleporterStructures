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
    private boolean _isPublic;

    public TeleportGroup(String name, UUID owner, boolean isPublic) {
        _groupName = name;
        _ownerUUID = owner;
        _isPublic = isPublic;
        _teleports = new HashSet<>(); //TODO: Should this be HashSet? Teleporters should not be randomized in menu
        //                                    (but insert order iteration in LinkedHashSet is also not particulary
        //                                    great way to deal with this).
    }

    public void addTeleport(Teleport teleport) {
        //TODO: Can teleport be linked to more groups? If no, is it necessary to check for presence in other groups?
        _teleports.add(teleport);
    }

    public OfflinePlayer getOwner() {
        return Bukkit.getOfflinePlayer(_ownerUUID);
    }

    public boolean isPublic() {
        return _isPublic;
    }

    public Set<Teleport> getTeleports() {
        return _teleports;
    }

    public boolean HasTeleportOnLocation(Location l) {
        //TODO: Rather check nearby locations as we probably want player to be able to use teleporter at certain
        //      distance from teleporter centre (if this method is to be used as teleporter check)
        return _teleports.stream().anyMatch(x -> x.getLocation().equals(l));
    }

}
