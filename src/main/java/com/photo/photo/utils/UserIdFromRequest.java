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

    public static String getUserId (HttpServletRequest request)
    {
        HttpSession session = getSession (request);

        return getUID (session);
    }

    public static HttpSession getSession (HttpServletRequest request)
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
        return session;
    }

    public static String getUserId (HttpServletRequest request, String sessionId)
    {
        HttpSession session = request.getSession(false);
        session = myc.getSession (sessionId);

        return getUID (session);
    }


    private static String getUID (HttpSession session)
    {
        if (session == null)
        {
            log.info ("error, 无发获取到登陆用户信息！");
            return "error, 无发获取到登陆用户信息！";
        }
        String  userId;
        if (session.getAttribute("userId") == null) {
            log.info("error, cookie中未能获取userId");
            return "error, cookie中未能获取userId";
        } else {
            userId = session.getAttribute("userId").toString ();
            log.info("用户存在" + userId);
        }

        return userId;
    }
}
