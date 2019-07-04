package com.photo.photo.repository;

import com.photo.photo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User,Long> , JpaSpecificationExecutor<User> {

    //User findById(Integer Id);

    @Query("select s from User s where s.userName = ?1 and s.pwd = ?2")
    User findByUserNameAndPwd (String userName, String pwd);

    @Query("select s from User s where s.userName = ?1")
    User findByUserName (String userName);

    @Query("select s from User s where s.email = ?1")
    User findByEmail (String email);


}
