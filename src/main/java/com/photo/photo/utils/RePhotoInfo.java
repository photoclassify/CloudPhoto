package com.photo.photo.utils;

import java.util.HashMap;
import java.util.Map;

public class RePhotoInfo
{
    private String photoPath;
    private String thPath;
    private Map<Object, String> message;

    public RePhotoInfo(String photoPath, String thPath)
    {
        this.setPhotoPath (photoPath);
        this.setThPath (thPath);
    }

    public String getPhotoPath ()
    {
        return photoPath;
    }

    public void setPhotoPath (String photoPath)
    {
        this.photoPath = photoPath;
    }

    public String getThPath ()
    {
        return thPath;
    }

    public void setThPath (String thPath)
    {
        this.thPath = thPath;
    }

    public Map<Object, String> getMessage ()
    {
        return message;
    }

    public void setMessage (Map<Object, String> message)
    {
        this.message = message;
    }

    public void setMessage (Object message1, String message2)
    {
        Map<Object, String> res = new HashMap<> ();
        res.put (message1, message2);
        this.setMessage (res);
    }

}
