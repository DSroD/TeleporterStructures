package cz.cuni.mff.rodd.teleportstructures.teleports;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.Material;

import cz.cuni.mff.rodd.teleportstructures.TeleportStructures;
import cz.cuni.mff.rodd.teleportstructures.handlers.TeleporterMenu;

public class Teleport {

    private static final List<List<Integer>> _relMaterialBlocks = Arrays.asList(
        Arrays.asList(0,1,0),
        Arrays.asList(1,1,0),
        Arrays.asList(-1,1,0),
        Arrays.asList(0,1,1),
        Arrays.asList(0,1,-1));

    private static final List<List<Integer>> _relSBStructureBlocks = Arrays.asList(
        Arrays.asList(3,1,1),
        Arrays.asList(3,1,0),
        Arrays.asList(3,1,-1),

        Arrays.asList(2,1,2),
        Arrays.asList(2,1,1),
        Arrays.asList(2,1,0),
        Arrays.asList(2,1,-1),
        Arrays.asList(2,1,-2),

        Arrays.asList(1,1,3),
        Arrays.asList(1,1,2),
        Arrays.asList(1,1,1),
        Arrays.asList(1,1,-1),
        Arrays.asList(1,1,-2),
        Arrays.asList(1,1,-3),

        Arrays.asList(0,1,3),
        Arrays.asList(0,1,2),
        Arrays.asList(0,1,-2),
        Arrays.asList(0,1,-3),

        Arrays.asList(-1,1,3),
        Arrays.asList(-1,1,2),
        Arrays.asList(-1,1,1),
        Arrays.asList(-1,1,-1),
        Arrays.asList(-1,1,-2),
        Arrays.asList(-1,1,-3),
        
        Arrays.asList(-2,1,2),
        Arrays.asList(-2,1,1),
        Arrays.asList(-2,1,0),
        Arrays.asList(-2,1,-1),
        Arrays.asList(-2,1,-2),

        Arrays.asList(-3,1,1),
        Arrays.asList(-3,1,0),
        Arrays.asList(-3,1,-1),

        Arrays.asList(2,2,2),
        Arrays.asList(2,2,-2),
        Arrays.asList(-2,2,2),
        Arrays.asList(-2,2,-2),

        Arrays.asList(2,3,2),
        Arrays.asList(2,3,-2),
        Arrays.asList(-2,3,2),
        Arrays.asList(-2,3,-2)
    );

    private static final List<List<Integer>> _relSSStructureBlocks = Arrays.asList(
        Arrays.asList(1,2,3),
        Arrays.asList(-1,2,3),
        Arrays.asList(1,2,-3),
        Arrays.asList(-1,2,-3),
        Arrays.asList(3,2,1),
        Arrays.asList(-3,2,1),
        Arrays.asList(3,2,-1),
        Arrays.asList(-3,2,-1)
    );

    private final TeleportStructures _plugin;
    private final Location _location;
    private final String _teleportName;
    private final TeleportGroup _teleportGroup;
    private int _fuel;
    private final TeleporterMenu _menu;
    private boolean _requiresFuel;

    public Teleport(TeleportStructures plugin, Location loc, String name, TeleportGroup teleportGroup) {
        _plugin = plugin;
        _location = loc;
        _teleportName = name;
        _teleportGroup = teleportGroup;
        _menu = new TeleporterMenu(_plugin, this);
        _fuel = 0;
        _requiresFuel = true;
    }

    public Location getLocation() {
        return _location;
    }

    public String getName() {
        return _teleportName;
    }

    public int getFuel() {
        return _fuel;
    }

    public void setFuel(int fuel) {
        _fuel = fuel;
    }

    public void addFuel(int fuel) {
        _fuel = (_fuel + fuel > _plugin.getMainConfig().getBaseMaxFuel()) ? _plugin.getMainConfig().getBaseMaxFuel() : _fuel + fuel;
    }

    public void substractFuel(int fuel) {
        _fuel -= fuel;
    }

    public TeleportGroup getTeleportGroup() {
        return _teleportGroup;
    }

    public List<Teleport> getAvailibleTeleports() {
        return _teleportGroup.getTeleports().stream().filter((el) -> {
            return el.getLocation().distance(_location) <= _plugin.getMainConfig().getBaseMaxDistance() * getDistanceModifier()  && !el.equals(this);
        }).collect(Collectors.toList());
    }

    public int getFuelCost(Location loc) {
        if(!_requiresFuel) {return 0;}
        int cpb = _plugin.getMainConfig().getCostPerBlockTraveled();
        return (int) Math.ceil(cpb * _location.distance(loc) * getCostModifier());
    }

    public double getCostModifier() {
        double costModifier = 1;
        costModifier *= _relMaterialBlocks.stream().mapToDouble((el) -> {
            Location l = _location.clone();
            l.add(el.get(0), el.get(1), el.get(2));
            Double d = _plugin.getMainConfig().getCostModifier(l.getBlock().getType());
            d = (d == null) ? 1 : d;
            return d;
        }).reduce(1, (el1, el2) -> el1 * el2);
        return costModifier;
    }

    public double getDistanceModifier() {
        double distanceModifier = 1;
        distanceModifier *= _relMaterialBlocks.stream().mapToDouble((el) -> {
            Location l = _location.clone();
            l.add(el.get(0), el.get(1), el.get(2));
            Double d = _plugin.getMainConfig().getDistanceModifier(l.getBlock().getType());
            d = (d == null) ? 1 : d;
            return d;
        }).reduce(1, (el1, el2) -> el1 * el2);
        return distanceModifier;
    }

    public static boolean isValidStructure(TeleportStructures plugin, Location location) {
        boolean check_mat = _relMaterialBlocks.stream().takeWhile(rel_pos -> {
            Location loc = location.clone();
            loc = loc.add(rel_pos.get(0), rel_pos.get(1), rel_pos.get(2));
            if(!loc.getChunk().isLoaded()) {
                loc.getChunk().load(true);
            }
            Material block = loc.getBlock().getType();
            return plugin.getMainConfig().isModifier(block) || plugin.getMainConfig().isSecondaryStructureBlock(block) || plugin.getMainConfig().isStructureBlock(block);
        }).toArray().length == _relMaterialBlocks.size();
        if(!check_mat) {
            return false;
        }
        // Check structure blocks (secondary)
        check_mat = _relSSStructureBlocks.stream().takeWhile(rel_pos -> {
            Location loc = location.clone();
            loc = loc.add(rel_pos.get(0), rel_pos.get(1), rel_pos.get(2));
            if(!loc.getChunk().isLoaded()) {
                loc.getChunk().load(true);
            }
            return plugin.getMainConfig().isSecondaryStructureBlock(loc.getBlock().getType());
        }).count() == _relSSStructureBlocks.size();
        if(!check_mat) {
            return false;
        }
        // Check structure blocks (main) - StoneBricks
        check_mat = _relSBStructureBlocks.stream().takeWhile(rel_pos -> {
            Location loc = location.clone();
            loc = loc.add(rel_pos.get(0), rel_pos.get(1), rel_pos.get(2));
            if(!loc.getChunk().isLoaded()) {
                loc.getChunk().load(true);
            }
            return plugin.getMainConfig().isStructureBlock(loc.getBlock().getType());
        }).count() == _relSBStructureBlocks.size();
        return check_mat;
    }

    public static void buildStructure(Location loc) {
        _relMaterialBlocks.stream().forEach((el) -> {
            Location _loc = loc.clone().add(el.get(0), el.get(1)-1, el.get(2));
            _loc.getBlock().setType(Material.STONE_BRICKS);
        });
        _relSSStructureBlocks.stream().forEach((el) -> {
            Location _loc = loc.clone().add(el.get(0), el.get(1)-1, el.get(2));
            _loc.getBlock().setType(Material.SMOOTH_STONE_SLAB);
        });
        _relSBStructureBlocks.stream().forEach((el) -> {
            Location _loc = loc.clone().add(el.get(0), el.get(1)-1, el.get(2));
            _loc.getBlock().setType(Material.STONE_BRICKS);
        });
    }

    public boolean isValidStructure() {
        // Check modifier
        boolean check_mat = _relMaterialBlocks.stream().takeWhile(rel_pos -> {
            Location loc = _location.clone();
            loc.add(rel_pos.get(0), rel_pos.get(1), rel_pos.get(2));
            if(!loc.getChunk().isLoaded()) {
                loc.getChunk().load(true);
            }
            Material block = loc.getBlock().getType();
            return _plugin.getMainConfig().isModifier(block) || _plugin.getMainConfig().isSecondaryStructureBlock(block) || _plugin.getMainConfig().isStructureBlock(block);
        }).toArray().length == _relMaterialBlocks.size();
        if(!check_mat) {
            return false;
        }
        // Check structure blocks (secondary)
        check_mat = _relSSStructureBlocks.stream().takeWhile(rel_pos -> {
            Location loc = _location.clone();
            loc.add(rel_pos.get(0), rel_pos.get(1), rel_pos.get(2));
            if(!loc.getChunk().isLoaded()) {
                loc.getChunk().load(true);
            }
            return _plugin.getMainConfig().isSecondaryStructureBlock(loc.getBlock().getType());
        }).count() == _relSSStructureBlocks.size();
        if(!check_mat) {
            return false;
        }
        // Check structure blocks (main) - StoneBricks
        check_mat = _relSBStructureBlocks.stream().takeWhile(rel_pos -> {
            Location loc = _location.clone();
            loc.add(rel_pos.get(0), rel_pos.get(1), rel_pos.get(2));
            if(!loc.getChunk().isLoaded()) {
                loc.getChunk().load(true);
            }
            return _plugin.getMainConfig().isStructureBlock(loc.getBlock().getType());
        }).count() == _relSBStructureBlocks.size();
        return check_mat;
    }

    public TeleporterMenu getTeleporterMenu() {
        return _menu;
    }

    public void setRequiresFuel(boolean rf) {
        _requiresFuel = rf;
    }

    public boolean requiresFuel() {
        return _requiresFuel;
    }

}
