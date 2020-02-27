package com.szymon.ffproject.web.config;

import com.google.gson.Gson;
import com.szymon.ffproject.database.entity.User;
import com.szymon.ffproject.database.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsProvider implements UserDetailsService {

    @Autowired
    UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = repository.findById(username).orElseGet(()->null);
        System.out.println(new Gson().toJson(repository.findAll()));

        UserBuilder builder;
        if (user != null) {
            builder = org.springframework.security.core.userdetails.User.withUsername(username);
            builder.password(user.getPassword());
            builder.roles(user.getRoles().toArray(new String[0]));
        } else {
            throw new UsernameNotFoundException("User not found.");
        }

        return builder.build();
    }

}
