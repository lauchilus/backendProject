package com.gamelist.main.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(requests ->
        requests
        .requestMatchers("/auth/**").permitAll()
        .requestMatchers(HttpMethod.GET).permitAll()
        
            .anyRequest().authenticated())
			.oauth2ResourceServer(oAuth -> oAuth.jwt(Customizer.withDefaults()));
		return http.build();
	}
}
