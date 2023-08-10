package com.example.controller;


import com.example.dto.UserDto;
import com.example.dto.requestDto.AddUserRequestDto;
import com.example.dto.responseDto.CommonResponseDTO;
import com.example.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity registerNewUser(@RequestBody @Valid AddUserRequestDto addUserRequestDto) {
        boolean result = userService.addUser(addUserRequestDto);
        return new ResponseEntity<>(
                new CommonResponseDTO(true, "New user successfully registered..", null), HttpStatus.CREATED
        );
    }

    @GetMapping("/user/{email}")
    public ResponseEntity getUserByEmail(@PathVariable String email) {
        UserDto user = userService.getUserDetailsByUserEmail(email);
        // Return user details if found
        return new ResponseEntity<>(
                new CommonResponseDTO(true, "User found!", user), HttpStatus.OK);

    }
}
