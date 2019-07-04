package com.photo.photo.controller;

import com.drew.imaging.ImageProcessingException;
import com.photo.photo.entity.Photo;
import com.photo.photo.service.PhotoService;
import com.photo.photo.service.TagService;
import com.photo.photo.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


@RestController
@RequestMapping ("/file")
public class FileController
{
    private static final Logger log = LoggerFactory.getLogger(FileController.class);

    private static String path = PhotoService.getPhotoStorePath ();

    MySessionContext myc = MySessionContext.getInstance();

    private String th = "th/";


    @Autowired
    private PhotoService photoService;

    @Autowired
    private TagService tagService;

    @ResponseBody
    @RequestMapping ("/upload")
    public Result uploadPhoto(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException, ImageProcessingException
    {
        String message = UserIdFromRequest.getUserId (request);
        String userId;
        switch (message)
        {
            case "error, 无session！":
            case "error, session中未能获取userId":
                return (new Result (message));
            default:
                userId = message;

        }
        Result res = photoService.upload (file, userId);                                                          //处理上传的图片
        if (res.getCode () == 1)
        {
            Photo photo = res.getPhoto ();
            photoService.saveTag (photo, tagService.writeTag (photo.getName (), photo.getPhotoId (), userId));            //通过百度云图像识别获取标签
            ThumbnailsMake.Make (330, 250, path, path + th, photo.getName ());               //生成缩略图
        }
        return res;
    }

//    @ResponseBody
//    @PostMapping("/multiUpload")
//    public Result multiUploadPhoto(HttpServletRequest request) throws IOException, ImageProcessingException
//    {
//        Result res = new Result ();
//        ArrayList<String> tagList = new ArrayList<> ();
//        Photo photo = photoService.newPhoto ();
//        String result = _unused_PhotoUpload.multiUpload(request, photo);     //若上传成功，result为文件名;失败则为报错信息。
//        log.info ("result: " + result);
//        if (!result.contains ("!"))
//        {
//            photoService.delete (photo);
//            res.setMessage ("上传结果" + result);
//            return res;
//        }
//        else
//        {
//            String[] resultCut = result.split("!");
//            for (int i = 0; i < resultCut.length; i++)
//            {
//                if (resultCut[i] != null)
//                {
//                    String tag = tagService.writeTag (resultCut[i], photo.getPhotoId () + i);             //通过百度云图像识别获取标签
//                    log.info(tag + resultCut[i]);
//                    tagList.add ("第" + (i + 1) + "张图识别为：" + tag);
//                    photoService.save (photo,photo.getPhotoId () + i,resultCut[i],tag,userId);     //存储照片信息到数据库
//                    ThumbnailsMake.Make(150,150, path, path + th, resultCut[i]) ;
//                    //↑生成缩略图
//                }
//            }
//        }
//        res.setData (tagList);
//        res.setMessage ("上传结果: 上传成功");
//        log.info(String.valueOf (res.getData()));
//        return res;
//    }

}
