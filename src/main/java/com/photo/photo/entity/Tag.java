package com.photo.photo.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Tag
{
    @Id
    @GeneratedValue
    private Integer tagId;
    private Integer photoId;
    private String photoName;
    private String userId;
    private String firstRoot;
    private String secondRoot;
    private String keyword;
    private double score;

    public Integer getTagId ()
    {
        return tagId;
    }

    public void setTagId (Integer tagId)
    {
        this.tagId = tagId;
    }

    public Integer getPhotoId ()
    {
        return photoId;
    }

    public void setPhotoId (Integer photoId)
    {
        this.photoId = photoId;
    }

    public String getUserId ()
    {
        return userId;
    }

    public void setUserId (String userId)
    {
        this.userId = userId;
    }

    public String getFirstRoot ()
    {
        return firstRoot;
    }

    public void setFirstRoot (String firstRoot)
    {
        this.firstRoot = firstRoot;
    }

    public String getSecondRoot ()
    {
        return secondRoot;
    }

    public void setSecondRoot (String secondRoot)
    {
        this.secondRoot = secondRoot;
    }

    public String getKeyword ()
    {
        return keyword;
    }

    public void setKeyword (String keyword)
    {
        this.keyword = keyword;
    }

    public double getScore ()
    {
        return score;
    }

    public void setScore (double score)
    {
        this.score = score;
    }

    public String getPhotoName ()
    {
        return photoName;
    }

    public void setPhotoName (String photoName)
    {
        this.photoName = photoName;
    }



}
