package com.gamelist.main.models.list;

import com.gamelist.main.igbd.CoverGame;

import lombok.Data;

@Data
public class GameListData {

	private long id;
	private String name;
	private String storyline;
	private CoverGame cover;
	private Integer follows;
}
