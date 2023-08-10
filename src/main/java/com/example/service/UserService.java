package com.example.service;

import com.example.dto.UserDto;
import com.example.dto.requestDto.AddUserRequestDto;
import com.example.dto.requestDto.UpdateUserRequestDto;

import java.util.List;

public interface UserService {

    UserDto getUserDetailsByUserEmail(String userEmail);
    Boolean addUser(AddUserRequestDto addUserRequestDto);
    Boolean updateUser(UpdateUserRequestDto updateUserRequestDto);
    List<UserDto> getAllUsers();

}
