package com.drizzard.usernamerestrictor;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.drizzard.usernamerestrictor.handler.SettingsManager;

import net.md_5.bungee.api.ChatColor;

public class RestrictorCommands implements CommandExecutor {

	SettingsManager settings = SettingsManager.getInstance();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (cmd.getName().equalsIgnoreCase("urestrictor")) {
			
			if (args.length < 1) { 
				sender.sendMessage(ChatColor.RED + "Invalid format! /urestrictor [add/delete/search]");
				return true;
			}
			
			if (args[0].equalsIgnoreCase("add")) {
				if (sender.hasPermission("urestrictor.add") || sender.hasPermission("urestrictor.*")) {
					
					if (args.length != 2) {
						sender.sendMessage(ChatColor.RED + "Invalid format! /urestrictor add [name]");
						return true;
					}
					
					String username = args[1];
					
					List<String> names = settings.getPlayerData().getStringList("KnownUsernames");
					
					if (names.contains(username)) {
						sender.sendMessage(ChatColor.RED + "This username is already in Known Usernames.");
						return true;
					}
					
					names.add(username);
					settings.getPlayerData().set("KnownUsernames", names);
					settings.savePlayerData();
					
					sender.sendMessage(ChatColor.AQUA + username + ChatColor.GREEN + " has been added to known usernames.");
					
					return true;
				}
			}

			if (args[0].equalsIgnoreCase("delete")) {
				if (sender.hasPermission("urestrictor.delete") || sender.hasPermission("urestrictor.*")) {
					
					if (args.length != 2) {
						sender.sendMessage(ChatColor.RED + "Invalid format! /urestrictor delete [name]");
						return true;
					}
					
					String username = args[1];
					
					List<String> names = settings.getPlayerData().getStringList("KnownUsernames");
					
					if (!names.contains(username)) {
						sender.sendMessage(ChatColor.RED + "This username is not in Known Usernames.");
						return true;
					}
					
					names.remove(username);
					settings.getPlayerData().set("KnownUsernames", names);
					settings.savePlayerData();
					
					sender.sendMessage(ChatColor.AQUA + username + ChatColor.GREEN + " has been removed from known usernames.");
					
					return true;
				}
			}
			
			if (args[0].equalsIgnoreCase("search")) {
				if (sender.hasPermission("urestrictor.search") || sender.hasPermission("urestrictor.*")) {
					
					if (args.length != 2) {
						sender.sendMessage(ChatColor.RED + "Invalid format! /urestrictor search [name]");
						return true;
					}
					
					String username = args[1];
					
					List<String> names = settings.getPlayerData().getStringList("KnownUsernames");
					
					if (names.contains(username)) {
						sender.sendMessage(ChatColor.GREEN + "The username " + ChatColor.AQUA + username + ChatColor.GREEN + " is in Known Usernames.");
						return true;
					}else {
						sender.sendMessage(ChatColor.RED + "The username " + ChatColor.AQUA + username + ChatColor.RED + " is not in Known Usernames.");
						return true;
					}
				}
			}
			
		}
		
		return true;
	}

}
