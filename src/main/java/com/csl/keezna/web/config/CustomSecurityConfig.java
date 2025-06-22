package com.csl.keezna.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class CustomSecurityConfig {

	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		log.info("====================== Security Config ======================");

		return http.build();
	}
}
