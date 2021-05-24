package cz.cuni.mff.rodd.teleportstructures.config;

import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import java.util.List;
import java.util.ArrayList;

public class MainConfig {

    public static final int configVersion = 1;

    private int _maxDistance = 1000;

    private int _costPerBlockTraveled = 1;

    private int _baseMaxFuel = 14000;

    private final Map<Material, Double> _costModifiers;
    private final Map<Material, Double> _distanceModifiers;
    private final Map<Material, Integer> _fuelValues;
    private final List<Material> _structureBlocks;
    private final List<Material> _secondaryStructureBlocks;

    public MainConfig() {
        _costModifiers = new HashMap<>();
        _distanceModifiers = new HashMap<>();
        _fuelValues = new HashMap<>();
        _structureBlocks = new ArrayList<>();
        _secondaryStructureBlocks = new ArrayList<>();
    }

    public void setBaseMaxDistance(int maxDistance) {
        this._maxDistance = maxDistance;
    }

    public int getBaseMaxDistance() {
        return _maxDistance;
    }

    public void setBaseMaxFuel(int maxFuel) {
        this._baseMaxFuel = maxFuel;
    }

    public int getBaseMaxFuel() {
        return _baseMaxFuel;
    }

    public void setCostPerBlockTraveled(int costPerBlockTraveled) {
        this._costPerBlockTraveled = costPerBlockTraveled;
    }

    public int getCostPerBlockTraveled() {
        return _costPerBlockTraveled;
    }

    public void addCostModifier(Material material, Double modifier) {
        _costModifiers.put(material, modifier);
    }

    @Nullable
    public Double getCostModifier(Material material) {
        return _costModifiers.get(material);
    }

    public void addDistanceModifiers(Material material, Double modifier) {
        _distanceModifiers.put(material, modifier);
    }

    @Nullable
    public Double getDistanceModifier(Material material) {
        return _distanceModifiers.get(material);
    }

    public void addFuel(Material material, Integer fuelValue) {
        _fuelValues.put(material, fuelValue);
    }

    @Nullable
    public int getFuelValue(Material material) {
        return _fuelValues.get(material);
    }

    public void addStructureBlock(Material material) {
        _structureBlocks.add(material);
    }

    public void addSecondaryStructureBlock(Material material) {
        _secondaryStructureBlocks.add(material);
    }

    public boolean isCostModifier(Material material) {
        return _costModifiers.containsKey(material);
    }

    public boolean isDistanceModifier(Material material) {
        return _distanceModifiers.containsKey(material);
    }

    public boolean isModifier(Material material) {
        return _costModifiers.containsKey(material) || _distanceModifiers.containsKey(material);
    }

    public boolean isFuel(Material material) {
        return _fuelValues.containsKey(material);
    }

    public boolean isStructureBlock(Material material) {
        return _structureBlocks.contains(material);
    }

    public boolean isSecondaryStructureBlock(Material material) {
        return _secondaryStructureBlocks.contains(material);
    }
}
