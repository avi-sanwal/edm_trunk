package com.tweaker.edm.appconfig;

import java.io.File;

import com.tweaker.edm.interfaces.config.ApplicationConfiguration;

public enum ApplicationConfigurationImpl implements ApplicationConfiguration {

    INSTANCE;

    public static final String APP_NAME = "Etherial Download Manager";
    public static final String SHORT_APP_NAME = "EDM";

    private File appConfigFile;
    private File dataFile;
    private String appDataPath;
    private File incompleteDownloadDirectory;

    private ApplicationConfigurationImpl() {
        appDataPath = determineAppDataFolder();
        File appDataFolder = new File(appDataPath);
        if(!appDataFolder.isDirectory()) {
            appDataFolder.mkdirs();
        }
    }

    private String determineAppDataFolder() {
        String os = System.getProperty("os.name");
        if (os.contains("Windows")) {
            return System.getenv("LOCALAPPDATA") + "\\" + SHORT_APP_NAME;
        }
        String homeDir = System.getProperty("user.home");
        if (os.contains("mac")) {
            return homeDir + "/Library/Application Support/" + SHORT_APP_NAME;
        }
        return homeDir + "/.local/share/" + SHORT_APP_NAME;
    }

    @Override
    public File getAppConfigFile() {
        return appConfigFile;
    }

    @Override
    public String getAppDataPath() {
        return appDataPath;
    }

    @Override
    public File getDataFile() {
        return dataFile;
    }

    @Override
    public File getIncompleteDownloadDirectory() {
        return incompleteDownloadDirectory;
    }
    
}
