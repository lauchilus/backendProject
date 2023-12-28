package com.gamelist.main.models.user;

import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.Nullable;

public record UpdateUser(
		@Nullable
		MultipartFile avatar,
		@Nullable
		String bio,
		@Nullable
		String username
		) {

}
