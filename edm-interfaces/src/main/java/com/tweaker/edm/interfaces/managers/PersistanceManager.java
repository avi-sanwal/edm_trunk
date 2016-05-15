package com.tweaker.edm.interfaces.managers;

public interface PersistanceManager<E> {
    
    E getPersistedData();
    
    void persistWorkers();

}
