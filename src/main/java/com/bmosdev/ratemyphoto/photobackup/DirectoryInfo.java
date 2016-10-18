package com.bmosdev.ratemyphoto.photobackup;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DirectoryInfo extends AbstractInfo {
    
    private final List<DirectoryInfo> subDirs = new ArrayList<DirectoryInfo>();
    private final List<FileInfo> files = new ArrayList<FileInfo>();
    
    public DirectoryInfo(File path) {
        super(path);
    }
    
    public List<FileInfo> getFiles() {
        return files;
    }
    
    public List<DirectoryInfo> getSubDirs() {
        return subDirs;
    }

    public void addFile(FileInfo file) {
        files.add(file);
    }

    public void addSubDir(DirectoryInfo directory) {
        subDirs.add(directory);
    }
}
