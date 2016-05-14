package com.tweaker.edm.queuemanager;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.easymock.EasyMock;
import org.easymock.IMockBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.tweaker.edm.interfaces.Worker;
import com.tweaker.edm.interfaces.managers.PersistanceManager;
import com.tweaker.edm.interfaces.managers.WorkerPoolManager;

public class AbstractWorkerPoolManagerTest {
    
    private WorkerPoolManager workerManager;
    private static IMockBuilder<AbstractWorkerPoolManager> managerBuilder;
    private PersistanceManager mockPersistanceManager;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        managerBuilder = EasyMock.createMockBuilder(AbstractWorkerPoolManager.class);
        managerBuilder.addMockedMethod("getPersistanceManager");
    }

    @Before
    public void setUp() throws Exception {
        workerManager = managerBuilder.createMock();
        mockPersistanceManager = EasyMock.createMock(PersistanceManager.class);
    }

    @Test
    public void shouldNotStartProcessingForEmptyList() {
        Queue<Worker> testWorkers = new LinkedBlockingQueue<>();
        expect(((AbstractWorkerPoolManager) workerManager).getPersistanceManager()).andReturn(mockPersistanceManager);
        expect(mockPersistanceManager.getPersistedWorkers()).andReturn(testWorkers);
        replay(workerManager, mockPersistanceManager);
        workerManager.startProcessing();
        verify(workerManager, mockPersistanceManager);
        Assert.assertEquals(WorkerPoolManager.State.STOPPED, workerManager.queryManagerState());
    }

}
