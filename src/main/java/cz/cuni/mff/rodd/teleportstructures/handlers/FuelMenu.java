package cz.cuni.mff.rodd.teleportstructures.handlers;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

import cz.cuni.mff.rodd.teleportstructures.TeleportStructures;
import cz.cuni.mff.rodd.teleportstructures.teleports.Teleport;

public class FuelMenu implements Listener {

    private final TeleportStructures _plugin;
    private final Teleport _t;
    private final Inventory _inv;

    public FuelMenu(TeleportStructures plugin, Teleport t) {
        _plugin = plugin;
        _t = t;
        _inv = Bukkit.createInventory(null, 9, _plugin.getStrings().getFuelInvName());
        Bukkit.getPluginManager().registerEvents(this, _plugin);
    }

    public void openInventory(final HumanEntity ent) {
        ent.openInventory(_inv);
    }

    public Inventory getInventory() {
        return _inv;
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent e) {
        if(e.getInventory() != _inv) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if(e.getClickedInventory() != _inv) return;
        if(e.getCursor() == null) return;
        if(e.getCursor().getType() == Material.AIR) return;
        if(!_plugin.getMainConfig().isFuel(e.getCursor().getType())) {
            e.setCancelled(true);
            return;
        }
        int fuelVal = _plugin.getMainConfig().getFuelValue(e.getCursor().getType());
        int missing = _plugin.getMainConfig().getBaseMaxFuel() - _t.getFuel();
        int max_num = missing / fuelVal;
        if (max_num <= e.getCursor().getAmount()) {
            _t.addFuel(fuelVal*max_num);
            e.getCursor().setAmount(e.getCursor().getAmount() - max_num);
            e.setCancelled(true);
        }
        else {
            _t.addFuel(fuelVal * e.getCursor().getAmount());
            e.getCursor().setAmount(0);
            e.setCancelled(true);
        }
        _plugin.getTeleporterData().saveFuel(_t);
    }
    
}
