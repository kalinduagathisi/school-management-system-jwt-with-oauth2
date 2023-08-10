package com.example.config;

import com.example.constants.OAuth2Constant;
import com.example.dto.UserDto;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Component
public class CustomTokenEnhancer extends JwtAccessTokenConverter {

    private final UserService userService;

    @Autowired
    public CustomTokenEnhancer(UserService userService) {
        this.userService = userService;
    }


    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) {
        final Map<String, Object> additionalInfo = new HashMap<>();

        User user = (User) oAuth2Authentication.getPrincipal();

        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        if (user.getUsername().equals(OAuth2Constant.ADMIN_CLIENT_ID)) {
            UserDto userDetailsByUserEmail = userService.getUserDetailsByUserEmail(user.getUsername());
            additionalInfo.put("user", userDetailsByUserEmail);
            additionalInfo.put("user_id", userDetailsByUserEmail.getId());
        }

        // set custom claims
        ((DefaultOAuth2AccessToken) oAuth2AccessToken).setAdditionalInformation(additionalInfo);

        return super.enhance(oAuth2AccessToken, oAuth2Authentication);
    }

}
