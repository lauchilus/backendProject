package com.gamelist.main.models.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public record UserResponseDto(
		long id,
		String username,
		String bio,
		String imageUrl
		) {

}
