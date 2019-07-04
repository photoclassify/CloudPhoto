package com.photo.photo.service;

import com.photo.photo.entity.User;
import com.photo.photo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService
{
    @Autowired
    private UserRepository userRepository;

    public User findByUserName (String userName)
    {
        return userRepository.findByUserName (userName);
    }

    public User findByUserNameAndPwd(String userName, String pwd)
    {
        return userRepository.findByUserNameAndPwd (userName, pwd);
    }

    public User findByEmail (String email){
        return userRepository.findByEmail (email);
    }


    public void save (User user)
    {
        userRepository.save (user);
    }
}
