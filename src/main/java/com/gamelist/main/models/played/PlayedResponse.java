package com.gamelist.main.models.played;

import java.time.LocalDate;

public record PlayedResponse(
		String id,
		Long gameId,
		LocalDate finish_date,
		String gameImage
		) {

	
}
