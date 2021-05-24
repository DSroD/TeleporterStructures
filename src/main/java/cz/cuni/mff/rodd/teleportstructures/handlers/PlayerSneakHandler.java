package cz.cuni.mff.rodd.teleportstructures.handlers;

import cz.cuni.mff.rodd.teleportstructures.TeleportStructures;
import cz.cuni.mff.rodd.teleportstructures.teleports.Teleport;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class PlayerSneakHandler implements Listener {

    private final TeleportStructures _plugin;

    public PlayerSneakHandler(TeleportStructures plugin) {
        _plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled=true)
    public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent event) {
        if (event.isSneaking()) return;
        Player player = event.getPlayer();
        Teleport t = _plugin.getTeleporterData().getTeleportOnLocation(player.getLocation());
        if(t == null) return;
        if(!t.isValidStructure()) {
            event.getPlayer().sendMessage(_plugin.getStrings().teleporterAtDestinationBroken);
            return;
        }
        t.getTeleporterMenu().openInventory(player);
    }

}
