package com.tweaker.edm.common.dto.downloadimpl;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;
import java.util.Collection;

import org.easymock.EasyMock;
import org.easymock.IMockBuilder;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.tweaker.edm.interfaces.download.DownloadChunk;
import com.tweaker.edm.interfaces.download.DownloadState;
import com.tweaker.edm.interfaces.managers.WorkerPoolManager;

public class HttpDownloadTest {

    private HttpDownload testDownload;
    private static IMockBuilder<HttpDownload> mockBuilder;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        mockBuilder = EasyMock.createMockBuilder(HttpDownload.class).addMockedMethod("setDownloadState")
                .addMockedMethod("getDownloadState").addMockedMethod("getIncompleteChunks")
                .addMockedMethod("getCurrentPoolManager");
    }

    @Before
    public void setUp() throws Exception {
        testDownload = mockBuilder.createMock();
    }

    @Test
    public void shouldNotStartDownloadIfAlreadyOngoing() {
        expect(testDownload.getDownloadState()).andReturn(DownloadState.ONGOING).once();
        replay(testDownload);
        testDownload.startDownload();
        verify(testDownload);
    }

    @Test
    public void shouldNotStartDownloadIfWaiting() {
        expect(testDownload.getDownloadState()).andReturn(DownloadState.WAITING).once();
        replay(testDownload);
        testDownload.startDownload();
        verify(testDownload);
    }

    @Test
    public void shouldStartDownload() {
        Collection<DownloadChunk> chunks = createMockChunks(2);
        WorkerPoolManager mockPM = EasyMock.createMock(WorkerPoolManager.class);
        expect(testDownload.getDownloadState()).andReturn(DownloadState.NEW).once();
        testDownload.setDownloadState(DownloadState.WAITING);
        expectLastCall().once();
        expect(testDownload.getIncompleteChunks()).andReturn(chunks).once();
        expect(testDownload.getCurrentPoolManager()).andReturn(mockPM).once();
        mockPM.addWorkers(chunks);
        expectLastCall().once();
        replay(testDownload, mockPM);
        testDownload.startDownload();
        verify(testDownload, mockPM);
    }

    @Test
    public void testStopDownload() {
        Collection<DownloadChunk> chunks = createMockChunks(2);
        WorkerPoolManager mockPM = EasyMock.createMock(WorkerPoolManager.class);
        expect(testDownload.getDownloadState()).andReturn(DownloadState.ONGOING).once();
        testDownload.setDownloadState(DownloadState.STOPPED);
        expectLastCall().once();
        expect(testDownload.getIncompleteChunks()).andReturn(chunks).once();
        expect(testDownload.getCurrentPoolManager()).andReturn(mockPM).once();
        mockPM.stopWorkers(chunks);
        expectLastCall().once();
        replay(testDownload, mockPM);
        testDownload.stopDownload();
        verify(testDownload, mockPM);
    }

    @Test
    public void shouldDeleteDownload() {
        testDownload = EasyMock.createMockBuilder(HttpDownload.class).addMockedMethod("stopDownload")
                .addMockedMethod("getAllChunks").createMock();
        Collection<DownloadChunk> chunks = createMockChunks(2);
        testDownload.stopDownload();
        expectLastCall().once();
        expect(testDownload.getAllChunks()).andReturn(chunks).once();
        setDeleteExpectationForChunks(chunks);
        replay(testDownload);
        testDownload.deleteDownload();
        verify(testDownload);
        verifyChunks(chunks);
    }

    private void setDeleteExpectationForChunks(Collection<DownloadChunk> chunks) {
        for (DownloadChunk chunk : chunks) {
            chunk.delete();
            expectLastCall().once();
            replay(chunk);
        }
    }

    private void verifyChunks(Collection<DownloadChunk> chunks) {
        for (DownloadChunk chunk : chunks) {
            verify(chunk);
        }
    }

    private Collection<DownloadChunk> createMockChunks(int n) {
        Collection<DownloadChunk> testChunks = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            testChunks.add(EasyMock.createMock(DownloadChunk.class));
        }
        return testChunks;
    }
}
