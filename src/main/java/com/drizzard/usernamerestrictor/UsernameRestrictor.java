package com.drizzard.usernamerestrictor;

import com.drizzard.usernamerestrictor.handler.SQLHandler;
import com.drizzard.usernamerestrictor.handler.SettingsManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class UsernameRestrictor extends JavaPlugin implements Listener {

	private static UsernameRestrictor instance;

	SettingsManager settings = SettingsManager.getInstance();

	@Override
	public void onLoad() {

		setInstance();

		settings.setup(this);

		PluginDescriptionFile pluginDescriptionFile = this.getDescription();
		Bukkit.getLogger().log(Level.INFO, String.format("Loaded %s version %s by RoyalNinja.",
				pluginDescriptionFile.getName(),
				pluginDescriptionFile.getVersion())
		);
	}

	public void onEnable() {

		SQLHandler.setupMySQL();

		registerCommands();
		registerHandlers();
		registerEvents();
	}

	public void onDisable() {
		SQLHandler.cleanupConnection();
	}

	public void registerCommands() {

		getCommand("urestrictor").setExecutor(new RestrictorCommands());

	}

	public void registerHandlers() {

		if (settings.getSQLData().getConfigurationSection("MySQL") == null) {
			settings.getSQLData().set("MySQL.Enabled", false);
			settings.getSQLData().set("MySQL.IP", "ip");
			settings.getSQLData().set("MySQL.Port", "port");
			settings.getSQLData().set("MySQL.Database", "database");
			settings.getSQLData().set("MySQL.User", "user");
			settings.getSQLData().set("MySQL.Password", "pass");
			settings.saveSQLData();
		}

		if (settings.getPlayerData().getList("KnownUsernames") == null) {
			settings.getPlayerData().set("KnownUsernames", new ArrayList<>());
			settings.savePlayerData();
		}
		if (settings.getPlayerData().getString("KickMessage") == null) {
			settings.getPlayerData().set("KickMessage", "&cYou may not change your name! Original name: &b%name%");
		}

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

		List<String> names = settings.getPlayerData().getStringList("KnownUsernames");

		if (settings.getSQLData().getBoolean("MySQL.Enabled")) {
			try {
				PreparedStatement sql = SQLHandler.connection.prepareStatement("SELECT `username` FROM `knownUsernames`");

				ResultSet result = sql.executeQuery();

				while (result.next()) {

					String username = result.getString(1);

					if (playerName.equalsIgnoreCase(username)) {
						if (!username.equals(playerName)) {
							event.setKickMessage(ChatColor.translateAlternateColorCodes('&', settings.getPlayerData().getString("KickMessage").replace("%name%", username)));
							event.setLoginResult(Result.KICK_OTHER);
							break;
						}
					} else {
						SQLHandler.addUsername(playerName);
					}

				}

				result.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			if (settings.getPlayerData().getList("KnownUsernames").isEmpty()) {
				names.add(playerName);
				settings.getPlayerData().set("KnownUsernames", names);
				settings.savePlayerData();
			}
			for (String username : (List<String>) settings.getPlayerData().getList("KnownUsernames")) {
				if (playerName.equalsIgnoreCase(username)) {
					if (!username.equals(playerName)) {
						event.setKickMessage(ChatColor.translateAlternateColorCodes('&', settings.getPlayerData().getString("KickMessage").replace("%name%", username)));
						event.setLoginResult(Result.KICK_OTHER);
						break;
					}
				} else {
					names.add(playerName);
					settings.getPlayerData().set("KnownUsernames", names);
					settings.savePlayerData();
				}
			}
		}

	}
}
