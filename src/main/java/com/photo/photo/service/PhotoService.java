package com.photo.photo.service;

import com.drew.imaging.ImageProcessingException;
import com.photo.photo.entity.Photo;
import com.photo.photo.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
public class PhotoService
{
    @Autowired
    private PhotoRepository photoRepository;



    public void delete (Photo photo)
    {
        photoRepository.delete (photo);
    }

    public Photo newPhoto ()
    {
        Photo photo = new Photo ();
        photoRepository.save (photo);
        return photo;
    }

    public void save (Photo photo)
    {
        photoRepository.save (photo);
    }

    //写入photo基本数据
    public void save(Photo photo, Integer photoId, String photoName, String photoTag, String userId) throws ImageProcessingException, IOException
    {
        photo.setPhotoId (photoId);
        photo.setName (photoName);
        photo.setTag (photoTag);
        photo.setUserId (userId);
//                if (photo.getDateDay () == null)
//                {
//                    photo = PhotoDateSet.setDate (photo);
//                }

        this.photoRepository.save (photo);
    }


    //通过tag查询所有照片
    public List<String> photosByTag (String tag, String photoPath)
    {
        List<Photo> photos = photoRepository.findByTag (tag);

        ArrayList<String> photoNames = new ArrayList<> ();

        for (Photo photo : photos)
        {
            if (photo.getName () != null)
            {
                String name = photo.getName ();
                photoNames.add (name);
            }
        }

        for (int i = 0; i < photoNames.size (); i++)
        {
            String name = photoNames.get (i);
            photoNames.set (i, photoPath + name);
        }

        return photoNames;
    }


    //获取所有照片的tag
    public List<String> getTagList (String photoPath)
    {
        List<Photo> allPhotos = photoRepository.findAll ();
        ArrayList<String> photoTags = new ArrayList<> ();

        for (int i = 0; i < allPhotos.size () - 1; i++)
        {
            for (int j = allPhotos.size () - 1; j > i; j--)
            {
                if (allPhotos.get (j).getTag () == null || allPhotos.get (j).getTag ().equals (allPhotos.get (i).getTag ()))
                {

                    allPhotos.remove (j);//删除重复元素
                }
            }
        }

        for (Photo photo : allPhotos)
        {
            String tag = photo.getTag ();
            photoTags.add (tag);

            String name = photo.getName ();
            photoTags.add (photoPath + name);
        }
        return photoTags;
    }


    //删除该tag的所有图片
    public void deleteByTag(String tag){
        if(tag != null)
        {
            List<Photo> photos = photoRepository.findByTag(tag);
            for (int i = 0; i < photos.size(); i++)
            {
                photoRepository.delete(photos.get(i));
            }

        }
    }


    //修改类别
    public void updateTag(String oldTag, String newTag)
    {
        if(oldTag!=null && newTag!=null ) {

            List<Photo> photos = photoRepository.findByTag(oldTag);
            for (int i = 0; i < photos.size(); i++)
            {
                photos.get(i).setTag(newTag);
                photoRepository.save(photos.get(i));
            }
        }

    }

}
