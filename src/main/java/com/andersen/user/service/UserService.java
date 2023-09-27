package com.andersen.user.service;

import java.util.List;

public interface UserService {

    List<UserResponseDto> getAll();

    UserResponseDto getById(Long id);

    UserResponseDto getByEmail(String email);

    UserResponseDto create(UserCreateDto user);

    UserResponseDto update(UserResponseDto user);

    boolean validateUserForLogin(String email, String password);

    void deleteById(Long id);
}
