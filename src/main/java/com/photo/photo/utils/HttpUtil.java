package com.photo.photo.utils;


import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


public class HttpUtil
{
    private static OkHttpClient okHttpClient;
    private static int num = 0;

    private static final Logger log = LoggerFactory.getLogger(HttpUtil.class);


    static{
        okHttpClient = new OkHttpClient ();
        //                Builder()
        //                .readTimeout(1, TimeUnit.SECONDS)
        //                .connectTimeout(1, TimeUnit.SECONDS)
        //                .build();


    }



    public static String get(String path){
        //创建连接客户端
        Request request = new Request.Builder()
                .url(path)
                .build();
        //创建"调用" 对象
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();//执行
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (IOException e) {
            log.error ("链接格式有误:"+path);
        }
        return null;
    }

}