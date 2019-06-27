package com.photo.photo;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {


    private String relPhyPath = "/static" + "/";
    private String absPhyPath = "/img" + "/";           //图片保存的绝对路径

    public String getRelPhyPath ()
    {
        return relPhyPath;
    }

    public String getAbsPhyPath ()
    {
        return absPhyPath;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(relPhyPath + "**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler(absPhyPath + "**").addResourceLocations("file:" + PhotoUpload.getPhotoStorePath ());

    }

}
