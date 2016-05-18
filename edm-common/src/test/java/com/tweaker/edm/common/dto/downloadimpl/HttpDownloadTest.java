package com.tweaker.edm.common.dto.downloadimpl;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;

import static org.easymock.EasyMock.*;

import org.easymock.EasyMock;
import org.easymock.IMockBuilder;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.tweaker.edm.interfaces.download.DownloadChunk;
import com.tweaker.edm.interfaces.download.DownloadState;

public class HttpDownloadTest {
    
    private HttpDownload testDownload;
    private static IMockBuilder<HttpDownload> mockBuilder;
    private Collection<DownloadChunk> chunks;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        mockBuilder = EasyMock.createMockBuilder(HttpDownload.class).addMockedMethod("setDownloadState")
                .addMockedMethod("getDownloadState");
    }

    @Before
    public void setUp() throws Exception {
        testDownload = mockBuilder.createMock();
        chunks = createMockChunks(2);
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
    
    @Test @Ignore
    public void shouldStartDownload() {
        expect(testDownload.getDownloadState()).andReturn(DownloadState.NEW).once();
        testDownload.setDownloadState(DownloadState.WAITING);
        expectLastCall().once();
        setStartExpectationOnChunks();
        replay(testDownload);
        testDownload.startDownload();
        verify(testDownload);
        verifyChunks();
    }


    @Test @Ignore
    public void testStopDownload() {
        fail("Not yet implemented");
    }

    @Test @Ignore
    public void testDeleteDownload() {
        fail("Not yet implemented");
    }

    @Test @Ignore
    public void testGetLocalFile() {
        fail("Not yet implemented");
    }

    private Collection<DownloadChunk> createMockChunks(int n) {
        Collection<DownloadChunk> testChunks = new ArrayList<>();
        for(int i=0;i<n;i++){
            testChunks.add(EasyMock.createMock(DownloadChunk.class));
        }
        return testChunks;
    }
    private void verifyChunks() {
        chunks.iterator().forEachRemaining(new Consumer<DownloadChunk>() {
            
            @Override
            public void accept(DownloadChunk t) {
                verify(t);
            }
            
        });
    }
    
    private void setStartExpectationOnChunks() {
        chunks.iterator().forEachRemaining(new Consumer<DownloadChunk>() {
            
            @Override
            public void accept(DownloadChunk t) {
                t.start();
                expectLastCall().once();
                replay(t);
            }
            
        });
    }
}
