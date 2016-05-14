package com.tweaker.edm.interfaces.managers;

import java.util.Queue;

import com.tweaker.edm.interfaces.Worker;

public interface PersistanceManager {
    
    Queue<Worker> getPersistedWorkers();
    
    void persistWorkers();

}
