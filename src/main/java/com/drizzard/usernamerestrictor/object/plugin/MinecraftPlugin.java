package com.drizzard.usernamerestrictor.object.plugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

/**
 * @author stuntguy3000
 */
public abstract class MinecraftPlugin extends JavaPlugin {
    public abstract void registerHandlers();

    public abstract void registerCommands();

    public abstract void registerEvents();

    public abstract void setInstance();

    @Override
    public void onLoad() {
        setInstance();

        if (!this.getDataFolder().exists()) {
            if (!this.getDataFolder().mkdir()) {
                Bukkit.getLogger().log(Level.SEVERE, "Unable to create configuration folder!");
                Bukkit.getPluginManager().disablePlugin(this);
            }
        }

        PluginDescriptionFile pluginDescriptionFile = this.getDescription();
        Bukkit.getLogger().log(Level.INFO, String.format("Loaded %s version %s by stuntguy3000.",
                pluginDescriptionFile.getName(),
                pluginDescriptionFile.getVersion())
        );
    }

    @Override
    public void onEnable() {
        registerHandlers();
        registerEvents();
        registerCommands();
    }

    @Override
    public abstract void onDisable();
}
