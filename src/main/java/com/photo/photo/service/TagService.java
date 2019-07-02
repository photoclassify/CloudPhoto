package com.photo.photo.service;

import com.baidu.aip.imageclassify.AipImageClassify;
import com.photo.photo.config.WebMvcConfig;
import com.photo.photo.entity.Tag;
import com.photo.photo.repository.PhotoRepository;
import com.photo.photo.repository.TagRepository;
import com.photo.photo.utils.RePhotoInfo;
import com.photo.photo.utils._unused_PhotoUpload;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class TagService
{
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private PhotoRepository photoRepository;

    private RePhotoInfo rePhotoInfo = new RePhotoInfo (WebMvcConfig.getAbsPhyPath (), WebMvcConfig.getAbsPhyPath () + PhotoService.getTh ());


    //设置APPID/AK/SK
    public static final String APP_ID = "16620004";
    public static final String API_KEY = "sQKGNkySVVif7GtnGg529VHC";
    public static final String SECRET_KEY = "R8uPXSIGXgGHXjCjeu37RojtAWGsAk0L";

    public String writeTag(String photoName, Integer photoId, String userId) throws JSONException             //图像识别，获取tag的id们返回
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
                tag.setPhotoName (photoName);
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

    public RePhotoInfo listFirstRoot (String userId)
    {
        Map<Object,String> firstRoots = new HashMap<> ();
        for (Tag tag : tagRepository.findByUserId (userId))
        {
            if(tag.getFirstRoot () != null && !firstRoots.containsKey (tag.getFirstRoot ()))
            {
                firstRoots.put (tag.getFirstRoot (), tag.getPhotoName ());
            }
        }
        rePhotoInfo.setMessage (firstRoots);
        return rePhotoInfo;

    }

    public RePhotoInfo listSecondRoot (String firstRoot, String userId)
    {
        Map<Object,String> secondRoots = new HashMap<> ();
        for (Tag tag : tagRepository.findByUserIdAndFirstRoot (userId, firstRoot))
        {
            if(tag.getSecondRoot () != null && !secondRoots.containsKey (tag.getSecondRoot ()))
            {
                secondRoots.put(tag.getSecondRoot (), tag.getPhotoName ());
            }
        }
        rePhotoInfo.setMessage (secondRoots);
        return rePhotoInfo;
    }

    public RePhotoInfo listKeywords (String firstRoot, String secondRoot, String userId)
    {
        Map<Object,String> tagKeywords = new HashMap<> ();
        for (Tag tag : tagRepository.findByUserIdAndFirstRootAndSecondRoot (userId, firstRoot, secondRoot))
        {
            if(tag.getKeyword () != null && !tagKeywords.containsKey (tag.getKeyword ()))
            {
                tagKeywords.put (tag.getKeyword (), tag.getPhotoName ());
            }
        }
        rePhotoInfo.setMessage (tagKeywords);
        return rePhotoInfo;
    }

    public RePhotoInfo listPhotos (String firstRoot, String secondRoot, String keyword, String userId)
    {
        Map<Object, String> photos = new HashMap<> ();
        for (Tag tag : tagRepository.findByUserIdAndFirstRootAndSecondRootAndKeyword (userId, firstRoot, secondRoot, keyword))
        {
            if (tag.getKeyword () != null && ! photos.containsValue (tag.getPhotoName ()))
            {
                photos.put  ( tag.getScore(), tag.getPhotoName ());
            }
        }
        rePhotoInfo.setMessage (photos);
        return rePhotoInfo;
    }

    public RePhotoInfo delete (String firstRoot, String secondRoot, String keyword, String userId)
    {
        if (firstRoot != null)
        {
            if (secondRoot != null)
            {
                if (keyword != null)
                {
                    for (Tag tag : tagRepository.findByUserIdAndFirstRootAndSecondRootAndKeyword (userId, firstRoot, secondRoot, keyword))
                    {
                        tagRepository.delete (tag);
                    }
                    rePhotoInfo.setMessage ("删除结果", "删除keyword成功");
                    return rePhotoInfo;
                }
                else
                {
                    for (Tag tag : tagRepository.findByUserIdAndFirstRootAndSecondRoot (userId, firstRoot, secondRoot))
                    {
                        tagRepository.delete (tag);
                    }
                    rePhotoInfo.setMessage ("删除结果", "删除secondRoot成功");

                    return rePhotoInfo;
                }
            }
            else
            {
                if (keyword == null)
                {
                    for (Tag tag : tagRepository.findByUserIdAndFirstRoot (userId, firstRoot))
                    {
                        tagRepository.delete (tag);
                    }
                    rePhotoInfo.setMessage ("删除结果", "删除firstRoot成功");
                    return rePhotoInfo;

                }
                else
                {
                    rePhotoInfo.setMessage ("删除结果", "删除失败，secondRoot缺失！");
                    return rePhotoInfo;
                }
            }
        }
        else
        {
            if (secondRoot != null || keyword != null)
            {
                rePhotoInfo.setMessage ("删除结果", "删除失败，firstRoot缺失！");
                return rePhotoInfo;
            }
            else
            {
                rePhotoInfo.setMessage ("删除结果", "删除失败，缺少参数！");
                return rePhotoInfo;
            }
        }
    }
}