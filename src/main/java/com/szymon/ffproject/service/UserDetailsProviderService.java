package com.szymon.ffproject.service;

import com.szymon.ffproject.dao.UserDAO;
import com.szymon.ffproject.database.entity.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsProviderService implements UserDetailsService {

    final
    UserDAO userDAO;

    public UserDetailsProviderService(UserDAO userDAO) {this.userDAO = userDAO;}


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userDAO.get(username, true);

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
