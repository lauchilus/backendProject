package com.gamelist.main.models.reviews;

import java.time.LocalDate;
import java.util.List;

import com.gamelist.main.models.game.Game;

public record ReviewResponseDto(
		LocalDate date,
		String review,
		float rating,
		String name,
		String gameImage,
		long gameId
		) {

	

}
