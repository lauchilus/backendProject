package com.gamelist.main.igbd;

import java.time.LocalDate;

public record SearchGameDetailsDto(
		Long id,
		String summary,
		String name,
		LocalDate releaseDate,
		String imageUrl,
		String involvedCompany
		
		) {

	
}
