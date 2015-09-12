package com.headspring.application.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	@Autowired
	private Authentication authentication;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authenticationProvider(authentication).httpBasic().and().logout()
				.and().csrf().csrfTokenRepository(csrfTokenRepository()).and()
				.authorizeRequests()
				.antMatchers("/index.html", "/home.html", "/login.html", "/")
				.permitAll().antMatchers("/user").authenticated()
				.antMatchers(HttpMethod.GET, "/employee/**")
				.hasAnyAuthority("USER", "ADMIN")
				.antMatchers(HttpMethod.PUT, "/employee/**")
				.hasAnyAuthority("ADMIN")
				.antMatchers(HttpMethod.POST, "/employee/**")
				.hasAnyAuthority("ADMIN")
				.antMatchers(HttpMethod.DELETE, "/employee/**")
				.hasAnyAuthority("ADMIN").anyRequest().permitAll().and()
				.addFilterAfter(new CsrfHeaderFilter(), CsrfFilter.class);

	}

	private CsrfTokenRepository csrfTokenRepository() {
		HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
		repository.setHeaderName("X-XSRF-TOKEN");
		return repository;
	}
}