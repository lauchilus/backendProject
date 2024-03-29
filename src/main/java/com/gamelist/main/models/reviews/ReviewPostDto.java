package com.gamelist.main.models.reviews;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReviewPostDto(
		@NotBlank
		String review,
		@NotNull
		String userId,
		@NotNull
		float rating,
		@NotNull
		Integer gameId
		) {

}
