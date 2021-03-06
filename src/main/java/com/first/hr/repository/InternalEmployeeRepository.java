package com.first.hr.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import com.first.hr.domain.Employee;

@Component
public interface InternalEmployeeRepository extends
		MongoRepository<Employee, String> {
	List<Employee> findByLastName(@Param("name") String name);

}