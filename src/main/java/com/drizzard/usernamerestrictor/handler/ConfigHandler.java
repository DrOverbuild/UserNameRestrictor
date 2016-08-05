package com.drizzard.usernamerestrictor.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.drizzard.usernamerestrictor.UsernameRestrictor;
import com.drizzard.usernamerestrictor.object.plugin.MinecraftConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;

// @author Luke Anderson | stuntguy3000
public class ConfigHandler {
    @Getter
    private HashMap<String, MinecraftConfig> configClasses = new HashMap<>();
    @Getter
    private Gson gson;
    @Getter
    private List<String> loadedConfigs = new ArrayList<>();
    private UsernameRestrictor plugin;

    public ConfigHandler() {
        this.plugin = UsernameRestrictor.getInstance();

        GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
        gson = builder.create();
    }

    public void registerConfiguration(MinecraftConfig minecraftConfig) {
        configClasses.put(minecraftConfig.getConfigName(), minecraftConfig);
    }

    public void loadConfigurations() {
        for (Map.Entry<String, MinecraftConfig> config : new HashMap<>(configClasses).entrySet()) {
            File configFile = new File(plugin.getDataFolder() + File.separator + config.getKey() + ".json");

            try {
                if (!configFile.exists()) {
                    saveConfiguration(config.getValue());
                    loadConfigurations();
                    return;
                } else {
                    InputStreamReader inputStreamReader = new InputStreamReader(
                            new FileInputStream(configFile), Charset.forName("UTF-8"));

                    configClasses.put(config.getKey(), gson.fromJson(inputStreamReader, config.getValue().getClass()));
                    loadedConfigs.add(config.getKey());
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    public MinecraftConfig getConfig(String name) {
        for (Map.Entry<String, MinecraftConfig> minecraftConfig : getConfigClasses().entrySet()) {
            if (minecraftConfig.getKey().equalsIgnoreCase(name)) {
                return minecraftConfig.getValue();
            }
        }

        return null;
    }

    public void saveConfiguration(MinecraftConfig minecraftConfig) {
        File configFile = new File(plugin.getDataFolder() + File.separator + minecraftConfig.getConfigName() + ".json");

        if (!loadedConfigs.contains(minecraftConfig.getConfigName())) {
            loadedConfigs.add(minecraftConfig.getConfigName());
        }

        String json = gson.toJson(minecraftConfig);
        FileOutputStream outputStream;

        try {
            if (!configFile.exists()) {
                json = gson.toJson(minecraftConfig.getSampleConfig());
                configFile.createNewFile();
            }
            outputStream = new FileOutputStream(configFile);
            assert json != null;
            outputStream.write(json.getBytes(Charset.forName("UTF-8")));
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

