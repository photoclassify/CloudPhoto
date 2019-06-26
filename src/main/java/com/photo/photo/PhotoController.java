package com.photo.photo;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class PhotoController
{
    @Autowired
    private PhotoRepository photoRepository;

    String photoStorePath = FileUpload.getPhotoStorePath ();


    String userId = "testUserId";    //TODO 获取userId


    @PostMapping ("/upload")
    public Object upload(@RequestParam("file") MultipartFile  file) throws JSONException
    {

        String filename = FileUpload.writeUploadFile (file);
        String tag = PhotoClassify.Classify(photoStorePath+ filename);
        if (filename != null && filename != "errorExt")
        {
            Photo photo = new Photo ();
            photo.setUserId (userId);
            photo.setName (filename);
            photo.setTag (tag);
            photoRepository.save (photo);

        }
        return tag;
    }

}