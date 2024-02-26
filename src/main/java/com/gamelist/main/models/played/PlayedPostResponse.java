package com.gamelist.main.models.played;

import java.time.LocalDate;

public record PlayedPostResponse(
		String id,
		long gameId,
		LocalDate finish_date
		
		) {

}
