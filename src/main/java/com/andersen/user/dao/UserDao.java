package com.andersen.user.dao;

import java.util.List;

public interface UserDao {

    User getById(Long id);

    List<User> getAll();

    User getByEmail(String email);

    User create(User e);

    User update(User e);

    boolean deleteById(Long id);
}
