package com.tweaker.edm.interfaces.download;

import java.util.Collection;

public interface Download {

    DownloadState getDownloadState();
    
    double getCompletionPercent();
    
    double getTotalBytes();
    
    double getCompletedBytes();
    
    Collection<DownloadChunk> createChunks();
}
