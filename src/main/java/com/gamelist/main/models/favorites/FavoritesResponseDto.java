package com.gamelist.main.models.favorites;

public record FavoritesResponseDto(
		long id,
		long gameId,
		String gameImageUrl,
		String name
		) {

}
