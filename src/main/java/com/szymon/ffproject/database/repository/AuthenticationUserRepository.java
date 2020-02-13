package com.szymon.ffproject.database.repository;

import com.szymon.ffproject.database.entity.AuthenticationUser;
import java.util.List;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface AuthenticationUserRepository extends CrudRepository<AuthenticationUser, String> {
    AuthenticationUser findFirstAuthenticationUserByName(String name);
}
