package com.andersen.user.controller;

import com.andersen.exception.DatabaseException;
import com.andersen.exception.EntityNotFoundException;
import com.andersen.user.service.UserCreateDto;
import com.andersen.user.service.UserResponseDto;
import com.andersen.user.service.UserService;
import com.andersen.user.service.UserServiceImpl;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@WebServlet("/users")
public class UserController extends HttpServlet {

    private UserService userService = UserServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String param = req.getParameter("id");
        JSONObject result = new JSONObject();
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        if (param == null) {
            List<UserResponseDto> users = userService.getAll();
            JSONArray usersJson = new JSONArray(users);
            out.print(usersJson);
        } else {
            Long id = Long.parseLong(param);
            try {
                UserResponseDto user = userService.getById(id);
                result = new JSONObject(user);
                out.print(result);
            } catch (EntityNotFoundException | DatabaseException ex) {
                result.put("message", ex.getMessage());
                out.print(result);
            }
        }
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BufferedReader reader = req.getReader();
        UserCreateDto userCreateDto = getUserDtoCreate(req);
        System.out.println(userCreateDto.toString());
        UserResponseDto userResponse = userService.create(userCreateDto);
        JSONObject userJson = new JSONObject(userResponse);
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        out.print(userJson);
        out.flush();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserResponseDto userDtoFull = userService.update(getUserDtoUpdate(req));

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = Long.parseLong(req.getParameter("id"));
        try {
            req.setAttribute("userId", id);
            userService.deleteById(id);
            req.getRequestDispatcher("/WEB-INF/pages/deletedRedirect.jsp").forward(req, resp);
        } catch (EntityNotFoundException ex) {
            req.getRequestDispatcher("/WEB-INF/pages/notFound.jsp").forward(req, resp);
        }
    }

    private UserCreateDto getUserDtoCreate(HttpServletRequest request) {
        return UserCreateDto.builder()
                .name(request.getParameter("name"))
                .lastname(request.getParameter("lastname"))
                .email(request.getParameter("email"))
                .password(request.getParameter("password"))
                .build();
    }

    private UserResponseDto getUserDtoUpdate(HttpServletRequest request) {
        return UserResponseDto.builder()
                .id(Long.parseLong(request.getParameter("id")))
                .name(request.getParameter("name"))
                .lastname(request.getParameter("lastname"))
                .email(request.getParameter("email"))
                .build();
    }
}
