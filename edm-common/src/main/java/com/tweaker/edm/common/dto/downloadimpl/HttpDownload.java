package com.tweaker.edm.common.dto.downloadimpl;

import java.io.File;

import com.tweaker.edm.common.dto.AbstractDownload;
import com.tweaker.edm.interfaces.download.DownloadState;
import com.tweaker.edm.interfaces.managers.WorkerPoolManager;

public class HttpDownload extends AbstractDownload {

    private static final long serialVersionUID = 3089204306327085953L;

    @Override
    public void startDownload() {
        DownloadState currentState = getDownloadState();
        if (currentState == DownloadState.ONGOING || currentState == DownloadState.WAITING) {
            return;
        }
        setDownloadState(DownloadState.WAITING);
        getCurrentPoolManager().addWorkers(this.getIncompleteChunks());
    }

    private WorkerPoolManager getCurrentPoolManager() {
        return null;
    }

    @Override
    public void stopDownload() {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteDownload() {
        // TODO Auto-generated method stub

    }

    @Override
    public File getLocalFile() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String determineDownloadFileName() {
        // TODO Auto-generated method stub
        return null;
    }

    protected void setDownloadState(DownloadState state) {
        this.state = state;
    }

}
