package com.tweaker.edm.exceptions.persistance;

import com.tweaker.edm.exceptions.DownloadManagerException;

public class DataReadException extends DownloadManagerException {

    private static final long serialVersionUID = 6906708505633759010L;

    public DataReadException(String message, Throwable t) {
        super(message, t);
    }

    public DataReadException(String message) {
        super(message);
    }

}
