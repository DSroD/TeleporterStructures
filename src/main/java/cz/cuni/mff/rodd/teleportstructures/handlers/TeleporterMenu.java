package cz.cuni.mff.rodd.teleportstructures.handlers;

import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import cz.cuni.mff.rodd.teleportstructures.TeleportStructures;
import cz.cuni.mff.rodd.teleportstructures.teleports.Teleport;

public class TeleporterMenu implements Listener {


    TeleportStructures _plugin;
    private final HashMap<ItemStack, Teleport> _teleports;
    private final Teleport _source;
    private final Inventory _inv;
    private final FuelMenu _fm;
    // private int page // TODO: Add possibility of more pages

    public TeleporterMenu(TeleportStructures plugin, Teleport source) {
        _plugin = plugin;
        _teleports = new HashMap<>();
        _source = source;
        _fm = new FuelMenu(_plugin, _source);
        _inv = Bukkit.createInventory(null, 27, source.getName());
        Bukkit.getPluginManager().registerEvents(this, _plugin);
    }

    private void initializeItems() {
        _teleports.keySet().stream().forEach((el) -> {
            _inv.addItem(el);
        });
        final ItemStack fit = createFuelItem();
        _inv.setItem(26, fit);
    }

    private ItemStack createFuelItem() {
        final ItemStack item = new ItemStack(Material.BLAZE_POWDER, 1);
        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Fuel");
        final StringBuilder lr1 = new StringBuilder("Current fuel: ").append(_source.getFuel());
        final StringBuilder lr2 = new StringBuilder("Max fuel: ").append(_plugin.getMainConfig().getBaseMaxFuel());
        meta.setLore(Arrays.asList(lr1.toString(), lr2.toString()));
        item.setItemMeta(meta);

        return item;
    }

    private ItemStack createTeleportItem(Teleport t) {
        final ItemStack item = new ItemStack(Material.ENDER_PEARL, 1);
        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(t.getName());
        final StringBuilder lr1 = new StringBuilder();
        lr1.append(t.getLocation().getWorld().getName())
            .append(", x:").append(t.getLocation().getBlockX())
            .append(", y:").append(t.getLocation().getBlockY())
            .append(", z:").append(t.getLocation().getBlockZ());
        
        final StringBuilder lr2 = new StringBuilder();
        lr2.append("Fuel cost: ").append(_source.getFuelCost(t.getLocation()));
        final StringBuilder lr3 = new StringBuilder("Cost modifier: ").append(_source.getCostModifier());
        final StringBuilder lr4 = new StringBuilder("Distance modifier: ").append(_source.getDistanceModifier());
        final StringBuilder lr5 = new StringBuilder("Maximal distance: ").append((int) Math.floor(_source.getDistanceModifier() * _plugin.getMainConfig().getBaseMaxDistance()));
        
        meta.setLore(Arrays.asList(lr1.toString(), lr2.toString(), lr3.toString(), lr4.toString(), lr5.toString()));
        item.setItemMeta(meta);
        return item;
    }

    public void openInventory(final HumanEntity ent) {
        _teleports.clear();
        _inv.clear();
        _source.getAvailibleTeleports().stream().forEach((el) -> {
            if(el.getAvailibleTeleports().contains(_source)) {
                final ItemStack tit = createTeleportItem(el);
                _teleports.put(tit, el);
            }
        });
        initializeItems();
        ent.openInventory(_inv);
    }

    public FuelMenu getFuelMenu() {
        return _fm;
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if(e.getInventory() != _inv) return;

        e.setCancelled(true);

        final ItemStack clickedItem = e.getCurrentItem();
        if(clickedItem == null || clickedItem.getType() == Material.AIR) {return;}
        final Player p = (Player) e.getWhoClicked();

        if(clickedItem.getType() == Material.BLAZE_POWDER) {
            _plugin.getServer().getScheduler().scheduleSyncDelayedTask(_plugin, new Runnable() {
                @Override
                public void run() {
                    p.closeInventory();
                }
            }, 2L);
            _plugin.getServer().getScheduler().scheduleSyncDelayedTask(_plugin, new Runnable() {
                @Override
                public void run() {
                    _fm.openInventory(p);
                }
            }, 3L);
            

        }

        if(clickedItem.getType() == Material.ENDER_PEARL) { // Perform teleport
            Teleport dest = _teleports.get(clickedItem);
            
            if(_source.getFuelCost(dest.getLocation()) > _source.getFuel()) { // Check fuel
                p.sendMessage("There is not enough fuel!");
                return;
            }
            
            if(!dest.isValidStructure()) { // Check structure
                p.sendMessage("Teleport at chosen destination is broken!"); // TODO: this should not be static string
                return;
            }

            // Checks OK, do teleport!

            // Substract fuel
            _plugin.getServer().getScheduler().scheduleSyncDelayedTask(_plugin, new Runnable() {
                @Override
                public void run() {
                    p.closeInventory();
                    p.setVelocity(new Vector(0,2,0));
                }
            }, 1L);
            _plugin.getServer().getScheduler().scheduleSyncDelayedTask(_plugin, new Runnable() {
                @Override
                public void run() {
                    p.setVelocity(new Vector(0,0,0));
                    p.teleportAsync(dest.getLocation().clone().add(0,3,0), TeleportCause.PLUGIN); // TODO: effects?
                }
            }, 10L);
            _source.substractFuel(_source.getFuelCost(dest.getLocation()));
            _plugin.getTeleporterData().saveFuel(_source);
            
        }
    }

    @EventHandler
    public void onInventoryDrag(final InventoryDragEvent e) {
        if(e.getInventory() != _inv) return;

        e.setCancelled(true);
    }
}
