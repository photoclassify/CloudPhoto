package com.photo.photo.config;

import com.photo.photo.service.PhotoService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {


    private static String relPhyPath = "/static" + "/";         //在项目中/static/的图片的url映射
    private static String absPhyPath = "/img" + "/";           //图片保存的物理地址的url映射

    public static String getRelPhyPath ()
    {
        return relPhyPath;
    }

    public static String getAbsPhyPath ()
    {
        return absPhyPath;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(relPhyPath + "**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler(absPhyPath + "**").addResourceLocations("file:" + PhotoService.getPhotoStorePath ());

    }

}
