package com.photo.photo.utils;

import com.photo.photo.entity.Photo;

public class Result
{
    private Integer code;
    private String message;
    private Photo photo;

    public Integer getCode ()
    {
        return code;
    }

    public void setCode (Integer code)
    {
        this.code = code;
    }

    public String getMessage ()
    {
        return message;
    }

    public void setMessage (String message)
    {
        this.message = message;
    }

    public Result ()
    {
    }

    public Result (String message)
    {
        this.message = message;
    }

    public Photo getPhoto ()
    {
        return photo;
    }

    public void setPhoto (Photo photo)
    {
        this.photo = photo;
    }

}
