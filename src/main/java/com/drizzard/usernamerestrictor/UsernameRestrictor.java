package com.drizzard.usernamerestrictor;

import com.drizzard.usernamerestrictor.datasource.DataSource;
import com.drizzard.usernamerestrictor.datasource.FlatFileDataSource;
import com.drizzard.usernamerestrictor.datasource.MySQLDataSource;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;

public class UsernameRestrictor extends JavaPlugin implements Listener {

	public static UsernameRestrictor instance;

	DataSource dataSource;
	ConfigManager configManager;

	@Override
	public void onLoad() {
		setInstance();

//		settings.setup(this);

		PluginDescriptionFile pluginDescriptionFile = this.getDescription();
		Bukkit.getLogger().log(Level.INFO, String.format("Loaded %s version %s by RoyalNinja.",
				pluginDescriptionFile.getName(),
				pluginDescriptionFile.getVersion())
		);
	}

	public void onEnable() {
		configManager = new ConfigManager(this);
		configManager.setupDefaultConfig();

		setupDataSource();

		registerCommands();
		registerEvents();
	}

	public void onDisable() {
		dataSource.cleanupConnection();
	}

	public void setupDataSource() {
		if (configManager.mySQLIsEnabled()) {
			try{
				dataSource = new MySQLDataSource(this);
				return;
			} catch (SQLException e){
				getLogger().warning("--- FAILED TO CONNECT TO DATABASE ---");
				getLogger().warning(" - Message: " + e.getMessage());
				getLogger().warning(" - Caused by: " + e.getCause().getClass().getName());
				getLogger().warning(" - SQL State: " + e.getSQLState());
				getLogger().warning(" - SQL Error code: " + e.getErrorCode());
				getLogger().warning("-------------------------------------");
				getLogger().warning(" - It's possible your database login information, including ");
				getLogger().warning(" - host, port, database, username, and/or password is not");
				getLogger().warning(" - correct. Please check your information.");
				getLogger().warning(" - ");
				getLogger().warning(" - Using players.yml instead of MySQL.");
				getLogger().warning("-------------------------------------");
			}
		}

		try {
			dataSource = new FlatFileDataSource(this);
		} catch (IOException e) {
			getLogger().warning("Failed to load player data file: " + e.getMessage());
		}
	}

	public void registerCommands() {

		getCommand("urestrictor").setExecutor(new RestrictorCommands(this));

	}

	public void registerEvents() {
		getServer().getPluginManager().registerEvents(this, this);
	}

	public void setInstance() {
		instance = this;
	}

	@SuppressWarnings("unchecked")
	@EventHandler(priority = EventPriority.LOWEST)
	public void onLogin(AsyncPlayerPreLoginEvent event) {
		String playerName = event.getName();

		String username = dataSource.getUsernameIgnoreCase(playerName);

		if(username != null) {
			event.setKickMessage(ChatColor.translateAlternateColorCodes('&', configManager.getKickMessage().replace("%name%", username)));
			event.setLoginResult(Result.KICK_OTHER);
		} else {
			if (!dataSource.containsUsername(playerName)){
				dataSource.addUsername(playerName);
			}
		}

	}

	public DataSource getDataSource(){
		return dataSource;
	}

	public static ConfigManager getConfigManager(){
		return instance.configManager;
	}
}
