package com.tweaker.edm.queuemanager;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;
import java.util.Collection;

import org.easymock.EasyMock;
import org.easymock.IMockBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.tweaker.edm.common.dto.DownloadData;
import com.tweaker.edm.interfaces.Worker;
import com.tweaker.edm.interfaces.download.Download;
import com.tweaker.edm.interfaces.download.DownloadChunk;
import com.tweaker.edm.interfaces.managers.PersistanceManager;
import com.tweaker.edm.interfaces.managers.WorkerPoolManager;

public class AbstractWorkerPoolManagerTest {

    private AbstractWorkerPoolManager workerManager;
    private static IMockBuilder<AbstractWorkerPoolManager> managerBuilder;
    private PersistanceManager<DownloadData> mockPersistanceManager;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        managerBuilder = EasyMock.createMockBuilder(AbstractWorkerPoolManager.class);
        managerBuilder.addMockedMethod("getPersistanceManager").addMockedMethod("activateWorkers");
    }

    @Before
    public void setUp() throws Exception {
        workerManager = managerBuilder.createMock();
        mockPersistanceManager = EasyMock.createMock(PersistanceManager.class);
        workerManager.activeWorkers = new ArrayList<>();
        workerManager.waitingWorkers = new ArrayList<>();
    }

    @Test
    public void shouldNotStartProcessingWhenAlreadyStarted() {
        workerManager.managerState = WorkerPoolManager.State.STARTED;
        workerManager.startProcessing();
        Assert.assertEquals(WorkerPoolManager.State.STARTED, workerManager.queryManagerState());
    }

    @Test
    public void shouldNotStartProcessingForEmptyList() {
        DownloadData testData = new DownloadData();
        expect(workerManager.getPersistanceManager()).andReturn(mockPersistanceManager).once();
        expect(mockPersistanceManager.getPersistedData()).andReturn(testData).once();
        replay(workerManager, mockPersistanceManager);
        workerManager.startProcessing();
        verify(workerManager, mockPersistanceManager);
        Assert.assertEquals(WorkerPoolManager.State.STOPPED, workerManager.queryManagerState());
    }

    @Test
    public void shouldStartProcessingForNonEmptyWorkers() {
        DownloadData testData = createTestData(2, 2);
        expect(workerManager.getPersistanceManager()).andReturn(mockPersistanceManager).once();
        expect(mockPersistanceManager.getPersistedData()).andReturn(testData).once();
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
        Collection<Worker> testWorkers = createTestWorkers(2, true);
        workerManager.activeWorkers = testWorkers;
        expect(workerManager.getPersistanceManager()).andReturn(mockPersistanceManager).once();
        mockPersistanceManager.persistWorkers();
        EasyMock.expectLastCall().once();
        replay(workerManager, mockPersistanceManager);
        workerManager.stopProcessing();
        verify(workerManager, mockPersistanceManager);
        verifyWorkers(testWorkers);
        Assert.assertEquals(WorkerPoolManager.State.STOPPED, workerManager.queryManagerState());
        Assert.assertTrue(workerManager.activeWorkers.isEmpty());
    }

    private void verifyWorkers(Collection<Worker> activeWorkers) {
        for (Worker w : activeWorkers) {
            verify(w);
        }
    }

    private DownloadData createTestData(int howManyDownloads, int howManyWorkers) {
        Collection<Download> testDownloads = new ArrayList<>(howManyDownloads);
        for (int i = 0; i < howManyDownloads; i++) {
            Download mockDownload = EasyMock.createMock(Download.class);
            Collection<DownloadChunk> workers = createTestWorkers(howManyWorkers, false);
            expect(mockDownload.getIncompleteChunks()).andReturn(workers).once();
            replay(mockDownload);
            testDownloads.add(mockDownload);
        }
        DownloadData dd = new DownloadData();
        dd.setDownloads(testDownloads);
        return dd;
    }

    private <E extends Worker> Collection<E> createTestWorkers(int howManyWorkers, boolean expectStop) {
        Collection<E> workers = new ArrayList<>();
        for (int j = 0; j < howManyWorkers; j++) {
            @SuppressWarnings("unchecked")
            E mockWorker = (E) EasyMock.createMock(Worker.class);
            if(expectStop) {
                mockWorker.stop();
                EasyMock.expectLastCall().once();
            }
            replay(mockWorker);
            workers.add(mockWorker);
        }
        return workers;
    }
}
