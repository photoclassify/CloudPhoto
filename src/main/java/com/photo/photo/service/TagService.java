package com.photo.photo.service;

import com.baidu.aip.imageclassify.AipImageClassify;
import com.photo.photo.config.WebMvcConfig;
import com.photo.photo.entity.Tag;
import com.photo.photo.repository.TagRepository;
import com.photo.photo.utils.RePhotoInfo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TagService
{
    @Autowired
    private TagRepository tagRepository;

    private static final Logger log = LoggerFactory.getLogger(TagService.class);


    //设置APPID/AK/SK
    public static final String APP_ID = "16620004";
    public static final String API_KEY = "sQKGNkySVVif7GtnGg529VHC";
    public static final String SECRET_KEY = "R8uPXSIGXgGHXjCjeu37RojtAWGsAk0L";

    public String writeTag(String photoName, Integer photoId, String userId) throws JSONException         //图像识别，获取tag的id们返回
    {
        log.info("开始图像识别");
        long start = System.currentTimeMillis();

        AipImageClassify aic = new AipImageClassify(APP_ID, API_KEY, SECRET_KEY);
        if (photoName != null)
        {
            String path = PhotoService.getPhotoStorePath () + photoName;
            String tagIdList = "";
            JSONObject res = aic.advancedGeneral (path, new HashMap<> ());

            try
            {
                JSONArray results = res.getJSONArray ("result");
                for (int i = 0; i < 5; i++)
                {
                    Tag tag = new Tag ();
                    JSONObject obj = (JSONObject) results.get (i);
                    String[] tagRoot = ((String) obj.get ("root")).split ("-");
                    tag.setFirstRoot (tagRoot[0]);
                    tag.setSecondRoot (tagRoot[1]);
                    tag.setScore ((Double) obj.get ("score"));
                    tag.setKeyword ((String) obj.get ("keyword"));
                    tag.setPhotoId (photoId);
                    tag.setPhotoName (photoName);
                    tag.setUserId (userId);
                    tagRepository.save (tag);
                    tagIdList += (tag.getTagId () + ";");
                    log.info (tag.getKeyword ());
                }
            } catch (JSONException e)
            {
                e.printStackTrace ();
            }

            long end = System.currentTimeMillis();
            log.info("完成图像识别，耗时：" + (end - start) + "毫秒");
            return tagIdList;
        }else
        {
            log.info("图像识别失败，未能获取图片名");
            return null;
        }
    }


    public RePhotoInfo listFirstRoot (String userId)
    {
        RePhotoInfo rePhotoInfo = new RePhotoInfo (WebMvcConfig.getAbsPhyPath (), WebMvcConfig.getAbsPhyPath () + PhotoService.getTh ());
        Map<String, Object> firstRoots = new HashMap<> ();
        List<Tag> allTags = tagRepository.findByUserId (userId);
        for (Tag tag : allTags)
        {
            if(tag.getFirstRoot () != null && !firstRoots.containsKey (tag.getFirstRoot ()))
            {
                firstRoots.put ( tag.getFirstRoot (), tag.getPhotoName ());
            }
        }
        rePhotoInfo.setData (firstRoots);
        return rePhotoInfo;

    }

    public RePhotoInfo listSecondRoot (String firstRoot, String userId)
    {
        RePhotoInfo rePhotoInfo = new RePhotoInfo (WebMvcConfig.getAbsPhyPath (), WebMvcConfig.getAbsPhyPath () + PhotoService.getTh ());
        Map<String, Object> secondRoots = new HashMap<> ();
        List<Tag> allTags = tagRepository.findByUserIdAndFirstRoot (userId, firstRoot);
        for (Tag tag : allTags)
        {
            if(tag.getSecondRoot () != null && !secondRoots.containsKey (tag.getSecondRoot ()))
            {
                secondRoots.put(tag.getSecondRoot (), tag.getPhotoName ());
            }
        }
        rePhotoInfo.setData (secondRoots);
        return rePhotoInfo;
    }

    public RePhotoInfo listKeywords (String firstRoot, String secondRoot, String userId)
    {
        RePhotoInfo rePhotoInfo = new RePhotoInfo (WebMvcConfig.getAbsPhyPath (), WebMvcConfig.getAbsPhyPath () + PhotoService.getTh ());
        Map<String, Object> tagKeywords = new HashMap<> ();
        List<Tag> allTags = tagRepository.findByUserIdAndFirstRootAndSecondRoot (userId, firstRoot, secondRoot);
        for (Tag tag : allTags)
        {
            if(tag.getKeyword () != null && !tagKeywords.containsKey (tag.getKeyword ()))
            {
                tagKeywords.put (tag.getKeyword (), tag.getPhotoName ());
            }
        }
        rePhotoInfo.setData (tagKeywords);
        return rePhotoInfo;
    }

    public RePhotoInfo listPhotos (String firstRoot, String secondRoot, String keyword, String userId)
    {
        RePhotoInfo rePhotoInfo = new RePhotoInfo (WebMvcConfig.getAbsPhyPath (), WebMvcConfig.getAbsPhyPath () + PhotoService.getTh ());
        Map<String, Object> photos = new HashMap<> ();
        List<Tag> allTags = tagRepository.findByUserIdAndFirstRootAndSecondRootAndKeyword (userId, firstRoot, secondRoot, keyword);
        for (Tag tag : allTags)
        {
            if (tag.getKeyword () != null && ! photos.containsValue (tag.getPhotoName ()))
            {
                photos.put  ( tag.getPhotoName (), tag.getScore());
            }
        }
        rePhotoInfo.setData (photos);

        return rePhotoInfo;

    }

    public RePhotoInfo delete (String firstRoot, String secondRoot, String keyword, String userId)
    {
        RePhotoInfo rePhotoInfo = new RePhotoInfo ();
        if (firstRoot != null)
        {
            if (secondRoot != null)
            {
                if (keyword != null)
                {
                    List<Tag> allTags = tagRepository.findByUserIdAndFirstRootAndSecondRootAndKeyword (userId, firstRoot, secondRoot, keyword);
                    for (Tag tag : allTags)
                    {
                        tagRepository.delete (tag);
                    }
                    rePhotoInfo.getData ().put ("删除结果", "删除keyword成功");
                    return rePhotoInfo;
                }
                else
                {
                    List<Tag> allTags = tagRepository.findByUserIdAndFirstRootAndSecondRoot (userId, firstRoot, secondRoot);
                    for (Tag tag : allTags)
                    {
                        tagRepository.delete (tag);
                    }
                    rePhotoInfo.getData ().put ("删除结果", "删除secondRoot成功");

                    return rePhotoInfo;
                }
            }
            else
            {
                if (keyword == null)
                {
                    List<Tag> allTags = tagRepository.findByUserIdAndFirstRoot (userId, firstRoot);
                    for (Tag tag : allTags)
                    {
                        tagRepository.delete (tag);
                    }
                    rePhotoInfo.getData ().put ("删除结果", "删除firstRoot成功");
                    return rePhotoInfo;

                }
                else
                {
                    rePhotoInfo.getData ().put ("删除结果", "删除失败，secondRoot缺失！");
                    return rePhotoInfo;
                }
            }
        }
        else
        {
            if (secondRoot != null || keyword != null)
            {
                rePhotoInfo.getData ().put ("删除结果", "删除失败，firstRoot缺失！");
                return rePhotoInfo;
            }
            else
            {
                rePhotoInfo.getData ().put ("删除结果", "删除失败，缺少参数！");
                return rePhotoInfo;
            }
        }
    }

    public RePhotoInfo update (String newData, String firstRoot, String secondRoot, String keyword, String userId)
    {
        RePhotoInfo rePhotoInfo = new RePhotoInfo (newData);
        if (firstRoot != null)
        {
            if (secondRoot != null)
            {
                if (keyword != null)
                {
                    List<Tag> allTags = tagRepository.findByUserIdAndFirstRootAndSecondRootAndKeyword (userId, firstRoot, secondRoot, keyword);
                    for (Tag tag : allTags)
                    {
                        tag.setKeyword (newData);
                        tagRepository.save (tag);
                    }
                    rePhotoInfo.getData ().put ("更新结果", "更新keyword成功");
                    return rePhotoInfo;
                }
                else
                {
                    List<Tag> allTags = tagRepository.findByUserIdAndFirstRootAndSecondRoot (userId, firstRoot, secondRoot);
                    for (Tag tag : allTags)
                    {
                        tag.setSecondRoot (newData);
                        tagRepository.save (tag);
                    }
                    rePhotoInfo.getData ().put ("更新结果", "更新secondRoot成功");

                    return rePhotoInfo;
                }
            }
            else
            {
                if (keyword == null)
                {
                    List<Tag> allTags = tagRepository.findByUserIdAndFirstRoot (userId, firstRoot);
                    for (Tag tag : allTags)
                    {
                        tag.setFirstRoot (newData);
                        tagRepository.save (tag);
                    }
                    rePhotoInfo.getData ().put ("更新结果", "更新firstRoot成功");
                    return rePhotoInfo;

                }
                else
                {
                    rePhotoInfo.getData ().put ("更新结果", "更新失败，secondRoot缺失！");
                    return rePhotoInfo;
                }
            }
        }
        else
        {
            if (secondRoot != null || keyword != null)
            {
                rePhotoInfo.getData ().put ("更新结果", "更新失败，firstRoot缺失！");
                return rePhotoInfo;
            }
            else
            {
                rePhotoInfo.getData ().put ("更新结果", "更新失败，缺少参数！");
                return rePhotoInfo;
            }
        }
    }

    public RePhotoInfo showTag (RePhotoInfo res, String photoTagList)
    {
        String[] tagCut = photoTagList.split(";");
        for(int i = 0; i < tagCut.length; i++)
        {
            res.getData ().put ("tag" + (i + 1), tagRepository.findByTagId (Integer.valueOf (tagCut[i])));
        }
        return res;
    }


    public RePhotoInfo findByNameLike(String keyword, String userId)
    {
        RePhotoInfo rePhotoInfo = new RePhotoInfo (WebMvcConfig.getAbsPhyPath (), WebMvcConfig.getAbsPhyPath () + PhotoService.getTh ());
        Map<String, List<String>> photos = new HashMap<> ();
        // 一定要加 "%"+参数名+"%"
        List<Tag> allTags = tagRepository.findByKeywordLikeAndUserId ("%" + keyword + "%", userId);
        for (Tag tag : allTags)
        {
            if (tag.getKeyword () != null)
            {
                if(photos.containsKey (tag.getPhotoName ()))
                {
                    photos.get (tag.getPhotoName ()).add (tag.getKeyword ());
                }
                else
                {
                    ArrayList<String> keywords = new ArrayList<> ();
                    keywords.add(tag.getKeyword ());
                    photos.put (tag.getPhotoName (), keywords);
                }
            }
        }

        Map<String, Object> tempPhotos = new HashMap<> ();
        for (String key : photos.keySet ())
        {
            tempPhotos.put (key, photos.get (key));
        }
        rePhotoInfo.setData (tempPhotos);
        return rePhotoInfo;
    }

    //删除photo信息
    public void deleteTagByName (String photoName)
    {
        List<Tag> tagList = tagRepository.findByPhotoName (photoName);
        for (Tag tag : tagList)
        {
            tagRepository.delete (tag);
        }
    }
}
