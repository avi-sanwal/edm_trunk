package com.tweaker.edm.queuemanager;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Logger;

import com.tweaker.edm.common.dto.DownloadData;
import com.tweaker.edm.exceptions.DownloadManagerException;
import com.tweaker.edm.exceptions.persistance.DataReadException;
import com.tweaker.edm.exceptions.persistance.DataWriteException;
import com.tweaker.edm.interfaces.Worker;
import com.tweaker.edm.interfaces.download.Download;
import com.tweaker.edm.interfaces.managers.PersistanceManager;
import com.tweaker.edm.interfaces.managers.WorkerPoolManager;
import com.tweaker.edm.persisitance.DownloadPersistanceManager;

public abstract class AbstractWorkerPoolManager implements WorkerPoolManager {

    private static final Logger LOGGER = Logger.getLogger(AbstractWorkerPoolManager.class.getName());

    protected static final int MAX_PARALLEL_WORKERS = 5;

    protected State managerState = State.STOPPED;

    private DownloadData downloadData;
    protected Queue<Worker> waitingWorkers = new LinkedList<>();
    protected Queue<Worker> activeWorkers = new LinkedList<>();

    protected abstract void activateWorkers();

    private void fetchAndActivateWorkers() throws DataReadException {
        getWorkersFromPersistanceManager();
        if (waitingWorkers.isEmpty()) {
            managerState = State.STOPPED;
        } else {
            activateWorkers();
        }
    }

    private void getWorkersFromPersistanceManager() throws DataReadException {
        downloadData = getPersistanceManager().getPersistedData();
        for(Download download : downloadData.getDownloads()){
            waitingWorkers.addAll(download.getIncompleteChunks());
        }
    }

    protected PersistanceManager<DownloadData> getPersistanceManager() {
        return DownloadPersistanceManager.getInstance();
    }

    @Override
    public State queryManagerState() {
        return managerState;
    }

    @Override
    public void startProcessing() throws DownloadManagerException {
        if (managerState == State.STARTED) {
            LOGGER.warning("Pool manager is already STARTED");
            return;
        }
        managerState = State.STARTED;
        fetchAndActivateWorkers();
    }

    private void stopAllActiveWorkers() {
        for (Worker worker : activeWorkers) {
            worker.stop();
        }
        activeWorkers.clear();
    }

    @Override
    public void stopProcessing() throws DataWriteException {
        if (managerState == State.STOPPED) {
            LOGGER.warning("Pool manager is already STOPPED");
            return;
        }
        stopAllActiveWorkers();
        managerState = State.STOPPED;
        getPersistanceManager().persistData(downloadData);
    }

    @Override
    public <E extends Worker> void addWorkers(Collection<E> workers) {
        waitingWorkers.addAll(workers);
        if(managerState == State.STARTED){
            activateWorkers();
        }
    }
}
