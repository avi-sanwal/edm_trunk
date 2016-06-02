package com.tweaker.edm.common.dto;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Collections;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import com.tweaker.edm.interfaces.config.ApplicationConfiguration;
import com.tweaker.edm.interfaces.download.DownloadChunk;

public class AbstractDownloadTest {

    private AbstractDownload absDownload;

    @Before
    public void setUp() throws Exception {
        absDownload = EasyMock.createMockBuilder(AbstractDownload.class)
                .addMockedMethods("getAllChunks", "getAppConfig").createMock();
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
        EasyMock.expect(mockChunk.getCompletedBytes()).andReturn(new Long(10)).once();
        EasyMock.replay(mockChunk, absDownload);
        double percent = absDownload.getCompletionPercent();
        assertEquals(10, percent, 0.001);
        EasyMock.verify(mockChunk, absDownload);
    }

    @Test
    public void shouldGetLocalFile() {
        ApplicationConfiguration mockConfig = EasyMock.createMock(ApplicationConfiguration.class);
        EasyMock.expect(absDownload.determineDownloadFileName()).andReturn("test.dnld").once();
        EasyMock.expect(absDownload.getAppConfig()).andReturn(mockConfig).anyTimes();
        EasyMock.expect(mockConfig.getCompletedDownloadDirectory()).andReturn(new File("/tmp/")).once();
        EasyMock.replay(absDownload, mockConfig);
        File localFile = absDownload.getLocalFile();
        assertEquals(new File("/tmp/test.dnld").getAbsolutePath(), localFile.getAbsolutePath());
        EasyMock.verify(absDownload, mockConfig);
    }

}
