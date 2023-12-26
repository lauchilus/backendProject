package com.gamelist.main.models.played;

import java.time.LocalDate;

public record PlayedResponse(
		long id,
		long gameId,
		LocalDate finish_date,
		String gameImage
		) {

	
}
