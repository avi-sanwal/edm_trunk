package com.tweaker.edm.interfaces.managers;

import com.tweaker.edm.exceptions.persistance.DataReadException;
import com.tweaker.edm.exceptions.persistance.DataWriteException;

public interface PersistanceManager<E> {
    
    E getPersistedData() throws DataReadException;
    
    void persistData(E data)throws DataWriteException;

}
