package com.gamelist.main.models.list;

public record CreateCollectionDto(

		String id,
		String name,
		long likes,
		String imageUrl
		) {

}
