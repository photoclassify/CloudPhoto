package com.photo.photo.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Tag
{
    @Id
    @GeneratedValue
    private Integer TagId;
    private Integer PhotoId;
    private String UserId;
    private String firstRoot;
    private String SecondRoot;
    private String keyword;
    private double score;

    public Integer getTagId ()
    {
        return TagId;
    }

    public void setTagId (Integer tagId)
    {
        TagId = tagId;
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
        return SecondRoot;
    }

    public void setSecondRoot (String secondRoot)
    {
        SecondRoot = secondRoot;
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

    public Integer getPhotoId ()
    {
        return PhotoId;
    }

    public void setPhotoId (Integer photoId)
    {
        PhotoId = photoId;
    }

    public String getUserId ()
    {
        return UserId;
    }

    public void setUserId (String userId)
    {
        UserId = userId;
    }
}
