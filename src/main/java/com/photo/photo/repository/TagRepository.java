package com.photo.photo.repository;

import com.photo.photo.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Integer>
{
    List<Tag> findByUserId (String userId);
}