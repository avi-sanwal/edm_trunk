package com.tweaker.edm.persisitance;

import com.tweaker.edm.common.dto.DownloadData;

public final class DownloadPersistanceManager extends AbstractPersistanceManager<DownloadData> {

    private static DownloadPersistanceManager instance;
    
    private DownloadPersistanceManager() {
        // Singleton
    }
    
    @Override
    public DownloadData getPersistedData() {
        return null;
    }

    @Override
    public void persistWorkers() {
        // TODO Auto-generated method stub
        
    }

    public static DownloadPersistanceManager getInstance() {
        if(instance == null){
            instance = new DownloadPersistanceManager();
        }
        return instance;
    }

}
