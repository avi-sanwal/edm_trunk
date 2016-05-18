package com.tweaker.edm.queuemanager;

import com.tweaker.edm.interfaces.Worker;

public final class SimpleQueueManager extends AbstractWorkerPoolManager {

    private static SimpleQueueManager instance;
    
    private SimpleQueueManager(){
        //Singleton
    }
    
    public static SimpleQueueManager getInstance(){
        if(instance == null){
            instance = new SimpleQueueManager();
        }
        return instance;
    }
    
    @Override
    public String getManagerDescription() {
        return "A stupid implementation. Uses blind queue.";
    }

    @Override
    protected void activateWorkers() {
        while(activeWorkers.size() < MAX_PARALLEL_WORKERS && !waitingWorkers.isEmpty()){
            Worker polledWorker = waitingWorkers.poll();
            activeWorkers.add(polledWorker);
            polledWorker.start();
        }
        
    }

}
