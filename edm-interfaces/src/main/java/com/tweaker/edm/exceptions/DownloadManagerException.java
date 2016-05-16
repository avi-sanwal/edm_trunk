package com.tweaker.edm.exceptions;

public class DownloadManagerException extends Exception {

    private static final long serialVersionUID = 3096064219590403377L;

    public DownloadManagerException(String message, Throwable t) {
        super(message, t);
    }

    public DownloadManagerException(String message) {
        super(message);
    }

}
