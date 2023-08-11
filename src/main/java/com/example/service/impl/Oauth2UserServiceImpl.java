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

@Service(value = "userService")
@Log4j2
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
@RequiredArgsConstructor
public class Oauth2UserServiceImpl implements Oauth2UserService, UserDetailsService {

    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        try {
            Optional<UserEntity> byUserEmail = userRepository.findByEmail(userEmail);
            if (!byUserEmail.isPresent()) {
                throw new UsernameNotFoundException("User not found with email: " + userEmail);
            }

            UserEntity userEntity = byUserEmail.get();
            String roleString = userEntity.getRole().toString(); // Assuming user type is an Enum

            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_" + roleString));

            return new org.springframework.security.core.userdetails.User(
                    userEntity.getEmail(),
                    userEntity.getPassword(),
                    authorities
            );
        } catch (Exception e) {
            log.error("Error in loadUserByUsername: " + e.getMessage(), e);
            throw new RuntimeException("An error occurred while loading user details.", e);
        }
    }
}

