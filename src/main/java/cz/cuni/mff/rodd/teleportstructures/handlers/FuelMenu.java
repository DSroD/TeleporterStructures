package cz.cuni.mff.rodd.teleportstructures.handlers;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import cz.cuni.mff.rodd.teleportstructures.TeleportStructures;
import cz.cuni.mff.rodd.teleportstructures.teleports.Teleport;

public class FuelMenu implements Listener {

    private final TeleportStructures _plugin;
    private final Teleport _t;
    private final Inventory _inv;

    public FuelMenu(TeleportStructures plugin, Teleport t) {
        _plugin = plugin;
        _t = t;
        _inv = Bukkit.createInventory(null, 9, "Add Fuel");
        Bukkit.getPluginManager().registerEvents(this, _plugin);
    }

    public void openInventory(final HumanEntity ent) {
        ent.openInventory(_inv);
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent e) {
        if(e.getInventory() != _inv) return;
        if(_t.getFuel() == _plugin.getMainConfig().getBaseMaxFuel()) {
            e.setCancelled(true);
            return;
        }
        if(e.getNewItems().size() != 1) {
            e.setCancelled(true);
            return;
        }

        ItemStack fuel = e.getNewItems().values().stream().findFirst().get();
        if(!_plugin.getMainConfig().isFuel(fuel.getType())) {e.setCancelled(true);}
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if(e.getInventory() != _inv) return;

        int nf = Arrays.asList(e.getInventory().getContents()).stream().mapToInt((el) -> {
            return _plugin.getMainConfig().getFuelValue(el.getType()) * el.getAmount();
        }).sum();

        _t.addFuel(nf);   
    }
    
}
