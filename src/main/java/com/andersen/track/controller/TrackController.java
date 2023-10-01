package com.andersen.track.controller;

import com.andersen.exception.DatabaseException;
import com.andersen.exception.EntityNotFoundException;
import com.andersen.track.service.TrackCreateDto;
import com.andersen.track.service.TrackResponseDto;
import com.andersen.track.service.TrackService;
import com.andersen.track.service.TrackServiceImpl;
import com.andersen.user.service.UserResponseDto;
import org.json.JSONArray;
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
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@WebServlet("/tracks")
public class TrackController extends HttpServlet {

    public final TrackService trackService = TrackServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        if (session.getAttribute("user") != null) {
            PrintWriter out = resp.getWriter();
            resp.setContentType("application/json");

            UserResponseDto userResponseDto = (UserResponseDto) session.getAttribute("user");
            Long userId = userResponseDto.getId();
            try {
                if (req.getParameter("id") == null) {
                    List<TrackResponseDto> tracks = trackService.getByUserId(userId);
                    JSONArray tracksJson = new JSONArray(tracks);
                    out.print(tracksJson);
                } else {
                    Long id = Long.valueOf(req.getParameter("id"));
                    if (isCorrectUser(session, id)) {
                        TrackResponseDto responseDto = trackService.getById(id);
                        JSONObject responseJson = new JSONObject(responseDto);
                        out.print(responseJson);
                    } else {
                        resp.setStatus(403);
                    }
                }
            } catch (DatabaseException | EntityNotFoundException ex) {
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
        HttpSession session = req.getSession();
        Long id = Long.parseLong(req.getParameter("id"));

        if (session.getAttribute("user") != null && isCorrectUser(session, id)) {
            UserResponseDto authUser = (UserResponseDto) session.getAttribute("user");
            JSONObject body = readBody(req);
            PrintWriter out = resp.getWriter();
            resp.setContentType("application/json");
            try {
                TrackResponseDto forUpdate = getTrackResponseDto(body);
                forUpdate.setUserId(authUser.getId());
                forUpdate.setId(id);
                TrackResponseDto updated = trackService.update(forUpdate);

                JSONObject updatedJson = new JSONObject(updated);
                out.print(updatedJson);
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
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Long id = Long.parseLong(req.getParameter("id"));

        if (session.getAttribute("user") != null && isCorrectUser(session, id)) {
            try {
                trackService.deleteById(id);
            } catch (EntityNotFoundException | DatabaseException ex) {
                JSONObject exception = new JSONObject("message", ex.getMessage());
                PrintWriter out = resp.getWriter();
                resp.setContentType("application/json");
                resp.setStatus(400);
                out.print(exception);
                out.flush();
            }
        } else {
            resp.setStatus(403);
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

    private TrackResponseDto getTrackResponseDto(JSONObject jsonObject) {
        return TrackResponseDto.builder()
                .time(Double.parseDouble(jsonObject.getString("time")))
                .note(jsonObject.getString("note"))
                .date(LocalDate.parse(jsonObject.getString("date")))
                .build();
    }

    private boolean isCorrectUser(HttpSession session, Long trackId) {
        try {
            UserResponseDto userResponseDto = (UserResponseDto) session.getAttribute("user");
            TrackResponseDto trackResponseDto = trackService.getById(trackId);
            return Objects.equals(userResponseDto.getId(), trackResponseDto.getUserId());
        } catch (EntityNotFoundException | DatabaseException ex) {
            return false;
        }
    }
}
