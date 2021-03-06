package com.tweaker.edm.common.dto;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import com.tweaker.edm.appconfig.ApplicationConfigurationImpl;
import com.tweaker.edm.interfaces.config.ApplicationConfiguration;
import com.tweaker.edm.interfaces.download.Download;
import com.tweaker.edm.interfaces.download.DownloadChunk;
import com.tweaker.edm.interfaces.download.DownloadState;

public abstract class AbstractDownload implements Download {

    private static final long serialVersionUID = 2573383961400716640L;

    private File localFile;
    protected DownloadState state = DownloadState.NEW;
    protected long totalBytes = 0;
    private Collection<DownloadChunk> chunks;

    private String completedFilePath;

    public AbstractDownload() {
        completedFilePath = determineDownloadFileName();
    }

    protected abstract String determineDownloadFileName();

    @Override
    public DownloadState getDownloadState() {
        return state;
    }

    @Override
    public double getCompletionPercent() {
        if (totalBytes == 0) {
            return 0;
        }
        return (double) getCompletedBytes() / (double) totalBytes * 100;
    }

    @Override
    public long getTotalBytes() {
        return totalBytes;
    }

    @Override
    public long getCompletedBytes() {
        long downloadedBytes = 0;
        for (DownloadChunk c : getAllChunks()) {
            downloadedBytes += c.getCompletedBytes();
        }
        return downloadedBytes;
    }

    @Override
    public Collection<DownloadChunk> getIncompleteChunks() {
        Collection<DownloadChunk> incompleteChunks = new ArrayList<>();
        for (DownloadChunk dc : getAllChunks()) {
            if (dc.getChunkState() != DownloadState.COMPLETED) {
                incompleteChunks.add(dc);
            }
        }
        return incompleteChunks;
    }

    @Override
    public Collection<DownloadChunk> getAllChunks() {
        return chunks;
    }

    @Override
    public File getLocalFile() {
        if (localFile == null) {
            String fullFilePath = getAppConfig().getCompletedDownloadDirectory().getAbsolutePath()
                    + File.separator + determineDownloadFileName();
            localFile = new File(fullFilePath);
        }
        return localFile;
    }

    protected ApplicationConfiguration getAppConfig() {
        return ApplicationConfigurationImpl.INSTANCE;
    }

    public String getCompletedFilePath() {
        return completedFilePath;
    }

    public void setCompletedFilePath(String completedFilePath) {
        this.completedFilePath = completedFilePath;
    }
}
