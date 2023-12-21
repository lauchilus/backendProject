package com.gamelist.main.igbd;

public record SearchGameDetailsDto(
		Long id,
		String summary,
		String name,
		Long releaseDate,
		String imageUrl,
		String involvedCompany
		
		) {

	
}
