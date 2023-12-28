package com.gamelist.main.models.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.firebase.database.annotations.Nullable;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public record UserResponseDto(
		String id,
		String username,
		String bio,
		@Nullable
		String imageUrl
		) {

}
