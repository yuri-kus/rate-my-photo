package com.bmosdev.ratemyphoto.core;

//import com.drew.imaging.ImageMetadataReader;
//import com.drew.metadata.Directory;
//import com.drew.metadata.Metadata;
//import com.drew.metadata.Tag;

import com.bmosdev.ratemyphoto.model.Orientation;
import com.bmosdev.ratemyphoto.model.Photo;
import com.bmosdev.ratemyphoto.photobackup.FileInfo;
import com.bmosdev.ratemyphoto.util.HashHelper;
import com.bmosdev.ratemyphoto.util.ImageHelper;
import it.tidalwave.imageio.nef.NEFMetadata;
import it.tidalwave.imageio.tiff.ThumbnailLoader;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class NefReader {

    public Photo.ImageInfo readImageInfo(FileInfo fileInfo) throws IOException {
        File file = fileInfo.getPath();
        ImageReader reader = ImageIO.getImageReaders(file).next();
        reader.setInput(ImageIO.createImageInputStream(file));
        IIOMetadata metadata = reader.getImageMetadata(0);
        NEFMetadata nefMetadata = (NEFMetadata) metadata;

        ThumbnailLoader smallThumbnailHelper = nefMetadata.getThumbnailHelper()[0];
        ThumbnailLoader fullSizeThumbnailHelper = nefMetadata.getThumbnailHelper()[1];

        Orientation orientation = Orientation.findByIndex(nefMetadata.getPrimaryIFD().getOrientation().intValue()); // TODO add tag existence checking
        Dimension thumbnailDimension = new Dimension(smallThumbnailHelper.getWidth(), smallThumbnailHelper.getHeight());
        Dimension fullImageDimension = new Dimension(fullSizeThumbnailHelper.getWidth(), fullSizeThumbnailHelper.getHeight());
        return new Photo.ImageInfo(thumbnailDimension, fullImageDimension, orientation);

//        String id = HashHelper.calculateMd5(fileInfo.getPath().getCanonicalPath().toString());
//        Photo photo = new Photo(id, fileInfo, thumbnailDimension, fullImageDimension, orientation);
//        return photo;
//        throw new RuntimeException("not implemented");
    }

    public byte[] readThumbnail(Photo photo) throws IOException {
        File file = photo.getFileInfo().getPath();
        ImageReader reader = ImageIO.getImageReaders(file).next();
        reader.setInput(ImageIO.createImageInputStream(file));

        BufferedImage image = reader.readThumbnail(0, 0);
        BufferedImage rotatedImage = ImageHelper.rotateImageToOrientation(image, photo.getImageInfo().getOrientation());
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ImageIO.write(rotatedImage, "jpg", byteOut);

        return byteOut.toByteArray();
    }

    public byte[] readFullSizeImage(Photo photo) throws IOException {
        File file = photo.getFileInfo().getPath();
        ImageReader reader = ImageIO.getImageReaders(file).next();
        reader.setInput(ImageIO.createImageInputStream(file));

        BufferedImage image = reader.readThumbnail(0, 1);
        BufferedImage rotatedImage = ImageHelper.rotateImageToOrientation(image, photo.getImageInfo().getOrientation());
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ImageIO.write(rotatedImage, "jpg", byteOut);

        return byteOut.toByteArray();
    }

//        public static void main(String[] args) throws Exception {
////        BufferedImage image= ImageIO.read(new File("data/jpg.jpg"));
////        ImageIO.write(image, "jpg", new File("e:/a.jpg"));
//
//
////        ImageInputStream is = ImageIO.createImageInputStream(new File("data/raw.nef"));
////        Iterator<ImageReader> it = ImageIO.getImageReaders(is);
////
////        if(!it.hasNext()) {
////            System.out.println("Can't process this image.");
////            System.exit(1);
////        }
////        ImageReader imageReader = it.next();
////        imageReader.setInput(is);
////
////        BufferedImage image = imageReader.read(0);
////
////        // Write
////        if(!ImageIO.write(image, "JPEG", new File("e:/DSC_0001.jpg"))) {
////            System.out.println("Can't write the image");
////            System.exit(1);
////        }
//
////        File file = new File("data/raw.nef");
////        BufferedImage image = ImageIO.read(file);
////        ImageIO.write(image, "jpg", new File("e:/DSC_0002.jpg"));
//
//
//            readFile("E:\\garbage\\-photos\\_DSC8247.NEF");
//            System.out.println("==============================================================");
////        readFile("data/raw-rate1.nef");
////        System.out.println("==============================================================");
////        readFile("data/raw-rate2.nef");
//
////            Metadata metadata = ImageMetadataReader.readMetadata(new File("data/raw-rate1.nef"));
////            for (Directory directory : metadata.getDirectories()) {
////                for (Tag tag : directory.getTags()) {
////                    System.out.format("[%s] - %s = %s",
////                            directory.getName(), tag.getTagName(), tag.getDescription());
////                    System.out.println();
////                }
////                if (directory.hasErrors()) {
////                    for (String error : directory.getErrors()) {
////                        System.err.format("ERROR: %s", error);
////                    }
////                }
////            }
//        }
//
//
//
//        private static void readFile(String fileName) throws IOException {
//            File file = new File(fileName);
//            ImageReader reader = ImageIO.getImageReaders(file).next();
//            reader.setInput(ImageIO.createImageInputStream(file));
//            IIOMetadata metadata = reader.getImageMetadata(0);
//            NEFMetadata nefMetadata = (NEFMetadata)metadata;
//            System.out.println(nefMetadata.getNikonMakerNote());
//            System.out.println("------------------------------------------------");
//            System.out.println(Arrays.toString(nefMetadata.getMetadataFormatNames()));
//            System.out.println("------------------------------------------------");
//            System.out.println(nefMetadata.getMakerNote());
//            System.out.println("------------------------------------------------");
//            System.out.println(nefMetadata.getNativeMetadataFormatName());
//            System.out.println("------------------------------------------------");
//            System.out.println(Arrays.toString(nefMetadata.getThumbnailHelper()));
//            System.out.println(nefMetadata.getThumbnailHelper()[0].getHeight());
//            System.out.println(nefMetadata.getThumbnailHelper()[0].getWidth());
//            System.out.println(nefMetadata.getThumbnailHelper()[1].getHeight());
//            System.out.println(nefMetadata.getThumbnailHelper()[1].getWidth());
//            BufferedImage thumbnail = reader.readThumbnail(0, 1);
//            ImageIO.write(thumbnail, "jpg", new File("e:/thumbnail.jpg"));
////        System.out.println(nefMetadata.getThumbnailHelper()[1].load());
//        }
//
}
