package com.headspring.application.resource;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Resources {
	private static final Logger logger = LoggerFactory.getLogger(Resources.class);
	public Resources() {
		logger.info("resources initialized");
	}

	@RequestMapping("/user")
	public Principal user(Principal user) {
		return user;
	}
}
