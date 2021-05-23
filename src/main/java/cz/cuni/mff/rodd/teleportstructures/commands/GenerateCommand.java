package cz.cuni.mff.rodd.teleportstructures.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cz.cuni.mff.rodd.teleportstructures.teleports.Teleport;

public class GenerateCommand implements CommandExecutor {


    public GenerateCommand() {
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if (sender instanceof Player) {

            if (args.length > 0) {
                if(args[0].equals("genstructure")) {
                    Player player = (Player) sender;

                    if(!player.hasPermission("teleportstructures.genstruct")) {
                        player.sendMessage("You are not allowed to do this.");
                        return false;
                    }
            
                    Teleport.buildStructure(player.getLocation().toBlockLocation().clone().add(0, -1, 0));
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
