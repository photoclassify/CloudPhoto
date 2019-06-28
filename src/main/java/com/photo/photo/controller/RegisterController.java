package com.photo.photo.controller;

import com.photo.photo.entity.User;
import com.photo.photo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value="/user")
public class RegisterController {

    @Autowired
    private UserRepository userRepository;
    private User user = new User();

    @GetMapping (value = "/register")
    public Integer register(HttpServletRequest request) {

        String userName= request.getParameter("userName");
        String pwd = request.getParameter("pwd");
        String rePwd = request.getParameter("rePwd");
        Boolean registerstate=false;
        String str = "";
        Integer regnum=0;

        if (pwd.equals(rePwd)) {
            user = userRepository.findByUserName(userName);
            if (user == null) {
                User user = new User();
                user.setUserName(userName);
                user.setPwd(pwd);
                userRepository.save(user);

                regnum=1;
            } else {

                regnum=0;
            }
        }else{

            regnum=2;
        }
        return regnum;
    }
}

