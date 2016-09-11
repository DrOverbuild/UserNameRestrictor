package com.drizzard.usernamerestrictor;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by jasper on 9/10/16.
 */
public class ConfigManager {

	UsernameRestrictor plugin;

	public ConfigManager(UsernameRestrictor plugin) {
		this.plugin = plugin;
	}

	public void setupDefaultConfig(){
		plugin.getConfig().addDefault("mysql.enabled", false);
		plugin.getConfig().addDefault("mysql.host", "127.0.0.1");
		plugin.getConfig().addDefault("mysql.port", "3306");
		plugin.getConfig().addDefault("mysql.database", "database");
		plugin.getConfig().addDefault("mysql.username", "username");
		plugin.getConfig().addDefault("mysql.password", "password");
		plugin.getConfig().addDefault("kick-message", "&cYou may not change your name! Original name: &b%name%");

		plugin.getConfig().options().copyDefaults(true);
		plugin.saveConfig();
	}

	public boolean mySQLIsEnabled(){
		return plugin.getConfig().getBoolean("mysql.enabled", false);
	}

	public String getMySQLHost(){
		return plugin.getConfig().getString("mysql.host");
	}

	public int getMySQLPort(){
		return plugin.getConfig().getInt("mysql.port");
	}

	public String getMySQLDatabase(){
		return plugin.getConfig().getString("mysql.database");
	}

	public String getMySQLUsername(){
		return plugin.getConfig().getString("mysql.username");
	}

	public String getMySQLPassword(){
		return plugin.getConfig().getString("mysql.password");
	}

	public String getKickMessage(){
		return plugin.getConfig().getString("kick-message");
	}
}
