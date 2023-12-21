package com.gamelist.main.backlog;

import java.util.List;

import com.gamelist.main.igbd.SearchGameListDto;

public record BacklogUserResponseDto(
		List<SearchGameListDto> games
		) {

}
