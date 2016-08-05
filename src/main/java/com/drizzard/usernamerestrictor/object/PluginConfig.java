package com.drizzard.usernamerestrictor.object;

import com.drizzard.usernamerestrictor.UsernameRestrictor;
import com.drizzard.usernamerestrictor.object.plugin.MinecraftConfig;
import com.drizzard.usernamerestrictor.object.plugin.MinecraftConfigData;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author stuntguy3000
 */
@EqualsAndHashCode(callSuper = true)
@Data
@MinecraftConfigData(configFilename = "config")
public class PluginConfig extends MinecraftConfig {
    private List<String> knownUsernames;
    private String kickMessage = "&cYou cannot change your username! Original name %name%";

    public PluginConfig() {
        super("config");
    }

    public static PluginConfig getConfig() {
        return (PluginConfig) UsernameRestrictor.getInstance().getConfigHandler().getConfig(PluginConfig.class.getAnnotation(MinecraftConfigData.class).configFilename());
    }

    @Override
    public MinecraftConfig getSampleConfig() {
        return new PluginConfig();
    }
}
