package com.tweaker.edm.interfaces.download;

import java.io.File;
import java.io.Serializable;
import java.util.Collection;

public interface Download extends Serializable{

    DownloadState getDownloadState();
    
    double getCompletionPercent();
    
    long getTotalBytes();
    
    long getCompletedBytes();
    
    Collection<DownloadChunk> getIncompleteChunks();

    Collection<DownloadChunk> getAllChunks();
    
    void startDownload();

    void stopDownload();

    void deleteDownload();

    File getLocalFile();
}
