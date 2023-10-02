package com.andersen.config;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
public class CORSFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        httpResponse.setHeader("Access-Control-Allow-Origin", "http://34.118.36.24:3000");
//        httpResponse.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");

        httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");

        httpResponse.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, API-Key");

        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");

        httpResponse.setHeader("Access-Control-Max-Age", "3600");

//        httpResponse.setHeader("Set-Cookie", httpResponse.getHeader("Set-Cookie") + "; SameSite=none");

//        request.getServletContext().getSessionCookieConfig().setSecure(true);

        Cookie cookie = new Cookie("Set-Cookie", "SameSite=none");
        cookie.setMaxAge(120);
        cookie.setPath("/time-tracker");
        cookie.setSecure(false);
        cookie.setHttpOnly(true);
        httpResponse.addCookie(cookie);

        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
