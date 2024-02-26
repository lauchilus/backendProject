package com.gamelist.main.models.list;

import java.util.List;

import com.gamelist.main.igbd.GameData;

public record GetCollectionDto(
		String id,
		String name,
		String imageUrl,
		long likes
		) {

}
