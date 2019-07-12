package com.photo.photo.controller;

import com.photo.photo.service.PhotoService;
import com.photo.photo.service.TagService;
import com.photo.photo.utils.RePhotoInfo;
import com.photo.photo.utils.UserIdFromRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Map;

@RestController
public class TagController
{
    private static final Logger log = LoggerFactory.getLogger(TagController.class);

    @Autowired
    private PhotoService photoService;

    @Autowired
    private TagService tagService;

    @RequestMapping (value = "/tag")
    public Object allFirstRoot (HttpServletRequest request,  HttpServletResponse response)
    {

        String userId;
        String message = UserIdFromRequest.getUserId (request);
        switch (message)
        {
            case "error, 无发获取到登陆用户信息！":
            case "error, cookie中未能获取userId":
                return (new RePhotoInfo (message));
            default:
                userId = message;

        }

        switch (request.getParameter("operate"))
        {
            case "allFirstRoots":
                RePhotoInfo res = tagService.listFirstRoot (userId);
                res.setMessage (UserIdFromRequest.getSession (request).getId ());
                return res;
            case "allSecondRoots":
                return tagService.listSecondRoot (request.getParameter ("firstRoot"), userId);
            case "allKeywords":
                return tagService.listKeywords (request.getParameter ("firstRoot"), request.getParameter ("secondRoot"), userId);
            case "allPhotos":
            {
                //                return tagService.listPhotos (request.getParameter ("firstRoot"), request.getParameter ("secondRoot"), request.getParameter ("keyword"), userId);
                //------------------------------------前端要求：返回所有图片信息--------------------------------

                ArrayList<RePhotoInfo> resList = new ArrayList<> ();
                RePhotoInfo rpi = tagService.listPhotos (request.getParameter ("firstRoot"), request.getParameter ("secondRoot"), request.getParameter ("keyword"), userId);
                resList.add (rpi);
                Map<String, Object> photos = rpi.getData ();
                for (String key : photos.keySet ())
                {
                    resList.add (getPhotoInfo (key));
                }
                return resList;
            }
            case "delete":
                return tagService.delete (request.getParameter ("firstRoot"), request.getParameter ("secondRoot"), request.getParameter ("keyword"), userId);
            case "update":
                return tagService.update (request.getParameter ("newData"), request.getParameter ("firstRoot"), request.getParameter ("secondRoot"), request.getParameter ("keyword"), userId);

            default:
                return null;
        }
    }

    @RequestMapping (value = "/photo")
    public RePhotoInfo showPhotoInfo (HttpServletRequest request) throws IllegalAccessException
    {
//        String message = UserIdFromRequest.getUserId (request);
//        String userId;
//        switch (message)
//        {
//            case "error, 无session！":
//            case "error, session中未能获取userId":
//                return (new RePhotoInfo (message));
//            default:
//                userId = message;
//
//        }

        String photoName = request.getParameter ("photoName");
        return getPhotoInfo (photoName);
    }

    @RequestMapping("/search")
    public RePhotoInfo findByNameLike(HttpServletRequest request)
    {
        String message = UserIdFromRequest.getUserId (request);
        String userId;
        switch (message)
        {
            case "error, 无发获取到登陆用户信息！":
            case "error, cookie中未能获取userId":
                return (new RePhotoInfo (message));
            default:
                userId = message;
        }

        return tagService.findByNameLike (request.getParameter ("keyword"), userId);
    }

    @RequestMapping("/delete")
    public void deletePhoto (HttpServletRequest request)
    {
        String photoName = request.getParameter ("photoName");
        if (photoName != null)
        {
            tagService.deleteTagByName (photoName);
            photoService.deletePhotoByName (photoName);
        }
    }

    public RePhotoInfo getPhotoInfo (String photoName)
    {
        RePhotoInfo res = photoService.showPhoto (photoName);              //为res中添加数据结构photo中的数据
        String tags = res.getMessage ();
        if (tags != null)
        {
            res.setMessage (photoName);
            return tagService.showTag (res, tags);
        } else
        {
            res.getData ().put ("error", "图片信息有误，无法获取");
            return res;
        }
    }
}
