package com.andersen.telegram.dao;

import com.andersen.exception.DatabaseException;
import com.andersen.exception.EntityNotFoundException;
import com.andersen.user.dao.User;
import com.andersen.util.DBConfigurator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ChatIDDaoImpl implements ChatIDDao{

    private static ChatIDDao chatIDDao;


    //TODO: Write EXISTS statement
    private final String CREATE = "INSERT INTO chat_ids (id) VALUES (?);";
    private final String GET_BY_ID = "SELECT id " +
            "FROM chat_ids WHERE id = ?";
    private final String GET_ALL = "SELECT id FROM chat_ids";
    private final String DELETE = "DELETE FROM chat_ids WHERE id = ?";
    private final Long CHAT_ID_NOT_FOUND = -1L;
    private ChatIDDaoImpl(){

    }


    public static ChatIDDao getInstance() {
        if (chatIDDao == null) {
            chatIDDao = new ChatIDDaoImpl();
        }
        return chatIDDao;
    }

    @Override
    public Long create(Long chatId) {
        try (PreparedStatement statement = DBConfigurator.getConnection().prepareStatement(CREATE)) {
            statement.setLong(1, chatId);
            ResultSet resultSet = statement.executeQuery();
            System.out.println(resultSet);
            if (resultSet.next()) {
                return resultSet.getLong("id");
            } else {
                throw new EntityNotFoundException("There is no chatID = " + chatId);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Can't get chatId with id = " + chatId);
        }
    }

    @Override
    public Long getById(Long chatId) {
        try (PreparedStatement statement = DBConfigurator.getConnection().prepareStatement(GET_BY_ID)) {
            statement.setLong(1, chatId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                long id = resultSet.getLong("id");
                return id;
            }else {
                return CHAT_ID_NOT_FOUND;
            }
        } catch (SQLException e) {
            return CHAT_ID_NOT_FOUND;
        }
    }

    @Override
    public List<Long> getAll() {
        List<Long> ids = new ArrayList<>();
        try (Statement statement = DBConfigurator.getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery(GET_ALL);
            while (resultSet.next()) {
                ids.add(resultSet.getLong("id"));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Can't get all users");
        }
        return ids;
    }

    @Override
    public boolean delete(Long chatId) {
        try (PreparedStatement statement = DBConfigurator.getConnection()
                .prepareStatement(DELETE)) {
            statement.setLong(1, chatId);
            int result = statement.executeUpdate();
            if (result == 1) {
                return true;
            } else {
                throw new EntityNotFoundException("There is no such chatId: " + chatId);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Can't delete chat with id: " + chatId);
        }
    }
}
