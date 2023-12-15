package com.gamelist.main.models.list;

import org.springframework.boot.context.properties.bind.DefaultValue;

public record GameResponseDTO(
		long id,
		String name,
		@DefaultValue("NO IMAGE")
		String storyline,
		String image,
		@DefaultValue("NO follows")
		Integer follows,
		long game_id
		) {

	public GameResponseDTO(GameListData data, String image2,long gameId) {
		this(gameId,data.getName(),data.getStoryline(),image2,data.getFollows(),data.getId());
	}

	

}
