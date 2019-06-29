package com.photo.photo.utils;


import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 单点登录拦截器，只拦截.do的访问
 * 并如果session被销毁，使其返回异常页面
 * @author maybe
 */

@Slf4j
@WebFilter(filterName="*",urlPatterns="*.*")
public class LoginFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(LoginFilter.class);

    private static final String ERRORURL = "/myweb/LoginPage.jsp";

    /**
     * 拦截器处理方法
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain fc) throws IOException, ServletException
    {
        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse)resp;
        String uri = request.getRequestURI();
        log.info ("request.getRequestURI() = " + request.getRequestURI());
        if(!uri.equals(ERRORURL)) {
            log.info("进入判断是否只有单点登录");
            String forcedOut = (String) request.getSession().getAttribute("forcedOut");
            if(null!=forcedOut&&!"".equals(forcedOut)) {
                if(forcedOut.equals("yes")) {
                    log.info("该用户已经在异地重新登录，进入异常提示！");
                    response.sendRedirect(ERRORURL);
                    return;
                }
            }
        }
        fc.doFilter(req, resp);
    }

    /**
     * 在系统启动时初始化拦截器
     */
    @Override
    public void init(FilterConfig config) throws ServletException {}

    /**
     * 在系统停止时销毁拦截器
     */
    @Override
    public void destroy() {}

}
