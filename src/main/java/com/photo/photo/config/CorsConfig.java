package com.photo.photo.config;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CorsConfig implements javax.servlet.Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //支持所有域的跨域调用
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String origin = request.getHeader("Origin");
        if (origin != null && !"".equals(origin)) {
            response.addHeader("Access-Control-Allow-Origin", origin);
        }

        //前端自定义头header时，需要设置Access-Control-Allow-Headers
        //支持所有带头的跨域调用
        String headers = request.getHeader("Access-Control-Allow-Headers");
        if (headers != null && !"".equals(headers)) {
            response.addHeader("Access-Control-Allow-Headers", headers);
        }


        //带Cookie的时候，origin必须是全匹配，不能使用*,或者使用上方的获取后再设置，支持所有的域
//        response.addHeader("Access-Control-Allow-Origin","*");
        response.addHeader("Access-Control-Allow-Method", "*");
//        response.addHeader("Access-Control-Allow-Headers","Content-Type");
        response.addHeader("Access-Control-Max-Age", "3600");
        response.addHeader("Access-Control-Allow-Credentials", "true");


        filterChain.doFilter(servletRequest, response);
    }

    @Override
    public void destroy() {

    }
}