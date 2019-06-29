package com.photo.photo.listener;

import com.photo.photo.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;


/**
 * session监听器
 * 当有session被创建时，会进入该监听器
 * 比如：request.getSession()，这时候，会进入。
 * @author maybe
 */
@Slf4j
@WebListener
public class SessionListener implements HttpSessionListener {//实现HttpSessionListener接口的监听，是监听session的创建和销毁

    private static final Logger log = LoggerFactory.getLogger(SessionListener.class);

    /**
     * session创建时，初始化session
     */
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        log.info("Session{}被创建",se.getSession().getId());
        se.getSession().setAttribute("forcedOut", "no");//给一个标识，标识新创建的session给他一个标识没有被强制下线
    }

    /**
     * session失效时调用。
     * 我发现这个方法被调用的时间比我设置的超时时间要长1分钟左右。
     * 我设置5分钟，那么第6分钟才会调用这个方法销毁session。
     * 具体原因不明，还需要看源码。
     */
    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        User user = (User) httpSessionEvent.getSession().getAttribute("user");
        if(user!=null) SinglePointListener.map.remove(user.getUserName ());//session销毁时，要将session从map中remove掉
        log.info("Session{}被销毁",httpSessionEvent.getSession().getId());
    }

}
