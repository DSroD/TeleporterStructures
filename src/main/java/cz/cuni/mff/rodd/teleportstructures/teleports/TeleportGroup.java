package cz.cuni.mff.rodd.teleportstructures.teleports;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class TeleportGroup {

    private String _groupName;
    private Set<Teleport> _teleports;
    private UUID _ownerUUID;
    private Set<UUID> _editorUUIDS;

    public TeleportGroup(String name, UUID owner) {
        _groupName = name;
        _ownerUUID = owner;
        _teleports = new HashSet<>();
        _editorUUIDS = new HashSet<>();
    }

    public void addTeleport(Teleport teleport) {
        //TODO: Can teleport be linked to more groups?
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
        return _teleports.stream().anyMatch(x -> x.getLocation().equals(location));
    }

    public boolean isTeleportInGroup(String teleportername) {
        return _teleports.stream().anyMatch(x -> x.getName().equals(teleportername));
    }

    public boolean removeTeleport(Teleport t) {
        return _teleports.remove(t);
    }

    public boolean removeTeleport(String teleportName) {
        for(Teleport t : _teleports) {
            if(t.getName().equals(teleportName)) {
                _teleports.remove(t);
                return true;
            }
        }
        return false;
    }

    public boolean isEditor(UUID uuid) {
        return _editorUUIDS.contains(uuid);
    }

    public boolean addEditor(UUID uuid) {
        return _editorUUIDS.add(uuid);
    }

    public boolean removeEditor(UUID uuid) {
        return _editorUUIDS.remove(uuid);
    }

    public Set<UUID> getEditors() {
        return _editorUUIDS;
    }

    @Nullable
    public Teleport getTeleport(String teleportName) {
        for (Teleport t : _teleports) {
            if(t.getName().equals(teleportName)) {
                return t;
            }
        }
        return null;
    }

}
