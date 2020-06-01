package cz.cuni.mff.rodd.teleportstructures.config;

import cz.cuni.mff.rodd.teleportstructures.TeleportStructures;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public class MainConfig {

    private TeleportStructures plugin;

    public static final int configVersion = 1;

    private int _maxDistance = 1000;

    private int _costPerBlockTraveled = 1;

    private Map<Material, Double> _costModifiers;
    private Map<Material, Double> _distanceModifiers;
    private Map<Material, Integer> _fuelValues;

    public MainConfig(TeleportStructures plugin) {
        this.plugin = plugin;
        _costModifiers = new HashMap<>();
        _distanceModifiers = new HashMap<>();
        _fuelValues = new HashMap<>();
    }

    public void setMaxDistance(int maxDistance) {
        this._maxDistance = maxDistance;
    }

    public void setCostPerBlockTraveled(int costPerBlockTraveled) {
        this._costPerBlockTraveled = costPerBlockTraveled;
    }

    public void addCostModifier(Material material, Double modifier) {
        _costModifiers.put(material, modifier);
    }

    public void addDistanceModifiers(Material material, Double modifier) {
        _distanceModifiers.put(material, modifier);
    }

    public void addFuel(Material material, Integer fuelValue) {
        _fuelValues.put(material, fuelValue);
    }

    public boolean isCostModifier(Material material) {
        return _costModifiers.containsKey(material);
    }

    public boolean isDistanceModifier(Material material) {
        return _distanceModifiers.containsKey(material);
    }

    public boolean isFuel(Material material) {
        return _fuelValues.containsKey(material);
    }
}
