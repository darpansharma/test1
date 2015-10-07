package com.first.hr.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import com.first.hr.domain.Employee;

@RepositoryEventHandler
@Component
public class EntityListener {
	private Logger logger = LoggerFactory.getLogger(EntityListener.class);
	@HandleAfterCreate(Employee.class)
	public void Created(Employee employee)
	{
		logger.debug(employee.getIdentityString() + " : created");
	}
}
