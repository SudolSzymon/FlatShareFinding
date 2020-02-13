package com.szymon.ffproject.server;

import com.szymon.ffproject.database.entity.AuthenticationUser;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

public class CreateUserData {



    private String user;
    private String pass;

    public CreateUserData(PasswordEncoder passwordEncoder) {}


    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public AuthenticationUser buildAuthUser(PasswordEncoder passwordEncoder) {
        return new AuthenticationUser().setName(user).setPassword(passwordEncoder.encode(pass));
    }
}
