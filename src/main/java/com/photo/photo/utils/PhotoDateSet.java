package com.photo.photo.utils;


import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.photo.photo.entity.Photo;
import com.photo.photo.service.PhotoService;

import java.io.File;
import java.io.IOException;


public class PhotoDateSet
{

    public static Photo setDate(Photo photo) throws ImageProcessingException, IOException
    {
        String photoPath = PhotoService.getPhotoStorePath () + photo.getName ();
        File photoFile = new File(photoPath);
        Metadata metadata = ImageMetadataReader.readMetadata(photoFile);

        for (Directory directory : metadata.getDirectories())
        {
            for (Tag tag : directory.getTags ())
            {
                String tagName = tag.getTagName ();                  //标签名
                String desc = tag.getDescription ();                 //标签信息
                if (tagName.equals ("Date/Time Original"))
                {
                    photo.setDateYear (desc.substring (0, 4));
                    photo.setDateMonth (desc.substring (5, 7));
                    photo.setDateDay (desc.substring (8, 10));
                }
            }
        };
        return photo;
    }
}