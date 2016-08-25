package com.drizzard.usernamerestrictor;

import com.drizzard.usernamerestrictor.handler.SettingsManager;
import com.drizzard.usernamerestrictor.object.plugin.MinecraftPlugin;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

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
    	
    	registerCommands();
        registerHandlers();
        registerEvents();
        
    }
    
    public void registerCommands() {
    
    	getCommand("urestrictor").setExecutor(new RestrictorCommands());
    	
    }

    public void registerHandlers() {
    	
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

    @Override
    public void onDisable() {

    }

    @SuppressWarnings("unchecked")
	@EventHandler(priority = EventPriority.LOWEST)
    public void onLogin(AsyncPlayerPreLoginEvent event) {
        String playerName = event.getName();
        List<String> names = (List<String>) settings.getPlayerData().getList("KnownUsernames");

        if (!containsCaseInsensitive(playerName, names)) {
            names.add(playerName);
            settings.getPlayerData().set("KnownUsernames", names);
            settings.savePlayerData();
        } else {
            for (String username : (List<String>) settings.getPlayerData().getList("KnownUsernames")) {
                if (!username.equals(playerName)) {
                    event.setKickMessage(ChatColor.translateAlternateColorCodes('&', settings.getPlayerData().getString("KickMessage").replace("%name%", username)));
                    event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
                    break;
                }
            }
        }
    }

    private boolean containsCaseInsensitive(String strToCompare, List<String> list) {
        for (String str : list) {
            if (str.equalsIgnoreCase(strToCompare)) {
                return true;
            }
        }
        return false;
    }
}
