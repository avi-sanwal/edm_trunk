package com.tweaker.edm.persisitance;

import static org.easymock.EasyMock.createMockBuilder;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.*;

import com.tweaker.edm.common.dto.DownloadData;
import com.tweaker.edm.exceptions.DownloadManagerException;
import com.tweaker.edm.exceptions.persistance.DataReadException;
import com.tweaker.edm.interfaces.download.Download;
import org.easymock.EasyMock;
import org.easymock.IMockBuilder;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class AbstractPersistanceManagerTest {

    private String testValidDataFile = "src/test/resources/valid.data";
    private String testMissingFile = "src/test/resources/non-existent.data";

    private AbstractPersistanceManager<DownloadData> pManager;
    private static IMockBuilder<AbstractPersistanceManager> managerBuilder;

    private static DownloadData testData = new DownloadData();

    @BeforeClass
    public static void setupBeforeClass() {
        testData.addDownload(mock(Download.class));
        managerBuilder = createMockBuilder(AbstractPersistanceManager.class);
    }

    @Before
    public void setUp() throws Exception {
        pManager = managerBuilder.createMock();
    }

    @Test(expected = DataReadException.class)
    public void shouldThrowExceptionForInvalidDataFile() throws DataReadException {
        expect(pManager.getDataFile()).andThrow(new ClassCastException("just for a test"));
        replay(pManager);
        pManager.getPersistedData();
    }

    @Test(expected = DataReadException.class)
    public void shouldThrowExceptionForMissingDataFile() throws DataReadException {
        expect(pManager.getDataFile()).andReturn(testMissingFile);
        replay(pManager);
        pManager.getPersistedData();
    }

    @Test @Ignore
    public void shouldReadDataFileFromValidFile() throws DataReadException {
        expect(pManager.getDataFile()).andReturn(testValidDataFile).once();
        replay(pManager);
        pManager.getPersistedData();
        verify(pManager);
    }
    
    @Test @Ignore
    public void shouldPersistData() throws DownloadManagerException {
        expect(pManager.getDataFile()).andReturn(testValidDataFile).once();
        replay(pManager);
        pManager.persistData(testData);
        verify(pManager);
        getPersistedTestData(testValidDataFile, testData);
    }

    private void getPersistedTestData(String dataFile, DownloadData testData) throws DataReadException {
        EasyMock.reset(pManager);
        expect(pManager.getDataFile()).andReturn(dataFile);
        replay(pManager);
        assertEquals(testData, pManager.getPersistedData());
    }
}
