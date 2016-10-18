package com.bmosdev.ratemyphoto.model;

import com.bmosdev.ratemyphoto.photobackup.FileInfo;
import com.bmosdev.ratemyphoto.web.JacksonBean;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.awt.*;

public class Photo implements JacksonBean {

    private final String id;
    private final FileInfo fileInfo;
    private final ImageInfo imageInfo;

    public Photo(String id, FileInfo fileInfo, ImageInfo imageInfo) {
        this.id = id;
        this.fileInfo = fileInfo;
        this.imageInfo = imageInfo;
    }

    public String getId() {
        return id;
    }

    public FileInfo getFileInfo() {
        return fileInfo;
    }

    public ImageInfo getImageInfo() {
        return imageInfo;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public static class ImageInfo {
        private final Orientation orientation;

        private final int fullWidth;
        private final int fullHeight;
        private final int thumbnailWidth;
        private final int thumbnailHeight;

        public ImageInfo(Dimension thumbnailDimension, Dimension fullImageDimension, Orientation orientation) {
            switch (orientation) {
                case TOP_LEFT:
                case BOTTOM_RIGHT:
                    this.thumbnailWidth = thumbnailDimension.width;
                    this.thumbnailHeight = thumbnailDimension.height;
                    this.fullWidth = fullImageDimension.width;
                    this.fullHeight = fullImageDimension.height;
                    break;
                case RIGHT_TOP:
                case LEFT_BOTTOM:
                    this.thumbnailWidth = thumbnailDimension.height;
                    this.thumbnailHeight = thumbnailDimension.width;
                    this.fullWidth = fullImageDimension.height;
                    this.fullHeight = fullImageDimension.width;
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported orientation: " + orientation);
            }
            this.orientation = orientation;
        }

        public Orientation getOrientation() {
            return orientation;
        }

        public int getFullWidth() {
            return fullWidth;
        }

        public int getFullHeight() {
            return fullHeight;
        }

        public int getThumbnailWidth() {
            return thumbnailWidth;
        }

        public int getThumbnailHeight() {
            return thumbnailHeight;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
        }
    }

}
