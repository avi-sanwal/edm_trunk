package com.tweaker.edm.interfaces.managers;

import com.tweaker.edm.exceptions.DownloadManagerException;
import com.tweaker.edm.exceptions.persistance.DataWriteException;

public interface WorkerPoolManager {

    enum State {
        STOPPED, STARTED
    }
    
    void startProcessing() throws DownloadManagerException;
    
    State queryManagerState();
    
    String getManagerDescription();
    
    void stopProcessing() throws DataWriteException;
    
}
