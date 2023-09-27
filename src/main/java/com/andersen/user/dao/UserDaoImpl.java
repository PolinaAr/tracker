package com.andersen.user.dao;

import com.andersen.exception.DatabaseException;
import com.andersen.exception.EntityNotFoundException;
import com.andersen.util.DBConfigurator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao<User> {

    private static UserDao userDao;

    private final String GET_BY_ID = "SELECT u.id, u.name, u.lastname, u.email, u.password " +
            "FROM users u WHERE id = ?";
    private final String GET_ALL = "SELECT u.id, u.name, u.lastname, u.email, u.password FROM users u";
    private final String GET_BY_EMAIL = "SELECT u.id, u.name, u.lastname, u.email, u.password " +
            "FROM users u WHERE email = ?";
    private final String CREATE = "INSERT INTO users (name, lastname, email, password) VALUES (?, ?, ?, ?);";
    private final String UPDATE = "UPDATE users SET name = ?, lastname = ?, email = ? WHERE id= ?";
    private final String DELETE = "DELETE FROM users WHERE id = ?";

    public static UserDao getInstance() {
        if (userDao == null) {
            userDao = new UserDaoImpl();
        }
        return userDao;
    }

    @Override
    public User getById(Long id) {
        try (PreparedStatement statement = DBConfigurator.getConnection().prepareStatement(GET_BY_ID)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return getUserFromResultSet(resultSet);
            } else {
                throw new EntityNotFoundException("There is no user with id: " + id);
            }
        } catch (SQLException e) {
            throw new EntityNotFoundException("Can't get user with id: " + id);
        }
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        try (Statement statement = DBConfigurator.getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery(GET_ALL);
            while (resultSet.next()) {
                users.add(getUserFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Can't get all users");
        }
        return users;
    }

    @Override
    public User getByEmail(String email) {
        try (PreparedStatement statement = DBConfigurator.getConnection().prepareStatement(GET_BY_EMAIL)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return getUserFromResultSet(resultSet);
            } else {
                throw new EntityNotFoundException("There is no user with email: " + email);
            }
        } catch (SQLException e) {
            throw new EntityNotFoundException("Can't get user with email: " + email);
        }
    }

    @Override
    public User create(User user) {
        try (PreparedStatement statement = DBConfigurator.getConnection()
                .prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS)) {
            setStatement(user, statement);
            statement.setString(4, user.getPassword());
            int result = statement.executeUpdate();
            if (result != 1) {
                throw new DatabaseException("Can't create user");
            }
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return getById(generatedKeys.getLong("id"));
            } else {
                throw new DatabaseException("Can't create user");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Can't create user");
        }
    }

    @Override
    public User update(User user) {
        try (PreparedStatement statement = DBConfigurator.getConnection()
                .prepareStatement(UPDATE)) {
            setStatement(user, statement);
            statement.setLong(4, user.getId());
            int result = statement.executeUpdate();
            if (result != 1) {
                throw new DatabaseException("Can't update user with id: " + user.getId());
            }
            return getById(user.getId());
        } catch (SQLException e) {
            throw new DatabaseException("Can't update user with id: " + user.getId());
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
                throw new EntityNotFoundException("There is no user with id: " + id);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Can't delete user with id: " + id);
        }
    }

    private User getUserFromResultSet(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("id"));
        user.setName(resultSet.getString("name"));
        user.setLastname(resultSet.getString("lastname"));
        user.setEmail(resultSet.getString("email"));
        if (resultSet.getString("password") != null) {
            user.setPassword(resultSet.getString("password"));
        }
        return user;
    }

    private static void setStatement(User user, PreparedStatement statement) throws SQLException {
        statement.setString(1, user.getName());
        statement.setString(2, user.getLastname());
        statement.setString(3, user.getEmail());
    }
}
