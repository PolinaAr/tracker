package com.andersen.track;

import com.andersen.exception.DatabaseException;
import com.andersen.exception.EntityNotFoundException;
import com.andersen.track.service.TrackCreateDto;
import com.andersen.track.service.TrackResponseDto;
import com.andersen.track.service.TrackService;
import com.andersen.track.service.TrackServiceImpl;
import com.andersen.user.service.UserResponseDto;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.stream.Collectors;

@WebServlet("/tracks")
public class TrackController extends HttpServlet {

    public final TrackService trackService = TrackServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        if (session.getAttribute("user") != null) {
            UserResponseDto authUser = (UserResponseDto) session.getAttribute("user");
            JSONObject body = readBody(req);
            PrintWriter out = resp.getWriter();
            resp.setContentType("application/json");
            try {
                TrackCreateDto trackCreateDto = getTrackCreateDto(body);
                trackCreateDto.setUserId(authUser.getId());
                TrackResponseDto createdTrack = trackService.create(trackCreateDto);

                JSONObject trackJson = new JSONObject(createdTrack);
                out.print(trackJson);
            } catch (DatabaseException ex) {
                JSONObject exception = new JSONObject();
                exception.put("message", ex.getMessage());
                out.print(exception);
            }
            out.flush();
        } else {
            resp.setStatus(403);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = Long.parseLong(req.getParameter("id"));
        try {
            trackService.deleteById(id);
        } catch (EntityNotFoundException | DatabaseException ex) {
            JSONObject userJson = new JSONObject("message", ex.getMessage());
            PrintWriter out = resp.getWriter();
            resp.setContentType("application/json");
            resp.setStatus(400);
            out.print(userJson);
            out.flush();
        }
    }

    private static JSONObject readBody(HttpServletRequest req) throws IOException {
        String body = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        return new JSONObject(body);
    }

    private TrackCreateDto getTrackCreateDto(JSONObject jsonObject) {
        return TrackCreateDto.builder()
                .time(Double.parseDouble(jsonObject.getString("time")))
                .note(jsonObject.getString("note"))
                .date(LocalDate.parse(jsonObject.getString("date")))
                .build();
    }
}
