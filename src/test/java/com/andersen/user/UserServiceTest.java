package com.andersen.user;


import com.andersen.exception.DatabaseException;
import com.andersen.exception.EntityNotFoundException;
import com.andersen.user.dao.User;
import com.andersen.user.dao.UserDao;
import com.andersen.user.service.UserCreateDto;
import com.andersen.user.service.UserResponseDto;
import com.andersen.user.service.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserDao userDao;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private UserServiceImpl userService;
    private User mockUser;
    private UserCreateDto userCreateDto;
    private UserResponseDto expectedUserResponseDto;
    private List<User> mockUserList;
    private List<UserResponseDto> mockUserResponseDtoList;
    private Long userId;
    private String userName;
    private String userLastname;
    private String userEmail;
    private String userPassword;

    @BeforeEach
    public void initData() {
        userId = 1L;
        userName = "Anton";
        userLastname = "Developer";
        userEmail = "anton@mail.com";
        userPassword = "12345";
        mockUser = new User(userId, userName, userLastname, userEmail, userPassword);
        userCreateDto = new UserCreateDto(userName, userLastname, userEmail, userPassword);
        expectedUserResponseDto = new UserResponseDto(userId, userName, userLastname, userEmail);
        mockUserList = new ArrayList<>();
        mockUserList.add(mockUser);
        mockUserResponseDtoList = new ArrayList<>();
        mockUserResponseDtoList.add(expectedUserResponseDto);
    }


    @Test
    void testGetAll() {
        when(userDao.getAll()).thenReturn(mockUserList);
        when(userMapper.mapToResponseDto(mockUser)).thenReturn(expectedUserResponseDto);

        List<UserResponseDto> actualUserResponseDtoList = userService.getAll();

        assertNotNull(actualUserResponseDtoList);
        assertEquals(1, actualUserResponseDtoList.size());
        assertEquals(expectedUserResponseDto, actualUserResponseDtoList.get(0));
        verify(userDao).getAll();
        verify(userMapper).mapToResponseDto(mockUser);
    }

    @Test
    void testGetAllReturnEmptyList() {
        when(userDao.getAll()).thenReturn(Collections.emptyList());

        List<UserResponseDto> actualUserResponseDtoList = userService.getAll();

        assertNotNull(actualUserResponseDtoList);
        assertTrue(actualUserResponseDtoList.isEmpty());
        verify(userDao).getAll();
        verifyNoInteractions(userMapper);
    }

    @Test
    void testGetAllThrowsDatabaseException() {
        when(userDao.getAll()).thenThrow(DatabaseException.class);

        assertThrows(DatabaseException.class, () -> userService.getAll());
        verify(userDao).getAll();
    }

    @Test
    void testGetById() {
        when(userDao.getById(userId)).thenReturn(mockUser);
        when(userMapper.mapToResponseDto(mockUser)).thenReturn(expectedUserResponseDto);

        UserResponseDto actualUserResponseDto = userService.getById(userId);

        assertNotNull(actualUserResponseDto);
        assertEquals(expectedUserResponseDto.getId(), actualUserResponseDto.getId());
        verify(userDao).getById(userId);
        verify(userMapper).mapToResponseDto(mockUser);
    }

    @Test
    void testGetByIdWithIncorrectId() {
        Long userWithIncorrectId = anyLong();
        when(userDao.getById(userWithIncorrectId)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> userService.getById(userWithIncorrectId));
        verify(userDao).getById(userWithIncorrectId);
        verifyNoInteractions(userMapper);
    }

    @Test
    void testGetByIdReturnNull() {
        Long userWithIncorrectId = anyLong();
        when(userDao.getById(userWithIncorrectId)).thenReturn(null);

        UserResponseDto actualUserResponseDto = userService.getById(userWithIncorrectId);

        assertNull(actualUserResponseDto);
        verify(userDao).getById(userWithIncorrectId);
        verify(userMapper).mapToResponseDto(isNull());
    }

    @Test
    void testGetByEmail() {
        when(userDao.getByEmail(userEmail)).thenReturn(mockUser);
        when(userMapper.mapToResponseDto(mockUser)).thenReturn(expectedUserResponseDto);

        UserResponseDto actualUserResponseDto = userService.getByEmail(userEmail);

        assertNotNull(actualUserResponseDto);
        assertEquals(userEmail, actualUserResponseDto.getEmail());
        verify(userDao).getByEmail(userEmail);
        verify(userMapper).mapToResponseDto(mockUser);
    }

    @Test
    void testGetByNotExistingEmail() {
        String notExistingEmail = "notexisting@mail.com";
        when(userDao.getByEmail(notExistingEmail)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> userService.getByEmail(notExistingEmail));
        verify(userDao).getByEmail(notExistingEmail);
        verifyNoInteractions(userMapper);
    }

    @Test
    void testCreate() {
        when(userDao.create(any(User.class))).thenReturn(mockUser);
        when(userMapper.mapToEntity(userCreateDto)).thenReturn(mockUser);
        when(userMapper.mapToResponseDto(mockUser)).thenReturn(expectedUserResponseDto);

        UserResponseDto actualUserResponseDto = userService.create(userCreateDto);

        assertNotNull(actualUserResponseDto);
        assertEquals(userCreateDto.getName(), actualUserResponseDto.getName());
        assertEquals(userCreateDto.getLastname(), actualUserResponseDto.getLastname());
        assertEquals(userCreateDto.getEmail(), actualUserResponseDto.getEmail());
        verify(userDao).create(any(User.class));
        verify(userMapper).mapToEntity(userCreateDto);
        verify(userMapper).mapToResponseDto(mockUser);
    }

    @Test
    void testCreateUserWithInvalidData() {
        UserCreateDto invalidUserDto = new UserCreateDto(null, null, null, null);
        when(userDao.create(userMapper.mapToEntity(invalidUserDto))).thenReturn(null);

        UserResponseDto actualUserResponseDto = userService.create(invalidUserDto);

        assertNull(actualUserResponseDto);
        verify(userDao).create(userMapper.mapToEntity(invalidUserDto));
        verify(userMapper).mapToResponseDto(isNull());
    }

    @Test
    void testUpdate() {
        when(userDao.update(any(User.class))).thenReturn(mockUser);
        when(userMapper.mapToEntity(expectedUserResponseDto)).thenReturn(mockUser);
        when(userMapper.mapToResponseDto(mockUser)).thenReturn(expectedUserResponseDto);

        UserResponseDto actualUserResponseDto = userService.update(expectedUserResponseDto);

        assertNotNull(actualUserResponseDto);
        assertEquals(mockUser.getId(), actualUserResponseDto.getId());
        assertEquals(expectedUserResponseDto.getName(), actualUserResponseDto.getName());
        assertEquals(expectedUserResponseDto.getLastname(), actualUserResponseDto.getLastname());
        assertEquals(expectedUserResponseDto.getEmail(), actualUserResponseDto.getEmail());
        verify(userDao).update(any(User.class));
        verify(userMapper).mapToEntity(expectedUserResponseDto);
        verify(userMapper).mapToResponseDto(mockUser);
    }

    @Test
    void testUpdateUserWithInvalidData() {
        UserResponseDto invalidUserDto = new UserResponseDto(null, null, null, null);
        when(userDao.update(userMapper.mapToEntity(invalidUserDto))).thenReturn(null);

        UserResponseDto actualUserResponseDto = userService.update(invalidUserDto);

        assertNull(actualUserResponseDto);
        verify(userDao).update(userMapper.mapToEntity(invalidUserDto));
        verify(userMapper).mapToResponseDto(isNull());
    }

    @Test
    void testDeleteById() {
        Long userToDelete = anyLong();
        when(userDao.deleteById(userToDelete)).thenReturn(true);

        boolean result = userService.deleteById(userToDelete);

        assertTrue(result);
        verify(userDao).deleteById(userToDelete);
    }

    @Test
    void testDeleteByIdUserNotFound() {
        Long notExistingUserId = anyLong();
        when(userDao.deleteById(notExistingUserId)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> userService.deleteById(notExistingUserId));
        verify(userDao).deleteById(notExistingUserId);
    }

    @Test
    void testValidateUserForLoginWithInvalidPassword() {
        String wrongPassword = "wrongpassword";
        when(userDao.getByEmail(userEmail)).thenReturn(mockUser);

        boolean result = userService.validateUserForLogin(userEmail, wrongPassword);

        assertFalse(result);
        assertNotEquals(userPassword, wrongPassword);
        verify(userDao).getByEmail(userEmail);
    }

    @Test
    void testValidateUserForLoginWithInvalidEmail() {
        String notExistingEmail = "notexisting@mail.com";
        when(userDao.getByEmail(notExistingEmail)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> userService.getByEmail(notExistingEmail));
        verify(userDao).getByEmail(notExistingEmail);
    }

    @Test
    void testValidateUserForLoginWithInvalidData() {
        String nullEmail = null;
        String nullPassword = null;

        boolean result = userService.validateUserForLogin(nullEmail, nullPassword);

        assertFalse(result);
        verify(userDao).getByEmail(nullEmail);
    }

    @AfterEach
    public void clearData() {
        userId = null;
        userName = null;
        userLastname = null;
        userEmail = null;
        userPassword = null;
        mockUser = null;
        userCreateDto = null;
        expectedUserResponseDto = null;
        mockUserList = null;
        mockUserResponseDtoList = null;
    }
}