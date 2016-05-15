package com.tweaker.edm.common.dto;

import static org.junit.Assert.*;

import java.util.Collections;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import com.tweaker.edm.interfaces.download.DownloadChunk;

public class AbstractDownloadTest {
    
    private AbstractDownload absDownload;

    @Before
    public void setUp() throws Exception {
        absDownload = EasyMock.createMockBuilder(AbstractDownload.class).addMockedMethod("getAllChunks").createMock();
    }

    @Test
    public void shouldGetZeroPercentCompletedWhenNew() {
        double percent = absDownload.getCompletionPercent();
        assertEquals(0, percent, 0.001);
    }

    @Test
    public void shouldGetNonZeroPercentCompleted() {
        absDownload.totalBytes = 100;
        DownloadChunk mockChunk = EasyMock.createMock(DownloadChunk.class);
        EasyMock.expect(absDownload.getAllChunks()).andReturn(Collections.singleton(mockChunk)).once();
        EasyMock.expect(mockChunk.getCompletedBytes()).andReturn(10L).once();
        EasyMock.replay(mockChunk, absDownload);
        double percent = absDownload.getCompletionPercent();
        assertEquals(10, percent, 0.001);
        EasyMock.verify(mockChunk, absDownload);
    }
}
