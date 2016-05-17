package com.tweaker.edm.interfaces.config;

import java.io.File;
import java.io.Serializable;

public interface ApplicationConfiguration extends Serializable {

    String getAppDataPath();

    File getAppConfigFile();

    File getDataFile();

    File getIncompleteDownloadDirectory();

}
