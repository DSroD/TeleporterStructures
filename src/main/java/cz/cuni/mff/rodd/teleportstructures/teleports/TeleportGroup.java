package cz.cuni.mff.rodd.teleportstructures.teleports;

import java.util.Set;

public class TeleportGroup {

    private String _groupName;
    private Set<Teleport> _teleports;

    public TeleportGroup(String name) {
        _groupName = name;
    }

    public TeleportGroup(String name, Set<Teleport> teleports) {

    }

}
