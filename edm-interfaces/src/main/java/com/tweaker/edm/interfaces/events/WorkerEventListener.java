package com.tweaker.edm.interfaces.events;

@FunctionalInterface
public interface WorkerEventListener <E>{
    void eventTriggered(E source);
}
