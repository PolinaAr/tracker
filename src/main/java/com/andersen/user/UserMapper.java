package com.andersen.user;

import com.andersen.user.dao.User;
import com.andersen.user.service.UserCreateDto;
import com.andersen.user.service.UserResponseDto;

public class UserMapper {

    private static UserMapper userMapper;

    public static UserMapper getInstance() {
        if (userMapper == null) {
            userMapper = new UserMapper();
        }
        return userMapper;
    }

    public User mapToEntity(UserResponseDto userResponseDto) {
        return User.builder()
                .id(userResponseDto.getId())
                .name(userResponseDto.getName())
                .lastname(userResponseDto.getLastname())
                .email(userResponseDto.getEmail())
                .build();
    }

    public User mapToEntity(UserCreateDto userCreateDto){
        return User.builder()
                .name(userCreateDto.getName())
                .lastname(userCreateDto.getLastname())
                .email(userCreateDto.getEmail())
                .password(userCreateDto.getPassword())
                .build();
    }

    public UserResponseDto mapToResponseDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .build();
    }
}
