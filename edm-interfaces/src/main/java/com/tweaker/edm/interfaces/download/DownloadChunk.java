package com.tweaker.edm.interfaces.download;

import com.tweaker.edm.interfaces.Worker;

public interface DownloadChunk extends Worker {
    
    DownloadState getChunkState();
    
    double getCompletionPercent();
    
    double getTotalBytes();
    
    double getCompletedBytes();
}
