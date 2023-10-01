package com.andersen.config;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
public class CORSFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

//        ((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        ((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Origin", "*");
//        ((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Methods","GET, POST, PUT, DELETE");
        ((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Methods","GET,POST,PUT,DELETE,OPTIONS,HEAD");
//        ((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-HEADERS","Content-Type, API-Key");
        ((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-HEADERS","Origin, X-Requested-With, Content-Type, Accept, Authorization, API-Key");
        ((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Credentials", "true");

        filterChain.doFilter(request, servletResponse);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
