package com.photo.photo.controller;

import com.photo.photo.config.WebMvcConfig;
import com.photo.photo.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping (value="/tag")
public class TagController
{

    @Autowired
    private PhotoService photoService;

    private String ip = "192.168.151.77:8080";  //IP地址

    private String photoPath = ip + WebMvcConfig.getAbsPhyPath ();    //图片映射地址



    @GetMapping (value = "/find/{tag}")
    public List<String> photoListByTag (@PathVariable ("tag") String tag)
    {
        return photoService.photosByTag (tag, photoPath);
    }


    @GetMapping (value = "/findAll")
    public List<String> tagList ()
    {
        return photoService.getTagList (photoPath);
    }


    @DeleteMapping (value = "/delete/{tag}")
    public void tagDelete (@PathVariable ("tag") String tag)
    {
        photoService.deleteByTag (tag);
    }


    @PutMapping (value = "/update/{oldTag}/{newTag}")
    public void tagUpdate (@PathVariable ("oldTag") String oldTag, @PathVariable ("newTag") String newTag)
    {
        photoService.updateTag (oldTag, newTag);
    }

}
