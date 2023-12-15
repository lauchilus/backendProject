package com.gamelist.main.models.list;

public record CreateCollectionDto(
		String name,
		long likes,
		String imageUrl
		) {

}
