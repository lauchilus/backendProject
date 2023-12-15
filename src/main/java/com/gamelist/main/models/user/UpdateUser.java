package com.gamelist.main.models.user;

import org.springframework.web.multipart.MultipartFile;

public record UpdateUser(
		MultipartFile avatar,
		String bio
		) {

}
