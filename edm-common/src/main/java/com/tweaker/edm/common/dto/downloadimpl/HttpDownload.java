package com.tweaker.edm.common.dto.downloadimpl;

import com.tweaker.edm.common.dto.AbstractDownload;
import com.tweaker.edm.interfaces.download.DownloadChunk;
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
        getCurrentPoolManager().addWorkers(getIncompleteChunks());
    }

    protected WorkerPoolManager getCurrentPoolManager() {
        return null;
    }

    @Override
    public void stopDownload() {
        if (getDownloadState() == DownloadState.STOPPED) {
            return;
        }
        setDownloadState(DownloadState.STOPPED);
        getCurrentPoolManager().stopWorkers(getIncompleteChunks());
    }

    @Override
    public void deleteDownload() {
        stopDownload();
        for (DownloadChunk chunk : getAllChunks()) {
            chunk.delete();
        }
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
