package com.photo.photo.controller;

import com.photo.photo.entity.User;
import com.photo.photo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value="/user")
public class UserController
{
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    private User user = new User();

    @RequestMapping (value = "/register")
    public Integer register(HttpServletRequest request) {

        String userName = request.getParameter("userName");
        String pwd = request.getParameter("pwd");
        String rePwd = request.getParameter("rePwd");
        Boolean registerstate = false;
        String str = "";
        Integer regNum = 0;

        if (pwd.equals(rePwd)) {
            user = userService.findByUserName(userName);
            if (user == null)
            {
                User user = new User();
                user.setUserName(userName);
                user.setPwd(pwd);
                userService.save(user);

                regNum = 1;
            } else {

                regNum = 0;
            }
        }else{

            regNum = 2;
        }
        return regNum;
    }


    @RequestMapping("/login")
    public Integer login(HttpServletRequest request, HttpServletResponse response)
    {
        String userName = request.getParameter("userName");
        String pwd = request.getParameter("pwd");

        HttpSession session = request.getSession();
//        session.setAttribute("username",userName);
//        session.setMaxInactiveInterval(30*60);//以秒为单位，即在没有活动30分钟后，session将失效





        user = userService.findByUserNameAndPwd(userName, pwd);

        if (user != null) {
            session.setAttribute("userId",userName);
            session.setMaxInactiveInterval(30*60);//以秒为单位，即在没有活动30分钟后，session将失效
            //写cookie并返回
            String sessionId = session.getId();
            Cookie cookie = new Cookie("JSESSIONID", sessionId);
            cookie.setPath("/");
            response.addCookie(cookie);
            return  1;
        } else {
            return  0;
        }
    }




}

