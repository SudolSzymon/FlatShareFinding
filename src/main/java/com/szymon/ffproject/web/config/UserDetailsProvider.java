package com.szymon.ffproject.web.config;

import com.google.gson.Gson;
import com.szymon.ffproject.database.entity.AuthenticationUser;
import com.szymon.ffproject.database.repository.AuthenticationUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserDetailsProvider implements UserDetailsService {

    @Autowired
    AuthenticationUserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        AuthenticationUser user = repository.findFirstAuthenticationUserByName(username);
        System.out.println(new Gson().toJson(repository.findAll()));

        UserBuilder builder;
        if (user != null) {
            builder = org.springframework.security.core.userdetails.User.withUsername(username);
            builder.password(user.getPassword());
            builder.roles(user.getRolesAsArray());
        } else {
            throw new UsernameNotFoundException("User not found.");
        }

        return builder.build();
    }

}
