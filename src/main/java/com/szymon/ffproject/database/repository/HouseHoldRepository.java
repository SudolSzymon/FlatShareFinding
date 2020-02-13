package com.szymon.ffproject.database.repository;

import com.szymon.ffproject.database.entity.HouseHold;
import com.szymon.ffproject.database.entity.User;
import java.util.List;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface HouseHoldRepository extends CrudRepository<HouseHold, String> {
    List<HouseHold> findAllByNameContaining(String name);
}
