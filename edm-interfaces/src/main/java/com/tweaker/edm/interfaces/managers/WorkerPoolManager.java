package com.tweaker.edm.interfaces.managers;

import java.util.Collection;

import com.tweaker.edm.exceptions.DownloadManagerException;
import com.tweaker.edm.exceptions.persistance.DataWriteException;
import com.tweaker.edm.interfaces.Worker;

public interface WorkerPoolManager {

    enum State {
        STOPPED, STARTED
    }
    
    void startProcessing() throws DownloadManagerException;
    
    State queryManagerState();
    
    String getManagerDescription();
    
    void stopProcessing() throws DataWriteException;
    
    <E extends Worker> void addWorkers(Collection<E> workers);

    <E extends Worker> void stopWorkers(Collection<E> workers);
    
}
