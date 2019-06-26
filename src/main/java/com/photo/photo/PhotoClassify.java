package com.photo.photo;

import com.baidu.aip.imageclassify.AipImageClassify;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class PhotoClassify {
    //设置APPID/AK/SK
    public static final String APP_ID = "16620004";
    public static final String API_KEY = "sQKGNkySVVif7GtnGg529VHC";
    public static final String SECRET_KEY = "R8uPXSIGXgGHXjCjeu37RojtAWGsAk0L";

    public static String  Classify(String path) throws JSONException
    {
        //初始化
        AipImageClassify aic = new AipImageClassify(APP_ID, API_KEY, SECRET_KEY);
        //图片路径作为参数，此处使用相对路径，相对于项目根目录而言，即cat.jpg放在项目根目录下
        String path1 = path;
        //返回JSON格式的数据
        JSONObject res = aic.advancedGeneral(path1, new HashMap<String, String>());

        String tag;
        JSONArray results = res.getJSONArray("result");
        JSONObject obj = (JSONObject)results.get(1);
        tag = (String) obj.get("keyword");


        return tag;
    }

}