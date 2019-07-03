package com.photo.photo.controller;

import com.photo.photo.config.WebMvcConfig;
import com.photo.photo.service.PhotoService;
import com.photo.photo.service.TagService;
import com.photo.photo.utils.RePhotoInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
public class TagController
{
    private static final Logger log = LoggerFactory.getLogger(TagController.class);

    @Autowired
    private PhotoService photoService;

    @Autowired
    private TagService tagService;

    private String ip = "192.168.151.77:8080";  //IP地址

    private String photoPath = ip + WebMvcConfig.getAbsPhyPath ();    //图片映射地址


    @GetMapping (value = "/tag")
    public RePhotoInfo allFirstRoot (HttpServletRequest request)
    {
        HttpSession session = request.getSession(false);

        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().contains("JSESSION"))
                {
                    System.out.print("Name:" + cookie.getName() + "\nValue" + cookie.getValue());
                }
            }
        }
        Object value = session.getAttribute("username");
        if (value == null) {
            System.out.print("用户不存在");
        } else {
            System.out.print("用户存在" + value);
        }


        String userId = "testUserId2.0"; //TODO userID!!!
        switch (request.getParameter("operate"))
        {
            case "allFirstRoots":
                System.out.println (session.getAttribute ("username"));
                return tagService.listFirstRoot (userId);
            case "allSecondRoots":
                return tagService.listSecondRoot (request.getParameter ("firstRoot"), userId);
            case "allKeywords":
                return tagService.listKeywords (request.getParameter ("firstRoot"), request.getParameter ("secondRoot"), userId);
            case "allPhotos":
                return tagService.listPhotos (request.getParameter ("firstRoot"), request.getParameter ("secondRoot"), request.getParameter ("keyword"), userId);
            case "delete":
                return tagService.delete (request.getParameter ("firstRoot"), request.getParameter ("secondRoot"), request.getParameter ("keyword"), userId);
            case "update":
                return tagService.update (request.getParameter ("newData"), request.getParameter ("firstRoot"), request.getParameter ("secondRoot"), request.getParameter ("keyword"), userId);

            default:
                return null;
        }
    }

    @GetMapping (value = "/photo")
    public RePhotoInfo showPhotoInfo (HttpServletRequest request, HttpSession session) throws IllegalAccessException
    {
        String userId = "testUserId2.0"; //TODO userID!!!

        RePhotoInfo res = photoService.showPhoto (request.getParameter ("photoName"));              //为res中添加数据结构photo中的数据
        String tags = res.getMessage ();
        if (tags != null)
        {
            return tagService.showTag (res, tags);
        } else
        {
            res.getData ().put ("error", "图片信息有误，无法获取");
            res.getData ().put ("tags", tags);
            return res;
        }
    }
}
