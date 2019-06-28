package com.photo.photo.controller;


import com.photo.photo.entity.User;
import com.photo.photo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
public class LoginController {

    @Autowired
    private UserRepository userRepository;
    private User user = new User();

    @GetMapping("/login")
    public Integer login(HttpServletRequest request, HttpSession session) {
        String userName = request.getParameter("userName");
        String pwd = request.getParameter("pwd");

        user = userRepository.findByUserNameAndPwd(userName, pwd);
        boolean loginstate;
        Integer lognum=0;

        if (user != null) {
            session.setAttribute("userLogin", user);
            loginstate=true;
            lognum=1;
        } else {
            loginstate=false;
            lognum=0;
        }
        return lognum;
    }


}
