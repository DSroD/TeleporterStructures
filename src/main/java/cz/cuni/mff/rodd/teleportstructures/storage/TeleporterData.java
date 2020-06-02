package cz.cuni.mff.rodd.teleportstructures.storage;

import cz.cuni.mff.rodd.teleportstructures.TeleportStructures;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class TeleporterData {
    //TODO: This class will be used to save and load teleporter groups.
    //TODO: Should datafile contain version check for deprecated formats?

    TeleportStructures _plugin;

    public TeleporterData(TeleportStructures plugin) {
        _plugin = plugin;

        //TODO: Initialize config-like file for data storage

    }

    public void loadTeleporterDataFromFile() {
        throw new NotImplementedException();
    }

    public void saveTeleporterDataToFile() {
        throw new NotImplementedException();
    }

}
