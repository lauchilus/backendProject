package com.gamelist.main.models.favorites;

public record FavoritesResponseDto(
		long id,
		long gameId,
		String name,
		String gameImageUrl
		) {

}
