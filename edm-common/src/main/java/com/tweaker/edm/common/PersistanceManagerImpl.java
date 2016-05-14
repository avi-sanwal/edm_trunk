package com.tweaker.edm.common;

import java.util.Queue;

import com.tweaker.edm.interfaces.Worker;
import com.tweaker.edm.interfaces.managers.PersistanceManager;

public enum PersistanceManagerImpl implements PersistanceManager {

    INSTANCE;
    
    @Override
    public Queue<Worker> getPersistedWorkers(){
        return null;
    }

    @Override
    public void persistWorkers() {
    }
}
