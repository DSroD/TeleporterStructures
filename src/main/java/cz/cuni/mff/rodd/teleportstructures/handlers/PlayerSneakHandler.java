package cz.cuni.mff.rodd.teleportstructures.handlers;

import cz.cuni.mff.rodd.teleportstructures.TeleportStructures;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class PlayerSneakHandler implements Listener {

    private TeleportStructures _plugin;

    public PlayerSneakHandler(TeleportStructures plugin) {
        this._plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled=true)
    public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        Location location = player.getLocation();
        //TODO: Check if player stands on portal

        //TODO: Open GUI with group-linked portals (link only two-way possible portals - if player teleports somewhere
        //      portal there HAS to be able to teleport him back, this makes system more interesting and encourages
        //      usage of short-distance teleporters near hubs
    }

}
