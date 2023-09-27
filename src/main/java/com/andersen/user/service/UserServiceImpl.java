package com.andersen.user.service;

import com.andersen.user.UserMapper;
import com.andersen.user.dao.User;
import com.andersen.user.dao.UserDao;
import com.andersen.user.dao.UserDaoImpl;
import com.andersen.util.EncryptorUtil;

import java.util.List;
import java.util.stream.Collectors;

public class UserServiceImpl implements UserService {

    private final UserDao<User> userDao = UserDaoImpl.getInstance();
    private static UserService userService;
    private static UserMapper userMapper = UserMapper.getInstance();

    public static UserService getInstance() {
        if (userService == null) {
            userService = new UserServiceImpl();
        }
        return userService;
    }

    @Override
    public List<UserResponseDto> getAll() {
        return userDao.getAll().stream()
                .map(userMapper::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDto getById(Long id) {
        return userMapper.mapToResponseDto(userDao.getById(id));
    }

    @Override
    public UserResponseDto getByEmail(String email) {
        return userMapper.mapToResponseDto(userDao.getByEmail(email));
    }

    @Override
    public UserResponseDto create(UserCreateDto userDto) {
        User user = userDao.create(userMapper.mapToEntity(userDto));
        return userMapper.mapToResponseDto(user);
    }

    @Override
    public boolean validateUserForLogin(String email, String password) {
        User user = userDao.getByEmail(email);
        if (user == null) {
            return false;
        }
        String hashPassword = EncryptorUtil.encrypt(password);
        return user.getPassword().equals(hashPassword);
    }

    @Override
    public UserResponseDto update(UserResponseDto userDto) {
        User user = userDao.update(userMapper.mapToEntity(userDto));
        return userMapper.mapToResponseDto(user);
    }

    @Override
    public void deleteById(Long id) {
        userDao.deleteById(id);
    }
}
