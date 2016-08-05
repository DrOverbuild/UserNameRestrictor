package com.drizzard.usernamerestrictor.object.plugin;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author stuntguy3000
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface MinecraftConfigData {
    String configFilename();
}
