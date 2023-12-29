package com.gamelist.main.backlog;

import java.util.List;

import com.gamelist.main.igbd.SearchGameListDto;

import lombok.Builder;

@Builder
public record BacklogUserResponseDto(
		long idBacklog,
		long id,
		String name,
		String imageUrl
		) {

}
