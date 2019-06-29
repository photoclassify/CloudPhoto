package com.photo.photo.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Photo
{
    @Id
    @GeneratedValue
    private Integer photoId;
    private String tag;
    private String userId;
    private String name;
    private String dateYear;
    private String dateMonth;
    private String dateDay;

    public String getDateYear ()
    {
        return dateYear;
    }

    public void setDateYear (String dateYear)
    {
        this.dateYear = dateYear;
    }

    public String getDateMonth ()
    {
        return dateMonth;
    }

    public void setDateMonth (String dateMon)
    {
        this.dateMonth = dateMon;
    }

    public String getDateDay ()
    {
        return dateDay;
    }

    public void setDateDay (String dateDay)
    {
        this.dateDay = dateDay;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public Integer getPhotoId ()
    {
        return photoId;
    }

    public void setPhotoId (Integer photoId)
    {
        this.photoId = photoId;
    }

    public String getTag ()
    {
        return tag;
    }

    public void setTag (String tag)
    {
        this.tag = tag;
    }

    public String getUserId ()
    {
        return userId;
    }

    public void setUserId (String userId)
    {
        this.userId = userId;
    }
}
