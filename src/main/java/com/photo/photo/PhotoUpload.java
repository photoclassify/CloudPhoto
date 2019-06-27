package com.photo.photo;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;


//@RestController
public class PhotoUpload {
    private static final Logger log = LoggerFactory.getLogger(PhotoUpload.class);

    private static String path = "/Library/Storage/2017/PhotoCloud" + "/";     //图片保存的绝对路径
static String getPhotoStorePath ()
        {
            return path;
        }






    //    @PostMapping("/upload")
    //    @ResponseBody
    public static String upload (MultipartFile file, Photo photo) {

        if (file.isEmpty()) {
            return "上传失败，请选择文件";
        }

        String fileName = file.getOriginalFilename();
        String realpath = path;
        File fileDir = new File(realpath);                  //如果没有指定目录 新建一个文件夹
        if (!fileDir.exists())
            fileDir.mkdirs();

        String extname = FilenameUtils.getExtension(fileName);          //文件格式判断
        String allowImgFormat = "jpg,jpeg,png";
        if (!allowImgFormat.contains(extname.toLowerCase())) {
            log.error ("错误的文件格式");
            return "错误的文件格式";
        }

        fileName = String.valueOf (photo.getPhotoId ()) + '_' + GetNowTime.getDate () + "." + extname;

        File dest = new File(realpath + fileName);
        try {
            file.transferTo(dest);
            log.info("上传成功");

            return fileName;
        } catch (IOException e) {
            log.error(e.toString(), e);
        }
        return "上传失败！";
    }


    //    @PostMapping("/multiUpload")
    //    @ResponseBody
    public static String multiUpload (HttpServletRequest request, Photo photo)
    {
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        String fileNameList = "";
        String filePath = path;
        File fileDir = new File(filePath);                  //如果没有指定目录 新建一个文件夹
        if (!fileDir.exists())
            fileDir.mkdirs();
        for (int i = 0; i < files.size(); i++)
        {
            MultipartFile file = files.get(i);
            if (file.isEmpty())
            {
                return "上传第" + (i++) + "个文件失败";
            }else {

            }
            String fileName = file.getOriginalFilename();
            String extname = FilenameUtils.getExtension(fileName);          //文件格式判断
            String allowImgFormat = "jpg,jpeg,png";
            if (!allowImgFormat.contains(extname.toLowerCase())) {
                log.error ("第" + (i + 1) + "个文件格式错误");
                return "第" + (i + 1) + "个文件格式错误";
            }

            fileName = String.valueOf (photo.getPhotoId () + i) + '_' + GetNowTime.getDate () + "." + extname;
            fileNameList += (fileName + "!");

            File dest = new File(filePath + fileName);
            try
            {
                file.transferTo(dest);
                log.info("第" + (i + 1) + "个文件上传成功");
            } catch (IOException e)
            {
                log.error(e.toString(), e);
                return "上传第" + (i++) + "个文件失败";
            }
        }

        return fileNameList;

    }
}

