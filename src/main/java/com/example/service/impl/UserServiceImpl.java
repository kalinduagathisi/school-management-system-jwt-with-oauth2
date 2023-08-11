package com.example.service.impl;

import com.example.constants.ApplicationConstants;
import com.example.dto.UserDto;
import com.example.dto.requestDto.AddUserRequestDto;
import com.example.dto.requestDto.UpdateUserRequestDto;
import com.example.entity.UserEntity;
import com.example.exception.StudentException;
import com.example.exception.UserException;
import com.example.repository.UserRepository;
import com.example.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// Declare the class as a service
@Service
// Enable logging for the class
@Log4j2
// Enable transaction management
@Transactional
// Use Lombok to generate constructor injection
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    // Inject UserRepository dependency
    private final UserRepository userRepository;
    // Inject PasswordEncoder dependency
    private final PasswordEncoder passwordEncoder;

    // Implement the method to get user details by email
    @Override
    public UserDto getUserDetailsByUserEmail(String userEmail) {
        try {
            // Check if a user with the given email exists
            Optional<UserEntity> byUserEmail = userRepository.findByEmail(userEmail);
            if (!byUserEmail.isPresent()) {
                throw new UserException(ApplicationConstants.RESOURCE_NOT_FOUND, "User email not found.");
            } else {
                // Create and return UserDto
                return new UserDto(
                        byUserEmail.get().getId(),
                        byUserEmail.get().getFirstname(),
                        byUserEmail.get().getLastname(),
                        byUserEmail.get().getEmail(),
                        byUserEmail.get().getRole()
                );
            }
        } catch (Exception e) {
            // Log and handle any exceptions
            log.error("Method getUserDetailsByUserEmail : " + e.getMessage(), e);
            throw e;
        }
    }

    // Implement the method to add a new user
    @Override
    public Boolean addUser(AddUserRequestDto addUserRequestDto) {
        try {
            // Check if a user with the same email already exists
            Optional<UserEntity> byUserEmail = userRepository.findByEmail(addUserRequestDto.getEmail());
            if (byUserEmail.isPresent()) {
                throw new UserException(ApplicationConstants.RESOURCE_ALREADY_EXIST, "Email already exists.");
            } else {
                // Create and save a new UserEntity
                userRepository.save(
                        new UserEntity(
                                addUserRequestDto.getFirstname(),
                                addUserRequestDto.getLastname(),
                                addUserRequestDto.getEmail(),
                                passwordEncoder.encode(addUserRequestDto.getPassword()),
                                addUserRequestDto.getRole()
                        )
                );
                return true;
            }
        } catch (Exception e) {
            // Log and handle any exceptions
            log.error("Method addUser : " + e.getMessage(), e);
            throw e;
        }
    }

    // Implement the method to update user details
    @Override
    public Boolean updateUser(String email, UpdateUserRequestDto updateUserRequestDto) {
        try {
            // Check if a user with the given email exists
            Optional<UserEntity> byUserEmail = userRepository.findByEmail(email);
            Optional<UserEntity> byUserId = userRepository.findById(updateUserRequestDto.getId());

            if (!byUserEmail.isPresent()) {
                throw new UserException(ApplicationConstants.RESOURCE_NOT_FOUND, "User with given email not found.");
            }

            UserEntity userEntity = byUserEmail.get();

            // Check whether same user email
            if (byUserId.isPresent()) {
                if (userEntity.getId() != byUserId.get().getId()) {
                    throw new StudentException(ApplicationConstants.RESOURCE_ALREADY_EXIST, "User with given email already exists.");
                }
            }

            // Update user details
            userEntity.setFirstname(updateUserRequestDto.getFirstname());
            userEntity.setLastname(updateUserRequestDto.getLastname());
            userEntity.setEmail(updateUserRequestDto.getEmail());
            userEntity.setRole(updateUserRequestDto.getRole());

            return true;

        } catch (Exception e) {
            // Log and handle any exceptions
            log.error("Method updateUser : " + e.getMessage(), e);
            throw e;
        }
    }

    // Implement the method to get details of all users
    @Override
    public List<UserDto> getAllUsers() {
        log.info("Execute method getAllUsers : ");

        // Retrieve all user entities
        List<UserEntity> allUsers = userRepository.findAll();
        List<UserDto> allUsersToBeGet = new ArrayList<>();

        // Create UserDto for each user entity
        for (UserEntity user : allUsers) {
            allUsersToBeGet.add(
                    new UserDto(
                            user.getId(),
                            user.getFirstname(),
                            user.getLastname(),
                            user.getEmail(),
                            user.getRole()
                    )
            );
        }
        return allUsersToBeGet;
    }
}
