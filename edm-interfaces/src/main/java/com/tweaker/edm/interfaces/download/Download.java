package com.tweaker.edm.interfaces.download;

import java.io.Serializable;
import java.util.Collection;

public interface Download extends Serializable{

    DownloadState getDownloadState();
    
    double getCompletionPercent();
    
    double getTotalBytes();
    
    double getCompletedBytes();
    
    Collection<DownloadChunk> getChunks();
}
