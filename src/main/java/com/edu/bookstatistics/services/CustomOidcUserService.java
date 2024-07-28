package com.edu.bookstatistics.services;

import com.edu.bookstatistics.entities.User;
import com.edu.bookstatistics.repositiories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomOidcUserService extends OidcUserService {

    private final UserRepository userRepository;

    public CustomOidcUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest oidcUserRequest) {
        OidcUser oidcUser = super.loadUser(oidcUserRequest);

        String googleId = oidcUser.getSubject();
        String name = oidcUser.getFullName();
        String email = oidcUser.getEmail();
        String picture = oidcUser.getPicture();

        Optional<User> optionalUser = userRepository.findByGoogleId(googleId);
        if (optionalUser.isEmpty()) {
            User user = new User();
            user.setGoogleId(googleId);
            user.setName(name);
            user.setEmail(email);
            user.setPicture(picture);
            userRepository.save(user);
        }

        return oidcUser;
    }
}