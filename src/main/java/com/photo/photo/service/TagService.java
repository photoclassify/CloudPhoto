package com.photo.photo.service;

import com.baidu.aip.imageclassify.AipImageClassify;
import com.photo.photo.entity.Tag;
import com.photo.photo.repository.TagRepository;
import com.photo.photo.utils._unused_PhotoUpload;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class TagService
{
    @Autowired
    private TagRepository tagRepository;

    //设置APPID/AK/SK
    public static final String APP_ID = "16620004";
    public static final String API_KEY = "sQKGNkySVVif7GtnGg529VHC";
    public static final String SECRET_KEY = "R8uPXSIGXgGHXjCjeu37RojtAWGsAk0L";

    public String writeTag(String photoName, Integer photoId, String userId) throws JSONException
    {
        AipImageClassify aic = new AipImageClassify(APP_ID, API_KEY, SECRET_KEY);
        if (photoName != null)
        {
            String path = _unused_PhotoUpload.getPhotoStorePath () + photoName;
            String tagIdList = "";
            JSONObject res = aic.advancedGeneral (path, new HashMap<> ());

            JSONArray results = res.getJSONArray ("result");
            for (int i = 0; i < 5; i++)
            {
                Tag tag = new Tag ();
                JSONObject obj = (JSONObject) results.get (i);
                String[] tagRoot = ((String)obj.get("root")).split("-");
                tag.setFirstRoot (tagRoot[0]);
                tag.setSecondRoot (tagRoot[1]);
                tag.setScore ((Double) obj.get("score"));
                tag.setKeyword ((String) obj.get ("keyword"));
                tag.setPhotoId (photoId);
                tag.setUserId (userId);
                tagRepository.save (tag);
                tagIdList += (tag.getTagId ()+";");
            }

            return tagIdList;
        }else
        {
            return null;
        }
    }

}