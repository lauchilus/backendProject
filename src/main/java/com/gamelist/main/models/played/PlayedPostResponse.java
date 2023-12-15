package com.gamelist.main.models.played;

import java.time.LocalDate;

public record PlayedPostResponse(
		long id,
		long game_id,
		LocalDate finish_date
		) {

}
