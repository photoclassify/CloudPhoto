package com.photo.photo;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class ThumbnailsMake
{
    public static void Make (Integer height, Integer width, String  fromPath, String toPath, String photoName) throws IOException
    {
        String fromPhoto = fromPath + photoName;
        String toPhoto = toPath + photoName;

        File fileDir = new File(toPath);
        if (!fileDir.exists())
        {
            fileDir.mkdirs();
        }

        BufferedImage image = ImageIO.read (new File (fromPhoto));
        Thumbnails.Builder<BufferedImage> builder = null;
        int imageWidth = image.getWidth ();
        int imageHeitht = image.getHeight ();
        if ((float) height / width != (float) imageWidth / imageHeitht)
        {
            if (imageWidth > imageHeitht)
            {
                image = Thumbnails.of (fromPhoto).height (height).asBufferedImage ();
            } else
            {
                image = Thumbnails.of (fromPhoto).width (width).asBufferedImage ();
            }
            builder = Thumbnails.of (image).sourceRegion (Positions.CENTER, height, width).size (height, width);
        } else
        {
            builder = Thumbnails.of (image).size (height, width);
        }
        builder.outputFormat ("jpg").toFile (toPhoto);

    }
}
