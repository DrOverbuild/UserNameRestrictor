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
	  
	  public static SettingsManager getInstance() {
	    return instance;
	  }
	  
	  public void setup(Plugin p) {
	    if (!p.getDataFolder().exists()) {
	      p.getDataFolder().mkdir();
	    }
	    
	    PlayerDataFile = new File(p.getDataFolder(), "PlayerData.yml");
	    

	    if (!PlayerDataFile.exists()) {
	        try {
	          this.PlayerDataFile.createNewFile();
	        }
	        catch (IOException e) {
	          Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not create PlayerData.yml!");
	        }
	    }
	    
	    PlayerData = YamlConfiguration.loadConfiguration(this.PlayerDataFile);
	    
	  }
	  

	  public FileConfiguration getPlayerData() {
		return PlayerData;
	  }
	  

	  public void savePlayerData() {
		    try {
		    	PlayerData.save(PlayerDataFile);
		    }
		    catch (IOException e) {
		      Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not save PlayerData.yml!");
		    }
	  }
	
	
}

