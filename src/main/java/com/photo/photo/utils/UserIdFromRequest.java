package com.photo.photo.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class UserIdFromRequest
{
    static MySessionContext myc= MySessionContext.getInstance();

    private static final Logger log = LoggerFactory.getLogger(UserIdFromRequest.class);

    public static String  getUserId (HttpServletRequest request)
    {
        HttpSession session = request.getSession(false);
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().contains("JSESSION"))
                {
                    session = myc.getSession (cookie.getValue ());
                }
            }
        }
        if (session == null)
        {
            log.info ("error, 无session！");
            return "error, 无session！";
        }
        String  userId;
        if (session.getAttribute("userId") == null) {
            log.info("error, session中未能获取userId");
            return "error, session中未能获取userId";
        } else {
            userId = session.getAttribute("userId").toString ();
            log.info("用户存在" + userId);
        }

        return userId;
    }
}
