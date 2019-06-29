package com.photo.photo.listener;

import com.photo.photo.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 单点登录
 * 监控session中属性user的变化
 * HttpSessionAttributeListener 监听session范围内属性变化
 * @author maybe
 *
 */
@WebListener
@Slf4j
public class SinglePointListener implements HttpSessionAttributeListener{

    private static final Logger log = LoggerFactory.getLogger(SinglePointListener.class);

    //key:username value:session，用于存放已经登录的用户的session
    public static Map<String, HttpSession> map = new HashMap<> ();

    /**
     * 当属性增加时，触发该方法
     */
    @Override
    public void attributeAdded(HttpSessionBindingEvent httpSessionBindingEvent) {
        User user = (User) httpSessionBindingEvent.getSession().getAttribute("user");
        if(user!=null) {//登录时需要把user信息放入session以供后续使用。session其他值得变化，不在本方法考虑范围内，
            if(SinglePointListener.map!=null) {
                if(SinglePointListener.map.containsKey(user.getUserName())) { //存在key，把之前的session失效，
                    log.info("map中存在key={},取出sessionOld清空数据，并设置属性forcedOut强制下线");
                    HttpSession sessionOld = SinglePointListener.map.get(user.getUserName());
                    if (sessionOld !=null) {
                        Enumeration<?> e = sessionOld.getAttributeNames();
                        while(e.hasMoreElements()){
                            String sessionKeyName = (String) e.nextElement();
                            sessionOld.removeAttribute(sessionKeyName);
                        }
                        sessionOld.setAttribute("forcedOut","yes");
                    }
                }
            }
            SinglePointListener.map.put(user.getUserName(), httpSessionBindingEvent.getSession());//最后把这次的user和session放入map以供后续比对。
        }
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent se) {}

    @Override
    public void attributeReplaced(HttpSessionBindingEvent se) {}

}
