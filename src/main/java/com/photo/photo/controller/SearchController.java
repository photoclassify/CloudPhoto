package com.photo.photo.controller;

import com.photo.photo.WebMvcConfig;
import com.photo.photo.entity.Photo;
import com.photo.photo.repository.PhotoRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping (value="/photo")
public class SearchController
{

    private final PhotoRepository photoRepository;

    private String ip = "192.168.151.77:8080";  //IP地址

    private String photoPath = ip + WebMvcConfig.getAbsPhyPath();    //图片映射地址

    public SearchController (PhotoRepository photoRepository) {this.photoRepository = photoRepository;}

    //通过tag查询
    @GetMapping (value = "/tag/{tag}")
    public List<String> photoListByTag (@PathVariable ("tag") String tag)
    {
        List<Photo> photos = photoRepository.findByTag (tag);

        ArrayList<String> photoNames = new ArrayList<> ();

        for (Photo photo : photos)
        {
            String name = photo.getName ();
            photoNames.add (name);
        }

        for (int i = 0; i < photoNames.size (); i++)
        {
            String name = photoNames.get (i);
            photoNames.set (i, photoPath + name);
        }

        return photoNames;
    }


    //所有分类
    @GetMapping (value = "/allTag")
    public List<String> tagList ()
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
}
