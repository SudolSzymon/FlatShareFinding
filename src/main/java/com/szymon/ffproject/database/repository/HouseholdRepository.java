package com.szymon.ffproject.database.repository;

import com.szymon.ffproject.database.entity.Household;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface HouseholdRepository extends CrudRepository<Household, String> {

}
