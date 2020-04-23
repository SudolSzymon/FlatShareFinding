package com.szymon.ffproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    public WebSecurityConfig(PasswordEncoder passwordEncoder,
                             UserDetailsService userDetailsService) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .antMatchers("/", "/home").permitAll()
            .antMatchers("/user/create", "/user/register")
            .permitAll()
            .antMatchers("/user/**", "/calendar/**", "/expense/**", "/house/**", "/search/**", "/shop/**")
            .authenticated()
            .anyRequest()
            .denyAll()
            .and()
            .formLogin()
            .loginPage("/user/login")
            .permitAll()
            .and()
            .logout()
            .logoutUrl("/user/logout")
            .logoutSuccessUrl("/home")
            .permitAll()
            .and()
            .exceptionHandling().accessDeniedPage("/403");

    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Bean
    public SpringSecurityDialect secDialect() {
        return new SpringSecurityDialect();
    }


}
