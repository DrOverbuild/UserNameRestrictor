package com.drizzard.usernamerestrictor.object.plugin;

import com.drizzard.usernamerestrictor.UsernameRestrictor;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public abstract class MinecraftConfig {
    @Getter
    private transient String configName;

    public abstract MinecraftConfig getSampleConfig();

    public void saveConfig() {
        UsernameRestrictor.getInstance().getConfigHandler().saveConfiguration(this);
    }
}
