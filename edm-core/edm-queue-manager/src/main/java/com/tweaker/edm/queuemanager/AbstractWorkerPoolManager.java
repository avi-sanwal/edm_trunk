package com.tweaker.edm.queuemanager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Queue;

import com.tweaker.edm.common.PersistanceManagerImpl;
import com.tweaker.edm.interfaces.Worker;
import com.tweaker.edm.interfaces.managers.PersistanceManager;
import com.tweaker.edm.interfaces.managers.WorkerPoolManager;

public abstract class AbstractWorkerPoolManager implements WorkerPoolManager {
    
    private static final int MAX_PARALLEL_WORKERS = 5;
    
    private State managerState = State.STOPPED;
    
    protected Queue<Worker> waitingWorkers;
    protected Collection<Worker> activeWorkers;

	private void activateWorkers() {
        do {
            activeWorkers.add(waitingWorkers.poll());
        } while (activeWorkers.size() < MAX_PARALLEL_WORKERS && !waitingWorkers.isEmpty());
    }

    private void fetchAndActivateWorkers() {
        waitingWorkers = getPersistanceManager().getPersistedWorkers();
        if(waitingWorkers.isEmpty()){
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
	    if(managerState == State.STARTED){
	        return;
	    }
	    activeWorkers = new ArrayList<>();
	    managerState = State.STARTED;
		fetchAndActivateWorkers();
	}

}
