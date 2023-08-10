package com.example.service.impl;

import com.example.constants.OAuth2Constant;
import com.example.entity.UserEntity;
import com.example.repository.UserRepository;
import com.example.service.Oauth2UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Service(value = "userService")
@Log4j2
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
@RequiredArgsConstructor
public class Oauth2UserServiceImpl implements Oauth2UserService, UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {

        /*loadUserByUsername method users by username. So if you don't specify an email as a username, you need to pass the param as username in postman. */

        log.info("Execute loadUserByUsername: s: " + userEmail);
        try {
            UsernamePasswordAuthenticationToken authentication =
                    (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            User user = (User) authentication.getPrincipal();
            String clientId = user.getUsername();

            if(clientId.equals(OAuth2Constant.ADMIN_CLIENT_ID)) {
                Optional<UserEntity> byUserEmail = userRepository.findByEmail(userEmail);
                if(!byUserEmail.isPresent()) throw new RuntimeException();
                return new org.springframework.security.core.userdetails.
                        User(byUserEmail.get().getEmail(), byUserEmail.get().getPassword(),
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));
            }

            return null;
        } catch (Exception e) {
            log.error("Execute loadUserByUsername: " + e.getMessage());
            throw e;
        }
    }
}
