package com.andersen.track.dao;

import com.andersen.exception.DatabaseException;
import com.andersen.exception.EntityNotFoundException;
import com.andersen.util.DBConfigurator;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TrackDaoImpl implements TrackDao {

    private static TrackDao trackDao;

    private final String GET_BY_USER_ID = "SELECT t.id, t.time, t.note, t.date, t.user_id FROM tracks t WHERE user_id = ?";
    private final String GET_BY_ID = "SELECT t.id, t.time, t.note, t.date, t.user_id FROM tracks t WHERE id = ?";
    private final String GET_BY_DATE = "SELECT t.id, t.time, t.note, t.user_id FROM tracks WHERE date = ?";
    private final String CREATE = "INSERT INTO tracks (time, note, date, user_id) VALUES (?, ?, ?, ?);";
    private final String UPDATE = "UPDATE tracks SET time = ?, note = ?, date = ? WHERE id= ?";
    private final String DELETE = "DELETE FROM tracks WHERE id = ?";

    public static TrackDao getInstance() {
        if (trackDao == null) {
            trackDao = new TrackDaoImpl();
        }
        return trackDao;
    }

    @Override
    public List<Track> getByUserId(Long userId) {
        List<Track> tracks = new ArrayList<>();
        try (PreparedStatement statement = DBConfigurator.getConnection().prepareStatement(GET_BY_USER_ID)) {
            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                tracks.add(getTrackFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Can't get report for user with id " + userId);
        }
        return tracks;
    }

    private Track getTrackFromResultSet(ResultSet resultSet) throws SQLException {
        return Track.builder()
                .id(resultSet.getLong("id"))
                .time(resultSet.getDouble("time"))
                .note(resultSet.getString("note"))
                .date(resultSet.getDate("date").toLocalDate())
                .userId(resultSet.getLong("user_id")).build();
    }

    @Override
    public Track getById(Long id) {
        try (PreparedStatement statement = DBConfigurator.getConnection().prepareStatement(GET_BY_ID)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return getTrackFromResultSet(resultSet);
            } else {
                throw new EntityNotFoundException("There is no note with id = " + id);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Can't get note with id = " + id);
        }
    }

    @Override
    public List<Track> getByDate(LocalDate localDate) {
        List<Track> tracks = new ArrayList<>();
        try (PreparedStatement statement = DBConfigurator.getConnection().prepareStatement(GET_BY_DATE)){
            statement.setDate(1, Date.valueOf(localDate));
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                tracks.add(getTrackFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Can't get notes with date: " + localDate);
        }
        return tracks;
    }

    @Override
    public Track create(Track track) {
        try (PreparedStatement statement = DBConfigurator.getConnection()
                .prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS)) {
            setStatement(track, statement);
            statement.setLong(4, track.getUserId());
            int result = statement.executeUpdate();
            if (result != 1) {
                throw new DatabaseException("Can't create track");
            }
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return getById(generatedKeys.getLong("id"));
            } else {
                throw new DatabaseException("Can't create track");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Can't create track");
        }
    }

    private static void setStatement(Track track, PreparedStatement statement) throws SQLException {
        statement.setDouble(1, track.getTime());
        statement.setString(2, track.getNote());
        statement.setDate(3, Date.valueOf(track.getDate()));
    }

    @Override
    public Track update(Track track) {
        try (PreparedStatement statement = DBConfigurator.getConnection().prepareStatement(UPDATE)){
            setStatement(track, statement);
            statement.setLong(4, track.getId());
            int result = statement.executeUpdate();
            if (result != 1) {
                throw new DatabaseException("Can't update track with id: " + track.getId());
            }
            return getById(track.getId());
        } catch (SQLException e) {
            throw new DatabaseException("Can't update track with id: " + track.getId());
        }
    }

    @Override
    public boolean deleteById(Long id) {
        try (PreparedStatement statement = DBConfigurator.getConnection()
                .prepareStatement(DELETE)) {
            statement.setLong(1, id);
            int result = statement.executeUpdate();
            if (result == 1) {
                return true;
            } else {
                throw new EntityNotFoundException("There is no track with id: " + id);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Can't delete track with id: " + id);
        }
    }
}
