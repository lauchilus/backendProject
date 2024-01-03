package com.gamelist.main.models.list;

import com.gamelist.main.igbd.CoverGame;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameListData {

	private long id;
	private String name;
	private String storyline;
	private CoverGame cover;
	private Integer follows;
}
