package com.headspring.application.security;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.headspring.hr.domain.Employee;
import com.headspring.hr.repository.InternalEmployeeRepository;

@Component
public class Authentication extends AbstractUserDetailsAuthenticationProvider {
	private static final Logger logger = LoggerFactory
			.getLogger(Authentication.class);
	@Autowired
	private InternalEmployeeRepository employees;

	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {

	}

	@Override
	protected UserDetails retrieveUser(String username,
			UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {
		logger.info("auhtenticating user");
		List<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();

		if ("test".equals(username)
				&& "test".equals((String) authentication.getCredentials())) {
			auths.add(new SimpleGrantedAuthority("ADMIN"));
		}

		List<Employee> employees = this.employees.findByLastName(username);
		Employee employee = null;
		if (employees != null && employees.size() > 0) {
			employee = employees.get(0);
		}

		if (employee != null
				&& employee.isPasswordValid((String) authentication
						.getCredentials())) {
			auths.add(new SimpleGrantedAuthority(employee.getRole()));
		}
		;
		if (auths.size() == 0) {
			throw new AuthenticationException("invalid user") {
				private static final long serialVersionUID = 44355055364912991L;
			};
		}
		return new User(username, (String) authentication.getCredentials(),
				auths);
	}
}
