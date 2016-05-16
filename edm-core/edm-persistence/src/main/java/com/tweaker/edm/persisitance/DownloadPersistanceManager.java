package com.tweaker.edm.persisitance;

import com.tweaker.edm.common.dto.DownloadData;

public final class DownloadPersistanceManager extends AbstractPersistanceManager<DownloadData> {
    
    private final String dataFile = System.getProperty("user.home") + "/edm/downloads.dat";
    private static DownloadPersistanceManager instance;
    
    private DownloadPersistanceManager() {
        // Singleton
    }
    
    public static DownloadPersistanceManager getInstance() {
        if(instance == null){
            instance = new DownloadPersistanceManager();
        }
        return instance;
    }

    @Override
    protected String getDataFile() {
        return dataFile;
    }

}
