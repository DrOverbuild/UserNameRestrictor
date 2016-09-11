package com.drizzard.usernamerestrictor;

import com.drizzard.usernamerestrictor.datasource.DataSource;
import com.drizzard.usernamerestrictor.datasource.FlatFileDataSource;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.io.IOException;

public class RestrictorCommands implements CommandExecutor {
	UsernameRestrictor plugin;

	public RestrictorCommands(UsernameRestrictor plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		DataSource dataSource = plugin.getDataSource();

		if (cmd.getName().equalsIgnoreCase("urestrictor")) {

			if (args.length < 1) {
				sender.sendMessage(ChatColor.RED + "Invalid format! /urestrictor [add/delete/search/reload]");
				return true;
			}

			if (args[0].equalsIgnoreCase("add")) {
				if (sender.hasPermission("urestrictor.add") || sender.hasPermission("urestrictor.*")) {

					if (args.length != 2) {
						sender.sendMessage(ChatColor.RED + "Invalid format! /urestrictor add [name]");
						return true;
					}

					String username = args[1];

					if (dataSource.containsUsername(username)) {
						sender.sendMessage(ChatColor.RED + "This username is already in Known Usernames.");
						return true;
					}

					dataSource.addUsername(username);

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

					if (!dataSource.containsUsername(username)) {
						sender.sendMessage(ChatColor.RED + "This username is not in Known Usernames.");
						return true;
					}

					dataSource.removeUsername(username);

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

					if (dataSource.containsUsername(username)) {
						sender.sendMessage(ChatColor.GREEN + "The username " + ChatColor.AQUA + username + ChatColor.GREEN + " is in Known Usernames.");
						return true;
					} else {
						sender.sendMessage(ChatColor.RED + "The username " + ChatColor.AQUA + username + ChatColor.RED + " is not in Known Usernames.");
						return true;
					}
				}
			}

			if (args[0].equalsIgnoreCase("reload")) {
				if (dataSource instanceof FlatFileDataSource) {
					FlatFileDataSource flatFileDataSource = (FlatFileDataSource) dataSource;
					try {
						flatFileDataSource.loadPlayers();
					} catch (IOException e) {
						plugin.getLogger().warning("Failed to load players");
						sender.sendMessage(ChatColor.RED + "Failed to load players.");
					}
				} else {
					sender.sendMessage(ChatColor.RED + "Hey now! Why would you reload the flat file when you are using MySQL?");
				}
			}

		}

		return true;
	}

}
