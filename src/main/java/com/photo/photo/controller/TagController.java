package com.photo.photo.controller;

import com.photo.photo.config.WebMvcConfig;
import com.photo.photo.service.PhotoService;
import com.photo.photo.service.TagService;
import com.photo.photo.utils.RePhotoInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
    public RePhotoInfo allFirstRoot (HttpServletRequest request, HttpSession session)
    {
        String userId = "testUserId2.0";
        switch (request.getParameter("operate"))
        {
            case "allFirstRoots":
                return tagService.listFirstRoot (userId);
            case "allSecondRoots":
                return tagService.listSecondRoot (request.getParameter ("firstRoot"), userId);
            case "allKeywords":
                return tagService.listKeywords (request.getParameter ("firstRoot"), request.getParameter ("secondRoot"), userId);
            case "allPhotos":
                return tagService.listPhotos (request.getParameter ("firstRoot"), request.getParameter ("secondRoot"), request.getParameter ("keyword"), userId);
            case "delete":
                return tagService.delete (request.getParameter ("firstRoot"), request.getParameter ("secondRoot"), request.getParameter ("keyword"), userId);
            default:
                return null;
        }
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
