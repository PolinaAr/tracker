package com.andersen.user.controller;

import com.andersen.exception.DatabaseException;
import com.andersen.exception.EntityNotFoundException;
import com.andersen.user.service.UserCreateDto;
import com.andersen.user.service.UserResponseDto;
import com.andersen.user.service.UserService;
import com.andersen.user.service.UserServiceImpl;
import com.andersen.util.EncryptorUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

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
        JSONObject body = readBody(req);
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        try {
            UserCreateDto userCreateDto = getUserDtoCreate(body);
            UserResponseDto userResponse = userService.create(userCreateDto);

            JSONObject userJson = new JSONObject(userResponse);
            out.print(userJson);
        } catch (DatabaseException ex) {
            JSONObject exception = new JSONObject();
            exception.put("message", ex.getMessage());
            out.print(exception);
        }
        out.flush();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JSONObject body = readBody(req);
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        try {
            UserResponseDto userForUpdate = getUserDtoUpdate(body);
            userForUpdate.setId(Long.valueOf(req.getParameter("id")));
            UserResponseDto userResponse = userService.update(userForUpdate);

            JSONObject userJson = new JSONObject(userResponse);
            out.print(userJson);
        } catch (DatabaseException ex) {
            JSONObject exception = new JSONObject();
            exception.put("message", ex.getMessage());
            out.print(exception);
        }
        out.flush();
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = Long.parseLong(req.getParameter("id"));
        try {
            userService.deleteById(id);
        } catch (EntityNotFoundException | DatabaseException ex) {
            JSONObject userJson = new JSONObject("message", ex.getMessage());
            PrintWriter out = resp.getWriter();
            resp.setContentType("application/json");
            out.print(userJson);
            out.flush();
        }
    }

    private static JSONObject readBody(HttpServletRequest req) throws IOException {
        String body = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        return new JSONObject(body);
    }

    private UserCreateDto getUserDtoCreate(JSONObject jsonObject) {
        return UserCreateDto.builder()
                .name(jsonObject.getString("name"))
                .lastname(jsonObject.getString("lastname"))
                .email(jsonObject.getString("email"))
                .password(EncryptorUtil.encrypt(jsonObject.getString("password")))
                .build();
    }

    private UserResponseDto getUserDtoUpdate(JSONObject jsonObject) {
        return UserResponseDto.builder()
                .name(jsonObject.getString("name"))
                .lastname(jsonObject.getString("lastname"))
                .email(jsonObject.getString("email"))
                .build();
    }
}
