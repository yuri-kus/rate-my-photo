package com.bmosdev.ratemyphoto.util;

import com.bmosdev.ratemyphoto.photobackup.*;
import com.bmosdev.ratemyphoto.photobackup.SyncCommand.Action;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.*;

public class DirectoryExplorer {

    private static final Logger log = LogManager.getLogger();
    
    public DirectoryInfo explore(File rootDirectory, final List<String> allowedExtensions) {
        if (!rootDirectory.isDirectory()) {
            throw new IllegalArgumentException(rootDirectory + " should be directory");
        }
        DirectoryInfo directoryInfo = new DirectoryInfo(rootDirectory);
        
        File[] files;
        if (allowedExtensions != null) {
            FileFilter filter = new FileFilter() {
                public boolean accept(File file) {
                    if (file.isDirectory()) {
                        return true;
                    } else {
                        String name = file.getName().toLowerCase();
                        for (String extension : allowedExtensions) {
                            if (name.endsWith(extension.toLowerCase())) {
                                return true;
                            }
                        }
                        return false;
                    }
                }
            };
            files = rootDirectory.listFiles(filter);
        } else {
            files = rootDirectory.listFiles();
        }

        if (files == null) {
            log.warn("error processing [" + rootDirectory + "]. skipping");
        } else {
            for (File file : files) {
                if (file.isFile()) {
                    directoryInfo.addFile(new FileInfo(file));
                } else if (file.isDirectory()) {
                    DirectoryInfo directory = explore(file, allowedExtensions);
                    directoryInfo.addSubDir(directory);
                }
            }
        }
        
        return directoryInfo;
    }
}
