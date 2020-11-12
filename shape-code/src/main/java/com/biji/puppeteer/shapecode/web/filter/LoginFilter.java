package com.biji.puppeteer.shapecode.web.filter;

import com.biji.puppeteer.shapecode.util.RequestUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


//@WebFilter(urlPatterns = "/*", filterName = "loginFilter")
public class LoginFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        Object accountId = request.getSession().getAttribute("login_user");
        if (accountId != null && (int)(accountId) > 0) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        Cookie authCookie = RequestUtil.getCookie(request, "AUTH_TOKEN");
        if (authCookie == null) {
            response.sendError(401, "您尚未登录");
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
