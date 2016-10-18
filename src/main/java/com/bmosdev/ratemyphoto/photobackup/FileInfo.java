package com.bmosdev.ratemyphoto.photobackup;

import java.io.File;

public class FileInfo extends AbstractInfo {
    
    private final long size;
    
    public FileInfo(File path) {
        super(path);
        this.size = path.length();
    }

    public long getSize() {
        return size;
    }
}
