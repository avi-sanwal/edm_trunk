package com.tweaker.edm.common.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import com.tweaker.edm.interfaces.download.Download;

public class DownloadData implements Serializable {

    private static final long serialVersionUID = 4215418493951059913L;

    private Collection<Download> downloads = new ArrayList<>();

    public Collection<Download> getDownloads() {
        return downloads;
    }

    public void setDownloads(Collection<Download> downloads) {
        this.downloads = downloads;
    }

    public void addDownload(Download download) {
        downloads.add(download);
    }

    public void removeDownload(Download download) {
        downloads.remove(download);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DownloadData)) {
            return false;
        }
        DownloadData otherData = (DownloadData) obj;
        return Arrays.equals(otherData.getDownloads().toArray(new Download[0]),
                this.downloads.toArray(new Download[0]));
    }

    @Override
    public int hashCode() {
        return downloads.hashCode();
    }
}
