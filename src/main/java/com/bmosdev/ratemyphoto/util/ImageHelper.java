package com.bmosdev.ratemyphoto.util;

import com.bmosdev.ratemyphoto.model.Orientation;
import org.imgscalr.Scalr;

import java.awt.image.BufferedImage;

public class ImageHelper {

    public static BufferedImage rotateImageToOrientation(BufferedImage image, Orientation orientation) {
        switch (orientation) {
            case BOTTOM_RIGHT:
                return Scalr.rotate(image, Scalr.Rotation.CW_180);
            case RIGHT_TOP:
                return Scalr.rotate(image, Scalr.Rotation.CW_90);
            case LEFT_BOTTOM:
                return Scalr.rotate(image, Scalr.Rotation.CW_270);
            case TOP_LEFT:
                return image;
            default:
                return image;
        }
    }
}
