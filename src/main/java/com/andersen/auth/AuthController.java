package com.andersen.auth;

import com.andersen.user.service.UserService;
import com.andersen.user.service.UserServiceImpl;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;

@WebServlet({"/login", "/logout"})
public class AuthController extends HttpServlet {

    private UserService userService = UserServiceImpl.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String body = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

        String path = req.getServletPath();
        HttpSession httpSession = req.getSession();
        switch (path){
            case "/login":
                JSONObject user = new JSONObject(body);
                String email = user.getString("email");
                if (userService.validateUserForLogin(email, user.getString("password"))){
                    httpSession.setAttribute("user", userService.getByEmail(email));
                } else {
                    resp.setStatus(403);
                }
                break;
            case "/logout":
                if (httpSession.getAttribute("user") != null){
                    httpSession.removeAttribute("user");
                }
                break;
        }
    }
}
