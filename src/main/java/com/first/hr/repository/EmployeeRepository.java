package com.first.hr.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Component;

import com.first.hr.domain.Employee;
@RepositoryRestResource(collectionResourceRel = "employee", path = "employee")
@Component
public interface EmployeeRepository extends MongoRepository<Employee, String> {
	List<Employee> findByLastNameContaining(@Param("name") String name);

}