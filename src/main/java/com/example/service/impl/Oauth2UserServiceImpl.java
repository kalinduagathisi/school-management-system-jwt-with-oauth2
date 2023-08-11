package com.example.service.impl;

import com.example.entity.UserEntity;
import com.example.repository.UserRepository;
import com.example.service.Oauth2UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

// Declare the class as a service
@Service(value = "userService")
@Log4j2
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
@RequiredArgsConstructor
public class Oauth2UserServiceImpl implements Oauth2UserService, UserDetailsService {

    // Inject UserRepository dependency
    private final UserRepository userRepository;

    // Implement the UserDetailsService interface
    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        try {
            // Retrieve the user entity by email
            Optional<UserEntity> byUserEmail = userRepository.findByEmail(userEmail);
            if (!byUserEmail.isPresent()) {
                throw new UsernameNotFoundException("User not found with email: " + userEmail);
            }

            // Get the user entity
            UserEntity userEntity = byUserEmail.get();
            String roleString = userEntity.getRole().toString(); // Assuming user type is an Enum

            // Create authorities for the user
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_" + roleString));

            // Create and return UserDetails
            return new org.springframework.security.core.userdetails.User(
                    userEntity.getEmail(),
                    userEntity.getPassword(),
                    authorities
            );
        } catch (Exception e) {
            // Log and handle any exceptions
            log.error("Error in loadUserByUsername: " + e.getMessage(), e);
            throw new RuntimeException("An error occurred while loading user details.", e);
        }
    }
}
