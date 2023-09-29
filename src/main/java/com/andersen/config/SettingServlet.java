package com.andersen.config;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public class SettingServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        System.out.println("\n\n\n----------Init-------------\n\n\n");
    }
}
