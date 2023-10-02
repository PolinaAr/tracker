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
import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;
import java.util.Collection;

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

        addSameSiteCookieAttribute((HttpServletResponse) response);

        chain.doFilter(request, response);
    }

    private void addSameSiteCookieAttribute(HttpServletResponse response) {
        Collection<String> headers = response.getHeaders(HttpHeaders.SET_COOKIE);
        boolean firstHeader = true;
        for (String header : headers) {
            if (firstHeader) {
                response.setHeader(HttpHeaders.SET_COOKIE, String.format("%s; %s", header, "SameSite=none"));
                firstHeader = false;
                continue;
            }
            response.addHeader(HttpHeaders.SET_COOKIE, String.format("%s; %s", header, "SameSite=none"));
        }
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
