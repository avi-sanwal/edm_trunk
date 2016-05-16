package com.tweaker.edm.exceptions.persistance;

import com.tweaker.edm.exceptions.DownloadManagerException;

public class DataWriteException extends DownloadManagerException {

    private static final long serialVersionUID = 6906708505633759010L;

    public DataWriteException(String message, Throwable t) {
        super(message, t);
    }

    public DataWriteException(String message) {
        super(message);
    }

}
