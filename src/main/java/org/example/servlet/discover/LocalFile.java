package org.example.servlet.discover;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LocalFile {
    private final String path;
    private final long size;
    private final String date;

    public LocalFile(File file) {
        this.path = file.getName();
        this.size = file.isFile() ? file.length() : 0;
        this.date = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")
                .format(new Date(file.lastModified()));
    }

    public String getPath() { return path; }
    public long getSize() { return size; }
    public String getDate() { return date; }
}
