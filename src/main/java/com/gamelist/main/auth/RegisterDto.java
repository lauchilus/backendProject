package com.gamelist.main.auth;

import jakarta.validation.constraints.NotNull;

public record RegisterDto(
		@NotNull
		String email,
		@NotNull
		String userUID
		) {

}
