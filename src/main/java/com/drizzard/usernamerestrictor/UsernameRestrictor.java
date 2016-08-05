package com.drizzard.usernamerestrictor;

import com.drizzard.usernamerestrictor.handler.ConfigHandler;
import com.drizzard.usernamerestrictor.object.PluginConfig;
import com.drizzard.usernamerestrictor.object.plugin.MinecraftPlugin;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

/**
 * @author stuntguy3000
 */
public class UsernameRestrictor extends MinecraftPlugin implements Listener {
    @Getter
    private static UsernameRestrictor instance;

    @Getter
    private ConfigHandler configHandler;
    @Getter
    private PluginConfig pluginConfig;

    @Override
    public void registerHandlers() {
        configHandler = new ConfigHandler();
        configHandler.registerConfiguration(new PluginConfig());
        configHandler.loadConfigurations();

        pluginConfig = PluginConfig.getConfig();
        if (pluginConfig.getKnownUsernames() == null) {
            pluginConfig.setKnownUsernames(new ArrayList<>());
            configHandler.saveConfiguration(pluginConfig);
        }
    }

    @Override
    public void registerCommands() {

    }

    @Override
    public void registerEvents() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void setInstance() {
        instance = this;
    }

    @Override
    public void onDisable() {

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLogin(AsyncPlayerPreLoginEvent event) {
        String playerName = event.getName();
        List<String> names = pluginConfig.getKnownUsernames();

        if (!containsCaseInsensitive(playerName, names)) {
            names.add(playerName);
            configHandler.saveConfiguration(pluginConfig);
        } else {
            for (String username : pluginConfig.getKnownUsernames()) {
                if (!username.equals(playerName)) {
                    event.setKickMessage(ChatColor.translateAlternateColorCodes('&', pluginConfig.getKickMessage().replace("%name%", username)));
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
