package com.gamelist.main.models.favorites;

public record FavoritesResponseDto(
		String id,
		long gameId,
		String name,
		String gameImageUrl
		) {

}
