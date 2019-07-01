package com.photo.photo.controller;

import com.photo.photo.config.WebMvcConfig;
import com.photo.photo.service.PhotoService;
import com.photo.photo.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class TagController
{

    @Autowired
    private PhotoService photoService;

    @Autowired
    private TagService tagService;

    private String ip = "192.168.151.77:8080";  //IP地址

    private String photoPath = ip + WebMvcConfig.getAbsPhyPath ();    //图片映射地址


    @GetMapping (value = "/tag")
    public List<String> allFirstRoot (HttpServletRequest request, HttpSession session)
    {
        String userId = "testUserId2.0";
        String operate = request.getParameter("operate");
        if (operate.equals ("allFirstRoot"))
            return tagService.listFirstRoot (userId);
        else
            return null;
    }

    @GetMapping (value = "/find/{tag}")
    public List<String> photoListByTag (@PathVariable ("tag") String tag)
    {
        return photoService.photosByTag (tag, photoPath);
    }


    @GetMapping (value = "/findAll")
    public List<String> tagList (HttpServletRequest request)
    {
        String userId = request.getParameter("userId");
        return photoService.getTagList (photoPath, userId);
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
