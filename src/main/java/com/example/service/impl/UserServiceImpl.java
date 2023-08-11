package com.example.service.impl;

import com.example.constants.ApplicationConstants;
import com.example.dto.StudentDto;
import com.example.dto.UserDto;
import com.example.dto.requestDto.AddUserRequestDto;
import com.example.dto.requestDto.UpdateUserRequestDto;
import com.example.entity.StudentEntity;
import com.example.entity.UserEntity;
import com.example.exception.UserException;
import com.example.repository.UserRepository;
import com.example.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto getUserDetailsByUserEmail(String userEmail) {
        try {

            Optional<UserEntity> byUserEmail = userRepository.findByEmail(userEmail);

            if (!byUserEmail.isPresent()){
                throw new UserException(ApplicationConstants.RESOURCE_NOT_FOUND, "User email not found!");
            }
            else {
                return new UserDto(
                        byUserEmail.get().getId(),
                        byUserEmail.get().getFirstname(),
                        byUserEmail.get().getLastname(),
                        byUserEmail.get().getEmail(),
                        byUserEmail.get().getRole()
                );
            }

        }catch (Exception e){
            log.error("Method getUserDetailsByUserEmail : "+ e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public Boolean addUser(AddUserRequestDto addUserRequestDto) {
        try {

            Optional<UserEntity> byUserEmail = userRepository.findByEmail(addUserRequestDto.getEmail());

            // check if user with same email already exists
            if (byUserEmail.isPresent()){
                throw new UserException(ApplicationConstants.RESOURCE_ALREADY_EXIST, "Email already exist");
            }
            else {
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

        }catch (Exception e){
            log.error("Method addUser : "+ e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public Boolean updateUser(UpdateUserRequestDto updateUserRequestDto) {
        // TODO: 11-Aug-23  
        return null;
    }

    // get all users
    @Override
    public List<UserDto> getAllUsers() {

        log.info("Execute method getAllUsers : ");

        List<UserEntity> allUsers = userRepository.findAll();
        List<UserDto> allUsersToBeGet = new ArrayList<>();

        for (UserEntity user: allUsers){
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
