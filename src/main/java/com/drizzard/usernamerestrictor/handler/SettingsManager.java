package com.drizzard.usernamerestrictor.handler;

import java.io.File; 
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;


// @author Sanan Rao | RoyalNinja
public class SettingsManager {


	  static SettingsManager instance = new SettingsManager();
	  
	  Plugin p;
	  
	  
	  FileConfiguration PlayerData;
	  File PlayerDataFile;
	  FileConfiguration SQLData;
	  File SQLDataFile;
	  
	  public static SettingsManager getInstance() {
	    return instance;
	  }
	  
	  public void setup(Plugin p) {
	    if (!p.getDataFolder().exists()) {
	      p.getDataFolder().mkdir();
	    }
	    
	    PlayerDataFile = new File(p.getDataFolder(), "PlayerData.yml");
	    SQLDataFile = new File(p.getDataFolder(), "SQLData.yml");
	    

	    if (!PlayerDataFile.exists()) {
	        try {
	          this.PlayerDataFile.createNewFile();
	        }
	        catch (IOException e) {
	          Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not create PlayerData.yml!");
	        }
	    }

	    if (!SQLDataFile.exists()) {
	        try {
	          this.SQLDataFile.createNewFile();
	        }
	        catch (IOException e) {
	          Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not create SQLData.yml!");
	        }
	    }
	    
	    PlayerData = YamlConfiguration.loadConfiguration(this.PlayerDataFile);
	    SQLData = YamlConfiguration.loadConfiguration(this.SQLDataFile);
	    
	  }
	  

	  public FileConfiguration getPlayerData() {
		return PlayerData;
	  }
	  public FileConfiguration getSQLData() {
		return SQLData;
	  }
	  

	  public void savePlayerData() {
		    try {
		    	PlayerData.save(PlayerDataFile);
		    }
		    catch (IOException e) {
		      Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not save PlayerData.yml!");
		    }
	  }
	  public void saveSQLData() {
		    try {
		    	SQLData.save(SQLDataFile);
		    }
		    catch (IOException e) {
		      Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not save SQLData.yml!");
		    }
	  }
	
	
}

