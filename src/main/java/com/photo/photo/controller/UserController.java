package com.photo.photo.controller;

import com.photo.photo.entity.User;
import com.photo.photo.utils.SendMail;
import com.photo.photo.service.UserService;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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

    @Autowired
    private SendMail sendMail;

    @GetMapping(value = "/email")
    public String email(HttpServletRequest request) {
        String emailString="";
        String emailaddress= request.getParameter("email");
        user = userService.findByEmail(emailaddress);

        if(user==null&&emailaddress!=null){
            emailString= RandomStringUtils.randomAlphanumeric(6);
            sendMail.sendSimpleMail(emailaddress,"Welcome to 井井","您的验证码是："+emailString);
        }

        return emailString;
    }


    @GetMapping (value = "/register")
    public Integer register(HttpServletRequest request) {

        String userName = request.getParameter("userName");
        String pwd = request.getParameter("pwd");
        String emailaddress = request.getParameter("email");
        Integer regNum = 0;

        user = userService.findByUserName(userName);
        if (user == null && userName !=null && pwd !=null)
        {
            User user = new User();
            user.setUserName(userName);
            user.setPwd(pwd);
            user.setEmail(emailaddress);
            userService.save(user);
            regNum = 1;
        } else {

            regNum = 0;
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

