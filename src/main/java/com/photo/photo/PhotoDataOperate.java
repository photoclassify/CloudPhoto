package com.photo.photo;

public class PhotoDataOperate
{

    public static Photo UpdatePhoto(Photo photo, Integer photoId, String photoName, String photoTag, String userId)
    {
        photo.setPhotoId (photoId);
        photo.setName (photoName);
        photo.setTag (photoTag);
        photo.setUserId (userId);

        return photo;
    }
}
