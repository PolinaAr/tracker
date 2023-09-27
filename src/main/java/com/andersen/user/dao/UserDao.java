package com.andersen.user.dao;

import java.util.List;

public interface UserDao<E> {

    E getById(Long id);

    List<E> getAll();

    E getByEmail(String email);

    E create(E e);

    E update(E e);

    boolean deleteById(Long id);
}
