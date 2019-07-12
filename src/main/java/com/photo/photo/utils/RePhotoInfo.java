package com.photo.photo.utils;

import java.util.HashMap;
import java.util.Map;

public class RePhotoInfo
{

    private String photoPath;
    private String thPath;
    private String message;
    private Map<String, Object> data;

    public RePhotoInfo (String photoPath, String thPath)
    {
        this.data = new HashMap<> ();
        this.photoPath = photoPath;
        this.thPath = thPath;
    }

    public RePhotoInfo (String message)
    {
        this.data = new HashMap<> ();
        this.message = message;

    }

    public RePhotoInfo()
    {
        this.data = new HashMap<> ();
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

    public String getMessage ()
    {
        return message;
    }

    public void setMessage (String message)
    {
        this.message = message;
    }

    public Map<String, Object> getData ()
    {
        return data;
    }

    public void setData (Map<String, Object> data)
    {
        this.data = data;
    }






}
