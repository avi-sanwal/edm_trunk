package com.tweaker.edm.queuemanager;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Collection;
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

    private AbstractWorkerPoolManager workerManager;
    private static IMockBuilder<AbstractWorkerPoolManager> managerBuilder;
    private PersistanceManager mockPersistanceManager;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        managerBuilder = EasyMock.createMockBuilder(AbstractWorkerPoolManager.class);
        managerBuilder.addMockedMethod("getPersistanceManager").addMockedMethod("activateWorkers");
    }

    @Before
    public void setUp() throws Exception {
        workerManager = managerBuilder.createMock();
        mockPersistanceManager = EasyMock.createMock(PersistanceManager.class);
    }

    @Test
    public void shouldNotStartProcessingWhenAlreadyStarted() {
        workerManager.managerState = WorkerPoolManager.State.STARTED;
        workerManager.startProcessing();
        Assert.assertEquals(WorkerPoolManager.State.STARTED, workerManager.queryManagerState());
    }

    @Test
    public void shouldNotStartProcessingForEmptyList() {
        Queue<Worker> testWorkers = new LinkedBlockingQueue<>();
        expect(workerManager.getPersistanceManager()).andReturn(mockPersistanceManager).once();
        expect(mockPersistanceManager.getPersistedWorkers()).andReturn(testWorkers).once();
        replay(workerManager, mockPersistanceManager);
        workerManager.startProcessing();
        verify(workerManager, mockPersistanceManager);
        Assert.assertEquals(WorkerPoolManager.State.STOPPED, workerManager.queryManagerState());
    }

    @Test
    public void shouldStartProcessingForNonEmptyWorkers() {
        Queue<Worker> testWorkers = createTestWorkers(2);
        expect(workerManager.getPersistanceManager()).andReturn(mockPersistanceManager).once();
        expect(mockPersistanceManager.getPersistedWorkers()).andReturn(testWorkers).once();
        workerManager.activateWorkers();
        EasyMock.expectLastCall().once();
        replay(workerManager, mockPersistanceManager);
        workerManager.startProcessing();
        verify(workerManager, mockPersistanceManager);
        Assert.assertEquals(WorkerPoolManager.State.STARTED, workerManager.queryManagerState());
    }

    @Test
    public void shouldNotStopProcessingWhenAlreadyStopped() {
        workerManager.managerState = WorkerPoolManager.State.STOPPED;
        workerManager.stopProcessing();
        Assert.assertEquals(WorkerPoolManager.State.STOPPED, workerManager.queryManagerState());
    }

    @Test
    public void shouldStopProcessingAndPersistWorkerData() {
        workerManager.managerState = WorkerPoolManager.State.STARTED;
        workerManager.activeWorkers = createTestWorkers(2);
        expect(workerManager.getPersistanceManager()).andReturn(mockPersistanceManager).once();
        mockPersistanceManager.persistWorkers();
        EasyMock.expectLastCall().once();
        setWorkerStopExpectations(workerManager.activeWorkers);
        replay(workerManager, mockPersistanceManager);
        workerManager.stopProcessing();
        verify(workerManager, mockPersistanceManager);
        verifyWorkers(workerManager.activeWorkers);
        Assert.assertEquals(WorkerPoolManager.State.STOPPED, workerManager.queryManagerState());
        Assert.assertTrue(workerManager.activeWorkers.isEmpty());
    }

    private void verifyWorkers(Collection<Worker> activeWorkers) {
        for (Worker w : activeWorkers) {
            verify(w);
        }
    }

    private void setWorkerStopExpectations(Collection<Worker> activeWorkers) {
        for (Worker w : activeWorkers) {
            w.stop();
            EasyMock.expectLastCall().once();
            replay(w);
        }
    }

    private Queue<Worker> createTestWorkers(int howMany) {
        Queue<Worker> testWorkers = new LinkedBlockingQueue<>(howMany);
        for (int i = 0; i < howMany; i++) {
            Worker mockWorker = EasyMock.createMock(Worker.class);
            testWorkers.add(mockWorker);
        }
        return testWorkers;
    }
}
