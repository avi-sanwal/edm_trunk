package com.tweaker.edm.persisitance;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.tweaker.edm.exceptions.persistance.DataReadException;
import com.tweaker.edm.exceptions.persistance.DataWriteException;
import com.tweaker.edm.interfaces.managers.PersistanceManager;

public abstract class AbstractPersistanceManager <E> implements PersistanceManager<E> {
    
    @SuppressWarnings("unchecked")
    @Override
    public E getPersistedData() throws DataReadException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(getDataFile()));){
            Object readObject = ois.readObject();
            return (E) readObject;
        } catch (IOException e) {
            throw new DataReadException("Unable to read downloads", e);
        } catch (ClassNotFoundException e) {
            throw new DataReadException("Corrupted data", e);
        } catch (ClassCastException e) {
            throw new DataReadException("Unknown instance of persisted object", e);
        }
    }
    
    @Override
    public void persistData(E data) throws DataWriteException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(getDataFile()));){
            oos.writeObject(data);
        } catch (IOException e) {
            throw new DataWriteException("Unable to persist downloads", e);
        }
    }

    protected abstract String getDataFile();

}
