package com.tweaker.edm.queuemanager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Queue;
import java.util.logging.Logger;

import com.tweaker.edm.common.PersistanceManagerImpl;
import com.tweaker.edm.interfaces.Worker;
import com.tweaker.edm.interfaces.managers.PersistanceManager;
import com.tweaker.edm.interfaces.managers.WorkerPoolManager;

public abstract class AbstractWorkerPoolManager implements WorkerPoolManager {

    private static final Logger LOGGER = Logger.getLogger(AbstractWorkerPoolManager.class.getName());

    protected static final int MAX_PARALLEL_WORKERS = 5;

    protected State managerState = State.STOPPED;

    protected Queue<Worker> waitingWorkers;
    protected Collection<Worker> activeWorkers = new ArrayList<>();

    protected abstract void activateWorkers();

    private void fetchAndActivateWorkers() {
        waitingWorkers = getPersistanceManager().getPersistedWorkers();
        if (waitingWorkers.isEmpty()) {
            managerState = State.STOPPED;
        } else {
            activateWorkers();
        }
    }

    protected PersistanceManager getPersistanceManager() {
        return PersistanceManagerImpl.INSTANCE;
    }

    @Override
    public State queryManagerState() {
        return managerState;
    }

    @Override
    public void startProcessing() {
        if (managerState == State.STARTED) {
            LOGGER.warning("Pool manager is already STARTED");
            return;
        }
        managerState = State.STARTED;
        fetchAndActivateWorkers();
    }

    @Override
    public void stopProcessing() {
        if (managerState == State.STOPPED) {
            LOGGER.warning("Pool manager is already STOPPED");
            return;
        }
        stopAllActiveWorkers();
        managerState = State.STOPPED;
        getPersistanceManager().persistWorkers();
    }

    private void stopAllActiveWorkers() {
        for (Worker worker : activeWorkers) {
            worker.stop();
        }
        activeWorkers.clear();
    }

}
