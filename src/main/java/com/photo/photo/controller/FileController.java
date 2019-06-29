package com.photo.photo.controller;

import com.drew.imaging.ImageProcessingException;
import com.photo.photo.entity.Photo;
import com.photo.photo.service.PhotoService;
import com.photo.photo.utils.PhotoClassify;
import com.photo.photo.utils.PhotoUpload;
import com.photo.photo.utils.Result;
import com.photo.photo.utils.ThumbnailsMake;
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
import java.util.ArrayList;


@RestController
@RequestMapping ("/file")
public class FileController
{
    private static final Logger log = LoggerFactory.getLogger(PhotoUpload.class);

    private static String path = PhotoUpload.getPhotoStorePath ();

    private String th = "th/";

    String userId = "testUserId2.0";   //TODO 获取userId


    @Autowired
    private PhotoService photoService;

    @ResponseBody
    @RequestMapping ("/upload")
    public Result uploadPhoto(@RequestParam("file") MultipartFile file) throws IOException, ImageProcessingException
    {
        Result res = new Result ();
        Photo photo = photoService.newPhoto ();
        String result = PhotoUpload.upload (file, photo);   //若上传成功，result为文件名;失败则为报错信息。
        if (result.equals ("上传失败，请选择文件") || result.equals ("错误的文件格式") || result.equals ("上传失败！"))
        {
            photoService.delete (photo);
            res.setMessage ("上传结果:" + result);
            return res;
        }
        else
        {
            String tag = PhotoClassify.Classify(path+ result);                  //通过百度云图像识别获取标签
            log.info(tag + result);
            this.photoService.save (photo,photo.getPhotoId (),result,tag,userId);     //存储照片信息到数据库
            ThumbnailsMake.Make(150,150, path, path + th, result) ;
            //↑生成缩略图

            res.setMessage ("上传结果: 上传成功");
            res.setData (tag);
            return res;
        }
    }

    @ResponseBody
    @RequestMapping("/multiUpload")
    public Result multiUploadPhoto(HttpServletRequest request) throws IOException, ImageProcessingException
    {
        Result res = new Result ();
        ArrayList<String> tagList = new ArrayList<> ();
        Photo photo = photoService.newPhoto ();
        String result = PhotoUpload.multiUpload(request, photo);     //若上传成功，result为文件名;失败则为报错信息。
        if (!result.contains ("!"))
        {
            photoService.delete (photo);
            res.setMessage ("上传结果" + result);
            return res;
        }
        else
        {
            String[] resultCut = result.split("!");
            for (int i = 0; i < resultCut.length; i++)
            {
                if (resultCut[i] != null)
                {
                    String tag = PhotoClassify.Classify (path + resultCut[i]);             //通过百度云图像识别获取标签
                    log.info(tag + resultCut[i]);
                    tagList.add ("第" + (i + 1) + "张图识别为：" + tag);
                    photoService.save (photo,photo.getPhotoId () + i,resultCut[i],tag,userId);     //存储照片信息到数据库
                    ThumbnailsMake.Make(150,150, path, path + th, resultCut[i]) ;
                    //↑生成缩略图
                }
            }
        }
        res.setData (tagList);
        res.setMessage ("上传结果: 上传成功");
        log.info(String.valueOf (res.getData()));
        return res;
    }

}
