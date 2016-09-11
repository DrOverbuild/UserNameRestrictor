package com.drizzard.usernamerestrictor.datasource;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jasper on 9/10/16.
 */
public class FlatFileDataSource implements DataSource {

	YamlConfiguration players;
	File playersFile;

	JavaPlugin plugin;

	public FlatFileDataSource(JavaPlugin plugin) throws IOException {
		this.plugin = plugin;

		playersFile = new File(plugin.getDataFolder(), "players.yml");

		loadPlayers();
	}

	public void savePlayers() {
		try {
			players.save(playersFile);
		} catch (IOException e) {
			plugin.getLogger().warning("Failed to save known usernames.");
		}
	}

	public void loadPlayers() throws IOException {
		if (!playersFile.exists()) {
			playersFile.createNewFile();
		}

		players = YamlConfiguration.loadConfiguration(playersFile);

		players.addDefault("players", new ArrayList<String>());
		players.options().copyDefaults(true);
		savePlayers();
	}

	@Override
	public String getUsernameIgnoreCase(String playerName) {
		List<String> names = players.getStringList("players");
		for (String username : names) {
			if (playerName.equalsIgnoreCase(username) && !username.equals(playerName)) {
				return username;
			}
		}
		return null;
	}

	@Override
	public void addUsername(String playerName) {
		List<String> names = players.getStringList("players");
		if (!names.contains(playerName)) {
			names.add(playerName);
			players.set("players", names);
			savePlayers();
		}
	}

	@Override
	public void removeUsername(String username) {
		List<String> names = players.getStringList("players");
		names.remove(username);
		players.set("players", names);
		savePlayers();
	}

	@Override
	public boolean containsUsername(String username) {
		return players.getStringList("players").contains(username);
	}

	@Override
	public void cleanupConnection() {
		// #donothing
	}
}
