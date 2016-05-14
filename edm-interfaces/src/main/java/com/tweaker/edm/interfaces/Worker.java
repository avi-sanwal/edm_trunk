package com.tweaker.edm.interfaces;

import java.io.Serializable;

import com.tweaker.edm.interfaces.events.WorkerEventListener;

public interface Worker extends Comparable<Worker>, Runnable, Serializable{
	
	void start();
	
	void stop();

	void addEventListener(WorkerEventListener<Worker> eventListener);
}
