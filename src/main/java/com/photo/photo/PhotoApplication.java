package com.photo.photo;

import com.photo.photo.config.CorsConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PhotoApplication
{
    public static void main (String[] args)
    {
        SpringApplication.run (PhotoApplication.class, args);
    }

    @Bean
    public FilterRegistrationBean registrationBean(){
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.addUrlPatterns("/*");
        bean.setFilter(new CorsConfig ());
        return bean;
    }
}
