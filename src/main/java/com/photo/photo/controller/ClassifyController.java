package com.photo.photo.controller;

import com.photo.photo.entity.Photo;
import com.photo.photo.repository.PhotoRepository;
import com.photo.photo.utils.PhotoClassify;
import com.photo.photo.utils.PhotoDataOperate;
import com.photo.photo.utils.PhotoUpload;
import com.photo.photo.utils.ThumbnailsMake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping ("/file")

public class ClassifyController
{
    private static final Logger log = LoggerFactory.getLogger(PhotoUpload.class);

    private static String path = PhotoUpload.getPhotoStorePath ();

    private String th = "th/";

    String userId = "testUserId2.0";   //TODO 获取userId


    @Autowired
    private PhotoRepository photoRepository;

    @PostMapping ("/upload")
    @ResponseBody
    public String uploadPhoto(@RequestParam("file") MultipartFile file) throws IOException
    {
        Photo photo = new Photo ();
        photoRepository.save (photo);
        String result = PhotoUpload.upload (file, photo);   //若上传成功，result为文件名;失败则为报错信息。
        if (result.equals ("上传失败，请选择文件") || result.equals ("错误的文件格式") || result.equals ("上传失败！"))
        {
            photoRepository.delete (photo);
            return result;
        }
        else
        {
            String tag = PhotoClassify.Classify(path+ result);                  //通过百度云图像识别获取标签
            log.info(tag + result);
            photoRepository.save(PhotoDataOperate.UpdatePhoto (photo,photo.getPhotoId (),result,tag,userId));     //存储照片信息到数据库
            ThumbnailsMake.Make(150,150, path, path + th, result) ;
            //↑生成缩略图

            return  "上传成功! " + "识别为：" + tag;
        }
    }

    @PostMapping("/multiUpload")
    @ResponseBody
    public String multiUploadPhoto(HttpServletRequest request) throws IOException
    {
        Photo photo = new Photo ();
        photoRepository.save (photo);
        String result = PhotoUpload.multiUpload(request, photo);     //若上传成功，result为文件名;失败则为报错信息。
        List<String> tags = new ArrayList<> ();
        if (!result.contains ("!"))
        {
            photoRepository.delete (photo);
            return result;
        }
        else
        {
            String[] resultCut = result.split("!");
            for (int i = 0; i < resultCut.length; i++)
            {
                if (resultCut[i] != null)
                {
//                    log.info (resultCut[i]);
                    String tag = PhotoClassify.Classify (path + resultCut[i]);             //通过百度云图像识别获取标签
                    log.info(tag + resultCut[i]);
                    tags.add(tag);
                    photoRepository.save (PhotoDataOperate.UpdatePhoto (photo,photo.getPhotoId () + i,resultCut[i], tag, userId));  //存储照片信息到数据库
                    ThumbnailsMake.Make(150,150, path, path + th, resultCut[i]) ;
                    //↑生成缩略图
                }
            }
        }
        String tagsString = "";
        for(int i = 0; i < tags.size();i++)
        {
            tagsString += ("\n图" + (i + 1) + "识别为" + tags.get (i));
        }
        return "上传成功! " + tagsString;
    }
}
