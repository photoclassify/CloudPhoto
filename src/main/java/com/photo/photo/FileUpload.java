package com.photo.photo;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class FileUpload {

    private static String photoStorePath = "/Library/Storage/2017/PhotoCloud/";

    public static String getPhotoStorePath ()
    {
        return photoStorePath;
    }

    /**
     * 文件上传处理
     *
     * @param file
     * @return
     */
    public static String writeUploadFile(MultipartFile file) {
        String filename = file.getOriginalFilename();
        String realpath = photoStorePath;
        File fileDir = new File(realpath);                  //如果没有指定目录 新建一个文件夹
        if (!fileDir.exists())
            fileDir.mkdirs();

        String extname = FilenameUtils.getExtension(filename);          //文件格式判断
        String allowImgFormat = "gif,jpg,jpeg,png";
        if (!allowImgFormat.contains(extname.toLowerCase())) {
            return "errorExt";
        }

        filename = Math.abs(file.getOriginalFilename().hashCode()) + "." + extname;
        InputStream input = null;
        FileOutputStream fos = null;
        try {
            input = file.getInputStream();
            fos = new FileOutputStream(realpath + "/" + filename);
            IOUtils.copy(input, fos);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            IOUtils.closeQuietly(input);
            IOUtils.closeQuietly(fos);
        }
        return filename;
    }
}