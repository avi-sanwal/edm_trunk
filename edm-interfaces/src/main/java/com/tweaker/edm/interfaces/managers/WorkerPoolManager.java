package com.tweaker.edm.interfaces.managers;

public interface WorkerPoolManager {

    enum State {
        STOPPED, STARTED
    }
    
    void startProcessing();
    
    State queryManagerState();
    
}
