/**
 * Sự kiện file
 * @author Danh, To Huu
 */

package com.danh.common.model;

import java.io.Serializable;
import java.util.Date;

public class FileEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    private String fileName;
    private String filePath;
    private String eventType; // CREATE, MODIFY, DELETE
    private long timestamp;

    public FileEvent(String fileName, String filePath, String eventType) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.eventType = eventType;
        this.timestamp = System.currentTimeMillis();
    }

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getEventType() {
        return eventType;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Date getEventTime() {
        return new Date(timestamp);
    }

    @Override
    public String toString() {
        return String.format("[%s] File: %s - Path: %s", eventType, fileName, filePath);
    }
}