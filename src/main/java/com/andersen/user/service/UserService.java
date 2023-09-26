package com.andersen.user.service;

import java.util.List;

public interface UserService {

    List<UserResponseDto> getAll();

    UserResponseDto getById(Long id);

    UserResponseDto create(UserCreateDto user);

    UserResponseDto update(UserResponseDto user);

    void deleteById(Long id);
}
