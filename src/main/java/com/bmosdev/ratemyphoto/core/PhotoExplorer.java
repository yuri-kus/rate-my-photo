package com.bmosdev.ratemyphoto.core;

import com.bmosdev.ratemyphoto.model.Photo;
import com.bmosdev.ratemyphoto.photobackup.FileInfo;
import com.bmosdev.ratemyphoto.util.HashHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PhotoExplorer {

    private static final Logger log = LogManager.getLogger();

    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList(".nef");

    private final Map<String, Photo> photos = new ConcurrentHashMap<>();
    private volatile List<Photo> sortedPhotos;

    private final Comparator<Photo> photoComparator = (o1, o2) -> o1.getFileInfo().getName().compareTo(o2.getFileInfo().getName());

    public void clear() {
        photos.clear();
        if (sortedPhotos != null) {
            sortedPhotos.clear();
        }
    }

    public void explore(String rootPath) {
        clear();

        File rootDirectory = new File(rootPath);
        if (!rootDirectory.isDirectory()) {
            log.info("Root directory {} is not directory");
            return;
        }

        File[] files = rootDirectory.listFiles((file) -> {
            String name = file.getName().toLowerCase();
            for (String extension : ALLOWED_EXTENSIONS) {
                if (name.endsWith(extension.toLowerCase())) {
                    return true;
                }
            }
            return false;
        });

        if (files == null) {
            log.warn("error processing [" + rootDirectory + "]. skipping");
        } else {
            NefReader nefReader = new NefReader();
            for (File file : files) {
                FileInfo fileInfo = new FileInfo(file);
                String id = generatePhotoId(fileInfo);
                try {
                    Photo.ImageInfo imageInfo = nefReader.readImageInfo(fileInfo);
                    Photo photo = new Photo(id, fileInfo, imageInfo);
                    photos.put(photo.getId(), photo);
                } catch (IOException e) {
                    log.error("Error reading photo " + fileInfo, e);
                }
            }
        }

        sortedPhotos = new ArrayList<>(photos.values());
        Collections.sort(sortedPhotos, photoComparator);
    }

    private String generatePhotoId(FileInfo fileInfo) {
        try {
            return HashHelper.calculateMd5(fileInfo.getPath().getCanonicalPath().toString());
        } catch (IOException e) {
            throw new RuntimeException("Error generating photoId for " + fileInfo, e);
        }
    }

//    public void explore(String root) {
//        photos.clear();
//
//        NefReader nefReader = new NefReader();
//        DirectoryInfo directoryInfo = new DirectoryExplorer().explore(new File(root), asList(".nef"));
//        directoryInfo.getFiles().stream().sorted((f1, f2) -> f1.getName().compareTo(f2.getName())).forEach(fileInfo -> {
//            log.debug("file: {}", fileInfo);
//            try {
//                Photo photo = nefReader.readPhotoInfo(fileInfo);
//                photos.put(photo.getId(), photo);
//            } catch (IOException e) {
//                log.warn("Error reading photo " + fileInfo, e);
//            }
//        });
//
//        sortedPhotos = new ArrayList<>(photos.values());
//        Collections.sort(sortedPhotos, photoComparator);
//    }

    public Photo getPhoto(String photoId) {
        return photos.get(photoId);
    }

    public List<Photo> getPhotos() {
        return sortedPhotos;
    }
}
