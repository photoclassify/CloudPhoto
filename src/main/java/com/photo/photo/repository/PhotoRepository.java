package com.photo.photo.repository;

import com.photo.photo.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PhotoRepository extends JpaRepository<Photo, Integer>
{
    List<Photo> findByTag (String tag);
    List<Photo> findByUserId (String userId);
    Photo findByPhotoId (Integer photoId);
    Photo findByName (String photoName);

}