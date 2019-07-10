package com.photo.photo.service;

import com.drew.imaging.ImageProcessingException;
import com.photo.photo.entity.Photo;
import com.photo.photo.repository.PhotoRepository;
import com.photo.photo.utils.GetNowTime;
import com.photo.photo.utils.PhotoDateSet;
import com.photo.photo.utils.RePhotoInfo;
import com.photo.photo.utils.Result;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Component
@Service
public class PhotoService
{
    @Autowired
    private PhotoRepository photoRepository;


    private static final Logger log = LoggerFactory.getLogger(PhotoService.class);

    private static String th = "th/";
    private static String path = "/Library/Storage/2017/PhotoCloud" + "/";     //图片保存的绝对路径
    public static String getPhotoStorePath ()
    {
        return path;
    }
    public static String getTh ()
    {
        return th;
    }



    public Result upload (MultipartFile file, String userId) throws ImageProcessingException
    {

        Result res = new Result ();
        Photo photo = new Photo ();
        photoRepository.save (photo);

        if (file.isEmpty()) {
            photoRepository.delete (photo);
            res.setCode (0);
            res.setMessage ("上传失败，请选择文件");
            return res;
        }

        String fileName = file.getOriginalFilename();
        String realpath = path;
        File fileDir = new File(realpath);                                                      //如果没有指定目录 新建一个文件夹
        if (!fileDir.exists())
            fileDir.mkdirs();

        String extname = FilenameUtils.getExtension(fileName);                                  //文件格式判断
        String allowImgFormat = "jpg,jpeg,png";
        if (!allowImgFormat.contains(extname.toLowerCase()))
        {
            log.error ("错误的文件格式");
            photoRepository.delete (photo);
            res.setCode (0);
            res.setMessage ("错误的文件格式");
            return res;
        }

        fileName = String.valueOf (photo.getPhotoId ()) + '_' + GetNowTime.getDate () + "." + extname;

        File dest = new File(realpath + fileName);
        try {
            file.transferTo(dest);
            this.save (photo,photo.getPhotoId (),fileName,userId);                              //存储照片信息到数据库
            log.info("上传成功, " + "," + fileName);
            res.setCode (1);
            res.setMessage ("上传成功");
            res.setPhoto (photo);
            return res;

        } catch (IOException e) {
            log.error(e.toString(), e);
            photoRepository.delete (photo);
            res.setCode (0);
            res.setMessage ("上传失败！");
            return res;
        }
    }


    public void delete (Photo photo)
    {
        photoRepository.delete (photo);
    }


    public void save (Photo photo)
    {
        photoRepository.save (photo);
    }

    //写入photoTag
    @Async
    public void saveTag (Photo photo, String tag)
    {
        photo.setTag (tag);
        photoRepository.save (photo);
    }

    //写入photo基本数据
    public void save(Photo photo, Integer photoId, String photoName, String userId) throws ImageProcessingException, IOException
    {
        photo.setPhotoId (photoId);
        photo.setName (photoName);
        photo.setUserId (userId);
                if (photo.getDateDay () == null)
                {
                    photo = PhotoDateSet.setDate (photo);
                }

        this.photoRepository.save (photo);
    }

    //显示photo信息
    public RePhotoInfo showPhoto (String photoName)
    {

        Photo photo;
        photo = photoRepository.findByName (photoName);
        RePhotoInfo res = new RePhotoInfo();
        String tag = photo.getTag ();
        res.setMessage (tag);
        res.getData ().put ("Photo", photo);

        return res;
    }

    //删除photo信息
    public void deletePhotoByName (String photoName)
    {
        photoRepository.delete (photoRepository.findByName (photoName));
    }

}
