package cz.cuni.mff.rodd.teleportstructures.handlers;

import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;

import cz.cuni.mff.rodd.teleportstructures.TeleportStructures;
import cz.cuni.mff.rodd.teleportstructures.teleports.Teleport;

public class SignHandler implements Listener {

    TeleportStructures _plugin;

    public SignHandler(TeleportStructures plugin) {
        _plugin = plugin;
    }

    @EventHandler
    public void onSignChangeEvent(SignChangeEvent e) {
        if(!e.getLine(0).equals("[TeleStruct]")) { return; }
        String name = e.getLine(1);
        if(name.length() == 0) {
            e.setLine(0, "WRONG NAME");
        }
        if(_plugin.getTeleporterData().isTeleport(name)) {
            e.setLine(0, "NAME ALREADY");
            e.setLine(1, "IN USE");
            return;
        }
        if (!Teleport.isValidStructure(_plugin, e.getBlock().getLocation())) {
            e.setLine(0, "INVALID");
            e.setLine(1, "STRUCTURE");
            return;
        }
        String group = e.getLine(2);
        if(group.length() == 0) {
            e.setLine(0, "WRONG GROUP");
            return;
        }
        _plugin.getLogger().log(Level.INFO, name);
        if(!_plugin.getTeleporterData().createTeleport(e.getBlock().getLocation(), name, group, e.getPlayer().getUniqueId())) {
            e.setLine(0, "COULD NOT");
            e.setLine(1, "CREATE");
            return;
        }
        _plugin.getTeleporterData().saveTeleporterDataToFile();
    }

    @EventHandler
    public void onSignDestructionEvent(BlockBreakEvent e) {
        Material m = e.getBlock().getType();
        if(m == Material.OAK_SIGN || m == Material.BIRCH_SIGN || m == Material.ACACIA_SIGN
        || m == Material.JUNGLE_SIGN || m == Material.SPRUCE_SIGN || m == Material.DARK_OAK_SIGN ||
        m == Material.OAK_WALL_SIGN || m == Material.BIRCH_WALL_SIGN || m == Material.ACACIA_WALL_SIGN
        || m == Material.JUNGLE_WALL_SIGN || m == Material.SPRUCE_WALL_SIGN || m == Material.DARK_OAK_WALL_SIGN) {
            Teleport t = _plugin.getTeleporterData().getTeleportAtBlockPosition(e.getBlock().getLocation());
            if(t == null) return;
            if(t.getTeleportGroup().getOwnerUUID() != e.getPlayer().getUniqueId() && !e.getPlayer().hasPermission("teleportstructures.breaksign")) {
                e.setCancelled(true);
                return;
            }
            _plugin.getTeleporterData().removeTeleport(t);
            _plugin.getTeleporterData().saveTeleporterDataToFile();
        }
    }
    
}
