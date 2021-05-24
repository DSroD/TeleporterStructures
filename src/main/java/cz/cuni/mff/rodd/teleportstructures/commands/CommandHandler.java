package cz.cuni.mff.rodd.teleportstructures.commands;

import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cz.cuni.mff.rodd.teleportstructures.TeleportStructures;
import cz.cuni.mff.rodd.teleportstructures.teleports.Teleport;

public class CommandHandler implements CommandExecutor {

    TeleportStructures _plugin;

    public CommandHandler(TeleportStructures plugin) {
        _plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // TODO: Having base command class and exteding it would be preferable
        if (sender instanceof Player) {
            Player player = (Player) sender;
            
            if (args.length > 0) {
                if(args[0].equals("genstructure")) {
                    

                    if(!player.hasPermission("teleportstructures.genstruct")) {
                        player.sendMessage("You are not allowed to do this.");
                        return false;
                    }
            
                    Teleport.buildStructure(player.getLocation().toBlockLocation().clone().add(0, -1, 0));
                }

                if(args[0].equals("requirefuel")) {
                    if(!player.hasPermission("teleportstructures.changereqfuel")) {
                        player.sendMessage("You are not allowed to do this.");
                        return false;
                    }
                    Teleport t = _plugin.getTeleporterData().getTeleportOnLocation(player.getLocation());
                    if(t == null) {
                        player.sendMessage("You are not standing on any teleport.");
                        return false;
                    }
                    boolean pervstate = t.requiresFuel();
                    t.setRequiresFuel(!pervstate);
                    _plugin.getTeleporterData().saveFuelRequired(t);
                    if(pervstate) {
                        player.sendMessage("Fuel no longer required!");
                    }
                    else {
                        player.sendMessage("Fuel is now required!");
                    }
                }

                if(args[0].equals("addeditor")) {
                    if(args.length == 1) {
                        player.sendMessage("Usage: /telestruct addeditor [player]");
                        return false;
                    }
                    
                    UUID editor = Bukkit.getPlayerUniqueId(args[1]); // TODO: Null check against players here!!!
                    if(editor == null) {
                        player.sendMessage("Player not found");
                        return false;
                    }
                    Teleport t = _plugin.getTeleporterData().getTeleportOnLocation(player.getLocation());
                    if(t == null) {
                        player.sendMessage("You are not standing on any teleport!");
                        return false;
                    }
                    if(!t.getTeleportGroup().getOwnerUUID().equals(player.getUniqueId())) {
                        player.sendMessage("You are not allowed to edit this teleport group!");
                        return false;
                    }
                    t.getTeleportGroup().addEditor(editor);
                    _plugin.getTeleporterData().saveEditors(t.getTeleportGroup());
                }

                if(args[0].equals("removeeditor")) {
                    if(args.length == 1) {
                        player.sendMessage("Usage: /telestruct removeeditor [player]");
                    }
                    UUID editor = Bukkit.getPlayerUniqueId(args[1]);
                    if(editor == null) {
                        player.sendMessage("Player not found");
                        return false;
                    }
                    Teleport t = _plugin.getTeleporterData().getTeleportOnLocation(player.getLocation());
                    if(t == null) {
                        player.sendMessage("You are not standing on any teleport!");
                        return false;
                    }
                    if(!t.getTeleportGroup().getOwnerUUID().equals(player.getUniqueId())) {
                        player.sendMessage("You are not allowed to edit this teleport group!");
                        return false;
                    }
                    t.getTeleportGroup().removeEditor(editor);
                    _plugin.getTeleporterData().saveEditors(t.getTeleportGroup());
                }

                if(args[0].equals("listeditors")) {
                    Teleport t = _plugin.getTeleporterData().getTeleportOnLocation(player.getLocation());
                    if(t == null) {
                        player.sendMessage("You are not standing on any teleport!");
                        return false;
                    }
                    if(!t.getTeleportGroup().getOwnerUUID().equals(player.getUniqueId())) {
                        player.sendMessage("You are not allowed to view info about this teleport group!");
                        return false;
                    }
                    
                    String editors = t.getTeleportGroup().getEditors().stream().map((uid) -> {
                        return Bukkit.getOfflinePlayer(uid).getName();
                    }).collect(Collectors.joining( ", "));
                    player.sendMessage("Editors of group "+t.getTeleportGroup().getGroupName()+": "+editors);
                }
            }
            
            return true;
        }
        else {
            sender.sendMessage("You must be a player!");
            return false;
        }
            
    }
    
}
