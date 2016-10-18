package com.bmosdev.ratemyphoto.web.actions;

import com.bmosdev.ratemyphoto.web.JpegBinary;
import com.bmosdev.ratemyphoto.core.NefReader;
import com.bmosdev.ratemyphoto.model.Photo;
import com.bmosdev.ratemyphoto.core.PhotoExplorer;
import com.bmosdev.ratemyphoto.web.Action;
import com.bmosdev.ratemyphoto.web.Template;
import com.bmosdev.ratemyphoto.web.TemplatePaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class BaseAction {

    private static final Logger log = LogManager.getLogger();

    private static final PhotoExplorer photoExplorer = new PhotoExplorer();

//    static {
//        photoExplorer.explore("E:/garbage/-photos");
//    }

    public String photoId;
    public Integer offset;
    public Integer count;

    public String rootDir;

    private final HttpSession httpSession;

    public BaseAction(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    @Action(path = "/")
	public Object home() {

        log.info("session id: {}", httpSession.getId());

        photoExplorer.clear();

//        List<Photo> photos = photoExplorer.getPhotos();
//        if (offset != null) {
//            if (offset >= photos.size()) {
//                photos = Collections.emptyList();
//            } else {
//                photos = photos.subList(offset, photos.size());
//            }
//        }
//        if (count != null) {
//            photos = photos.subList(0, Math.max(count, photos.size()));
//        }
        return new Template(TemplatePaths.BODY,
        		"content", TemplatePaths.MAIN);
//        		"photos", photos);
	}

    @Action(path = "/photoList")
	public Object getPhotoList() throws IOException {
        photoExplorer.explore(rootDir);
        List<Photo> photos = photoExplorer.getPhotos();
        if (offset != null) {
            if (offset >= photos.size()) {
                photos = Collections.emptyList();
            } else {
                photos = photos.subList(offset, photos.size());
            }
        }
        if (count != null) {
            photos = photos.subList(0, Math.max(count, photos.size()));
        }
        return photos.toArray(new Photo[photos.size()]);
	}

    @Action(path = "/photo")
	public Object getFullSizePhoto() throws IOException {
        Photo photo = photoExplorer.getPhoto(photoId);
        NefReader nefReader = new NefReader();
        byte[] photoData = nefReader.readFullSizeImage(photo);
        return new JpegBinary(photoData);
	}

    @Action(path = "/thumbnail")
	public Object getThumbnail() throws IOException {
        Photo photo = photoExplorer.getPhoto(photoId);
        NefReader nefReader = new NefReader();
        byte[] photoData = nefReader.readThumbnail(photo);
        return new JpegBinary(photoData);
	}
}
